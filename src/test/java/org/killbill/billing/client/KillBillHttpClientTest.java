/*
 * Copyright 2014-2018 Groupon, Inc
 * Copyright 2014-2018 The Billing Project, LLC
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

import com.ning.http.client.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;


public class KillBillHttpClientTest {

    private KillBillHttpClient client;

    @BeforeMethod
    public void setUp() {
        client = spy(KillBillHttpClient.class);
    }

    @Test(groups="fast")
    public void testThrowExceptionOnResponseError401() {
        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(401);

        try {
            client.throwExceptionOnResponseError(response);
            fail();
        } catch (KillBillClientException e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertSame(response, e.getResponse());
            assertNull(e.getBillingException());
        }
    }

    @Test(groups="fast")
    public void testThrowExceptionOnResponseWithBillingException() throws Exception {
        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(400);

        final String BILLING_EXCEPTION_MESSAGE = "billing exception message";
        BillingException billingException = new BillingException(null, null, BILLING_EXCEPTION_MESSAGE, null, null, null);
        doReturn(billingException).when(client).deserializeResponse(response, BillingException.class);

        try {
            client.throwExceptionOnResponseError(response);
            fail();
        } catch (KillBillClientException e) {
            assertEquals(BILLING_EXCEPTION_MESSAGE, e.getMessage());
            assertSame(response, e.getResponse());
            assertSame(billingException, e.getBillingException());
        }
    }

    @Test(groups="fast")
    public void testThrowExceptionOnResponseNoBillingException() throws Exception {
        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(400);

        doReturn(null).when(client).deserializeResponse(response, BillingException.class);

        try {
            client.throwExceptionOnResponseError(response);
            fail();
        } catch (KillBillClientException e) {
            assertSame(response, e.getResponse());
            assertNull(e.getBillingException());
        }
    }
}