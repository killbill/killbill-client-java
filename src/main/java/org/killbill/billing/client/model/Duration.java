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

import org.killbill.billing.catalog.api.CurrencyValueNull;
import org.killbill.billing.catalog.api.TimeUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Duration {

    private final TimeUnit unit;
    private final int number;

    @JsonCreator
    public Duration(@JsonProperty("unit") final TimeUnit unit,
                        @JsonProperty("number") final int number) {
        this.unit = unit;
        this.number = number;
    }

    public Duration(final org.killbill.billing.catalog.api.Duration duration) throws CurrencyValueNull {
        this(duration.getUnit(), duration.getNumber());
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DurationJson{");
        sb.append("unit='").append(unit).append('\'');
        sb.append(", number=").append(number);
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

        final Duration that = (Duration) o;

        if (unit != null ? !unit.equals(that.unit) : that.unit != null) {
            return false;
        }

        return number == that.number;

    }

    @Override
    public int hashCode() {
        int result = unit != null ? unit.hashCode() : 0;
        result = 31 * result + number;
        return result;
    }

}
