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


package org.killbill.billing.client.model.gen;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.killbill.billing.client.model.gen.AuditLog;
import org.killbill.billing.client.model.gen.PaymentMethodPluginDetail;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
import org.killbill.billing.client.model.KillBillObject;

public class PaymentMethod extends KillBillObject {

    private UUID paymentMethodId = null;

    private String externalKey = null;

    private UUID accountId = null;

    private Boolean isDefault = null;

    private String pluginName = null;

    private PaymentMethodPluginDetail pluginInfo = null;



    public PaymentMethod() {
    }

    public PaymentMethod(final UUID paymentMethodId,
                     final String externalKey,
                     final UUID accountId,
                     final Boolean isDefault,
                     final String pluginName,
                     final PaymentMethodPluginDetail pluginInfo,
                     final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.paymentMethodId = paymentMethodId;
        this.externalKey = externalKey;
        this.accountId = accountId;
        this.isDefault = isDefault;
        this.pluginName = pluginName;
        this.pluginInfo = pluginInfo;

    }


    public PaymentMethod setPaymentMethodId(final UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public PaymentMethod setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
        return this;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public PaymentMethod setAccountId(final UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public PaymentMethod setIsDefault(final Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    @JsonProperty(value="isDefault")
    public Boolean isDefault() {
        return isDefault;
    }

    public PaymentMethod setPluginName(final String pluginName) {
        this.pluginName = pluginName;
        return this;
    }

    public String getPluginName() {
        return pluginName;
    }

    public PaymentMethod setPluginInfo(final PaymentMethodPluginDetail pluginInfo) {
        this.pluginInfo = pluginInfo;
        return this;
    }

    public PaymentMethodPluginDetail getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentMethod paymentMethod = (PaymentMethod) o;
        return Objects.equals(this.paymentMethodId, paymentMethod.paymentMethodId) &&
            Objects.equals(this.externalKey, paymentMethod.externalKey) &&
            Objects.equals(this.accountId, paymentMethod.accountId) &&
            Objects.equals(this.isDefault, paymentMethod.isDefault) &&
            Objects.equals(this.pluginName, paymentMethod.pluginName) &&
            Objects.equals(this.pluginInfo, paymentMethod.pluginInfo) &&
            true /* ignoring this.auditLogs for identity operations */;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentMethodId,
            externalKey,
            accountId,
            isDefault,
            pluginName,
            pluginInfo,
            0 /* ignoring auditLogs for identity operations */ );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PaymentMethod {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    paymentMethodId: ").append(toIndentedString(paymentMethodId)).append("\n");
        sb.append("    externalKey: ").append(toIndentedString(externalKey)).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    isDefault: ").append(toIndentedString(isDefault)).append("\n");
        sb.append("    pluginName: ").append(toIndentedString(pluginName)).append("\n");
        sb.append("    pluginInfo: ").append(toIndentedString(pluginInfo)).append("\n");
        sb.append("    auditLogs: ").append(toIndentedString(auditLogs)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

