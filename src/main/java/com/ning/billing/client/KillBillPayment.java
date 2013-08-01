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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ning.billing.jaxrs.json.AccountJson;
import com.ning.billing.jaxrs.json.InvoiceJsonSimple;
import com.ning.billing.jaxrs.json.PaymentJsonSimple;
import com.ning.billing.jaxrs.json.PaymentJsonWithBundleKeys;
import com.ning.billing.jaxrs.json.PaymentMethodJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;

public class KillBillPayment extends KillBillBaseResource {

    protected PaymentJsonSimple getPayment(final String paymentId) throws IOException {
        return doGetPayment(paymentId, DEFAULT_EMPTY_QUERY, PaymentJsonSimple.class);
    }

    protected PaymentJsonWithBundleKeys getPaymentWithRefundsAndChargebacks(final String paymentId) throws IOException {
        return doGetPayment(paymentId, ImmutableMap.<String, String>of(JaxrsResource.QUERY_PAYMENT_WITH_REFUNDS_AND_CHARGEBACKS, "true"), PaymentJsonWithBundleKeys.class);
    }

    protected <T extends PaymentJsonSimple> T doGetPayment(final String paymentId, final Map<String, String> queryParams, final Class<T> clazz) throws IOException {
        final String paymentURI = JaxrsResource.PAYMENTS_PATH + "/" + paymentId;

        final Response paymentResponse = httpClient.doGet(paymentURI, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final T paymentJsonSimple = mapper.readValue(paymentResponse.getResponseBody(), clazz);

        return paymentJsonSimple;
    }

    protected PaymentMethodJson getPaymentMethod(final String paymentMethodId) throws IOException {
        final String paymentMethodURI = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;
        final Response paymentMethodResponse = httpClient.doGet(paymentMethodURI, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final PaymentMethodJson paymentMethodJson = mapper.readValue(paymentMethodResponse.getResponseBody(), PaymentMethodJson.class);

        return paymentMethodJson;
    }

    protected PaymentMethodJson getPaymentMethodWithPluginInfo(final String paymentMethodId) throws IOException {
        final String paymentMethodURI = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;

        final Map<String, String> queryPaymentMethods = new HashMap<String, String>();
        final Response paymentMethodResponse = httpClient.doGet(paymentMethodURI, queryPaymentMethods, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final PaymentMethodJson paymentMethodJson = mapper.readValue(paymentMethodResponse.getResponseBody(), PaymentMethodJson.class);

        return paymentMethodJson;
    }

    protected void deletePaymentMethod(final String paymentMethodId, final Boolean deleteDefault) throws IOException {
        final String paymentMethodURI = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;
        final Response response = httpClient.doDelete(paymentMethodURI, ImmutableMap.<String, String>of(JaxrsResource.QUERY_DELETE_DEFAULT_PM_WITH_AUTO_PAY_OFF, deleteDefault.toString()), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected List<PaymentJsonSimple> getPaymentsForAccount(final String accountId) throws IOException {
        final String paymentsURI = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENTS;
        final Response paymentsResponse = httpClient.doGet(paymentsURI, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        final String paymentsBaseJson = paymentsResponse.getResponseBody();

        final List<PaymentJsonSimple> paymentJsonSimples = mapper.readValue(paymentsBaseJson, new TypeReference<List<PaymentJsonSimple>>() {});

        return paymentJsonSimples;
    }

    protected List<PaymentJsonSimple> getPaymentsForInvoice(final String invoiceId) throws IOException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId + "/" + JaxrsResource.PAYMENTS;
        final Response response = httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final String baseJson = response.getResponseBody();
        final List<PaymentJsonSimple> objFromJson = mapper.readValue(baseJson, new TypeReference<List<PaymentJsonSimple>>() {});

        return objFromJson;
    }

    protected void payAllInvoices(final AccountJson accountJson, final Boolean externalPayment) throws IOException {
        final PaymentJsonSimple payment = new PaymentJsonSimple(null, null, accountJson.getAccountId(), null, null, null, null,
                                                                null, 0, null, null, null, null, null, null, null);
        final String postJson = mapper.writeValueAsString(payment);

        final String uri = JaxrsResource.INVOICES_PATH + "/" + JaxrsResource.PAYMENTS;
        httpClient.doPost(uri, postJson, ImmutableMap.<String, String>of("externalPayment", externalPayment.toString()), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected List<PaymentJsonSimple> createInstaPayment(final AccountJson accountJson, final InvoiceJsonSimple invoice) throws IOException {
        final PaymentJsonSimple payment = new PaymentJsonSimple(invoice.getAmount(), BigDecimal.ZERO, accountJson.getAccountId(),
                                                                invoice.getInvoiceId(), null, null, null, null, 0, null, null, null, null, null, null, null);
        final String postJson = mapper.writeValueAsString(payment);

        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoice.getInvoiceId() + "/" + JaxrsResource.PAYMENTS;
        httpClient.doPost(uri, postJson, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        return getPaymentsForInvoice(invoice.getInvoiceId());
    }

    protected List<PaymentJsonSimple> createExternalPayment(final AccountJson accountJson, final String invoiceId, final BigDecimal paidAmount) throws IOException {
        final PaymentJsonSimple payment = new PaymentJsonSimple(paidAmount, BigDecimal.ZERO, accountJson.getAccountId(),
                                                                invoiceId, null, null, null, null, 0,
                                                                null, null, null, null, null, null, null);
        final String postJson = mapper.writeValueAsString(payment);

        final String paymentURI = JaxrsResource.INVOICES_PATH + "/" + invoiceId + "/" + JaxrsResource.PAYMENTS;
        final Response paymentResponse = httpClient.doPost(paymentURI, postJson, ImmutableMap.<String, String>of("externalPayment", "true"), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        return getPaymentsForInvoice(invoiceId);
    }
}
