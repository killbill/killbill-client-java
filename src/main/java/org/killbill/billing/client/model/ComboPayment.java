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

public abstract class ComboPayment extends KillBillObject {

    private Account account;
    private PaymentMethod paymentMethod;
    private List<PluginProperty> paymentMethodPluginProperties;

    public ComboPayment() { }

    @JsonCreator
    public ComboPayment(@JsonProperty("account") final Account account,
                        @JsonProperty("paymentMethod") final PaymentMethod paymentMethod,
                        @JsonProperty("paymentMethodPluginProperties") final List<PluginProperty> paymentMethodPluginProperties) {
        super(null);
        this.account = account;
        this.paymentMethod = paymentMethod;
        this.paymentMethodPluginProperties = paymentMethodPluginProperties;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(final PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<PluginProperty> getPaymentMethodPluginProperties() {
        return paymentMethodPluginProperties;
    }

    public void setPaymentMethodPluginProperties(final List<PluginProperty> paymentMethodPluginProperties) {
        this.paymentMethodPluginProperties = paymentMethodPluginProperties;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComboPayment{");
        sb.append("account=").append(account);
        sb.append(", paymentMethod=").append(paymentMethod);
        sb.append(", paymentMethodPluginProperties=").append(paymentMethodPluginProperties);
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

        final ComboPayment that = (ComboPayment) o;

        if (account != null ? !account.equals(that.account) : that.account != null) {
            return false;
        }
        if (paymentMethod != null ? !paymentMethod.equals(that.paymentMethod) : that.paymentMethod != null) {
            return false;
        }
        return !(paymentMethodPluginProperties != null ? !paymentMethodPluginProperties.equals(that.paymentMethodPluginProperties) : that.paymentMethodPluginProperties != null);
    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (paymentMethod != null ? paymentMethod.hashCode() : 0);
        result = 31 * result + (paymentMethodPluginProperties != null ? paymentMethodPluginProperties.hashCode() : 0);
        return result;
    }
}
