package cloud.fogbow.ms.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.Role;
import cloud.fogbow.ms.core.models.operation.OperationType;
import cloud.fogbow.ms.core.models.permission.AllowOnlyPermission;
import cloud.fogbow.ms.core.models.role.DefaultRole;

public class RoleAttributionManager {
    private Map<String, Set<Role>> usersRoles;
    private Map<String, Role> availableRoles;
    
    public RoleAttributionManager() {
        this.availableRoles = new HashMap<String, Role>();
        this.usersRoles = new HashMap<String, Set<Role>>();
        
        String rolesNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ROLES_NAMES_KEY);
        
        for (String roleName : rolesNamesString.split(",")) {
            String permissionName = PropertiesHolder.getInstance().getProperty(roleName);
            String permissionType = PropertiesHolder.getInstance().getProperty(permissionName);
            
            // TODO create a permission instantiator
            if (permissionType.equals("AllowOnly")) {
                // TODO move this to constructor
                Set<OperationType> operations = new HashSet<OperationType>();
                String operationTypesString = PropertiesHolder.getInstance().getProperty(permissionName + "_operations");
                
                for (String operationString : operationTypesString.split(",")) {
                    operations.add(OperationType.fromString(operationString.trim()));
                }
                
                Permission p = new AllowOnlyPermission(operations);
                
                Role role = new DefaultRole(roleName, p);
                availableRoles.put(roleName, role);
            }
        }
        
        String userNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.USER_NAMES_KEY);
        
        for (String userName : userNamesString.split(",")) {
            String userRolesString = PropertiesHolder.getInstance().getProperty(userName);
            Set<Role> userRoles = new HashSet<Role>();
            
            for (String roleName : userRolesString.split(",")) {
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
