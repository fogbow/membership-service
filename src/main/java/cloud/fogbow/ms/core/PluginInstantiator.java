package cloud.fogbow.ms.core;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;
import cloud.fogbow.ms.core.plugins.authorization.DefaultAuthorizationPlugin;

public class PluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();
    
    public static AuthorizationPlugin getAuthorizationPlugin() throws ConfigurationErrorException {
        if (PropertiesHolder.getInstance().getProperties().containsKey(ConfigurationPropertyKeys.AUTHORIZATION_PLUGIN_CLASS_KEY)) {
            String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AUTHORIZATION_PLUGIN_CLASS_KEY);
            return (AuthorizationPlugin) PluginInstantiator.classFactory.createPluginInstance(className);
        } else {
            return new DefaultAuthorizationPlugin();
        }
    }
        
}
