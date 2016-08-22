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
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account extends KillBillObject {

    private UUID accountId;
    private String externalKey;
    private BigDecimal accountCBA;
    private BigDecimal accountBalance;
    private String name;
    private Integer firstNameLength;
    private String email;
    private Integer billCycleDayLocal;
    private String currency;
    private UUID parentAccountId;
    private Boolean isPaymentDelegatedToParent;
    private UUID paymentMethodId;
    private String timeZone;
    private String address1;
    private String address2;
    private String postalCode;
    private String company;
    private String city;
    private String state;
    private String country;
    private String locale;
    private String phone;
    private String notes;
    private Boolean isMigrated;
    private Boolean isNotifiedForInvoices;

    public Account() { }

    @JsonCreator
    public Account(@JsonProperty("accountId") final UUID accountId,
                   @JsonProperty("name") final String name,
                   @JsonProperty("firstNameLength") final Integer firstNameLength,
                   @JsonProperty("externalKey") final String externalKey,
                   @JsonProperty("email") final String email,
                   @JsonProperty("billCycleDayLocal") final Integer billCycleDayLocal,
                   @JsonProperty("currency") final String currency,
                   @JsonProperty("parentAccountId") final UUID parentAccountId,
                   @JsonProperty("isPaymentDelegatedToParent") final Boolean isPaymentDelegatedToParent,
                   @JsonProperty("paymentMethodId") final UUID paymentMethodId,
                   @JsonProperty("timeZone") final String timeZone,
                   @JsonProperty("address1") final String address1,
                   @JsonProperty("address2") final String address2,
                   @JsonProperty("postalCode") final String postalCode,
                   @JsonProperty("company") final String company,
                   @JsonProperty("city") final String city,
                   @JsonProperty("state") final String state,
                   @JsonProperty("country") final String country,
                   @JsonProperty("locale") final String locale,
                   @JsonProperty("phone") final String phone,
                   @JsonProperty("notes") final String notes,
                   @JsonProperty("isMigrated") final Boolean isMigrated,
                   @JsonProperty("isNotifiedForInvoices") final Boolean isNotifiedForInvoices,
                   @JsonProperty("accountBalance") final BigDecimal accountBalance,
                   @JsonProperty("accountCBA") final BigDecimal accountCBA) {
        super(null);
        this.accountBalance = accountBalance;
        this.externalKey = externalKey;
        this.accountId = accountId;
        this.name = name;
        this.firstNameLength = firstNameLength;
        this.email = email;
        this.billCycleDayLocal = billCycleDayLocal;
        this.currency = currency;
        this.parentAccountId = parentAccountId;
        this.isPaymentDelegatedToParent = isPaymentDelegatedToParent;
        this.paymentMethodId = paymentMethodId;
        this.timeZone = timeZone;
        this.address1 = address1;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.company = company;
        this.city = city;
        this.state = state;
        this.country = country;
        this.locale = locale;
        this.phone = phone;
        this.notes = notes;
        this.isMigrated = isMigrated;
        this.isNotifiedForInvoices = isNotifiedForInvoices;
        this.accountCBA = accountCBA;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    public BigDecimal getAccountCBA() {
        return accountCBA;
    }

    public void setAccountCBA(final BigDecimal accountCBA) {
        this.accountCBA = accountCBA;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(final BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getFirstNameLength() {
        return firstNameLength;
    }

    public void setFirstNameLength(final Integer firstNameLength) {
        this.firstNameLength = firstNameLength;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Integer getBillCycleDayLocal() {
        return billCycleDayLocal;
    }

    public void setBillCycleDayLocal(final Integer billCycleDayLocal) {
        this.billCycleDayLocal = billCycleDayLocal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public UUID getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(final UUID parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    public Boolean getIsPaymentDelegatedToParent() {
        return isPaymentDelegatedToParent;
    }

    public void setIsPaymentDelegatedToParent(final Boolean isPaymentDelegatedToParent) {
        this.isPaymentDelegatedToParent = isPaymentDelegatedToParent;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(final UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(final String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public Boolean getIsMigrated() {
        return isMigrated;
    }

    public void setIsMigrated(final Boolean isMigrated) {
        this.isMigrated = isMigrated;
    }

    public Boolean getIsNotifiedForInvoices() {
        return isNotifiedForInvoices;
    }

    public void setIsNotifiedForInvoices(final Boolean isNotifiedForInvoices) {
        this.isNotifiedForInvoices = isNotifiedForInvoices;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("accountId='").append(accountId).append('\'');
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", accountCBA=").append(accountCBA);
        sb.append(", accountBalance=").append(accountBalance);
        sb.append(", name='").append(name).append('\'');
        sb.append(", firstNameLength=").append(firstNameLength);
        sb.append(", email='").append(email).append('\'');
        sb.append(", billCycleDayLocal=").append(billCycleDayLocal);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", parentAccountId='").append(parentAccountId).append('\'');
        sb.append(", isPaymentDelegatedToParent='").append(isPaymentDelegatedToParent).append('\'');
        sb.append(", paymentMethodId='").append(paymentMethodId).append('\'');
        sb.append(", timeZone='").append(timeZone).append('\'');
        sb.append(", address1='").append(address1).append('\'');
        sb.append(", address2='").append(address2).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", locale='").append(locale).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", notes='").append(notes).append('\'');
        sb.append(", isMigrated=").append(isMigrated);
        sb.append(", isNotifiedForInvoices=").append(isNotifiedForInvoices);
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

        final Account account = (Account) o;

        if (accountBalance != null ? accountBalance.compareTo(account.accountBalance) != 0 : account.accountBalance != null) {
            return false;
        }
        if (accountCBA != null ? accountCBA.compareTo(account.accountCBA) != 0 : account.accountCBA != null) {
            return false;
        }
        if (accountId != null ? !accountId.equals(account.accountId) : account.accountId != null) {
            return false;
        }
        if (address1 != null ? !address1.equals(account.address1) : account.address1 != null) {
            return false;
        }
        if (address2 != null ? !address2.equals(account.address2) : account.address2 != null) {
            return false;
        }
        if (billCycleDayLocal != null ? !billCycleDayLocal.equals(account.billCycleDayLocal) : account.billCycleDayLocal != null) {
            return false;
        }
        if (city != null ? !city.equals(account.city) : account.city != null) {
            return false;
        }
        if (company != null ? !company.equals(account.company) : account.company != null) {
            return false;
        }
        if (country != null ? !country.equals(account.country) : account.country != null) {
            return false;
        }
        if (currency != null ? !currency.equals(account.currency) : account.currency != null) {
            return false;
        }
        if (parentAccountId != null ? !parentAccountId.equals(account.parentAccountId) : account.parentAccountId != null) {
            return false;
        }
        if (isPaymentDelegatedToParent != null ? !isPaymentDelegatedToParent.equals(account.isPaymentDelegatedToParent) : account.isPaymentDelegatedToParent != null) {
            return false;
        }
        if (email != null ? !email.equals(account.email) : account.email != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(account.externalKey) : account.externalKey != null) {
            return false;
        }
        if (firstNameLength != null ? !firstNameLength.equals(account.firstNameLength) : account.firstNameLength != null) {
            return false;
        }
        if (isMigrated != null ? !isMigrated.equals(account.isMigrated) : account.isMigrated != null) {
            return false;
        }
        if (isNotifiedForInvoices != null ? !isNotifiedForInvoices.equals(account.isNotifiedForInvoices) : account.isNotifiedForInvoices != null) {
            return false;
        }
        if (locale != null ? !locale.equals(account.locale) : account.locale != null) {
            return false;
        }
        if (name != null ? !name.equals(account.name) : account.name != null) {
            return false;
        }
        if (paymentMethodId != null ? !paymentMethodId.equals(account.paymentMethodId) : account.paymentMethodId != null) {
            return false;
        }
        if (phone != null ? !phone.equals(account.phone) : account.phone != null) {
            return false;
        }
        if (notes != null ? !notes.equals(account.notes) : account.notes != null) {
            return false;
        }
        if (postalCode != null ? !postalCode.equals(account.postalCode) : account.postalCode != null) {
            return false;
        }
        if (state != null ? !state.equals(account.state) : account.state != null) {
            return false;
        }
        if (timeZone != null ? !timeZone.equals(account.timeZone) : account.timeZone != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (accountCBA != null ? accountCBA.hashCode() : 0);
        result = 31 * result + (accountBalance != null ? accountBalance.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (firstNameLength != null ? firstNameLength.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (billCycleDayLocal != null ? billCycleDayLocal.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (parentAccountId != null ? parentAccountId.hashCode() : 0);
        result = 31 * result + (isPaymentDelegatedToParent != null ? isPaymentDelegatedToParent.hashCode() : 0);
        result = 31 * result + (paymentMethodId != null ? paymentMethodId.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (isMigrated != null ? isMigrated.hashCode() : 0);
        result = 31 * result + (isNotifiedForInvoices != null ? isNotifiedForInvoices.hashCode() : 0);
        return result;
    }
}
