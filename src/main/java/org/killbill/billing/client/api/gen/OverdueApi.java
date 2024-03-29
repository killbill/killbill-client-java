/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2021 Equinix, Inc
 * Copyright 2014-2021 The Billing Project, LLC
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

import java.util.Objects;

import org.killbill.billing.client.model.gen.Overdue;

import org.killbill.billing.client.Converter;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;

import org.killbill.billing.client.util.Preconditions;
import org.killbill.billing.client.util.Multimap;
import org.killbill.billing.client.util.TreeMapSetMultimap;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
public class OverdueApi {

    private final KillBillHttpClient httpClient;

    public OverdueApi() {
        this(new KillBillHttpClient());
    }

    public OverdueApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Overdue getOverdueConfigJson(final RequestOptions inputOptions) throws KillBillClientException {

        final String uri = "/1.0/kb/overdue";


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, Overdue.class, requestOptions);
    }

    public String getOverdueConfigXml(final RequestOptions inputOptions) throws KillBillClientException {

        final String uri = "/1.0/kb/overdue/xml";


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "text/xml");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doGet(uri, String.class, requestOptions);
    }

    public Overdue uploadOverdueConfigJson(final Overdue body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling uploadOverdueConfigJson");

        final String uri = "/1.0/kb/overdue";


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, Overdue.class, requestOptions);
    }

    public String uploadOverdueConfigXml(final String body, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling uploadOverdueConfigXml");

        final String uri = "/1.0/kb/overdue/xml";


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "text/xml");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, String.class, requestOptions);
    }

}
