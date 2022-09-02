/*
 * Copyright 2020-2022 Equinix, Inc
 * Copyright 2014-2022 The Billing Project, LLC
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTreeMapSetMultimap {

    @Test(groups = "fast")
    public void put() {
        final Multimap<String, String> multimap = new TreeMapSetMultimap<>();
        multimap.put("key", "value");
        Assert.assertEquals(multimap.asMap().size(), 1);
        Assert.assertTrue(multimap.asMap().keySet().stream().allMatch("key"::equals));
        Assert.assertTrue(multimap.asMap().values().stream().allMatch(values -> values.contains("value")));

        try {
            multimap.put(null, "any");
            Assert.fail("Null key should not supported");
        } catch (final NullPointerException ignored) {
        }

        try {
            multimap.put(null, "any");
            Assert.fail("Null values should not supported");
        } catch (final NullPointerException ignored) {
        }
    }

    @Test(groups = "fast")
    public void putAll() {
        final Multimap<String, String> multimap = new TreeMapSetMultimap<>();
        multimap.putAll("key1", List.of("1.1", "1.2", "1.3"));

        Assert.assertTrue(multimap.asMap().keySet().stream().allMatch("key1"::equals));
        Assert.assertTrue(multimap.asMap().values().stream().allMatch(values -> values.containsAll(List.of("1.1", "1.2", "1.3"))));

        // No matter what user supplied in values, it should be able to put more.
        multimap.put("key1", "1.4");
        multimap.putAll("key1", Collections.singleton("1.5"));
        multimap.putAll("key1", Set.of("1.5", "1.6")); // 1.5 purposely duplicated.
        multimap.putAll("key2", List.of("2.1", "2.2"));
        multimap.putAll("key1", Collections.emptyList());

        Assert.assertEquals(multimap.asMap().get("key1").size(), 6);
        Assert.assertTrue(multimap.asMap().get("key1").containsAll(List.of("1.1", "1.2", "1.3", "1.4", "1.5", "1.6")));
    }

    @Test
    public void remove() {
        final Multimap<String, String> multimap = new TreeMapSetMultimap<>();
        multimap.put("key1", "val1");
        multimap.put("key2", "val2");

        Assert.assertEquals(multimap.asMap().size(), 2);

        multimap.remove("key1");

        Assert.assertEquals(multimap.asMap().size(), 1);
    }

    @Test(groups = "fast")
    public void asMap() {
        final Multimap<String, String> multimap = new TreeMapSetMultimap<>();
        multimap.putAll("key1", List.of("1.1", "1.2", "1.3"));

        final Map<String, Collection<String>> asMap = multimap.asMap();

        Assert.assertEquals(asMap.size(), 1);
        Assert.assertEquals(asMap.get("key1").size(), 3);

        multimap.put("key1", "1.4");
        multimap.putAll("key1", List.of("1.5", "1.6"));
        multimap.put("key2", "2.1");

        Assert.assertEquals(asMap.get("key2"), List.of("2.1"));
        Assert.assertEquals(asMap.size(), 2);
        Assert.assertEquals(asMap.get("key1").size(), 6);
    }
}
