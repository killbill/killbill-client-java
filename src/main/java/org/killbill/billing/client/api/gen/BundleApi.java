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
import org.killbill.billing.client.model.gen.BlockingState;
import org.killbill.billing.client.model.gen.Bundle;
import org.killbill.billing.client.model.gen.CustomField;
import org.joda.time.LocalDate;
import org.killbill.billing.client.model.gen.Tag;
import java.util.UUID;
import org.killbill.billing.client.model.BlockingStates;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.killbill.billing.client.model.CustomFields;
import org.killbill.billing.client.model.Tags;
import org.killbill.billing.util.api.AuditLevel;
import org.killbill.billing.client.model.AuditLogs;
import org.killbill.billing.client.model.Bundles;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.entitlement.api.BcdTransfer;

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
public class BundleApi {

    private final KillBillHttpClient httpClient;

    public BundleApi() {
        this(new KillBillHttpClient());
    }

    public BundleApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public BlockingStates addBundleBlockingState(final UUID bundleId, final BlockingState body, final LocalDate requestedDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling addBundleBlockingState");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling addBundleBlockingState");

        final String uri = "/1.0/kb/bundles/{bundleId}/block"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (requestedDate != null) {
            queryParams.put("requestedDate", String.valueOf(requestedDate));
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, BlockingStates.class, requestOptions);
    }

    public BlockingStates addBundleBlockingState(final UUID bundleId, final BlockingState body, final DateTime requestedDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling addBundleBlockingState");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling addBundleBlockingState");

        final String uri = "/1.0/kb/bundles/{bundleId}/block"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (requestedDate != null) {
            queryParams.put("requestedDate", String.valueOf(requestedDate));
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, BlockingStates.class, requestOptions);
    }

    public CustomFields createBundleCustomFields(final UUID bundleId, final CustomFields body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling createBundleCustomFields");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling createBundleCustomFields");

        final String uri = "/1.0/kb/bundles/{bundleId}/customFields"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, CustomFields.class, requestOptions);
    }

    public Tags createBundleTags(final UUID bundleId, final List<UUID> body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling createBundleTags");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling createBundleTags");

        final String uri = "/1.0/kb/bundles/{bundleId}/tags"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, Tags.class, requestOptions);
    }


    public void deleteBundleCustomFields(final UUID bundleId, final List<UUID> customField, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling deleteBundleCustomFields");

        final String uri = "/1.0/kb/bundles/{bundleId}/customFields"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

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


    public void deleteBundleTags(final UUID bundleId, final List<UUID> tagDef, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling deleteBundleTags");

        final String uri = "/1.0/kb/bundles/{bundleId}/tags"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

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

    public Bundle getBundle(final UUID bundleId, final RequestOptions inputOptions) throws KillBillClientException {
        return getBundle(bundleId, AuditLevel.NONE, inputOptions);
    }

    public Bundle getBundle(final UUID bundleId, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling getBundle");

        final String uri = "/1.0/kb/bundles/{bundleId}"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (audit != null) {
            queryParams.put("audit", String.valueOf(audit));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Bundle.class, requestOptions);
    }

    public AuditLogs getBundleAuditLogsWithHistory(final UUID bundleId, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling getBundleAuditLogsWithHistory");

        final String uri = "/1.0/kb/bundles/{bundleId}/auditLogsWithHistory"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, AuditLogs.class, requestOptions);
    }

    public Bundles getBundleByKey(final String externalKey, final RequestOptions inputOptions) throws KillBillClientException {
        return getBundleByKey(externalKey, Boolean.valueOf(false), AuditLevel.NONE, inputOptions);
    }

    public Bundles getBundleByKey(final String externalKey, final Boolean includedDeleted, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(externalKey, "Missing the required parameter 'externalKey' when calling getBundleByKey");

        final String uri = "/1.0/kb/bundles";

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (externalKey != null) {
            queryParams.put("externalKey", String.valueOf(externalKey));
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

        return httpClient.doGet(uri, Bundles.class, requestOptions);
    }

    public CustomFields getBundleCustomFields(final UUID bundleId, final RequestOptions inputOptions) throws KillBillClientException {
        return getBundleCustomFields(bundleId, AuditLevel.NONE, inputOptions);
    }

    public CustomFields getBundleCustomFields(final UUID bundleId, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling getBundleCustomFields");

        final String uri = "/1.0/kb/bundles/{bundleId}/customFields"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

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

    public Tags getBundleTags(final UUID bundleId, final RequestOptions inputOptions) throws KillBillClientException {
        return getBundleTags(bundleId, Boolean.valueOf(false), AuditLevel.NONE, inputOptions);
    }

    public Tags getBundleTags(final UUID bundleId, final Boolean includedDeleted, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling getBundleTags");

        final String uri = "/1.0/kb/bundles/{bundleId}/tags"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
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

    public Bundles getBundles(final RequestOptions inputOptions) throws KillBillClientException {
        return getBundles(Long.valueOf(0), Long.valueOf(100), AuditLevel.NONE, inputOptions);
    }

    public Bundles getBundles(final Long offset, final Long limit, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {

        final String uri = "/1.0/kb/bundles/pagination";

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (offset != null) {
            queryParams.put("offset", String.valueOf(offset));
        }
        if (limit != null) {
            queryParams.put("limit", String.valueOf(limit));
        }
        if (audit != null) {
            queryParams.put("audit", String.valueOf(audit));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Bundles.class, requestOptions);
    }

    public void modifyBundleCustomFields(final UUID bundleId, final CustomFields body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling modifyBundleCustomFields");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling modifyBundleCustomFields");

        final String uri = "/1.0/kb/bundles/{bundleId}/customFields"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPut(uri, body, requestOptions);
    }

    public void pauseBundle(final UUID bundleId, final LocalDate requestedDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling pauseBundle");

        final String uri = "/1.0/kb/bundles/{bundleId}/pause"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (requestedDate != null) {
            queryParams.put("requestedDate", String.valueOf(requestedDate));
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPut(uri, null, requestOptions);
    }

    public void renameExternalKey(final UUID bundleId, final Bundle body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling renameExternalKey");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling renameExternalKey");

        final String uri = "/1.0/kb/bundles/{bundleId}/renameKey"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPut(uri, body, requestOptions);
    }

    public void resumeBundle(final UUID bundleId, final LocalDate requestedDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling resumeBundle");

        final String uri = "/1.0/kb/bundles/{bundleId}/resume"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (requestedDate != null) {
            queryParams.put("requestedDate", String.valueOf(requestedDate));
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPut(uri, null, requestOptions);
    }

    public Bundles searchBundles(final String searchKey, final RequestOptions inputOptions) throws KillBillClientException {
        return searchBundles(searchKey, Long.valueOf(0), Long.valueOf(100), AuditLevel.NONE, inputOptions);
    }

    public Bundles searchBundles(final String searchKey, final Long offset, final Long limit, final AuditLevel audit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(searchKey, "Missing the required parameter 'searchKey' when calling searchBundles");

        final String uri = "/1.0/kb/bundles/search/{searchKey}"
          .replaceAll("\\{" + "searchKey" + "\\}", searchKey.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (offset != null) {
            queryParams.put("offset", String.valueOf(offset));
        }
        if (limit != null) {
            queryParams.put("limit", String.valueOf(limit));
        }
        if (audit != null) {
            queryParams.put("audit", String.valueOf(audit));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Bundles.class, requestOptions);
    }

    public Bundle transferBundle(final UUID bundleId, final Bundle body, final LocalDate requestedDate, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        return transferBundle(bundleId, body, requestedDate, BillingActionPolicy.END_OF_TERM, BcdTransfer.USE_EXISTING, pluginProperty, inputOptions);
    }

    public Bundle transferBundle(final UUID bundleId, final Bundle body, final LocalDate requestedDate, final BillingActionPolicy billingPolicy, final BcdTransfer bcdTransfer, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "Missing the required parameter 'bundleId' when calling transferBundle");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling transferBundle");

        final String uri = "/1.0/kb/bundles/{bundleId}"
          .replaceAll("\\{" + "bundleId" + "\\}", bundleId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (requestedDate != null) {
            queryParams.put("requestedDate", String.valueOf(requestedDate));
        }
        if (billingPolicy != null) {
            queryParams.put("billingPolicy", String.valueOf(billingPolicy));
        }
        if (bcdTransfer != null) {
            queryParams.put("bcdTransfer", String.valueOf(bcdTransfer));
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, Bundle.class, requestOptions);
    }

}
