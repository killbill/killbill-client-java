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

package org.killbill.billing.client.model;

import org.killbill.billing.util.audit.AuditLog;

import java.util.List;

import javax.annotation.Nullable;

public abstract class KillBillObject {

    protected List<AuditLog> auditLogs;

    public KillBillObject() {
        this(null);
    }

    public KillBillObject(@Nullable final List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(final List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
}
