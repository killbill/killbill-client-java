/*
 * Copyright 2010-2013 Ning, Inc.
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

public class ComboPaymentTransaction extends KillBillObject {

    private Account account;
    private PaymentMethod paymentMethod;
    private PaymentTransaction transaction;
    private List<PluginProperty> paymentMethodPluginProperties;
    private List<PluginProperty> transactionPluginProperties;


    public ComboPaymentTransaction() { }

    @JsonCreator
    public ComboPaymentTransaction(@JsonProperty("account") final Account account,
                                   @JsonProperty("paymentMethod") final PaymentMethod paymentMethod,
                                   @JsonProperty("transaction") final PaymentTransaction transaction,
                                   @JsonProperty("paymentMethodPluginProperties") final List<PluginProperty> paymentMethodPluginProperties,
                                   @JsonProperty("transactionPluginProperties") final List<PluginProperty> transactionPluginProperties) {
        super(null);
        this.account = account;
        this.paymentMethod = paymentMethod;
        this.transaction = transaction;
        this.paymentMethodPluginProperties = paymentMethodPluginProperties;
        this.transactionPluginProperties = transactionPluginProperties;
    }

    public Account getAccount() {
        return account;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentTransaction getTransaction() {
        return transaction;
    }

    public List<PluginProperty> getPaymentMethodPluginProperties() {
        return paymentMethodPluginProperties;
    }

    public List<PluginProperty> getTransactionPluginProperties() {
        return transactionPluginProperties;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public void setPaymentMethod(final PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTransaction(final PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public void setPaymentMethodPluginProperties(final List<PluginProperty> paymentMethodPluginProperties) {
        this.paymentMethodPluginProperties = paymentMethodPluginProperties;
    }

    public void setTransactionPluginProperties(final List<PluginProperty> transactionPluginProperties) {
        this.transactionPluginProperties = transactionPluginProperties;
    }

    @Override
    public String toString() {
        return "ComboPaymentTransaction{" +
               "account=" + account +
               ", paymentMethod=" + paymentMethod +
               ", transaction=" + transaction +
               ", paymentMethodPluginProperties=" + paymentMethodPluginProperties +
               ", transactionPluginProperties=" + transactionPluginProperties +
               '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComboPaymentTransaction)) {
            return false;
        }

        final ComboPaymentTransaction that = (ComboPaymentTransaction) o;

        if (account != null ? !account.equals(that.account) : that.account != null) {
            return false;
        }
        if (paymentMethod != null ? !paymentMethod.equals(that.paymentMethod) : that.paymentMethod != null) {
            return false;
        }
        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) {
            return false;
        }
        if (paymentMethodPluginProperties != null ? !paymentMethodPluginProperties.equals(that.paymentMethodPluginProperties) : that.paymentMethodPluginProperties != null) {
            return false;
        }
        return !(transactionPluginProperties != null ? !transactionPluginProperties.equals(that.transactionPluginProperties) : that.transactionPluginProperties != null);

    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (paymentMethod != null ? paymentMethod.hashCode() : 0);
        result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
        result = 31 * result + (paymentMethodPluginProperties != null ? paymentMethodPluginProperties.hashCode() : 0);
        result = 31 * result + (transactionPluginProperties != null ? transactionPluginProperties.hashCode() : 0);
        return result;
    }
}
