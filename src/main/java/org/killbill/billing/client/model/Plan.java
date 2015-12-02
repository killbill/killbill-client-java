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

import org.killbill.billing.catalog.api.BillingPeriod;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Plan extends KillBillObject {

    private final String name;
    private final BillingPeriod billingPeriod;
    private final List<Phase> phases;

    @JsonCreator
    public Plan(@JsonProperty("name") final String name,
                @JsonProperty("billingPeriod") final BillingPeriod billingPeriod,
                @JsonProperty("phases") final List<Phase> phases) {
        this.name = name;
        this.billingPeriod = billingPeriod;
        this.phases = phases;
    }

    public String getName() {
        return name;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Plan{");
        sb.append("name='").append(name).append('\'');
        sb.append(", billingPeriod='").append(billingPeriod).append('\'');
        sb.append(", phases=").append(phases);
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

        final Plan plan = (Plan) o;

        if (name != null ? !name.equals(plan.name) : plan.name != null) {
            return false;
        }
        if (billingPeriod != null ? !billingPeriod.equals(plan.billingPeriod) : plan.billingPeriod != null) {
            return false;
        }
        if (phases != null ? !phases.equals(plan.phases) : plan.phases != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (billingPeriod != null ? billingPeriod.hashCode() : 0);
        result = 31 * result + (phases != null ? phases.hashCode() : 0);
        return result;
    }
}
