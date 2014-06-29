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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment extends KillBillObject {

    private UUID accountId;
    private UUID paymentId;
    private Integer paymentNumber;
    private String paymentExternalKey;
    private BigDecimal authAmount;
    private BigDecimal capturedAmount;
    private BigDecimal purchasedAmount;
    private BigDecimal refundedAmount;
    private BigDecimal creditedAmount;
    private String currency;
    private UUID paymentMethodId;
    private List<Transaction> transactions;

    public Payment() {}

    @JsonCreator
    public Payment(@JsonProperty("accountId") final UUID accountId,
                   @JsonProperty("paymentId") final UUID paymentId,
                   @JsonProperty("paymentNumber") final Integer paymentNumber,
                   @JsonProperty("paymentExternalKey") final String paymentExternalKey,
                   @JsonProperty("authAmount") final BigDecimal authAmount,
                   @JsonProperty("capturedAmount") final BigDecimal capturedAmount,
                   @JsonProperty("purchasedAmount") final BigDecimal purchasedAmount,
                   @JsonProperty("refundedAmount") final BigDecimal refundedAmount,
                   @JsonProperty("creditedAmount") final BigDecimal creditedAmount,
                   @JsonProperty("currency") final String currency,
                   @JsonProperty("paymentMethodId") final UUID paymentMethodId,
                   @JsonProperty("transactions") final List<Transaction> transactions,
                   @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.accountId = accountId;
        this.paymentId = paymentId;
        this.paymentNumber = paymentNumber;
        this.paymentExternalKey = paymentExternalKey;
        this.authAmount = authAmount;
        this.capturedAmount = capturedAmount;
        this.purchasedAmount = purchasedAmount;
        this.refundedAmount = refundedAmount;
        this.creditedAmount = creditedAmount;
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

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(final Integer paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentExternalKey() {
        return paymentExternalKey;
    }

    public void setPaymentExternalKey(final String paymentExternalKey) {
        this.paymentExternalKey = paymentExternalKey;
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

    public BigDecimal getPurchasedAmount() {
        return purchasedAmount;
    }

    public void setPurchasedAmount(final BigDecimal purchasedAmount) {
        this.purchasedAmount = purchasedAmount;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(final BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public BigDecimal getCreditedAmount() {
        return creditedAmount;
    }

    public void setCreditedAmount(final BigDecimal creditedAmount) {
        this.creditedAmount = creditedAmount;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }

        final Payment payment = (Payment) o;

        if (accountId != null ? !accountId.equals(payment.accountId) : payment.accountId != null) {
            return false;
        }
        if (authAmount != null ? authAmount.compareTo(payment.authAmount) != 0 : payment.authAmount != null) {
            return false;
        }
        if (capturedAmount != null ? capturedAmount.compareTo(payment.capturedAmount) != 0 : payment.capturedAmount != null) {
            return false;
        }
        if (creditedAmount != null ? creditedAmount.compareTo(payment.creditedAmount) != 0 : payment.creditedAmount != null) {
            return false;
        }
        if (currency != null ? !currency.equals(payment.currency) : payment.currency != null) {
            return false;
        }
        if (paymentExternalKey != null ? !paymentExternalKey.equals(payment.paymentExternalKey) : payment.paymentExternalKey != null) {
            return false;
        }
        if (paymentId != null ? !paymentId.equals(payment.paymentId) : payment.paymentId != null) {
            return false;
        }
        if (paymentMethodId != null ? !paymentMethodId.equals(payment.paymentMethodId) : payment.paymentMethodId != null) {
            return false;
        }
        if (paymentNumber != null ? !paymentNumber.equals(payment.paymentNumber) : payment.paymentNumber != null) {
            return false;
        }
        if (purchasedAmount != null ? purchasedAmount.compareTo(payment.purchasedAmount) != 0 : payment.purchasedAmount != null) {
            return false;
        }
        if (refundedAmount != null ? refundedAmount.compareTo(payment.refundedAmount) != 0 : payment.refundedAmount != null) {
            return false;
        }
        if (transactions != null ? !transactions.equals(payment.transactions) : payment.transactions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (paymentId != null ? paymentId.hashCode() : 0);
        result = 31 * result + (paymentNumber != null ? paymentNumber.hashCode() : 0);
        result = 31 * result + (paymentExternalKey != null ? paymentExternalKey.hashCode() : 0);
        result = 31 * result + (authAmount != null ? authAmount.hashCode() : 0);
        result = 31 * result + (capturedAmount != null ? capturedAmount.hashCode() : 0);
        result = 31 * result + (purchasedAmount != null ? purchasedAmount.hashCode() : 0);
        result = 31 * result + (refundedAmount != null ? refundedAmount.hashCode() : 0);
        result = 31 * result + (creditedAmount != null ? creditedAmount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (paymentMethodId != null ? paymentMethodId.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }
}
