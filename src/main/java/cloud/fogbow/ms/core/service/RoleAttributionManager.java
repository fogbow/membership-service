package cloud.fogbow.ms.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.constants.Messages;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.RoleManager;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.Role;
import cloud.fogbow.ms.core.models.role.DefaultRole;

public class RoleAttributionManager implements RoleManager {
    private Map<String, Set<Role>> usersRoles;
    private Map<String, Role> availableRoles;
    // This field was defined as a Set instead of 
    // a Role to simplify some of the authorization code
    // and to not need to recreate this object on every 
    // authorization call.
    private HashSet<Role> defaultRoles;
    
    public RoleAttributionManager(PermissionInstantiator permissionInstantiator) throws ConfigurationErrorException {
        setUpAvailableRoles(permissionInstantiator);
        setUpUsersRoles();
        setUpDefaultRole();
    }

    private void setUpAvailableRoles(PermissionInstantiator permissionInstantiator) {
        this.availableRoles = new HashMap<String, Role>();
        String rolesNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ROLES_NAMES_KEY);
        
        for (String roleName : rolesNamesString.split(SystemConstants.ROLE_NAME_SEPARATOR)) {
            String permissionName = PropertiesHolder.getInstance().getProperty(roleName);
            String permissionType = PropertiesHolder.getInstance().getProperty(permissionName);
            
            Permission permission = permissionInstantiator.getPermissionInstance(permissionType, permissionName);
            Role role = new DefaultRole(roleName, permission);
            this.availableRoles.put(roleName, role);
        }
    }
    
    private void setUpUsersRoles() {
        this.usersRoles = new HashMap<String, Set<Role>>();
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
    
    private void setUpDefaultRole() throws ConfigurationErrorException {
        String defaultRoleName = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.DEFAULT_ROLE_KEY);
        
        if (availableRoles.containsKey(defaultRoleName)) {
            this.defaultRoles = new HashSet<Role>();
            Role defaultRole = availableRoles.get(defaultRoleName);
            this.defaultRoles.add(defaultRole);
        } else {
            throw new ConfigurationErrorException(Messages.Exception.DEFAULT_ROLE_NAME_IS_INVALID);
        }
    }
    
    @Override
    public boolean isUserAuthorized(SystemUser user, AuthorizableOperation operation) {
        String userId = user.getId();
        Set<Role> userRoles;
        
        if (usersRoles.containsKey(userId)) {
            userRoles = usersRoles.get(userId);
        } else {
            userRoles = defaultRoles;
        }
        
        for (Role role : userRoles) {
            if (role.canPerformOperation(operation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRemoteUserAuthorized(SystemUser user, AuthorizableOperation operation) {
        for (String tokenRole : user.getUserRoles()) {
            if (availableRoles.containsKey(tokenRole)) {
                Role role = availableRoles.get(tokenRole);
                
                if (role.canPerformOperation(operation)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setRoles(SystemUser user) {
        Set<String> tokenRoles = new HashSet<String>();
        
        for (Role role : usersRoles.get(user.getId())) {
            tokenRoles.add(role.getName());
        }
        
        user.setUserRoles(tokenRoles);
    }

}