package cloud.fogbow.ms.core;

import cloud.fogbow.ms.core.models.Permission;

public class PermissionInstantiator {

    private ClassFactory classFactory;
    
    public PermissionInstantiator() {
        this.classFactory = new ClassFactory();
    }
    
    public Permission getPermissionInstance(String type, String ... params) {
        return (Permission) this.classFactory.createPluginInstance(type, params);
    }
}
