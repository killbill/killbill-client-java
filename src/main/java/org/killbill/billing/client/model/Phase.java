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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Phase extends KillBillObject {

    private String type;
    private List<Price> prices;

    @JsonCreator
    public Phase(@JsonProperty("type") final String type,
                 @JsonProperty("prices") final List<Price> prices) {
        this.type = type;
        this.prices = prices;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(final List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Phase{");
        sb.append("type='").append(type).append('\'');
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

        final Phase phase = (Phase) o;

        if (prices != null ? !prices.equals(phase.prices) : phase.prices != null) {
            return false;
        }
        if (type != null ? !type.equals(phase.type) : phase.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (prices != null ? prices.hashCode() : 0);
        return result;
    }
}
