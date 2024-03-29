/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2021 Equinix, Inc
 * Copyright 2014-2021 The Billing Project, LLC
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
import java.util.ArrayList;
import java.util.List;
import org.killbill.billing.client.model.gen.PluginServiceInfo;

/**
 *           DO NOT EDIT !!!
 *
 * This code has been generated by the Kill Bill swagger generator.
 *  @See https://github.com/killbill/killbill-swagger-coden
 */
import org.killbill.billing.client.model.KillBillObject;

public class PluginInfo {

    private String bundleSymbolicName = null;

    private String pluginKey = null;

    private String pluginName = null;

    private String version = null;

    private String state = null;

    private Boolean isSelectedForStart = null;

    private List<PluginServiceInfo> services = null;


    public PluginInfo() {
    }

    public PluginInfo(final String bundleSymbolicName,
                     final String pluginKey,
                     final String pluginName,
                     final String version,
                     final String state,
                     final Boolean isSelectedForStart,
                     final List<PluginServiceInfo> services) {
        this.bundleSymbolicName = bundleSymbolicName;
        this.pluginKey = pluginKey;
        this.pluginName = pluginName;
        this.version = version;
        this.state = state;
        this.isSelectedForStart = isSelectedForStart;
        this.services = services;

    }


    public PluginInfo setBundleSymbolicName(final String bundleSymbolicName) {
        this.bundleSymbolicName = bundleSymbolicName;
        return this;
    }

    public String getBundleSymbolicName() {
        return bundleSymbolicName;
    }

    public PluginInfo setPluginKey(final String pluginKey) {
        this.pluginKey = pluginKey;
        return this;
    }

    public String getPluginKey() {
        return pluginKey;
    }

    public PluginInfo setPluginName(final String pluginName) {
        this.pluginName = pluginName;
        return this;
    }

    public String getPluginName() {
        return pluginName;
    }

    public PluginInfo setVersion(final String version) {
        this.version = version;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PluginInfo setState(final String state) {
        this.state = state;
        return this;
    }

    public String getState() {
        return state;
    }

    public PluginInfo setIsSelectedForStart(final Boolean isSelectedForStart) {
        this.isSelectedForStart = isSelectedForStart;
        return this;
    }

    @JsonProperty(value="isSelectedForStart")
    public Boolean isSelectedForStart() {
        return isSelectedForStart;
    }

    public PluginInfo setServices(final List<PluginServiceInfo> services) {
        this.services = services;
        return this;
    }

    public PluginInfo addServicesItem(final PluginServiceInfo servicesItem) {
        if (this.services == null) {
            this.services = new ArrayList<PluginServiceInfo>();
        }
        this.services.add(servicesItem);
        return this;
    }

    public List<PluginServiceInfo> getServices() {
        return services;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PluginInfo pluginInfo = (PluginInfo) o;
        return Objects.equals(this.bundleSymbolicName, pluginInfo.bundleSymbolicName) &&
            Objects.equals(this.pluginKey, pluginInfo.pluginKey) &&
            Objects.equals(this.pluginName, pluginInfo.pluginName) &&
            Objects.equals(this.version, pluginInfo.version) &&
            Objects.equals(this.state, pluginInfo.state) &&
            Objects.equals(this.isSelectedForStart, pluginInfo.isSelectedForStart) &&
            Objects.equals(this.services, pluginInfo.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bundleSymbolicName,
            pluginKey,
            pluginName,
            version,
            state,
            isSelectedForStart,
            services);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PluginInfo {\n");
        
        sb.append("    bundleSymbolicName: ").append(toIndentedString(bundleSymbolicName)).append("\n");
        sb.append("    pluginKey: ").append(toIndentedString(pluginKey)).append("\n");
        sb.append("    pluginName: ").append(toIndentedString(pluginName)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    isSelectedForStart: ").append(toIndentedString(isSelectedForStart)).append("\n");
        sb.append("    services: ").append(toIndentedString(services)).append("\n");
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

