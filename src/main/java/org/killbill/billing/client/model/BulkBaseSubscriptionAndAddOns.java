/*
 * Copyright 2016 The Billing Project, LLC
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

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkBaseSubscriptionAndAddOns {

    private final List<Subscription> baseEntitlementAndAddOns;

    @JsonCreator
    public BulkBaseSubscriptionAndAddOns(
            @JsonProperty("baseEntitlementAndAddOns") @Nullable final List<Subscription> baseEntitlementAndAddOns) {
        this.baseEntitlementAndAddOns = baseEntitlementAndAddOns;
    }

    public List<Subscription> getBaseEntitlementAndAddOns() {
        return baseEntitlementAndAddOns;
    }

    @Override
    public String toString() {
        return "BulkBaseSubscriptionAndAddOns{" +
               "baseEntitlementAndAddOns=" + baseEntitlementAndAddOns +
               '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BulkBaseSubscriptionAndAddOns that = (BulkBaseSubscriptionAndAddOns) o;

        return baseEntitlementAndAddOns != null ? baseEntitlementAndAddOns.equals(that.baseEntitlementAndAddOns) : that.baseEntitlementAndAddOns == null;

    }

    @Override
    public int hashCode() {
        return baseEntitlementAndAddOns != null ? baseEntitlementAndAddOns.hashCode() : 0;
    }

}
