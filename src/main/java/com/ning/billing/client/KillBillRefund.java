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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ning.billing.jaxrs.json.InvoiceItemJsonSimple;
import com.ning.billing.jaxrs.json.RefundJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;

public class KillBillRefund extends KillBillBaseResource {

    protected static final String DEFAULT_CURRENCY = "USD";

    protected List<RefundJson> getRefundsForAccount(final String accountId) throws IOException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.REFUNDS;
        final Response response = httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final String baseJson = response.getResponseBody();
        final List<RefundJson> refunds = mapper.readValue(baseJson, new TypeReference<List<RefundJson>>() {});

        return refunds;
    }

    protected List<RefundJson> getRefundsForPayment(final String paymentId) throws IOException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + paymentId + "/" + JaxrsResource.REFUNDS;
        final Response response = httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final String baseJson = response.getResponseBody();
        final List<RefundJson> refunds = mapper.readValue(baseJson, new TypeReference<List<RefundJson>>() {});

        return refunds;
    }

    protected RefundJson createRefund(final String paymentId, final BigDecimal amount) throws IOException {
        return doCreateRefund(paymentId, amount, false, ImmutableMap.<String, BigDecimal>of());
    }

    protected RefundJson createRefundWithInvoiceAdjustment(final String paymentId, final BigDecimal amount) throws IOException {
        return doCreateRefund(paymentId, amount, true, ImmutableMap.<String, BigDecimal>of());
    }

    protected RefundJson createRefundWithInvoiceItemAdjustment(final String paymentId, final String invoiceItemId, final BigDecimal amount) throws IOException {
        final Map<String, BigDecimal> adjustments = new HashMap<String, BigDecimal>();
        adjustments.put(invoiceItemId, amount);
        return doCreateRefund(paymentId, amount, true, adjustments);
    }

    private RefundJson doCreateRefund(final String paymentId, final BigDecimal amount, final boolean adjusted, final Map<String, BigDecimal> itemAdjustments) throws IOException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + paymentId + "/" + JaxrsResource.REFUNDS;

        final List<InvoiceItemJsonSimple> adjustments = new ArrayList<InvoiceItemJsonSimple>();
        for (final String itemId : itemAdjustments.keySet()) {
            adjustments.add(new InvoiceItemJsonSimple(itemId, null, null, null, null, null, null, null, null, null, null,
                                                      itemAdjustments.get(itemId), null, null));
        }
        final RefundJson refundJson = new RefundJson(null, paymentId, amount, DEFAULT_CURRENCY, adjusted, null, null, adjustments, null);
        final String baseJson = mapper.writeValueAsString(refundJson);
        final Response response = httpClient.doPost(uri, baseJson, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final String locationCC = response.getHeader("Location");

        // Retrieves by Id based on Location returned
        final Response retrievedResponse = httpClient.doGetWithUrl(locationCC, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        final String retrievedBaseJson = retrievedResponse.getResponseBody();
        final RefundJson retrievedRefundJson = mapper.readValue(retrievedBaseJson, RefundJson.class);
        // Verify we have the adjusted items
        if (retrievedRefundJson.getAdjustments() != null) {
            final Set<String> allLinkedItemIds = new HashSet<String>(Collections2.transform(retrievedRefundJson.getAdjustments(), new Function<InvoiceItemJsonSimple, String>() {
                @Override
                public String apply(final InvoiceItemJsonSimple input) {
                    if (input != null) {
                        return input.getLinkedInvoiceItemId();
                    } else {
                        return null;
                    }
                }
            }));
            //assertEquals(allLinkedItemIds, itemAdjustments.keySet());
        }

        return retrievedRefundJson;
    }

    protected Map<String, String> getQueryParamsForCallCompletion(final String timeoutSec) {
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_CALL_COMPLETION, "true");
        queryParams.put(JaxrsResource.QUERY_CALL_TIMEOUT, timeoutSec);
        return queryParams;
    }
}
