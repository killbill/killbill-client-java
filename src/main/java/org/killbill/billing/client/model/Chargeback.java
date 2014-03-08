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

public class Chargeback extends KillBillObject {

    private UUID chargebackId;
    private UUID accountId;
    private DateTime requestedDate;
    private DateTime effectiveDate;
    private BigDecimal amount;
    private UUID paymentId;
    private String currency;

    public Chargeback() {}

    @JsonCreator
    public Chargeback(@JsonProperty("chargebackId") final UUID chargebackId,
                      @JsonProperty("accountId") final UUID accountId,
                      @JsonProperty("requestedDate") final DateTime requestedDate,
                      @JsonProperty("effectiveDate") final DateTime effectiveDate,
                      @JsonProperty("amount") final BigDecimal chargebackAmount,
                      @JsonProperty("paymentId") final UUID paymentId,
                      @JsonProperty("currency") final String currency,
                      @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.chargebackId = chargebackId;
        this.accountId = accountId;
        this.requestedDate = requestedDate;
        this.effectiveDate = effectiveDate;
        this.amount = chargebackAmount;
        this.paymentId = paymentId;
        this.currency = currency;
    }

    public UUID getChargebackId() {
        return chargebackId;
    }

    public void setChargebackId(final UUID chargebackId) {
        this.chargebackId = chargebackId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Chargeback{");
        sb.append("chargebackId='").append(chargebackId).append('\'');
        sb.append(", accountId='").append(accountId).append('\'');
        sb.append(", requestedDate=").append(requestedDate);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", amount=").append(amount);
        sb.append(", paymentId='").append(paymentId).append('\'');
        sb.append(", currency='").append(currency).append('\'');
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

        final Chargeback that = (Chargeback) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null) {
            return false;
        }
        if (chargebackId != null ? !chargebackId.equals(that.chargebackId) : that.chargebackId != null) {
            return false;
        }
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(that.effectiveDate) != 0 : that.effectiveDate != null) {
            return false;
        }
        if (paymentId != null ? !paymentId.equals(that.paymentId) : that.paymentId != null) {
            return false;
        }
        if (requestedDate != null ? requestedDate.compareTo(that.requestedDate) != 0 : that.requestedDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargebackId != null ? chargebackId.hashCode() : 0;
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (paymentId != null ? paymentId.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
