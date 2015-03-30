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

    private final String phaseName;
    private final String phaseType;
    private final BigDecimal fixedPrice;
    private final BigDecimal recurringPrice;

    @JsonCreator
    public PhasePriceOverride(@JsonProperty("phaseName") final String phaseName,
                              @JsonProperty("phaseType") final String phaseType,
                              @Nullable @JsonProperty("fixedPrice") final BigDecimal fixedPrice,
                              @Nullable @JsonProperty("recurringPrice") final BigDecimal recurringPrice) {
        this.phaseName = phaseName;
        this.phaseType = phaseType;
        this.fixedPrice = fixedPrice;
        this.recurringPrice = recurringPrice;
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
}
