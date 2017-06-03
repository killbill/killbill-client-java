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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Unit extends KillBillObject {

    private String name;
    private String prettyName;

    @JsonCreator
    public Unit(@JsonProperty("name") final String name,
                @JsonProperty("prettyName") final String prettyName) {
        this.name = name;
        this.prettyName = prettyName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public void setPrettyName(final String prettyName) {
        this.prettyName = prettyName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Unit)) {
            return false;
        }

        final Unit unit = (Unit) o;

        if (name != null ? !name.equals(unit.name) : unit.name != null) {
            return false;
        }
        return prettyName != null ? prettyName.equals(unit.prettyName) : unit.prettyName == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (prettyName != null ? prettyName.hashCode() : 0);
        return result;
    }
}
