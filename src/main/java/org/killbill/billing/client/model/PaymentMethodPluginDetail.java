/*
 * Copyright 2010-2014 Ning, Inc.
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

public class PaymentMethodPluginDetail {

    private String externalPaymentMethodId;
    private Boolean isDefaultPaymentMethod;
    private List<PluginProperty> properties;

    public PaymentMethodPluginDetail() {}

    @JsonCreator
    public PaymentMethodPluginDetail(@JsonProperty("externalPaymentMethodId") final String externalPaymentMethodId,
                                     @JsonProperty("isDefaultPaymentMethod") final Boolean isDefaultPaymentMethod,
                                     @JsonProperty("properties") final List<PluginProperty> properties) {
        this.externalPaymentMethodId = externalPaymentMethodId;
        this.isDefaultPaymentMethod = isDefaultPaymentMethod;
        this.properties = properties;
    }

    public String getExternalPaymentMethodId() {
        return externalPaymentMethodId;
    }

    public void setExternalPaymentMethodId(final String externalPaymentMethodId) {
        this.externalPaymentMethodId = externalPaymentMethodId;
    }

    public Boolean getIsDefaultPaymentMethod() {
        return isDefaultPaymentMethod;
    }

    public void setIsDefaultPaymentMethod(final Boolean isDefaultPaymentMethod) {
        this.isDefaultPaymentMethod = isDefaultPaymentMethod;
    }

    public List<PluginProperty> getProperties() {
        return properties;
    }

    public void setProperties(final List<PluginProperty> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentMethodPluginDetail{");
        sb.append("externalPaymentMethodId='").append(externalPaymentMethodId).append('\'');
        sb.append(", isDefaultPaymentMethod=").append(isDefaultPaymentMethod);
        sb.append(", properties=").append(properties);
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

        final PaymentMethodPluginDetail that = (PaymentMethodPluginDetail) o;

        if (externalPaymentMethodId != null ? !externalPaymentMethodId.equals(that.externalPaymentMethodId) : that.externalPaymentMethodId != null) {
            return false;
        }
        if (isDefaultPaymentMethod != null ? !isDefaultPaymentMethod.equals(that.isDefaultPaymentMethod) : that.isDefaultPaymentMethod != null) {
            return false;
        }
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = externalPaymentMethodId != null ? externalPaymentMethodId.hashCode() : 0;
        result = 31 * result + (isDefaultPaymentMethod != null ? isDefaultPaymentMethod.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
