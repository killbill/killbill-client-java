/*
 * Copyright 2016 The Billing Project, LLC
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

public class Usage {

    private final String billingPeriod;
    private final List<Tier> tiers;

    @JsonCreator
    public Usage(@JsonProperty("billingPeriod") final String billingPeriod,
                 @JsonProperty("tiers") final List<Tier> tiers) {
        this.billingPeriod = billingPeriod;
        this.tiers = tiers;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }
    public List<Tier> getTiers() {
        return tiers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Usage{");
        sb.append("billingPeriod='").append(billingPeriod).append('\'');
        sb.append(", tiers=").append(tiers);
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

        final Usage usage = (Usage) o;

        if (billingPeriod != null ? !billingPeriod.equals(usage.billingPeriod) : usage.billingPeriod != null) {
            return false;
        }
        if (tiers != null ? !tiers.equals(usage.tiers) : usage.tiers != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = billingPeriod != null ? billingPeriod.hashCode() : 0;
        result = 31 * result + (tiers != null ? tiers.hashCode() : 0);
        return result;
    }
}
