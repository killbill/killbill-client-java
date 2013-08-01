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

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ning.billing.jaxrs.json.AccountJson;

public class TestKillBillAccount {

    @Test(groups = "slow")
    public void testCreateAndRetrieveAccount() throws Exception {
        final KillBillAccount account = new KillBillAccount();
        account.setName(UUID.randomUUID().toString());
        account.setExternalKey(UUID.randomUUID().toString());
        account.setEmail(UUID.randomUUID().toString());
        account.setCurrency("EUR");

        final KillBillAccount retrievedAccount = account.createAccount();
        Assert.assertEquals(retrievedAccount.getName(), account.getName());
        Assert.assertEquals(retrievedAccount.getExternalKey(), account.getExternalKey());
        Assert.assertEquals(retrievedAccount.getEmail(), account.getEmail());
        Assert.assertEquals(retrievedAccount.getCurrency(), account.getCurrency());

        final AccountJson retrievedAccountJson = KillBillAccount.doGetAccountById(retrievedAccount.getAccountId());
        Assert.assertEquals(retrievedAccountJson.getName(), account.getName());
        Assert.assertEquals(retrievedAccountJson.getExternalKey(), account.getExternalKey());
        Assert.assertEquals(retrievedAccountJson.getEmail(), account.getEmail());
        Assert.assertEquals(retrievedAccountJson.getCurrency(), account.getCurrency());
    }
}
