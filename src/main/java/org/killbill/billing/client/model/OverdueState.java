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

public class OverdueState extends KillBillObject {

    private String name;
    private String externalMessage;
    private List<Integer> daysBetweenPaymentRetries;
    private Boolean disableEntitlementAndChangesBlocked;
    private Boolean blockChanges;
    private Boolean isClearState;
    private Integer reevaluationIntervalDays;

    @JsonCreator
    public OverdueState(@JsonProperty("name") final String name,
                        @JsonProperty("externalMessage") final String externalMessage,
                        @JsonProperty("daysBetweenPaymentRetries") final List<Integer> daysBetweenPaymentRetries,
                        @JsonProperty("disableEntitlementAndChangesBlocked") final Boolean disableEntitlementAndChangesBlocked,
                        @JsonProperty("blockChanges") final Boolean blockChanges,
                        @JsonProperty("clearState") final Boolean isClearState,
                        @JsonProperty("reevaluationIntervalDays") final Integer reevaluationIntervalDays) {
        this.name = name;
        this.externalMessage = externalMessage;
        this.daysBetweenPaymentRetries = daysBetweenPaymentRetries;
        this.disableEntitlementAndChangesBlocked = disableEntitlementAndChangesBlocked;
        this.blockChanges = blockChanges;
        this.isClearState = isClearState;
        this.reevaluationIntervalDays = reevaluationIntervalDays;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getExternalMessage() {
        return externalMessage;
    }

    public void setExternalMessage(final String externalMessage) {
        this.externalMessage = externalMessage;
    }

    public List<Integer> getDaysBetweenPaymentRetries() {
        return daysBetweenPaymentRetries;
    }

    public void setDaysBetweenPaymentRetries(final List<Integer> daysBetweenPaymentRetries) {
        this.daysBetweenPaymentRetries = daysBetweenPaymentRetries;
    }

    public Boolean getDisableEntitlementAndChangesBlocked() {
        return disableEntitlementAndChangesBlocked;
    }

    public void setDisableEntitlementAndChangesBlocked(final Boolean disableEntitlementAndChangesBlocked) {
        this.disableEntitlementAndChangesBlocked = disableEntitlementAndChangesBlocked;
    }

    public Boolean getBlockChanges() {
        return blockChanges;
    }

    public void setBlockChanges(final Boolean blockChanges) {
        this.blockChanges = blockChanges;
    }

    public Boolean getIsClearState() {
        return isClearState;
    }

    public void setIsClearState(final Boolean isClearState) {
        this.isClearState = isClearState;
    }

    public Integer getReevaluationIntervalDays() {
        return reevaluationIntervalDays;
    }

    public void setReevaluationIntervalDays(final Integer reevaluationIntervalDays) {
        this.reevaluationIntervalDays = reevaluationIntervalDays;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OverdueState{");
        sb.append("name='").append(name).append('\'');
        sb.append(", externalMessage='").append(externalMessage).append('\'');
        sb.append(", daysBetweenPaymentRetries=").append(daysBetweenPaymentRetries);
        sb.append(", disableEntitlementAndChangesBlocked=").append(disableEntitlementAndChangesBlocked);
        sb.append(", blockChanges=").append(blockChanges);
        sb.append(", isClearState=").append(isClearState);
        sb.append(", reevaluationIntervalDays=").append(reevaluationIntervalDays);
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

        final OverdueState that = (OverdueState) o;

        if (blockChanges != null ? !blockChanges.equals(that.blockChanges) : that.blockChanges != null) {
            return false;
        }
        if (daysBetweenPaymentRetries != null ? !daysBetweenPaymentRetries.equals(that.daysBetweenPaymentRetries) : that.daysBetweenPaymentRetries != null) {
            return false;
        }
        if (disableEntitlementAndChangesBlocked != null ? !disableEntitlementAndChangesBlocked.equals(that.disableEntitlementAndChangesBlocked) : that.disableEntitlementAndChangesBlocked != null) {
            return false;
        }
        if (externalMessage != null ? !externalMessage.equals(that.externalMessage) : that.externalMessage != null) {
            return false;
        }
        if (isClearState != null ? !isClearState.equals(that.isClearState) : that.isClearState != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (reevaluationIntervalDays != null ? !reevaluationIntervalDays.equals(that.reevaluationIntervalDays) : that.reevaluationIntervalDays != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (externalMessage != null ? externalMessage.hashCode() : 0);
        result = 31 * result + (daysBetweenPaymentRetries != null ? daysBetweenPaymentRetries.hashCode() : 0);
        result = 31 * result + (disableEntitlementAndChangesBlocked != null ? disableEntitlementAndChangesBlocked.hashCode() : 0);
        result = 31 * result + (blockChanges != null ? blockChanges.hashCode() : 0);
        result = 31 * result + (isClearState != null ? isClearState.hashCode() : 0);
        result = 31 * result + (reevaluationIntervalDays != null ? reevaluationIntervalDays.hashCode() : 0);
        return result;
    }
}
