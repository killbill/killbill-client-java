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

    private String product;
    private String plan;
    private BillingPeriod finalPhaseBillingPeriod;
    private String priceList;
    private List<Price> finalPhaseRecurringPrice;

    @JsonCreator
    public PlanDetail(@JsonProperty("product") final String product,
                      @JsonProperty("plan") final String plan,
                      @JsonProperty("final_phase_billing_period") final BillingPeriod finalPhaseBillingPeriod,
                      @JsonProperty("priceList") final String priceList,
                      @JsonProperty("final_phase_recurring_price") final List<Price> finalPhaseRecurringPrice) {
        this.product = product;
        this.plan = plan;
        this.finalPhaseBillingPeriod = finalPhaseBillingPeriod;
        this.priceList = priceList;
        this.finalPhaseRecurringPrice = finalPhaseRecurringPrice;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(final String product) {
        this.product = product;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(final String plan) {
        this.plan = plan;
    }

    public BillingPeriod getFinalPhaseBillingPeriod() {
        return finalPhaseBillingPeriod;
    }

    public void setFinalPhaseBillingPeriod(final BillingPeriod finalPhaseBillingPeriod) {
        this.finalPhaseBillingPeriod = finalPhaseBillingPeriod;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(final String priceList) {
        this.priceList = priceList;
    }

    public List<Price> getFinalPhaseRecurringPrice() {
        return finalPhaseRecurringPrice;
    }

    public void setFinalPhaseRecurringPrice(final List<Price> finalPhaseRecurringPrice) {
        this.finalPhaseRecurringPrice = finalPhaseRecurringPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanDetail{");
        sb.append("product='").append(product).append('\'');
        sb.append(", plan='").append(plan).append('\'');
        sb.append(", finalPhaseBillingPeriod=").append(finalPhaseBillingPeriod);
        sb.append(", priceList='").append(priceList).append('\'');
        sb.append(", finalPhaseRecurringPrice=").append(finalPhaseRecurringPrice);
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

        if (finalPhaseBillingPeriod != that.finalPhaseBillingPeriod) {
            return false;
        }
        if (finalPhaseRecurringPrice != null ? !finalPhaseRecurringPrice.equals(that.finalPhaseRecurringPrice) : that.finalPhaseRecurringPrice != null) {
            return false;
        }
        if (plan != null ? !plan.equals(that.plan) : that.plan != null) {
            return false;
        }
        if (priceList != null ? !priceList.equals(that.priceList) : that.priceList != null) {
            return false;
        }
        if (product != null ? !product.equals(that.product) : that.product != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + (plan != null ? plan.hashCode() : 0);
        result = 31 * result + (finalPhaseBillingPeriod != null ? finalPhaseBillingPeriod.hashCode() : 0);
        result = 31 * result + (priceList != null ? priceList.hashCode() : 0);
        result = 31 * result + (finalPhaseRecurringPrice != null ? finalPhaseRecurringPrice.hashCode() : 0);
        return result;
    }
}
