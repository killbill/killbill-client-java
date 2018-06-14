package org.killbill.billing.client;

import com.ning.http.client.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class KillBillHttpClientTest {

    private KillBillHttpClient client;

    @BeforeMethod
    public void setUp() {
        client = spy(KillBillHttpClient.class);
    }

    @Test
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

    @Test
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

    @Test
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