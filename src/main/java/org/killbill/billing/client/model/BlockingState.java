/*
 * Copyright 2010-2013 Ning, Inc.
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
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;
import org.killbill.billing.entitlement.api.BlockingStateType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockingState extends KillBillObject {


    private UUID blockedId;
    private String stateName;
    private String service;
    private Boolean blockChange;
    private Boolean blockEntitlement;
    private Boolean blockBilling;
    private LocalDate effectiveDate;
    private BlockingStateType type;


    @JsonCreator
    public BlockingState(@JsonProperty("blockedId") final UUID blockedId,
                         @JsonProperty("stateName") final String stateName,
                         @JsonProperty("service") final String service,
                         @JsonProperty("blockChange") final Boolean blockChange,
                         @JsonProperty("blockEntitlement") final Boolean blockEntitlement,
                         @JsonProperty("blockBilling") final Boolean blockBilling,
                         @JsonProperty("effectiveDate") final LocalDate effectiveDate,
                         @JsonProperty("type") final BlockingStateType type,
                         @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.blockedId = blockedId;
        this.stateName = stateName;
        this.service = service;
        this.blockChange = blockChange;
        this.blockEntitlement = blockEntitlement;
        this.blockBilling = blockBilling;
        this.effectiveDate = effectiveDate;
        this.type = type;
    }

    public UUID getBlockedId() {
        return blockedId;
    }

    public String getStateName() {
        return stateName;
    }

    public String getService() {
        return service;
    }

    public Boolean getBlockChange() {
        return blockChange;
    }

    public Boolean getBlockEntitlement() {
        return blockEntitlement;
    }

    public Boolean getBlockBilling() {
        return blockBilling;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public BlockingStateType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockingState)) {
            return false;
        }

        final BlockingState that = (BlockingState) o;

        if (blockedId != null ? !blockedId.equals(that.blockedId) : that.blockedId != null) {
            return false;
        }
        if (stateName != null ? !stateName.equals(that.stateName) : that.stateName != null) {
            return false;
        }
        if (service != null ? !service.equals(that.service) : that.service != null) {
            return false;
        }
        if (blockChange != null ? !blockChange.equals(that.blockChange) : that.blockChange != null) {
            return false;
        }
        if (blockEntitlement != null ? !blockEntitlement.equals(that.blockEntitlement) : that.blockEntitlement != null) {
            return false;
        }
        if (blockBilling != null ? !blockBilling.equals(that.blockBilling) : that.blockBilling != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(that.effectiveDate) != 0 : that.effectiveDate != null) {
            return false;
        }
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = blockedId != null ? blockedId.hashCode() : 0;
        result = 31 * result + (stateName != null ? stateName.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        result = 31 * result + (blockChange != null ? blockChange.hashCode() : 0);
        result = 31 * result + (blockEntitlement != null ? blockEntitlement.hashCode() : 0);
        result = 31 * result + (blockBilling != null ? blockBilling.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BlockingState{" +
               "blockedId=" + blockedId +
               ", stateName='" + stateName + '\'' +
               ", service='" + service + '\'' +
               ", blockChange=" + blockChange +
               ", blockEntitlement=" + blockEntitlement +
               ", blockBilling=" + blockBilling +
               ", effectiveDate=" + effectiveDate +
               ", type=" + type +
               '}';
    }
}
