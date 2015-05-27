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

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Usage {

    private LocalDate recordDate;
    private Long amount;
   
    @JsonCreator
    public Usage(@JsonProperty("recordDate") final LocalDate recordDate,
                 @JsonProperty("amount") final Long amount) {
        this.recordDate = recordDate;
        this.amount = amount;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public Long getAmount() {
        return amount;
    }
       
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Usage{");
        sb.append("recordDate=").append(recordDate);
        sb.append(", amount=").append(amount);       
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((amount == null) ? 0 : amount.hashCode());
      result = prime * result + ((recordDate == null) ? 0 : recordDate.hashCode());
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
      Usage other = (Usage) obj;
      if (amount == null)
      {
        if (other.amount != null)
          return false;
      }
      else if (!amount.equals(other.amount))
        return false;
      if (recordDate == null)
      {
        if (other.recordDate != null)
          return false;
      }
      else if (!recordDate.equals(other.recordDate))
        return false;
      return true;
    }
    
    
}
