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
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventSubscription extends EventBaseSubscription {

    private UUID eventId;
    private LocalDate effectiveDate;

    @JsonCreator
    public EventSubscription(@JsonProperty("eventId") final UUID eventId,
                             @JsonProperty("billingPeriod") final String billingPeriod,
                             @JsonProperty("requestedDt") final LocalDate requestedDate,
                             @JsonProperty("effectiveDt") final LocalDate effectiveDate,
                             @JsonProperty("product") final String product,
                             @JsonProperty("priceList") final String priceList,
                             @JsonProperty("eventType") final String eventType,
                             @JsonProperty("phase") final String phase,
                             @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(billingPeriod, requestedDate, product, priceList, eventType, phase, auditLogs);
        this.eventId = eventId;
        this.effectiveDate = effectiveDate;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(final UUID eventId) {
        this.eventId = eventId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventSubscription{");
        sb.append("billingPeriod='").append(getBillingPeriod()).append('\'');
        sb.append(", requestedDate=").append(getRequestedDate());
        sb.append(", product='").append(getProduct()).append('\'');
        sb.append(", priceList='").append(getPriceList()).append('\'');
        sb.append(", eventType='").append(getEventType()).append('\'');
        sb.append(", phase='").append(getPhase()).append('\'');
        sb.append(", eventId='").append(eventId).append('\'');
        sb.append(", effectiveDate=").append(effectiveDate);
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
        if (!super.equals(o)) {
            return false;
        }

        final EventSubscription that = (EventSubscription) o;

        if (effectiveDate != null ? effectiveDate.compareTo(that.effectiveDate) != 0 : that.effectiveDate != null) {
            return false;
        }
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        return result;
    }
}
