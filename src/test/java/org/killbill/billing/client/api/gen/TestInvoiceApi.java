/*
 * Copyright 2020-2022 Equinix, Inc
 * Copyright 2014-2022 The Billing Project, LLC
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

import java.util.UUID;

import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;
import org.killbill.billing.client.util.Multimap;
import org.killbill.billing.client.util.TreeMapSetMultimap;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class TestInvoiceApi {

    // make sure that withQueryParams() will not throw the same problem as before
    // adding RequestOptionsBuilder#toMutableMapValues()
    @Test(groups = "fast")
    public void getPaymentsForInvoice() throws KillBillClientException {
        final KillBillHttpClient httpClient = Mockito.mock(KillBillHttpClient.class);

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>();
        queryParams.put("key1", "value1");

        final RequestOptions requestOptions = new RequestOptionsBuilder()
                .withQueryParams(queryParams.asMap())
                .build();

        final InvoiceApi api = new InvoiceApi(httpClient);
        api.getPaymentsForInvoice(UUID.randomUUID(), requestOptions);

        Mockito.verify(httpClient, Mockito.times(1)).doGet(Mockito.anyString(), (Class<?>) Mockito.any(), Mockito.any());
    }
}
