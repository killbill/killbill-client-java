/*
 * Copyright 2014-2017 Groupon, Inc
 * Copyright 2014-2017 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
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

package org.killbill.billing.client.util;

import java.io.Closeable;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.killbill.billing.catalog.api.TimeUnit;
import org.killbill.billing.client.JaxrsResource;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.model.Clock;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ClockUtil implements Closeable {


    private static final String TEST = "test";
    private static final String TEST_PATH = JaxrsResource.PREFIX + "/" + TEST;
    private static final String CLOCK_PATH = TEST_PATH + "/clock";

    private static final String QUERY_TIMEZONE = "timeZone";
    private static final String QUERY_REQUESTED_DT = "requestedDate";
    private static final String QUERY_TIMEOUT_SEC = "timeoutSec";


    private final KillBillHttpClient httpClient;

    public ClockUtil(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void close() {
        httpClient.close();
    }


    public Clock getCurrentTime(@Nullable final String timeZone, final RequestOptions inputOptions) throws KillBillClientException {
        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        if (timeZone != null) {
            queryParams.put(QUERY_TIMEZONE, timeZone);
        }
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(CLOCK_PATH, Clock.class, requestOptions);
    }

    public Clock setCurrentTime(final DateTime targetDateTime, @Nullable final String timeZone, @Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(QUERY_REQUESTED_DT, targetDateTime.toString());
        if (timeZone != null) {
            queryParams.put(QUERY_TIMEZONE, timeZone);
        }
        if (timeoutSec != null) {
            queryParams.put(QUERY_TIMEOUT_SEC, Long.toString(timeoutSec));
        }
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();
        return httpClient.doPost(CLOCK_PATH, null, Clock.class, requestOptions);
    }


    public Clock addDays(final int value, @Nullable final String timeZone, @Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException {
        return addDuration(value, TimeUnit.DAYS, timeZone, timeoutSec, inputOptions);
    }

    public Clock addWeeks(final int value, @Nullable final String timeZone, @Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException  {
        return addDuration(value, TimeUnit.WEEKS, timeZone, timeoutSec, inputOptions);
    }

    public Clock addMonths(final int value, @Nullable final String timeZone, @Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException  {
        return addDuration(value, TimeUnit.MONTHS, timeZone, timeoutSec, inputOptions);
    }

    public Clock addYears(final int value, @Nullable final String timeZone, @Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException  {
        return addDuration(value, TimeUnit.YEARS, timeZone, timeoutSec, inputOptions);
    }

    private Clock addDuration(final int value, final TimeUnit unit, @Nullable final String timeZone, @Nullable final Long timeoutSec, final RequestOptions inputOptions) throws KillBillClientException  {
        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put(unit.name().toLowerCase(), Integer.toString(value));
        if (timeZone != null) {
            queryParams.put(QUERY_TIMEZONE, timeZone);
        }
        if (timeoutSec != null) {
            queryParams.put(QUERY_TIMEOUT_SEC, Long.toString(timeoutSec));
        }
        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();
        return httpClient.doPut(CLOCK_PATH, null, Clock.class, requestOptions);
    }
}
