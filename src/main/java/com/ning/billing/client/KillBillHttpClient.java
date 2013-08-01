/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
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

package com.ning.billing.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Realm;
import com.ning.http.client.Realm.RealmBuilder;
import com.ning.http.client.Response;

public class KillBillHttpClient {

    public static String KBServerUrl = PropertiesManager.getInstance().getCommonProperty("killbill.server.host.url", "http://127.0.0.1:8080/killbill");

    public static final int DEFAULT_HTTP_TIMEOUT_SEC = 10;

    private String createdBy;
    private String reason;
    private String comment;

    public KillBillHttpClient() {}

    public KillBillHttpClient(String createdBy, String reason, String comment) {
        this.createdBy = createdBy;
        this.reason = reason;
        this.comment = comment;
    }

    protected Response doPost(final String uri, final String body, final Map<String, String> queryParams, final int timeoutSec) {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery("POST", getKBServerUrl(uri), queryParams);
        if (body != null) {
            builder.setBody(body);
        } else {
            builder.setBody("{}");
        }
        return executeAndWait(builder, timeoutSec, true);
    }

    protected Response doPut(final String uri, final String body, final Map<String, String> queryParams, final int timeoutSec) {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery("PUT", getKBServerUrl(uri), queryParams);
        if (body != null) {
            builder.setBody(body);
        } else {
            builder.setBody("{}");
        }
        return executeAndWait(builder, timeoutSec, true);
    }

    protected Response doDelete(final String uri, final Map<String, String> queryParams, final int timeoutSec) {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery("DELETE", getKBServerUrl(uri), queryParams);
        return executeAndWait(builder, timeoutSec, true);
    }

    protected Response doGet(final String uri, final Map<String, String> queryParams, final int timeoutSec) {
        return doGetWithUrl(getKBServerUrl(uri), queryParams, timeoutSec);
    }

    protected Response doHead(final String uri, final Map<String, String> queryParams, final int timeoutSec) {
        return doHeadWithUrl(getKBServerUrl(uri), queryParams, timeoutSec);
    }

    protected Response doOptions(final String uri, final Map<String, String> queryParams, final int timeoutSec) {
        return doOptionsWithUrl(getKBServerUrl(uri), queryParams, timeoutSec);
    }

    protected Response doGetWithUrl(final String url, final Map<String, String> queryParams, final int timeoutSec) {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery("GET", url, queryParams);
        return executeAndWait(builder, timeoutSec, false);
    }

    protected Response doHeadWithUrl(final String url, final Map<String, String> queryParams, final int timeoutSec) {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery("HEAD", url, queryParams);
        return executeAndWait(builder, timeoutSec, false);
    }

    protected Response doOptionsWithUrl(final String url, final Map<String, String> queryParams, final int timeoutSec) {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery("OPTIONS", url, queryParams);
        return executeAndWait(builder, timeoutSec, false);
    }

    private Response executeAndWait(final BoundRequestBuilder builder, final int timeoutSec, final boolean addContextHeader) {
        if (addContextHeader) {
            builder.addHeader(JaxrsResource.HDR_CREATED_BY, createdBy);
            builder.addHeader(JaxrsResource.HDR_REASON, reason);
            builder.addHeader(JaxrsResource.HDR_COMMENT, comment);
        }

        Response response = null;
        try {
            final ListenableFuture<Response> futureStatus =
                    builder.execute(new AsyncCompletionHandler<Response>() {
                        @Override
                        public Response onCompleted(final Response response) throws Exception {
                            return response;
                        }
                    });
            response = futureStatus.get(timeoutSec, TimeUnit.SECONDS);
        } catch (final Exception e) {
        }

        if (response != null && response.getStatusCode() == 401) {
            throw new IllegalArgumentException("Unauthorized - did you configure your tenant credentials?");
        }
        return response;
    }

    private BoundRequestBuilder getBuilderWithHeaderAndQuery(final String verb, final String url, final Map<String, String> queryParams) {
        BoundRequestBuilder builder = null;

        String apiKey = PropertiesManager.getInstance().getCommonProperty("killbill.tenant.apiKey", "bob");
        String apiSecret = PropertiesManager.getInstance().getCommonProperty("killbill.tenant.apiSecret", "lazar");
        Realm realm = new RealmBuilder().setPrincipal(apiKey).setPassword(apiSecret).build();
        AsyncHttpClientConfig cfg = new AsyncHttpClientConfig.Builder().setRealm(realm).build();

        AsyncHttpClient httpClient = new AsyncHttpClient(cfg);
        if (verb.equals("GET")) {
            builder = httpClient.prepareGet(url);
        } else if (verb.equals("POST")) {
            builder = httpClient.preparePost(url);
        } else if (verb.equals("PUT")) {
            builder = httpClient.preparePut(url);
        } else if (verb.equals("DELETE")) {
            builder = httpClient.prepareDelete(url);
        } else if (verb.equals("HEAD")) {
            builder = httpClient.prepareHead(url);
        } else if (verb.equals("OPTIONS")) {
            builder = httpClient.prepareOptions(url);
        } else {
        }
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        for (final Entry<String, String> q : queryParams.entrySet()) {
            builder.addQueryParameter(q.getKey(), q.getValue());
        }

        return builder;
    }

    private String getKBServerUrl(String uri) {
        return String.format("%s%s", KBServerUrl, uri);
    }
}
