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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Limit {

    private final String unit;
    private final String max;
    private final String min;

    @JsonCreator
    public Limit(@JsonProperty("unit") final String unit,
                 @JsonProperty("max") final String max,
                 @JsonProperty("min") final String min) {
        this.unit = unit;
        this.max = max;
        this.min = min;
    }

    public String getUnit() {
        return unit;
    }
    public String getSize() {
        return min;
    }
    public String getMax() {
        return max;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Limit{");
        sb.append("unit='").append(unit).append('\'');
        sb.append(", max=").append(max);
        sb.append(", min=").append(min);
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

        final Limit limit = (Limit) o;

        if (unit != null ? !unit.equals(limit.unit) : limit.unit != null) {
            return false;
        }
        if (min != null ? !min.equals(limit.min) : limit.min != null) {
            return false;
        }
        if (max != null ? !max.equals(limit.max) : limit.max != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = unit != null ? unit.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (min != null ? min.hashCode() : 0);
        return result;
    }
}
