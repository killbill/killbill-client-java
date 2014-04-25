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

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DirectTransaction extends KillBillObject {

    private UUID directTransactionId;
    private UUID directPaymentId;
    private String transactionType;
    private DateTime effectiveDate;
    private Integer retryCount;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String externalKey;
    private String gatewayErrorCode;
    private String gatewayErrorMsg;

    public DirectTransaction() {
    }

    @JsonCreator
    public DirectTransaction(@JsonProperty("directTransactionId") UUID directTransactionId,
                             @JsonProperty("directPaymentId") UUID directPaymentId,
                             @JsonProperty("transactionType") String transactionType,
                             @JsonProperty("effectiveDate") DateTime effectiveDate,
                             @JsonProperty("retryCount") Integer retryCount,
                             @JsonProperty("status") String status,
                             @JsonProperty("amount") BigDecimal amount,
                             @JsonProperty("currency") String currency,
                             @JsonProperty("externalKey") String externalKey,
                             @JsonProperty("gatewayErrorCode") String gatewayErrorCode,
                             @JsonProperty("gatewayErrorMsg") String gatewayErrorMsg,
                             @JsonProperty("auditLogs") @Nullable List<AuditLog> auditLogs) {
        super(auditLogs);
        this.directTransactionId = directTransactionId;
        this.directPaymentId = directPaymentId;
        this.transactionType = transactionType;
        this.effectiveDate = effectiveDate;
        this.retryCount = retryCount;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.externalKey = externalKey;
        this.gatewayErrorCode = gatewayErrorCode;
        this.gatewayErrorMsg = gatewayErrorMsg;
    }

    public UUID getDirectTransactionId() {
        return directTransactionId;
    }

    public void setDirectTransactionId(UUID directTransactionId) {
        this.directTransactionId = directTransactionId;
    }

    public UUID getDirectPaymentId() {
        return directPaymentId;
    }

    public void setDirectPaymentId(UUID directPaymentId) {
        this.directPaymentId = directPaymentId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(DateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getGatewayErrorCode() {
        return gatewayErrorCode;
    }

    public void setGatewayErrorCode(String gatewayErrorCode) {
        this.gatewayErrorCode = gatewayErrorCode;
    }

    public String getGatewayErrorMsg() {
        return gatewayErrorMsg;
    }

    public void setGatewayErrorMsg(String gatewayErrorMsg) {
        this.gatewayErrorMsg = gatewayErrorMsg;
    }

    @Override
    public String toString() {
        return "DirectTransactionJson{" +
                "directPaymentId=" + directPaymentId +
                "directTransactionId=" + directTransactionId +
                "transactionType=" + transactionType +
                ", effectiveDate=" + effectiveDate +
                ", retryCount=" + retryCount +
                ", status='" + status + '\'' +
                ", externalKey='" + externalKey + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", gatewayErrorCode='" + gatewayErrorCode + '\'' +
                ", gatewayErrorMsg='" + gatewayErrorMsg + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectTransaction)) {
            return false;
        }

        final DirectTransaction that = (DirectTransaction) o;

        if (directPaymentId != null ? !directPaymentId.equals(that.directPaymentId) : that.directPaymentId != null) {
            return false;
        }
        if (directTransactionId != null ? !directTransactionId.equals(that.directTransactionId) : that.directTransactionId != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null) {
            return false;
        }
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(that.effectiveDate) != 0 : that.effectiveDate != null) {
            return false;
        }
        if (gatewayErrorCode != null ? !gatewayErrorCode.equals(that.gatewayErrorCode) : that.gatewayErrorCode != null) {
            return false;
        }
        if (gatewayErrorMsg != null ? !gatewayErrorMsg.equals(that.gatewayErrorMsg) : that.gatewayErrorMsg != null) {
            return false;
        }
        if (retryCount != null ? !retryCount.equals(that.retryCount) : that.retryCount != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(that.externalKey) : that.externalKey != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (transactionType.equals(that.transactionType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionType != null ? transactionType.hashCode() : 0;
        result = 31 * result + (directPaymentId != null ? directPaymentId.hashCode() : 0);
        result = 31 * result + (directTransactionId != null ? directTransactionId.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (retryCount != null ? retryCount.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (gatewayErrorCode != null ? gatewayErrorCode.hashCode() : 0);
        result = 31 * result + (gatewayErrorMsg != null ? gatewayErrorMsg.hashCode() : 0);
        return result;
    }
}
