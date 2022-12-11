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

package org.killbill.billing.client;

/**
 * Convenience class that encapsulates details of "percent encoding"
 * (as per RFC-3986, see [http://www.ietf.org/rfc/rfc3986.txt]).
 */
public final class UTF8UrlEncoder {

    /**
     * Encoding table used for figuring out ascii characters that must be escaped
     * (all non-Ascii characters need to be encoded anyway)
     */
    private static final boolean[] SAFE_ASCII = new boolean[128];

    static {
        for (int i = 'a'; i <= 'z'; ++i) {
            SAFE_ASCII[i] = true;
        }
        for (int i = 'A'; i <= 'Z'; ++i) {
            SAFE_ASCII[i] = true;
        }
        for (int i = '0'; i <= '9'; ++i) {
            SAFE_ASCII[i] = true;
        }
        SAFE_ASCII['-'] = true;
        SAFE_ASCII['.'] = true;
        SAFE_ASCII['_'] = true;
        SAFE_ASCII['~'] = true;
    }

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private UTF8UrlEncoder() {
    }

    public static String encode(final CharSequence input) {
        return encode(input, false);
    }

    public static String encode(final CharSequence input, final boolean encodeSpaceUsingPlus) {
        final StringBuilder sb = new StringBuilder(input.length() + 16);
        appendEncoded(sb, input, encodeSpaceUsingPlus);
        return sb.toString();
    }

    public static StringBuilder appendEncoded(final StringBuilder sb, final CharSequence input) {
        return appendEncoded(sb, input, false);
    }

    public static StringBuilder appendEncoded(final StringBuilder sb, final CharSequence input, final boolean encodeSpaceUsingPlus) {
        int c;
        for (int i = 0; i < input.length(); i += Character.charCount(c)) {
            c = Character.codePointAt(input, i);
            if (c <= 127) {
                if (SAFE_ASCII[c]) {
                    sb.append((char) c);
                } else {
                    appendSingleByteEncoded(sb, c, encodeSpaceUsingPlus);
                }
            } else {
                appendMultiByteEncoded(sb, c, encodeSpaceUsingPlus);
            }
        }
        return sb;
    }

    private static void appendSingleByteEncoded(final StringBuilder sb, final int value, final boolean encodeSpaceUsingPlus) {
        if (encodeSpaceUsingPlus && value == 32) {
            sb.append('+');
            return;
        }

        sb.append('%');
        sb.append(HEX[value >> 4]);
        sb.append(HEX[value & 0xF]);
    }

    private static void appendMultiByteEncoded(final StringBuilder sb, final int value, final boolean encodeSpaceUsingPlus) {
        if (value < 0x800) {
            appendSingleByteEncoded(sb, (0xc0 | (value >> 6)), encodeSpaceUsingPlus);
            appendSingleByteEncoded(sb, (0x80 | (value & 0x3f)), encodeSpaceUsingPlus);
        } else if (value < 0x10000) {
            appendSingleByteEncoded(sb, (0xe0 | (value >> 12)), encodeSpaceUsingPlus);
            appendSingleByteEncoded(sb, (0x80 | ((value >> 6) & 0x3f)), encodeSpaceUsingPlus);
            appendSingleByteEncoded(sb, (0x80 | (value & 0x3f)), encodeSpaceUsingPlus);
        } else {
            appendSingleByteEncoded(sb, (0xf0 | (value >> 18)), encodeSpaceUsingPlus);
            appendSingleByteEncoded(sb, (0x80 | (value >> 12) & 0x3f), encodeSpaceUsingPlus);
            appendSingleByteEncoded(sb, (0x80 | (value >> 6) & 0x3f), encodeSpaceUsingPlus);
            appendSingleByteEncoded(sb, (0x80 | (value & 0x3f)), encodeSpaceUsingPlus);
        }
    }
}