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

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RolledUpUsage  {

    private UUID subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RolledUpUnit> rolledUpUnits;
   
    @JsonCreator
    public RolledUpUsage(@JsonProperty("subscriptionId") final UUID subscriptionId,
                         @JsonProperty("startDate") final LocalDate startDate,
                         @JsonProperty("endDate") final LocalDate endDate,
                         @JsonProperty("rolledUpUnits") final List<RolledUpUnit> rolledUpUnits){
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rolledUpUnits = rolledUpUnits;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public LocalDate getStartDate() {
      return startDate;
    }
    
    public LocalDate getEndDate() {
      return endDate;
    }
    
    public List<RolledUpUnit> getRolledUpUnits() {
        return rolledUpUnits;
    }    
   
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RolledUpUsage{");        
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append(", startDate=").append(startDate);       
        sb.append(", endDate=").append(endDate);
        sb.append(", rolledUpUnits=").append(rolledUpUnits);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
      result = prime * result + ((rolledUpUnits == null) ? 0 : rolledUpUnits.hashCode());
      result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
      result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
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
      RolledUpUsage other = (RolledUpUsage) obj;
      if (endDate == null)
      {
        if (other.endDate != null)
          return false;
      }
      else if (!endDate.equals(other.endDate))
        return false;
      if (rolledUpUnits == null)
      {
        if (other.rolledUpUnits != null)
          return false;
      }
      else if (!rolledUpUnits.equals(other.rolledUpUnits))
        return false;
      if (startDate == null)
      {
        if (other.startDate != null)
          return false;
      }
      else if (!startDate.equals(other.startDate))
        return false;
      if (subscriptionId == null)
      {
        if (other.subscriptionId != null)
          return false;
      }
      else if (!subscriptionId.equals(other.subscriptionId))
        return false;
      return true;
    }

    
    
    
}
