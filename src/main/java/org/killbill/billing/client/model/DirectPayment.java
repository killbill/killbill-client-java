/*
 * Copyright 2014 Groupon, Inc
 *
 * Groupon licenses this file to you under the Apache License, version 2.0
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DirectPayment extends KillBillObject {

    private UUID accountId;
    private UUID directPaymentId;
    private Integer paymentNumber;
    private String directPaymentExternalKey;
    private BigDecimal authAmount;
    private BigDecimal capturedAmount;
    private BigDecimal refundedAmount;
    private String currency;
    private UUID paymentMethodId;
    private List<DirectTransaction> transactions;

    public DirectPayment() {
    }

    @JsonCreator
    public DirectPayment(@JsonProperty("accountId") final UUID accountId,
                         @JsonProperty("directPaymentId") final UUID directPaymentId,
                         @JsonProperty("paymentNumber") final Integer paymentNumber,
                         @JsonProperty("directPaymentExternalKey") final String directPaymentExternalKey,
                         @JsonProperty("authAmount") final BigDecimal authAmount,
                         @JsonProperty("capturedAmount") final BigDecimal capturedAmount,
                         @JsonProperty("refundedAmount") final BigDecimal refundedAmount,
                         @JsonProperty("currency") final String currency,
                         @JsonProperty("paymentMethodId") final UUID paymentMethodId,
                         @JsonProperty("transactions") final List<DirectTransaction> transactions,
                         @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.accountId = accountId;
        this.directPaymentId = directPaymentId;
        this.paymentNumber = paymentNumber;
        this.directPaymentExternalKey = directPaymentExternalKey;
        this.authAmount = authAmount;
        this.capturedAmount = capturedAmount;
        this.refundedAmount = refundedAmount;
        this.currency = currency;
        this.paymentMethodId = paymentMethodId;
        this.transactions = transactions;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getDirectPaymentId() {
        return directPaymentId;
    }

    public void setDirectPaymentId(final UUID directPaymentId) {
        this.directPaymentId = directPaymentId;
    }

    public Integer getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(final Integer paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getDirectPaymentExternalKey() {
        return directPaymentExternalKey;
    }

    public void setDirectPaymentExternalKey(final String directPaymentExternalKey) {
        this.directPaymentExternalKey = directPaymentExternalKey;
    }

    public BigDecimal getAuthAmount() {
        return authAmount;
    }

    public void setAuthAmount(final BigDecimal authAmount) {
        this.authAmount = authAmount;
    }

    public BigDecimal getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(final BigDecimal capturedAmount) {
        this.capturedAmount = capturedAmount;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(final BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(final UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public List<DirectTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<DirectTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DirectPayment{");
        sb.append("accountId=").append(accountId);
        sb.append(", directPaymentId=").append(directPaymentId);
        sb.append(", paymentNumber=").append(paymentNumber);
        sb.append(", directPaymentExternalKey='").append(directPaymentExternalKey).append('\'');
        sb.append(", authAmount=").append(authAmount);
        sb.append(", capturedAmount=").append(capturedAmount);
        sb.append(", refundedAmount=").append(refundedAmount);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", paymentMethodId=").append(paymentMethodId);
        sb.append(", transactions=").append(transactions);
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

        final DirectPayment that = (DirectPayment) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (authAmount != null ? authAmount.compareTo(that.authAmount) != 0 : that.authAmount != null) {
            return false;
        }
        if (capturedAmount != null ? capturedAmount.compareTo(that.capturedAmount) != 0 : that.capturedAmount != null) {
            return false;
        }
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) {
            return false;
        }
        if (directPaymentExternalKey != null ? !directPaymentExternalKey.equals(that.directPaymentExternalKey) : that.directPaymentExternalKey != null) {
            return false;
        }
        if (directPaymentId != null ? !directPaymentId.equals(that.directPaymentId) : that.directPaymentId != null) {
            return false;
        }
        if (paymentMethodId != null ? !paymentMethodId.equals(that.paymentMethodId) : that.paymentMethodId != null) {
            return false;
        }
        if (paymentNumber != null ? !paymentNumber.equals(that.paymentNumber) : that.paymentNumber != null) {
            return false;
        }
        if (refundedAmount != null ? refundedAmount.compareTo(that.refundedAmount) != 0 : that.refundedAmount != null) {
            return false;
        }
        if (transactions != null ? !transactions.equals(that.transactions) : that.transactions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (directPaymentId != null ? directPaymentId.hashCode() : 0);
        result = 31 * result + (paymentNumber != null ? paymentNumber.hashCode() : 0);
        result = 31 * result + (directPaymentExternalKey != null ? directPaymentExternalKey.hashCode() : 0);
        result = 31 * result + (authAmount != null ? authAmount.hashCode() : 0);
        result = 31 * result + (capturedAmount != null ? capturedAmount.hashCode() : 0);
        result = 31 * result + (refundedAmount != null ? refundedAmount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (paymentMethodId != null ? paymentMethodId.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }
}
