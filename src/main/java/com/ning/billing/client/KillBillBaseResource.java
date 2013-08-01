/*
 * Copyright 2010-2013 Ning, Inc.
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

package com.ning.billing.client;

import java.util.HashMap;
import java.util.Map;

import com.ning.billing.ErrorCode;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class KillBillBaseResource {

    public static ObjectMapper mapper = new ObjectMapper();

    protected final Map<String, String> DEFAULT_EMPTY_QUERY = new HashMap<String, String>();
    protected KillBillHttpClient httpClient = new KillBillHttpClient();

    protected static KillBillException nullResponse() {
        String msg = "Response is null: REST call might have timed out..";
        return new KillBillException(new NullPointerException(msg), ErrorCode.UNEXPECTED_ERROR, msg);
    }
}
