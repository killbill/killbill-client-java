/*
 * Copyright 2010-2013 Ning, Inc.
 * Copyright 2016 Groupon, Inc
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

package org.killbill.billing.client;

import java.util.UUID;

import org.killbill.billing.client.RequestOptions.RequestOptionsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by arodrigues on 5/27/16.
 */
@Test(groups = "fast")
public class TestRequestOptions {

    private final RequestOptionsBuilder builder = RequestOptions.builder();

    @Test
    public void testBuilder() {
        final String requestId = randomString();
        builder.withRequestId(requestId);
        final String user = randomString();
        builder.withUser(user);
        final String password = randomString();
        builder.withPassword(password);
        final String createdBy = randomString();
        builder.withCreatedBy(createdBy);
        final String reason = randomString();
        builder.withReason(reason);
        final String comment = randomString();
        builder.withComment(comment);
        final String tenantApiKey = randomString();
        builder.withTenantApiKey(tenantApiKey);
        final String tenantApiSecret = randomString();
        builder.withTenantApiSecret(tenantApiSecret);

        final RequestOptions options = builder.build();
        Assert.assertEquals(options.getRequestId(), requestId);
        Assert.assertEquals(options.getUser(), user);
        Assert.assertEquals(options.getPassword(), password);
        Assert.assertEquals(options.getCreatedBy(), createdBy);
        Assert.assertEquals(options.getReason(), reason);
        Assert.assertEquals(options.getComment(), comment);
        Assert.assertEquals(options.getTenantApiKey(), tenantApiKey);
        Assert.assertEquals(options.getTenantApiSecret(), tenantApiSecret);
    }

    @Test
    public void testBuilderWithQueryParams() {
        final Multimap<String, String> queryParams = ArrayListMultimap.<String, String>create();
        queryParams.put("test-param", randomString());
        builder.withQueryParams(queryParams);

        final Multimap<String, String> queryParamsForFollow = ArrayListMultimap.<String, String>create();
        queryParamsForFollow.put("test-param-for-follow", randomString());
        builder.withQueryParamsForFollow(queryParamsForFollow);

        final RequestOptions options = builder.build();
        Assert.assertNotSame(options.getQueryParams(), queryParams);
        Assert.assertEquals(options.getQueryParams(), queryParams);

        Assert.assertNotSame(options.getQueryParamsForFollow(), queryParamsForFollow);
        Assert.assertEquals(options.getQueryParamsForFollow(), queryParamsForFollow);
    }

    @Test
    public void testBuilderWithHeaders() {
        final String header = randomString();
        builder.withHeader("test-header", header);

        final RequestOptions options = builder.build();
        Assert.assertEquals(options.getHeaders().get("test-header"), header);
    }

    @Test
    public void testBuilderWithNullFollowLocation() {
        builder.withFollowLocation(null);

        final RequestOptions options = builder.build();
        Assert.assertNull(options.getFollowLocation());
        Assert.assertFalse(options.shouldFollowLocation());
    }

    @Test
    public void testBuilderWithFollowLocation() {
        final Boolean follow = Boolean.TRUE;
        builder.withFollowLocation(follow);

        final RequestOptions options = builder.build();
        Assert.assertEquals(options.getFollowLocation(), follow);
        Assert.assertTrue(options.shouldFollowLocation());
    }

    @Test
    public void testBuilderWithFollowLocationFalse() {
        final Boolean follow = Boolean.FALSE;
        builder.withFollowLocation(follow);

        final RequestOptions options = builder.build();
        Assert.assertEquals(options.getFollowLocation(), follow);
        Assert.assertFalse(options.shouldFollowLocation());
    }

    @Test
    public void testExtend() {
        final String requestId = randomString();
        builder.withRequestId(requestId);
        final String user = randomString();
        builder.withUser(user);
        final String password = randomString();
        builder.withPassword(password);
        final String createdBy = randomString();
        builder.withCreatedBy(createdBy);
        final String reason = randomString();
        builder.withReason(reason);
        final String comment = randomString();
        builder.withComment(comment);
        final String tenantApiKey = randomString();
        builder.withTenantApiKey(tenantApiKey);
        final String tenantApiSecret = randomString();
        builder.withTenantApiSecret(tenantApiSecret);
        final Multimap<String, String> queryParams = ImmutableMultimap.of("param", randomString());
        builder.withQueryParams(queryParams);
        final String header = randomString();
        builder.withHeader("test-header", header);
        final Boolean followLocation = true;
        builder.withFollowLocation(followLocation);
        final Multimap<String, String> queryParamsForFollow = ImmutableMultimap.of("param-for-follow", randomString());
        builder.withQueryParamsForFollow(queryParamsForFollow);

        final RequestOptions extendedOptions = builder.build().extend().build();
        Assert.assertEquals(extendedOptions.getRequestId(), requestId);
        Assert.assertEquals(extendedOptions.getUser(), user);
        Assert.assertEquals(extendedOptions.getPassword(), password);
        Assert.assertEquals(extendedOptions.getCreatedBy(), createdBy);
        Assert.assertEquals(extendedOptions.getReason(), reason);
        Assert.assertEquals(extendedOptions.getComment(), comment);
        Assert.assertEquals(extendedOptions.getTenantApiKey(), tenantApiKey);
        Assert.assertEquals(extendedOptions.getTenantApiSecret(), tenantApiSecret);
        Assert.assertEquals(extendedOptions.getHeaders().get("test-header"), header);
        Assert.assertEquals(extendedOptions.getQueryParams(), queryParams);
        Assert.assertEquals(extendedOptions.getFollowLocation(), followLocation);
        Assert.assertEquals(extendedOptions.getQueryParamsForFollow(), queryParamsForFollow);
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }
}
