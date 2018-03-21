/*
 * Copyright 2010-2013 Ning, Inc.
 * Copyright 2014-2016 Groupon, Inc
 * Copyright 2014-2016 The Billing Project, LLC
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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import org.killbill.billing.client.model.KillBillObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Realm;
import com.ning.http.client.Realm.RealmBuilder;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class KillBillHttpClient implements Closeable {

    public static final int DEFAULT_HTTP_TIMEOUT_SEC = 10;

    public static final Multimap<String, String> DEFAULT_EMPTY_QUERY = ImmutableMultimap.<String, String>of();
    public static final String AUDIT_OPTION_CREATED_BY = "__AUDIT_OPTION_CREATED_BY";
    public static final String AUDIT_OPTION_REASON = "__AUDIT_OPTION_REASON";
    public static final String AUDIT_OPTION_COMMENT = "__AUDIT_OPTION_COMMENT";
    public static final String TENANT_OPTION_API_KEY = "__TENANT_OPTION_API_KEY";
    public static final String TENANT_OPTION_API_SECRET = "__TENANT_OPTION_API_SECRET";
    public static final String RBAC_OPTION_USERNAME = "__RBAC_OPTION_USERNAME";
    public static final String RBAC_OPTION_PASSWORD = "__RBAC_OPTION_PASSWORD";

    public static final String CONTROL_PLUGIN_NAME = "controlPluginName";

    public static final String HTTP_HEADER_ACCEPT = "Accept";
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    public static final String ACCEPT_HTML = "text/html";
    public static final String ACCEPT_JSON = "application/json";
    public static final String ACCEPT_XML = "application/xml";
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    public static final String CONTENT_TYPE_XML = "application/xml; charset=utf-8";

    private static final Logger log = LoggerFactory.getLogger(KillBillHttpClient.class);
    private static final String USER_AGENT = "KillBill-JavaClient/1.0";

    private static final Joiner CSV_JOINER = Joiner.on(",");

    private final boolean DEBUG = Boolean.parseBoolean(System.getProperty("org.killbill.client.debug", "false"));

    private final String kbServerUrl;
    private final String username;
    private final String password;
    private final String apiKey;
    private final String apiSecret;
    private final AsyncHttpClient httpClient;
    private final ObjectMapper mapper;
    private final int requestTimeoutSec;

    /**
     * @param kbServerUrl    Kill Bill url
     * @param username       Kill Bill username
     * @param password       Kill Bill password
     * @param apiKey         Kill Bill api key
     * @param apiSecret      Kill Bill api secret
     * @param proxyHost      hostname of a proxy server that the client should use
     * @param proxyPort      port number of a proxy server that the client should use
     * @param connectTimeOut connect timeout in milliseconds
     * @param readTimeOut    read timeout in milliseconds
     * @param requestTimeout request timeout in milliseconds
     */
    public KillBillHttpClient(final String kbServerUrl,
                              final String username,
                              final String password,
                              final String apiKey,
                              final String apiSecret,
                              final String proxyHost,
                              final Integer proxyPort,
                              final Integer connectTimeOut,
                              final Integer readTimeOut,
                              final Integer requestTimeout) {
        this(kbServerUrl, username, password, apiKey, apiSecret, proxyHost, proxyPort, connectTimeOut, readTimeOut, requestTimeout, null, null);
    }

    /**
     * @param kbServerUrl    Kill Bill url
     * @param username       Kill Bill username
     * @param password       Kill Bill password
     * @param apiKey         Kill Bill api key
     * @param apiSecret      Kill Bill api secret
     * @param proxyHost      hostname of a proxy server that the client should use
     * @param proxyPort      port number of a proxy server that the client should use
     * @param connectTimeOut connect timeout in milliseconds
     * @param readTimeOut    read timeout in milliseconds
     * @param requestTimeout request timeout in milliseconds
     * @param strictSSL      whether to bypass SSL certificates validation
     * @param SSLProtocol    SSL protocol to use
     */
    public KillBillHttpClient(final String kbServerUrl,
                              final String username,
                              final String password,
                              final String apiKey,
                              final String apiSecret,
                              final String proxyHost,
                              final Integer proxyPort,
                              final Integer connectTimeOut,
                              final Integer readTimeOut,
                              final Integer requestTimeout,
                              final Boolean strictSSL,
                              final String SSLProtocol) {
        this.kbServerUrl = kbServerUrl;
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;

        final AsyncHttpClientConfig.Builder cfg = new AsyncHttpClientConfig.Builder();

        if (requestTimeout != null) {
            cfg.setRequestTimeout(requestTimeout);
            int timeoutSec = (int) TimeUnit.MILLISECONDS.toSeconds(requestTimeout);
            if (TimeUnit.SECONDS.toMillis(timeoutSec) != requestTimeout) {
                timeoutSec += 1;
            }
            requestTimeoutSec = timeoutSec;
        } else {
            cfg.setRequestTimeout(DEFAULT_HTTP_TIMEOUT_SEC * 1000);
            requestTimeoutSec = DEFAULT_HTTP_TIMEOUT_SEC;
        }

        cfg.setConnectTimeout(MoreObjects.firstNonNull(connectTimeOut, DEFAULT_HTTP_TIMEOUT_SEC * 1000));
        cfg.setReadTimeout(MoreObjects.firstNonNull(readTimeOut, DEFAULT_HTTP_TIMEOUT_SEC * 1000));
        cfg.setUserAgent(USER_AGENT);

        if (proxyHost != null && proxyPort != null) {
            final ProxyServer proxyServer = new ProxyServer(proxyHost, proxyPort);
            cfg.setProxyServer(proxyServer);
        }

        if (strictSSL != null) {
            try {
                cfg.setSSLContext(SslUtils.getInstance().getSSLContext(strictSSL, SSLProtocol));
            } catch (final GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }

        this.httpClient = new AsyncHttpClient(cfg.build());

        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }

    public KillBillHttpClient(final String kbServerUrl, final String username, final String password, final String apiKey, final String apiSecret) {
        this(kbServerUrl, username, password, apiKey, apiSecret, null, null, null, null, null);
    }

    public KillBillHttpClient(final String kbServerUrl, final String username, final String password, final String apiKey, final String apiSecret, final String proxyHost, final Integer proxyPort) {
        this(kbServerUrl, username, password, apiKey, apiSecret, proxyHost, proxyPort, null, null, null);
    }

    public KillBillHttpClient() {
        this(System.getProperty("killbill.url", "http://127.0.0.1:8080/"),
             System.getProperty("killbill.username", "admin"),
             System.getProperty("killbill.password", "password"),
             System.getProperty("killbill.apiKey", "bob"),
             System.getProperty("killbill.apiSecret", "lazar"));
    }

    @Override
    public void close() {
        httpClient.close();
    }

    // POST

    @Deprecated
    public Response doPost(final String uri, final Object body, final Multimap<String, String> options) throws KillBillClientException {
        return doPost(uri, body, options, Response.class);
    }

    @Deprecated
    public <T> T doPost(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPost(uri, body, options, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doPost(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPostAndMaybeFollowLocation(uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, false);
    }

    public Response doPost(final String uri, final Object body, final RequestOptions requestOptions) throws KillBillClientException {
        return doPost(uri, body, Response.class, requestOptions);
    }

    public <T> T doPost(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions) throws KillBillClientException {
        return doPost(uri, body, returnClass, requestOptions, this.requestTimeoutSec);
    }

    public <T> T doPost(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "POST";
        return doPrepareRequest(verb, uri, body, returnClass, requestOptions, timeoutSec);
    }

    @Deprecated
    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPostAndFollowLocation(uri, body, options, DEFAULT_EMPTY_QUERY, clazz);
    }

    @Deprecated
    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final Class<T> clazz) throws KillBillClientException {
        return doPostAndFollowLocation(uri, body, options, optionsForFollow, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPostAndFollowLocation(uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    @Deprecated
    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPostAndMaybeFollowLocation(uri, body, options, optionsForFollow, timeoutSec, clazz, true);
    }

    @Deprecated
    public <T> T doPostAndMaybeFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "POST";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, body, options, optionsForFollow, timeoutSec, clazz, followLocation);
    }

    // PUT

    @Deprecated
    public Response doPut(final String uri, final Object body, final Multimap<String, String> options) throws KillBillClientException {
        return doPut(uri, body, options, Response.class);
    }

    @Deprecated
    public <T> T doPut(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPut(uri, body, options, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doPut(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPutAndMaybeFollowLocation(uri, body, options, timeoutSec, clazz, false);
    }

    @Deprecated
    public <T> T doPutAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPutAndFollowLocation(uri, body, options, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doPutAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPutAndMaybeFollowLocation(uri, body, options, timeoutSec, clazz, true);
    }

    @Deprecated
    public <T> T doPutAndMaybeFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "PUT";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, followLocation);
    }

    public Response doPut(final String uri, final Object body, final RequestOptions options) throws KillBillClientException {
        return doPut(uri, body, Response.class, options);
    }

    public <T> T doPut(final String uri, final Object body, final Class<T> returnClass, final RequestOptions options) throws KillBillClientException {
        return doPut(uri, body, returnClass, options, this.requestTimeoutSec);
    }

    public <T> T doPut(final String uri, final Object body, final Class<T> returnClass, final RequestOptions options, final int timeoutSec) throws KillBillClientException {
        final String verb = "PUT";
        return doPrepareRequest(verb, uri, body, returnClass, options, timeoutSec);
    }

    // DELETE

    @Deprecated
    public Response doDelete(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doDelete(uri, options, Response.class);
    }

    @Deprecated
    public <T> T doDelete(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doDeleteAndMaybeFollowLocation(uri, options, requestTimeoutSec, clazz, false);
    }

    @Deprecated
    public <T> T doDeleteAndMaybeFollowLocation(final String uri, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "DELETE";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, followLocation);
    }

    public Response doDelete(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doDelete(uri, null, Response.class, requestOptions);
    }

    public <T> T doDelete(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions) throws KillBillClientException {
        return doDelete(uri, body, returnClass, requestOptions, this.requestTimeoutSec);
    }

    public <T> T doDelete(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "DELETE";
        return doPrepareRequest(verb, uri, body, returnClass, requestOptions, timeoutSec);
    }

    // GET

    @Deprecated
    public Response doGet(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doGet(uri, options, Response.class);
    }

    @Deprecated
    public <T> T doGet(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doGetWithUrl(uri, options, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doGetWithUrl(final String url, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        final String verb = "GET";
        return doPrepareRequestAndMaybeFollowLocation(verb, url, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    public Response doGet(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doGet(uri, Response.class, requestOptions);
    }

    public <T> T doGet(final String uri, final Class<T> returnClass, final RequestOptions requestOptions) throws KillBillClientException {
        return doGet(uri, returnClass, requestOptions, this.requestTimeoutSec);
    }

    public <T> T doGet(final String uri, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "GET";
        return doPrepareRequest(verb, uri, null, returnClass, requestOptions, timeoutSec);
    }

    // HEAD

    @Deprecated
    public Response doHead(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doHead(uri, options, Response.class);
    }

    @Deprecated
    public <T> T doHead(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doHeadWithUrl(uri, options, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doHeadWithUrl(final String url, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        final String verb = "HEAD";
        return doPrepareRequestAndMaybeFollowLocation(verb, url, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    public Response doHead(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doHead(uri, requestOptions, this.requestTimeoutSec);
    }

    public Response doHead(final String uri, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "HEAD";
        return doPrepareRequest(verb, uri, null, Response.class, requestOptions, timeoutSec);
    }

    // OPTIONS

    @Deprecated
    public Response doOptions(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doOptions(uri, options, Response.class);
    }

    @Deprecated
    public <T> T doOptions(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doOptionsWithUrl(uri, options, requestTimeoutSec, clazz);
    }

    @Deprecated
    public <T> T doOptionsWithUrl(final String url, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        final String verb = "OPTIONS";
        return doPrepareRequestAndMaybeFollowLocation(verb, url, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    public Response doOptions(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doOptions(uri, requestOptions, this.requestTimeoutSec);
    }

    public Response doOptions(final String uri, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "OPTIONS";
        return doPrepareRequest(verb, uri, null, Response.class, requestOptions, timeoutSec);
    }

    // COMMON
    @Deprecated
    private <T> T doPrepareRequestAndMaybeFollowLocation(final String verb, final String uri, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, null, options, optionsForFollow, timeoutSec, clazz, false);
    }

    @Deprecated
    private <T> T doPrepareRequestAndMaybeFollowLocation(final String verb, final String uri, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, null, options, optionsForFollow, timeoutSec, clazz, followLocation);
    }

    private <T> T doPrepareRequest(final String verb, final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery(verb, getKBServerUrl(uri), requestOptions);

        // Multi-Tenancy headers
        final String apiKey = requestOptions.getTenantApiKey() != null ? requestOptions.getTenantApiKey() : this.apiKey;
        addHeader(builder, JaxrsResource.HDR_API_KEY, apiKey);
        final String apiSecret = requestOptions.getTenantApiSecret() != null ? requestOptions.getTenantApiSecret() : this.apiSecret;
        addHeader(builder, JaxrsResource.HDR_API_SECRET, apiSecret);

        // Metadata Additional headers
        addHeader(builder, JaxrsResource.HDR_CREATED_BY, requestOptions.getCreatedBy());
        addHeader(builder, JaxrsResource.HDR_REASON, requestOptions.getReason());
        addHeader(builder, JaxrsResource.HDR_COMMENT, requestOptions.getComment());

        addHeader(builder, JaxrsResource.HDR_REQUEST_ID, requestOptions.getRequestId());

        if (!"GET".equals(verb) && !"HEAD".equals(verb)) {
            if (body != null) {
                if (body instanceof String) {
                    builder.setBody((String) body);
                } else {
                    try {
                        builder.setBody(mapper.writeValueAsString(body));
                    } catch (final JsonProcessingException e) {
                        throw new KillBillClientException(e);
                    }
                }
            } else {
                builder.setBody("{}");
            }
        }

        final Response response = doRequest(builder, timeoutSec);
        if (response.getStatusCode() == 404 || response.getStatusCode() == 204) {
            return createEmptyResult(returnClass);
        }

        if (requestOptions.shouldFollowLocation() && response.getHeader("Location") != null) {
            final String location = response.getHeader("Location");
            final RequestOptions optionsForFollow = RequestOptions.builder()
                                                                  .withUser(requestOptions.getUser())
                                                                  .withPassword(requestOptions.getPassword())
                                                                  .withTenantApiKey(requestOptions.getTenantApiKey())
                                                                  .withTenantApiSecret(requestOptions.getTenantApiSecret())
                                                                  .withRequestId(requestOptions.getRequestId())
                                                                  .withFollowLocation(false)
                                                                  .withQueryParams(requestOptions.getQueryParamsForFollow())
                                                                  .build();
            return doGet(location, returnClass, optionsForFollow, timeoutSec);
        }
        throwExceptionOnResponseError(response);
        return deserializeResponse(response, returnClass);
    }

    private static void addHeader(final BoundRequestBuilder builder, final String headerName, final String value) {
        if (value != null) {
            builder.addHeader(headerName, value);
        }
    }

    @Deprecated
    private <T> T doPrepareRequestAndMaybeFollowLocation(final String verb, final String uri, final Object body, final Multimap<String, String> optionsRo, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final Multimap<String, String> options = HashMultimap.<String, String>create(optionsRo);

        final String createdBy = getUniqueValue(options, AUDIT_OPTION_CREATED_BY);
        final String reason = getUniqueValue(options, AUDIT_OPTION_REASON);
        final String comment = getUniqueValue(options, AUDIT_OPTION_COMMENT);
        String apiKey = getUniqueValue(options, TENANT_OPTION_API_KEY);
        if (apiKey == null) {
            apiKey = this.apiKey;
        }
        String apiSecret = getUniqueValue(options, TENANT_OPTION_API_SECRET);
        if (apiSecret == null) {
            apiSecret = this.apiSecret;
        }
        String username = getUniqueValue(options, RBAC_OPTION_USERNAME);
        if (username == null) {
            username = this.username;
        }
        String password = getUniqueValue(options, RBAC_OPTION_PASSWORD);
        if (password == null) {
            password = this.password;
        }

        options.removeAll(AUDIT_OPTION_CREATED_BY);
        options.removeAll(AUDIT_OPTION_REASON);
        options.removeAll(AUDIT_OPTION_COMMENT);
        options.removeAll(TENANT_OPTION_API_KEY);
        options.removeAll(TENANT_OPTION_API_SECRET);
        options.removeAll(RBAC_OPTION_USERNAME);
        options.removeAll(RBAC_OPTION_PASSWORD);

        final BoundRequestBuilder builder = getBuilderWithHeaderAndQuery(verb, getKBServerUrl(uri), username, password, options);

        // Multi-Tenancy headers
        if (apiKey != null) {
            builder.addHeader(JaxrsResource.HDR_API_KEY, apiKey);
        }
        if (apiSecret != null) {
            builder.addHeader(JaxrsResource.HDR_API_SECRET, apiSecret);
        }
        // Metadata Additional headers
        if (createdBy != null) {
            builder.addHeader(JaxrsResource.HDR_CREATED_BY, createdBy);
        }
        if (reason != null) {
            builder.addHeader(JaxrsResource.HDR_REASON, reason);
        }
        if (comment != null) {
            builder.addHeader(JaxrsResource.HDR_COMMENT, comment);
        }

        if (!"GET".equals(verb) && !"HEAD".equals(verb)) {
            if (body != null) {
                if (body instanceof String) {
                    builder.setBody((String) body);
                } else {
                    try {
                        builder.setBody(mapper.writeValueAsString(body));
                    } catch (final JsonProcessingException e) {
                        throw new KillBillClientException(e);
                    }
                }
            } else {
                builder.setBody("{}");
            }
        }

        final Response response = doRequest(builder, timeoutSec);
        if (response.getStatusCode() == 404 || response.getStatusCode() == 204) {
            return createEmptyResult(clazz);
        }

        if (followLocation && response.getHeader("Location") != null) {
                final String location = response.getHeader("Location");
                return doGetWithUrl(location, optionsForFollow, timeoutSec, clazz);
        }
        throwExceptionOnResponseError(response);
        return deserializeResponse(response, clazz);
    }

    private String getUniqueValue(final Multimap<String, String> options, final String key) {
        final Collection<String> values = options.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        } else {
            Preconditions.checkState(values.size() == 1, "You can only specify a unique value for " + key);
            return values.iterator().next();
        }
    }

    private static Response doRequest(final BoundRequestBuilder builder, final int timeoutSec) throws KillBillClientException {
        try {
            final ListenableFuture<Response> futureStatus = builder.execute(new AsyncCompletionHandler<Response>() {
                @Override
                public Response onCompleted(final Response response) throws Exception {
                    return response;
                }
            });
            return futureStatus.get(timeoutSec, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            throw new KillBillClientException(e);
        } catch (final ExecutionException e) {
            throw new KillBillClientException(e);
        } catch (final TimeoutException e) {
            throw new KillBillClientException(e);
        }
    }

    private void throwExceptionOnResponseError(final Response response) throws KillBillClientException {
        if (response.getStatusCode() == 401) {
            throw new KillBillClientException(
                    new IllegalArgumentException("Unauthorized - did you configure your RBAC and/or tenant credentials?"), response);
        }
        if (response.getStatusCode() >= 400) {
            final BillingException exception = deserializeResponse(response, BillingException.class);
            if (exception != null) {
                log.warn("Error " + response.getStatusCode() + " from Kill Bill: " + exception.getMessage());
            } else {
                log.warn("Error " + response.getStatusCode() + " from Kill Bill");
            }
            throw new KillBillClientException(exception, response);
        }
    }

    private <T> T deserializeResponse(final Response response, final Class<T> clazz) throws KillBillClientException {
        // No deserialization required
        if (Response.class.isAssignableFrom(clazz)) {
            return clazz.cast(response);
        }

        final T result = unmarshalResponse(response, clazz);
        if (KillBillObjects.class.isAssignableFrom(clazz)) {
            final KillBillObjects objects = ((KillBillObjects) result);
            final String paginationCurrentOffset = response.getHeader(JaxrsResource.HDR_PAGINATION_CURRENT_OFFSET);
            if (paginationCurrentOffset != null) {
                objects.setPaginationCurrentOffset(Integer.parseInt(paginationCurrentOffset));
            }
            final String paginationNextOffset = response.getHeader(JaxrsResource.HDR_PAGINATION_NEXT_OFFSET);
            if (paginationNextOffset != null) {
                objects.setPaginationNextOffset(Integer.parseInt(paginationNextOffset));
            }
            final String paginationTotalNbRecords = response.getHeader(JaxrsResource.HDR_PAGINATION_TOTAL_NB_RECORDS);
            if (paginationTotalNbRecords != null) {
                objects.setPaginationTotalNbRecords(Integer.parseInt(paginationTotalNbRecords));
            }
            final String paginationMaxNbRecords = response.getHeader(JaxrsResource.HDR_PAGINATION_MAX_NB_RECORDS);
            if (paginationMaxNbRecords != null) {
                objects.setPaginationMaxNbRecords(Integer.parseInt(paginationMaxNbRecords));
            }
            objects.setPaginationNextPageUri(response.getHeader(JaxrsResource.HDR_PAGINATION_NEXT_PAGE_URI));
            objects.setKillBillHttpClient(this);
        }

        return result;
    }

    private static <T> T createEmptyResult(final Class<T> clazz) {// Return empty list for KillBillObjects instead of null for convenience
        if (Iterable.class.isAssignableFrom(clazz)) {
            for (final Constructor constructor : clazz.getConstructors()) {
                if (constructor.getParameterTypes().length == 0) {
                    try {
                        return clazz.cast(constructor.newInstance());
                    } catch (final InstantiationException e) {
                        return null;
                    } catch (final IllegalAccessException e) {
                        return null;
                    } catch (final InvocationTargetException e) {
                        return null;
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    private <T> T unmarshalResponse(final Response response, final Class<T> clazz) throws KillBillClientException {
        final T result;

        final boolean requiresMapper = (String.class != clazz);
        try {
            if (DEBUG || !requiresMapper) {
                final String content = response.getResponseBody();
                log.debug("Received: " + content);
                result = requiresMapper ? mapper.readValue(content, clazz) : (T) content;
            } else {
                InputStream in = null;
                try {
                    in = response.getResponseBodyAsStream();
                    result = mapper.readValue(in, clazz);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (final IOException e) {
                            log.warn("Failed to close http-client - provided InputStream: {}", e.getLocalizedMessage());
                        }
                    }
                }
            }
        } catch (final IOException e) {
            throw new KillBillClientException(e, response);
        }
        return result;
    }

    @Deprecated
    private BoundRequestBuilder getBuilderWithHeaderAndQuery(final String verb, final String url, @Nullable final String username, @Nullable final String password, final Multimap<String, String> options) {
        final BoundRequestBuilder builder;

        if ("GET".equals(verb)) {
            builder = httpClient.prepareGet(url);
        } else if ("POST".equals(verb)) {
            builder = httpClient.preparePost(url);
        } else if ("PUT".equals(verb)) {
            builder = httpClient.preparePut(url);
        } else if ("DELETE".equals(verb)) {
            builder = httpClient.prepareDelete(url);
        } else if ("HEAD".equals(verb)) {
            builder = httpClient.prepareHead(url);
        } else if ("OPTIONS".equals(verb)) {
            builder = httpClient.prepareOptions(url);
        } else {
            throw new IllegalArgumentException("Unrecognized verb: " + verb);
        }

        if (username != null && password != null) {
            final Realm realm = new RealmBuilder().setPrincipal(username).setPassword(password).setScheme(Realm.AuthScheme.BASIC).setUsePreemptiveAuth(true).build();
            builder.setRealm(realm);
        }

        final Collection<String> acceptHeaders = options.removeAll(HTTP_HEADER_ACCEPT);
        final String acceptHeader;
        if (!acceptHeaders.isEmpty()) {
            acceptHeader = CSV_JOINER.join(acceptHeaders);
        } else {
            acceptHeader = ACCEPT_JSON;
        }
        builder.addHeader(HTTP_HEADER_ACCEPT, acceptHeader);

        String contentTypeHeader = getUniqueValue(options, HTTP_HEADER_CONTENT_TYPE);
        if (contentTypeHeader == null) {
            contentTypeHeader = CONTENT_TYPE_JSON;
        } else {
            options.removeAll(HTTP_HEADER_CONTENT_TYPE);
        }
        builder.addHeader(HTTP_HEADER_CONTENT_TYPE, contentTypeHeader);

        builder.setBodyEncoding("UTF-8");

        for (final String key : options.keySet()) {
            if (options.get(key) != null) {
                for (final String value : options.get(key)) {
                    builder.addQueryParam(key, value);
                }
            }
        }

        return builder;
    }

    private BoundRequestBuilder getBuilderWithHeaderAndQuery(final String verb, final String url, final RequestOptions requestOptions) {
        final BoundRequestBuilder builder;

        if ("GET".equals(verb)) {
            builder = httpClient.prepareGet(url);
        } else if ("POST".equals(verb)) {
            builder = httpClient.preparePost(url);
        } else if ("PUT".equals(verb)) {
            builder = httpClient.preparePut(url);
        } else if ("DELETE".equals(verb)) {
            builder = httpClient.prepareDelete(url);
        } else if ("HEAD".equals(verb)) {
            builder = httpClient.prepareHead(url);
        } else if ("OPTIONS".equals(verb)) {
            builder = httpClient.prepareOptions(url);
        } else {
            throw new IllegalArgumentException("Unrecognized verb: " + verb);
        }

        final String username = requestOptions.getUser() != null ? requestOptions.getUser() : this.username;
        final String password = requestOptions.getPassword() != null ? requestOptions.getPassword() : this.password;
        if (username != null && password != null) {
            final Realm realm = new RealmBuilder().setPrincipal(username).setPassword(password).setScheme(Realm.AuthScheme.BASIC).setUsePreemptiveAuth(true).build();
            builder.setRealm(realm);
        }
        for (final Entry<String, String> header : requestOptions.getHeaders().entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }

        builder.setBodyEncoding("UTF-8");
        final Multimap<String, String> queryParams = requestOptions.getQueryParams();
        for (final String key : queryParams.keySet()) {
            if (queryParams.get(key) != null) {
                for (final String value : queryParams.get(key)) {
                    builder.addQueryParam(key, value);
                }
            }
        }

        return builder;
    }

    private String getKBServerUrl(final String uri) throws KillBillClientException {
        try {
            final URI u = new URI(uri);
            if (u.isAbsolute()) {
                return uri;
            } else {
                return String.format("%s%s", kbServerUrl, uri);
            }
        } catch (final URISyntaxException e) {
            throw new KillBillClientException(e);
        }
    }
}
