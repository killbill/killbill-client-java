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

import java.util.HashMap;
import java.util.Map;

import com.ning.billing.catalog.api.PriceListSet;
import com.ning.billing.jaxrs.json.BundleJsonNoSubscriptions;
import com.ning.billing.jaxrs.json.SubscriptionJsonNoEvents;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

public class KillBillSubscription extends KillBillBaseResource {

    protected BundleJsonNoSubscriptions createBundle(final String accountId, final String externalKey) throws Exception {
        final BundleJsonNoSubscriptions input = new BundleJsonNoSubscriptions(null, accountId, externalKey, null, null);
        String baseJson = mapper.writeValueAsString(input);
        Response response = httpClient.doPost(JaxrsResource.BUNDLES_PATH, baseJson, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String location = response.getHeader("Location");
        // Retrieves by Id based on Location returned
        response = httpClient.doGetWithUrl(location, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        baseJson = response.getResponseBody();
        final BundleJsonNoSubscriptions objFromJson = mapper.readValue(baseJson, BundleJsonNoSubscriptions.class);
        return objFromJson;
    }

    protected SubscriptionJsonNoEvents createSubscription(final String bundleId, final String productName, final String productCategory,
                                                          final String billingPeriod, final boolean waitCompletion) throws Exception {
        final SubscriptionJsonNoEvents input = new SubscriptionJsonNoEvents(null, bundleId, null, productName, productCategory,
                                                                            billingPeriod, PriceListSet.DEFAULT_PRICELIST_NAME,
                                                                            null, null, null);
        String baseJson = mapper.writeValueAsString(input);
        final Map<String, String> queryParams = waitCompletion ? getQueryParamsForCallCompletion(KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC + "") : DEFAULT_EMPTY_QUERY;
        Response response = httpClient.doPost(JaxrsResource.SUBSCRIPTIONS_PATH, baseJson, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC * 1000);
        if (null == response) {
            throw nullResponse();
        }
        final String location = response.getHeader("Location");
        // Retrieves by Id based on Location returned
        response = httpClient.doGetWithUrl(location, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        baseJson = response.getResponseBody();
        final SubscriptionJsonNoEvents objFromJson = mapper.readValue(baseJson, SubscriptionJsonNoEvents.class);
        return objFromJson;
    }

    protected Map<String, String> getQueryParamsForCallCompletion(final String timeoutSec) {
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_CALL_COMPLETION, "true");
        queryParams.put(JaxrsResource.QUERY_CALL_TIMEOUT, timeoutSec);
        return queryParams;
    }
}
