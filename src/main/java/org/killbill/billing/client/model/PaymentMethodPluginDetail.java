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
    private String type;
    private String ccName;
    private String ccType;
    private String ccExpirationMonth;
    private String ccExpirationYear;
    private String ccLast4;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private List<PluginProperty> properties;

    public PaymentMethodPluginDetail() {}

    @JsonCreator
    public PaymentMethodPluginDetail(@JsonProperty("externalPaymentId") final String externalPaymentId,
                                     @JsonProperty("isDefaultPaymentMethod") final Boolean isDefaultPaymentMethod,
                                     @JsonProperty("type") final String type,
                                     @JsonProperty("ccName") final String ccName,
                                     @JsonProperty("ccType") final String ccType,
                                     @JsonProperty("ccExpirationMonth") final String ccExpirationMonth,
                                     @JsonProperty("ccExpirationYear") final String ccExpirationYear,
                                     @JsonProperty("ccLast4") final String ccLast4,
                                     @JsonProperty("address1") final String address1,
                                     @JsonProperty("address2") final String address2,
                                     @JsonProperty("city") final String city,
                                     @JsonProperty("state") final String state,
                                     @JsonProperty("zip") final String zip,
                                     @JsonProperty("country") final String country,
                                     @JsonProperty("properties") final List<PluginProperty> properties) {
        this.externalPaymentId = externalPaymentId;
        this.isDefaultPaymentMethod = isDefaultPaymentMethod;
        this.type = type;
        this.ccName = ccName;
        this.ccType = ccType;
        this.ccExpirationMonth = ccExpirationMonth;
        this.ccExpirationYear = ccExpirationYear;
        this.ccLast4 = ccLast4;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(final String ccName) {
        this.ccName = ccName;
    }

    public String getCcType() {
        return ccType;
    }

    public void setCcType(final String ccType) {
        this.ccType = ccType;
    }

    public String getCcExpirationMonth() {
        return ccExpirationMonth;
    }

    public void setCcExpirationMonth(final String ccExpirationMonth) {
        this.ccExpirationMonth = ccExpirationMonth;
    }

    public String getCcExpirationYear() {
        return ccExpirationYear;
    }

    public void setCcExpirationYear(final String ccExpirationYear) {
        this.ccExpirationYear = ccExpirationYear;
    }

    public String getCcLast4() {
        return ccLast4;
    }

    public void setCcLast4(final String ccLast4) {
        this.ccLast4 = ccLast4;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
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
        sb.append(", type='").append(type).append('\'');
        sb.append(", ccName='").append(ccName).append('\'');
        sb.append(", ccType='").append(ccType).append('\'');
        sb.append(", ccExpirationMonth='").append(ccExpirationMonth).append('\'');
        sb.append(", ccExpirationYear='").append(ccExpirationYear).append('\'');
        sb.append(", ccLast4='").append(ccLast4).append('\'');
        sb.append(", address1='").append(address1).append('\'');
        sb.append(", address2='").append(address2).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", zip='").append(zip).append('\'');
        sb.append(", country='").append(country).append('\'');
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

        if (address1 != null ? !address1.equals(that.address1) : that.address1 != null) {
            return false;
        }
        if (address2 != null ? !address2.equals(that.address2) : that.address2 != null) {
            return false;
        }
        if (ccExpirationMonth != null ? !ccExpirationMonth.equals(that.ccExpirationMonth) : that.ccExpirationMonth != null) {
            return false;
        }
        if (ccExpirationYear != null ? !ccExpirationYear.equals(that.ccExpirationYear) : that.ccExpirationYear != null) {
            return false;
        }
        if (ccLast4 != null ? !ccLast4.equals(that.ccLast4) : that.ccLast4 != null) {
            return false;
        }
        if (ccName != null ? !ccName.equals(that.ccName) : that.ccName != null) {
            return false;
        }
        if (ccType != null ? !ccType.equals(that.ccType) : that.ccType != null) {
            return false;
        }
        if (city != null ? !city.equals(that.city) : that.city != null) {
            return false;
        }
        if (country != null ? !country.equals(that.country) : that.country != null) {
            return false;
        }
        if (externalPaymentId != null ? !externalPaymentId.equals(that.externalPaymentId) : that.externalPaymentId != null) {
            return false;
        }
        if (isDefaultPaymentMethod != null ? !isDefaultPaymentMethod.equals(that.isDefaultPaymentMethod) : that.isDefaultPaymentMethod != null) {
            return false;
        }
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) {
            return false;
        }
        if (state != null ? !state.equals(that.state) : that.state != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (zip != null ? !zip.equals(that.zip) : that.zip != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalPaymentId != null ? externalPaymentId.hashCode() : 0;
        result = 31 * result + (isDefaultPaymentMethod != null ? isDefaultPaymentMethod.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (ccName != null ? ccName.hashCode() : 0);
        result = 31 * result + (ccType != null ? ccType.hashCode() : 0);
        result = 31 * result + (ccExpirationMonth != null ? ccExpirationMonth.hashCode() : 0);
        result = 31 * result + (ccExpirationYear != null ? ccExpirationYear.hashCode() : 0);
        result = 31 * result + (ccLast4 != null ? ccLast4.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
