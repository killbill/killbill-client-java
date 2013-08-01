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
import java.util.UUID;

import org.joda.time.DateTime;

import com.ning.billing.jaxrs.json.CreditJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

public class KillBillCredit extends KillBillBaseResource {

    protected CreditJson createCreditForAccount(final String accountId, final BigDecimal creditAmount,
                                                final DateTime requestedDate, final DateTime effectiveDate) throws IOException {
        return createCreditForInvoice(accountId, null, creditAmount, requestedDate, effectiveDate);
    }

    protected CreditJson createCreditForInvoice(final String accountId, final String invoiceId, final BigDecimal creditAmount,
                                                final DateTime requestedDate, final DateTime effectiveDate) throws IOException {
        final CreditJson input = new CreditJson(creditAmount, invoiceId, UUID.randomUUID().toString(),
                                                requestedDate, effectiveDate,
                                                UUID.randomUUID().toString(), accountId,
                                                null);
        final String jsonInput = mapper.writeValueAsString(input);

        // Create the credit
        Response response = httpClient.doPost(JaxrsResource.CREDITS_PATH, jsonInput, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final String location = response.getHeader("Location");

        // Retrieves by Id based on Location returned
        response = httpClient.doGetWithUrl(location, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        return mapper.readValue(response.getResponseBody(), CreditJson.class);
    }
}
