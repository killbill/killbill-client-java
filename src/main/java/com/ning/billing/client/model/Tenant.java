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

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tenant extends KillBillObject {

    private UUID tenantId;
    private String externalKey;
    private String apiKey;
    private String apiSecret;

    public Tenant() {}

    @JsonCreator
    public Tenant(@JsonProperty("tenantId") final UUID tenantId,
                  @JsonProperty("externalKey") final String externalKey,
                  @JsonProperty("apiKey") final String apiKey,
                  @JsonProperty("apiSecret") final String apiSecret) {
        this.tenantId = tenantId;
        this.externalKey = externalKey;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(final String apiSecret) {
        this.apiSecret = apiSecret;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tenant{");
        sb.append("tenantId='").append(tenantId).append('\'');
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", apiKey='").append(apiKey).append('\'');
        sb.append(", apiSecret='").append(apiSecret).append('\'');
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

        final Tenant tenant = (Tenant) o;

        if (apiKey != null ? !apiKey.equals(tenant.apiKey) : tenant.apiKey != null) {
            return false;
        }
        if (apiSecret != null ? !apiSecret.equals(tenant.apiSecret) : tenant.apiSecret != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(tenant.externalKey) : tenant.externalKey != null) {
            return false;
        }
        if (tenantId != null ? !tenantId.equals(tenant.tenantId) : tenant.tenantId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = tenantId != null ? tenantId.hashCode() : 0;
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
        result = 31 * result + (apiSecret != null ? apiSecret.hashCode() : 0);
        return result;
    }
}
