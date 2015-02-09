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

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventSubscription extends KillBillObject {

    private String eventId;
    private String billingPeriod;
    private LocalDate requestedDate;
    private LocalDate effectiveDate;
    private String product;
    private String priceList;
    private String eventType;
    private Boolean isBlockedBilling;
    private Boolean isBlockedEntitlement;
    private String serviceName;
    private String serviceStateName;
    private String phase;

    @JsonCreator
    public EventSubscription(@JsonProperty("eventId") final String eventId,
                             @JsonProperty("billingPeriod") final String billingPeriod,
                             @JsonProperty("requestedDt") final LocalDate requestedDate,
                             @JsonProperty("effectiveDt") final LocalDate effectiveDate,
                             @JsonProperty("product") final String product,
                             @JsonProperty("priceList") final String priceList,
                             @JsonProperty("eventType") final String eventType,
                             @JsonProperty("isBlockedBilling") final Boolean isBlockedBilling,
                             @JsonProperty("isBlockedEntitlement") final Boolean isBlockedEntitlement,
                             @JsonProperty("serviceName") final String serviceName,
                             @JsonProperty("serviceStateName") final String serviceStateName,
                             @JsonProperty("phase") final String phase,
                             @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.eventId = eventId;
        this.billingPeriod = billingPeriod;
        this.requestedDate = requestedDate;
        this.effectiveDate = effectiveDate;
        this.product = product;
        this.priceList = priceList;
        this.eventType = eventType;
        this.isBlockedBilling = isBlockedBilling;
        this.isBlockedEntitlement = isBlockedEntitlement;
        this.serviceName = serviceName;
        this.serviceStateName = serviceStateName;
        this.phase = phase;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(final String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(final LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(final String product) {
        this.product = product;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(final String priceList) {
        this.priceList = priceList;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public Boolean getIsBlockedBilling() {
        return isBlockedBilling;
    }

    public void setIsBlockedBilling(final Boolean isBlockedBilling) {
        this.isBlockedBilling = isBlockedBilling;
    }

    public Boolean getIsBlockedEntitlement() {
        return isBlockedEntitlement;
    }

    public void setIsBlockedEntitlement(final Boolean isBlockedEntitlement) {
        this.isBlockedEntitlement = isBlockedEntitlement;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceStateName() {
        return serviceStateName;
    }

    public void setServiceStateName(final String serviceStateName) {
        this.serviceStateName = serviceStateName;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(final String phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventSubscriptionJson{");
        sb.append("eventId='").append(eventId).append('\'');
        sb.append(", billingPeriod='").append(billingPeriod).append('\'');
        sb.append(", requestedDate=").append(requestedDate);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", product='").append(product).append('\'');
        sb.append(", priceList='").append(priceList).append('\'');
        sb.append(", eventType='").append(eventType).append('\'');
        sb.append(", isBlockedBilling=").append(isBlockedBilling);
        sb.append(", isBlockedEntitlement=").append(isBlockedEntitlement);
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", serviceStateName='").append(serviceStateName).append('\'');
        sb.append(", phase='").append(phase).append('\'');
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

        final EventSubscription that = (EventSubscription) o;

        if (billingPeriod != null ? !billingPeriod.equals(that.billingPeriod) : that.billingPeriod != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(that.effectiveDate) != 0 : that.effectiveDate != null) {
            return false;
        }
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) {
            return false;
        }
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) {
            return false;
        }
        if (isBlockedBilling != null ? !isBlockedBilling.equals(that.isBlockedBilling) : that.isBlockedBilling != null) {
            return false;
        }
        if (isBlockedEntitlement != null ? !isBlockedEntitlement.equals(that.isBlockedEntitlement) : that.isBlockedEntitlement != null) {
            return false;
        }
        if (phase != null ? !phase.equals(that.phase) : that.phase != null) {
            return false;
        }
        if (priceList != null ? !priceList.equals(that.priceList) : that.priceList != null) {
            return false;
        }
        if (product != null ? !product.equals(that.product) : that.product != null) {
            return false;
        }
        if (requestedDate != null ? requestedDate.compareTo(that.requestedDate) != 0 : that.requestedDate != null) {
            return false;
        }
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) {
            return false;
        }
        if (serviceStateName != null ? !serviceStateName.equals(that.serviceStateName) : that.serviceStateName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (billingPeriod != null ? billingPeriod.hashCode() : 0);
        result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (priceList != null ? priceList.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (isBlockedBilling != null ? isBlockedBilling.hashCode() : 0);
        result = 31 * result + (isBlockedEntitlement != null ? isBlockedEntitlement.hashCode() : 0);
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (serviceStateName != null ? serviceStateName.hashCode() : 0);
        result = 31 * result + (phase != null ? phase.hashCode() : 0);
        return result;
    }
}
