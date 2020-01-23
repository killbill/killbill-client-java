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


package org.killbill.billing.client.model.gen;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.killbill.billing.client.model.gen.Session;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
import org.killbill.billing.client.model.KillBillObject;

public class Subject {

    private String principal = null;

    private Boolean isAuthenticated = null;

    private Boolean isRemembered = null;

    private Session session = null;


    public Subject() {
    }

    public Subject(final String principal,
                     final Boolean isAuthenticated,
                     final Boolean isRemembered,
                     final Session session) {
        this.principal = principal;
        this.isAuthenticated = isAuthenticated;
        this.isRemembered = isRemembered;
        this.session = session;

    }


    public Subject setPrincipal(final String principal) {
        this.principal = principal;
        return this;
    }

    public String getPrincipal() {
        return principal;
    }

    public Subject setIsAuthenticated(final Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
        return this;
    }

    @JsonProperty(value="isAuthenticated")
    public Boolean isAuthenticated() {
        return isAuthenticated;
    }

    public Subject setIsRemembered(final Boolean isRemembered) {
        this.isRemembered = isRemembered;
        return this;
    }

    @JsonProperty(value="isRemembered")
    public Boolean isRemembered() {
        return isRemembered;
    }

    public Subject setSession(final Session session) {
        this.session = session;
        return this;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subject subject = (Subject) o;
        return Objects.equals(this.principal, subject.principal) &&
        Objects.equals(this.isAuthenticated, subject.isAuthenticated) &&
        Objects.equals(this.isRemembered, subject.isRemembered) &&
        Objects.equals(this.session, subject.session);

    }

    @Override
    public int hashCode() {
        return Objects.hash(principal,
                            isAuthenticated,
                            isRemembered,
                            session);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Subject {\n");
        
        sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
        sb.append("    isAuthenticated: ").append(toIndentedString(isAuthenticated)).append("\n");
        sb.append("    isRemembered: ").append(toIndentedString(isRemembered)).append("\n");
        sb.append("    session: ").append(toIndentedString(session)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

