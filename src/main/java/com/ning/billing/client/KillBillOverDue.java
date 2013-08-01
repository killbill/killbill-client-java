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

import com.ning.billing.jaxrs.json.OverdueStateJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

public class KillBillOverDue extends KillBillBaseResource {

    protected OverdueStateJson getOverdueStateForAccount(final String accountId) throws Exception {
        return doGetOverdueState(accountId, JaxrsResource.ACCOUNTS);
    }

    protected OverdueStateJson getOverdueStateForBundle(final String bundleId) throws Exception {
        return doGetOverdueState(bundleId, JaxrsResource.BUNDLES);
    }

    protected OverdueStateJson getOverdueStateForSubscription(final String subscriptionId) throws Exception {
        return doGetOverdueState(subscriptionId, JaxrsResource.SUBSCRIPTIONS);
    }

    protected OverdueStateJson doGetOverdueState(final String id, final String resourceType) throws Exception {
        final String overdueURI = JaxrsResource.OVERDUE_PATH + "/" + resourceType + "/" + id;
        final Response overdueResponse = httpClient.doGet(overdueURI, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);

        final OverdueStateJson overdueStateJson = mapper.readValue(overdueResponse.getResponseBody(), OverdueStateJson.class);

        return overdueStateJson;
    }
}
