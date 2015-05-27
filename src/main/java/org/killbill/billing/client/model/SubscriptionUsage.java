/*
 * Copyright 2015 Cloudyle GmbH
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionUsage  {

    private UUID subscriptionId;
    private List<UnitUsage> unitUsageRecords;
   
    @JsonCreator
    public SubscriptionUsage(@JsonProperty("subscriptionId") final UUID subscriptionId,
                             @JsonProperty("unitUsageRecords") final List<UnitUsage> unitUsageRecords) {
        this.subscriptionId = subscriptionId;
        this.unitUsageRecords = unitUsageRecords;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public List<UnitUsage> getUnitUsageRecords() {
        return unitUsageRecords;
    }    
   
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionUsage{");        
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append(", unitUsageRecords=").append(unitUsageRecords);       
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
      result = prime * result + ((unitUsageRecords == null) ? 0 : unitUsageRecords.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      SubscriptionUsage other = (SubscriptionUsage) obj;
      if (subscriptionId == null)
      {
        if (other.subscriptionId != null)
          return false;
      }
      else if (!subscriptionId.equals(other.subscriptionId))
        return false;
      if (unitUsageRecords == null)
      {
        if (other.unitUsageRecords != null)
          return false;
      }
      else if (!unitUsageRecords.equals(other.unitUsageRecords))
        return false;
      return true;
    }
    
    
}
