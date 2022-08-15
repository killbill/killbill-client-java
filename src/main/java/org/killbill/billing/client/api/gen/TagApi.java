/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2021 Equinix, Inc
 * Copyright 2014-2021 The Billing Project, LLC
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


package org.killbill.billing.client.api.gen;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import org.killbill.billing.client.model.gen.AuditLog;
import org.killbill.billing.client.model.gen.Tag;
import java.util.UUID;
import org.killbill.billing.client.model.AuditLogs;
import java.util.List;
import org.killbill.billing.client.model.Tags;
import org.killbill.billing.util.api.AuditLevel;

import org.killbill.billing.client.Converter;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;


/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
public class TagApi {

    private final KillBillHttpClient httpClient;

    public TagApi() {
        this(new KillBillHttpClient());
    }

    public TagApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private <K, V> void addToMapValues(final Map<K, Collection<V>> map, final K key, final Collection<V> values) {
        if (map.containsKey(key)) {
            map.get(key).addAll(values);
        } else {
            map.put(key, values);
        }
    }

    public static <T> T checkNotNull(final T reference, final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public AuditLogs getTagAuditLogsWithHistory(final UUID tagId, final RequestOptions inputOptions) throws KillBillClientException {
        checkNotNull(tagId, "Missing the required parameter 'tagId' when calling getTagAuditLogsWithHistory");

        final String uri = "/1.0/kb/tags/{tagId}/auditLogsWithHistory"
          .replaceAll("\\{" + "tagId" + "\\}", tagId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, AuditLogs.class, requestOptions);
    }

    public Tags getTags(final RequestOptions inputOptions) throws KillBillClientException {
        return getTags(Long.valueOf(0), Long.valueOf(100), AuditLevel.NONE, inputOptions);
    }

    public Tags getTags(final Long offset, final Long limit, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {

        final String uri = "/1.0/kb/tags/pagination";

        final Map<String, Collection<String>> queryParams = new HashMap<>(inputOptions.getQueryParams());
        if (offset != null) {
            addToMapValues(queryParams, "offset", List.of(String.valueOf(offset)));
        }
        if (limit != null) {
            addToMapValues(queryParams, "limit", List.of(String.valueOf(limit)));
        }
        if (audit != null) {
            addToMapValues(queryParams, "audit", List.of(String.valueOf(audit)));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

    public Tags searchTags(final String searchKey, final RequestOptions inputOptions) throws KillBillClientException {
        return searchTags(searchKey, Long.valueOf(0), Long.valueOf(100), AuditLevel.NONE, inputOptions);
    }

    public Tags searchTags(final String searchKey, final Long offset, final Long limit, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        checkNotNull(searchKey, "Missing the required parameter 'searchKey' when calling searchTags");

        final String uri = "/1.0/kb/tags/search/{searchKey}"
          .replaceAll("\\{" + "searchKey" + "\\}", searchKey.toString());

        final Map<String, Collection<String>> queryParams = new HashMap<>(inputOptions.getQueryParams());
        if (offset != null) {
            addToMapValues(queryParams, "offset", List.of(String.valueOf(offset)));
        }
        if (limit != null) {
            addToMapValues(queryParams, "limit", List.of(String.valueOf(limit)));
        }
        if (audit != null) {
            addToMapValues(queryParams, "audit", List.of(String.valueOf(audit)));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

}
