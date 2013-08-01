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

import java.util.List;

import com.ning.billing.jaxrs.json.PaymentMethodJson;
import com.ning.billing.jaxrs.json.PaymentMethodJson.PaymentMethodPluginDetailJson;
import com.ning.billing.jaxrs.json.PaymentMethodJson.PaymentMethodProperties;

public class KillBillPaymentMethod extends KillBillBaseResource {

    public static final String PLUGIN_NAME_NOOP = "noop";

    public static PaymentMethodJson getPaymentMethodJson(final String accountId, final List<PaymentMethodProperties> properties) {
        final PaymentMethodPluginDetailJson info = new PaymentMethodPluginDetailJson(null, null, null, null, null, null, null, null, null, null, null, null, null, null, properties);
        return new PaymentMethodJson(null, accountId, true, PLUGIN_NAME_NOOP, info);
    }
}
