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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.ning.billing.catalog.api.Currency;
import com.ning.billing.jaxrs.json.InvoiceItemJsonSimple;
import com.ning.billing.jaxrs.json.InvoiceJsonWithItems;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KillBillInvoiceItem extends KillBillInvoice {

    private String bundleId;
    private String description;
    private String invoiceItemId;
    private String linkedInvoiceItemId;
    private String phaseName;
    private String planName;
    private String subscriptionId;
    private Date startDate;
    private Date endDate;
    private Date requestedDate;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(String invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public String getLinkedInvoiceItemId() {
        return linkedInvoiceItemId;
    }

    public void setLinkedInvoiceItemId(String linkedInvoiceItemId) {
        this.linkedInvoiceItemId = linkedInvoiceItemId;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public InvoiceItemJsonSimple toInvoiceItemJsonSimple() {
        LocalDate sd = (null == startDate) ? null : new LocalDate(startDate.getTime());
        LocalDate ed = (null == endDate) ? null : new LocalDate(endDate.getTime());
        return new InvoiceItemJsonSimple(invoiceItemId, invoiceId, linkedInvoiceItemId, accountId, bundleId, subscriptionId,
                                         planName, phaseName, description, sd, ed, amount, Currency.getDefaultCurrency(), null);
    }

    public InvoiceJsonWithItems createExternalCharge(final Date requestedDate) throws KillBillException, IOException {
        if (null == getInvoiceId() || getInvoiceId().trim().length() == 0) {
            return doCreateExternalCharge(toInvoiceItemJsonSimple(), requestedDate, JaxrsResource.CHARGES_PATH);
        } else {
            return doCreateExternalCharge(toInvoiceItemJsonSimple(), requestedDate, JaxrsResource.INVOICES_PATH + "/" + getInvoiceId() + "/" + JaxrsResource.CHARGES);
        }
    }

    public static KillBillInvoiceItem toInvoiceItem(InvoiceItemJsonSimple json) {
        if (null == json) {
            return null;
        }
        KillBillInvoiceItem item = new KillBillInvoiceItem();
        item.setAmount(json.getAmount());
        item.setBundleId(json.getBundleId());
        item.setCurrency(json.getCurrency().toString());
        item.setDescription(json.getDescription());
        item.setEndDate(json.getEndDate().toDate());
        item.setInvoiceItemId(json.getInvoiceItemId());
        item.setLinkedInvoiceItemId(json.getLinkedInvoiceItemId());
        item.setPhaseName(json.getPhaseName());
        item.setPlanName(json.getPlanName());
        item.setStartDate(json.getStartDate().toDate());
        item.setSubscriptionId(json.getSubscriptionId());
        return item;
    }

    public static void adjustInvoiceItem(final String accountId, final String invoiceId, final String invoiceItemId,
                                         final Date requestedDate, final BigDecimal amount, final Currency currency) throws IOException, KillBillException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId;
        final Map<String, String> queryParams = new HashMap<String, String>();
        if (requestedDate != null) {
            DateTime dt = new DateTime(requestedDate.getTime());
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, dt.toDateTimeISO().toString());
        }
        final InvoiceItemJsonSimple adjustment = new InvoiceItemJsonSimple(invoiceItemId, invoiceId, null, accountId,
                                                                           null, null, null, null, null, null, null, amount, currency, null);
        ObjectMapper mapper = new ObjectMapper();
        final String adjustmentJson = mapper.writeValueAsString(adjustment);
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doPost(uri, adjustmentJson, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
    }

    public static InvoiceJsonWithItems createExternalCharge(final String accountId, final BigDecimal amount, final String bundleId,
                                                            final Currency currency, final Date requestedDate) throws Exception {
        return doCreateExternalCharge(accountId, null, bundleId, amount, currency, requestedDate, JaxrsResource.CHARGES_PATH);
    }

    public static InvoiceJsonWithItems createExternalChargeForInvoice(final String accountId, final String invoiceId, final String bundleId,
                                                                      final BigDecimal amount, final Currency currency, final Date requestedDate) throws Exception {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId + "/" + JaxrsResource.CHARGES;
        return doCreateExternalCharge(accountId, invoiceId, bundleId, amount, currency, requestedDate, uri);
    }

    private static InvoiceJsonWithItems doCreateExternalCharge(final String accountId, final String invoiceId, final String bundleId, final BigDecimal amount,
                                                               final Currency currency, final Date requestedDate, final String uri) throws IOException, KillBillException {
        final InvoiceItemJsonSimple externalCharge = new InvoiceItemJsonSimple(null, invoiceId, null, accountId, bundleId, null, null, null, null, null, null, amount, currency, null);
        return doCreateExternalCharge(externalCharge, requestedDate, uri);
    }

    private static InvoiceJsonWithItems doCreateExternalCharge(final InvoiceItemJsonSimple externalCharge, final Date requestedDate, final String uri) throws IOException, KillBillException {
        final Map<String, String> queryParams = new HashMap<String, String>();
        if (requestedDate != null) {
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, (new DateTime(requestedDate.getTime())).toDateTimeISO().toString());
        }
        ObjectMapper mapper = new ObjectMapper();
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final String externalChargeJson = mapper.writeValueAsString(externalCharge);
        Response response = httpClient.doPost(uri, externalChargeJson, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String location = response.getHeader("Location");
        final Map<String, String> queryParamsForInvoice = new HashMap<String, String>();
        queryParamsForInvoice.put(JaxrsResource.QUERY_ACCOUNT_ID, externalCharge.getAccountId());
        queryParamsForInvoice.put(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, "true");
        response = httpClient.doGetWithUrl(location, queryParamsForInvoice, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String invoicesBaseJson = response.getResponseBody();
        final InvoiceJsonWithItems invoice = mapper.readValue(invoicesBaseJson, new TypeReference<InvoiceJsonWithItems>() {});
        return invoice;
    }
}
