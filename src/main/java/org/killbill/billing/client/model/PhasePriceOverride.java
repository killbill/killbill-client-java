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

import java.math.BigDecimal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PhasePriceOverride {

    private final String planName;
    private final String phaseName;
    private final String phaseType;
    private final BigDecimal fixedPrice;
    private final BigDecimal recurringPrice;

    @JsonCreator
    public PhasePriceOverride(@JsonProperty("planName") final String planName,
                              @JsonProperty("phaseName") final String phaseName,
                              @JsonProperty("phaseType") final String phaseType,
                              @Nullable @JsonProperty("fixedPrice") final BigDecimal fixedPrice,
                              @Nullable @JsonProperty("recurringPrice") final BigDecimal recurringPrice) {
        this.planName = planName;
        this.phaseName = phaseName;
        this.phaseType = phaseType;
        this.fixedPrice = fixedPrice;
        this.recurringPrice = recurringPrice;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public String getPhaseType() {
        return phaseType;
    }

    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }

    public BigDecimal getRecurringPrice() {
        return recurringPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhasePriceOverride)) {
            return false;
        }

        final PhasePriceOverride that = (PhasePriceOverride) o;

        if (planName != null ? !planName.equals(that.planName) : that.planName != null) {
            return false;
        }
        if (phaseName != null ? !phaseName.equals(that.phaseName) : that.phaseName != null) {
            return false;
        }
        if (phaseType != null ? !phaseType.equals(that.phaseType) : that.phaseType != null) {
            return false;
        }
        if (fixedPrice != null ? fixedPrice.compareTo(that.fixedPrice) != 0 : that.fixedPrice != null) {
            return false;
        }
        if (recurringPrice != null ? recurringPrice.compareTo(that.recurringPrice) != 0 : that.recurringPrice != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = phaseName != null ? phaseName.hashCode() : 0;
        result = 31 * result + (planName != null ? planName.hashCode() : 0);
        result = 31 * result + (phaseType != null ? phaseType.hashCode() : 0);
        result = 31 * result + (fixedPrice != null ? fixedPrice.hashCode() : 0);
        result = 31 * result + (recurringPrice != null ? recurringPrice.hashCode() : 0);
        return result;
    }
}
