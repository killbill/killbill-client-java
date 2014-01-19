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

import com.ning.billing.ObjectType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag extends KillBillObject {

    private UUID tagId;
    private ObjectType objectType;
    private UUID tagDefinitionId;
    private String tagDefinitionName;

    public Tag() { }

    @JsonCreator
    public Tag(@JsonProperty("tagId") final UUID tagId,
               @JsonProperty("objectType") final ObjectType objectType,
               @JsonProperty("tagDefinitionId") final UUID tagDefinitionId,
               @JsonProperty("tagDefinitionName") final String tagDefinitionName,
               @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.tagId = tagId;
        this.objectType = objectType;
        this.tagDefinitionId = tagDefinitionId;
        this.tagDefinitionName = tagDefinitionName;
    }

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(final UUID tagId) {
        this.tagId = tagId;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(final ObjectType objectType) {
        this.objectType = objectType;
    }

    public UUID getTagDefinitionId() {
        return tagDefinitionId;
    }

    public void setTagDefinitionId(final UUID tagDefinitionId) {
        this.tagDefinitionId = tagDefinitionId;
    }

    public String getTagDefinitionName() {
        return tagDefinitionName;
    }

    public void setTagDefinitionName(final String tagDefinitionName) {
        this.tagDefinitionName = tagDefinitionName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("tagId=").append(tagId);
        sb.append(", objectType=").append(objectType);
        sb.append(", tagDefinitionId=").append(tagDefinitionId);
        sb.append(", tagDefinitionName='").append(tagDefinitionName).append('\'');
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

        final Tag tag = (Tag) o;

        if (objectType != tag.objectType) {
            return false;
        }
        if (tagDefinitionId != null ? !tagDefinitionId.equals(tag.tagDefinitionId) : tag.tagDefinitionId != null) {
            return false;
        }
        if (tagDefinitionName != null ? !tagDefinitionName.equals(tag.tagDefinitionName) : tag.tagDefinitionName != null) {
            return false;
        }
        if (tagId != null ? !tagId.equals(tag.tagId) : tag.tagId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = tagId != null ? tagId.hashCode() : 0;
        result = 31 * result + (objectType != null ? objectType.hashCode() : 0);
        result = 31 * result + (tagDefinitionId != null ? tagDefinitionId.hashCode() : 0);
        result = 31 * result + (tagDefinitionName != null ? tagDefinitionName.hashCode() : 0);
        return result;
    }
}
