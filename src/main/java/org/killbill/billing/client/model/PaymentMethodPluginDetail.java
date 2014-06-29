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

public class PaymentMethodPluginDetail {

    private String externalPaymentId;
    private Boolean isDefaultPaymentMethod;
    private List<PluginProperty> properties;

    public PaymentMethodPluginDetail() {}

    @JsonCreator
    public PaymentMethodPluginDetail(@JsonProperty("externalPaymentId") final String externalPaymentId,
                                     @JsonProperty("isDefaultPaymentMethod") final Boolean isDefaultPaymentMethod,
                                     @JsonProperty("properties") final List<PluginProperty> properties) {
        this.externalPaymentId = externalPaymentId;
        this.isDefaultPaymentMethod = isDefaultPaymentMethod;
        this.properties = properties;
    }

    public String getExternalPaymentId() {
        return externalPaymentId;
    }

    public void setExternalPaymentId(final String externalPaymentId) {
        this.externalPaymentId = externalPaymentId;
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
        sb.append("externalPaymentId='").append(externalPaymentId).append('\'');
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

        if (externalPaymentId != null ? !externalPaymentId.equals(that.externalPaymentId) : that.externalPaymentId != null) {
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
        int result = externalPaymentId != null ? externalPaymentId.hashCode() : 0;
        result = 31 * result + (isDefaultPaymentMethod != null ? isDefaultPaymentMethod.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
