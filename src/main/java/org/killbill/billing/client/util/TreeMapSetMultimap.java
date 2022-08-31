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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * <p>Implementation of {@link Multimap}: </p>
 * <ul>
 *     <li>backed by {@link SortedMap} and use {@link TreeSet} as a values</li>
 *     <li>key cannot be {@code null}</li>
 *     <li>values cannot be {@code null}</li>
 *     <li>{@link #asMap()} return backed map (the {@code SortedMap}).</li>
 * </ul>
 */
public class TreeMapSetMultimap<K, V> implements Multimap<K, V> {

    private final SortedMap<K, Collection<V>> delegate = new TreeMap<>();

    public TreeMapSetMultimap() {
    }

    public TreeMapSetMultimap(final Map<K, ? extends Collection<V>> map) {
        if (map != null && !map.isEmpty()) {
            delegate.putAll(map);
        }
    }

    @Override
    public void put(final K key, final V value) {
        Preconditions.checkNotNull(key, "Cannot #put() with null key");
        Preconditions.checkNotNull(value, "Cannot #put() with null value");

        if (delegate.containsKey(key)) {
            delegate.get(key).add(value);
        } else {
            final Collection<V> list = new TreeSet<>();
            list.add(value);
            delegate.put(key, list);
        }
    }

    @Override
    public void putAll(final K key, final Collection<V> values) {
        Preconditions.checkNotNull(key, "Cannot #putAll() with null key");
        Preconditions.checkNotNull(values, "Cannot #putAll() with null values");

        if (!values.isEmpty()) {
            if (delegate.containsKey(key)) {
                delegate.get(key).addAll(values);
            } else {
                delegate.put(key, new TreeSet<>(values));
            }
        }
    }

    @Override
    public void remove(final K key) {
        Preconditions.checkNotNull(key, "Cannot #remove() with null key");
        delegate.remove(key);
    }

    @Override
    public Map<K, Collection<V>> asMap() {
        return delegate;
    }

    @Override
    public String toString() {
        return "TreeMapSetMultimap {"  + delegate + '}';
    }
}
