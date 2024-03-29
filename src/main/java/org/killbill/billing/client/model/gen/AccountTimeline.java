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
import org.killbill.billing.client.model.gen.Bundle;
import org.killbill.billing.client.model.gen.Invoice;
import org.killbill.billing.client.model.gen.InvoicePayment;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
import org.killbill.billing.client.model.KillBillObject;

public class AccountTimeline {

    private Account account = null;

    private List<Bundle> bundles = null;

    private List<Invoice> invoices = null;

    private List<InvoicePayment> payments = null;


    public AccountTimeline() {
    }

    public AccountTimeline(final Account account,
                     final List<Bundle> bundles,
                     final List<Invoice> invoices,
                     final List<InvoicePayment> payments) {
        this.account = account;
        this.bundles = bundles;
        this.invoices = invoices;
        this.payments = payments;

    }


    public AccountTimeline setAccount(final Account account) {
        this.account = account;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public AccountTimeline setBundles(final List<Bundle> bundles) {
        this.bundles = bundles;
        return this;
    }

    public AccountTimeline addBundlesItem(final Bundle bundlesItem) {
        if (this.bundles == null) {
            this.bundles = new ArrayList<Bundle>();
        }
        this.bundles.add(bundlesItem);
        return this;
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public AccountTimeline setInvoices(final List<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public AccountTimeline addInvoicesItem(final Invoice invoicesItem) {
        if (this.invoices == null) {
            this.invoices = new ArrayList<Invoice>();
        }
        this.invoices.add(invoicesItem);
        return this;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public AccountTimeline setPayments(final List<InvoicePayment> payments) {
        this.payments = payments;
        return this;
    }

    public AccountTimeline addPaymentsItem(final InvoicePayment paymentsItem) {
        if (this.payments == null) {
            this.payments = new ArrayList<InvoicePayment>();
        }
        this.payments.add(paymentsItem);
        return this;
    }

    public List<InvoicePayment> getPayments() {
        return payments;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTimeline accountTimeline = (AccountTimeline) o;
        return Objects.equals(this.account, accountTimeline.account) &&
            Objects.equals(this.bundles, accountTimeline.bundles) &&
            Objects.equals(this.invoices, accountTimeline.invoices) &&
            Objects.equals(this.payments, accountTimeline.payments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account,
            bundles,
            invoices,
            payments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccountTimeline {\n");
        
        sb.append("    account: ").append(toIndentedString(account)).append("\n");
        sb.append("    bundles: ").append(toIndentedString(bundles)).append("\n");
        sb.append("    invoices: ").append(toIndentedString(invoices)).append("\n");
        sb.append("    payments: ").append(toIndentedString(payments)).append("\n");
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

