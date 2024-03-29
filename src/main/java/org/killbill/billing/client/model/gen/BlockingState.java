/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2021 Equinix, Inc
 * Copyright 2014-2021 The Billing Project, LLC
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


package org.killbill.billing.client.model.gen;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.killbill.billing.client.model.gen.AuditLog;
import org.killbill.billing.entitlement.api.BlockingStateType;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
import org.killbill.billing.client.model.KillBillObject;

public class BlockingState extends KillBillObject {

    private UUID blockedId = null;

    private String stateName = null;

    private String service = null;

    private Boolean isBlockChange = null;

    private Boolean isBlockEntitlement = null;

    private Boolean isBlockBilling = null;

    private DateTime effectiveDate = null;

    private BlockingStateType type = null;



    public BlockingState() {
    }

    public BlockingState(final UUID blockedId,
                     final String stateName,
                     final String service,
                     final Boolean isBlockChange,
                     final Boolean isBlockEntitlement,
                     final Boolean isBlockBilling,
                     final DateTime effectiveDate,
                     final BlockingStateType type,
                     final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.blockedId = blockedId;
        this.stateName = stateName;
        this.service = service;
        this.isBlockChange = isBlockChange;
        this.isBlockEntitlement = isBlockEntitlement;
        this.isBlockBilling = isBlockBilling;
        this.effectiveDate = effectiveDate;
        this.type = type;

    }


    public BlockingState setBlockedId(final UUID blockedId) {
        this.blockedId = blockedId;
        return this;
    }

    public UUID getBlockedId() {
        return blockedId;
    }

    public BlockingState setStateName(final String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public BlockingState setService(final String service) {
        this.service = service;
        return this;
    }

    public String getService() {
        return service;
    }

    public BlockingState setIsBlockChange(final Boolean isBlockChange) {
        this.isBlockChange = isBlockChange;
        return this;
    }

    @JsonProperty(value="isBlockChange")
    public Boolean isBlockChange() {
        return isBlockChange;
    }

    public BlockingState setIsBlockEntitlement(final Boolean isBlockEntitlement) {
        this.isBlockEntitlement = isBlockEntitlement;
        return this;
    }

    @JsonProperty(value="isBlockEntitlement")
    public Boolean isBlockEntitlement() {
        return isBlockEntitlement;
    }

    public BlockingState setIsBlockBilling(final Boolean isBlockBilling) {
        this.isBlockBilling = isBlockBilling;
        return this;
    }

    @JsonProperty(value="isBlockBilling")
    public Boolean isBlockBilling() {
        return isBlockBilling;
    }

    public BlockingState setEffectiveDate(final DateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public BlockingState setType(final BlockingStateType type) {
        this.type = type;
        return this;
    }

    public BlockingStateType getType() {
        return type;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockingState blockingState = (BlockingState) o;
        return Objects.equals(this.blockedId, blockingState.blockedId) &&
            Objects.equals(this.stateName, blockingState.stateName) &&
            Objects.equals(this.service, blockingState.service) &&
            Objects.equals(this.isBlockChange, blockingState.isBlockChange) &&
            Objects.equals(this.isBlockEntitlement, blockingState.isBlockEntitlement) &&
            Objects.equals(this.isBlockBilling, blockingState.isBlockBilling) &&
            Objects.equals(this.effectiveDate, blockingState.effectiveDate) &&
            Objects.equals(this.type, blockingState.type) &&
            true /* ignoring this.auditLogs for identity operations */;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockedId,
            stateName,
            service,
            isBlockChange,
            isBlockEntitlement,
            isBlockBilling,
            effectiveDate,
            type,
            0 /* ignoring auditLogs for identity operations */ );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BlockingState {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    blockedId: ").append(toIndentedString(blockedId)).append("\n");
        sb.append("    stateName: ").append(toIndentedString(stateName)).append("\n");
        sb.append("    service: ").append(toIndentedString(service)).append("\n");
        sb.append("    isBlockChange: ").append(toIndentedString(isBlockChange)).append("\n");
        sb.append("    isBlockEntitlement: ").append(toIndentedString(isBlockEntitlement)).append("\n");
        sb.append("    isBlockBilling: ").append(toIndentedString(isBlockBilling)).append("\n");
        sb.append("    effectiveDate: ").append(toIndentedString(effectiveDate)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    auditLogs: ").append(toIndentedString(auditLogs)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

