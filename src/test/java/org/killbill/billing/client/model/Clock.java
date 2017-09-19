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

package org.killbill.billing.client.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Clock {

    private final DateTime currentUtcTime;
    private final String timeZone;
    private final LocalDate localDate;

    @JsonCreator
    public Clock(@JsonProperty("currentUtcTime") final DateTime currentUtcTime,
                         @JsonProperty("timeZone") final String timeZone,
                         @JsonProperty("localDate") final LocalDate localDate) {

        this.currentUtcTime = currentUtcTime;
        this.timeZone = timeZone;
        this.localDate = localDate;
    }

    public DateTime getCurrentUtcTime() {
        return currentUtcTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    @Override
    public String toString() {
        return "Clock{" +
               "currentUtcTime=" + currentUtcTime +
               ", timeZone='" + timeZone + '\'' +
               ", localDate=" + localDate +
               '}';
    }
}
