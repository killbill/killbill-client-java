package org.killbill.billing.client.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionUsage extends KillBillObject {

    private UUID subscriptionId;
    private List<UnitUsage> unitUsage;

    @JsonCreator
    public SubscriptionUsage(@JsonProperty("subscriptionId") final UUID subscriptionId,
                             @JsonProperty("unitUsage") final List<UnitUsage> unitUsage) {
        this.subscriptionId = subscriptionId;
        this.unitUsage = unitUsage;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public List<UnitUsage> getUnitUsage() {
        return unitUsage;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionUsage{");        
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append(", unitUsage=").append(unitUsage);       
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
      result = prime * result + ((unitUsage == null) ? 0 : unitUsage.hashCode());
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
      if (unitUsage == null)
      {
        if (other.unitUsage != null)
          return false;
      }
      else if (!unitUsage.equals(other.unitUsage))
        return false;
      return true;
    }
    
    
}
