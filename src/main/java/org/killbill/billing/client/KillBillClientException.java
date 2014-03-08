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

package org.killbill.billing.client;

import org.killbill.billing.client.model.BillingException;
import com.ning.http.client.Response;

@SuppressWarnings("serial")
public class KillBillClientException extends Exception {

    private final Response response;
    private final BillingException billingException;

    public KillBillClientException(final Exception cause) {
        super(cause);
        response = null;
        billingException = null;
    }

    public KillBillClientException(final Exception cause, final Response response) {
        super(cause);
        this.response = response;
        billingException = null;
    }

    public KillBillClientException(final BillingException exception, final Response response) {
        super(exception.getMessage());
        this.response = response;
        this.billingException = exception;
    }

    public Response getResponse() {
        return response;
    }

    public BillingException getBillingException() {
        return billingException;
    }
}
