/*
 * Copyright 2014-2018 Groupon, Inc
 * Copyright 2014-2018 The Billing Project, LLC
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

package org.killbill.billing.client.gen.api;

import java.io.IOException;
import java.util.UUID;

import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.CatalogApi;
import org.killbill.billing.client.api.gen.SubscriptionApi;
import org.killbill.billing.client.api.gen.TenantApi;
import org.killbill.billing.client.model.Accounts;
import org.killbill.billing.client.model.gen.Account;
import org.killbill.billing.client.model.gen.Subscription;
import org.killbill.billing.client.model.gen.Tenant;
import org.killbill.billing.util.api.AuditLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/**
 * API tests for AccountApi
 */
public class AccountApiTest {

    private final static String SERVER_HOST = "127.0.0.1";
    private final static int SERVER_PORT = 8080;

    private final static String TENANT_API_KEY = "swagger";
    private final static String TENANT_API_SECRET = "swagger$ecr$t";

    private Tenant tenant;

    private TenantApi tenantApi;
    private CatalogApi catalogApi;
    private AccountApi accountApi;
    private SubscriptionApi subscriptionApi;

    private RequestOptions requestOptions;

    @BeforeClass
    public void setup() throws KillBillClientException, IOException {
        final KillBillHttpClient httpClient = new KillBillHttpClient(String.format("http://%s:%d", SERVER_HOST, SERVER_PORT),
                                                                     "admin",
                                                                     "password",
                                                                     TENANT_API_KEY,
                                                                     TENANT_API_SECRET,
                                                                     null,
                                                                     null,
                                                                     3 * 1000,
                                                                     1 * 1000,
                                                                     5 * 1000);
        tenantApi = new TenantApi(httpClient);
        catalogApi = new CatalogApi(httpClient);
        accountApi = new AccountApi(httpClient);
        subscriptionApi = new SubscriptionApi(httpClient);

        requestOptions = RequestOptions.builder()
                                       .withCreatedBy("Something")
                                       .withReason("No Reason")
                                       .withComment("No Comment")
                                       .build();

        tenant = setupTenant();
    }

    @Test
    public void basicTest() throws Exception {

        final Account account = new Account();
        final String externalKey = UUID.randomUUID().toString();
        account.setExternalKey(externalKey);
        final Account result = accountApi.createAccount(account, requestOptions);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getExternalKey(), externalKey);

        final UUID accountId = result.getAccountId();

        account.setEmail("somebody@something.org");
        account.setCurrency(Currency.USD);
        accountApi.updateAccount(accountId, account, false, requestOptions);

        final Account result3 = accountApi.getAccount(accountId, false, false, AuditLevel.FULL, requestOptions);
        Assert.assertNotNull(result3);
        Assert.assertEquals(result3.getAccountId(), accountId);
        Assert.assertEquals(result3.getExternalKey(), externalKey);
        Assert.assertEquals(result3.getEmail(), "somebody@something.org");

        final Subscription subscription = new Subscription();
        subscription.setAccountId(accountId);
        //subscription.setExternalKey(bundleExternalKey);
        subscription.setPlanName("simple-monthly");
        final Subscription subscription2 = subscriptionApi.createSubscription(subscription, null, null, false, false, null, false, -1L, ImmutableMap.<String, String>of(), requestOptions);
        Assert.assertNotNull(subscription2);

        accountApi.closeAccount(accountId, false, false, false, true, requestOptions);

        final Account result4 = accountApi.getAccount(accountId, false, false, AuditLevel.FULL, requestOptions);
        Assert.assertNotNull(result4);
        Assert.assertEquals(result4.getAccountId(), accountId);
        Assert.assertEquals(result4.getExternalKey(), externalKey);
        Assert.assertEquals(result4.getEmail(), "somebody@something.org");

    }

    @Test
    public void basicPaginationTest() throws Exception {

        for (int i = 0; i < 5; i++) {
            final Account account = new Account();
            final Account result = accountApi.createAccount(account, requestOptions);
            Assert.assertNotNull(result);
        }

        final Accounts accounts = accountApi.getAccounts(0L, 100L, false, false, AuditLevel.MINIMAL, requestOptions);
        Assert.assertNotNull(accounts);
        Assert.assertTrue(accounts.size() >= 5);
    }

    @Test
    public void basicSearchTest() throws Exception {

        final String externalKey = "Bingo-" + UUID.randomUUID().toString();
        final Account account = new Account();
        account.setExternalKey(externalKey);
        final Account result = accountApi.createAccount(account, requestOptions);
        Assert.assertNotNull(result);

        final Accounts accounts = accountApi.searchAccounts(externalKey, 0L, 100L, false, false, AuditLevel.MINIMAL, requestOptions);
        Assert.assertNotNull(accounts);
        Assert.assertEquals(accounts.size(), 1);
    }

    private Tenant setupTenant() throws KillBillClientException, IOException {
        Tenant tenant = null;
        try {
            tenant = tenantApi.getTenantByApiKey(TENANT_API_KEY, requestOptions);
        } catch (KillBillClientException e) {
            if (tenant == null) {
                Tenant inputTenant = new Tenant();
                inputTenant.setApiKey(TENANT_API_KEY);
                inputTenant.setApiSecret(TENANT_API_SECRET);
                inputTenant.setExternalKey("SWAGGER_GEN");
                tenant = tenantApi.createTenant(inputTenant, false, requestOptions);
            }
        }
        Assert.assertEquals(tenant.getApiKey(), TENANT_API_KEY);
        Assert.assertEquals(tenant.getExternalKey(), "SWAGGER_GEN");

        /*
        String catalog = catalogApi.getCatalogXml(requestOptions);
        Assert.assertNotNull(catalog);

        final InputStream catalogXML = Resources.getResource("catalog.xml").openStream();
        final Readable reader = new InputStreamReader(catalogXML, Charset.forName("UTF-8"));
        final String body = CharStreams.toString(reader);
        catalogApi.uploadCatalogXml(body, requestOptions);
*/
        return tenant;
    }

}
