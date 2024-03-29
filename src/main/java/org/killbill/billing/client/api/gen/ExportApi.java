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

import java.util.UUID;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;

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
public class ExportApi {

    private final KillBillHttpClient httpClient;

    public ExportApi() {
        this(new KillBillHttpClient());
    }

    public ExportApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public int exportDataForAccount(final UUID accountId, final OutputStream outputStream, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(accountId, "Missing the required parameter 'accountId' when calling exportDataForAccount");

        final String uri = "/1.0/kb/export/{accountId}"
          .replaceAll("\\{" + "accountId" + "\\}", accountId.toString());


        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/octet-stream");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        final HttpResponse<InputStream> response = httpClient.doGet(uri, outputStream, requestOptions);
        return response.statusCode();
    }

}
