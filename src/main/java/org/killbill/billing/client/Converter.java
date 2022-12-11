/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2020 Equinix, Inc
 * Copyright 2014-2020 The Billing Project, LLC
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

package org.killbill.billing.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

public class Converter {

    public static List<String> convertEnumListToStringList(final List<? extends Enum<?>> in) {
        return in.stream()
                .map(input -> input == null ? null : input.name())
                .collect(Collectors.toUnmodifiableList());
    }

    public static List<String> convertUUIDListToStringList(final List<UUID> in) {
        return in.stream()
                .map(input -> input == null ? null : input.toString())
                .collect(Collectors.toUnmodifiableList());
    }

    public static List<String> convertPluginPropertyMap(final Map<String, String> pluginProperties) {
        if (pluginProperties == null || pluginProperties.isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> result = new ArrayList<>();
        for (final Entry<String, String> entry : pluginProperties.entrySet()) {
            final String encodedKey = UTF8UrlEncoder.encode(entry.getKey());
            final String encodedValue = UTF8UrlEncoder.encode(entry.getValue());
            result.add(String.format("%s=%s", encodedKey, encodedValue));
        }
        return result;
    }

}
