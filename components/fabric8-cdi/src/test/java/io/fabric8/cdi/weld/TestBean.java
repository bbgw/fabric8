/*
 * Copyright 2005-2014 Red Hat, Inc.                                    
 *                                                                      
 * Red Hat licenses this file to you under the Apache License, version  
 * 2.0 (the "License"); you may not use this file except in compliance  
 * with the License.  You may obtain a copy of the License at           
 *                                                                      
 *    http://www.apache.org/licenses/LICENSE-2.0                        
 *                                                                      
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,    
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or      
 * implied.  See the License for the specific language governing        
 * permissions and limitations under the License.
 */

package io.fabric8.cdi.weld;


import io.fabric8.cdi.annotations.Configuration;
import io.fabric8.cdi.annotations.Service;

import javax.inject.Inject;

public class TestBean {

    @Inject
    @Service("kubernetes")
    String kubernetesUrl;

    @Inject
    @Service("fabric8-console-service")
    String consoleUrl;
    
    @Inject
    @Configuration("MY_CONFIG")
    ConfigBean configBean;

    @Inject
    @Configuration("MY_OTHER_CONFIG")
    ConfigBean otherConfigBean;

    public TestBean() {
    }

    public String getKubernetesUrl() {
        return kubernetesUrl;
    }

    public String getConsoleUrl() {
        return consoleUrl;
    }

    public ConfigBean getConfigBean() {
        return configBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.configBean = configBean;
    }

    public ConfigBean getOtherConfigBean() {
        return otherConfigBean;
    }

    public void setOtherConfigBean(ConfigBean otherConfigBean) {
        this.otherConfigBean = otherConfigBean;
    }
}
