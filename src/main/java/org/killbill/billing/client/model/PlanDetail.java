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

package org.killbill.billing.client.model;

import java.util.List;

import org.killbill.billing.catalog.api.BillingPeriod;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanDetail extends KillBillObject {

    private String productName;
    private String planName;
    private BillingPeriod billingPeriod;
    private String priceListName;
    private List<Price> finalPhasePrice;

    @JsonCreator
    public PlanDetail(@JsonProperty("product") final String productName,
                      @JsonProperty("plan") final String planName,
                      @JsonProperty("final_phase_billing_period") final BillingPeriod billingPeriod,
                      @JsonProperty("priceList") final String priceListName,
                      @JsonProperty("final_phase_recurring_price") final List<Price> finalPhasePrice) {
        this.productName = productName;
        this.planName = planName;
        this.billingPeriod = billingPeriod;
        this.priceListName = priceListName;
        this.finalPhasePrice = finalPhasePrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(final String planName) {
        this.planName = planName;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(final BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(final String priceListName) {
        this.priceListName = priceListName;
    }

    public List<Price> getFinalPhasePrice() {
        return finalPhasePrice;
    }

    public void setFinalPhasePrice(final List<Price> finalPhasePrice) {
        this.finalPhasePrice = finalPhasePrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanDetail{");
        sb.append("productName='").append(productName).append('\'');
        sb.append(", planName='").append(planName).append('\'');
        sb.append(", billingPeriod=").append(billingPeriod);
        sb.append(", priceListName='").append(priceListName).append('\'');
        sb.append(", finalPhasePrice=").append(finalPhasePrice);
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

        final PlanDetail that = (PlanDetail) o;

        if (billingPeriod != that.billingPeriod) {
            return false;
        }
        if (finalPhasePrice != null ? !finalPhasePrice.equals(that.finalPhasePrice) : that.finalPhasePrice != null) {
            return false;
        }
        if (planName != null ? !planName.equals(that.planName) : that.planName != null) {
            return false;
        }
        if (priceListName != null ? !priceListName.equals(that.priceListName) : that.priceListName != null) {
            return false;
        }
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = productName != null ? productName.hashCode() : 0;
        result = 31 * result + (planName != null ? planName.hashCode() : 0);
        result = 31 * result + (billingPeriod != null ? billingPeriod.hashCode() : 0);
        result = 31 * result + (priceListName != null ? priceListName.hashCode() : 0);
        result = 31 * result + (finalPhasePrice != null ? finalPhasePrice.hashCode() : 0);
        return result;
    }
}
