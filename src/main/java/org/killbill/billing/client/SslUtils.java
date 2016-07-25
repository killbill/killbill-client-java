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

package org.killbill.billing.client;

import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.common.base.MoreObjects;

public class SslUtils {

    private static final String TLS_PROTOCOL_DEFAULT = "TLSv1.2";
    private final SSLContext looseTrustManagerSSLContext = looseTrustManagerSSLContext();
    private SSLContext context;

    public static SslUtils getInstance() {
        return SingletonHolder.instance;
    }

    private SSLContext looseTrustManagerSSLContext() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new LooseTrustManager()}, new SecureRandom());
            return sslContext;
        } catch (final NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        } catch (final KeyManagementException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public SSLContext getSSLContext(final boolean acceptAnyCertificate, @Nullable final String protocol) throws GeneralSecurityException {
        if (acceptAnyCertificate) {
            return looseTrustManagerSSLContext;
        } else if (context == null) {
            this.context = SSLContext.getInstance(MoreObjects.firstNonNull(protocol, TLS_PROTOCOL_DEFAULT));
            this.context.init(null, null, null);
        }
        return this.context;
    }

    private static class LooseTrustManager implements X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
        }

        public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
        }
    }

    private static class SingletonHolder {

        static final SslUtils instance = new SslUtils();
    }
}
