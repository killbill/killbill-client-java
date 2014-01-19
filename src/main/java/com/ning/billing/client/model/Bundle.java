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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bundle extends KillBillObject {

    private UUID accountId;
    private UUID bundleId;
    private String externalKey;
    private List<Subscription> subscriptions;

    @JsonCreator
    public Bundle(@JsonProperty("accountId") @Nullable final UUID accountId,
                  @JsonProperty("bundleId") @Nullable final UUID bundleId,
                  @JsonProperty("externalKey") @Nullable final String externalKey,
                  @JsonProperty("subscriptions") @Nullable final List<Subscription> subscriptions,
                  @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.accountId = accountId;
        this.bundleId = bundleId;
        this.externalKey = externalKey;
        this.subscriptions = subscriptions;
    }

    public Bundle() {}

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getBundleId() {
        return bundleId;
    }

    public void setBundleId(final UUID bundleId) {
        this.bundleId = bundleId;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(final List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bundle{");
        sb.append("accountId='").append(accountId).append('\'');
        sb.append(", bundleId='").append(bundleId).append('\'');
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", subscriptions=").append(subscriptions);
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

        final Bundle bundle = (Bundle) o;

        if (accountId != null ? !accountId.equals(bundle.accountId) : bundle.accountId != null) {
            return false;
        }
        if (bundleId != null ? !bundleId.equals(bundle.bundleId) : bundle.bundleId != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(bundle.externalKey) : bundle.externalKey != null) {
            return false;
        }
        if (subscriptions != null ? !subscriptions.equals(bundle.subscriptions) : bundle.subscriptions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (subscriptions != null ? subscriptions.hashCode() : 0);
        return result;
    }
}
