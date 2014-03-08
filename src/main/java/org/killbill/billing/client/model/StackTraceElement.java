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

package org.killbill.billing.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StackTraceElement {

    private String className;
    private String fileName;
    private Integer lineNumber;
    private String methodName;
    private Boolean nativeMethod;

    @JsonCreator
    public StackTraceElement(@JsonProperty("className") final String className,
                             @JsonProperty("fileName") final String fileName,
                             @JsonProperty("lineNumber") final Integer lineNumber,
                             @JsonProperty("methodName") final String methodName,
                             @JsonProperty("nativeMethod") final Boolean nativeMethod) {
        this.className = className;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.methodName = methodName;
        this.nativeMethod = nativeMethod;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(final Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    public Boolean getNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(final Boolean nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StackTraceElement{");
        sb.append("className='").append(className).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", lineNumber=").append(lineNumber);
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", nativeMethod=").append(nativeMethod);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final StackTraceElement that = (StackTraceElement) o;

        if (className != null ? !className.equals(that.className) : that.className != null) {
            return false;
        }
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) {
            return false;
        }
        if (lineNumber != null ? !lineNumber.equals(that.lineNumber) : that.lineNumber != null) {
            return false;
        }
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) {
            return false;
        }
        if (nativeMethod != null ? !nativeMethod.equals(that.nativeMethod) : that.nativeMethod != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (nativeMethod != null ? nativeMethod.hashCode() : 0);
        return result;
    }
}
