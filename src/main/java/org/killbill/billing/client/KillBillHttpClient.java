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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Response;
import com.ning.http.client.Realm;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.AsyncCompletionHandler;

import org.killbill.billing.client.model.BillingException;
import org.killbill.billing.client.model.KillBillObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.Realm.RealmBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class KillBillHttpClient {

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

    public static final int HTTP_PAYMENT_REQUIRED_STATUS = 402;

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
     * @param kbServerUrl Kill Bill url
     * @param username Kill Bill username
     * @param password Kill Bill password
     * @param apiKey Kill Bill api key
     * @param apiSecret Kill Bill api secret
     * @param proxyHost hostname of a proxy server that the client should use
     * @param proxyPort port number of a proxy server that the client should use
     * @param connectTimeOut connect timeout in milliseconds
     * @param readTimeOut read timeout in milliseconds
     * @param requestTimeout request timeout in milliseconds
     *
     */
    public KillBillHttpClient(final String kbServerUrl, final String username, final String password, final String apiKey, final String apiSecret, final String proxyHost, final Integer proxyPort, final Integer connectTimeOut, final Integer readTimeOut, final Integer requestTimeout) {
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
                timeoutSec+=1;
            }
            requestTimeoutSec = timeoutSec;
        } else {
            cfg.setRequestTimeout(DEFAULT_HTTP_TIMEOUT_SEC*1000);
            requestTimeoutSec = DEFAULT_HTTP_TIMEOUT_SEC;
        }

        cfg.setConnectTimeout(MoreObjects.firstNonNull(connectTimeOut, DEFAULT_HTTP_TIMEOUT_SEC * 1000));
        cfg.setReadTimeout(MoreObjects.firstNonNull(readTimeOut, DEFAULT_HTTP_TIMEOUT_SEC * 1000));
        cfg.setUserAgent(USER_AGENT);

        if (proxyHost != null && proxyPort != null) {
            final ProxyServer proxyServer = new ProxyServer(proxyHost, proxyPort);
            cfg.setProxyServer(proxyServer);
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

    public void close() {
        httpClient.close();
    }

    // POST

    public Response doPost(final String uri, final Object body, final Multimap<String, String> options) throws KillBillClientException {
        return doPost(uri, body, options, Response.class);
    }

    public <T> T doPost(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPost(uri, body, options, requestTimeoutSec, clazz);
    }

    public <T> T doPost(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPostAndMaybeFollowLocation(uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, false);
    }

    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPostAndFollowLocation(uri, body, options, DEFAULT_EMPTY_QUERY, clazz);
    }

    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final Class<T> clazz) throws KillBillClientException {
        return doPostAndFollowLocation(uri, body, options, optionsForFollow, requestTimeoutSec, clazz);
    }

    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPostAndFollowLocation(uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    public <T> T doPostAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPostAndMaybeFollowLocation(uri, body, options, optionsForFollow, timeoutSec, clazz, true);
    }

    public <T> T doPostAndMaybeFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "POST";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, body, options, optionsForFollow, timeoutSec, clazz, followLocation);
    }

    // PUT

    public Response doPut(final String uri, final Object body, final Multimap<String, String> options) throws KillBillClientException {
        return doPut(uri, body, options, Response.class);
    }

    public <T> T doPut(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPut(uri, body, options, requestTimeoutSec, clazz);
    }

    public <T> T doPut(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPutAndMaybeFollowLocation(uri, body, options, timeoutSec, clazz, false);
    }

    public <T> T doPutAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doPutAndFollowLocation(uri, body, options, requestTimeoutSec, clazz);
    }

    public <T> T doPutAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPutAndMaybeFollowLocation(uri, body, options, timeoutSec, clazz, true);
    }

    public <T> T doPutAndMaybeFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "PUT";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, followLocation);
    }

    // DELETE

    public Response doDelete(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doDelete(uri, options, Response.class);
    }

    public <T> T doDelete(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doDeleteAndMaybeFollowLocation(uri, options, requestTimeoutSec, clazz, false);
    }

    public <T> T doDeleteAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doDeleteAndFollowLocation(uri, body, options, requestTimeoutSec, clazz);
    }

    public <T> T doDeleteAndFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doDeleteAndMaybeFollowLocation(uri, body, options, timeoutSec, clazz, true);
    }

    public <T> T doDeleteAndMaybeFollowLocation(final String uri, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "DELETE";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, followLocation);
    }

    public <T> T doDeleteAndMaybeFollowLocation(final String uri, final Object body, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        final String verb = "DELETE";
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, body, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz, followLocation);
    }

    // GET

    public Response doGet(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doGet(uri, options, Response.class);
    }

    public <T> T doGet(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doGetWithUrl(uri, options, requestTimeoutSec, clazz);
    }

    public <T> T doGet(final String uri, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doGetWithUrl(uri, options, timeoutSec, clazz);
    }

    public <T> T doGetWithUrl(final String url, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        final String verb = "GET";
        return doPrepareRequestAndMaybeFollowLocation(verb, url, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    // HEAD

    public Response doHead(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doHead(uri, options, Response.class);
    }

    public <T> T doHead(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doHeadWithUrl(uri, options, requestTimeoutSec, clazz);
    }

    public <T> T doHead(final String uri, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doHeadWithUrl(uri, options, timeoutSec, clazz);
    }

    public <T> T doHeadWithUrl(final String url, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        final String verb = "HEAD";
        return doPrepareRequestAndMaybeFollowLocation(verb, url, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    // OPTIONS

    public Response doOptions(final String uri, final Multimap<String, String> options) throws KillBillClientException {
        return doOptions(uri, options, Response.class);
    }

    public <T> T doOptions(final String uri, final Multimap<String, String> options, final Class<T> clazz) throws KillBillClientException {
        return doOptionsWithUrl(uri, options, requestTimeoutSec, clazz);
    }

    public <T> T doOptions(final String uri, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doOptionsWithUrl(uri, options, timeoutSec, clazz);
    }

    public <T> T doOptionsWithUrl(final String url, final Multimap<String, String> options, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        final String verb = "OPTIONS";
        return doPrepareRequestAndMaybeFollowLocation(verb, url, options, DEFAULT_EMPTY_QUERY, timeoutSec, clazz);
    }

    // COMMON

    private <T> T doPrepareRequestAndMaybeFollowLocation(final String verb, final String uri, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, null, options, optionsForFollow, timeoutSec, clazz, false);
    }

    private <T> T doPrepareRequestAndMaybeFollowLocation(final String verb, final String uri, final Multimap<String, String> options, final Multimap<String, String> optionsForFollow, final int timeoutSec, final Class<T> clazz, final boolean followLocation) throws KillBillClientException {
        return doPrepareRequestAndMaybeFollowLocation(verb, uri, null, options, optionsForFollow, timeoutSec, clazz, followLocation);
    }

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
                    } catch (JsonProcessingException e) {
                        throw new KillBillClientException(e);
                    }
                }
            } else {
                builder.setBody("{}");
            }
        }

        if (followLocation) {
            final Response response = executeAndWait(builder, timeoutSec, Response.class);
            if (response == null || response.getHeader("Location") == null) {
                // 404, bad request, ...
                return Response.class.isAssignableFrom(clazz) ? (T) response : null;
            } else {
                final String location = response.getHeader("Location");
                return doGetWithUrl(location, optionsForFollow, timeoutSec, clazz);
            }
        } else {
            return executeAndWait(builder, timeoutSec, clazz);
        }
    }

    private String getUniqueValue(final Multimap<String, String> options, final String key) {
        final Collection<String> values = options.get(key);
        if (values == null || values.size() == 0) {
            return null;
        } else {
            Preconditions.checkState(values.size() == 1, "You can only specify a unique value for " + key);
            return values.iterator().next();
        }
    }

    private <T> T executeAndWait(final BoundRequestBuilder builder, final int timeoutSec, final Class<T> clazz) throws KillBillClientException {
        Response response;
        final ListenableFuture<Response> futureStatus;
        try {
            futureStatus = builder.execute(new AsyncCompletionHandler<Response>() {
                @Override
                public Response onCompleted(final Response response) throws Exception {
                    return response;
                }
            });
            response = futureStatus.get(timeoutSec, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            throw new KillBillClientException(e);
        } catch (final ExecutionException e) {
            throw new KillBillClientException(e);
        } catch (final TimeoutException e) {
            throw new KillBillClientException(e);
        }

        if (response != null && response.getStatusCode() == 401) {
            throw new KillBillClientException(new IllegalArgumentException("Unauthorized - did you configure your RBAC and/or tenant credentials?"),
                                              response);
        } else if (response != null && (response.getStatusCode() == 404 || response.getStatusCode() == 204)) {
            // Return empty list for KillBillObjects instead of null for convenience
            if (Iterable.class.isAssignableFrom(clazz)) {
                for (final Constructor constructor : clazz.getConstructors()) {
                    if (constructor.getParameterTypes().length == 0) {
                        try {
                            return (T) constructor.newInstance();
                        } catch (InstantiationException e) {
                            return null;
                        } catch (IllegalAccessException e) {
                            return null;
                        } catch (InvocationTargetException e) {
                            return null;
                        }
                    }
                }
                return null;
            } else {
                return null;
            }
        } else if (response != null && response.getStatusCode() >= 400
                   && response.getStatusCode() != HTTP_PAYMENT_REQUIRED_STATUS) {
            final BillingException exception = deserializeResponse(response, BillingException.class);
            log.warn("Error " + response.getStatusCode() + " from Kill Bill: " + exception.getMessage());
            throw new KillBillClientException(exception, response);
        }

        // No deserialization required?
        if (Response.class.isAssignableFrom(clazz)) {
            return (T) response;
        }

        return deserializeResponse(response, clazz);
    }

    private <T> T deserializeResponse(final Response response, final Class<T> clazz) throws KillBillClientException {
        final T result;
        try {
            if (DEBUG) {
                final String content = response.getResponseBody();
                log.debug("Received: " + content);
                result = mapper.readValue(content, clazz);
            } else {
                InputStream in = null;
                try {
                    in = response.getResponseBodyAsStream();
                    result = mapper.readValue(in, clazz);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            log.warn("Failed to close http-client - provided InputStream: {}", e.getLocalizedMessage());
                        }
                    }
                }
            }
        } catch (final IOException e) {
            throw new KillBillClientException(e, response);
        }

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

    private BoundRequestBuilder getBuilderWithHeaderAndQuery(final String verb, final String url, @Nullable final String username, @Nullable final String password, final Multimap<String, String> options) {
        BoundRequestBuilder builder;

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

    private String getKBServerUrl(final String uri) throws KillBillClientException {
        try {
            final URI u = new URI(uri);
            if (u.isAbsolute()) {
                return uri;
            } else {
                return String.format("%s%s", kbServerUrl, uri);
            }
        } catch (URISyntaxException e) {
            throw new KillBillClientException(e);
        }
    }
}
