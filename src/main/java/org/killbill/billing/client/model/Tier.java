/*
 * Copyright 2016 The Billing Project, LLC
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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tier {

    private final List<TieredBlock> blocks;

    @JsonCreator
    public Tier(@JsonProperty("tiers") final List<TieredBlock> blocks) {
        this.blocks = blocks;
    }

    public List<TieredBlock> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tier{");
        sb.append("blocks='").append(blocks);
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

        final Tier tier = (Tier) o;

        if (blocks != null ? !blocks.equals(tier.blocks) : tier.blocks != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = blocks != null ? blocks.hashCode() : 0;
        return result;
    }
}
