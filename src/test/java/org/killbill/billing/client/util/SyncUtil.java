/*
 * Copyright 2014-2017 Groupon, Inc
 * Copyright 2014-2017 The Billing Project, LLC
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

package org.killbill.billing.client.util;

import java.io.Closeable;

import javax.annotation.Nullable;

import org.killbill.billing.client.JaxrsResource;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class SyncUtil implements Closeable {

    private static final String TEST = "test";
    private static final String TEST_PATH = JaxrsResource.PREFIX + "/" + TEST;
    private static final String QUEUES_PATH = TEST_PATH + "/queues";
    private static final String QUERY_TIMEOUT_SEC = "timeoutSec";

    private final KillBillHttpClient httpClient;

    public SyncUtil(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void close() {
        httpClient.close();
    }

    public void waitForQueuesToComplete(@Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (timeoutSec != null) {
            queryParams.put(QUERY_TIMEOUT_SEC, Long.toString(timeoutSec));
        }
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();
        httpClient.doGet(QUEUES_PATH, requestOptions);
    }

}
