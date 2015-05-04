package org.killbill.billing.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitUsage extends KillBillObject {

    private String unitType;
    private List<Usage> usage;

    @JsonCreator
    public UnitUsage(@JsonProperty("unitType") final String unitType,
                     @JsonProperty("usage") final List<Usage> usage) {
        this.unitType = unitType;
        this.usage = usage;
    }

    public String getUnitType() {
        return unitType;
    }

    public List<Usage> getDailyAmount() {
        return usage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UnitUsage{");
        sb.append("unitType=").append(unitType);
        sb.append(", usage=").append(usage);       
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((unitType == null) ? 0 : unitType.hashCode());
      result = prime * result + ((usage == null) ? 0 : usage.hashCode());
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
      if (usage == null)
      {
        if (other.usage != null)
          return false;
      }
      else if (!usage.equals(other.usage))
        return false;
      return true;
    }
    
    
}
