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

package org.killbill.billing.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;
import org.killbill.billing.client.util.Multimap;
import org.killbill.billing.client.util.TreeMapSetMultimap;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRequestOptions {

    @Test(groups = "fast")
    public void test() {
        // requestOptionsWithImmutableMapValues();
        requestOptionsWithMutableMapValues();
    }

    void requestOptionsWithImmutableMapValues() {
        // Let's consider scenario a user using the java client:
        final Map<String, Collection<String>> queryParams = new HashMap<>();
        // User can generously do this ...
        queryParams.put("key1", new ArrayList<>(List.of("1.1", "1.2", "1.3")));
        // .... or accidentally do this (because after guava removed, this is the "straight forward" approach)
        queryParams.put("key2", List.of("2.1", "2.2", "2.3"));
        // So far, good.
        final RequestOptions requestOptions = new RequestOptionsBuilder()
                .withQueryParams(queryParams)
                .build();

        // .... Then our internal generated API classes have something like this: (eg: InvoiceApi)
        // (Also note that as per '34b59c4c' commit, InvoiceApi still use internal #addToMapValues() method, which is do the same thing)
        final Multimap<String, String> queryParamsInApi = new TreeMapSetMultimap<>(requestOptions.getQueryParams());

        // This one Ok, because user pass mutable object (wrap List.of() to new ArrayList)
        queryParamsInApi.put("key1", "1.4");
        try {
            // This one not Ok, because user pass immutable object first (only use List.of() )
            queryParamsInApi.put("key2", "2.4");
            Assert.fail("Make sure that RequestOptionBuilder#withQueryParams NOT calling toMutableMapValues() method");
        } catch (final UnsupportedOperationException ignored) {
        }
    }

    // Simulating the same thing as "requestOptionsWithImmutableMapValues()", but expect that
    // RequestOptions#withQueryParams() call Req method.

    /**
     * Simulating the same thing as {@link #requestOptionsWithImmutableMapValues()}, but expect that
     */
    private void requestOptionsWithMutableMapValues() {
        final Map<String, Collection<String>> queryParams = new HashMap<>();
        queryParams.put("key1", new ArrayList<>(List.of("1.1", "1.2", "1.3")));
        queryParams.put("key2", List.of("2.1", "2.2", "2.3"));

        final RequestOptions requestOptions = new RequestOptionsBuilder()
                .withQueryParams(queryParams)
                .build();

        final Multimap<String, String> queryParamsInApi = new TreeMapSetMultimap<>(requestOptions.getQueryParams());

        queryParamsInApi.put("key1", "1.4");
        queryParamsInApi.put("key2", "2.4");
    }
}
