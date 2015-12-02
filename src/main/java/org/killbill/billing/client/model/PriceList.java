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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceList {

    private String name;
    private List<String> plans;

    @JsonCreator
    public PriceList(@JsonProperty("name") final String name,
                         @JsonProperty("plans") final List<String> plans) {
        this.name = name;
        this.plans = plans;
    }

    public String getName() {
        return name;
    }

    public List<String> getPlans() {
        return plans;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PriceList{");
        sb.append("name='").append(name).append('\'');
        sb.append(", plans=").append(plans);
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

        final PriceList that = (PriceList) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return !(plans != null ? !plans.equals(that.plans) : that.plans != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (plans != null ? plans.hashCode() : 0);
        return result;
    }

}
