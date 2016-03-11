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

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RolledUpUsage {

    private UUID subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RolledUpUnit> rolledUpUnits;

    public RolledUpUsage() {}

    @JsonCreator
    public RolledUpUsage(@JsonProperty("subscriptionId") final UUID subscriptionId,
                         @JsonProperty("startDate") final LocalDate startDate,
                         @JsonProperty("endDate") final LocalDate endDate,
                         @JsonProperty("rolledUpUnits") final List<RolledUpUnit> rolledUpUnits) {
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rolledUpUnits = rolledUpUnits;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<RolledUpUnit> getRolledUpUnits() {
        return rolledUpUnits;
    }

    public void setRolledUpUnits(final List<RolledUpUnit> rolledUpUnits) {
        this.rolledUpUnits = rolledUpUnits;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RolledUpUsage{");
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", rolledUpUnits=").append(rolledUpUnits);
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

        final RolledUpUsage that = (RolledUpUsage) o;

        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) {
            return false;
        }
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) {
            return false;
        }
        return rolledUpUnits != null ? rolledUpUnits.equals(that.rolledUpUnits) : that.rolledUpUnits == null;

    }

    @Override
    public int hashCode() {
        int result = subscriptionId != null ? subscriptionId.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (rolledUpUnits != null ? rolledUpUnits.hashCode() : 0);
        return result;
    }
}
