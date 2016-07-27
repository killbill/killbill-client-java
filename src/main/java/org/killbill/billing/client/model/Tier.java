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

public class Tier {

    private final List<TieredBlock> blocks;
    private final List<Limit> limits;
    private final List<Price> fixedPrice;
    private final List<Price> recurringPrice;


    @JsonCreator
    public Tier(@JsonProperty("blocks") final List<TieredBlock> blocks,
                @JsonProperty("limits") final List<Limit> limits,
                @JsonProperty("fixedPrice") final List<Price> fixedPrice,
                @JsonProperty("recurringPrice") final List<Price> recurringPrice) {
        this.blocks = blocks;
        this.limits = limits;
        this.fixedPrice = fixedPrice;
        this.recurringPrice = recurringPrice;
    }

    public List<TieredBlock> getBlocks() {
        return blocks;
    }
    public List<Limit> getLimits() {
        return limits;
    }
    public List<Price> getFixedPrice() {
        return fixedPrice;
    }
    public List<Price> getRecurringPrice() {
        return recurringPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tier{");
        sb.append("blocks='").append(blocks);
        sb.append(", limits=").append(limits);
        sb.append(", fixedPrice=").append(fixedPrice);
        sb.append(", recurringPrice=").append(recurringPrice);
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

        final Tier tier = (Tier) o;

        if (blocks != null ? !blocks.equals(tier.blocks) : tier.blocks != null) {
            return false;
        }
        if (limits != null ? !limits.equals(tier.limits) : tier.limits != null) {
            return false;
        }
        if (fixedPrice != null ? !fixedPrice.equals(tier.fixedPrice) : tier.fixedPrice != null) {
            return false;
        }
        if (recurringPrice != null ? !recurringPrice.equals(tier.recurringPrice) : tier.recurringPrice != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = blocks != null ? blocks.hashCode() : 0;
        result = 31 * result + (limits != null ? limits.hashCode() : 0);
        result = 31 * result + (fixedPrice != null ? fixedPrice.hashCode() : 0);
        result = 31 * result + (recurringPrice != null ? recurringPrice.hashCode() : 0);
        return result;
    }
}
