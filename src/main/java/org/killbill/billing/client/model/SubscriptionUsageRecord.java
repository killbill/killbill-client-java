/*
 * Copyright 2014-2016 Groupon, Inc
 * Copyright 2014-2016 The Billing Project, LLC
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
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionUsageRecord {

    private UUID subscriptionId;
    private String trackingId;
    private List<UnitUsageRecord> unitUsageRecords;

    public SubscriptionUsageRecord() {}

    @JsonCreator
    public SubscriptionUsageRecord(@JsonProperty("subscriptionId") final UUID subscriptionId,
                                   @JsonProperty("trackingId") final String trackingId,
                                   @JsonProperty("unitUsageRecords") final List<UnitUsageRecord> unitUsageRecords) {
        this.subscriptionId = subscriptionId;
        this.unitUsageRecords = unitUsageRecords;
        this.trackingId = trackingId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(final String trackingId) {
        this.trackingId = trackingId;
    }

    public List<UnitUsageRecord> getUnitUsageRecords() {
        return unitUsageRecords;
    }

    public void setUnitUsageRecords(final List<UnitUsageRecord> unitUsageRecords) {
        this.unitUsageRecords = unitUsageRecords;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionUsageRecord{");
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append("trackingId=").append(trackingId);
        sb.append(", unitUsageRecords=").append(unitUsageRecords);
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

        final SubscriptionUsageRecord that = (SubscriptionUsageRecord) o;

        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }
        if (trackingId != null ? !trackingId.equals(that.trackingId) : that.trackingId != null) {
            return false;
        }
        return unitUsageRecords != null ? unitUsageRecords.equals(that.unitUsageRecords) : that.unitUsageRecords == null;

    }

    @Override
    public int hashCode() {
        int result = subscriptionId != null ? subscriptionId.hashCode() : 0;
        result = 31 * result + (trackingId != null ? trackingId.hashCode() : 0);
        result = 31 * result + (unitUsageRecords != null ? unitUsageRecords.hashCode() : 0);
        return result;
    }
}