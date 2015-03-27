/*
 * Copyright 2010-2013 Ning, Inc.
 * Copyright 2014 Groupon, Inc
 * Copyright 2014 The Billing Project, LLC
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
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.PhaseType;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.entitlement.api.SubscriptionEventType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceDryRun {

    private final SubscriptionEventType dryRunAction;
    private final PhaseType phaseType;
    private final String productName;
    private final ProductCategory productCategory;
    private final BillingPeriod billingPeriod;
    private final String priceListName;
    private final LocalDate effectiveDate;
    private final UUID subscriptionId;
    private final UUID bundleId;
    private final BillingActionPolicy billingPolicy;
    private final List<PhasePriceOverride> priceOverrides;
    @JsonCreator
    public InvoiceDryRun(@JsonProperty("dryRunAction") @Nullable final SubscriptionEventType dryRunAction,
                         @JsonProperty("phaseType") @Nullable final PhaseType phaseType,
                         @JsonProperty("productName") @Nullable final String productName,
                         @JsonProperty("productCategory") @Nullable final ProductCategory productCategory,
                         @JsonProperty("billingPeriod") @Nullable final BillingPeriod billingPeriod,
                         @JsonProperty("priceListName") @Nullable final String priceListName,
                         @JsonProperty("subscriptionId") @Nullable final UUID subscriptionId,
                         @JsonProperty("bundleId") @Nullable final UUID bundleId,
                         @JsonProperty("effectiveDate") @Nullable final LocalDate effectiveDate,
                         @JsonProperty("billingPolicy") @Nullable final BillingActionPolicy billingPolicy,
                         @JsonProperty("priceOverrides") @Nullable final List<PhasePriceOverride> priceOverrides) {
        this.dryRunAction = dryRunAction;
        this.phaseType = phaseType;
        this.productName = productName;
        this.productCategory = productCategory;
        this.billingPeriod = billingPeriod;
        this.priceListName = priceListName;
        this.subscriptionId = subscriptionId;
        this.bundleId = bundleId;
        this.effectiveDate = effectiveDate;
        this.billingPolicy = billingPolicy;
        this.priceOverrides= priceOverrides;
    }

    public SubscriptionEventType getDryRunAction() {
        return dryRunAction;
    }

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public String getProductName() {
        return productName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public String getPriceListName() {
        return priceListName;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public UUID getBundleId() {
        return bundleId;
    }

    public BillingActionPolicy getBillingPolicy() {
        return billingPolicy;
    }

    public List<PhasePriceOverride> getPriceOverrides() {
        return priceOverrides;
    }
}
