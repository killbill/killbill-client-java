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

package org.killbill.billing.client.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Refund extends KillBillObject {

    private UUID refundId;
    private UUID paymentId;
    private BigDecimal amount;
    private String currency;
    private Boolean isAdjusted = false; // Always populate it to avoid autoboxing NPE errors on the server
    private DateTime requestedDate;
    private DateTime effectiveDate;
    private String status;
    private List<InvoiceItem> adjustments;

    public Refund() {}

    @JsonCreator
    public Refund(@JsonProperty("refundId") final UUID refundId,
                  @JsonProperty("paymentId") final UUID paymentId,
                  @JsonProperty("amount") final BigDecimal amount,
                  @JsonProperty("currency") final String currency,
                  @JsonProperty("status") final String status,
                  @JsonProperty("adjusted") final Boolean isAdjusted,
                  @JsonProperty("requestedDate") final DateTime requestedDate,
                  @JsonProperty("effectiveDate") final DateTime effectiveDate,
                  @JsonProperty("adjustments") @Nullable final List<InvoiceItem> adjustments,
                  @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.isAdjusted = isAdjusted;
        this.requestedDate = requestedDate;
        this.effectiveDate = effectiveDate;
        this.adjustments = adjustments;
    }

    public UUID getRefundId() {
        return refundId;
    }

    public void setRefundId(final UUID refundId) {
        this.refundId = refundId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final UUID paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Boolean isAdjusted() {
        return isAdjusted;
    }

    public void setAdjusted(final Boolean isAdjusted) {
        this.isAdjusted = isAdjusted;
    }

    public DateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(final DateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final DateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<InvoiceItem> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(final List<InvoiceItem> adjustments) {
        this.adjustments = adjustments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Refund{");
        sb.append("refundId='").append(refundId).append('\'');
        sb.append(", paymentId='").append(paymentId).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", isAdjusted=").append(isAdjusted);
        sb.append(", requestedDate=").append(requestedDate);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", status='").append(status).append('\'');
        sb.append(", adjustments=").append(adjustments);
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

        final Refund refund = (Refund) o;

        if (adjustments != null ? !adjustments.equals(refund.adjustments) : refund.adjustments != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(refund.amount) != 0 : refund.amount != null) {
            return false;
        }
        if (currency != null ? !currency.equals(refund.currency) : refund.currency != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(refund.effectiveDate) != 0 : refund.effectiveDate != null) {
            return false;
        }
        if (isAdjusted != null ? !isAdjusted.equals(refund.isAdjusted) : refund.isAdjusted != null) {
            return false;
        }
        if (paymentId != null ? !paymentId.equals(refund.paymentId) : refund.paymentId != null) {
            return false;
        }
        if (refundId != null ? !refundId.equals(refund.refundId) : refund.refundId != null) {
            return false;
        }
        if (requestedDate != null ? requestedDate.compareTo(refund.requestedDate) != 0 : refund.requestedDate != null) {
            return false;
        }
        if (status != null ? !status.equals(refund.status) : refund.status != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = refundId != null ? refundId.hashCode() : 0;
        result = 31 * result + (paymentId != null ? paymentId.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (isAdjusted != null ? isAdjusted.hashCode() : 0);
        result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (adjustments != null ? adjustments.hashCode() : 0);
        return result;
    }
}
