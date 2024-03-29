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

import org.killbill.billing.client.model.gen.ComboHostedPaymentPage;
import org.killbill.billing.client.model.gen.HostedPaymentPageFields;
import org.killbill.billing.client.model.gen.HostedPaymentPageFormDescriptor;
import java.util.UUID;
import java.util.List;
import java.util.Map;

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
public class PaymentGatewayApi {

    private final KillBillHttpClient httpClient;

    public PaymentGatewayApi() {
        this(new KillBillHttpClient());
    }

    public PaymentGatewayApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HostedPaymentPageFormDescriptor buildComboFormDescriptor(final ComboHostedPaymentPage body, final List<String> controlPluginName, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling buildComboFormDescriptor");

        final String uri = "/1.0/kb/paymentGateways/hosted/form";

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (controlPluginName != null) {
            queryParams.putAll("controlPluginName", controlPluginName);
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, HostedPaymentPageFormDescriptor.class, requestOptions);
    }

    public HostedPaymentPageFormDescriptor buildFormDescriptor(final UUID accountId, final HostedPaymentPageFields body, final UUID paymentMethodId, final List<String> controlPluginName, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(accountId, "Missing the required parameter 'accountId' when calling buildFormDescriptor");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling buildFormDescriptor");

        final String uri = "/1.0/kb/paymentGateways/hosted/form/{accountId}"
          .replaceAll("\\{" + "accountId" + "\\}", accountId.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (paymentMethodId != null) {
            queryParams.put("paymentMethodId", String.valueOf(paymentMethodId));
        }
        if (controlPluginName != null) {
            queryParams.putAll("controlPluginName", controlPluginName);
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        return httpClient.doPost(uri, body, HostedPaymentPageFormDescriptor.class, requestOptions);
    }

    public void processNotification(final String pluginName, final String body, final List<String> controlPluginName, final Map<String, String> pluginProperty, final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(pluginName, "Missing the required parameter 'pluginName' when calling processNotification");
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling processNotification");

        final String uri = "/1.0/kb/paymentGateways/notification/{pluginName}"
          .replaceAll("\\{" + "pluginName" + "\\}", pluginName.toString());

        final Multimap<String, String> queryParams = new TreeMapSetMultimap<>(inputOptions.getQueryParams());
        if (controlPluginName != null) {
            queryParams.putAll("controlPluginName", controlPluginName);
        }
        if (pluginProperty != null) {
            queryParams.putAll("pluginProperty", Converter.convertPluginPropertyMap(pluginProperty));
        }

        final RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = Objects.requireNonNullElse(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withQueryParams(queryParams.asMap());
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "*/*");
        final RequestOptions requestOptions = inputOptionsBuilder.build();

        httpClient.doPost(uri, body, requestOptions);
    }

}
