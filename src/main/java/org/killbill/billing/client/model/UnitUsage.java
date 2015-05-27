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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitUsage {

    private String unitType;
    private List<Usage> usageRecords;
   
    @JsonCreator
    public UnitUsage(@JsonProperty("unitType") final String unitType,
                     @JsonProperty("usageRecords") final List<Usage> usageRecords) {
        this.unitType = unitType;
        this.usageRecords = usageRecords;
    }

    public String getUnitType() {
        return unitType;
    }

    public List<Usage> getUsageRecords() {
        return usageRecords;
    }
       
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UnitUsage{");
        sb.append("unitType=").append(unitType);
        sb.append(", usageRecords=").append(usageRecords);       
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((unitType == null) ? 0 : unitType.hashCode());
      result = prime * result + ((usageRecords == null) ? 0 : usageRecords.hashCode());
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
      UnitUsage other = (UnitUsage) obj;
      if (unitType == null)
      {
        if (other.unitType != null)
          return false;
      }
      else if (!unitType.equals(other.unitType))
        return false;
      if (usageRecords == null)
      {
        if (other.usageRecords != null)
          return false;
      }
      else if (!usageRecords.equals(other.usageRecords))
        return false;
      return true;
    }
    
    
}
