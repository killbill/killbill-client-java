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
import java.util.UUID;

import javax.annotation.Nullable;

import org.killbill.billing.ObjectType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TagDefinition extends KillBillObject {

    private UUID id;
    private Boolean isControlTag;
    private String name;
    private String description;
    private List<ObjectType> applicableObjectTypes;

    public TagDefinition() { }

    @JsonCreator
    public TagDefinition(@JsonProperty("id") final UUID id,
                         @JsonProperty("isControlTag") final Boolean isControlTag,
                         @JsonProperty("name") final String name,
                         @JsonProperty("description") @Nullable final String description,
                         @JsonProperty("applicableObjectTypes") @Nullable final List<ObjectType> applicableObjectTypes) {
        this.id = id;
        this.isControlTag = isControlTag;
        this.name = name;
        this.description = description;
        this.applicableObjectTypes = applicableObjectTypes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Boolean getIsControlTag() {
        return isControlTag;
    }

    public void setIsControlTag(final Boolean isControlTag) {
        this.isControlTag = isControlTag;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ObjectType> getApplicableObjectTypes() {
        return applicableObjectTypes;
    }

    public void setApplicableObjectTypes(final List<ObjectType> applicableObjectTypes) {
        this.applicableObjectTypes = applicableObjectTypes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TagDefinition{");
        sb.append("id=").append(id);
        sb.append(", isControlTag=").append(isControlTag);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", applicableObjectTypes=").append(applicableObjectTypes);
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

        final TagDefinition that = (TagDefinition) o;

        if (applicableObjectTypes != null ? !applicableObjectTypes.equals(that.applicableObjectTypes) : that.applicableObjectTypes != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (isControlTag != null ? !isControlTag.equals(that.isControlTag) : that.isControlTag != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (isControlTag != null ? isControlTag.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (applicableObjectTypes != null ? applicableObjectTypes.hashCode() : 0);
        return result;
    }
}
