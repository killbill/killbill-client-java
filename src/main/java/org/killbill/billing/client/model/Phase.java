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
    private final List<Price> fixedPrices;
    private final Duration duration;

    @JsonCreator
    public Phase(@JsonProperty("type") final String type,
                 @JsonProperty("prices") final List<Price> prices,
                 @JsonProperty("fixedPrices") final List<Price> fixedPrices,
                 @JsonProperty("duration") final Duration duration) {
        this.type = type;
        this.prices = prices;
        this.fixedPrices = fixedPrices;
        this.duration = duration;
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

    public List<Price> getFixedPrices() {
        return fixedPrices;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Phase{");
        sb.append("type='").append(type).append('\'');
        sb.append(", prices=").append(prices);
        sb.append(", fixedPrices=").append(fixedPrices);
        sb.append(", duration=").append(duration);
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
        if (fixedPrices != null ? !fixedPrices.equals(phase.fixedPrices) : phase.fixedPrices != null) {
            return false;
        }
        if (duration != null ? !duration.equals(phase.duration) : phase.duration != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (prices != null ? prices.hashCode() : 0);
        result = 31 * result + (fixedPrices != null ? fixedPrices.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }
}
