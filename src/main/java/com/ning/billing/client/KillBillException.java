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

import java.io.IOException;

import com.ning.billing.BillingExceptionBase;
import com.ning.billing.ErrorCode;
import com.ning.billing.jaxrs.json.BillingExceptionJson;

@SuppressWarnings("serial")
public class KillBillException extends BillingExceptionBase {

    public KillBillException(BillingExceptionBase cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public KillBillException(ErrorCode code, Object... args) {
        super(code, args);
    }

    public KillBillException(Throwable cause, ErrorCode code, Object... args) {
        super(cause, code, args);
    }

    public KillBillException(Throwable cause, int code, String msg) {
        super(cause, code, msg);
    }

    public static KillBillException parse(String exceptionMsg) {
        try {
            final BillingExceptionJson exceptionJson = KillBillBaseResource.mapper.readValue(exceptionMsg, BillingExceptionJson.class);
            return new KillBillException(null, exceptionJson.getCode() == null ? 0 : exceptionJson.getCode(), exceptionJson.getMessage());
        } catch (IOException e) {
            return parseLegacyException(exceptionMsg);
        }
    }

    private static KillBillException parseLegacyException(final String exceptionMsg) {//{cause=null, code=3000, formattedMsg='Account already exists for key 123'}
        Throwable cause = new Throwable();
        int code = ErrorCode.__UNKNOWN_ERROR_CODE.getCode();
        String msg = null;
        int idx = exceptionMsg.indexOf("cause=");
        int idxComma;
        if (idx >= 0) {
            idxComma = exceptionMsg.indexOf(",", idx);
            String causeStr = exceptionMsg.substring(idx + "cause=".length(), idxComma);
            if (null != causeStr && !causeStr.equalsIgnoreCase("null")) {
                cause = new Throwable(causeStr);
            }
        }
        idx = exceptionMsg.indexOf("code=");
        if (idx >= 0) {
            idxComma = exceptionMsg.indexOf(",", idx);
            try {
                code = Integer.parseInt(exceptionMsg.substring(idx + "code=".length(), idxComma));
            } catch (NumberFormatException e) {
            }
        }
        idx = exceptionMsg.indexOf("formattedMsg=");
        if (idx >= 0) {
            idxComma = exceptionMsg.length() - 1;    // ignore last }
            msg = exceptionMsg.substring(idx + "formattedMsg=".length(), idxComma);
        }
        return new KillBillException(cause, code, msg);
    }
}
