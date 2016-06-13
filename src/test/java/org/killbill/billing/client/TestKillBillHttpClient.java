/*
 * Copyright 2010-2013 Ning, Inc.
 * Copyright 2016 Groupon, Inc
 * Copyright 2016 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.client;

import java.util.UUID;

import org.killbill.billing.client.model.PaymentTransaction;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ning.http.client.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.collect.ImmutableMultimap;

import static org.testng.Assert.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by arodrigues on 5/28/16.
 */
@Test(groups = "fast")
public class TestKillBillHttpClient {

    private WireMockServer server;

    private KillBillHttpClient httpClient;
    private final String apiKey = "bob";
    private final String apiSecret = "lazar";

    @BeforeClass
    public void startServer() {
        server = new WireMockServer();
        server.start();
    }

    @AfterClass
    public void stopServer() {
        server.stop();
    }
    private String serverBaseUrl() {
        return String.format("http://localhost:%d", server.port());
    }

    @BeforeMethod
    public void setUp() throws Exception {
        final String user = "admin";
        final String password = "password";
        httpClient = new KillBillHttpClient(serverBaseUrl(), user, password, apiKey, apiSecret);
        WireMock.reset();
    }

    @Test
    public void testDoPost() throws Exception {
        final String path = JaxrsResource.PAYMENTS_PATH;
        final String requestId = randomString();
        final String createdBy = "demo";
        final String reason = "test";
        final String comment = "No comments";
        stubFor(withAuthHeaders(
                post(urlEqualTo(path))
                        .withHeader(JaxrsResource.HDR_REQUEST_ID, equalTo(requestId))
                        .withHeader(JaxrsResource.HDR_CREATED_BY, equalTo(createdBy))
                        .withHeader(JaxrsResource.HDR_REASON, equalTo(reason))
                        .withHeader(JaxrsResource.HDR_COMMENT, equalTo(comment))
                        .willReturn(aResponse().withStatus(200)))
               );


        final RequestOptions options = RequestOptions.builder()
                                                     .withRequestId(requestId)
                                                     .withCreatedBy(createdBy)
                                                     .withReason(reason)
                                                     .withComment(comment)
                                                     .build();
        final PaymentTransaction transaction = new PaymentTransaction();
        final Response response = httpClient.doPost(path, transaction, options);

        verify(postRequestedFor(urlEqualTo(path)));

        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoPostFollowingLocation() throws Exception {
        final String path = JaxrsResource.PAYMENTS_PATH;
        final String requestId = randomString();
        final String createdBy = "demo";
        final String reason = "test";
        final String comment = "No comments";
        final String locationToFollow = "/location-to-follow";
        stubFor(withAuthHeaders(
                post(urlEqualTo(path))
                        .withHeader(JaxrsResource.HDR_REQUEST_ID, equalTo(requestId))
                        .withHeader(JaxrsResource.HDR_CREATED_BY, equalTo(createdBy))
                        .withHeader(JaxrsResource.HDR_REASON, equalTo(reason))
                        .withHeader(JaxrsResource.HDR_COMMENT, equalTo(comment))
                        .willReturn(aResponse()
                                            .withHeader("Location", serverBaseUrl() + locationToFollow)
                                            .withStatus(201)))
               );
        final String responseBody = "Success";
        stubFor(get(urlEqualTo(locationToFollow))
                .withHeader("X-Request-Id", equalTo(requestId))
                .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(responseBody)));

        final RequestOptions options = RequestOptions.builder()
                                                     .withRequestId(requestId)
                                                     .withCreatedBy(createdBy)
                                                     .withReason(reason)
                                                     .withComment(comment)
                                                     .withFollowLocation(true)
                                                     .build();
        final PaymentTransaction transaction = new PaymentTransaction();
        final Response response = httpClient.doPost(path, transaction, options);

        verify(postRequestedFor(urlEqualTo(path)));
        verify(getRequestedFor(
                urlMatching(locationToFollow))
                       .withHeader(JaxrsResource.HDR_REQUEST_ID, equalTo(requestId))
                       .withoutHeader(JaxrsResource.HDR_CREATED_BY)
                       .withoutHeader(JaxrsResource.HDR_REASON)
                       .withoutHeader(JaxrsResource.HDR_COMMENT));

        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getResponseBody(), responseBody);
    }

    @Test
    public void testPostWithQueryParams() throws Exception {
        final String path = JaxrsResource.PAYMENTS_PATH;
        final String paramName = "param";
        final String queryParam = randomString();
        final String locationToFollow = "/location-to-follow";
        stubFor(post(urlPathEqualTo(path))
                .withQueryParam(paramName, equalTo(queryParam))
                .willReturn(aResponse()
                            .withStatus(201)
                            .withHeader("Location", serverBaseUrl() + locationToFollow)));

        final String followParamName = "follow-param";
        final String followParam = randomString();
        stubFor(get(urlPathEqualTo(locationToFollow))
                .withQueryParam(followParamName, equalTo(followParam))
                .willReturn(aResponse()
                            .withStatus(200)));

        final RequestOptions options = RequestOptions.builder()
                                                     .withQueryParams(ImmutableMultimap.of(paramName, queryParam))
                                                     .withFollowLocation(true)
                                                     .withQueryParamsForFollow(ImmutableMultimap.of(followParamName, followParam))
                                                     .build();
        final String body = "Some body!";
        final Response response = httpClient.doPost(path, body, options);

        verify(postRequestedFor(urlPathEqualTo(path)).withQueryParam(paramName, equalTo(queryParam)).withRequestBody(equalTo(body)));
        verify(getRequestedFor(urlPathEqualTo(locationToFollow)).withQueryParam(followParamName, equalTo(followParam)));

        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoGetWithCustomHeader() throws Exception {
        final String path = "/some-path";
        final String acceptHeader = randomString();
        stubFor(get(urlPathEqualTo(path))
                    .withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, equalTo(acceptHeader))
                    .willReturn(aResponse().withStatus(200)));

        final RequestOptions options = RequestOptions.builder()
                                                     .withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, acceptHeader).build();
        final Response response = httpClient.doGet(path, options);

        verify(getRequestedFor(urlPathEqualTo(path)).withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, equalTo(acceptHeader)));
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoPut() throws Exception {
        final String path = "/put/some-path";
        final String body = randomString();
        final String contentType = randomString();
        stubFor(put(urlPathEqualTo(path))
                .withRequestBody(equalTo(body))
                .withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, equalTo(contentType))
                .willReturn(aResponse().withStatus(200)));

        final RequestOptions options = RequestOptions.builder()
                                                     .withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, contentType)
                                                     .build();
        final Response response = httpClient.doPut(path, body, options);

        verify(putRequestedFor(urlPathEqualTo(path))
               .withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, equalTo(contentType))
               .withRequestBody(equalTo(body)));
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoDelete() throws Exception {
        final String path = String.format("/%s", randomString());
        stubFor(delete(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200)));

        final Response response = httpClient.doDelete(path, RequestOptions.empty());

        verify(deleteRequestedFor(urlPathEqualTo(path)));
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoDeleteWithBody() throws Exception {
        final String path = String.format("/%s", randomString());
        final PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setTransactionType("CAPTURE");
        final String body = new ObjectMapper().writeValueAsString(transaction);
        stubFor(delete(urlPathEqualTo(path)).withRequestBody(equalTo(body)).willReturn(aResponse().withStatus(200)));

        final Response response = httpClient.doDelete(path, body, Response.class, RequestOptions.empty());

        verify(deleteRequestedFor(urlPathEqualTo(path)).withRequestBody(equalTo(body)));
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoHead() throws Exception {
        final String path = String.format("/%s", randomString());
        stubFor(head(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200)));

        final Response response = httpClient.doHead(path, RequestOptions.empty());

        verify(headRequestedFor(urlPathEqualTo(path)));
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDoOptions() throws Exception {
        final String path = String.format("/%s", randomString());
        stubFor(options(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200)));

        final Response response = httpClient.doOptions(path, RequestOptions.empty());

        verify(optionsRequestedFor(urlPathEqualTo(path)));
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    private MappingBuilder withAuthHeaders(final MappingBuilder mappingBuilder) {
        return withAuthHeaders(this.apiKey, this.apiSecret, mappingBuilder);
    }

    private MappingBuilder withAuthHeaders(final String apiKey, final String apiSecret, final MappingBuilder mappingBuilder) {
        return mappingBuilder
                .withHeader(JaxrsResource.HDR_API_KEY, equalTo(apiKey))
                .withHeader(JaxrsResource.HDR_API_SECRET, equalTo(apiSecret));
    }
}
