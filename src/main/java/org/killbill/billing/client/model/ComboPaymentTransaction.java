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

public class ComboPaymentTransaction extends ComboPayment {

    private PaymentTransaction transaction;
    private List<PluginProperty> transactionPluginProperties;

    public ComboPaymentTransaction() { }

    @JsonCreator
    public ComboPaymentTransaction(@JsonProperty("account") final Account account,
                                   @JsonProperty("paymentMethod") final PaymentMethod paymentMethod,
                                   @JsonProperty("transaction") final PaymentTransaction transaction,
                                   @JsonProperty("paymentMethodPluginProperties") final List<PluginProperty> paymentMethodPluginProperties,
                                   @JsonProperty("transactionPluginProperties") final List<PluginProperty> transactionPluginProperties) {
        super(account, paymentMethod, paymentMethodPluginProperties);
        this.transaction = transaction;
        this.transactionPluginProperties = transactionPluginProperties;
    }

    public PaymentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(final PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public List<PluginProperty> getTransactionPluginProperties() {
        return transactionPluginProperties;
    }

    public void setTransactionPluginProperties(final List<PluginProperty> transactionPluginProperties) {
        this.transactionPluginProperties = transactionPluginProperties;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComboPaymentTransaction{");
        sb.append("transaction=").append(transaction);
        sb.append(", transactionPluginProperties=").append(transactionPluginProperties);
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

        final ComboPaymentTransaction that = (ComboPaymentTransaction) o;

        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) {
            return false;
        }
        return !(transactionPluginProperties != null ? !transactionPluginProperties.equals(that.transactionPluginProperties) : that.transactionPluginProperties != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
        result = 31 * result + (transactionPluginProperties != null ? transactionPluginProperties.hashCode() : 0);
        return result;
    }
}
