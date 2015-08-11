/*
 * Copyright 2015 Groupon, Inc
 * Copyright 2015 The Billing Project, LLC
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

package org.killbill.billing.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ComboHostedPaymentPage extends ComboPayment {

    private HostedPaymentPageFields hostedPaymentPageFields;

    public ComboHostedPaymentPage() { }

    @JsonCreator
    public ComboHostedPaymentPage(@JsonProperty("account") final Account account,
                                  @JsonProperty("paymentMethod") final PaymentMethod paymentMethod,
                                  @JsonProperty("paymentMethodPluginProperties") final List<PluginProperty> paymentMethodPluginProperties,
                                  @JsonProperty("hostedPaymentPageFields") final HostedPaymentPageFields hostedPaymentPageFields) {
        super(account, paymentMethod, paymentMethodPluginProperties);
        this.hostedPaymentPageFields = hostedPaymentPageFields;
    }

    public HostedPaymentPageFields getHostedPaymentPageFields() {
        return hostedPaymentPageFields;
    }

    public void setHostedPaymentPageFields(final HostedPaymentPageFields hostedPaymentPageFields) {
        this.hostedPaymentPageFields = hostedPaymentPageFields;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComboHostedPaymentPage{");
        sb.append("hostedPaymentPageFields=").append(hostedPaymentPageFields);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ComboHostedPaymentPage that = (ComboHostedPaymentPage) o;

        return !(hostedPaymentPageFields != null ? !hostedPaymentPageFields.equals(that.hostedPaymentPageFields) : that.hostedPaymentPageFields != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (hostedPaymentPageFields != null ? hostedPaymentPageFields.hashCode() : 0);
        return result;
    }
}
