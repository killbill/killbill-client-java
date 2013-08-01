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

import com.ning.billing.jaxrs.json.PlanDetailJson;
import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KillbillCatalog extends KillBillBaseResource {

    protected ObjectMapper mapper = new ObjectMapper();
    protected KillBillHttpClient httpClient = new KillBillHttpClient();

    public static List<PlanDetailJson> getAvailableBasePlans() throws KillBillException, IOException {
        // 	GET /1.0/kb/catalog/availableBasePlans
        final String uri = JaxrsResource.CATALOG_PATH + "/availableBasePlans";
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doGet(uri, new HashMap<String, String>(), KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        String responseJson = response.getResponseBody();
        System.out.println(responseJson);
        ObjectMapper mapper = new ObjectMapper();
        List<PlanDetailJson> basePlans = mapper.readValue(responseJson, new TypeReference<List<PlanDetailJson>>() {});
        return basePlans;
    }

    public static List<PlanDetailJson> getAvailableAddOns(String baseProductName) throws KillBillException, IOException {
        //  GET /1.0/kb/catalog/availableAddons <-	provide 'baseProductName'
        final String uri = JaxrsResource.CATALOG_PATH + "/availableAddons";
        Map<String, String> DEFAULT_EMPTY_QUERY = new HashMap<String, String>();
        DEFAULT_EMPTY_QUERY.put("baseProductName", baseProductName);
        KillBillHttpClient httpClient = new KillBillHttpClient();
        final Response response = httpClient.doGet(uri, DEFAULT_EMPTY_QUERY, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
        if (null == response) {
            throw nullResponse();
        }
        String responseJson = response.getResponseBody();
        System.out.println(responseJson);
        ObjectMapper mapper = new ObjectMapper();
        List<PlanDetailJson> plansJson = mapper.readValue(responseJson, new TypeReference<List<PlanDetailJson>>() {});
        return plansJson;
    }
}
