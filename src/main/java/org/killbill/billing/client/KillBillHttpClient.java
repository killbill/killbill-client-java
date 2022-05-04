/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2022 Equinix, Inc
 * Copyright 2014-2022 The Billing Project, LLC
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
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.killbill.billing.client.model.KillBillObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
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

    private final boolean DEBUG = Boolean.parseBoolean(System.getProperty("org.killbill.client.debug", "false"));

    private final String kbServerUrl;
    private final String username;
    private final String password;
    private final String apiKey;
    private final String apiSecret;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final int defaultReadTimeoutSec;

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
     */
    public KillBillHttpClient(final String kbServerUrl,
                              final String username,
                              final String password,
                              final String apiKey,
                              final String apiSecret,
                              final String proxyHost,
                              final Integer proxyPort,
                              final Integer connectTimeOut,
                              final Integer readTimeOut) {
        this(kbServerUrl, username, password, apiKey, apiSecret, proxyHost, proxyPort, connectTimeOut, readTimeOut, true, null);
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
                              final Boolean strictSSL,
                              final String SSLProtocol) {
        this.kbServerUrl = kbServerUrl;
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;

        final HttpClient.Builder builder;
        try {
            builder = HttpClient.newBuilder()
                                .sslContext(SslUtils.getInstance().getSSLContext(!strictSSL, SSLProtocol))
                                .connectTimeout(Duration.of(MoreObjects.firstNonNull(connectTimeOut, DEFAULT_HTTP_TIMEOUT_SEC * 1000), ChronoUnit.MILLIS));
        } catch (final GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        if (proxyHost != null && proxyPort != null) {
            builder.proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));
        }

        httpClient = builder.build();

        if (readTimeOut != null) {
            int timeoutSec = (int) TimeUnit.MILLISECONDS.toSeconds(readTimeOut);
            if (TimeUnit.SECONDS.toMillis(timeoutSec) != readTimeOut) {
                timeoutSec += 1;
            }
            this.defaultReadTimeoutSec = timeoutSec;
        } else {
            this.defaultReadTimeoutSec = DEFAULT_HTTP_TIMEOUT_SEC;
        }

        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }

    public KillBillHttpClient(final String kbServerUrl, final String username, final String password, final String apiKey, final String apiSecret) {
        this(kbServerUrl, username, password, apiKey, apiSecret, null, null, null, null);
    }

    public KillBillHttpClient(final String kbServerUrl, final String username, final String password, final String apiKey, final String apiSecret, final String proxyHost, final Integer proxyPort) {
        this(kbServerUrl, username, password, apiKey, apiSecret, proxyHost, proxyPort, null, null);
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
    }

    // POST
    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doPost(final String uri, final Object body, final RequestOptions requestOptions) throws KillBillClientException {
        return doPost(uri, body, HttpResponse.class, requestOptions);
    }

    public <T> T doPost(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions) throws KillBillClientException {
        return doPost(uri, body, returnClass, requestOptions, this.defaultReadTimeoutSec);
    }

    public <T> T doPost(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "POST";
        return doPrepareRequest(verb, uri, body, returnClass, requestOptions, timeoutSec);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doPut(final String uri, final Object body, final RequestOptions options) throws KillBillClientException {
        return doPut(uri, body, HttpResponse.class, options);
    }

    public <T> T doPut(final String uri, final Object body, final Class<T> returnClass, final RequestOptions options) throws KillBillClientException {
        return doPut(uri, body, returnClass, options, this.defaultReadTimeoutSec);
    }

    public <T> T doPut(final String uri, final Object body, final Class<T> returnClass, final RequestOptions options, final int timeoutSec) throws KillBillClientException {
        final String verb = "PUT";
        return doPrepareRequest(verb, uri, body, returnClass, options, timeoutSec);
    }

    // DELETE
    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doDelete(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doDelete(uri, null, HttpResponse.class, requestOptions);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doDelete(final String uri, final Object body, final RequestOptions options) throws KillBillClientException {
        return doDelete(uri, body, HttpResponse.class, options);
    }

    public <T> T doDelete(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions) throws KillBillClientException {
        return doDelete(uri, body, returnClass, requestOptions, this.defaultReadTimeoutSec);
    }

    public <T> T doDelete(final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "DELETE";
        return doPrepareRequest(verb, uri, body, returnClass, requestOptions, timeoutSec);
    }

    // GET
    public HttpResponse<InputStream> doGet(final String uri, final OutputStream outputStream, final RequestOptions requestOptions) throws KillBillClientException {
        final String verb = "GET";
        return doPrepareRequest(verb, uri, null, outputStream, requestOptions, this.defaultReadTimeoutSec);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doGet(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doGet(uri, HttpResponse.class, requestOptions);
    }

    public <T> T doGet(final String uri, final Class<T> returnClass, final RequestOptions requestOptions) throws KillBillClientException {
        return doGet(uri, returnClass, requestOptions, this.defaultReadTimeoutSec);
    }

    public <T> T doGet(final String uri, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "GET";
        return doPrepareRequest(verb, uri, null, returnClass, requestOptions, timeoutSec);
    }

    // HEAD
    public HttpResponse<InputStream> doHead(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doHead(uri, requestOptions, this.defaultReadTimeoutSec);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doHead(final String uri, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "HEAD";
        return doPrepareRequest(verb, uri, null, HttpResponse.class, requestOptions, timeoutSec);
    }

    // OPTIONS
    public HttpResponse<InputStream> doOptions(final String uri, final RequestOptions requestOptions) throws KillBillClientException {
        return doOptions(uri, requestOptions, this.defaultReadTimeoutSec);
    }

    @SuppressWarnings("unchecked")
    public HttpResponse<InputStream> doOptions(final String uri, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final String verb = "OPTIONS";
        return doPrepareRequest(verb, uri, null, HttpResponse.class, requestOptions, timeoutSec);
    }

    // COMMON
    @SuppressWarnings("unchecked")
    private HttpResponse<InputStream> doPrepareRequest(final String verb, final String uri, final Object body, final OutputStream outputStream, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        return doPrepareRequestInternal(verb, uri, body, HttpResponse.class, outputStream, requestOptions, timeoutSec);
    }

    private <T> T doPrepareRequest(final String verb, final String uri, final Object body, final Class<T> returnClass, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        return doPrepareRequestInternal(verb, uri, body, returnClass, null, requestOptions, timeoutSec);
    }

    private <T> T doPrepareRequestInternal(final String verb, final String uri, final Object body, final Class<T> returnClass, final OutputStream outputStream, final RequestOptions requestOptions, final int timeoutSec) throws KillBillClientException {
        final HttpRequest.Builder builder = getBuilderWithHeaderAndQuery(verb, uri, requestOptions);

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
            String bodyString = "{}";
            if (body != null) {
                if (body instanceof String) {
                    bodyString = (String) body;
                } else {
                    try {
                        bodyString = mapper.writeValueAsString(body);
                    } catch (final JsonProcessingException e) {
                        throw new KillBillClientException(e);
                    }
                }
            }
            builder.method(verb, BodyPublishers.ofString(bodyString));
        }

        final HttpResponse<InputStream> response = outputStream != null ? doRequest(builder, outputStream, timeoutSec) : doRequest(builder, timeoutSec);
        if (response.statusCode() == 404 || response.statusCode() == 204) {
            return createEmptyResult(returnClass);
        }

        if (requestOptions.shouldFollowLocation() && response.headers().firstValue("Location").isPresent()) {
            final String location = response.headers().firstValue("Location").get();
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

    private static void addHeader(final HttpRequest.Builder builder, final String headerName, final String value) {
        if (value != null) {
            builder.header(headerName, value);
        }
    }

    private HttpResponse<InputStream> doRequest(final HttpRequest.Builder builder, final OutputStream outputStream, final int timeoutSec) throws KillBillClientException {
        final HttpResponse<InputStream> response = doRequest(builder, timeoutSec);
        try {
            response.body().transferTo(outputStream);
        } catch (final IOException e) {
            throw new KillBillClientException(e);
        }
        return response;
    }

    private HttpResponse<InputStream> doRequest(final HttpRequest.Builder builder, final int timeoutSec) throws KillBillClientException {
        builder.timeout(Duration.of(timeoutSec, ChronoUnit.SECONDS));

        try {
            return httpClient.send(builder.build(), BodyHandlers.ofInputStream());
        } catch (final IOException | InterruptedException e) {
            throw new KillBillClientException(e);
        }
    }

    @VisibleForTesting
    void throwExceptionOnResponseError(final HttpResponse<InputStream> response) throws KillBillClientException {
        if (response.statusCode() == 401) {
            throw new KillBillClientException(
                    new IllegalArgumentException("Unauthorized - did you configure your RBAC and/or tenant credentials?"), response);
        }
        if (response.statusCode() >= 400) {
            final BillingException exception = deserializeResponse(response, BillingException.class);
            if (exception != null) {
                log.warn("Error " + response.statusCode() + " from Kill Bill: " + exception.getMessage());
                throw new KillBillClientException(exception, response);
            } else {
                log.warn("Error " + response.statusCode() + " from Kill Bill");
                throw new KillBillClientException(response);
            }
        }
    }

    @VisibleForTesting
    <T> T deserializeResponse(final HttpResponse<InputStream> response, final Class<T> clazz) throws KillBillClientException {
        // No deserialization required
        if (HttpResponse.class.isAssignableFrom(clazz)) {
            return clazz.cast(response);
        }

        // Nothing to deserialize
        if (response.headers().firstValueAsLong("Content-Length").orElse(-1) == 0L) {
            return createEmptyResult(clazz);
        }

        final T result = unmarshalResponse(response, clazz);
        if (KillBillObjects.class.isAssignableFrom(clazz)) {
            final KillBillObjects<?> objects = ((KillBillObjects<?>) result);
            final Optional<String> paginationCurrentOffset = response.headers().firstValue(JaxrsResource.HDR_PAGINATION_CURRENT_OFFSET);
            paginationCurrentOffset.ifPresent(s -> objects.setPaginationCurrentOffset(Integer.parseInt(s)));

            final Optional<String> paginationNextOffset = response.headers().firstValue(JaxrsResource.HDR_PAGINATION_NEXT_OFFSET);
            paginationNextOffset.ifPresent(s -> objects.setPaginationNextOffset(Integer.parseInt(s)));

            final Optional<String> paginationTotalNbRecords = response.headers().firstValue(JaxrsResource.HDR_PAGINATION_TOTAL_NB_RECORDS);
            paginationTotalNbRecords.ifPresent(s -> objects.setPaginationTotalNbRecords(Integer.parseInt(s)));

            final Optional<String> paginationMaxNbRecords = response.headers().firstValue(JaxrsResource.HDR_PAGINATION_MAX_NB_RECORDS);
            paginationMaxNbRecords.ifPresent(s -> objects.setPaginationMaxNbRecords(Integer.parseInt(s)));

            final Optional<String> paginationNextPageUri = response.headers().firstValue(JaxrsResource.HDR_PAGINATION_NEXT_PAGE_URI);
            paginationNextPageUri.ifPresent(objects::setPaginationNextPageUri);

            objects.setKillBillHttpClient(this);
        }

        return result;
    }

    private static <T> T createEmptyResult(final Class<T> clazz) {// Return empty list for KillBillObjects instead of null for convenience
        if (Iterable.class.isAssignableFrom(clazz)) {
            for (final Constructor<?> constructor : clazz.getConstructors()) {
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

    @SuppressWarnings("unchecked")
    private <T> T unmarshalResponse(final HttpResponse<InputStream> response, final Class<T> clazz) throws KillBillClientException {
        final T result;

        final boolean requiresMapper = (String.class != clazz);
        try {
            if (DEBUG || !requiresMapper) {
                final String content = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                log.debug("Received: " + content);
                result = requiresMapper ? mapper.readValue(content, clazz) : (T) content;
            } else {
                InputStream in = null;
                try {
                    in = response.body();
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

    private HttpRequest.Builder getBuilderWithHeaderAndQuery(final String verb, final String url, final RequestOptions requestOptions) throws KillBillClientException {
        final HttpRequest.Builder builder = HttpRequest.newBuilder()
                                                       .uri(getURI(url, requestOptions.getQueryParams()))
                                                       .method(verb, BodyPublishers.noBody()); // Body overridden later on

        builder.header("User-Agent", USER_AGENT);

        final String usernameMaybeOverridden = requestOptions.getUser() != null ? requestOptions.getUser() : this.username;
        final String passwordMaybeOverridden = requestOptions.getPassword() != null ? requestOptions.getPassword() : this.password;
        if (usernameMaybeOverridden != null || passwordMaybeOverridden != null) {
            builder.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((usernameMaybeOverridden + ":" + passwordMaybeOverridden).getBytes(StandardCharsets.UTF_8)));
        }

        for (final Entry<String, String> header : requestOptions.getHeaders().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }

        return builder;
    }

    private URI getURI(final String url, final Multimap<String, String> queryParams) throws KillBillClientException {
        try {
            if (url == null) {
                throw new URISyntaxException("(null)", "HttpClient URL misconfigured");
            }

            URI u = new URI(url);
            if (!u.isAbsolute()) {
                u = new URI(String.format("%s%s", this.kbServerUrl, url));
            }

            if (queryParams.isEmpty()) {
                return u;
            }

            final StringBuilder sb = new StringBuilder(u.getQuery() == null ? "" : u.getQuery());
            queryParams.keySet().forEach(key -> {
                for (final String value : queryParams.get(key)) {
                    if (sb.length() > 0) {
                        sb.append('&');
                    }
                    sb.append(UTF8UrlEncoder.encode(key));
                    sb.append('=');
                    sb.append(UTF8UrlEncoder.encode(value));
                }
            });

            final String query = sb.toString();

            return new URI(buildURI(u.getScheme(),
                                    u.getUserInfo(),
                                    u.getHost(),
                                    u.getPort(),
                                    u.getAuthority(),
                                    u.getRawPath(), // Keep the raw path (don't decode it)
                                    query,
                                    u.getFragment()));
        } catch (final URISyntaxException e) {
            throw new KillBillClientException(e);
        }
    }

    private String buildURI(final String scheme,
                            final String userInfo,
                            final String host,
                            final int port,
                            final String authority,
                            final String path,
                            final String query,
                            final String fragment) {
        final StringBuilder sb = new StringBuilder();
        if (scheme != null) {
            sb.append(scheme);
            sb.append(':');
        }
        if (host != null) {
            sb.append("//");
            if (userInfo != null) {
                sb.append(userInfo);
                sb.append('@');
            }
            final boolean needBrackets = ((host.indexOf(':') >= 0)
                                          && !host.startsWith("[")
                                          && !host.endsWith("]"));
            if (needBrackets) {
                sb.append('[');
            }
            sb.append(host);
            if (needBrackets) {
                sb.append(']');
            }
            if (port != -1) {
                sb.append(':');
                sb.append(port);
            }
        } else if (authority != null) {
            sb.append("//");
            sb.append(authority);
        }
        if (path != null) {
            sb.append(path);
        }
        if (query != null) {
            sb.append('?');
            sb.append(query);
        }
        if (fragment != null) {
            sb.append('#');
            sb.append(fragment);
        }
        return sb.toString();
    }
}
