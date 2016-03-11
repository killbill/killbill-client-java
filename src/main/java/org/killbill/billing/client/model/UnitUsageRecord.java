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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitUsageRecord {

    private String unitType;
    private List<UsageRecord> usageRecords;

    public UnitUsageRecord() {}

    @JsonCreator
    public UnitUsageRecord(@JsonProperty("unitType") final String unitType,
                           @JsonProperty("usageRecords") final List<UsageRecord> usageRecords) {
        this.unitType = unitType;
        this.usageRecords = usageRecords;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(final String unitType) {
        this.unitType = unitType;
    }

    public List<UsageRecord> getUsageRecords() {
        return usageRecords;
    }

    public void setUsageRecords(final List<UsageRecord> usageRecords) {
        this.usageRecords = usageRecords;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UnitUsageRecord{");
        sb.append("unitType=").append(unitType);
        sb.append(", usageRecords=").append(usageRecords);
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

        final UnitUsageRecord that = (UnitUsageRecord) o;

        if (unitType != null ? !unitType.equals(that.unitType) : that.unitType != null) {
            return false;
        }
        return usageRecords != null ? usageRecords.equals(that.usageRecords) : that.usageRecords == null;

    }

    @Override
    public int hashCode() {
        int result = unitType != null ? unitType.hashCode() : 0;
        result = 31 * result + (usageRecords != null ? usageRecords.hashCode() : 0);
        return result;
    }
}
