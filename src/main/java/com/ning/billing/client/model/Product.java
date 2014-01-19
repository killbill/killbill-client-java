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

package com.ning.billing.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product extends KillBillObject {

    private String type;
    private String name;
    private List<Plan> plans;
    private List<String> included;
    private List<String> available;

    @JsonCreator
    public Product(@JsonProperty("type") final String type,
                   @JsonProperty("name") final String name,
                   @JsonProperty("plans") final List<Plan> plans,
                   @JsonProperty("included") final List<String> included,
                   @JsonProperty("available") final List<String> available) {
        this.type = type;
        this.name = name;
        this.plans = plans;
        this.included = included;
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(final List<Plan> plans) {
        this.plans = plans;
    }

    public List<String> getIncluded() {
        return included;
    }

    public void setIncluded(final List<String> included) {
        this.included = included;
    }

    public List<String> getAvailable() {
        return available;
    }

    public void setAvailable(final List<String> available) {
        this.available = available;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("type='").append(type).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", plans=").append(plans);
        sb.append(", included=").append(included);
        sb.append(", available=").append(available);
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

        final Product product = (Product) o;

        if (available != null ? !available.equals(product.available) : product.available != null) {
            return false;
        }
        if (included != null ? !included.equals(product.included) : product.included != null) {
            return false;
        }
        if (name != null ? !name.equals(product.name) : product.name != null) {
            return false;
        }
        if (plans != null ? !plans.equals(product.plans) : product.plans != null) {
            return false;
        }
        if (type != null ? !type.equals(product.type) : product.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (plans != null ? plans.hashCode() : 0);
        result = 31 * result + (included != null ? included.hashCode() : 0);
        result = 31 * result + (available != null ? available.hashCode() : 0);
        return result;
    }
}
