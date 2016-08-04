/*
 * Copyright 2010-2013 Ning, Inc.
 * Copyright 2014-2016 Groupon, Inc
 * Copyright 2014-2016 The Billing Project, LLC
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

package org.killbill.billing.client;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;
import org.killbill.billing.client.model.Account;
import org.killbill.billing.client.model.AccountEmail;
import org.killbill.billing.client.model.AccountEmails;
import org.killbill.billing.client.model.AccountTimeline;
import org.killbill.billing.client.model.Accounts;
import org.killbill.billing.client.model.BlockingState;
import org.killbill.billing.client.model.Bundle;
import org.killbill.billing.client.model.Bundles;
import org.killbill.billing.client.model.Catalog;
import org.killbill.billing.client.model.ComboHostedPaymentPage;
import org.killbill.billing.client.model.ComboPaymentTransaction;
import org.killbill.billing.client.model.Credit;
import org.killbill.billing.client.model.CustomField;
import org.killbill.billing.client.model.CustomFields;
import org.killbill.billing.client.model.HostedPaymentPageFields;
import org.killbill.billing.client.model.HostedPaymentPageFormDescriptor;
import org.killbill.billing.client.model.Invoice;
import org.killbill.billing.client.model.InvoiceDryRun;
import org.killbill.billing.client.model.InvoiceEmail;
import org.killbill.billing.client.model.InvoiceItem;
import org.killbill.billing.client.model.InvoiceItems;
import org.killbill.billing.client.model.InvoicePayment;
import org.killbill.billing.client.model.InvoicePaymentTransaction;
import org.killbill.billing.client.model.InvoicePayments;
import org.killbill.billing.client.model.Invoices;
import org.killbill.billing.client.model.OverdueState;
import org.killbill.billing.client.model.Payment;
import org.killbill.billing.client.model.PaymentMethod;
import org.killbill.billing.client.model.PaymentMethods;
import org.killbill.billing.client.model.PaymentTransaction;
import org.killbill.billing.client.model.Payments;
import org.killbill.billing.client.model.Permissions;
import org.killbill.billing.client.model.PlanDetail;
import org.killbill.billing.client.model.PlanDetails;
import org.killbill.billing.client.model.RoleDefinition;
import org.killbill.billing.client.model.RolledUpUsage;
import org.killbill.billing.client.model.Subscription;
import org.killbill.billing.client.model.SubscriptionUsageRecord;
import org.killbill.billing.client.model.TagDefinition;
import org.killbill.billing.client.model.TagDefinitions;
import org.killbill.billing.client.model.Tags;
import org.killbill.billing.client.model.Tenant;
import org.killbill.billing.client.model.TenantKey;
import org.killbill.billing.client.model.UserRoles;
import org.killbill.billing.entitlement.api.Entitlement.EntitlementActionPolicy;
import org.killbill.billing.util.api.AuditLevel;

import com.ning.http.client.Response;
import com.ning.http.util.UTF8UrlEncoder;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import static org.killbill.billing.client.KillBillHttpClient.ACCEPT_XML;
import static org.killbill.billing.client.KillBillHttpClient.CONTENT_TYPE_XML;
import static org.killbill.billing.client.KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC;

public class KillBillClient implements Closeable {

    private final KillBillHttpClient httpClient;

    public KillBillClient() {
        this(new KillBillHttpClient());
    }

    public KillBillClient(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void close() {
        httpClient.close();
    }

    // Accounts

    @Deprecated
    public Accounts getAccounts() throws KillBillClientException {
        return getAccounts(RequestOptions.empty());
    }

    public Accounts getAccounts(final RequestOptions inputOptions) throws KillBillClientException {
        return getAccounts(0L, 100L, inputOptions);
    }

    @Deprecated
    public Accounts getAccounts(final Long offset, final Long limit) throws KillBillClientException {
        return getAccounts(offset, limit, RequestOptions.empty());
    }

    public Accounts getAccounts(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getAccounts(offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Accounts getAccounts(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getAccounts(offset, limit, auditLevel, RequestOptions.empty());
    }

    public Accounts getAccounts(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Accounts.class, requestOptions);
    }

    @Deprecated
    public Account getAccount(final UUID accountId) throws KillBillClientException {
        return getAccount(accountId, RequestOptions.empty());
    }

    public Account getAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getAccount(accountId, false, false, inputOptions);
    }

    @Deprecated
    public Account getAccount(final UUID accountId, final boolean withBalance, final boolean withCBA) throws KillBillClientException {
        return getAccount(accountId, withBalance, withCBA, RequestOptions.empty());
    }

    public Account getAccount(final UUID accountId, final boolean withBalance, final boolean withCBA, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE, withBalance ? "true" : "false");
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE_AND_CBA, withCBA ? "true" : "false");

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Account.class, requestOptions);
    }

    @Deprecated
    public Account getAccount(final String externalKey) throws KillBillClientException {
        return getAccount(externalKey, RequestOptions.empty());
    }

    public Account getAccount(final String externalKey, final RequestOptions inputOptions) throws KillBillClientException {
        return getAccount(externalKey, false, false, inputOptions);
    }

    @Deprecated
    public Account getAccount(final String externalKey, final boolean withBalance, final boolean withCBA) throws KillBillClientException {
        return getAccount(externalKey, withBalance, withCBA, RequestOptions.empty());
    }

    public Account getAccount(final String externalKey, final boolean withBalance, final boolean withCBA, final RequestOptions inputOptions) throws KillBillClientException {
        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE, withBalance ? "true" : "false");
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE_AND_CBA, withCBA ? "true" : "false");

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(JaxrsResource.ACCOUNTS_PATH, Account.class, requestOptions);
    }

    @Deprecated
    public Accounts searchAccounts(final String key) throws KillBillClientException {
        return searchAccounts(key, RequestOptions.empty());
    }

    public Accounts searchAccounts(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchAccounts(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public Accounts searchAccounts(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchAccounts(key, offset, limit, RequestOptions.empty());
    }

    public Accounts searchAccounts(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchAccounts(key, offset, limit, AuditLevel.NONE, RequestOptions.empty());
    }

    @Deprecated
    public Accounts searchAccounts(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchAccounts(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Accounts searchAccounts(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Accounts.class, requestOptions);
    }

    @Deprecated
    public AccountTimeline getAccountTimeline(final UUID accountId) throws KillBillClientException {
        return getAccountTimeline(accountId, RequestOptions.empty());
    }

    public AccountTimeline getAccountTimeline(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getAccountTimeline(accountId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public AccountTimeline getAccountTimeline(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        return getAccountTimeline(accountId, auditLevel, RequestOptions.empty());
    }

    public AccountTimeline getAccountTimeline(final UUID accountId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.TIMELINE;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, AccountTimeline.class, requestOptions);
    }

    @Deprecated
    public Account createAccount(final Account account, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createAccount(account, RequestOptions.builder()
                                                    .withCreatedBy(createdBy)
                                                    .withReason(reason)
                                                    .withComment(comment).build());
    }

    public Account createAccount(final Account account, final RequestOptions inputOptions) throws KillBillClientException {
        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();
        return httpClient.doPost(JaxrsResource.ACCOUNTS_PATH, account, Account.class, requestOptions);
    }

    @Deprecated
    public Account updateAccount(final Account account, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateAccount(account, RequestOptions.builder()
                                                    .withCreatedBy(createdBy)
                                                    .withReason(reason)
                                                    .withComment(comment).build());
    }

    public Account updateAccount(final Account account, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(account.getAccountId(), "Account#accountId cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + account.getAccountId();

        return httpClient.doPut(uri, account, Account.class, inputOptions);
    }

    @Deprecated
    public AccountEmails getEmailsForAccount(final UUID accountId) throws KillBillClientException {
        return getEmailsForAccount(accountId, RequestOptions.empty());
    }

    public AccountEmails getEmailsForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAILS;

        return httpClient.doGet(uri, AccountEmails.class, inputOptions);
    }

    @Deprecated
    public void addEmailToAccount(final AccountEmail email, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        addEmailToAccount(email, RequestOptions.builder()
                                               .withCreatedBy(createdBy)
                                               .withReason(reason)
                                               .withComment(comment).build());
    }

    public void addEmailToAccount(final AccountEmail email, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(email.getAccountId(), "AccountEmail#accountId cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + email.getAccountId() + "/" + JaxrsResource.EMAILS;

        httpClient.doPost(uri, email, inputOptions);
    }

    @Deprecated
    public void removeEmailFromAccount(final AccountEmail email, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        removeEmailFromAccount(email, RequestOptions.builder()
                                                    .withCreatedBy(createdBy)
                                                    .withReason(reason)
                                                    .withComment(comment).build());
    }

    public void removeEmailFromAccount(final AccountEmail email, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(email.getAccountId(), "AccountEmail#accountId cannot be null");
        Preconditions.checkNotNull(email.getEmail(), "AccountEmail#email cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + email.getAccountId() + "/" + JaxrsResource.EMAILS + "/" + UTF8UrlEncoder.encode(email.getEmail());

        httpClient.doDelete(uri, inputOptions);
    }

    @Deprecated
    public InvoiceEmail getEmailNotificationsForAccount(final UUID accountId) throws KillBillClientException {
        return getEmailNotificationsForAccount(accountId, RequestOptions.empty());
    }

    public InvoiceEmail getEmailNotificationsForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAIL_NOTIFICATIONS;

        return httpClient.doGet(uri, InvoiceEmail.class, inputOptions);
    }

    @Deprecated
    public void updateEmailNotificationsForAccount(final InvoiceEmail invoiceEmail, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        updateEmailNotificationsForAccount(invoiceEmail, RequestOptions.builder()
                                                                       .withCreatedBy(createdBy)
                                                                       .withReason(reason)
                                                                       .withComment(comment).build());
    }

    public void updateEmailNotificationsForAccount(final InvoiceEmail invoiceEmail, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceEmail.getAccountId(), "InvoiceEmail#accountId cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + invoiceEmail.getAccountId() + "/" + JaxrsResource.EMAIL_NOTIFICATIONS;

        httpClient.doPut(uri, invoiceEmail, inputOptions);
    }

    // Bundles

    @Deprecated
    public Bundle getBundle(final UUID bundleId) throws KillBillClientException {
        return getBundle(bundleId, RequestOptions.empty());
    }

    public Bundle getBundle(final UUID bundleId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.BUNDLES_PATH + "/" + bundleId;

        return httpClient.doGet(uri, Bundle.class, inputOptions);
    }

    @Deprecated
    public Bundle getBundle(final String externalKey) throws KillBillClientException {
        return getBundle(externalKey, RequestOptions.empty());
    }

    public Bundle getBundle(final String externalKey, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.BUNDLES_PATH;

        final Multimap<String, String> queryParams = HashMultimap.create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Bundle.class, requestOptions);
    }

    @Deprecated
    public Bundles getAccountBundles(final UUID accountId) throws KillBillClientException {
        return getAccountBundles(accountId, RequestOptions.empty());
    }

    public Bundles getAccountBundles(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.BUNDLES;

        return httpClient.doGet(uri, Bundles.class, inputOptions);
    }

    @Deprecated
    public Bundles getAccountBundles(final UUID accountId, final String externalKey) throws KillBillClientException {
        return getAccountBundles(accountId, externalKey, RequestOptions.empty());
    }

    public Bundles getAccountBundles(final UUID accountId, final String externalKey, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.BUNDLES;

        final Multimap<String, String> queryParams = HashMultimap.create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Bundles.class, requestOptions);
    }

    @Deprecated
    public Bundles getBundles() throws KillBillClientException {
        return getBundles(RequestOptions.empty());
    }

    public Bundles getBundles(final RequestOptions inputOptions) throws KillBillClientException {
        return getBundles(0L, 100L, inputOptions);
    }

    @Deprecated
    public Bundles getBundles(final Long offset, final Long limit) throws KillBillClientException {
        return getBundles(offset, limit, RequestOptions.empty());
    }

    public Bundles getBundles(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getBundles(offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Bundles getBundles(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getBundles(offset, limit, auditLevel, RequestOptions.empty());
    }

    public Bundles getBundles(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.BUNDLES_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Bundles.class, requestOptions);
    }

    @Deprecated
    public Bundles searchBundles(final String key) throws KillBillClientException {
        return searchBundles(key, RequestOptions.empty());
    }

    public Bundles searchBundles(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchBundles(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public Bundles searchBundles(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchBundles(key, offset, limit, RequestOptions.empty());
    }

    public Bundles searchBundles(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchBundles(key, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Bundles searchBundles(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchBundles(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Bundles searchBundles(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.BUNDLES_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Bundles.class, requestOptions);
    }

    @Deprecated
    public Bundle transferBundle(final Bundle bundle, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return transferBundle(bundle, RequestOptions.builder()
                                                    .withCreatedBy(createdBy)
                                                    .withReason(reason)
                                                    .withComment(comment).build());
    }

    public Bundle transferBundle(final Bundle bundle, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundle.getBundleId(), "Bundle#bundleId cannot be null");
        Preconditions.checkNotNull(bundle.getAccountId(), "Bundle#accountId cannot be null");

        final String uri = JaxrsResource.BUNDLES_PATH + "/" + bundle.getBundleId();

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPut(uri, bundle, Bundle.class, requestOptions);
    }

    @Deprecated
    public void setBlockingState(final UUID bundleId, final BlockingState blockingState, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        setBlockingState(bundleId, blockingState, RequestOptions.builder()
                                                                .withCreatedBy(createdBy)
                                                                .withReason(reason)
                                                                .withComment(comment).build());
    }

    public void setBlockingState(final UUID bundleId, final BlockingState blockingState, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(bundleId, "bundleId cannot be null");
        Preconditions.checkNotNull(blockingState.getService(), "Bundle#service cannot be null");
        Preconditions.checkNotNull(blockingState.getStateName(), "Bundle#stateName cannot be null");
        Preconditions.checkNotNull(blockingState.getEffectiveDate(), "Bundle#effectiveDate cannot be null");
        Preconditions.checkNotNull(blockingState.getType(), "Bundle#type cannot be null");

        final String uri = JaxrsResource.BUNDLES_PATH + "/" + bundleId + "/" + JaxrsResource.BLOCK;

        httpClient.doPut(uri, blockingState, inputOptions);
    }

    // Subscriptions and entitlements

    @Deprecated
    public Subscription getSubscription(final UUID subscriptionId) throws KillBillClientException {
        return getSubscription(subscriptionId, RequestOptions.empty());
    }

    public Subscription getSubscription(final UUID subscriptionId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscriptionId;

        return httpClient.doGet(uri, Subscription.class, inputOptions);
    }

    @Deprecated
    public Subscription createSubscription(final Subscription subscription, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createSubscription(subscription, RequestOptions.builder()
                                                              .withCreatedBy(createdBy)
                                                              .withReason(reason)
                                                              .withComment(comment).build());
    }

    public Subscription createSubscription(final Subscription subscription, final RequestOptions inputOptions) throws KillBillClientException {
        return createSubscription(subscription, -1, inputOptions);
    }

    @Deprecated
    public Subscription createSubscription(final Subscription subscription, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createSubscription(subscription, timeoutSec, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment).build());
    }

    public Subscription createSubscription(final Subscription subscription, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        return createSubscription(subscription, null, timeoutSec, inputOptions);
    }

    @Deprecated
    public Subscription createSubscription(final Subscription subscription, final DateTime requestedDate, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createSubscription(subscription, requestedDate, timeoutSec, RequestOptions.builder()
                                                                                         .withCreatedBy(createdBy)
                                                                                         .withReason(reason)
                                                                                         .withComment(comment).build());
    }

    public Subscription createSubscription(final Subscription subscription, final DateTime requestedDate, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(subscription.getAccountId(), "Subscription#accountId cannot be null");
        Preconditions.checkNotNull(subscription.getProductName(), "Subscription#productName cannot be null");
        Preconditions.checkNotNull(subscription.getProductCategory(), "Subscription#productCategory cannot be null");
        Preconditions.checkNotNull(subscription.getBillingPeriod(), "Subscription#billingPeriod cannot be null");
        Preconditions.checkNotNull(subscription.getPriceList(), "Subscription#priceList cannot be null");
        if (subscription.getProductCategory() == ProductCategory.BASE) {
            Preconditions.checkNotNull(subscription.getAccountId(), "Account#accountId cannot be null");
        }

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false");
        queryParams.put(JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec));
        if (requestedDate != null) {
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());
        }

        final int httpTimeout = Math.max(DEFAULT_HTTP_TIMEOUT_SEC, timeoutSec);

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(JaxrsResource.SUBSCRIPTIONS_PATH, subscription, Subscription.class, requestOptions, httpTimeout);
    }

    @Deprecated
    public Bundle createSubscriptionWithAddOns(final Iterable<Subscription> subscriptions, final DateTime requestedDate, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createSubscriptionWithAddOns(subscriptions, requestedDate, timeoutSec, RequestOptions.builder()
                                                                                                    .withCreatedBy(createdBy)
                                                                                                    .withReason(reason)
                                                                                                    .withComment(comment).build());
    }

    public Bundle createSubscriptionWithAddOns(final Iterable<Subscription> subscriptions, final DateTime requestedDate, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        for (final Subscription subscription : subscriptions) {
            Preconditions.checkNotNull(subscription.getProductName(), "Subscription#productName cannot be null");
            Preconditions.checkNotNull(subscription.getProductCategory(), "Subscription#productCategory cannot be null");
            Preconditions.checkNotNull(subscription.getBillingPeriod(), "Subscription#billingPeriod cannot be null");
            Preconditions.checkNotNull(subscription.getPriceList(), "Subscription#priceList cannot be null");
            if (subscription.getProductCategory() == ProductCategory.BASE) {
                Preconditions.checkNotNull(subscription.getAccountId(), "Account#accountId cannot be null for base subscription");
            }
        }

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false");
        queryParams.put(JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec));
        if (requestedDate != null) {
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());
        }

        final int httpTimeout = Math.max(DEFAULT_HTTP_TIMEOUT_SEC, timeoutSec);

        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/createEntitlementWithAddOns";

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, subscriptions, Bundle.class, requestOptions, httpTimeout);
    }

    @Deprecated
    public Subscription updateSubscription(final Subscription subscription, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateSubscription(subscription, timeoutSec, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment).build());
    }

    public Subscription updateSubscription(final Subscription subscription, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        return updateSubscription(subscription, null, timeoutSec, inputOptions);
    }

    @Deprecated
    public Subscription updateSubscription(final Subscription subscription, @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateSubscription(subscription, billingPolicy, timeoutSec, RequestOptions.builder()
                                                                                         .withCreatedBy(createdBy)
                                                                                         .withReason(reason)
                                                                                         .withComment(comment).build());
    }

    public Subscription updateSubscription(final Subscription subscription, @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        return updateSubscription(subscription, null, billingPolicy, timeoutSec, inputOptions);
    }

    @Deprecated
    public Subscription updateSubscription(final Subscription subscription, @Nullable final DateTime requestedDate, @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateSubscription(subscription, requestedDate, billingPolicy, timeoutSec, RequestOptions.builder()
                                                                                                        .withCreatedBy(createdBy)
                                                                                                        .withReason(reason)
                                                                                                        .withComment(comment).build());
    }

    public Subscription updateSubscription(final Subscription subscription, @Nullable final DateTime requestedDate, @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(subscription.getSubscriptionId(), "Subscription#subscriptionId cannot be null");
        Preconditions.checkNotNull(subscription.getProductName(), "Subscription#productName cannot be null");
        Preconditions.checkNotNull(subscription.getBillingPeriod(), "Subscription#billingPeriod cannot be null");
        Preconditions.checkNotNull(subscription.getPriceList(), "Subscription#priceList cannot be null");

        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscription.getSubscriptionId();

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false");
        queryParams.put(JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec));
        if (requestedDate != null) {
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());
        }
        if (billingPolicy != null) {
            queryParams.put(JaxrsResource.QUERY_BILLING_POLICY, billingPolicy.toString());
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doPut(uri, subscription, Subscription.class, inputOptions);
    }

    @Deprecated
    public void cancelSubscription(final UUID subscriptionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, -1, RequestOptions.builder()
                                                             .withCreatedBy(createdBy)
                                                             .withReason(reason)
                                                             .withComment(comment).build());
    }

    public void cancelSubscription(final UUID subscriptionId, final RequestOptions inputOptions) throws KillBillClientException {
        cancelSubscription(subscriptionId, -1, inputOptions);
    }

    @Deprecated
    public void cancelSubscription(final UUID subscriptionId, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, timeoutSec, RequestOptions.builder()
                                                                     .withCreatedBy(createdBy)
                                                                     .withReason(reason)
                                                                     .withComment(comment).build());
    }

    public void cancelSubscription(final UUID subscriptionId, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        cancelSubscription(subscriptionId, null, null, timeoutSec, inputOptions);
    }

    @Deprecated
    public void cancelSubscription(final UUID subscriptionId, @Nullable final EntitlementActionPolicy entitlementPolicy, @Nullable final BillingActionPolicy billingPolicy,
                                   final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, entitlementPolicy, billingPolicy, timeoutSec, RequestOptions.builder()
                                                                                                       .withCreatedBy(createdBy)
                                                                                                       .withReason(reason)
                                                                                                       .withComment(comment).build());
    }

    public void cancelSubscription(final UUID subscriptionId, @Nullable final EntitlementActionPolicy entitlementPolicy, @Nullable final BillingActionPolicy billingPolicy,
                                   final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        cancelSubscription(subscriptionId, null, entitlementPolicy, billingPolicy, timeoutSec, inputOptions);
    }

    @Deprecated
    public void cancelSubscription(final UUID subscriptionId, @Nullable final DateTime requestedDate, @Nullable final EntitlementActionPolicy entitlementPolicy, @Nullable final BillingActionPolicy billingPolicy,
                                   final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, requestedDate, entitlementPolicy, billingPolicy, timeoutSec, RequestOptions.builder()
                                                                                                                      .withCreatedBy(createdBy)
                                                                                                                      .withReason(reason)
                                                                                                                      .withComment(comment).build());
    }

    public void cancelSubscription(final UUID subscriptionId, @Nullable final DateTime requestedDate, @Nullable final EntitlementActionPolicy entitlementPolicy, @Nullable final BillingActionPolicy billingPolicy,
                                   final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        cancelSubscription(subscriptionId, requestedDate, null, entitlementPolicy, billingPolicy, timeoutSec, inputOptions);
    }

    @Deprecated
    public void cancelSubscription(final UUID subscriptionId, @Nullable final DateTime requestedDate, @Nullable final Boolean useRequestedDateForBilling, @Nullable final EntitlementActionPolicy entitlementPolicy,
                                   @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, requestedDate, useRequestedDateForBilling, entitlementPolicy, billingPolicy, timeoutSec, RequestOptions.builder()
                                                                                                                                                  .withCreatedBy(createdBy)
                                                                                                                                                  .withReason(reason)
                                                                                                                                                  .withComment(comment).build());
    }

    public void cancelSubscription(final UUID subscriptionId, @Nullable final DateTime requestedDate, @Nullable final Boolean useRequestedDateForBilling, @Nullable final EntitlementActionPolicy entitlementPolicy,
                                   @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscriptionId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false");
        queryParams.put(JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec));
        if (requestedDate != null) {
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());
        }
        if (entitlementPolicy != null) {
            queryParams.put(JaxrsResource.QUERY_ENTITLEMENT_POLICY, entitlementPolicy.toString());
        }
        if (billingPolicy != null) {
            queryParams.put(JaxrsResource.QUERY_BILLING_POLICY, billingPolicy.toString());
        }
        if (useRequestedDateForBilling != null) {
            queryParams.put(JaxrsResource.QUERY_USE_REQUESTED_DATE_FOR_BILLING, useRequestedDateForBilling.toString());
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doDelete(uri, requestOptions);
    }

    @Deprecated
    public void uncancelSubscription(final UUID subscriptionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uncancelSubscription(subscriptionId, RequestOptions.builder()
                                                           .withCreatedBy(createdBy)
                                                           .withReason(reason)
                                                           .withComment(comment).build());
    }

    public void uncancelSubscription(final UUID subscriptionId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscriptionId + "/uncancel";

        httpClient.doPut(uri, null, inputOptions);
    }

    @Deprecated
    public void createSubscriptionUsageRecord(final SubscriptionUsageRecord subscriptionUsageRecord, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        createSubscriptionUsageRecord(subscriptionUsageRecord, RequestOptions.builder()
                                                                             .withCreatedBy(createdBy)
                                                                             .withReason(reason)
                                                                             .withComment(comment).build());
    }

    public void createSubscriptionUsageRecord(final SubscriptionUsageRecord subscriptionUsageRecord, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(subscriptionUsageRecord.getSubscriptionId(), "SubscriptionUsageRecord#subscriptionId cannot be null");
        Preconditions.checkNotNull(subscriptionUsageRecord.getUnitUsageRecords(), "SubscriptionUsageRecord#unitUsageRecords cannot be null");
        Preconditions.checkArgument(!subscriptionUsageRecord.getUnitUsageRecords().isEmpty(), "SubscriptionUsageRecord#unitUsageRecords cannot be empty");

        final String uri = JaxrsResource.USAGES_PATH;

        httpClient.doPost(uri, subscriptionUsageRecord, inputOptions);
    }

    @Deprecated
    public RolledUpUsage getRolledUpUsage(final UUID subscriptionId, @Nullable final String unitType, final LocalDate startDate, final LocalDate endDate) throws KillBillClientException {
        return getRolledUpUsage(subscriptionId, unitType, startDate, endDate, RequestOptions.empty());
    }

    public RolledUpUsage getRolledUpUsage(final UUID subscriptionId, @Nullable final String unitType, final LocalDate startDate, final LocalDate endDate, final RequestOptions inputOptions) throws KillBillClientException {
        String uri = JaxrsResource.USAGES_PATH + "/" + subscriptionId;

        if (unitType != null && !unitType.trim().isEmpty()) {
            uri = uri.concat("/").concat(unitType);
        }

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_START_DATE, startDate.toString());
        queryParams.put(JaxrsResource.QUERY_END_DATE, endDate.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, RolledUpUsage.class, requestOptions);
    }

    // Invoices

    @Deprecated
    public Invoices getInvoices() throws KillBillClientException {
        return getInvoices(RequestOptions.empty());
    }

    public Invoices getInvoices(final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoices(0L, 100L, inputOptions);
    }

    @Deprecated
    public Invoices getInvoices(final Long offset, final Long limit) throws KillBillClientException {
        return getInvoices(offset, limit, RequestOptions.empty());
    }

    public Invoices getInvoices(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoices(true, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Invoices getInvoices(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoices(true, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Invoices getInvoices(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoices(true, offset, limit, auditLevel, inputOptions);
    }

    @Deprecated
    public Invoices getInvoices(final boolean withItems, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoices(withItems, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Invoices getInvoices(final boolean withItems, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, String.valueOf(withItems));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Invoices.class, requestOptions);
    }

    @Deprecated
    public Invoice getInvoice(final UUID invoiceId) throws KillBillClientException {
        return getInvoice(invoiceId, RequestOptions.empty());
    }

    public Invoice getInvoice(final UUID invoiceId, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoice(invoiceId, true, inputOptions);
    }

    @Deprecated
    public Invoice getInvoice(final UUID invoiceId, final boolean withItems) throws KillBillClientException {
        return getInvoice(invoiceId, withItems, RequestOptions.empty());
    }

    public Invoice getInvoice(final UUID invoiceId, final boolean withItems, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoice(invoiceId, withItems, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Invoice getInvoice(final UUID invoiceId, final boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoice(invoiceId, withItems, auditLevel, RequestOptions.empty());
    }

    public Invoice getInvoice(final UUID invoiceId, final boolean withItems, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoiceByIdOrNumber(invoiceId.toString(), withItems, auditLevel, inputOptions);
    }

    @Deprecated
    public Invoice getInvoice(final Integer invoiceNumber) throws KillBillClientException {
        return getInvoice(invoiceNumber, RequestOptions.empty());
    }

    public Invoice getInvoice(final Integer invoiceNumber, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoice(invoiceNumber, true, inputOptions);
    }

    @Deprecated
    public Invoice getInvoice(final Integer invoiceNumber, final boolean withItems) throws KillBillClientException {
        return getInvoice(invoiceNumber, withItems, RequestOptions.empty());
    }

    public Invoice getInvoice(final Integer invoiceNumber, final boolean withItems, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoice(invoiceNumber, withItems, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Invoice getInvoice(final Integer invoiceNumber, final boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoice(invoiceNumber, withItems, auditLevel, RequestOptions.empty());
    }

    public Invoice getInvoice(final Integer invoiceNumber, final boolean withItems, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoiceByIdOrNumber(invoiceNumber.toString(), withItems, auditLevel, inputOptions);
    }

    @Deprecated
    public Invoice getInvoiceByIdOrNumber(final String invoiceIdOrNumber, final boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoiceByIdOrNumber(invoiceIdOrNumber, withItems, auditLevel, RequestOptions.empty());
    }

    public Invoice getInvoiceByIdOrNumber(final String invoiceIdOrNumber, final boolean withItems, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceIdOrNumber;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, String.valueOf(withItems));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Invoice.class, requestOptions);
    }

    @Deprecated
    public String getInvoiceAsHtml(final UUID invoiceId) throws KillBillClientException {
        return getInvoiceAsHtml(invoiceId, RequestOptions.empty());
    }

    public String getInvoiceAsHtml(final UUID invoiceId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId + "/" + JaxrsResource.INVOICE_HTML;
        return getResourceFile(uri, KillBillHttpClient.ACCEPT_HTML, inputOptions);
    }

    @Deprecated
    public Invoices getInvoicesForAccount(final UUID accountId) throws KillBillClientException {
        return getInvoicesForAccount(accountId, RequestOptions.empty());
    }

    public Invoices getInvoicesForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoicesForAccount(accountId, true, inputOptions);
    }

    @Deprecated
    public Invoices getInvoicesForAccount(final UUID accountId, final boolean withItems) throws KillBillClientException {
        return getInvoicesForAccount(accountId, withItems, RequestOptions.empty());
    }

    public Invoices getInvoicesForAccount(final UUID accountId, final boolean withItems, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoicesForAccount(accountId, withItems, false, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Invoices getInvoicesForAccount(final UUID accountId, final boolean withItems, final boolean unpaidOnly) throws KillBillClientException {
        return getInvoicesForAccount(accountId, withItems, unpaidOnly, RequestOptions.empty());
    }

    public Invoices getInvoicesForAccount(final UUID accountId, final boolean withItems, final boolean unpaidOnly, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoicesForAccount(accountId, withItems, unpaidOnly, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Invoices getInvoicesForAccount(final UUID accountId, final boolean withItems, final boolean unpaidOnly, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoicesForAccount(accountId, withItems, unpaidOnly, auditLevel, RequestOptions.empty());
    }

    public Invoices getInvoicesForAccount(final UUID accountId, final boolean withItems, final boolean unpaidOnly, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.INVOICES;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, String.valueOf(withItems));
        queryParams.put(JaxrsResource.QUERY_UNPAID_INVOICES_ONLY, String.valueOf(unpaidOnly));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Invoices.class, requestOptions);
    }

    @Deprecated
    public Invoices searchInvoices(final String key) throws KillBillClientException {
        return searchInvoices(key, RequestOptions.empty());
    }

    public Invoices searchInvoices(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchInvoices(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public Invoices searchInvoices(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchInvoices(key, offset, limit, RequestOptions.empty());
    }

    public Invoices searchInvoices(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchInvoices(key, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Invoices searchInvoices(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchInvoices(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Invoices searchInvoices(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Invoices.class, requestOptions);
    }

    @Deprecated
    public Invoice createDryRunInvoice(final UUID accountId, @Nullable final LocalDate futureDate, final InvoiceDryRun dryRunInfo, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createDryRunInvoice(accountId, futureDate, dryRunInfo, RequestOptions.builder()
                                                                                    .withCreatedBy(createdBy)
                                                                                    .withReason(reason)
                                                                                    .withComment(comment).build());
    }

    public Invoice createDryRunInvoice(final UUID accountId, @Nullable final LocalDate futureDate, final InvoiceDryRun dryRunInfo, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + JaxrsResource.DRY_RUN;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());

        final String futureDateOrUpcomingNextInvoice = (futureDate != null) ? futureDate.toString() : null;
        if (futureDateOrUpcomingNextInvoice != null) {
            queryParams.put(JaxrsResource.QUERY_ACCOUNT_ID, accountId.toString());
            queryParams.put(JaxrsResource.QUERY_TARGET_DATE, futureDateOrUpcomingNextInvoice);
            queryParams.put(JaxrsResource.QUERY_DRY_RUN, "true");
        } else {
            queryParams.put(JaxrsResource.QUERY_ACCOUNT_ID, accountId.toString());
            queryParams.put(JaxrsResource.QUERY_DRY_RUN, "true");
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doPost(uri, dryRunInfo, Invoice.class, requestOptions);
    }

    @Deprecated
    public Invoice createInvoice(final UUID accountId, final LocalDate futureDate, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createInvoice(accountId, futureDate, RequestOptions.builder()
                                                                  .withCreatedBy(createdBy)
                                                                  .withReason(reason)
                                                                  .withComment(comment).build());
    }

    public Invoice createInvoice(final UUID accountId, final LocalDate futureDate, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_ACCOUNT_ID, accountId.toString());
        queryParams.put(JaxrsResource.QUERY_TARGET_DATE, futureDate.toString());

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, null, Invoice.class, requestOptions);
    }

    @Deprecated
    public Invoice adjustInvoiceItem(final InvoiceItem invoiceItem, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return adjustInvoiceItem(invoiceItem, RequestOptions.builder()
                                                            .withCreatedBy(createdBy)
                                                            .withReason(reason)
                                                            .withComment(comment).build());
    }

    public Invoice adjustInvoiceItem(final InvoiceItem invoiceItem, final RequestOptions inputOptions) throws KillBillClientException {
        return adjustInvoiceItem(invoiceItem, new DateTime(DateTimeZone.UTC), inputOptions);
    }

    @Deprecated
    public Invoice adjustInvoiceItem(final InvoiceItem invoiceItem, final DateTime requestedDate, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return adjustInvoiceItem(invoiceItem, requestedDate, RequestOptions.builder()
                                                                           .withCreatedBy(createdBy)
                                                                           .withReason(reason)
                                                                           .withComment(comment).build());
    }

    public Invoice adjustInvoiceItem(final InvoiceItem invoiceItem, final DateTime requestedDate, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItem.getAccountId(), "InvoiceItem#accountId cannot be null");
        Preconditions.checkNotNull(invoiceItem.getInvoiceId(), "InvoiceItem#invoiceId cannot be null");
        Preconditions.checkNotNull(invoiceItem.getInvoiceItemId(), "InvoiceItem#invoiceItemId cannot be null");

        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceItem.getInvoiceId();

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, invoiceItem, Invoice.class, requestOptions);
    }

    @Deprecated
    public InvoiceItem createExternalCharge(final InvoiceItem externalCharge, final DateTime requestedDate, final Boolean autoPay, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createExternalCharge(externalCharge, requestedDate, autoPay, RequestOptions.builder()
                                                                                          .withCreatedBy(createdBy)
                                                                                          .withReason(reason)
                                                                                          .withComment(comment).build());
    }

    public InvoiceItem createExternalCharge(final InvoiceItem externalCharge, final DateTime requestedDate, final Boolean autoPay, final RequestOptions inputOptions) throws KillBillClientException {
        final List<InvoiceItem> externalCharges = createExternalCharges(ImmutableList.<InvoiceItem>of(externalCharge), requestedDate, autoPay, inputOptions);
        return externalCharges.isEmpty() ? null : externalCharges.get(0);
    }

    @Deprecated
    public List<InvoiceItem> createExternalCharges(final Iterable<InvoiceItem> externalCharges, final DateTime requestedDate, final Boolean autoPay, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createExternalCharges(externalCharges, requestedDate, autoPay, RequestOptions.builder()
                                                                                            .withCreatedBy(createdBy)
                                                                                            .withReason(reason)
                                                                                            .withComment(comment).build());
    }

    public List<InvoiceItem> createExternalCharges(final Iterable<InvoiceItem> externalCharges, final DateTime requestedDate, final Boolean autoPay, final RequestOptions inputOptions) throws KillBillClientException {
        final Map<UUID, Collection<InvoiceItem>> externalChargesPerAccount = new HashMap<UUID, Collection<InvoiceItem>>();
        for (final InvoiceItem externalCharge : externalCharges) {
            Preconditions.checkNotNull(externalCharge.getAccountId(), "InvoiceItem#accountId cannot be null");
            Preconditions.checkNotNull(externalCharge.getAmount(), "InvoiceItem#amount cannot be null");
            // We allow the currency to be null and in this case will default to account currency
            //Preconditions.checkNotNull(externalCharge.getCurrency(), "InvoiceItem#currency cannot be null");

            if (externalChargesPerAccount.get(externalCharge.getAccountId()) == null) {
                externalChargesPerAccount.put(externalCharge.getAccountId(), new LinkedList<InvoiceItem>());
            }
            externalChargesPerAccount.get(externalCharge.getAccountId()).add(externalCharge);
        }

        final List<InvoiceItem> createdExternalCharges = new LinkedList<InvoiceItem>();
        for (final UUID accountId : externalChargesPerAccount.keySet()) {
            final List<InvoiceItem> invoiceItems = createExternalCharges(accountId, externalChargesPerAccount.get(accountId), requestedDate, autoPay, inputOptions);
            createdExternalCharges.addAll(invoiceItems);
        }

        return createdExternalCharges;
    }

    private List<InvoiceItem> createExternalCharges(final UUID accountId, final Iterable<InvoiceItem> externalCharges, final DateTime requestedDate, final Boolean autoPay, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + JaxrsResource.CHARGES + "/" + accountId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());
        queryParams.put(JaxrsResource.QUERY_PAY_INVOICE, autoPay.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doPost(uri, externalCharges, InvoiceItems.class, requestOptions);
    }

    @Deprecated
    public void triggerInvoiceNotification(final UUID invoiceId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        triggerInvoiceNotification(invoiceId, RequestOptions.builder()
                                                            .withCreatedBy(createdBy)
                                                            .withReason(reason)
                                                            .withComment(comment).build());
    }

    public void triggerInvoiceNotification(final UUID invoiceId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId.toString() + "/" + JaxrsResource.EMAIL_NOTIFICATIONS;

        httpClient.doPost(uri, null, inputOptions);
    }

    @Deprecated
    public void uploadInvoiceTemplate(final String invoiceTemplateFilePath, final boolean manualPay, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadInvoiceTemplate(invoiceTemplateFilePath, manualPay, RequestOptions.builder()
                                                                                .withCreatedBy(createdBy)
                                                                                .withReason(reason)
                                                                                .withComment(comment).build());
    }

    public void uploadInvoiceTemplate(final String invoiceTemplateFilePath, final boolean manualPay, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + (manualPay ? "/manualPayTemplate" : "/template");
        uploadFile(invoiceTemplateFilePath, uri, "text/html", inputOptions, null);
    }

    @Deprecated
    public void uploadInvoiceTemplate(final InputStream invoiceTemplateInputStream, final boolean manualPay, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadInvoiceTemplate(invoiceTemplateInputStream, manualPay, RequestOptions.builder()
                                                                                   .withCreatedBy(createdBy)
                                                                                   .withReason(reason)
                                                                                   .withComment(comment).build());
    }

    public void uploadInvoiceTemplate(final InputStream invoiceTemplateInputStream, final boolean manualPay, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + (manualPay ? "/manualPayTemplate" : "/template");
        uploadFile(invoiceTemplateInputStream, uri, "text/html", inputOptions, null);
    }

    @Deprecated
    public String getInvoiceTemplate(final boolean manualPay) throws KillBillClientException {
        return getInvoiceTemplate(manualPay, RequestOptions.empty());
    }

    public String getInvoiceTemplate(final boolean manualPay, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + (manualPay ? "/manualPayTemplate" : "/template");
        return getResourceFile(uri, "text/html", inputOptions);
    }

    @Deprecated
    public void uploadInvoiceTranslation(final String invoiceTranslationFilePath, final String locale, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadInvoiceTranslation(invoiceTranslationFilePath, locale, RequestOptions.builder()
                                                                                   .withCreatedBy(createdBy)
                                                                                   .withReason(reason)
                                                                                   .withComment(comment).build());
    }

    public void uploadInvoiceTranslation(final String invoiceTranslationFilePath, final String locale, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + "/translation/" + locale;
        uploadFile(invoiceTranslationFilePath, uri, "text/plain", inputOptions, null);
    }

    @Deprecated
    public void uploadInvoiceTranslation(final InputStream invoiceTranslationInputStream, final String locale, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadInvoiceTranslation(invoiceTranslationInputStream, locale, RequestOptions.builder()
                                                                                      .withCreatedBy(createdBy)
                                                                                      .withReason(reason)
                                                                                      .withComment(comment).build());
    }

    public void uploadInvoiceTranslation(final InputStream invoiceTranslationInputStream, final String locale, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + "/translation/" + locale;
        uploadFile(invoiceTranslationInputStream, uri, "text/plain", inputOptions, null);
    }

    @Deprecated
    public String getInvoiceTranslation(final String locale) throws KillBillClientException {
        return getInvoiceTranslation(locale, RequestOptions.empty());
    }

    public String getInvoiceTranslation(final String locale, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + "/translation/" + locale;
        return getResourceFile(uri, "text/plain", inputOptions);
    }

    @Deprecated
    public void uploadCatalogTranslation(final String catalogTranslationFilePath, final String locale, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadCatalogTranslation(catalogTranslationFilePath, locale, RequestOptions.builder()
                                                                                   .withCreatedBy(createdBy)
                                                                                   .withReason(reason)
                                                                                   .withComment(comment).build());
    }

    public void uploadCatalogTranslation(final String catalogTranslationFilePath, final String locale, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + "/catalogTranslation/" + locale;
        uploadFile(catalogTranslationFilePath, uri, "text/plain", inputOptions, null);
    }

    @Deprecated
    public void uploadCatalogTranslation(final InputStream catalogTranslationInputStream, final String locale, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadCatalogTranslation(catalogTranslationInputStream, locale, RequestOptions.builder()
                                                                                      .withCreatedBy(createdBy)
                                                                                      .withReason(reason)
                                                                                      .withComment(comment).build());
    }

    public void uploadCatalogTranslation(final InputStream catalogTranslationInputStream, final String locale, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + "/catalogTranslation/" + locale;
        uploadFile(catalogTranslationInputStream, uri, "text/plain", inputOptions, null);
    }

    @Deprecated
    public String getCatalogTranslation(final String locale) throws KillBillClientException {
        return getCatalogTranslation(locale, RequestOptions.empty());
    }

    public String getCatalogTranslation(final String locale, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES + "/catalogTranslation/" + locale;
        return getResourceFile(uri, "text/plain", inputOptions);
    }

    // Credits

    @Deprecated
    public Credit getCredit(final UUID creditId) throws KillBillClientException {
        return getCredit(creditId, RequestOptions.empty());
    }

    public Credit getCredit(final UUID creditId, final RequestOptions inputOptions) throws KillBillClientException {
        return getCredit(creditId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Credit getCredit(final UUID creditId, final AuditLevel auditLevel) throws KillBillClientException {
        return getCredit(creditId, auditLevel, RequestOptions.empty());
    }

    public Credit getCredit(final UUID creditId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CREDITS_PATH + "/" + creditId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Credit.class, requestOptions);
    }

    @Deprecated
    public Credit createCredit(final Credit credit, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createCredit(credit, RequestOptions.builder()
                                                  .withCreatedBy(createdBy)
                                                  .withReason(reason)
                                                  .withComment(comment).build());
    }

    public Credit createCredit(final Credit credit, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(credit.getAccountId(), "Credt#accountId cannot be null");
        Preconditions.checkNotNull(credit.getCreditAmount(), "Credt#creditAmount cannot be null");

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(JaxrsResource.CREDITS_PATH, credit, Credit.class, requestOptions);
    }

    @Deprecated
    public Payments searchPayments(final String key) throws KillBillClientException {
        return searchPayments(key, RequestOptions.empty());
    }

    public Payments searchPayments(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPayments(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public Payments searchPayments(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchPayments(key, offset, limit, RequestOptions.empty());
    }

    public Payments searchPayments(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPayments(key, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Payments searchPayments(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchPayments(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Payments searchPayments(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Payments.class, requestOptions);
    }

    @Deprecated
    public InvoicePayments getInvoicePaymentsForAccount(final UUID accountId) throws KillBillClientException {
        return getInvoicePaymentsForAccount(accountId, RequestOptions.empty());
    }

    public InvoicePayments getInvoicePaymentsForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoicePaymentsForAccount(accountId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public InvoicePayments getInvoicePaymentsForAccount(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoicePaymentsForAccount(accountId, auditLevel, RequestOptions.empty());
    }

    public InvoicePayments getInvoicePaymentsForAccount(final UUID accountId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.INVOICE_PAYMENTS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, InvoicePayments.class, requestOptions);
    }

    @Deprecated
    public InvoicePayments getInvoicePayment(final UUID invoiceId) throws KillBillClientException {
        return getInvoicePayment(invoiceId, RequestOptions.empty());
    }

    public InvoicePayments getInvoicePayment(final UUID invoiceId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId + "/" + JaxrsResource.PAYMENTS;

        return httpClient.doGet(uri, InvoicePayments.class, inputOptions);
    }

    @Deprecated
    public void payAllInvoices(final UUID accountId, final boolean externalPayment, final BigDecimal paymentAmount, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        payAllInvoices(accountId, externalPayment, paymentAmount, RequestOptions.builder()
                                                                                .withCreatedBy(createdBy)
                                                                                .withReason(reason)
                                                                                .withComment(comment).build());
    }

    public void payAllInvoices(final UUID accountId, final boolean externalPayment, final BigDecimal paymentAmount, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.INVOICE_PAYMENTS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_PAYMENT_EXTERNAL, String.valueOf(externalPayment));
        if (paymentAmount != null) {
            queryParams.put("paymentAmount", String.valueOf(paymentAmount));
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doPost(uri, null, requestOptions);
    }

    @Deprecated
    public InvoicePayment createInvoicePayment(final InvoicePayment payment, final boolean isExternal, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createInvoicePayment(payment, isExternal, RequestOptions.builder()
                                                                       .withCreatedBy(createdBy)
                                                                       .withReason(reason)
                                                                       .withComment(comment).build());
    }

    public InvoicePayment createInvoicePayment(final InvoicePayment payment, final boolean isExternal, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(payment.getAccountId(), "InvoiceItem#accountId cannot be null");
        Preconditions.checkNotNull(payment.getTargetInvoiceId(), "InvoiceItem#invoiceId cannot be null");
        Preconditions.checkNotNull(payment.getPurchasedAmount(), "InvoiceItem#amount cannot be null");

        final String uri = JaxrsResource.INVOICES_PATH + "/" + payment.getTargetInvoiceId() + "/" + JaxrsResource.PAYMENTS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put("externalPayment", String.valueOf(isExternal));

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, payment, InvoicePayment.class, requestOptions);
    }

    @Deprecated
    public Payments getPayments() throws KillBillClientException {
        return getPayments(RequestOptions.empty());
    }

    public Payments getPayments(final RequestOptions inputOptions) throws KillBillClientException {
        return getPayments(0L, 100L, inputOptions);
    }

    @Deprecated
    public Payments getPayments(final Long offset, final Long limit) throws KillBillClientException {
        return getPayments(offset, limit, RequestOptions.empty());
    }

    public Payments getPayments(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getPayments(offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Payments getPayments(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getPayments(offset, limit, auditLevel, RequestOptions.empty());
    }

    public Payments getPayments(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getPayments(offset, limit, null, ImmutableMap.<String, String>of(), auditLevel, inputOptions);
    }

    @Deprecated
    public Payments getPayments(final Long offset, final Long limit, @Nullable final String pluginName, final Map<String, String> pluginProperties, final AuditLevel auditLevel) throws KillBillClientException {
        return getPayments(offset, limit, pluginName, pluginProperties, auditLevel, RequestOptions.empty());
    }

    public Payments getPayments(final Long offset, final Long limit, @Nullable final String pluginName, final Map<String, String> pluginProperties, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (pluginName != null) {
            queryParams.put(JaxrsResource.QUERY_PAYMENT_PLUGIN_NAME, pluginName);
        }
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());
        storePluginPropertiesAsParams(pluginProperties, queryParams);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Payments.class, requestOptions);
    }

    @Deprecated
    public Payment getPayment(final UUID paymentId) throws KillBillClientException {
        return getPayment(paymentId, RequestOptions.empty());
    }

    public Payment getPayment(final UUID paymentId, final RequestOptions inputOptions) throws KillBillClientException {
        return getPayment(paymentId, true, inputOptions);
    }

    @Deprecated
    public Payment getPayment(final UUID paymentId, final boolean withPluginInfo) throws KillBillClientException {
        return getPayment(paymentId, withPluginInfo, RequestOptions.empty());
    }

    public Payment getPayment(final UUID paymentId, final boolean withPluginInfo, final RequestOptions inputOptions) throws KillBillClientException {
        return getPayment(paymentId, withPluginInfo, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Payment getPayment(final UUID paymentId, final boolean withPluginInfo, final AuditLevel auditLevel) throws KillBillClientException {
        return getPayment(paymentId, withPluginInfo, auditLevel, RequestOptions.empty());
    }

    public Payment getPayment(final UUID paymentId, final boolean withPluginInfo, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getPayment(paymentId, withPluginInfo, ImmutableMap.<String, String>of(), auditLevel, inputOptions);
    }

    @Deprecated
    public Payment getPayment(final UUID paymentId, final boolean withPluginInfo, final Map<String, String> pluginProperties, final AuditLevel auditLevel) throws KillBillClientException {
        return getPayment(paymentId, withPluginInfo, pluginProperties, auditLevel, RequestOptions.empty());
    }

    public Payment getPayment(final UUID paymentId, final boolean withPluginInfo, final Map<String, String> pluginProperties, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + paymentId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_WITH_PLUGIN_INFO, String.valueOf(withPluginInfo));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());
        storePluginPropertiesAsParams(pluginProperties, queryParams);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment getPaymentByExternalKey(final String externalKey) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, RequestOptions.empty());
    }

    public Payment getPaymentByExternalKey(final String externalKey, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, true, inputOptions);
    }

    @Deprecated
    public Payment getPaymentByExternalKey(final String externalKey, final boolean withPluginInfo) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, withPluginInfo, RequestOptions.empty());
    }

    public Payment getPaymentByExternalKey(final String externalKey, final boolean withPluginInfo, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, withPluginInfo, AuditLevel.NONE);
    }

    @Deprecated
    public Payment getPaymentByExternalKey(final String externalKey, final boolean withPluginInfo, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, withPluginInfo, auditLevel, RequestOptions.empty());
    }

    public Payment getPaymentByExternalKey(final String externalKey, final boolean withPluginInfo, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, withPluginInfo, ImmutableMap.<String, String>of(), auditLevel, inputOptions);
    }

    @Deprecated
    public Payment getPaymentByExternalKey(final String externalKey, final boolean withPluginInfo, final Map<String, String> pluginProperties, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentByExternalKey(externalKey, withPluginInfo, pluginProperties, auditLevel, RequestOptions.empty());
    }

    public Payment getPaymentByExternalKey(final String externalKey, final boolean withPluginInfo, final Map<String, String> pluginProperties, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);
        queryParams.put(JaxrsResource.QUERY_WITH_PLUGIN_INFO, String.valueOf(withPluginInfo));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());
        storePluginPropertiesAsParams(pluginProperties, queryParams);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Payment.class, requestOptions);
    }

    @Deprecated
    public Payments getPaymentsForAccount(final UUID accountId) throws KillBillClientException {
        return getPaymentsForAccount(accountId, RequestOptions.empty());
    }

    public Payments getPaymentsForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentsForAccount(accountId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Payments getPaymentsForAccount(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentsForAccount(accountId, auditLevel, RequestOptions.empty());
    }

    public Payments getPaymentsForAccount(final UUID accountId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENTS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Payments.class, requestOptions);
    }

    @Deprecated
    public Payment createPayment(final ComboPaymentTransaction comboPaymentTransaction, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return this.createPayment(comboPaymentTransaction, pluginProperties, RequestOptions.builder()
                                                                                           .withCreatedBy(createdBy)
                                                                                           .withReason(reason)
                                                                                           .withComment(comment).build());
    }

    public Payment createPayment(final ComboPaymentTransaction comboPaymentTransaction, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        return this.createPayment(comboPaymentTransaction, null, pluginProperties, inputOptions);
    }

    @Deprecated
    public Payment createPayment(final ComboPaymentTransaction comboPaymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPayment(comboPaymentTransaction, controlPluginNames, pluginProperties, RequestOptions.builder()
                                                                                                          .withCreatedBy(createdBy)
                                                                                                          .withReason(reason)
                                                                                                          .withComment(comment).build());
    }

    public Payment createPayment(final ComboPaymentTransaction comboPaymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.COMBO;

        final Multimap<String, String> queryParams = LinkedListMultimap.create(inputOptions.getQueryParams());
        if (controlPluginNames != null) {
            queryParams.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }
        storePluginPropertiesAsParams(pluginProperties, queryParams);

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(queryParams)
                                                          .withFollowLocation(followLocation).build();
        return httpClient.doPost(uri, comboPaymentTransaction, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment createPayment(final UUID accountId, final PaymentTransaction paymentTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPayment(accountId, paymentTransaction, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment).build());
    }

    public Payment createPayment(final UUID accountId, final PaymentTransaction paymentTransaction, final RequestOptions inputOptions) throws KillBillClientException {
        return createPayment(accountId, null, paymentTransaction, null, ImmutableMap.<String, String>of(), inputOptions);
    }

    @Deprecated
    public Payment createPayment(final UUID accountId, final PaymentTransaction paymentTransaction, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPayment(accountId, paymentTransaction, pluginProperties, RequestOptions.builder()
                                                                                            .withCreatedBy(createdBy)
                                                                                            .withReason(reason)
                                                                                            .withComment(comment).build());
    }

    public Payment createPayment(final UUID accountId, final PaymentTransaction paymentTransaction, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        return createPayment(accountId, null, paymentTransaction, null, pluginProperties, inputOptions);
    }

    @Deprecated
    public Payment createPayment(final UUID accountId, @Nullable final UUID paymentMethodId, final PaymentTransaction paymentTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPayment(accountId, paymentMethodId, paymentTransaction, RequestOptions.builder()
                                                                                           .withCreatedBy(createdBy)
                                                                                           .withReason(reason)
                                                                                           .withComment(comment).build());
    }

    public Payment createPayment(final UUID accountId, @Nullable final UUID paymentMethodId, final PaymentTransaction paymentTransaction, final RequestOptions inputOptions) throws KillBillClientException {
        return createPayment(accountId, paymentMethodId, paymentTransaction, null, ImmutableMap.<String, String>of(), inputOptions);
    }

    @Deprecated
    public Payment createPayment(final UUID accountId, @Nullable final UUID paymentMethodId, final PaymentTransaction paymentTransaction, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPayment(accountId, paymentMethodId, paymentTransaction, pluginProperties, RequestOptions.builder()
                                                                                                             .withCreatedBy(createdBy)
                                                                                                             .withReason(reason)
                                                                                                             .withComment(comment)
                                                                                                             .build());
    }

    public Payment createPayment(final UUID accountId, @Nullable final UUID paymentMethodId, final PaymentTransaction paymentTransaction, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        return createPayment(accountId, paymentMethodId, paymentTransaction, null, pluginProperties, inputOptions);
    }

    public Payment createPayment(final UUID accountId, @Nullable final UUID paymentMethodId, final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(accountId, "accountId cannot be null");
        Preconditions.checkNotNull(paymentTransaction.getTransactionType(), "PaymentTransaction#transactionType cannot be null");
        Preconditions.checkArgument("AUTHORIZE".equals(paymentTransaction.getTransactionType()) ||
                                    "CREDIT".equals(paymentTransaction.getTransactionType()) ||
                                    "PURCHASE".equals(paymentTransaction.getTransactionType()),
                                    "Invalid paymentTransaction type " + paymentTransaction.getTransactionType()
                                   );
        Preconditions.checkNotNull(paymentTransaction.getAmount(), "PaymentTransaction#amount cannot be null");
        Preconditions.checkNotNull(paymentTransaction.getCurrency(), "PaymentTransaction#currency cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENTS;

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        if (paymentMethodId != null) {
            params.put("paymentMethodId", paymentMethodId.toString());
        }
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }
        storePluginPropertiesAsParams(pluginProperties, params);

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(followLocation).build();
        return httpClient.doPost(uri, paymentTransaction, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment completePayment(final PaymentTransaction paymentTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return completePayment(paymentTransaction, RequestOptions.builder()
                                                                 .withCreatedBy(createdBy)
                                                                 .withReason(reason)
                                                                 .withComment(comment)
                                                                 .build());
    }

    public Payment completePayment(final PaymentTransaction paymentTransaction, final RequestOptions requestOptions) throws KillBillClientException {
        return completePayment(paymentTransaction, ImmutableMap.<String, String>of(), requestOptions);
    }

    @Deprecated
    public Payment completePayment(final PaymentTransaction paymentTransaction, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return completePayment(paymentTransaction, pluginProperties, RequestOptions.builder()
                                                                                   .withCreatedBy(createdBy)
                                                                                   .withReason(reason)
                                                                                   .withComment(comment)
                                                                                   .build());
    }

    public Payment completePayment(final PaymentTransaction paymentTransaction, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkState(paymentTransaction.getPaymentId() != null || paymentTransaction.getPaymentExternalKey() != null, "PaymentTransaction#paymentId or PaymentTransaction#paymentExternalKey cannot be null");

        final String uri = (paymentTransaction.getPaymentId() != null) ?
                           JaxrsResource.PAYMENTS_PATH + "/" + paymentTransaction.getPaymentId() :
                           JaxrsResource.PAYMENTS_PATH;

        final Multimap<String, String> params = HashMultimap.create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);
        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(followLocation).build();

        return httpClient.doPut(uri, paymentTransaction, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment captureAuthorization(final PaymentTransaction paymentTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return captureAuthorization(paymentTransaction, RequestOptions.builder()
                                                                      .withCreatedBy(createdBy)
                                                                      .withReason(reason)
                                                                      .withComment(comment)
                                                                      .build());
    }

    public Payment captureAuthorization(final PaymentTransaction paymentTransaction, final RequestOptions inputOptions) throws KillBillClientException {
        return captureAuthorization(paymentTransaction, null, ImmutableMap.<String, String>of(), inputOptions);
    }

    @Deprecated
    public Payment captureAuthorization(final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return captureAuthorization(paymentTransaction, controlPluginNames, pluginProperties, RequestOptions.builder()
                                                                                                            .withCreatedBy(createdBy)
                                                                                                            .withReason(reason)
                                                                                                            .withComment(comment)
                                                                                                            .build());
    }

    public Payment captureAuthorization(final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkState(paymentTransaction.getPaymentId() != null || paymentTransaction.getPaymentExternalKey() != null, "PaymentTransaction#paymentId or PaymentTransaction#paymentExternalKey cannot be null");
        Preconditions.checkNotNull(paymentTransaction.getAmount(), "PaymentTransaction#amount cannot be null");

        final String uri = (paymentTransaction.getPaymentId() != null) ?
                           JaxrsResource.PAYMENTS_PATH + "/" + paymentTransaction.getPaymentId() :
                           JaxrsResource.PAYMENTS_PATH;

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, paymentTransaction, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment refundPayment(final PaymentTransaction paymentTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return refundPayment(paymentTransaction, RequestOptions.builder()
                                                               .withCreatedBy(createdBy)
                                                               .withReason(reason)
                                                               .withComment(comment)
                                                               .build());
    }

    public Payment refundPayment(final PaymentTransaction paymentTransaction, final RequestOptions inputOptions) throws KillBillClientException {
        return refundPayment(paymentTransaction, null, ImmutableMap.<String, String>of(), inputOptions);
    }

    @Deprecated
    public Payment refundPayment(final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return refundPayment(paymentTransaction, controlPluginNames, pluginProperties, RequestOptions.builder()
                                                                                                     .withCreatedBy(createdBy)
                                                                                                     .withReason(reason)
                                                                                                     .withComment(comment)
                                                                                                     .build());
    }

    public Payment refundPayment(final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkState(paymentTransaction.getPaymentId() != null || paymentTransaction.getPaymentExternalKey() != null, "PaymentTransaction#paymentId or PaymentTransaction#paymentExternalKey cannot be null");
        Preconditions.checkNotNull(paymentTransaction.getAmount(), "PaymentTransaction#amount cannot be null");

        final String uri = (paymentTransaction.getPaymentId() != null) ?
                           JaxrsResource.PAYMENTS_PATH + "/" + paymentTransaction.getPaymentId() + "/" + JaxrsResource.REFUNDS :
                           JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.REFUNDS;

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, paymentTransaction, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment chargebackPayment(final PaymentTransaction paymentTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return chargebackPayment(paymentTransaction, RequestOptions.builder()
                                                                   .withCreatedBy(createdBy)
                                                                   .withReason(reason)
                                                                   .withComment(comment)
                                                                   .build());
    }

    public Payment chargebackPayment(final PaymentTransaction paymentTransaction, final RequestOptions requestOptions) throws KillBillClientException {
        return chargebackPayment(paymentTransaction, null, ImmutableMap.<String, String>of(), requestOptions);
    }

    @Deprecated
    public Payment chargebackPayment(final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return chargebackPayment(paymentTransaction, controlPluginNames, pluginProperties, RequestOptions.builder()
                                                                                                         .withCreatedBy(createdBy)
                                                                                                         .withReason(reason)
                                                                                                         .withComment(comment)
                                                                                                         .build());
    }

    public Payment chargebackPayment(final PaymentTransaction paymentTransaction, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkState(paymentTransaction.getPaymentId() != null || paymentTransaction.getPaymentExternalKey() != null, "PaymentTransaction#paymentId or PaymentTransaction#paymentExternalKey cannot be null");
        Preconditions.checkNotNull(paymentTransaction.getAmount(), "PaymentTransaction#amount cannot be null");

        final String uri = (paymentTransaction.getPaymentId() != null) ?
                           JaxrsResource.PAYMENTS_PATH + "/" + paymentTransaction.getPaymentId() + "/" + JaxrsResource.CHARGEBACKS :
                           JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.CHARGEBACKS;

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }
        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, paymentTransaction, Payment.class, requestOptions);
    }

    @Deprecated
    public Payment voidPayment(final UUID paymentId, final String transactionExternalKey, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return voidPayment(paymentId, transactionExternalKey, RequestOptions.builder()
                                                                            .withCreatedBy(createdBy)
                                                                            .withReason(reason)
                                                                            .withComment(comment)
                                                                            .build());
    }

    public Payment voidPayment(final UUID paymentId, final String transactionExternalKey, final RequestOptions inputOptions) throws KillBillClientException {
        return voidPayment(paymentId, null, transactionExternalKey, null, ImmutableMap.<String, String>of(), inputOptions);
    }

    @Deprecated
    public Payment voidPayment(final UUID paymentId, final String paymentExternalKey, final String transactionExternalKey, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return voidPayment(paymentId, paymentExternalKey, transactionExternalKey, controlPluginNames, pluginProperties, RequestOptions.builder()
                                                                                                                                      .withCreatedBy(createdBy)
                                                                                                                                      .withReason(reason)
                                                                                                                                      .withComment(comment)
                                                                                                                                      .build());
    }

    public Payment voidPayment(final UUID paymentId, final String paymentExternalKey, final String transactionExternalKey, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {

        Preconditions.checkState(paymentId != null || paymentExternalKey != null, "PaymentTransaction#paymentId or PaymentTransaction#paymentExternalKey cannot be null");
        final String uri = (paymentId != null) ?
                           JaxrsResource.PAYMENTS_PATH + "/" + paymentId :
                           JaxrsResource.PAYMENTS_PATH;

        final PaymentTransaction paymentTransaction = new PaymentTransaction();
        if (paymentExternalKey != null) {
            paymentTransaction.setPaymentExternalKey(paymentExternalKey);
        }
        paymentTransaction.setTransactionExternalKey(transactionExternalKey);

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(followLocation).build();
        return httpClient.doDelete(uri, paymentTransaction, Payment.class, requestOptions);
    }

    // Hosted payment pages
    @Deprecated
    public HostedPaymentPageFormDescriptor buildFormDescriptor(final HostedPaymentPageFields fields, final UUID kbAccountId, @Nullable final UUID kbPaymentMethodId, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return buildFormDescriptor(fields, kbAccountId, kbPaymentMethodId, pluginProperties, RequestOptions.builder()
                                                                                                           .withCreatedBy(createdBy)
                                                                                                           .withReason(reason)
                                                                                                           .withComment(comment)
                                                                                                           .build());
    }

    public HostedPaymentPageFormDescriptor buildFormDescriptor(final HostedPaymentPageFields fields, final UUID kbAccountId, @Nullable final UUID kbPaymentMethodId, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        return buildFormDescriptor(fields, kbAccountId, kbPaymentMethodId, null, pluginProperties, inputOptions);
    }

    public HostedPaymentPageFormDescriptor buildFormDescriptor(final HostedPaymentPageFields fields, final UUID kbAccountId, @Nullable final UUID kbPaymentMethodId, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_GATEWAYS_PATH + "/" + JaxrsResource.HOSTED + "/" + JaxrsResource.FORM + "/" + kbAccountId;

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }
        if (kbPaymentMethodId != null) {
            params.put(JaxrsResource.QUERY_PAYMENT_METHOD_ID, kbPaymentMethodId.toString());
        }

        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(false).build();

        return httpClient.doPost(uri, fields, HostedPaymentPageFormDescriptor.class, requestOptions);
    }

    @Deprecated
    public HostedPaymentPageFormDescriptor buildFormDescriptor(final ComboHostedPaymentPage comboHostedPaymentPage, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return buildFormDescriptor(comboHostedPaymentPage, pluginProperties, RequestOptions.builder()
                                                                                           .withCreatedBy(createdBy)
                                                                                           .withReason(reason)
                                                                                           .withComment(comment)
                                                                                           .build());
    }

    public HostedPaymentPageFormDescriptor buildFormDescriptor(final ComboHostedPaymentPage comboHostedPaymentPage, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        return buildFormDescriptor(comboHostedPaymentPage, null, pluginProperties, inputOptions);
    }

    @Deprecated
    public HostedPaymentPageFormDescriptor buildFormDescriptor(final ComboHostedPaymentPage comboHostedPaymentPage, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return buildFormDescriptor(comboHostedPaymentPage, controlPluginNames, pluginProperties, RequestOptions.builder()
                                                                                                 .withCreatedBy(createdBy)
                                                                                                 .withReason(reason)
                                                                                                 .withComment(comment)
                                                                                                 .build());
    }

    public HostedPaymentPageFormDescriptor buildFormDescriptor(final ComboHostedPaymentPage comboHostedPaymentPage, @Nullable final List<String> controlPluginNames, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_GATEWAYS_PATH + "/" + JaxrsResource.HOSTED + "/" + JaxrsResource.FORM;

        final Multimap<String, String> params = LinkedListMultimap.create(inputOptions.getQueryParams());
        if (controlPluginNames != null) {
            params.putAll(KillBillHttpClient.CONTROL_PLUGIN_NAME, controlPluginNames);
        }
        storePluginPropertiesAsParams(pluginProperties, params);

        final RequestOptions requestOptions = inputOptions.extend()
                                                          .withQueryParams(params)
                                                          .withFollowLocation(false).build();

        return httpClient.doPost(uri, comboHostedPaymentPage, HostedPaymentPageFormDescriptor.class, requestOptions);
    }

    @Deprecated
    public Response processNotification(final String notification, final String pluginName, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return processNotification(notification, pluginName, pluginProperties, RequestOptions.builder()
                                                                                             .withCreatedBy(createdBy)
                                                                                             .withReason(reason)
                                                                                             .withComment(comment)
                                                                                             .build());
    }

    public Response processNotification(final String notification, final String pluginName, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_GATEWAYS_PATH + "/" + JaxrsResource.NOTIFICATION + "/" + pluginName;

        final Multimap<String, String> params = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        storePluginPropertiesAsParams(pluginProperties, params);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(params).build();

        return httpClient.doPost(uri, notification, requestOptions);
    }

    @Deprecated
    public InvoicePayment createInvoicePaymentRefund(final InvoicePaymentTransaction refundTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createInvoicePaymentRefund(refundTransaction, RequestOptions.builder()
                                                                           .withCreatedBy(createdBy)
                                                                           .withReason(reason)
                                                                           .withComment(comment)
                                                                           .build());
    }

    public InvoicePayment createInvoicePaymentRefund(final InvoicePaymentTransaction refundTransaction, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(refundTransaction.getPaymentId(), "InvoicePaymentTransaction#paymentId cannot be null");

        // Specify isAdjusted for invoice adjustment and invoice item adjustment
        // Specify adjustments for invoice item adjustments only
        if (refundTransaction.getAdjustments() != null) {
            for (final InvoiceItem invoiceItem : refundTransaction.getAdjustments()) {
                Preconditions.checkNotNull(invoiceItem.getInvoiceItemId(), "InvoiceItem#invoiceItemId cannot be null");
            }
        }

        final String uri = JaxrsResource.INVOICE_PAYMENTS_PATH + "/" + refundTransaction.getPaymentId() + "/" + JaxrsResource.REFUNDS;

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, refundTransaction, InvoicePayment.class, requestOptions);
    }

    // Chargebacks

    @Deprecated
    public InvoicePayment createInvoicePaymentChargeback(final InvoicePaymentTransaction chargebackTransaction, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createInvoicePaymentChargeback(chargebackTransaction, RequestOptions.builder()
                                                                                   .withCreatedBy(createdBy)
                                                                                   .withReason(reason)
                                                                                   .withComment(comment)
                                                                                   .build());
    }

    public InvoicePayment createInvoicePaymentChargeback(final InvoicePaymentTransaction chargebackTransaction, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(chargebackTransaction.getPaymentId(), "InvoicePaymentTransaction#paymentId cannot be null");

        final String uri = JaxrsResource.INVOICE_PAYMENTS_PATH + "/" + chargebackTransaction.getPaymentId() + "/" + JaxrsResource.CHARGEBACKS;

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, chargebackTransaction, InvoicePayment.class, requestOptions);
    }

    // Payment methods

    @Deprecated
    public PaymentMethods getPaymentMethods() throws KillBillClientException {
        return getPaymentMethods(RequestOptions.empty());
    }

    public PaymentMethods getPaymentMethods(final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethods(0L, 100L, inputOptions);
    }

    @Deprecated
    public PaymentMethods getPaymentMethods(final Long offset, final Long limit) throws KillBillClientException {
        return getPaymentMethods(offset, limit, RequestOptions.empty());
    }

    public PaymentMethods getPaymentMethods(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethods(offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public PaymentMethods getPaymentMethods(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentMethods(offset, limit, auditLevel, RequestOptions.empty());
    }

    public PaymentMethods getPaymentMethods(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PaymentMethods.class, requestOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethods(final String key) throws KillBillClientException {
        return searchPaymentMethods(key, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethods(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPaymentMethods(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethods(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchPaymentMethods(key, offset, limit, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethods(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPaymentMethods(key, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethods(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchPaymentMethods(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethods(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PaymentMethods.class, requestOptions);
    }

    @Deprecated
    public PaymentMethod getPaymentMethod(final UUID paymentMethodId) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, RequestOptions.empty());
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, false, inputOptions);
    }

    @Deprecated
    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final boolean withPluginInfo) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, withPluginInfo, RequestOptions.empty());
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final boolean withPluginInfo, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, withPluginInfo, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final boolean withPluginInfo, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, withPluginInfo, auditLevel, RequestOptions.empty());
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final boolean withPluginInfo, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_WITH_PLUGIN_INFO, String.valueOf(withPluginInfo));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PaymentMethod.class, requestOptions);
    }

    @Deprecated
    public PaymentMethod getPaymentMethodByKey(final String externalKey) throws KillBillClientException {
        return getPaymentMethodByKey(externalKey, RequestOptions.empty());
    }

    public PaymentMethod getPaymentMethodByKey(final String externalKey, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethodByKey(externalKey, false, inputOptions);
    }

    @Deprecated
    public PaymentMethod getPaymentMethodByKey(final String externalKey, final boolean withPluginInfo) throws KillBillClientException {
        return getPaymentMethodByKey(externalKey, withPluginInfo, RequestOptions.empty());
    }

    public PaymentMethod getPaymentMethodByKey(final String externalKey, final boolean withPluginInfo, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethodByKey(externalKey, withPluginInfo, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public PaymentMethod getPaymentMethodByKey(final String externalKey, final boolean withPluginInfo, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentMethodByKey(externalKey, withPluginInfo, auditLevel, RequestOptions.empty());
    }

    public PaymentMethod getPaymentMethodByKey(final String externalKey, final boolean withPluginInfo, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);
        queryParams.put(JaxrsResource.QUERY_WITH_PLUGIN_INFO, String.valueOf(withPluginInfo));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PaymentMethod.class, requestOptions);
    }

    @Deprecated
    public PaymentMethods getPaymentMethodsForAccount(final UUID accountId) throws KillBillClientException {
        return getPaymentMethodsForAccount(accountId, RequestOptions.empty());
    }

    public PaymentMethods getPaymentMethodsForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENT_METHODS;
        return httpClient.doGet(uri, PaymentMethods.class, inputOptions);
    }

    @Deprecated
    public PaymentMethods getPaymentMethodsForAccount(final UUID accountId, final Map<String, String> pluginProperties, final boolean withPluginInfo, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentMethodsForAccount(accountId, pluginProperties, withPluginInfo, auditLevel, RequestOptions.empty());
    }

    public PaymentMethods getPaymentMethodsForAccount(final UUID accountId, final Map<String, String> pluginProperties, final boolean withPluginInfo, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENT_METHODS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_WITH_PLUGIN_INFO, String.valueOf(withPluginInfo));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());
        storePluginPropertiesAsParams(pluginProperties, queryParams);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PaymentMethods.class, requestOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethodsByKey(final String key) throws KillBillClientException {
        return searchPaymentMethodsByKey(key, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethodsByKey(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, null, inputOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethodsByKey(final String key, final boolean withPluginInfo) throws KillBillClientException {
        return searchPaymentMethodsByKey(key, withPluginInfo, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethodsByKey(final String key, final boolean withPluginInfo, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, withPluginInfo, null, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, @Nullable final String pluginName) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, pluginName, AuditLevel.NONE, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, @Nullable final String pluginName, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, pluginName, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, @Nullable final String pluginName, final AuditLevel auditLevel) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, pluginName, auditLevel, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, @Nullable final String pluginName, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, pluginName != null, pluginName, auditLevel, inputOptions);
    }

    @Deprecated
    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, final boolean withPluginInfo, @Nullable final String pluginName, final AuditLevel auditLevel) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, withPluginInfo, pluginName, auditLevel, RequestOptions.empty());
    }

    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, final boolean withPluginInfo, @Nullable final String pluginName, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_PAYMENT_METHOD_PLUGIN_NAME, Strings.nullToEmpty(pluginName));
        queryParams.put(JaxrsResource.QUERY_WITH_PLUGIN_INFO, String.valueOf(withPluginInfo));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PaymentMethods.class, requestOptions);
    }

    @Deprecated
    public PaymentMethod createPaymentMethod(final PaymentMethod paymentMethod, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPaymentMethod(paymentMethod, RequestOptions.builder()
                                                                .withCreatedBy(createdBy)
                                                                .withReason(reason)
                                                                .withComment(comment)
                                                                .build());
    }

    public PaymentMethod createPaymentMethod(final PaymentMethod paymentMethod, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(paymentMethod.getAccountId(), "PaymentMethod#accountId cannot be null");
        Preconditions.checkNotNull(paymentMethod.getPluginName(), "PaymentMethod#pluginName cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + paymentMethod.getAccountId() + "/" + JaxrsResource.PAYMENT_METHODS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_PAYMENT_METHOD_IS_DEFAULT, paymentMethod.getIsDefault() ? "true" : "false");

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, paymentMethod, PaymentMethod.class, requestOptions);
    }

    @Deprecated
    public void updateDefaultPaymentMethod(final UUID accountId, final UUID paymentMethodId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        updateDefaultPaymentMethod(accountId, paymentMethodId, RequestOptions.builder()
                                                                             .withCreatedBy(createdBy)
                                                                             .withReason(reason)
                                                                             .withComment(comment)
                                                                             .build());
    }

    public void updateDefaultPaymentMethod(final UUID accountId, final UUID paymentMethodId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENT_METHODS + "/" + paymentMethodId + "/" + JaxrsResource.PAYMENT_METHODS_DEFAULT_PATH_POSTFIX;

        httpClient.doPut(uri, null, inputOptions);
    }

    @Deprecated
    public void deletePaymentMethod(final UUID paymentMethodId, final Boolean deleteDefault, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deletePaymentMethod(paymentMethodId, deleteDefault, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment)
                                                                          .build());
    }

    public void deletePaymentMethod(final UUID paymentMethodId, final Boolean deleteDefault, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_DELETE_DEFAULT_PM_WITH_AUTO_PAY_OFF, deleteDefault.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doDelete(uri, requestOptions);
    }

    @Deprecated
    public void refreshPaymentMethods(final UUID accountId, final String pluginName, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        refreshPaymentMethods(accountId, pluginName, pluginProperties, RequestOptions.builder()
                                                                                     .withCreatedBy(createdBy)
                                                                                     .withReason(reason)
                                                                                     .withComment(comment)
                                                                                     .build());
    }

    public void refreshPaymentMethods(final UUID accountId, final String pluginName, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENT_METHODS + "/refresh";

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (pluginName != null) {
            queryParams.put(JaxrsResource.QUERY_PAYMENT_METHOD_PLUGIN_NAME, pluginName);
        }
        storePluginPropertiesAsParams(pluginProperties, queryParams);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doPost(uri, null, requestOptions);
    }

    @Deprecated
    public void refreshPaymentMethods(final UUID accountId, final Map<String, String> pluginProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        refreshPaymentMethods(accountId, pluginProperties, RequestOptions.builder()
                                                                         .withCreatedBy(createdBy)
                                                                         .withReason(reason)
                                                                         .withComment(comment)
                                                                         .build());
    }

    public void refreshPaymentMethods(final UUID accountId, final Map<String, String> pluginProperties, final RequestOptions inputOptions) throws KillBillClientException {
        refreshPaymentMethods(accountId, null, pluginProperties, inputOptions);
    }

    // Overdue

    @Deprecated
    public void uploadXMLOverdueConfig(final String overdueConfigFilePath, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadXMLOverdueConfig(overdueConfigFilePath, RequestOptions.builder()
                                                                    .withCreatedBy(createdBy)
                                                                    .withReason(reason)
                                                                    .withComment(comment)
                                                                    .build());
    }

    public void uploadXMLOverdueConfig(final String overdueConfigFilePath, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.OVERDUE_PATH;
        uploadFile(overdueConfigFilePath, uri, "application/xml", inputOptions, null);
    }

    @Deprecated
    public void uploadXMLOverdueConfig(final InputStream overdueConfigInputStream, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadXMLOverdueConfig(overdueConfigInputStream, RequestOptions.builder()
                                                                       .withCreatedBy(createdBy)
                                                                       .withReason(reason)
                                                                       .withComment(comment)
                                                                       .build());
    }

    public void uploadXMLOverdueConfig(final InputStream overdueConfigInputStream, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.OVERDUE_PATH;
        uploadFile(overdueConfigInputStream, uri, "application/xml", inputOptions, null);
    }

    @Deprecated
    public String getXMLOverdueConfig() throws KillBillClientException {
        return getXMLOverdueConfig(RequestOptions.empty());
    }

    public String getXMLOverdueConfig(final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.OVERDUE_PATH;
        return getResourceFile(uri, "application/xml", inputOptions);
    }

    @Deprecated
    public OverdueState getOverdueStateForAccount(final UUID accountId) throws KillBillClientException {
        return getOverdueStateForAccount(accountId, RequestOptions.empty());
    }

    public OverdueState getOverdueStateForAccount(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.OVERDUE;

        return httpClient.doGet(uri, OverdueState.class, inputOptions);
    }

    // Tag definitions

    @Deprecated
    public TagDefinitions getTagDefinitions() throws KillBillClientException {
        return getTagDefinitions(RequestOptions.empty());
    }

    public TagDefinitions getTagDefinitions(final RequestOptions inputOptions) throws KillBillClientException {
        return httpClient.doGet(JaxrsResource.TAG_DEFINITIONS_PATH, TagDefinitions.class, inputOptions);
    }

    @Deprecated
    public TagDefinition getTagDefinition(final UUID tagDefinitionId) throws KillBillClientException {
        return getTagDefinition(tagDefinitionId, RequestOptions.empty());
    }

    public TagDefinition getTagDefinition(final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TAG_DEFINITIONS_PATH + "/" + tagDefinitionId;

        return httpClient.doGet(uri, TagDefinition.class, inputOptions);
    }

    @Deprecated
    public TagDefinition createTagDefinition(final TagDefinition tagDefinition, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createTagDefinition(tagDefinition, RequestOptions.builder()
                                                                .withCreatedBy(createdBy)
                                                                .withReason(reason)
                                                                .withComment(comment)
                                                                .build());
    }

    public TagDefinition createTagDefinition(final TagDefinition tagDefinition, final RequestOptions inputOptions) throws KillBillClientException {
        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(JaxrsResource.TAG_DEFINITIONS_PATH, tagDefinition, TagDefinition.class, requestOptions);
    }

    @Deprecated
    public void deleteTagDefinition(final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteTagDefinition(tagDefinitionId, RequestOptions.builder()
                                                           .withCreatedBy(createdBy)
                                                           .withReason(reason)
                                                           .withComment(comment)
                                                           .build());
    }

    public void deleteTagDefinition(final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TAG_DEFINITIONS_PATH + "/" + tagDefinitionId;

        httpClient.doDelete(uri, inputOptions);
    }

    // Tags

    @Deprecated
    public Tags getTags() throws KillBillClientException {
        return getTags(RequestOptions.empty());
    }

    public Tags getTags(final RequestOptions inputOptions) throws KillBillClientException {
        return getTags(0L, 100L, inputOptions);
    }

    @Deprecated
    public Tags getTags(final Long offset, final Long limit) throws KillBillClientException {
        return getTags(offset, limit, RequestOptions.empty());
    }

    public Tags getTags(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getTags(offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getTags(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getTags(offset, limit, auditLevel, RequestOptions.empty());
    }

    public Tags getTags(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TAGS_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

    @Deprecated
    public Tags searchTags(final String key) throws KillBillClientException {
        return searchTags(key, RequestOptions.empty());
    }

    public Tags searchTags(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchTags(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public Tags searchTags(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchTags(key, offset, limit, RequestOptions.empty());
    }

    public Tags searchTags(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchTags(key, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags searchTags(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchTags(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public Tags searchTags(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TAGS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

    @Deprecated
    public Tags getAllAccountTags(final UUID accountId, final String objectType) throws KillBillClientException {
        return getAllAccountTags(accountId, objectType, RequestOptions.empty());
    }

    public Tags getAllAccountTags(final UUID accountId, final String objectType, final RequestOptions inputOptions) throws KillBillClientException {
        return getAllAccountTags(accountId, objectType, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getAllAccountTags(final UUID accountId, @Nullable final String objectType, final AuditLevel auditLevel) throws KillBillClientException {
        return getAllAccountTags(accountId, objectType, auditLevel, RequestOptions.empty());
    }

    public Tags getAllAccountTags(final UUID accountId, @Nullable final String objectType, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.ALL_TAGS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());
        if (objectType != null) {
            queryParams.put(JaxrsResource.QUERY_OBJECT_TYPE, objectType);
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();
        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

    @Deprecated
    public Tags getAccountTags(final UUID accountId) throws KillBillClientException {
        return getAccountTags(accountId, RequestOptions.empty());
    }

    public Tags getAccountTags(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getAccountTags(accountId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getAccountTags(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        return getAccountTags(accountId, auditLevel, RequestOptions.empty());
    }

    public Tags getAccountTags(final UUID accountId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getObjectTags(accountId, JaxrsResource.ACCOUNTS_PATH, auditLevel, inputOptions);
    }

    @Deprecated
    public Tags createAccountTag(final UUID accountId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createAccountTag(accountId, tagDefinitionId, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment)
                                                                          .build());
    }

    public Tags createAccountTag(final UUID accountId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        return createObjectTag(accountId, JaxrsResource.ACCOUNTS_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public void deleteAccountTag(final UUID accountId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteAccountTag(accountId, tagDefinitionId, RequestOptions.builder()
                                                                   .withCreatedBy(createdBy)
                                                                   .withReason(reason)
                                                                   .withComment(comment)
                                                                   .build());
    }

    public void deleteAccountTag(final UUID accountId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteObjectTag(accountId, JaxrsResource.ACCOUNTS_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public Tags getBundleTags(final UUID bundleId) throws KillBillClientException {
        return getBundleTags(bundleId, RequestOptions.empty());
    }

    public Tags getBundleTags(final UUID bundleId, final RequestOptions inputOptions) throws KillBillClientException {
        return getBundleTags(bundleId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getBundleTags(final UUID bundleId, final AuditLevel auditLevel) throws KillBillClientException {
        return getBundleTags(bundleId, auditLevel, RequestOptions.empty());
    }

    public Tags getBundleTags(final UUID bundleId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getObjectTags(bundleId, JaxrsResource.BUNDLES_PATH, auditLevel, inputOptions);
    }

    @Deprecated
    public Tags createBundleTag(final UUID bundleId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createBundleTag(bundleId, tagDefinitionId, RequestOptions.builder()
                                                                        .withCreatedBy(createdBy)
                                                                        .withReason(reason)
                                                                        .withComment(comment)
                                                                        .build());
    }

    public Tags createBundleTag(final UUID bundleId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        return createObjectTag(bundleId, JaxrsResource.BUNDLES_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public void deleteBundleTag(final UUID bundleId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteBundleTag(bundleId, tagDefinitionId, RequestOptions.builder()
                                                                 .withCreatedBy(createdBy)
                                                                 .withReason(reason)
                                                                 .withComment(comment)
                                                                 .build());
    }

    public void deleteBundleTag(final UUID bundleId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteObjectTag(bundleId, JaxrsResource.BUNDLES_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public Tags getSubscriptionTags(final UUID subscriptionId) throws KillBillClientException {
        return getSubscriptionTags(subscriptionId, RequestOptions.empty());
    }

    public Tags getSubscriptionTags(final UUID subscriptionId, final RequestOptions inputOptions) throws KillBillClientException {
        return getSubscriptionTags(subscriptionId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getSubscriptionTags(final UUID subscriptionId, final AuditLevel auditLevel) throws KillBillClientException {
        return getSubscriptionTags(subscriptionId, auditLevel, RequestOptions.empty());
    }

    public Tags getSubscriptionTags(final UUID subscriptionId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getObjectTags(subscriptionId, JaxrsResource.SUBSCRIPTIONS_PATH, auditLevel, inputOptions);
    }

    @Deprecated
    public Tags createSubscriptionTag(final UUID subscriptionId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createSubscriptionTag(subscriptionId, tagDefinitionId, RequestOptions.builder()
                                                                                    .withCreatedBy(createdBy)
                                                                                    .withReason(reason)
                                                                                    .withComment(comment)
                                                                                    .build());
    }

    public Tags createSubscriptionTag(final UUID subscriptionId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        return createObjectTag(subscriptionId, JaxrsResource.SUBSCRIPTIONS_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public void deleteSubscriptionTag(final UUID subscriptionId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteSubscriptionTag(subscriptionId, tagDefinitionId, RequestOptions.builder()
                                                                             .withCreatedBy(createdBy)
                                                                             .withReason(reason)
                                                                             .withComment(comment)
                                                                             .build());
    }

    public void deleteSubscriptionTag(final UUID subscriptionId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteObjectTag(subscriptionId, JaxrsResource.SUBSCRIPTIONS_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public Tags getInvoiceTags(final UUID invoiceId) throws KillBillClientException {
        return getInvoiceTags(invoiceId, RequestOptions.empty());
    }

    public Tags getInvoiceTags(final UUID invoiceId, final RequestOptions inputOptions) throws KillBillClientException {
        return getInvoiceTags(invoiceId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getInvoiceTags(final UUID invoiceId, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoiceTags(invoiceId, auditLevel, RequestOptions.empty());
    }

    public Tags getInvoiceTags(final UUID invoiceId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getObjectTags(invoiceId, JaxrsResource.INVOICES_PATH, auditLevel, inputOptions);
    }

    @Deprecated
    public Tags createInvoiceTag(final UUID invoiceId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createInvoiceTag(invoiceId, tagDefinitionId, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment)
                                                                          .build());
    }

    public Tags createInvoiceTag(final UUID invoiceId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        return createObjectTag(invoiceId, JaxrsResource.INVOICES_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public void deleteInvoiceTag(final UUID invoiceId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteInvoiceTag(invoiceId, tagDefinitionId, RequestOptions.builder()
                                                                   .withCreatedBy(createdBy)
                                                                   .withReason(reason)
                                                                   .withComment(comment)
                                                                   .build());
    }

    public void deleteInvoiceTag(final UUID invoiceId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteObjectTag(invoiceId, JaxrsResource.INVOICES_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public Tags getPaymentTags(final UUID paymentId) throws KillBillClientException {
        return getPaymentTags(paymentId, RequestOptions.empty());
    }

    public Tags getPaymentTags(final UUID paymentId, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentTags(paymentId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public Tags getPaymentTags(final UUID paymentId, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentTags(paymentId, auditLevel, RequestOptions.empty());
    }

    public Tags getPaymentTags(final UUID paymentId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        return getObjectTags(paymentId, JaxrsResource.PAYMENTS_PATH, auditLevel, inputOptions);
    }

    @Deprecated
    public Tags createPaymentTag(final UUID paymentId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPaymentTag(paymentId, tagDefinitionId, RequestOptions.builder()
                                                                          .withCreatedBy(createdBy)
                                                                          .withReason(reason)
                                                                          .withComment(comment)
                                                                          .build());
    }

    public Tags createPaymentTag(final UUID paymentId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        return createObjectTag(paymentId, JaxrsResource.PAYMENTS_PATH, tagDefinitionId, inputOptions);
    }

    @Deprecated
    public void deletePaymentTag(final UUID paymentId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deletePaymentTag(paymentId, tagDefinitionId, RequestOptions.builder()
                                                                   .withCreatedBy(createdBy)
                                                                   .withReason(reason)
                                                                   .withComment(comment)
                                                                   .build());
    }

    public void deletePaymentTag(final UUID paymentId, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteObjectTag(paymentId, JaxrsResource.PAYMENTS_PATH, tagDefinitionId, inputOptions);
    }

    private Tags getObjectTags(final UUID objectId, final String resourcePathPrefix, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = resourcePathPrefix + "/" + objectId + "/" + JaxrsResource.TAGS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Tags.class, requestOptions);
    }

    private Tags createObjectTag(final UUID objectId, final String resourcePathPrefix, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = resourcePathPrefix + "/" + objectId + "/" + JaxrsResource.TAGS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_TAGS, tagDefinitionId.toString());

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, null, Tags.class, requestOptions);
    }

    private void deleteObjectTag(final UUID objectId, final String resourcePathPrefix, final UUID tagDefinitionId, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = resourcePathPrefix + "/" + objectId + "/" + JaxrsResource.TAGS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_TAGS, tagDefinitionId.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doDelete(uri, requestOptions);
    }

    // Custom fields

    @Deprecated
    public CustomFields getCustomFields() throws KillBillClientException {
        return getCustomFields(RequestOptions.empty());
    }

    public CustomFields getCustomFields(final RequestOptions inputOptions) throws KillBillClientException {
        return getCustomFields(0L, 100L, inputOptions);
    }

    @Deprecated
    public CustomFields getCustomFields(final Long offset, final Long limit) throws KillBillClientException {
        return getCustomFields(offset, limit, RequestOptions.empty());
    }

    public CustomFields getCustomFields(final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return getCustomFields(offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public CustomFields getCustomFields(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getCustomFields(offset, limit, auditLevel, RequestOptions.empty());
    }

    public CustomFields getCustomFields(final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CUSTOM_FIELDS_PATH + "/" + JaxrsResource.PAGINATION;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, CustomFields.class, requestOptions);
    }

    @Deprecated
    public CustomFields searchCustomFields(final String key) throws KillBillClientException {
        return searchCustomFields(key, RequestOptions.empty());
    }

    public CustomFields searchCustomFields(final String key, final RequestOptions inputOptions) throws KillBillClientException {
        return searchCustomFields(key, 0L, 100L, inputOptions);
    }

    @Deprecated
    public CustomFields searchCustomFields(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchCustomFields(key, offset, limit, RequestOptions.empty());
    }

    public CustomFields searchCustomFields(final String key, final Long offset, final Long limit, final RequestOptions inputOptions) throws KillBillClientException {
        return searchCustomFields(key, offset, limit, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public CustomFields searchCustomFields(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return searchCustomFields(key, offset, limit, auditLevel, RequestOptions.empty());
    }

    public CustomFields searchCustomFields(final String key, final Long offset, final Long limit, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CUSTOM_FIELDS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset));
        queryParams.put(JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit));
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, CustomFields.class, requestOptions);
    }

    @Deprecated
    public CustomFields getAccountCustomFields(final UUID accountId) throws KillBillClientException {
        return getAccountCustomFields(accountId, RequestOptions.empty());
    }

    public CustomFields getAccountCustomFields(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        return getAccountCustomFields(accountId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public CustomFields getAccountCustomFields(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        return getAccountCustomFields(accountId, auditLevel, RequestOptions.empty());
    }

    public CustomFields getAccountCustomFields(final UUID accountId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, CustomFields.class, requestOptions);
    }

    @Deprecated
    public CustomFields createAccountCustomField(final UUID accountId, final CustomField customField, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createAccountCustomField(accountId, customField, RequestOptions.builder()
                                                                              .withCreatedBy(createdBy)
                                                                              .withReason(reason)
                                                                              .withComment(comment)
                                                                              .build());
    }

    public CustomFields createAccountCustomField(final UUID accountId, final CustomField customField, final RequestOptions inputOptions) throws KillBillClientException {
        return createAccountCustomFields(accountId, ImmutableList.<CustomField>of(customField), inputOptions);
    }

    @Deprecated
    public CustomFields createAccountCustomFields(final UUID accountId, final Iterable<CustomField> customFields, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createAccountCustomFields(accountId, customFields, RequestOptions.builder()
                                                                                .withCreatedBy(createdBy)
                                                                                .withReason(reason)
                                                                                .withComment(comment)
                                                                                .build());
    }

    public CustomFields createAccountCustomFields(final UUID accountId, final Iterable<CustomField> customFields, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, customFields, CustomFields.class, requestOptions);
    }

    @Deprecated
    public void deleteAccountCustomField(final UUID accountId, final UUID customFieldId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteAccountCustomField(accountId, customFieldId, RequestOptions.builder()
                                                                         .withCreatedBy(createdBy)
                                                                         .withReason(reason)
                                                                         .withComment(comment)
                                                                         .build());
    }

    public void deleteAccountCustomField(final UUID accountId, final UUID customFieldId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteAccountCustomFields(accountId, ImmutableList.<UUID>of(customFieldId), inputOptions);
    }

    @Deprecated
    public void deleteAccountCustomFields(final UUID accountId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteAccountCustomFields(accountId, RequestOptions.builder()
                                                           .withCreatedBy(createdBy)
                                                           .withReason(reason)
                                                           .withComment(comment)
                                                           .build());
    }

    public void deleteAccountCustomFields(final UUID accountId, final RequestOptions inputOptions) throws KillBillClientException {
        deleteAccountCustomFields(accountId, null, inputOptions);
    }

    @Deprecated
    public void deleteAccountCustomFields(final UUID accountId, @Nullable final Iterable<UUID> customFieldIds, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteAccountCustomFields(accountId, customFieldIds, RequestOptions.builder()
                                                                           .withCreatedBy(createdBy)
                                                                           .withReason(reason)
                                                                           .withComment(comment)
                                                                           .build());
    }

    public void deleteAccountCustomFields(final UUID accountId, @Nullable final Iterable<UUID> customFieldIds, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (customFieldIds != null) {
            queryParams.put(JaxrsResource.QUERY_CUSTOM_FIELDS, Joiner.on(",").join(customFieldIds));
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doDelete(uri, requestOptions);
    }

    @Deprecated
    public CustomFields getPaymentMethodCustomFields(final UUID paymentMethodId) throws KillBillClientException {
        return getPaymentMethodCustomFields(paymentMethodId, RequestOptions.empty());
    }

    public CustomFields getPaymentMethodCustomFields(final UUID paymentMethodId, final RequestOptions inputOptions) throws KillBillClientException {
        return getPaymentMethodCustomFields(paymentMethodId, AuditLevel.NONE, inputOptions);
    }

    @Deprecated
    public CustomFields getPaymentMethodCustomFields(final UUID paymentMethodId, final AuditLevel auditLevel) throws KillBillClientException {
        return getPaymentMethodCustomFields(paymentMethodId, auditLevel, RequestOptions.empty());
    }

    public CustomFields getPaymentMethodCustomFields(final UUID paymentMethodId, final AuditLevel auditLevel, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, CustomFields.class, requestOptions);
    }

    @Deprecated
    public CustomFields createPaymentMethodCustomField(final UUID paymentMethodId, final CustomField customField, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPaymentMethodCustomField(paymentMethodId, customField, RequestOptions.builder()
                                                                                          .withCreatedBy(createdBy)
                                                                                          .withReason(reason)
                                                                                          .withComment(comment)
                                                                                          .build());
    }

    public CustomFields createPaymentMethodCustomField(final UUID paymentMethodId, final CustomField customField, final RequestOptions inputOptions) throws KillBillClientException {
        return createPaymentMethodCustomFields(paymentMethodId, ImmutableList.of(customField), inputOptions);
    }

    @Deprecated
    public CustomFields createPaymentMethodCustomFields(final UUID paymentMethodId, final Iterable<CustomField> customFields, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createPaymentMethodCustomFields(paymentMethodId, customFields, RequestOptions.builder()
                                                                                            .withCreatedBy(createdBy)
                                                                                            .withReason(reason)
                                                                                            .withComment(comment)
                                                                                            .build());
    }

    public CustomFields createPaymentMethodCustomFields(final UUID paymentMethodId, final Iterable<CustomField> customFields, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, customFields, CustomFields.class, requestOptions);
    }

    @Deprecated
    public void deletePaymentMethodCustomFields(final UUID paymentMethodId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deletePaymentMethodCustomFields(paymentMethodId, RequestOptions.builder()
                                                                       .withCreatedBy(createdBy)
                                                                       .withReason(reason)
                                                                       .withComment(comment)
                                                                       .build());
    }

    public void deletePaymentMethodCustomFields(final UUID paymentMethodId, final RequestOptions inputOptions) throws KillBillClientException {
        deletePaymentMethodCustomFields(paymentMethodId, null, inputOptions);
    }

    @Deprecated
    public void deletePaymentMethodCustomFields(final UUID paymentMethodId, @Nullable final Iterable<UUID> customFieldIds, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deletePaymentMethodCustomFields(paymentMethodId, customFieldIds, RequestOptions.builder()
                                                                                       .withCreatedBy(createdBy)
                                                                                       .withReason(reason)
                                                                                       .withComment(comment)
                                                                                       .build());
    }

    public void deletePaymentMethodCustomFields(final UUID paymentMethodId, @Nullable final Iterable<UUID> customFieldIds, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (customFieldIds != null) {
            queryParams.put(JaxrsResource.QUERY_CUSTOM_FIELDS, Joiner.on(",").join(customFieldIds));
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        httpClient.doDelete(uri, requestOptions);
    }

    // Catalog

    @Deprecated
    public List<PlanDetail> getAvailableAddons(final String baseProductName) throws KillBillClientException {
        return getAvailableAddons(baseProductName, RequestOptions.empty());
    }

    public List<PlanDetail> getAvailableAddons(final String baseProductName, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH + "/availableAddons";

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put("baseProductName", baseProductName);

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, PlanDetails.class, requestOptions);
    }

    @Deprecated
    public List<PlanDetail> getBasePlans() throws KillBillClientException {
        return getBasePlans(RequestOptions.empty());
    }

    public List<PlanDetail> getBasePlans(final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH + "/availableBasePlans";

        return httpClient.doGet(uri, PlanDetails.class, inputOptions);
    }

    @Deprecated
    public void uploadXMLCatalog(final String catalogFilePath, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadXMLCatalog(catalogFilePath, RequestOptions.builder()
                                                        .withCreatedBy(createdBy)
                                                        .withReason(reason)
                                                        .withComment(comment)
                                                        .build());
    }

    public void uploadXMLCatalog(final String catalogFilePath, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH;
        uploadFile(catalogFilePath, uri, CONTENT_TYPE_XML, inputOptions, null);
    }

    @Deprecated
    public void uploadXMLCatalog(final InputStream catalogInputStream, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        uploadXMLCatalog(catalogInputStream, RequestOptions.builder()
                                                           .withCreatedBy(createdBy)
                                                           .withReason(reason)
                                                           .withComment(comment)
                                                           .build());
    }

    public void uploadXMLCatalog(final InputStream catalogInputStream, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH;
        uploadFile(catalogInputStream, uri, CONTENT_TYPE_XML, inputOptions, null);
    }

    @Deprecated
    public Catalog getJSONCatalog() throws KillBillClientException {
        return getJSONCatalog(RequestOptions.empty());
    }

    public Catalog getJSONCatalog(final RequestOptions inputOptions) throws KillBillClientException {
        return this.getJSONCatalog(null, inputOptions);
    }

    @Deprecated
    public Catalog getJSONCatalog(final DateTime requestedDate) throws KillBillClientException {
        return getJSONCatalog(requestedDate, RequestOptions.empty());
    }

    public Catalog getJSONCatalog(final DateTime requestedDate, final RequestOptions inputOptions) throws KillBillClientException {
        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (requestedDate != null) {
            queryParams.put(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString());
        }

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        final String uri = JaxrsResource.CATALOG_PATH;
        return httpClient.doGet(uri, Catalog.class, requestOptions);
    }

    @Deprecated
    public String getXMLCatalog() throws KillBillClientException {
        return getXMLCatalog(RequestOptions.empty());
    }

    public String getXMLCatalog(final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH;
        return getResourceFile(uri, ACCEPT_XML, inputOptions);
    }

    // Tenants

    @Deprecated
    public Tenant createTenant(final Tenant tenant, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createTenant(tenant, RequestOptions.builder()
                                                  .withCreatedBy(createdBy)
                                                  .withReason(reason)
                                                  .withComment(comment)
                                                  .build());
    }

    public Tenant createTenant(final Tenant tenant, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(tenant.getApiKey(), "Tenant#apiKey cannot be null");
        Preconditions.checkNotNull(tenant.getApiSecret(), "Tenant#apiSecret cannot be null");

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withFollowLocation(followLocation).build();

        return httpClient.doPost(JaxrsResource.TENANTS_PATH, tenant, Tenant.class, requestOptions);
    }

    @Deprecated
    public TenantKey registerCallbackNotificationForTenant(final String callback, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return registerCallbackNotificationForTenant(callback, RequestOptions.builder()
                                                                             .withCreatedBy(createdBy)
                                                                             .withReason(reason)
                                                                             .withComment(comment)
                                                                             .build());
    }

    public TenantKey registerCallbackNotificationForTenant(final String callback, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.REGISTER_NOTIFICATION_CALLBACK;

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(JaxrsResource.QUERY_NOTIFICATION_CALLBACK, callback);

        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).withFollowLocation(followLocation).build();

        return httpClient.doPost(uri, null, TenantKey.class, requestOptions);
    }

    @Deprecated
    public TenantKey getCallbackNotificationForTenant() throws KillBillClientException {
        return getCallbackNotificationForTenant(RequestOptions.empty());
    }

    public TenantKey getCallbackNotificationForTenant(final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.REGISTER_NOTIFICATION_CALLBACK;
        return httpClient.doGet(uri, TenantKey.class, inputOptions);
    }

    @Deprecated
    public void unregisterCallbackNotificationForTenant(final String createdBy, final String reason, final String comment) throws KillBillClientException {
        unregisterCallbackNotificationForTenant(RequestOptions.builder()
                                                              .withCreatedBy(createdBy)
                                                              .withReason(reason)
                                                              .withComment(comment)
                                                              .build());
    }

    public void unregisterCallbackNotificationForTenant(final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.REGISTER_NOTIFICATION_CALLBACK;

        httpClient.doDelete(uri, inputOptions);
    }

    @Deprecated
    public TenantKey registerPluginConfigurationForTenant(final String pluginName, final String pluginConfigFilePath, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return registerPluginConfigurationForTenant(pluginName, pluginConfigFilePath, RequestOptions.builder()
                                                                                                    .withCreatedBy(createdBy)
                                                                                                    .withReason(reason)
                                                                                                    .withComment(comment)
                                                                                                    .build());
    }

    public TenantKey registerPluginConfigurationForTenant(final String pluginName, final String pluginConfigFilePath, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_CONFIG + "/" + pluginName;
        return uploadFile(pluginConfigFilePath, uri, "text/plain", inputOptions, TenantKey.class);
    }

    @Deprecated
    public TenantKey registerPluginConfigurationForTenant(final String pluginName, final InputStream pluginConfigInputStream, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return registerPluginConfigurationForTenant(pluginName, pluginConfigInputStream, RequestOptions.builder()
                                                                                                       .withCreatedBy(createdBy)
                                                                                                       .withReason(reason)
                                                                                                       .withComment(comment)
                                                                                                       .build());
    }

    public TenantKey registerPluginConfigurationForTenant(final String pluginName, final InputStream pluginConfigInputStream, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_CONFIG + "/" + pluginName;
        return uploadFile(pluginConfigInputStream, uri, "text/plain", inputOptions, TenantKey.class);
    }

    @Deprecated
    public TenantKey postPluginConfigurationPropertiesForTenant(final String pluginName, final String pluginConfigProperties, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final RequestOptions options = RequestOptions.builder()
                                                     .withCreatedBy(createdBy)
                                                     .withReason(reason)
                                                     .withComment(comment)
                                                     .build();
        return postPluginConfigurationPropertiesForTenant(pluginName, pluginConfigProperties, options);
    }

    public TenantKey postPluginConfigurationPropertiesForTenant(final String pluginName, final String pluginConfigProperties, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_CONFIG + "/" + pluginName;

        final RequestOptions options = inputOptions.extend()
                                                   .withFollowLocation(true)
                                                   .withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "text/plain")
                                                   .build();
        return httpClient.doPost(uri, pluginConfigProperties, TenantKey.class, options);
    }

    @Deprecated
    public TenantKey getPluginConfigurationForTenant(final String pluginName) throws KillBillClientException {
        return getPluginConfigurationForTenant(pluginName, RequestOptions.empty());
    }

    public TenantKey getPluginConfigurationForTenant(final String pluginName, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_CONFIG + "/" + pluginName;
        return httpClient.doGet(uri, TenantKey.class, inputOptions);
    }

    @Deprecated
    public void unregisterPluginConfigurationForTenant(final String pluginName, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        unregisterPluginConfigurationForTenant(pluginName, RequestOptions.builder()
                                                                         .withCreatedBy(createdBy)
                                                                         .withReason(reason)
                                                                         .withComment(comment)
                                                                         .build());
    }

    public void unregisterPluginConfigurationForTenant(final String pluginName, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_CONFIG + "/" + pluginName;
        httpClient.doDelete(uri, inputOptions);
    }

    public TenantKey registerPluginPaymentStateMachineConfigurationForTenant(final String pluginName, final String pluginPaymentStateMachineConfigFilePath, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_PAYMENT_STATE_MACHINE_CONFIG + "/" + pluginName;
        return uploadFile(pluginPaymentStateMachineConfigFilePath, uri, "text/plain", inputOptions, TenantKey.class);
    }

    public TenantKey registerPluginPaymentStateMachineConfigurationForTenant(final String pluginName, final InputStream pluginPaymentStateMachineConfigInputStream, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_PAYMENT_STATE_MACHINE_CONFIG + "/" + pluginName;
        return uploadFile(pluginPaymentStateMachineConfigInputStream, uri, "text/plain", inputOptions, TenantKey.class);
    }

    public TenantKey postPluginPaymentStateMachineConfigurationXMLForTenant(final String pluginName, final String pluginPaymentStateMachineConfigXML, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_PAYMENT_STATE_MACHINE_CONFIG + "/" + pluginName;

        final RequestOptions options = inputOptions.extend()
                                                   .withFollowLocation(true)
                                                   .withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "text/plain")
                                                   .build();
        return httpClient.doPost(uri, pluginPaymentStateMachineConfigXML, TenantKey.class, options);
    }

    public TenantKey getPluginPaymentStateMachineConfigurationForTenant(final String pluginName, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_PAYMENT_STATE_MACHINE_CONFIG + "/" + pluginName;
        return httpClient.doGet(uri, TenantKey.class, inputOptions);
    }

    public void unregisterPluginPaymentStateMachineConfigurationForTenant(final String pluginName, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.UPLOAD_PLUGIN_PAYMENT_STATE_MACHINE_CONFIG + "/" + pluginName;
        httpClient.doDelete(uri, inputOptions);
    }

    @Deprecated
    public Permissions getPermissions() throws KillBillClientException {
        return getPermissions(RequestOptions.empty());
    }

    public Permissions getPermissions(final RequestOptions inputOptions) throws KillBillClientException {
        return httpClient.doGet(JaxrsResource.SECURITY_PATH + "/permissions", Permissions.class, inputOptions);
    }

    @Deprecated
    public Response addUserRoles(final UserRoles userRoles, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return addUserRoles(userRoles, RequestOptions.builder()
                                                     .withCreatedBy(createdBy)
                                                     .withReason(reason)
                                                     .withComment(comment)
                                                     .build());
    }

    public Response addUserRoles(final UserRoles userRoles, final RequestOptions inputOptions) throws KillBillClientException {
        return httpClient.doPost(JaxrsResource.SECURITY_PATH + "/users", userRoles, inputOptions);
    }

    @Deprecated
    public Response updateUserPassword(final String username, final String newPassword, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateUserPassword(username, newPassword, RequestOptions.builder()
                                                                       .withCreatedBy(createdBy)
                                                                       .withReason(reason)
                                                                       .withComment(comment)
                                                                       .build());
    }

    public Response updateUserPassword(final String username, final String newPassword, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.SECURITY_PATH + "/users/" + username + "/password";
        final UserRoles userRoles = new UserRoles(username, newPassword, ImmutableList.<String>of());
        return httpClient.doPut(uri, userRoles, inputOptions);
    }

    @Deprecated
    public Response updateUserRoles(final String username, final List<String> newRoles, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateUserRoles(username, newRoles, RequestOptions.builder()
                                                                 .withCreatedBy(createdBy)
                                                                 .withReason(reason)
                                                                 .withComment(comment)
                                                                 .build());
    }

    public Response updateUserRoles(final String username, final List<String> newRoles, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.SECURITY_PATH + "/users/" + username + "/roles";
        final UserRoles userRoles = new UserRoles(username, null, newRoles);
        return httpClient.doPut(uri, userRoles, inputOptions);
    }

    @Deprecated
    public Response invalidateUser(final String username, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return invalidateUser(username, RequestOptions.builder()
                                                      .withCreatedBy(createdBy)
                                                      .withReason(reason)
                                                      .withComment(comment)
                                                      .build());
    }

    public Response invalidateUser(final String username, final RequestOptions inputOptions) throws KillBillClientException {
        final String uri = JaxrsResource.SECURITY_PATH + "/users/" + username;
        return httpClient.doDelete(uri, inputOptions);
    }

    @Deprecated
    public Response addRoleDefinition(final RoleDefinition roleDefinition, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return addRoleDefinition(roleDefinition, RequestOptions.builder()
                                                               .withCreatedBy(createdBy)
                                                               .withReason(reason)
                                                               .withComment(comment)
                                                               .build());
    }

    public Response addRoleDefinition(final RoleDefinition roleDefinition, final RequestOptions inputOptions) throws KillBillClientException {
        return httpClient.doPost(JaxrsResource.SECURITY_PATH + "/roles", roleDefinition, inputOptions);
    }

    // Plugin endpoints

    @Deprecated
    public Response pluginGET(final String uri) throws Exception {
        return pluginGET(uri, RequestOptions.empty());
    }

    @Deprecated
    public Response pluginGET(final String uri, final Multimap<String, String> queryParams) throws Exception {
        return pluginGET(uri, RequestOptions.builder().withQueryParams(queryParams).build());
    }

    public Response pluginGET(final String uri, final RequestOptions inputOptions) throws Exception {
        return httpClient.doGet(JaxrsResource.PLUGINS_PATH + "/" + uri, inputOptions);
    }

    @Deprecated
    public Response pluginHEAD(final String uri) throws Exception {
        return pluginHEAD(uri, RequestOptions.empty());
    }

    @Deprecated
    public Response pluginHEAD(final String uri, final Multimap<String, String> queryParams) throws Exception {
        return pluginHEAD(uri, RequestOptions.builder().withQueryParams(queryParams).build());
    }

    public Response pluginHEAD(final String uri, final RequestOptions inputOptions) throws Exception {
        return httpClient.doHead(JaxrsResource.PLUGINS_PATH + "/" + uri, inputOptions);
    }

    @Deprecated
    public Response pluginPOST(final String uri, @Nullable final String body) throws Exception {
        return pluginPOST(uri, body, RequestOptions.empty());
    }

    @Deprecated
    public Response pluginPOST(final String uri, @Nullable final String body, final Multimap<String, String> queryParams) throws Exception {
        return pluginPOST(uri, body, RequestOptions.builder().withQueryParams(queryParams).build());
    }

    public Response pluginPOST(final String uri, @Nullable final String body, final RequestOptions inputOptions) throws Exception {
        return httpClient.doPost(JaxrsResource.PLUGINS_PATH + "/" + uri, body, inputOptions);
    }

    @Deprecated
    public Response pluginPUT(final String uri, @Nullable final String body) throws Exception {
        return pluginPUT(uri, body, RequestOptions.empty());
    }

    @Deprecated
    public Response pluginPUT(final String uri, @Nullable final String body, final Multimap<String, String> queryParams) throws Exception {
        return pluginPUT(uri, body, RequestOptions.builder().withQueryParams(queryParams).build());
    }

    public Response pluginPUT(final String uri, @Nullable final String body, final RequestOptions inputOptions) throws Exception {
        return httpClient.doPut(JaxrsResource.PLUGINS_PATH + "/" + uri, body, inputOptions);
    }

    @Deprecated
    public Response pluginDELETE(final String uri) throws Exception {
        return pluginDELETE(uri, RequestOptions.empty());
    }

    @Deprecated
    public Response pluginDELETE(final String uri, final Multimap<String, String> queryParams) throws Exception {
        return pluginDELETE(uri, RequestOptions.builder().withQueryParams(queryParams).build());
    }

    public Response pluginDELETE(final String uri, final RequestOptions inputOptions) throws Exception {
        return httpClient.doDelete(JaxrsResource.PLUGINS_PATH + "/" + uri, inputOptions);
    }

    @Deprecated
    public Response pluginOPTIONS(final String uri) throws Exception {
        return pluginOPTIONS(uri, RequestOptions.empty());
    }

    @Deprecated
    public Response pluginOPTIONS(final String uri, final Multimap<String, String> queryParams) throws Exception {
        return pluginOPTIONS(uri, RequestOptions.builder().withQueryParams(queryParams).build());
    }

    public Response pluginOPTIONS(final String uri, final RequestOptions inputOptions) throws Exception {
        return httpClient.doOptions(JaxrsResource.PLUGINS_PATH + "/" + uri, inputOptions);
    }

    // Utilities

    private String getResourceFile(final String uri, final String contentType, final RequestOptions inputOptions) throws KillBillClientException {
        final RequestOptions requestOptions = inputOptions.extend().withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, contentType).build();
        final Response response = httpClient.doGet(uri, requestOptions);
        try {
            return response.getResponseBody("UTF-8");
        } catch (final IOException e) {
            throw new KillBillClientException(e);
        }
    }

    private <ReturnType> ReturnType uploadFile(final String fileToUpload, final String uri, final String contentType, final RequestOptions inputOptions, final Class<ReturnType> followUpClass) throws KillBillClientException {
        Preconditions.checkNotNull(fileToUpload, "fileToUpload cannot be null");
        final File catalogFile = new File(fileToUpload);
        Preconditions.checkArgument(catalogFile.exists() && catalogFile.isFile() && catalogFile.canRead(), "file to upload needs to be a valid file");
        try {
            final String body = Files.toString(catalogFile, Charset.forName("UTF-8"));
            return doUploadFile(body, uri, contentType, inputOptions, followUpClass);
        } catch (final IOException e) {
            throw new KillBillClientException(e);
        }
    }

    private <ReturnType> ReturnType uploadFile(final InputStream fileToUpload, final String uri, final String contentType, final RequestOptions inputOptions, final Class<ReturnType> followUpClass) throws KillBillClientException {
        Preconditions.checkNotNull(fileToUpload, "fileToUpload cannot be null");
        try {
            final Readable reader = new InputStreamReader(fileToUpload, Charset.forName("UTF-8"));
            final String body = CharStreams.toString(reader);
            return doUploadFile(body, uri, contentType, inputOptions, followUpClass);
        } catch (final IOException e) {
            throw new KillBillClientException(e);
        }
    }

    private <ReturnType> ReturnType doUploadFile(final String body, final String uri, final String contentType, final RequestOptions inputOptions, final Class<ReturnType> followUpClass) throws KillBillClientException {
        final RequestOptionsBuilder requestOptionsBuilder = inputOptions.extend().withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, contentType);

        if (followUpClass != null) {
            final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
            final RequestOptions requestOptions = requestOptionsBuilder.withFollowLocation(followLocation).build();
            return httpClient.doPost(uri, body, followUpClass, requestOptions);
        } else {
            final RequestOptions requestOptions = requestOptionsBuilder.build();
            httpClient.doPost(uri, body, requestOptions);
            return null;
        }
    }

    private void storePluginPropertiesAsParams(final Map<String, String> pluginProperties, final Multimap<String, String> params) {
        for (final String key : pluginProperties.keySet()) {
            params.put(JaxrsResource.QUERY_PLUGIN_PROPERTY, String.format("%s=%s", UTF8UrlEncoder.encode(key), UTF8UrlEncoder.encode(pluginProperties.get(key))));
        }
    }
}
