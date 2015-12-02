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

import java.util.Date;
import java.util.List;

import org.killbill.billing.catalog.api.Currency;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Catalog extends KillBillObject {

    private String name;
    private Date effectiveDate;
    private List<Currency> currencies;
    private List<Product> products;
    private List<PriceList> priceLists;

    public Catalog() {}

    @JsonCreator
    public Catalog(@JsonProperty("name") final String name,
                       @JsonProperty("effectiveDate") final Date effectiveDate,
                       @JsonProperty("currencies") final List<Currency> currencies,
                       @JsonProperty("products") final List<Product> products,
                       @JsonProperty("priceLists") final List<PriceList> priceLists) {
        this.name = name;
        this.effectiveDate = effectiveDate;
        this.currencies = currencies;
        this.products = products;
        this.priceLists = priceLists;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(final List<Product> products) {
        this.products = products;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(final List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    public void setPriceLists(final List<PriceList> priceLists) {
        this.priceLists = priceLists;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Catalog{");
        sb.append("name='").append(name).append('\'');
        sb.append(", effectiveDate='").append(effectiveDate).append('\'');
        sb.append(", currencies='").append(currencies).append('\'');
        sb.append(", products=").append(products);
        sb.append(", priceLists=").append(priceLists);
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

        final Catalog catalog = (Catalog) o;

        if (name != null ? !name.equals(catalog.name) : catalog.name != null) {
            return false;
        }
        if (effectiveDate != null ? !effectiveDate.equals(catalog.effectiveDate) : catalog.effectiveDate != null) {
            return false;
        }
        if (currencies != null ? !currencies.equals(catalog.currencies) : catalog.currencies != null) {
            return false;
        }
        if (products != null ? !products.equals(catalog.products) : catalog.products != null) {
            return false;
        }
        if (priceLists != null ? !priceLists.equals(catalog.priceLists) : catalog.priceLists != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (products != null ? products.hashCode() : 0);
        return result;
    }
}
