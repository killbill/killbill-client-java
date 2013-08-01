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

import java.util.Map;

import com.ning.billing.jaxrs.resources.JaxrsResource;
import com.ning.http.client.Response;

public class KillBillPlugin extends KillBillBaseResource {

    protected Response pluginGET(final String uri) throws Exception {
        return pluginGET(uri, DEFAULT_EMPTY_QUERY);
    }

    protected Response pluginGET(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doGet(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected Response pluginHEAD(final String uri) throws Exception {
        return pluginHEAD(uri, DEFAULT_EMPTY_QUERY);
    }

    protected Response pluginHEAD(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doHead(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected Response pluginPOST(final String uri, final String body) throws Exception {
        return pluginPOST(uri, body, DEFAULT_EMPTY_QUERY);
    }

    protected Response pluginPOST(final String uri, final String body, final Map<String, String> queryParams) throws Exception {
        return httpClient.doPost(JaxrsResource.PLUGINS_PATH + "/" + uri, body, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected Response pluginPUT(final String uri, final String body) throws Exception {
        return pluginPUT(uri, body, DEFAULT_EMPTY_QUERY);
    }

    protected Response pluginPUT(final String uri, final String body, final Map<String, String> queryParams) throws Exception {
        return httpClient.doPut(JaxrsResource.PLUGINS_PATH + "/" + uri, body, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected Response pluginDELETE(final String uri) throws Exception {
        return pluginDELETE(uri, DEFAULT_EMPTY_QUERY);
    }

    protected Response pluginDELETE(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doDelete(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }

    protected Response pluginOPTIONS(final String uri) throws Exception {
        return pluginOPTIONS(uri, DEFAULT_EMPTY_QUERY);
    }

    protected Response pluginOPTIONS(final String uri, final Map<String, String> queryParams) throws Exception {
        return httpClient.doOptions(JaxrsResource.PLUGINS_PATH + "/" + uri, queryParams, KillBillHttpClient.DEFAULT_HTTP_TIMEOUT_SEC);
    }
}
