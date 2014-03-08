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

import org.killbill.billing.clock.DefaultClock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment extends KillBillObject {

    private BigDecimal paidAmount;
    private BigDecimal amount;
    private UUID accountId;
    private UUID invoiceId;
    private UUID paymentId;
    private Integer paymentNumber;
    private DateTime requestedDate;
    private DateTime effectiveDate;
    private Integer retryCount;
    private String currency;
    private String status;
    private String gatewayErrorCode;
    private String gatewayErrorMsg;
    private UUID paymentMethodId;
    private String bundleKeys;
    private List<Refund> refunds;
    private List<Chargeback> chargebacks;

    public Payment() {}

    @JsonCreator
    public Payment(@JsonProperty("amount") final BigDecimal amount,
                   @JsonProperty("paidAmount") final BigDecimal paidAmount,
                   @JsonProperty("accountId") final UUID accountId,
                   @JsonProperty("invoiceId") final UUID invoiceId,
                   @JsonProperty("paymentId") final UUID paymentId,
                   @JsonProperty("paymentNumber") final Integer paymentNumber,
                   @JsonProperty("paymentMethodId") final UUID paymentMethodId,
                   @JsonProperty("requestedDate") final DateTime requestedDate,
                   @JsonProperty("effectiveDate") final DateTime effectiveDate,
                   @JsonProperty("retryCount") final Integer retryCount,
                   @JsonProperty("currency") final String currency,
                   @JsonProperty("status") final String status,
                   @JsonProperty("gatewayErrorCode") final String gatewayErrorCode,
                   @JsonProperty("gatewayErrorMsg") final String gatewayErrorMsg,
                   @JsonProperty("externalBundleKeys") final String bundleKeys,
                   @JsonProperty("refunds") final List<Refund> refunds,
                   @JsonProperty("chargebacks") final List<Chargeback> chargebacks,
                   @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.amount = amount;
        this.paidAmount = paidAmount;
        this.invoiceId = invoiceId;
        this.accountId = accountId;
        this.paymentId = paymentId;
        this.paymentNumber = paymentNumber;
        this.paymentMethodId = paymentMethodId;
        this.requestedDate = DefaultClock.toUTCDateTime(requestedDate);
        this.effectiveDate = DefaultClock.toUTCDateTime(effectiveDate);
        this.currency = currency;
        this.retryCount = retryCount;
        this.status = status;
        this.gatewayErrorCode = gatewayErrorCode;
        this.gatewayErrorMsg = gatewayErrorMsg;
        this.bundleKeys = bundleKeys;
        this.refunds = refunds;
        this.chargebacks = chargebacks;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(final BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final UUID invoiceId) {
        this.invoiceId = invoiceId;
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

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(final Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getGatewayErrorCode() {
        return gatewayErrorCode;
    }

    public void setGatewayErrorCode(final String gatewayErrorCode) {
        this.gatewayErrorCode = gatewayErrorCode;
    }

    public String getGatewayErrorMsg() {
        return gatewayErrorMsg;
    }

    public void setGatewayErrorMsg(final String gatewayErrorMsg) {
        this.gatewayErrorMsg = gatewayErrorMsg;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(final UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getBundleKeys() {
        return bundleKeys;
    }

    public void setBundleKeys(final String bundleKeys) {
        this.bundleKeys = bundleKeys;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(final List<Refund> refunds) {
        this.refunds = refunds;
    }

    public List<Chargeback> getChargebacks() {
        return chargebacks;
    }

    public void setChargebacks(final List<Chargeback> chargebacks) {
        this.chargebacks = chargebacks;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Payment{");
        sb.append("paidAmount=").append(paidAmount);
        sb.append(", amount=").append(amount);
        sb.append(", accountId='").append(accountId).append('\'');
        sb.append(", invoiceId='").append(invoiceId).append('\'');
        sb.append(", paymentId='").append(paymentId).append('\'');
        sb.append(", paymentNumber='").append(paymentNumber).append('\'');
        sb.append(", requestedDate=").append(requestedDate);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", retryCount=").append(retryCount);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", gatewayErrorCode='").append(gatewayErrorCode).append('\'');
        sb.append(", gatewayErrorMsg='").append(gatewayErrorMsg).append('\'');
        sb.append(", paymentMethodId='").append(paymentMethodId).append('\'');
        sb.append(", bundleKeys='").append(bundleKeys).append('\'');
        sb.append(", refunds=").append(refunds);
        sb.append(", chargebacks=").append(chargebacks);
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

        final Payment payment = (Payment) o;

        if (accountId != null ? !accountId.equals(payment.accountId) : payment.accountId != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(payment.amount) != 0 : payment.amount != null) {
            return false;
        }
        if (bundleKeys != null ? !bundleKeys.equals(payment.bundleKeys) : payment.bundleKeys != null) {
            return false;
        }
        if (chargebacks != null ? !chargebacks.equals(payment.chargebacks) : payment.chargebacks != null) {
            return false;
        }
        if (currency != null ? !currency.equals(payment.currency) : payment.currency != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(payment.effectiveDate) != 0 : payment.effectiveDate != null) {
            return false;
        }
        if (gatewayErrorCode != null ? !gatewayErrorCode.equals(payment.gatewayErrorCode) : payment.gatewayErrorCode != null) {
            return false;
        }
        if (gatewayErrorMsg != null ? !gatewayErrorMsg.equals(payment.gatewayErrorMsg) : payment.gatewayErrorMsg != null) {
            return false;
        }
        if (invoiceId != null ? !invoiceId.equals(payment.invoiceId) : payment.invoiceId != null) {
            return false;
        }
        if (paidAmount != null ? paidAmount.compareTo(payment.paidAmount) != 0 : payment.paidAmount != null) {
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
        if (refunds != null ? !refunds.equals(payment.refunds) : payment.refunds != null) {
            return false;
        }
        if (requestedDate != null ? requestedDate.compareTo(payment.requestedDate) != 0 : payment.requestedDate != null) {
            return false;
        }
        if (retryCount != null ? !retryCount.equals(payment.retryCount) : payment.retryCount != null) {
            return false;
        }
        if (status != null ? !status.equals(payment.status) : payment.status != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = paidAmount != null ? paidAmount.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (paymentId != null ? paymentId.hashCode() : 0);
        result = 31 * result + (paymentNumber != null ? paymentNumber.hashCode() : 0);
        result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (retryCount != null ? retryCount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (gatewayErrorCode != null ? gatewayErrorCode.hashCode() : 0);
        result = 31 * result + (gatewayErrorMsg != null ? gatewayErrorMsg.hashCode() : 0);
        result = 31 * result + (paymentMethodId != null ? paymentMethodId.hashCode() : 0);
        result = 31 * result + (bundleKeys != null ? bundleKeys.hashCode() : 0);
        result = 31 * result + (refunds != null ? refunds.hashCode() : 0);
        result = 31 * result + (chargebacks != null ? chargebacks.hashCode() : 0);
        return result;
    }
}
