package org.killbill.billing.client.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionUsage extends KillBillObject {

    private UUID subscriptionId;
    private List<UnitUsage> unitUsageRecords;
    @JsonIgnore
    private List<AuditLog> auditLogs;

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
    
    @JsonIgnore
    @Override
    public List<AuditLog> getAuditLogs() {
      return auditLogs;
    }
    
    @JsonIgnore
    @Override
    public void setAuditLogs(final List<AuditLog> auditLogs) {
      this.auditLogs = auditLogs;
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
