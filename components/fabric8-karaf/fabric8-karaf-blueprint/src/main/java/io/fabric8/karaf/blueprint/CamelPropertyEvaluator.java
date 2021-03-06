/**
 * Copyright 2016 Red Hat, Inc.
 * <p/>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.fabric8.karaf.blueprint;

import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.apache.aries.blueprint.ext.evaluator.PropertyEvaluator;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

/**
 * Ports Camel's env: sys: service: service.host: service.port: property
 * placeholder prefix resolution strategies so that they are supported in Blueprint
 * via an evaluator.
 *
 * see: http://camel.apache.org/using-propertyplaceholder.html
 *
 * It supports chained evaluators i.e ${env+service:MY_ENV_VAR} where the first
 * step is to resolve MY_ENV_VAR against environment variables then the result is
 * resolved using service function.
 */
@Component(
    name = "io.fabric8.karaf.camel",
    immediate = true,
    enabled = true,
    policy = ConfigurationPolicy.IGNORE,
    createPid = false
)
@org.apache.felix.scr.annotations.Properties({@Property(
    name = "org.apache.aries.blueprint.ext.evaluator.name", value = "camel")
})
@Service(PropertyEvaluator.class)
public class CamelPropertyEvaluator implements PropertyEvaluator {
    private final LinkedHashMap<String, PropertiesFunction> functions = new LinkedHashMap<>();

    public CamelPropertyEvaluator() {
        addFunction(new EnvPropertiesFunction());
        addFunction(new SysPropertiesFunction());
        addFunction(new ServicePropertiesFunction());
        addFunction(new ServiceHostPropertiesFunction());
        addFunction(new ServicePortPropertiesFunction());
    }

    private void addFunction(PropertiesFunction function) {
        functions.put(function.getName(), function);
    }

    @Override
    public String evaluate(String key, Dictionary<String, String> dictionary) {
        String[] resolvers = Support.before(key, ":").split("\\+");
        String remainder = Support.after(key, ":");
        String value = null;

        for (String resolver : resolvers) {
            PropertiesFunction function = functions.get(resolver);
            if (function == null) {
                value = null;
                break;
            }

            value = function.apply(remainder);
            if (value != null) {
                remainder = value;
            } else {
                break;
            }
        }

        return value != null ? value : dictionary.get(key);
    }



}
