/*
 * Copyright 2010-2014 Ning, Inc.
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

package com.ning.billing.client.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMethod extends KillBillObject {

    private UUID paymentMethodId;
    private UUID accountId;
    private Boolean isDefault;
    private String pluginName;
    private PaymentMethodPluginDetail pluginInfo;

    public PaymentMethod() {}

    @JsonCreator
    public PaymentMethod(@JsonProperty("paymentMethodId") final UUID paymentMethodId,
                         @JsonProperty("accountId") final UUID accountId,
                         @JsonProperty("isDefault") final Boolean isDefault,
                         @JsonProperty("pluginName") final String pluginName,
                         @JsonProperty("pluginInfo") final PaymentMethodPluginDetail pluginInfo) {
        this.paymentMethodId = paymentMethodId;
        this.accountId = accountId;
        this.isDefault = isDefault;
        this.pluginName = pluginName;
        this.pluginInfo = pluginInfo;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(final UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(final Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(final String pluginName) {
        this.pluginName = pluginName;
    }

    public PaymentMethodPluginDetail getPluginInfo() {
        return pluginInfo;
    }

    public void setPluginInfo(final PaymentMethodPluginDetail pluginInfo) {
        this.pluginInfo = pluginInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentMethod{");
        sb.append("paymentMethodId='").append(paymentMethodId).append('\'');
        sb.append(", accountId='").append(accountId).append('\'');
        sb.append(", isDefault=").append(isDefault);
        sb.append(", pluginName='").append(pluginName).append('\'');
        sb.append(", pluginInfo=").append(pluginInfo);
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

        final PaymentMethod that = (PaymentMethod) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) {
            return false;
        }
        if (paymentMethodId != null ? !paymentMethodId.equals(that.paymentMethodId) : that.paymentMethodId != null) {
            return false;
        }
        if (pluginInfo != null ? !pluginInfo.equals(that.pluginInfo) : that.pluginInfo != null) {
            return false;
        }
        if (pluginName != null ? !pluginName.equals(that.pluginName) : that.pluginName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentMethodId != null ? paymentMethodId.hashCode() : 0;
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        result = 31 * result + (pluginName != null ? pluginName.hashCode() : 0);
        result = 31 * result + (pluginInfo != null ? pluginInfo.hashCode() : 0);
        return result;
    }
}
