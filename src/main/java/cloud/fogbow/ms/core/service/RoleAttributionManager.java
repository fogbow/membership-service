package cloud.fogbow.ms.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.Role;
import cloud.fogbow.ms.core.models.role.DefaultRole;

public class RoleAttributionManager {
    private Map<String, Set<Role>> usersRoles;
    private Map<String, Role> availableRoles;
    
    public RoleAttributionManager(PermissionInstantiator permissionInstantiator) {
        this.availableRoles = new HashMap<String, Role>();
        this.usersRoles = new HashMap<String, Set<Role>>();
        
        String rolesNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ROLES_NAMES_KEY);
        
        for (String roleName : rolesNamesString.split(SystemConstants.ROLE_NAME_SEPARATOR)) {
            String permissionName = PropertiesHolder.getInstance().getProperty(roleName);
            String permissionType = PropertiesHolder.getInstance().getProperty(permissionName);
            
            Permission permission = permissionInstantiator.getPermissionInstance(permissionType, permissionName);
            Role role = new DefaultRole(roleName, permission);
            availableRoles.put(roleName, role);
        }
        
        String userNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.USER_NAMES_KEY);
        
        for (String userName : userNamesString.split(SystemConstants.USER_NAME_SEPARATOR)) {
            String userRolesString = PropertiesHolder.getInstance().getProperty(userName);
            Set<Role> userRoles = new HashSet<Role>();
            
            for (String roleName : userRolesString.split(SystemConstants.USER_ROLES_SEPARATOR)) {
                userRoles.add(this.availableRoles.get(roleName));
            }
            this.usersRoles.put(userName, userRoles);
        }
    }
    
    public boolean isUserAuthorized(String userId, AuthorizableOperation operation) {
        Set<Role> userRoles = usersRoles.get(userId);
        
        for (Role role : userRoles) {
            if (role.canPerformOperation(operation)) {
                return true;
            }
        }
        return false;
    }

}
