package cloud.fogbow.ms.core;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.service.WhiteList;

public class PluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();
    
    public static MembershipService getMembershipService() throws ConfigurationErrorException {
        if (PropertiesHolder.getInstance().getProperties().containsKey(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_CLASS_KEY)) {
            String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_CLASS_KEY);
            return (MembershipService) PluginInstantiator.classFactory.createPluginInstance(className);
        } else {
            return new WhiteList();
        }
    }
        
}
