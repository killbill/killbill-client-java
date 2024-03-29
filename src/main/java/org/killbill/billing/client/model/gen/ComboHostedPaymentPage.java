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
import java.util.ArrayList;
import java.util.List;
import org.killbill.billing.client.model.gen.Account;
import org.killbill.billing.client.model.gen.AuditLog;
import org.killbill.billing.client.model.gen.HostedPaymentPageFields;
import org.killbill.billing.client.model.gen.PaymentMethod;
import org.killbill.billing.client.model.gen.PluginProperty;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
import org.killbill.billing.client.model.KillBillObject;

public class ComboHostedPaymentPage extends KillBillObject {

    private Account account = null;

    private PaymentMethod paymentMethod = null;

    private HostedPaymentPageFields hostedPaymentPageFields = null;

    private List<PluginProperty> paymentMethodPluginProperties = null;



    public ComboHostedPaymentPage() {
    }

    public ComboHostedPaymentPage(final Account account,
                     final PaymentMethod paymentMethod,
                     final HostedPaymentPageFields hostedPaymentPageFields,
                     final List<PluginProperty> paymentMethodPluginProperties,
                     final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.account = account;
        this.paymentMethod = paymentMethod;
        this.hostedPaymentPageFields = hostedPaymentPageFields;
        this.paymentMethodPluginProperties = paymentMethodPluginProperties;

    }


    public ComboHostedPaymentPage setAccount(final Account account) {
        this.account = account;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public ComboHostedPaymentPage setPaymentMethod(final PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public ComboHostedPaymentPage setHostedPaymentPageFields(final HostedPaymentPageFields hostedPaymentPageFields) {
        this.hostedPaymentPageFields = hostedPaymentPageFields;
        return this;
    }

    public HostedPaymentPageFields getHostedPaymentPageFields() {
        return hostedPaymentPageFields;
    }

    public ComboHostedPaymentPage setPaymentMethodPluginProperties(final List<PluginProperty> paymentMethodPluginProperties) {
        this.paymentMethodPluginProperties = paymentMethodPluginProperties;
        return this;
    }

    public ComboHostedPaymentPage addPaymentMethodPluginPropertiesItem(final PluginProperty paymentMethodPluginPropertiesItem) {
        if (this.paymentMethodPluginProperties == null) {
            this.paymentMethodPluginProperties = new ArrayList<PluginProperty>();
        }
        this.paymentMethodPluginProperties.add(paymentMethodPluginPropertiesItem);
        return this;
    }

    public List<PluginProperty> getPaymentMethodPluginProperties() {
        return paymentMethodPluginProperties;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComboHostedPaymentPage comboHostedPaymentPage = (ComboHostedPaymentPage) o;
        return Objects.equals(this.account, comboHostedPaymentPage.account) &&
            Objects.equals(this.paymentMethod, comboHostedPaymentPage.paymentMethod) &&
            Objects.equals(this.hostedPaymentPageFields, comboHostedPaymentPage.hostedPaymentPageFields) &&
            Objects.equals(this.paymentMethodPluginProperties, comboHostedPaymentPage.paymentMethodPluginProperties) &&
            true /* ignoring this.auditLogs for identity operations */;
    }

    @Override
    public int hashCode() {
        return Objects.hash(account,
            paymentMethod,
            hostedPaymentPageFields,
            paymentMethodPluginProperties,
            0 /* ignoring auditLogs for identity operations */ );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ComboHostedPaymentPage {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    account: ").append(toIndentedString(account)).append("\n");
        sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
        sb.append("    hostedPaymentPageFields: ").append(toIndentedString(hostedPaymentPageFields)).append("\n");
        sb.append("    paymentMethodPluginProperties: ").append(toIndentedString(paymentMethodPluginProperties)).append("\n");
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

