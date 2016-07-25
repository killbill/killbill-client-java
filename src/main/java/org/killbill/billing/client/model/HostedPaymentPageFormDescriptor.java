/*
 * Copyright 2014 Groupon, Inc
 * Copyright 2014 The Billing Project, LLC
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

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HostedPaymentPageFormDescriptor extends KillBillObject {

    private UUID kbAccountId;
    private String formMethod;
    private String formUrl;
    private Map<String, Object> formFields;
    private Map<String, Object> properties;

    public HostedPaymentPageFormDescriptor() {}

    @JsonCreator
    public HostedPaymentPageFormDescriptor(@JsonProperty("kbAccountId") final UUID kbAccountId,
                                           @JsonProperty("formMethod") final String formMethod,
                                           @JsonProperty("formUrl") final String formUrl,
                                           @JsonProperty("formFields") final Map<String, Object> formFields,
                                           @JsonProperty("properties") final Map<String, Object> properties) {
        super();
        this.kbAccountId = kbAccountId;
        this.formMethod = formMethod;
        this.formUrl = formUrl;
        this.formFields = formFields;
        this.properties = properties;
    }

    public UUID getKbAccountId() {
        return kbAccountId;
    }

    public void setKbAccountId(final UUID kbAccountId) {
        this.kbAccountId = kbAccountId;
    }

    public String getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(final String formMethod) {
        this.formMethod = formMethod;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(final String formUrl) {
        this.formUrl = formUrl;
    }

    public Map<String, Object> getFormFields() {
        return formFields;
    }

    public void setFormFields(final Map<String, Object> formFields) {
        this.formFields = formFields;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HostedPaymentPageFormDescriptor{");
        sb.append("kbAccountId='").append(kbAccountId).append('\'');
        sb.append(", formMethod='").append(formMethod).append('\'');
        sb.append(", formUrl='").append(formUrl).append('\'');
        sb.append(", formFields=").append(formFields);
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

        final HostedPaymentPageFormDescriptor that = (HostedPaymentPageFormDescriptor) o;

        if (formFields != null ? !formFields.equals(that.formFields) : that.formFields != null) {
            return false;
        }
        if (formMethod != null ? !formMethod.equals(that.formMethod) : that.formMethod != null) {
            return false;
        }
        if (formUrl != null ? !formUrl.equals(that.formUrl) : that.formUrl != null) {
            return false;
        }
        if (kbAccountId != null ? !kbAccountId.equals(that.kbAccountId) : that.kbAccountId != null) {
            return false;
        }
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = kbAccountId != null ? kbAccountId.hashCode() : 0;
        result = 31 * result + (formMethod != null ? formMethod.hashCode() : 0);
        result = 31 * result + (formUrl != null ? formUrl.hashCode() : 0);
        result = 31 * result + (formFields != null ? formFields.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
