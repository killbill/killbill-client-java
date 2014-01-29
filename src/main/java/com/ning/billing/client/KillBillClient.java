/*
 * Copyright 2010-2014 Ning, Inc.
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.ning.billing.catalog.api.BillingActionPolicy;
import com.ning.billing.client.model.Account;
import com.ning.billing.client.model.AccountEmail;
import com.ning.billing.client.model.AccountEmails;
import com.ning.billing.client.model.AccountTimeline;
import com.ning.billing.client.model.Accounts;
import com.ning.billing.client.model.Bundle;
import com.ning.billing.client.model.Bundles;
import com.ning.billing.client.model.Catalog;
import com.ning.billing.client.model.Chargeback;
import com.ning.billing.client.model.Chargebacks;
import com.ning.billing.client.model.Credit;
import com.ning.billing.client.model.CustomField;
import com.ning.billing.client.model.CustomFields;
import com.ning.billing.client.model.Invoice;
import com.ning.billing.client.model.InvoiceEmail;
import com.ning.billing.client.model.InvoiceItem;
import com.ning.billing.client.model.Invoices;
import com.ning.billing.client.model.OverdueState;
import com.ning.billing.client.model.Payment;
import com.ning.billing.client.model.PaymentMethod;
import com.ning.billing.client.model.PaymentMethods;
import com.ning.billing.client.model.Payments;
import com.ning.billing.client.model.Permissions;
import com.ning.billing.client.model.PlanDetail;
import com.ning.billing.client.model.PlanDetails;
import com.ning.billing.client.model.Refund;
import com.ning.billing.client.model.Refunds;
import com.ning.billing.client.model.Subscription;
import com.ning.billing.client.model.TagDefinition;
import com.ning.billing.client.model.TagDefinitions;
import com.ning.billing.client.model.Tags;
import com.ning.billing.client.model.Tenant;
import com.ning.billing.client.model.TenantKey;
import com.ning.billing.entitlement.api.Entitlement.EntitlementActionPolicy;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.billing.util.api.AuditLevel;
import com.ning.http.client.Response;
import com.ning.http.util.UTF8UrlEncoder;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import static com.ning.billing.client.KillBillHttpClient.DEFAULT_EMPTY_QUERY;
import static com.ning.billing.client.KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC;
import static com.ning.billing.jaxrs.resources.JaxrsResource.OVERDUE;
import static com.ning.billing.jaxrs.resources.JaxrsResource.QUERY_DELETE_DEFAULT_PM_WITH_AUTO_PAY_OFF;

public class KillBillClient {

    private final KillBillHttpClient httpClient;

    public KillBillClient() {
        this(new KillBillHttpClient());
    }

    public KillBillClient(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void close() {
        httpClient.close();
    }

    // Accounts

    public Accounts getAccounts() throws KillBillClientException {
        return getAccounts(0L, 100L);
    }

    public Accounts getAccounts(final Long offset, final Long limit) throws KillBillClientException {
        return getAccounts(offset, limit, AuditLevel.NONE);
    }

    public Accounts getAccounts(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Accounts.class);
    }

    public Account getAccount(final UUID accountId) throws KillBillClientException {
        return getAccount(accountId, false, false);
    }

    public Account getAccount(final UUID accountId, final boolean withBalance, final boolean withCBA) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE, withBalance ? "true" : "false",
                                                                                JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE_AND_CBA, withCBA ? "true" : "false");

        return httpClient.doGet(uri, queryParams, Account.class);
    }

    public Account getAccount(final String externalKey) throws KillBillClientException {
        return getAccount(externalKey, false, false);
    }

    public Account getAccount(final String externalKey, final boolean withBalance, final boolean withCBA) throws KillBillClientException {
        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey,
                                                                                JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE, withBalance ? "true" : "false",
                                                                                JaxrsResource.QUERY_ACCOUNT_WITH_BALANCE_AND_CBA, withCBA ? "true" : "false");

        return httpClient.doGet(JaxrsResource.ACCOUNTS_PATH, queryParams, Account.class);
    }

    public Accounts searchAccounts(final String key) throws KillBillClientException {
        return searchAccounts(key, 0L, 100L);
    }

    public Accounts searchAccounts(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchAccounts(key, offset, limit, AuditLevel.NONE);
    }

    public Accounts searchAccounts(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Accounts.class);
    }

    public AccountTimeline getAccountTimeline(final UUID accountId) throws KillBillClientException {
        return getAccountTimeline(accountId, AuditLevel.NONE);
    }

    public AccountTimeline getAccountTimeline(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.TIMELINE;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, AccountTimeline.class);
    }

    public Account createAccount(final Account account, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(JaxrsResource.ACCOUNTS_PATH, account, queryParams, Account.class);
    }

    public Account updateAccount(final Account account, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(account.getAccountId(), "Account#accountId cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + account.getAccountId();

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPut(uri, account, queryParams, Account.class);
    }

    public AccountEmails getEmailsForAccount(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAILS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, AccountEmails.class);
    }

    public void addEmailToAccount(final AccountEmail email, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(email.getAccountId(), "AccountEmail#accountId cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + email.getAccountId() + "/" + JaxrsResource.EMAILS;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doPost(uri, email, queryParams);
    }

    public void removeEmailFromAccount(final AccountEmail email, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(email.getAccountId(), "AccountEmail#accountId cannot be null");
        Preconditions.checkNotNull(email.getEmail(), "AccountEmail#email cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + email.getAccountId() + "/" + JaxrsResource.EMAILS + "/" + UTF8UrlEncoder.encode(email.getEmail());

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doDelete(uri, queryParams);
    }

    public InvoiceEmail getEmailNotificationsForAccount(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAIL_NOTIFICATIONS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, InvoiceEmail.class);
    }

    public void updateEmailNotificationsForAccount(final InvoiceEmail invoiceEmail, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceEmail.getAccountId(), "InvoiceEmail#accountId cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + invoiceEmail.getAccountId() + "/" + JaxrsResource.EMAIL_NOTIFICATIONS;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doPut(uri, invoiceEmail, queryParams);
    }

    // Bundles

    public Bundle getBundle(final UUID bundleId) throws KillBillClientException {
        final String uri = JaxrsResource.BUNDLES_PATH + "/" + bundleId;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Bundle.class);
    }

    public Bundle getBundle(final String externalKey) throws KillBillClientException {
        final String uri = JaxrsResource.BUNDLES_PATH;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);

        return httpClient.doGet(uri, queryParams, Bundle.class);
    }

    public Bundles getAccountBundles(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.BUNDLES;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Bundles.class);
    }

    public Bundles getAccountBundles(final UUID accountId, final String externalKey) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.BUNDLES;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);

        return httpClient.doGet(uri, queryParams, Bundles.class);
    }

    public Bundle transferBundle(final Bundle bundle, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(bundle.getBundleId(), "Bundle#bundleId cannot be null");
        Preconditions.checkNotNull(bundle.getAccountId(), "Bundle#accountId cannot be null");

        final String uri = JaxrsResource.BUNDLES_PATH + "/" + bundle.getBundleId();

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPutAndFollowLocation(uri, bundle, queryParams, Bundle.class);

    }

    // Subscriptions and entitlements

    public Subscription getSubscription(final UUID subscriptionId) throws KillBillClientException {
        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscriptionId;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Subscription.class);
    }

    public Subscription createSubscription(final Subscription subscription, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createSubscription(subscription, -1, createdBy, reason, comment);
    }

    public Subscription createSubscription(final Subscription subscription, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(subscription.getAccountId(), "Subscription#accountId cannot be null");
        Preconditions.checkNotNull(subscription.getExternalKey(), "Subscription#externalKey cannot be null");
        Preconditions.checkNotNull(subscription.getProductName(), "Subscription#productName cannot be null");
        Preconditions.checkNotNull(subscription.getProductCategory(), "Subscription#productCategory cannot be null");
        Preconditions.checkNotNull(subscription.getBillingPeriod(), "Subscription#billingPeriod cannot be null");
        Preconditions.checkNotNull(subscription.getPriceList(), "Subscription#priceList cannot be null");

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false",
                                                                                                JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec)),
                                                                createdBy,
                                                                reason,
                                                                comment);

        final int httpTimeout = Math.max(DEFAULT_HTTP_TIMEOUT_SEC, timeoutSec);

        return httpClient.doPostAndFollowLocation(JaxrsResource.SUBSCRIPTIONS_PATH, subscription, queryParams, httpTimeout, Subscription.class);
    }

    public Subscription updateSubscription(final Subscription subscription, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateSubscription(subscription, -1, createdBy, reason, comment);
    }

    public Subscription updateSubscription(final Subscription subscription, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return updateSubscription(subscription, null, timeoutSec, createdBy, reason, comment);
    }

    public Subscription updateSubscription(final Subscription subscription, @Nullable final BillingActionPolicy billingPolicy, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(subscription.getSubscriptionId(), "Subscription#subscriptionId cannot be null");
        Preconditions.checkNotNull(subscription.getProductName(), "Subscription#productName cannot be null");
        Preconditions.checkNotNull(subscription.getBillingPeriod(), "Subscription#billingPeriod cannot be null");
        Preconditions.checkNotNull(subscription.getPriceList(), "Subscription#priceList cannot be null");

        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscription.getSubscriptionId();

        final Map<String, String> params = new HashMap<String, String>();
        params.put(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false");
        params.put(JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec));
        if (billingPolicy != null) {
            params.put(JaxrsResource.QUERY_BILLING_POLICY, billingPolicy.toString());
        }
        final Map<String, String> queryParams = paramsWithAudit(params, createdBy, reason, comment);

        return httpClient.doPut(uri, subscription, queryParams, Subscription.class);
    }

    public void cancelSubscription(final UUID subscriptionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, -1, createdBy, reason, comment);
    }

    public void cancelSubscription(final UUID subscriptionId, final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        cancelSubscription(subscriptionId, null, null, timeoutSec, createdBy, reason, comment);
    }

    public void cancelSubscription(final UUID subscriptionId, @Nullable final EntitlementActionPolicy entitlementPolicy, @Nullable final BillingActionPolicy billingPolicy,
                                   final int timeoutSec, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscriptionId;

        final Map<String, String> params = new HashMap<String, String>();
        params.put(JaxrsResource.QUERY_CALL_COMPLETION, timeoutSec > 0 ? "true" : "false");
        params.put(JaxrsResource.QUERY_CALL_TIMEOUT, String.valueOf(timeoutSec));
        if (entitlementPolicy != null) {
            params.put(JaxrsResource.QUERY_ENTITLEMENT_POLICY, entitlementPolicy.toString());
        }
        if (billingPolicy != null) {
            params.put(JaxrsResource.QUERY_BILLING_POLICY, billingPolicy.toString());
        }
        final Map<String, String> queryParams = paramsWithAudit(params, createdBy, reason, comment);

        httpClient.doDelete(uri, queryParams);
    }

    public void uncancelSubscription(final UUID subscriptionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.SUBSCRIPTIONS_PATH + "/" + subscriptionId + "/uncancel";

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doPut(uri, null, queryParams);
    }

    // Invoices

    public Invoices getInvoices() throws KillBillClientException {
        return getInvoices(0L, 100L);
    }

    public Invoices getInvoices(final Long offset, final Long limit) throws KillBillClientException {
        return getInvoices(true, offset, limit, AuditLevel.NONE);
    }

    public Invoices getInvoices(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoices(true, offset, limit, auditLevel);
    }

    public Invoices getInvoices(boolean withItems, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_INVOICE_WITH_ITEMS, String.valueOf(withItems),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Invoices.class);
    }

    public Invoice getInvoice(final UUID invoiceId) throws KillBillClientException {
        return getInvoice(invoiceId, true);
    }

    public Invoice getInvoice(final UUID invoiceId, boolean withItems) throws KillBillClientException {
        return getInvoice(invoiceId, withItems, AuditLevel.NONE);
    }

    public Invoice getInvoice(final UUID invoiceId, boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoiceByIdOrNumber(invoiceId.toString(), withItems, auditLevel);
    }

    public Invoice getInvoice(final Integer invoiceNumber) throws KillBillClientException {
        return getInvoice(invoiceNumber, true);
    }

    public Invoice getInvoice(final Integer invoiceNumber, boolean withItems) throws KillBillClientException {
        return getInvoice(invoiceNumber, withItems, AuditLevel.NONE);
    }

    public Invoice getInvoice(final Integer invoiceNumber, boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        return getInvoiceByIdOrNumber(invoiceNumber.toString(), withItems, auditLevel);
    }

    public Invoice getInvoiceByIdOrNumber(final String invoiceIdOrNumber, boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceIdOrNumber;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, String.valueOf(withItems),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Invoice.class);
    }

    public Invoices getInvoicesForAccount(final UUID accountId) throws KillBillClientException {
        return getInvoicesForAccount(accountId, true);
    }

    public Invoices getInvoicesForAccount(final UUID accountId, boolean withItems) throws KillBillClientException {
        return getInvoicesForAccount(accountId, withItems, AuditLevel.NONE);
    }

    public Invoices getInvoicesForAccount(final UUID accountId, boolean withItems, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.INVOICES;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, String.valueOf(withItems),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Invoices.class);
    }

    public Invoice createDryRunInvoice(final UUID accountId, final DateTime futureDate, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_ACCOUNT_ID, accountId.toString(),
                                                                                                JaxrsResource.QUERY_TARGET_DATE, futureDate.toString(),
                                                                                                JaxrsResource.QUERY_DRY_RUN, "true"),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPost(uri, null, queryParams, Invoice.class);
    }

    public Invoice createInvoice(final UUID accountId, final DateTime futureDate, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_ACCOUNT_ID, accountId.toString(),
                                                                                                JaxrsResource.QUERY_TARGET_DATE, futureDate.toString()),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPostAndFollowLocation(uri, null, queryParams, Invoice.class);
    }

    public Invoice adjustInvoiceItem(final InvoiceItem invoiceItem, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return adjustInvoiceItem(invoiceItem, new DateTime(DateTimeZone.UTC), createdBy, reason, comment);
    }

    public Invoice adjustInvoiceItem(final InvoiceItem invoiceItem, final DateTime requestedDate, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(invoiceItem.getAccountId(), "InvoiceItem#accountId cannot be null");
        Preconditions.checkNotNull(invoiceItem.getInvoiceId(), "InvoiceItem#invoiceId cannot be null");
        Preconditions.checkNotNull(invoiceItem.getInvoiceItemId(), "InvoiceItem#invoiceItemId cannot be null");

        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceItem.getInvoiceId();

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString()),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPostAndFollowLocation(uri, invoiceItem, queryParams, Invoice.class);
    }

    public Invoice createExternalCharge(final InvoiceItem externalCharge, final DateTime requestedDate, final Boolean autoPay, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(externalCharge.getAccountId(), "InvoiceItem#accountId cannot be null");
        Preconditions.checkNotNull(externalCharge.getAmount(), "InvoiceItem#amount cannot be null");

        final String uri;
        if (externalCharge.getInvoiceId() != null) {
            uri = JaxrsResource.INVOICES_PATH + "/" + externalCharge.getInvoiceId() + "/" + JaxrsResource.CHARGES;
        } else {
            uri = JaxrsResource.CHARGES_PATH;
        }

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_REQUESTED_DT, requestedDate.toDateTimeISO().toString(),
                                                                                                JaxrsResource.QUERY_PAY_INVOICE, autoPay.toString()),
                                                                createdBy,
                                                                reason,
                                                                comment);

        final Map<String, String> queryParamsForFollow = ImmutableMap.<String, String>of(JaxrsResource.QUERY_INVOICE_WITH_ITEMS, "true");

        return httpClient.doPostAndFollowLocation(uri, externalCharge, queryParams, queryParamsForFollow, Invoice.class);
    }

    public void triggerInvoiceNotification(final UUID invoiceId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId.toString() + "/" + JaxrsResource.EMAIL_NOTIFICATIONS;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doPost(uri, null, queryParams);
    }

    // Credits

    public Credit getCredit(final UUID creditId) throws KillBillClientException {
        return getCredit(creditId, AuditLevel.NONE);
    }

    public Credit getCredit(final UUID creditId, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.CREDITS_PATH + "/" + creditId;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Credit.class);
    }

    public Credit createCredit(final Credit credit, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(credit.getAccountId(), "Credt#accountId cannot be null");
        Preconditions.checkNotNull(credit.getCreditAmount(), "Credt#creditAmount cannot be null");

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(JaxrsResource.CREDITS_PATH, credit, queryParams, Credit.class);
    }

    // Payments

    public Payments getPayments() throws KillBillClientException {
        return getPayments(0L, 100L);
    }

    public Payments getPayments(final Long offset, final Long limit) throws KillBillClientException {
        return getPayments(offset, limit, AuditLevel.NONE);
    }

    public Payments getPayments(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Payments.class);
    }

    public Payments searchPayments(final String key) throws KillBillClientException {
        return searchPayments(key, 0L, 100L);
    }

    public Payments searchPayments(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchPayments(key, offset, limit, AuditLevel.NONE);
    }

    public Payments searchPayments(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Payments.class);
    }

    public Payment getPayment(final UUID paymentId) throws KillBillClientException {
        return getPayment(paymentId, true);
    }

    public Payment getPayment(final UUID paymentId, final boolean withRefundsAndChargebacks) throws KillBillClientException {
        return getPayment(paymentId, withRefundsAndChargebacks, AuditLevel.NONE);
    }

    public Payment getPayment(final UUID paymentId, final boolean withRefundsAndChargebacks, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + paymentId;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_PAYMENT_WITH_REFUNDS_AND_CHARGEBACKS, String.valueOf(withRefundsAndChargebacks),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Payment.class);
    }

    public Payments getPaymentsForAccount(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENTS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Payments.class);
    }

    public Payments getPaymentsForInvoice(final UUID invoiceId) throws KillBillClientException {
        final String uri = JaxrsResource.INVOICES_PATH + "/" + invoiceId + "/" + JaxrsResource.PAYMENTS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Payments.class);
    }

    public void payAllInvoices(final UUID accountId, final boolean externalPayment, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENTS;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of("externalPayment", String.valueOf(externalPayment)),
                                                                createdBy,
                                                                reason,
                                                                comment);

        httpClient.doPost(uri, null, queryParams);
    }

    public Payments createPayment(final Payment payment, final boolean isExternal, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(payment.getAccountId(), "InvoiceItem#accountId cannot be null");
        Preconditions.checkNotNull(payment.getInvoiceId(), "InvoiceItem#invoiceId cannot be null");
        Preconditions.checkNotNull(payment.getAmount(), "InvoiceItem#amount cannot be null");

        final String uri = JaxrsResource.INVOICES_PATH + "/" + payment.getInvoiceId() + "/" + JaxrsResource.PAYMENTS;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of("externalPayment", String.valueOf(isExternal)),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPostAndFollowLocation(uri, payment, queryParams, Payments.class);
    }

    // Refunds

    public Refunds getRefunds() throws KillBillClientException {
        return getRefunds(0L, 100L);
    }

    public Refunds getRefunds(final Long offset, final Long limit) throws KillBillClientException {
        return getRefunds(offset, limit, AuditLevel.NONE);
    }

    public Refunds getRefunds(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.REFUNDS_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Refunds.class);
    }

    public Refunds searchRefunds(final String key) throws KillBillClientException {
        return searchRefunds(key, 0L, 100L);
    }

    public Refunds searchRefunds(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchRefunds(key, offset, limit, AuditLevel.NONE);
    }

    public Refunds searchRefunds(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.REFUNDS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Refunds.class);
    }

    public Refunds getRefundsForAccount(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.REFUNDS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Refunds.class);
    }

    public Refunds getRefundsForPayment(final UUID paymentId) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + paymentId + "/" + JaxrsResource.REFUNDS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Refunds.class);
    }

    public Refund createRefund(final Refund refund, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(refund.getPaymentId(), "Refund#paymentId cannot be null");

        // Specify isAdjusted for invoice adjustment and invoice item adjustment
        // Specify adjustments for invoice item adjustments only
        if (refund.getAdjustments() != null) {
            for (final InvoiceItem invoiceItem : refund.getAdjustments()) {
                Preconditions.checkNotNull(invoiceItem.getInvoiceItemId(), "InvoiceItem#invoiceItemId cannot be null");
            }
        }

        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + refund.getPaymentId() + "/" + JaxrsResource.REFUNDS;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(uri, refund, queryParams, Refund.class);
    }

    // Chargebacks

    public Chargebacks getChargebacksForAccount(final UUID accountId) throws KillBillClientException {
        return getChargebacksForAccount(accountId, AuditLevel.NONE);
    }

    public Chargebacks getChargebacksForAccount(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CHARGEBACKS;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Chargebacks.class);
    }

    public Chargebacks getChargebacksForPayment(final UUID paymentId) throws KillBillClientException {
        return getChargebacksForPayment(paymentId, AuditLevel.NONE);
    }

    public Chargebacks getChargebacksForPayment(final UUID paymentId, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENTS_PATH + "/" + paymentId + "/" + JaxrsResource.CHARGEBACKS;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Chargebacks.class);
    }

    public Chargeback createChargeBack(final Chargeback chargeback, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(chargeback.getAmount(), "Chargeback#amount cannot be null");
        Preconditions.checkNotNull(chargeback.getPaymentId(), "Chargeback#paymentId cannot be null");

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(JaxrsResource.CHARGEBACKS_PATH, chargeback, queryParams, Chargeback.class);
    }

    // Payment methods

    public PaymentMethods getPaymentMethods() throws KillBillClientException {
        return getPaymentMethods(0L, 100L);
    }

    public PaymentMethods getPaymentMethods(final Long offset, final Long limit) throws KillBillClientException {
        return getPaymentMethods(offset, limit, AuditLevel.NONE);
    }

    public PaymentMethods getPaymentMethods(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, PaymentMethods.class);
    }


    public PaymentMethods searchPaymentMethods(final String key) throws KillBillClientException {
        return searchPaymentMethods(key, 0L, 100L);
    }

    public PaymentMethods searchPaymentMethods(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchPaymentMethods(key, offset, limit, AuditLevel.NONE);
    }

    public PaymentMethods searchPaymentMethods(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, PaymentMethods.class);
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, false);
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final boolean withPluginInfo) throws KillBillClientException {
        return getPaymentMethod(paymentMethodId, withPluginInfo, AuditLevel.NONE);
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final boolean withPluginInfo, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_PAYMENT_METHOD_PLUGIN_INFO, String.valueOf(withPluginInfo),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, PaymentMethod.class);
    }

    public PaymentMethods getPaymentMethodsForAccount(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENT_METHODS;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, PaymentMethods.class);
    }

    public PaymentMethods searchPaymentMethodsByKey(final String key) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, null);
    }

    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, @Nullable final String pluginName) throws KillBillClientException {
        return searchPaymentMethodsByKeyAndPlugin(key, pluginName, AuditLevel.NONE);
    }

    public PaymentMethods searchPaymentMethodsByKeyAndPlugin(final String key, @Nullable final String pluginName, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_PAYMENT_METHOD_PLUGIN_INFO, Strings.nullToEmpty(pluginName),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, PaymentMethods.class);
    }

    public PaymentMethod createPaymentMethod(final PaymentMethod paymentMethod, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(paymentMethod.getAccountId(), "PaymentMethod#accountId cannot be null");
        Preconditions.checkNotNull(paymentMethod.getPluginName(), "PaymentMethod#pluginName cannot be null");

        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + paymentMethod.getAccountId() + "/" + JaxrsResource.PAYMENT_METHODS;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_PAYMENT_METHOD_IS_DEFAULT, paymentMethod.getIsDefault() ? "true" : "false"),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPostAndFollowLocation(uri, paymentMethod, queryParams, PaymentMethod.class);
    }

    public void updateDefaultPaymentMethod(final UUID accountId, final UUID paymentMethodId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.PAYMENT_METHODS + "/" + paymentMethodId + "/" + JaxrsResource.PAYMENT_METHODS_DEFAULT_PATH_POSTFIX;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doPut(uri, null, queryParams);
    }

    public void deletePaymentMethod(final UUID paymentMethodId, final Boolean deleteDefault, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.PAYMENT_METHODS_PATH + "/" + paymentMethodId;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(QUERY_DELETE_DEFAULT_PM_WITH_AUTO_PAY_OFF, deleteDefault.toString()),
                                                                createdBy,
                                                                reason,
                                                                comment);

        httpClient.doDelete(uri, queryParams);
    }

    // Overdue

    public OverdueState getOverdueStateForAccount(final UUID accountId) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + OVERDUE;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, OverdueState.class);
    }

    // Tag definitions

    public TagDefinitions getTagDefinitions() throws KillBillClientException {
        return httpClient.doGet(JaxrsResource.TAG_DEFINITIONS_PATH, DEFAULT_EMPTY_QUERY, TagDefinitions.class);
    }

    public TagDefinition getTagDefinition(final UUID tagDefinitionId) throws KillBillClientException {
        final String uri = JaxrsResource.TAG_DEFINITIONS_PATH + "/" + tagDefinitionId;

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, TagDefinition.class);
    }

    public TagDefinition createTagDefinition(final TagDefinition tagDefinition, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(JaxrsResource.TAG_DEFINITIONS_PATH, tagDefinition, queryParams, TagDefinition.class);
    }

    public void deleteTagDefinition(final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.TAG_DEFINITIONS_PATH + "/" + tagDefinitionId;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        httpClient.doDelete(uri, queryParams);
    }

    // Tags

    public Tags getTags() throws KillBillClientException {
        return getTags(0L, 100L);
    }

    public Tags getTags(final Long offset, final Long limit) throws KillBillClientException {
        return getTags(offset, limit, AuditLevel.NONE);
    }

    public Tags getTags(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.TAGS_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Tags.class);
    }

    public Tags searchTags(final String key) throws KillBillClientException {
        return searchTags(key, 0L, 100L);
    }

    public Tags searchTags(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchTags(key, offset, limit, AuditLevel.NONE);
    }

    public Tags searchTags(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.TAGS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Tags.class);
    }

    public Tags getAccountTags(final UUID accountId) throws KillBillClientException {
        return getAccountTags(accountId, AuditLevel.NONE);
    }

    public Tags getAccountTags(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.TAGS;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, Tags.class);
    }

    public Tags createAccountTag(final UUID accountId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.TAGS;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_TAGS, tagDefinitionId.toString()),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPostAndFollowLocation(uri, null, queryParams, Tags.class);
    }

    public void deleteAccountTag(final UUID accountId, final UUID tagDefinitionId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.TAGS;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_TAGS, tagDefinitionId.toString()),
                                                                createdBy,
                                                                reason,
                                                                comment);

        httpClient.doDelete(uri, queryParams);
    }

    // Custom fields

    public CustomFields getCustomFields() throws KillBillClientException {
        return getCustomFields(0L, 100L);
    }

    public CustomFields getCustomFields(final Long offset, final Long limit) throws KillBillClientException {
        return getCustomFields(offset, limit, AuditLevel.NONE);
    }

    public CustomFields getCustomFields(final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.CUSTOM_FIELDS_PATH + "/" + JaxrsResource.PAGINATION;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, CustomFields.class);
    }

    public CustomFields searchCustomFields(final String key) throws KillBillClientException {
        return searchCustomFields(key, 0L, 100L);
    }

    public CustomFields searchCustomFields(final String key, final Long offset, final Long limit) throws KillBillClientException {
        return searchCustomFields(key, offset, limit, AuditLevel.NONE);
    }

    public CustomFields searchCustomFields(final String key, final Long offset, final Long limit, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.CUSTOM_FIELDS_PATH + "/" + JaxrsResource.SEARCH + "/" + UTF8UrlEncoder.encode(key);

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_SEARCH_OFFSET, String.valueOf(offset),
                                                                                JaxrsResource.QUERY_SEARCH_LIMIT, String.valueOf(limit),
                                                                                JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, CustomFields.class);
    }

    public CustomFields getAccountCustomFields(final UUID accountId) throws KillBillClientException {
        return getAccountCustomFields(accountId, AuditLevel.NONE);
    }

    public CustomFields getAccountCustomFields(final UUID accountId, final AuditLevel auditLevel) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Map<String, String> queryParams = ImmutableMap.<String, String>of(JaxrsResource.QUERY_AUDIT, auditLevel.toString());

        return httpClient.doGet(uri, queryParams, CustomFields.class);
    }

    public CustomFields createAccountCustomField(final UUID accountId, final CustomField customField, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        return createAccountCustomFields(accountId, ImmutableList.<CustomField>of(customField), createdBy, reason, comment);
    }

    public CustomFields createAccountCustomFields(final UUID accountId, final Iterable<CustomField> customFields, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(uri, customFields, queryParams, CustomFields.class);
    }

    public void deleteAccountCustomField(final UUID accountId, final UUID customFieldId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteAccountCustomFields(accountId, ImmutableList.<UUID>of(customFieldId), createdBy, reason, comment);
    }

    public void deleteAccountCustomFields(final UUID accountId, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        deleteAccountCustomFields(accountId, null, createdBy, reason, comment);
    }

    public void deleteAccountCustomFields(final UUID accountId, @Nullable final Iterable<UUID> customFieldIds, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.CUSTOM_FIELDS;

        final Map<String, String> paramCustomFields = customFieldIds == null ?
                                                      ImmutableMap.<String, String>of() :
                                                      ImmutableMap.<String, String>of(JaxrsResource.QUERY_CUSTOM_FIELDS, Joiner.on(",").join(customFieldIds));

        final Map<String, String> queryParams = paramsWithAudit(paramCustomFields,
                                                                createdBy,
                                                                reason,
                                                                comment);

        httpClient.doDelete(uri, queryParams);
    }

    // Catalog

    public Catalog getSimpleCatalog() throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH + "/simpleCatalog";

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, Catalog.class);
    }

    public List<PlanDetail> getAvailableAddons(final String baseProductName) throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH + "/availableAddons";

        final Map<String, String> queryParams = ImmutableMap.<String, String>of("baseProductName", baseProductName);

        return httpClient.doGet(uri, queryParams, PlanDetails.class);
    }

    public List<PlanDetail> getBasePlans() throws KillBillClientException {
        final String uri = JaxrsResource.CATALOG_PATH + "/availableBasePlans";

        return httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, PlanDetails.class);
    }

    // Tenants

    public Tenant createTenant(final Tenant tenant, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        Preconditions.checkNotNull(tenant.getApiKey(), "Tenant#apiKey cannot be null");
        Preconditions.checkNotNull(tenant.getApiSecret(), "Tenant#apiSecret cannot be null");

        final Map<String, String> queryParams = paramsWithAudit(createdBy, reason, comment);

        return httpClient.doPostAndFollowLocation(JaxrsResource.TENANTS_PATH, tenant, queryParams, Tenant.class);
    }

    public TenantKey registerCallbackNotificationForTenant(final String callback, final String createdBy, final String reason, final String comment) throws KillBillClientException {
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.REGISTER_NOTIFICATION_CALLBACK;

        final Map<String, String> queryParams = paramsWithAudit(ImmutableMap.<String, String>of(JaxrsResource.QUERY_NOTIFICATION_CALLBACK, callback),
                                                                createdBy,
                                                                reason,
                                                                comment);

        return httpClient.doPostAndFollowLocation(uri, null, queryParams, TenantKey.class);
    }

    public Permissions getPermissions() throws KillBillClientException {
        return httpClient.doGet(JaxrsResource.SECURITY_PATH + "/permissions", DEFAULT_EMPTY_QUERY, Permissions.class);
    }

    // Plugin endpoints

    public Response pluginGET(final String uri) throws Exception {
        return pluginGET(uri, DEFAULT_EMPTY_QUERY);
    }

    public Response pluginGET(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doGet(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams);
    }

    public Response pluginHEAD(final String uri) throws Exception {
        return pluginHEAD(uri, DEFAULT_EMPTY_QUERY);
    }

    public Response pluginHEAD(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doHead(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams);
    }

    public Response pluginPOST(final String uri, @Nullable final String body) throws Exception {
        return pluginPOST(uri, body, DEFAULT_EMPTY_QUERY);
    }

    public Response pluginPOST(final String uri, @Nullable final String body, final Map<String, String> queryParams) throws Exception {
        return httpClient.doPost(JaxrsResource.PLUGINS_PATH + "/" + uri, body, queryParams);
    }

    public Response pluginPUT(final String uri, @Nullable final String body) throws Exception {
        return pluginPUT(uri, body, DEFAULT_EMPTY_QUERY);
    }

    public Response pluginPUT(final String uri, @Nullable final String body, final Map<String, String> queryParams) throws Exception {
        return httpClient.doPut(JaxrsResource.PLUGINS_PATH + "/" + uri, body, queryParams);
    }

    public Response pluginDELETE(final String uri) throws Exception {
        return pluginDELETE(uri, DEFAULT_EMPTY_QUERY);
    }

    public Response pluginDELETE(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doDelete(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams);
    }

    public Response pluginOPTIONS(final String uri) throws Exception {
        return pluginOPTIONS(uri, DEFAULT_EMPTY_QUERY);
    }

    public Response pluginOPTIONS(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doOptions(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams);
    }

    // Utilities

    private Map<String, String> paramsWithAudit(final Map<String, String> queryParams, final String createdBy, final String reason, final String comment) {
        final Map<String, String> queryParamsWithAudit = new HashMap<String, String>();
        queryParamsWithAudit.putAll(queryParams);
        queryParamsWithAudit.putAll(paramsWithAudit(createdBy, reason, comment));
        return queryParamsWithAudit;
    }

    private Map<String, String> paramsWithAudit(final String createdBy, final String reason, final String comment) {
        return ImmutableMap.<String, String>of(KillBillHttpClient.AUDIT_OPTION_CREATED_BY, createdBy,
                                               KillBillHttpClient.AUDIT_OPTION_REASON, reason,
                                               KillBillHttpClient.AUDIT_OPTION_COMMENT, comment);
    }
}
