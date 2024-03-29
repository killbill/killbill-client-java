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

import java.util.Objects;

import org.killbill.billing.client.model.gen.AuditLog;
import org.killbill.billing.client.model.gen.CustomField;
import org.killbill.billing.client.model.gen.Tag;
import java.util.UUID;
import org.killbill.billing.client.model.CustomFields;
import java.util.List;
import org.killbill.billing.client.model.Tags;
import org.killbill.billing.client.model.AuditLogs;
import org.killbill.billing.util.api.AuditLevel;

import org.killbill.billing.client.Converter;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;

import org.killbill.billing.client.util.Preconditions;
import org.killbill.billing.client.util.Multimap;
import org.killbill.billing.client.util.TreeMapSetMultimap;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
public class InvoiceItemApi {

    private final KillBillHttpClient httpClient;

    public InvoiceItemApi() {
        this(new KillBillHttpClient());
    }

    public InvoiceItemApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CustomFields createInvoiceItemCustomFields(final UUID invoiceItemId, final CustomFields body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling createInvoiceItemCustomFields");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling createInvoiceItemCustomFields");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/customFields"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, CustomFields.class, requestOptions);
    }

    public Tags createInvoiceItemTags(final UUID invoiceItemId, final List<UUID> body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling createInvoiceItemTags");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling createInvoiceItemTags");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/tags"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, Tags.class, requestOptions);
    }


    public void deleteInvoiceItemCustomFields(final UUID invoiceItemId, final List<UUID> customField, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling deleteInvoiceItemCustomFields");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/customFields"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (customField != null) {
            queryParams.putAll("customField", Converter.convertUUIDListToStringList(customField));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doDelete(uri, requestOptions);
    }


    public void deleteInvoiceItemTags(final UUID invoiceItemId, final List<UUID> tagDef, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling deleteInvoiceItemTags");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/tags"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (tagDef != null) {
            queryParams.putAll("tagDef", Converter.convertUUIDListToStringList(tagDef));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doDelete(uri, requestOptions);
    }

    public AuditLogs getInvoiceItemAuditLogsWithHistory(final UUID invoiceItemId, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling getInvoiceItemAuditLogsWithHistory");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/auditLogsWithHistory"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, AuditLogs.class, requestOptions);
    }

    public CustomFields getInvoiceItemCustomFields(final UUID invoiceItemId, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoiceItemCustomFields(invoiceItemId, AuditLevel.NONE, inputOptions);
    }

    public CustomFields getInvoiceItemCustomFields(final UUID invoiceItemId, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling getInvoiceItemCustomFields");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/customFields"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (audit != null) {
            queryParams.put("audit", String.valueOf(audit));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, CustomFields.class, requestOptions);
    }

    public Tags getInvoiceItemTags(final UUID invoiceItemId, final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoiceItemTags(invoiceItemId, accountId, Boolean.valueOf(false), AuditLevel.NONE, inputOptions);
    }

    public Tags getInvoiceItemTags(final UUID invoiceItemId, final UUID accountId, final Boolean includedDeleted, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling getInvoiceItemTags");
        Preconditions.checkNotNull(accountId, "Missing the required parameter 'accountId' when calling getInvoiceItemTags");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/tags"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (accountId != null) {
            queryParams.put("accountId", String.valueOf(accountId));
        }
        if (includedDeleted != null) {
            queryParams.put("includedDeleted", String.valueOf(includedDeleted));
        }
        if (audit != null) {
            queryParams.put("audit", String.valueOf(audit));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

    public void modifyInvoiceItemCustomFields(final UUID invoiceItemId, final CustomFields body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItemId, "Missing the required parameter 'invoiceItemId' when calling modifyInvoiceItemCustomFields");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling modifyInvoiceItemCustomFields");

        final String uri = "/1.0/kb/invoiceItems/{invoiceItemId}/customFields"
          .replaceAll("\\{" + "invoiceItemId" + "\\}", invoiceItemId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPut(uri, body, requestOptions);
    }

}
