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

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceItem extends KillBillObject {

    private UUID invoiceItemId;
    private UUID invoiceId;
    private UUID linkedInvoiceItemId;
    private UUID accountId;
    private UUID childAccountId;
    private UUID bundleId;
    private UUID subscriptionId;
    private String productName;
    private String planName;
    private String phaseName;
    private String usageName;

    private String prettyProductName;
    private String prettyPlanName;
    private String prettyPhaseName;
    private String prettyUsageName;

    private String itemType;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private String currency;
    private List<InvoiceItem> childItems;

    private BigDecimal rate;
    private Integer quantity;
    private String itemDetails;

    public InvoiceItem() {}

    @JsonCreator
    public InvoiceItem(@JsonProperty("invoiceItemId") final UUID invoiceItemId,
                       @JsonProperty("invoiceId") final UUID invoiceId,
                       @JsonProperty("linkedInvoiceItemId") final UUID linkedInvoiceItemId,
                       @JsonProperty("accountId") final UUID accountId,
                       @JsonProperty("childAccountId") final UUID childAccountId,
                       @JsonProperty("bundleId") final UUID bundleId,
                       @JsonProperty("subscriptionId") final UUID subscriptionId,
                       @JsonProperty("productName") final String productName,
                       @JsonProperty("planName") final String planName,
                       @JsonProperty("phaseName") final String phaseName,
                       @JsonProperty("usageName") final String usageName,
                       @JsonProperty("prettyProductName") final String prettyProductName,
                       @JsonProperty("prettyPlanName") final String prettyPlanName,
                       @JsonProperty("prettyPhaseName") final String prettyPhaseName,
                       @JsonProperty("prettyUsageName") final String prettyUsageName,
                       @JsonProperty("itemType") final String itemType,
                       @JsonProperty("description") final String description,
                       @JsonProperty("startDate") final LocalDate startDate,
                       @JsonProperty("endDate") final LocalDate endDate,
                       @JsonProperty("amount") final BigDecimal amount,
                       @JsonProperty("currency") final String currency,
                       @JsonProperty("childItems") final List<InvoiceItem> childItems,
                       @JsonProperty("rate") final BigDecimal rate,
                       @JsonProperty("quantity") final Integer quantity,
                       @JsonProperty("itemDetails") String itemDetails,
                       @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.invoiceItemId = invoiceItemId;
        this.invoiceId = invoiceId;
        this.linkedInvoiceItemId = linkedInvoiceItemId;
        this.accountId = accountId;
        this.childAccountId = childAccountId;
        this.bundleId = bundleId;
        this.subscriptionId = subscriptionId;
        this.productName = productName;
        this.planName = planName;
        this.phaseName = phaseName;
        this.usageName = usageName;
        this.prettyProductName = prettyProductName;
        this.prettyPlanName = prettyPlanName;
        this.prettyPhaseName = prettyPhaseName;
        this.prettyUsageName = prettyUsageName;
        this.itemType = itemType;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.currency = currency;
        this.childItems = childItems;
        this.rate = rate;
        this.quantity = quantity;
        this.itemDetails = itemDetails;
    }

    public UUID getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(final UUID invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getLinkedInvoiceItemId() {
        return linkedInvoiceItemId;
    }

    public void setLinkedInvoiceItemId(final UUID linkedInvoiceItemId) {
        this.linkedInvoiceItemId = linkedInvoiceItemId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getChildAccountId() {
        return childAccountId;
    }

    public void setChildAccountId(final UUID childAccountId) {
        this.childAccountId = childAccountId;
    }

    public UUID getBundleId() {
        return bundleId;
    }

    public void setBundleId(final UUID bundleId) {
        this.bundleId = bundleId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPlanName() {
        return planName;
    }

    public String getUsageName() {
        return usageName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public void setPlanName(final String planName) {
        this.planName = planName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(final String phaseName) {
        this.phaseName = phaseName;
    }

    public void setUsageName(final String usageName) {
        this.usageName = usageName;
    }

    public String getPrettyProductName() {
        return prettyProductName;
    }

    public void setPrettyProductName(final String prettyProductName) {
        this.prettyProductName = prettyProductName;
    }

    public String getPrettyPlanName() {
        return prettyPlanName;
    }

    public void setPrettyPlanName(final String prettyPlanName) {
        this.prettyPlanName = prettyPlanName;
    }

    public String getPrettyPhaseName() {
        return prettyPhaseName;
    }

    public void setPrettyPhaseName(final String prettyPhaseName) {
        this.prettyPhaseName = prettyPhaseName;
    }

    public String getPrettyUsageName() {
        return prettyUsageName;
    }

    public void setPrettyUsageName(final String prettyUsageName) {
        this.prettyUsageName = prettyUsageName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(final BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(final String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public List<InvoiceItem> getChildItems() {
        return childItems;
    }

    public void setChildItems(final List<InvoiceItem> childItems) {
        this.childItems = childItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final InvoiceItem that = (InvoiceItem) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (childAccountId != null ? !childAccountId.equals(that.childAccountId) : that.childAccountId != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null) {
            return false;
        }
        if (bundleId != null ? !bundleId.equals(that.bundleId) : that.bundleId != null) {
            return false;
        }
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (endDate != null ? endDate.compareTo(that.endDate) != 0 : that.endDate != null) {
            return false;
        }
        if (invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) {
            return false;
        }
        if (invoiceItemId != null ? !invoiceItemId.equals(that.invoiceItemId) : that.invoiceItemId != null) {
            return false;
        }
        if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) {
            return false;
        }
        if (linkedInvoiceItemId != null ? !linkedInvoiceItemId.equals(that.linkedInvoiceItemId) : that.linkedInvoiceItemId != null) {
            return false;
        }
        if (phaseName != null ? !phaseName.equals(that.phaseName) : that.phaseName != null) {
            return false;
        }
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) {
            return false;
        }
        if (planName != null ? !planName.equals(that.planName) : that.planName != null) {
            return false;
        }
        if (usageName != null ? !usageName.equals(that.usageName) : that.usageName != null) {
            return false;
        }
        if (startDate != null ? startDate.compareTo(that.startDate) != 0 : that.startDate != null) {
            return false;
        }
        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }
        if (rate != null ? rate.compareTo(that.rate) != 0 : that.rate != null) {
            return false;
        }
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) {
            return false;
        }
        if (itemDetails != null ? !itemDetails.equals(that.itemDetails) : that.itemDetails != null) {
            return false;
        }
        if (childItems != null ? !childItems.equals(that.childItems) : that.childItems != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceItemId != null ? invoiceItemId.hashCode() : 0;
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (linkedInvoiceItemId != null ? linkedInvoiceItemId.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (childAccountId != null ? childAccountId.hashCode() : 0);
        result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
        result = 31 * result + (subscriptionId != null ? subscriptionId.hashCode() : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (planName != null ? planName.hashCode() : 0);
        result = 31 * result + (phaseName != null ? phaseName.hashCode() : 0);
        result = 31 * result + (usageName != null ? usageName.hashCode() : 0);
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (itemDetails != null ? itemDetails.hashCode() : 0);
        result = 31 * result + (childItems != null ? childItems.hashCode() : 0);
        return result;
    }
}
