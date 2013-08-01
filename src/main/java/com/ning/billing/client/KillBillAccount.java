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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ning.billing.jaxrs.json.AccountEmailJson;
import com.ning.billing.jaxrs.json.AccountJson;
import com.ning.billing.jaxrs.json.AccountTimelineJson;
import com.ning.billing.jaxrs.json.PaymentMethodJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.billing.util.api.AuditLevel;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KillBillAccount extends KillBillBaseResource {

    protected String accountId;
    protected String name;
    protected String externalKey;
    protected String email;
    protected String currency;
    protected String paymentMethodId;
    protected String address1;
    protected String address2;
    protected String postalCode;
    protected String company;
    protected String city;
    protected String state;
    protected String country;
    protected String locale;
    protected String phone;
    protected String timeZone;

    protected Integer firstNameLength;
    protected Integer billCycleDayLocal;
    protected Boolean isMigrated = false;
    protected Boolean isNotifiedForInvoices = true;

    protected KillBillAccount() {
        super();
    }

    public KillBillAccount(String accountId, String name, String externalKey, String email) {
        this.accountId = accountId;
        if (null == accountId) {
            this.accountId = UUID.randomUUID().toString();
        }
        this.name = name;
        this.externalKey = externalKey;
        this.email = email;
    }

    public KillBillAccount createAccount() throws IOException, KillBillException {
        AccountJson json = doCreateAccount(toAccountJson());
        return toAccount(json);
    }

    public KillBillAccount updateAccountDetails() throws KillBillException, Exception {
        final String baseJson = mapper.writeValueAsString(toAccountJson());
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId;
        final Response response = httpClient.doPut(uri, baseJson, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String retrievedJson = response.getResponseBody();
        final AccountJson objFromJson = mapper.readValue(retrievedJson, AccountJson.class);
        return toAccount(objFromJson);
    }

    public KillBillAccount getAccountDetailsByExternalKey() throws KillBillException, Exception {
        return toAccount(doGetAccountByExternalKey(externalKey));
    }

    public KillBillAccount getAccountDetailsById() throws KillBillException, Exception {
        return toAccount(doGetAccountById(accountId));
    }

    public KillBillAccount getAccountDetails() throws KillBillException, Exception {
        if (null != this.accountId) {
            return getAccountDetailsById();
        } else if (null != this.externalKey) {
            return getAccountDetailsByExternalKey();
        }
        return null;
    }

    public boolean _isAccountAlreadyExists() throws KillBillException, Exception {
        return (null != getAccountDetails());
    }

    public AccountJson toAccountJson() {
        return new AccountJson(accountId, name, firstNameLength, externalKey, email, billCycleDayLocal, currency, paymentMethodId, timeZone,
                               address1, address2, postalCode, company, city, state, country, locale, phone, isMigrated, isNotifiedForInvoices);
    }

    public List<AccountEmailJson> getEmailsForAccount(final String accountId) throws IOException, KillBillException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAILS;
        final Response response = httpClient.doGet(uri, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        return mapper.readValue(response.getResponseBody(), new TypeReference<List<AccountEmailJson>>() {});
    }

    public void addEmailToAccount(final String email) throws IOException, KillBillException {
        AccountEmailJson json = new AccountEmailJson(accountId, email);
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAILS;
        final String emailString = mapper.writeValueAsString(json);
        final Response response = httpClient.doPost(uri, emailString, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
    }

    public void removeEmailFromAccount(final String email) throws IOException, KillBillException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.EMAILS;
        final Response response = httpClient.doDelete(uri + "/" + email, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
    }

    public AccountTimelineJson doGetAccountTimeline(final String accountId) throws IOException, KillBillException {
        return doGetAccountTimeline(accountId, AuditLevel.NONE);
    }

    public AccountTimelineJson getAccountTimelineWithAudits(final String accountId, final AuditLevel auditLevel) throws IOException, KillBillException {
        return doGetAccountTimeline(accountId, auditLevel);
    }

    private static String searchAccount(String searchKey) throws KillBillException, IOException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/search/" + searchKey;
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doGet(uri, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        String responseText = response.getResponseBody();
        return responseText;
    }

    public static KillBillAccount toAccount(AccountJson json) throws IOException, KillBillException {
        if (null == json) {
            return null;
        }
        KillBillAccount account = new KillBillAccount();
        account.setAccountId(json.getAccountId());
        account.setAddress1(json.getAddress1());
        account.setAddress2(json.getAddress2());
        account.setBillCycleDayLocal(json.getBillCycleDayLocal());
        account.setCity(json.getCity());
        account.setCompany(json.getCompany());
        account.setCountry(json.getCountry());
        account.setCurrency(json.getCurrency());
        account.setEmail(json.getEmail());
        account.setExternalKey(json.getExternalKey());
        account.setIsMigrated(json.isMigrated());
        account.setIsNotifiedForInvoices(json.isNotifiedForInvoices());
        account.setFirstNameLength(json.getFirstNameLength());
        account.setLocale(json.getLocale());
        account.setName(json.getName());
        account.setPaymentMethodId(json.getPaymentMethodId());
        account.setPhone(json.getPhone());
        account.setPostalCode(json.getPostalCode());
        account.setState(json.getState());
        account.setTimeZone(json.getTimeZone());
        return account;
    }

    protected AccountTimelineJson doGetAccountTimeline(final String accountId, final AuditLevel auditLevel) throws IOException, KillBillException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + accountId + "/" + JaxrsResource.TIMELINE;
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_AUDIT, auditLevel.toString());
        final Response response = httpClient.doGet(uri, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String baseJson = response.getResponseBody();
        final AccountTimelineJson objFromJson = mapper.readValue(baseJson, AccountTimelineJson.class);
        return objFromJson;
    }

    protected static AccountJson doGetAccountById(final String id) throws IOException, KillBillException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + id;
        ObjectMapper mapper = new ObjectMapper();
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doGet(uri, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        final String baseJson = response.getResponseBody();
        final AccountJson objFromJson = mapper.readValue(baseJson, AccountJson.class);
        return objFromJson;
    }

    protected static AccountJson doGetAccountByExternalKey(final String externalKey) throws IOException, KillBillException {
        ObjectMapper mapper = new ObjectMapper();
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_EXTERNAL_KEY, externalKey);
        Response response = httpClient.doGet(JaxrsResource.ACCOUNTS_PATH, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        String baseJson = response.getResponseBody();
        AccountJson objFromJson = mapper.readValue(baseJson, AccountJson.class);
        return objFromJson;
    }

    protected AccountJson doCreateAccountWithDefaultPaymentMethod(final AccountJson input) throws IOException, KillBillException {
        final String uri = JaxrsResource.ACCOUNTS_PATH + "/" + input.getAccountId() + "/" + JaxrsResource.PAYMENT_METHODS;
        final PaymentMethodJson paymentMethodJson = KillBillPaymentMethod.getPaymentMethodJson(input.getAccountId(), null);
        String baseJson = mapper.writeValueAsString(paymentMethodJson);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_PAYMENT_METHOD_IS_DEFAULT, "true");
        Response response = httpClient.doPost(uri, baseJson, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        return doGetAccountByExternalKey(input.getExternalKey());
    }

    protected AccountJson doCreateAccount(AccountJson input) throws IOException, KillBillException {
        String baseJson = mapper.writeValueAsString(input);
        Response response = httpClient.doPost(JaxrsResource.ACCOUNTS_PATH, baseJson, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        return doGetAccountByExternalKey(input.getExternalKey());
    }

    private AccountJson getAccountJson(String json) throws KillBillException {
        if (null == json) {
            return null;
        }
        try {
            AccountJson objFromJson = mapper.readValue(json, AccountJson.class);
            return objFromJson;
        } catch (IOException e) {    // json is plain text like: {cause=null, code=3000, formattedMsg='Account already exists for key 123'}
            throw KillBillException.parse(json);
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getFirstNameLength() {
        return firstNameLength;
    }

    public void setFirstNameLength(Integer length) {
        this.firstNameLength = length;
    }

    public Integer getBillCycleDayLocal() {
        return billCycleDayLocal;
    }

    public void setBillCycleDayLocal(Integer billCycleDayLocal) {
        this.billCycleDayLocal = billCycleDayLocal;
    }

    public Boolean getIsMigrated() {
        return isMigrated;
    }

    public void setIsMigrated(Boolean isMigrated) {
        this.isMigrated = isMigrated;
    }

    public Boolean getIsNotifiedForInvoices() {
        return isNotifiedForInvoices;
    }

    public void setIsNotifiedForInvoices(Boolean isNotifiedForInvoices) {
        this.isNotifiedForInvoices = isNotifiedForInvoices;
    }
}
