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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.ning.billing.jaxrs.json.InvoiceItemJsonSimple;
import com.ning.billing.jaxrs.json.InvoiceJsonSimple;
import com.ning.billing.jaxrs.json.InvoiceJsonWithItems;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KillBillInvoice extends KillBillBaseResource {

    protected String accountId;
    protected String invoiceId;
    protected String invoiceNumber;
    protected Date invoiceDate;
    protected Date targetDate;
    protected BigDecimal amount;
    protected BigDecimal creditAdj;
    protected BigDecimal refundAdj;
    protected BigDecimal balance;
    protected String currency;
    protected List<KillBillInvoiceItem> items;

    public KillBillInvoice() {}

    public KillBillInvoice(String accountId, String invoiceId) {
        this.accountId = accountId;
        this.invoiceId = invoiceId;
    }

    public static KillBillInvoice getInvoiceById(final String invoiceId, boolean withItems) throws IOException, KillBillException {
        InvoiceJsonSimple invoiceJson = null;
        if (withItems) {
            invoiceJson = doGetInvoiceById(invoiceId, Boolean.TRUE, InvoiceJsonWithItems.class);
        } else {
            invoiceJson = doGetInvoiceById(invoiceId, Boolean.FALSE, InvoiceJsonSimple.class);
        }
        KillBillInvoice invoice = toInvoice(invoiceJson);
        return invoice;
    }

    public static List<KillBillInvoice> getInvoicesForAccount(final String accountId) throws IOException, KillBillException {
        List<KillBillInvoice> invoices = new ArrayList<KillBillInvoice>();
        List<InvoiceJsonSimple> jsons = doGetInvoicesForAccount(accountId, Boolean.FALSE, new TypeReference<List<InvoiceJsonSimple>>() {});
        for (InvoiceJsonSimple json : jsons) {
            invoices.add(toInvoice(json));
        }
        return invoices;
    }

    public static List<KillBillInvoice> getInvoicesForAccountWithItems(final String accountId) throws IOException, KillBillException {
        List<KillBillInvoice> invoices = new ArrayList<KillBillInvoice>();
        List<InvoiceJsonWithItems> jsons = doGetInvoicesForAccount(accountId, Boolean.TRUE, new TypeReference<List<InvoiceJsonWithItems>>() {});
        for (InvoiceJsonWithItems json : jsons) {
            invoices.add(toInvoice(json));
        }
        return invoices;
    }

    public KillBillInvoice createInvoice(final Date futureDate, boolean isDryRun) throws IOException, KillBillException {
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_ID, accountId);
        DateTime dateTime = new DateTime(futureDate.getTime());
        queryParams.put(JaxrsResource.QUERY_TARGET_DATE, dateTime.toString());
        if (isDryRun) {
            queryParams.put(JaxrsResource.QUERY_DRY_RUN, "true");
        }
        final Response response = httpClient.doPost(JaxrsResource.INVOICES_PATH, null, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        //final String location = response.getHeader("Location");
        final String baseJson = response.getResponseBody();
        final InvoiceJsonSimple futureInvoice = mapper.readValue(baseJson, InvoiceJsonSimple.class);
        return toInvoice(futureInvoice);
    }

    private static <T> T doGetInvoiceById(final String invoiceId, final Boolean withItems, final Class<T> clazz) throws IOException, KillBillException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId;
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, withItems.toString());
        ObjectMapper mapper = new ObjectMapper();
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doGet(uri, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String baseJson = response.getResponseBody();
        final T firstInvoiceJson = mapper.readValue(baseJson, clazz);
        return firstInvoiceJson;
    }

    private static <T> List<T> doGetInvoicesForAccount(final String accountId, final Boolean withItems, final TypeReference<List<T>> clazz) throws IOException, KillBillException {
        final String invoicesURI = JaxrsResource.INVOICES_PATH;
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_ID, accountId);
        queryParams.put(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, withItems.toString());
        ObjectMapper mapper = new ObjectMapper();
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doGet(invoicesURI, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String invoicesBaseJson = response.getResponseBody();
        final List<T> invoices = mapper.readValue(invoicesBaseJson, clazz);
        return invoices;
    }

    public static KillBillInvoice toInvoice(InvoiceJsonSimple json) {
        if (null == json) {
            return null;
        }
        KillBillInvoice inv = new KillBillInvoice();
        inv.setAccountId(json.getAccountId());
        inv.setAmount(json.getAmount());
        inv.setBalance(json.getBalance());
        inv.setCreditAdj(json.getCreditAdj());
        inv.setCurrency(json.getCurrency());
        if (null != json.getInvoiceDate()) {
            inv.setInvoiceDate(json.getInvoiceDate().toDate());
        }
        inv.setInvoiceId(json.getInvoiceId());
        inv.setInvoiceNumber(json.getInvoiceNumber());
        inv.setRefundAdj(json.getRefundAdj());
        if (null != json.getTargetDate()) {
            inv.setTargetDate(json.getTargetDate().toDate());
        }
        if (json instanceof InvoiceJsonWithItems) {
            InvoiceJsonWithItems jsonWithItems = (InvoiceJsonWithItems) json;
            if (null != jsonWithItems.getItems() && jsonWithItems.getItems().size() > 0) {
                List<KillBillInvoiceItem> items = new ArrayList<KillBillInvoiceItem>();
                for (InvoiceItemJsonSimple iijs : jsonWithItems.getItems()) {
                    items.add(KillBillInvoiceItem.toInvoiceItem(iijs));
                }
                inv.setItems(items);
            }
        }
        return inv;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public BigDecimal getCreditAdj() {
        return creditAdj;
    }

    public void setCreditAdj(BigDecimal creditAdj) {
        this.creditAdj = creditAdj;
    }

    public BigDecimal getRefundAdj() {
        return refundAdj;
    }

    public void setRefundAdj(BigDecimal refundAdj) {
        this.refundAdj = refundAdj;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<KillBillInvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<KillBillInvoiceItem> items) {
        this.items = items;
    }

    public InvoiceJsonSimple toInvoiceJsonSimple() {
        LocalDate invDate = (null == invoiceDate) ? null : new LocalDate(invoiceDate.getTime());
        LocalDate tgtDate = (null == targetDate) ? null : new LocalDate(targetDate.getTime());
        return new InvoiceJsonSimple(amount, currency, creditAdj, refundAdj, invoiceId, invDate, tgtDate, invoiceNumber, balance, accountId, null);
    }
}
