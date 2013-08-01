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

package com.ning.billing.client;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    public static final String COMMON_PROPERTIES_FILE = "config.properties";

    // Works as cache
    private Properties commonProperties = null;

    private static PropertiesManager propManager = new PropertiesManager();

    private PropertiesManager() {
        try {
            commonProperties = new Properties();
            commonProperties.load(this.getClass().getClassLoader().getResourceAsStream(COMMON_PROPERTIES_FILE));
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    /**
     * Returns an instance of the PropertiesManager.
     *
     * @return PropertiesManager
     */
    public static PropertiesManager getInstance() {
        return propManager;
    }

    /**
     * Returns properties common to portal, defined in 'common.properties'.
     *
     * @param propKey
     * @return
     */
    public String getCommonProperty(String propKey) {
        if (commonProperties != null) {
            return commonProperties.getProperty(propKey);
        }
        return null;
    }

    public String getCommonProperty(String propKey, String defaultValue) {
        String propVal = getCommonProperty(propKey);
        if (propVal != null) {
            return propVal;
        }
        return defaultValue;
    }

    public synchronized void reLoad() {
        try {
            //keep a ref to existing properties
            Properties oldProps = commonProperties;
            //initialize a new one
            Properties newProps = new Properties();
            newProps.load(this.getClass().getClassLoader().getResourceAsStream(COMMON_PROPERTIES_FILE));
            //use the new one
            commonProperties = newProps;
            //clear the old one
            oldProps.clear();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
