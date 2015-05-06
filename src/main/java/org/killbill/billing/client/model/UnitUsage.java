package org.killbill.billing.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitUsage extends KillBillObject {

    private String unitType;
    private List<Usage> usageRecords;
    @JsonIgnore
    private List<AuditLog> auditLogs;

    @JsonCreator
    public UnitUsage(@JsonProperty("unitType") final String unitType,
                     @JsonProperty("usageRecords") final List<Usage> usageRecords) {
        this.unitType = unitType;
        this.usageRecords = usageRecords;
    }

    public String getUnitType() {
        return unitType;
    }

    public List<Usage> getDailyAmount() {
        return usageRecords;
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
        final StringBuilder sb = new StringBuilder("UnitUsage{");
        sb.append("unitType=").append(unitType);
        sb.append(", usage=").append(usageRecords);       
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
