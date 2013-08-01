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

import java.util.HashMap;
import java.util.Map;

import com.ning.billing.jaxrs.json.TenantJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KillBillTenant extends KillBillBaseResource {

    public static String createTenant(final String apiKey, final String apiSecret) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final String baseJson = mapper.writeValueAsString(new TenantJson(null, null, apiKey, apiSecret));
        final Response response = httpClient.doPost(JaxrsResource.TENANTS_PATH, baseJson, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        return response.getHeader("Location");
    }

    public static String registerCallbackNotificationForTenant(final String callback) throws Exception {
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(JaxrsResource.QUERY_NOTIFICATION_CALLBACK, callback);
        final String uri = JaxrsResource.TENANTS_PATH + "/" + JaxrsResource.REGISTER_NOTIFICATION_CALLBACK;
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doPost(uri, null, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        return response.getHeader("Location");
    }
}
