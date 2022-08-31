/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2020 Equinix, Inc
 * Copyright 2014-2020 The Billing Project, LLC
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.Map.Entry;
import java.util.Objects;

public class RequestOptions {

    private final String requestId;

    private final String user, password;

    private final String createdBy, reason, comment;

    private final String tenantApiKey, tenantApiSecret;

    private final Map<String, String> headers;

    private final Map<String, Collection<String>> queryParams;

    private final Boolean followLocation;

    private final Map<String, Collection<String>> queryParamsForFollow;

    public RequestOptions(final String requestId,
                          final String user, final String password,
                          final String createdBy, final String reason, final String comment,
                          final String tenantApiKey, final String tenantApiSecret,
                          final Map<String, String> headers,
                          final Map<String, ? extends Collection<String>> queryParams,
                          final Boolean followLocation,
                          final Map<String, ? extends Collection<String>> queryParamsForFollow) {
        this.requestId = requestId;
        this.user = user;
        this.password = password;
        this.createdBy = createdBy;
        this.reason = reason;
        this.comment = comment;
        this.tenantApiKey = tenantApiKey;
        this.tenantApiSecret = tenantApiSecret;
        this.headers = (headers != null) ? new HashMap<>(headers) : Collections.emptyMap();
        this.queryParams = (queryParams != null) ? new HashMap<>(queryParams) : Collections.emptyMap();
        this.followLocation = followLocation;
        this.queryParamsForFollow = (queryParamsForFollow != null) ? new HashMap<>(queryParamsForFollow) : Collections.emptyMap();
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getReason() {
        return reason;
    }

    public String getComment() {
        return comment;
    }

    public String getTenantApiKey() {
        return tenantApiKey;
    }

    public String getTenantApiSecret() {
        return tenantApiSecret;
    }

    public Map<String, String> getHeaders() {
        return Map.copyOf(this.headers);
    }

    public Map<String, Collection<String>> getQueryParams() {
        return new HashMap<>(queryParams);
    }

    public Boolean getFollowLocation() {
        return followLocation;
    }

    public boolean shouldFollowLocation() {
        if (followLocation == null) {
            return false;
        }
        return followLocation;
    }

    public Map<String, Collection<String>> getQueryParamsForFollow() {
        return new HashMap<>(queryParamsForFollow);
    }

    public RequestOptionsBuilder extend() {
        final RequestOptionsBuilder builder = new RequestOptionsBuilder();
        builder.headers.putAll(this.headers);
        return builder
                .withRequestId(requestId)
                .withUser(user).withPassword(password)
                .withCreatedBy(createdBy).withReason(reason).withComment(comment)
                .withTenantApiKey(tenantApiKey).withTenantApiSecret(tenantApiSecret)
                .withQueryParams(queryParams)
                .withFollowLocation(followLocation).withQueryParamsForFollow(queryParamsForFollow);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final RequestOptions that = (RequestOptions) o;

        return Objects.equals(requestId, that.requestId)
               && Objects.equals(user, that.user)
               && Objects.equals(password, that.password)
               && Objects.equals(createdBy, that.createdBy)
               && Objects.equals(reason, that.reason)
               && Objects.equals(comment, that.comment)
               && Objects.equals(tenantApiKey, that.tenantApiKey)
               && Objects.equals(tenantApiSecret, that.tenantApiSecret)
               && Objects.equals(headers, that.headers)
               && Objects.equals(queryParams, that.queryParams)
               && Objects.equals(followLocation, that.followLocation)
               && Objects.equals(queryParamsForFollow, that.queryParamsForFollow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                requestId,
                user,
                password,
                createdBy,
                reason,
                comment,
                tenantApiKey,
                tenantApiSecret,
                headers,
                queryParams,
                followLocation,
                queryParamsForFollow);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RequestOptions {\n");
        sb.append("    requestId: ").append(requestId).append("\n");
        sb.append("    user: ").append(user).append("\n");
        // Don't print the password
        sb.append("    createdBy: ").append(createdBy).append("\n");
        sb.append("    reason: ").append(reason).append("\n");
        sb.append("    comment: ").append(comment).append("\n");
        sb.append("    tenantApiKey: ").append(tenantApiKey).append("\n");
        // Don't print the secret
        sb.append("    headers: ").append(headers).append("\n");
        sb.append("    queryParams: ").append(queryParams).append("\n");
        sb.append("    followLocation: ").append(followLocation).append("\n");
        sb.append("    queryParamsForFollow: ").append(queryParamsForFollow).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Helper method for creating a new builder
     * @return a new instance of RequestOptionsBuilder
     */
    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    /**
     * Helper method for creating an empty RequestOptions object.
     * @return an empty RequestOptions object.
     */
    public static RequestOptions empty() {
        return new RequestOptionsBuilder().build();
    }

    public static class RequestOptionsBuilder {

        private String requestId;

        private String user, password;

        private String createdBy, reason, comment;

        private String tenantApiKey, tenantApiSecret;

        private final Map<String, String> headers = new HashMap<>();

        private Map<String, ? extends Collection<String>> queryParams = new HashMap<>();

        private Boolean followLocation;

        private Map<String, ? extends Collection<String>> queryParamsForFollow = new HashMap<>();

        public RequestOptionsBuilder withRequestId(final String requestId) {
            this.requestId = requestId;
            return this;
        }

        public RequestOptionsBuilder withUser(final String user) {
            this.user = user;
            return this;
        }

        public RequestOptionsBuilder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public RequestOptionsBuilder withCreatedBy(final String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public RequestOptionsBuilder withReason(final String reason) {
            this.reason = reason;
            return this;
        }

        public RequestOptionsBuilder withComment(final String comment) {
            this.comment = comment;
            return this;
        }

        public RequestOptionsBuilder withTenantApiKey(final String tenantApiKey) {
            this.tenantApiKey = tenantApiKey;
            return this;
        }

        public RequestOptionsBuilder withTenantApiSecret(final String tenantApiSecret) {
            this.tenantApiSecret = tenantApiSecret;
            return this;
        }

        public RequestOptionsBuilder withHeader(final String header, final String value) {
            this.headers.put(header, value);
            return this;
        }

        public RequestOptionsBuilder withQueryParams(final Map<String, ? extends Collection<String>> queryParams) {
            this.queryParams = toMutableMapValues(queryParams);
            return this;
        }

        public RequestOptionsBuilder withFollowLocation(final Boolean followLocation) {
            this.followLocation = followLocation;
            return this;
        }

        public RequestOptionsBuilder withQueryParamsForFollow(final Map<String, ? extends Collection<String>> queryParamsForFollow) {
            this.queryParamsForFollow = toMutableMapValues(queryParamsForFollow);
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(requestId, user, password, createdBy, reason, comment, tenantApiKey, tenantApiSecret,
                                      headers, queryParams, followLocation, queryParamsForFollow);
        }

        /**
         * Make sure that map collection values are mutable. Used to rebuild method parameter in
         * {@link #withQueryParams(Map)} and {@link #withQueryParamsForFollow(Map)} .
         *
         * See {@code TestRequestOptions} for why we may need this.
         */
        Map<String, ? extends Collection<String>> toMutableMapValues(final Map<String, ? extends Collection<String>> map) {
            final Map<String, Collection<String>> result = new HashMap<>();

            for (final Entry<String, ? extends Collection<String>> entry : map.entrySet()) {
                result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }

            return result;
        }
    }
}