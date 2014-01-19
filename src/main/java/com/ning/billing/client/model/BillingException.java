/*
 * Copyright 2010-2014 Ning, Inc.
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

package com.ning.billing.client.model;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BillingException {

    private String className;
    private Integer code;
    private String message;
    private String causeClassName;
    private String causeMessage;
    private List<StackTraceElement> stackTrace;
    // TODO add getSuppressed() from 1.7?

    @JsonCreator
    public BillingException(@JsonProperty("className") final String className,
                            @JsonProperty("code") @Nullable final Integer code,
                            @JsonProperty("message") final String message,
                            @JsonProperty("causeClassName") final String causeClassName,
                            @JsonProperty("causeMessage") final String causeMessage,
                            @JsonProperty("stackTrace") final List<StackTraceElement> stackTrace) {
        this.className = className;
        this.code = code;
        this.message = message;
        this.causeClassName = causeClassName;
        this.causeMessage = causeMessage;
        this.stackTrace = stackTrace;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getCauseClassName() {
        return causeClassName;
    }

    public void setCauseClassName(final String causeClassName) {
        this.causeClassName = causeClassName;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public void setCauseMessage(final String causeMessage) {
        this.causeMessage = causeMessage;
    }

    public List<StackTraceElement> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(final List<StackTraceElement> stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BillingException{");
        sb.append("className='").append(className).append('\'');
        sb.append(", code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", causeClassName='").append(causeClassName).append('\'');
        sb.append(", causeMessage='").append(causeMessage).append('\'');
        sb.append(", stackTrace=").append(stackTrace);
        sb.append('}');
        return sb.toString();
    }


}
