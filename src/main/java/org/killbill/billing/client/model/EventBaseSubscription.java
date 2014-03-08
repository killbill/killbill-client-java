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

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class EventBaseSubscription extends KillBillObject {

    private String billingPeriod;
    private LocalDate requestedDate;
    private String product;
    private String priceList;
    private String eventType;
    private String phase;

    @JsonCreator
    public EventBaseSubscription(@JsonProperty("billingPeriod") final String billingPeriod,
                                 @JsonProperty("requestedDate") final LocalDate requestedDate,
                                 @JsonProperty("product") final String product,
                                 @JsonProperty("priceList") final String priceList,
                                 @JsonProperty("eventType") final String eventType,
                                 @JsonProperty("phase") final String phase,
                                 @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.billingPeriod = billingPeriod;
        this.requestedDate = requestedDate;
        this.product = product;
        this.priceList = priceList;
        this.eventType = eventType;
        this.phase = phase;
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

    public String getPhase() {
        return phase;
    }

    public void setPhase(final String phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventBaseSubscription{");
        sb.append("billingPeriod='").append(billingPeriod).append('\'');
        sb.append(", requestedDate=").append(requestedDate);
        sb.append(", product='").append(product).append('\'');
        sb.append(", priceList='").append(priceList).append('\'');
        sb.append(", eventType='").append(eventType).append('\'');
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

        final EventBaseSubscription that = (EventBaseSubscription) o;

        if (billingPeriod != null ? !billingPeriod.equals(that.billingPeriod) : that.billingPeriod != null) {
            return false;
        }
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) {
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

        return true;
    }

    @Override
    public int hashCode() {
        int result = billingPeriod != null ? billingPeriod.hashCode() : 0;
        result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (priceList != null ? priceList.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (phase != null ? phase.hashCode() : 0);
        return result;
    }
}
