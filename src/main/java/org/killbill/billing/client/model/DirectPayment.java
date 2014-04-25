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
    private BigDecimal authAmount;
    private BigDecimal capturedAmount;
    private BigDecimal refundedAmount;
    private String currency;
    private UUID paymentMethodId;
    private List<DirectTransaction> transactions;

    public DirectPayment() {
    }

    @JsonCreator
    public DirectPayment(@JsonProperty("accountId") UUID accountId,
                         @JsonProperty("directPaymentId") UUID directPaymentId,
                         @JsonProperty("paymentNumber") Integer paymentNumber,
                         @JsonProperty("authAmount") BigDecimal authAmount,
                         @JsonProperty("capturedAmount") BigDecimal capturedAmount,
                         @JsonProperty("refundedAmount") BigDecimal refundedAmount,
                         @JsonProperty("currency") String currency,
                         @JsonProperty("paymentMethodId") UUID paymentMethodId,
                         @JsonProperty("transactions") List<DirectTransaction> transactions,
                         @JsonProperty("auditLogs") @Nullable List<AuditLog> auditLogs) {
        super(auditLogs);
        this.accountId = accountId;
        this.directPaymentId = directPaymentId;
        this.paymentNumber = paymentNumber;
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

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getDirectPaymentId() {
        return directPaymentId;
    }

    public void setDirectPaymentId(UUID directPaymentId) {
        this.directPaymentId = directPaymentId;
    }

    public Integer getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Integer paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public BigDecimal getAuthAmount() {
        return authAmount;
    }

    public void setAuthAmount(BigDecimal authAmount) {
        this.authAmount = authAmount;
    }

    public BigDecimal getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(BigDecimal capturedAmount) {
        this.capturedAmount = capturedAmount;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public List<DirectTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<DirectTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "DirectPaymentJson{" +
                "accountId='" + accountId + '\'' +
                ", directPaymentId='" + directPaymentId + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", authAmount=" + authAmount +
                ", capturedAmount=" + capturedAmount +
                ", refundedAmount=" + refundedAmount +
                ", currency='" + currency + '\'' +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", transactions=" + transactions +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectPayment)) {
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
        result = 31 * result + (authAmount != null ? authAmount.hashCode() : 0);
        result = 31 * result + (capturedAmount != null ? capturedAmount.hashCode() : 0);
        result = 31 * result + (refundedAmount != null ? refundedAmount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (paymentMethodId != null ? paymentMethodId.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }
}
