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

import java.util.ArrayList;

import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class KillBillObjects<T extends KillBillObject> extends ArrayList<T> {

    @JsonIgnore
    private KillBillHttpClient killBillHttpClient;

    @JsonIgnore
    private int paginationCurrentOffset;

    @JsonIgnore
    private int paginationNextOffset;

    @JsonIgnore
    private int paginationTotalNbRecords;

    @JsonIgnore
    private int paginationMaxNbRecords;

    @JsonIgnore
    private String paginationNextPageUri;

    @JsonIgnore
    public <U extends KillBillObjects<T>> U getNext(final Class<U> clazz) throws KillBillClientException {
        if (killBillHttpClient == null || paginationNextPageUri == null) {
            return null;
        }
        return killBillHttpClient.doGet(paginationNextPageUri, clazz, RequestOptions.empty());
    }

    @JsonIgnore
    public KillBillHttpClient getKillBillHttpClient() {
        return killBillHttpClient;
    }

    @JsonIgnore
    public void setKillBillHttpClient(final KillBillHttpClient killBillHttpClient) {
        this.killBillHttpClient = killBillHttpClient;
    }

    @JsonIgnore
    public int getPaginationCurrentOffset() {
        return paginationCurrentOffset;
    }

    @JsonIgnore
    public void setPaginationCurrentOffset(final int paginationCurrentOffset) {
        this.paginationCurrentOffset = paginationCurrentOffset;
    }

    @JsonIgnore
    public int getPaginationNextOffset() {
        return paginationNextOffset;
    }

    @JsonIgnore
    public void setPaginationNextOffset(final int paginationNextOffset) {
        this.paginationNextOffset = paginationNextOffset;
    }

    @JsonIgnore
    public int getPaginationTotalNbRecords() {
        return paginationTotalNbRecords;
    }

    @JsonIgnore
    public void setPaginationTotalNbRecords(final int paginationTotalNbRecords) {
        this.paginationTotalNbRecords = paginationTotalNbRecords;
    }

    @JsonIgnore
    public int getPaginationMaxNbRecords() {
        return paginationMaxNbRecords;
    }

    @JsonIgnore
    public void setPaginationMaxNbRecords(final int paginationMaxNbRecords) {
        this.paginationMaxNbRecords = paginationMaxNbRecords;
    }

    @JsonIgnore
    public String getPaginationNextPageUri() {
        return paginationNextPageUri;
    }

    @JsonIgnore
    public void setPaginationNextPageUri(final String paginationNextPageUri) {
        this.paginationNextPageUri = paginationNextPageUri;
    }
}
