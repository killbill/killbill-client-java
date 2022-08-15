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

import org.joda.time.LocalDate;
import org.killbill.billing.client.model.gen.RolledUpUsage;
import org.killbill.billing.client.model.gen.SubscriptionUsageRecord;
import java.util.UUID;
import java.util.Map;

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
public class UsageApi {

    private final KillBillHttpClient httpClient;

    public UsageApi() {
        this(new KillBillHttpClient());
    }

    public UsageApi(final KillBillHttpClient httpClient) {
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

    public RolledUpUsage getAllUsage(final UUID subscriptionId, final LocalDate startDate, final LocalDate endDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        checkNotNull(subscriptionId, "Missing the required parameter 'subscriptionId' when calling getAllUsage");

        final String uri = "/1.0/kb/usages/{subscriptionId}"
          .replaceAll("\\{" + "subscriptionId" + "\\}", subscriptionId.toString());

        final Map<String, Collection<String>> queryParams = new HashMap<>(inputOptions.getQueryParams());
        if (startDate != null) {
            addToMapValues(queryParams, "startDate", List.of(String.valueOf(startDate)));
        }
        if (endDate != null) {
            addToMapValues(queryParams, "endDate", List.of(String.valueOf(endDate)));
        }
        if (pluginProperty != null) {
            addToMapValues(queryParams, "pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, RolledUpUsage.class, requestOptions);
    }

    public RolledUpUsage getUsage(final UUID subscriptionId, final String unitType, final LocalDate startDate, final LocalDate endDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        checkNotNull(subscriptionId, "Missing the required parameter 'subscriptionId' when calling getUsage");
        checkNotNull(unitType, "Missing the required parameter 'unitType' when calling getUsage");

        final String uri = "/1.0/kb/usages/{subscriptionId}/{unitType}"
          .replaceAll("\\{" + "subscriptionId" + "\\}", subscriptionId.toString())
          .replaceAll("\\{" + "unitType" + "\\}", unitType.toString());

        final Map<String, Collection<String>> queryParams = new HashMap<>(inputOptions.getQueryParams());
        if (startDate != null) {
            addToMapValues(queryParams, "startDate", List.of(String.valueOf(startDate)));
        }
        if (endDate != null) {
            addToMapValues(queryParams, "endDate", List.of(String.valueOf(endDate)));
        }
        if (pluginProperty != null) {
            addToMapValues(queryParams, "pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, RolledUpUsage.class, requestOptions);
    }

    public void recordUsage(final SubscriptionUsageRecord body, final RequestOptions inputOptions) throws KillBillClientException {
        checkNotNull(body, "Missing the required parameter 'body' when calling recordUsage");

        final String uri = "/1.0/kb/usages";


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPost(uri, body, requestOptions);
    }

}
