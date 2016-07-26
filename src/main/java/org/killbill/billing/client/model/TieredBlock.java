/*
 * Copyright 2010-2013 Ning, Inc.
 * Copyright 2016 Groupon, Inc
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

public class TieredBlock {

    private final String unit;
    private final String size;
    private final String max;
    private final List<Price> prices;

    @JsonCreator
    public TieredBlock(@JsonProperty("billingPeriod") final String unit,
                           @JsonProperty("size") final String size,
                           @JsonProperty("max") final String max,
                           @JsonProperty("prices") final List<Price> prices) {
        this.unit = unit;
        this.size = size;
        this.max = max;
        this.prices = prices;
    }

    public String getUnit() {
        return unit;
    }
    public String getSize() {
        return size;
    }
    public String getMax() {
        return max;
    }
    public List<Price> getPrices() {
        return prices;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TieredBlock{");
        sb.append("unit='").append(unit).append('\'');
        sb.append(", size=").append(size);
        sb.append(", max=").append(max);
        sb.append(", prices=").append(prices);
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

        final TieredBlock block = (TieredBlock) o;

        if (unit != null ? !unit.equals(block.unit) : block.unit != null) {
            return false;
        }
        if (size != null ? !size.equals(block.size) : block.size != null) {
            return false;
        }
        if (max != null ? !max.equals(block.max) : block.max != null) {
            return false;
        }
        if (prices != null ? !prices.equals(block.prices) : block.prices != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = unit != null ? unit.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (prices != null ? prices.hashCode() : 0);
        return result;
    }
}
