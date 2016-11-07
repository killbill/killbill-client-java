/*
 * Copyright 2014-2015 Groupon, Inc
 * Copyright 2014-2015 The Billing Project, LLC
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

public class BlockPriceOverride {

    private String unitName;

    private Double size;

    private BigDecimal price;

    private Double max;

    @JsonCreator
    public BlockPriceOverride(@Nullable @JsonProperty("unitName") final String unitName,
                              @Nullable @JsonProperty("size") final Double size,
                              @Nullable @JsonProperty("price") final BigDecimal price,
                              @Nullable @JsonProperty("max") final Double max) {
        this.unitName = unitName;
        this.size = size;
        this.price = price;
        this.max = max;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Double getSize() {
        return size;
    }

    public String getUnitName() {
        return unitName;
    }

    public Double getMax() {
        return max;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockPriceOverride)) {
            return false;
        }

        final BlockPriceOverride that = (BlockPriceOverride) o;


        if (unitName != null ? !unitName.equals(that.unitName) : that.unitName != null) {
            return false;
        }
        if (size != null ? size.compareTo(that.size) != 0 : that.size != null) {
            return false;
        }

        if (price != null ? price.compareTo(that.price) != 0 : that.price != null) {
            return false;
        }

        if (max != null ? max.compareTo(that.max) != 0 : that.max != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = unitName != null ? unitName.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }
}
