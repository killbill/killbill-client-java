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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by arodrigues on 5/24/16.
 */
public class RequestOptions {

    private final String requestId;

    private final String user, password;

    private final String createdBy, reason, comment;

    private final String tenantApiKey, tenantApiSecret;

    private final ImmutableMap<String, String> headers;

    private final ImmutableMultimap<String, String> queryParams;

    private final Boolean followLocation;

    private final Multimap<String, String> queryParamsForFollow;

    public RequestOptions(final String requestId, final String user, final String password, final String createdBy,
                          final String reason, final String comment, final String tenantApiKey, final String tenantApiSecret,
                          final Map<String, String> headers, final Multimap<String, String> queryParams,
                          final Boolean followLocation, final Multimap<String, String> queryParamsForFollow) {
        this.requestId = requestId;
        this.user = user;
        this.password = password;
        this.createdBy = createdBy;
        this.reason = reason;
        this.comment = comment;
        this.tenantApiKey = tenantApiKey;
        this.tenantApiSecret = tenantApiSecret;
        this.headers = (headers != null) ? ImmutableMap.copyOf(headers) : ImmutableMap.<String, String>of();
        this.queryParams = (queryParams != null) ? ImmutableMultimap.copyOf(queryParams) : ImmutableMultimap.<String, String>of();
        this.followLocation = followLocation;
        this.queryParamsForFollow = ImmutableMultimap.copyOf(queryParamsForFollow);
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

    public ImmutableMap<String, String> getHeaders() {
        return this.headers;
    }

    public ImmutableMultimap<String, String> getQueryParams() {
        return queryParams;
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

    public Multimap<String, String> getQueryParamsForFollow() {
        return queryParamsForFollow;
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

    /**
     * Helper method for creating a new builder
     * @return a new instance of RequestOptionsBuilder
     */
    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    public static class RequestOptionsBuilder {

        private String requestId;

        private String user, password;

        private String createdBy, reason, comment;

        private String tenantApiKey, tenantApiSecret;

        private final Map<String, String> headers = new HashMap<String, String>();

        private Multimap<String, String> queryParams = HashMultimap.<String, String>create();

        private Boolean followLocation;

        private Multimap<String, String> queryParamsForFollow = HashMultimap.<String, String>create();

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

        public RequestOptionsBuilder withQueryParams(final Multimap<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public RequestOptionsBuilder withFollowLocation(final Boolean followLocation) {
            this.followLocation = followLocation;
            return this;
        }

        public RequestOptionsBuilder withQueryParamsForFollow(final Multimap<String, String> queryParamsForFollow) {
            this.queryParamsForFollow = queryParamsForFollow;
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(requestId, user, password, createdBy, reason, comment, tenantApiKey, tenantApiSecret,
                                      headers, queryParams, followLocation, queryParamsForFollow);
        }
    }
}
