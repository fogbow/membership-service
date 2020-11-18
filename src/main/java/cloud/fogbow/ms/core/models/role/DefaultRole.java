package cloud.fogbow.ms.core.models.role;

import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.Role;

public class DefaultRole implements Role {

    private String name;
    private Permission permission;
    
    public DefaultRole(String name, Permission permission) {
        this.name = name;
        this.permission = permission;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canPerformOperation(AuthorizableOperation operation) {
        return permission.isAuthorized(operation);
    }
}
