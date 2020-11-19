package cloud.fogbow.ms.core.plugins.authorization;

import java.util.List;

import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ms.MembershipService;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;
import cloud.fogbow.ms.core.service.RoleAttributionManager;
import cloud.fogbow.ms.core.service.WhiteList;

public class DefaultAuthorizationPlugin implements AuthorizationPlugin {

    private MembershipService membership;
    private RoleAttributionManager roleManager;
    
    public DefaultAuthorizationPlugin() {
        this.membership = new WhiteList(new PermissionInstantiator());
        this.roleManager = new RoleAttributionManager(new PermissionInstantiator());
    }
    
    @Override
    public boolean isAuthorized(SystemUser user, AuthorizableOperation operation) {
        String provider = operation.getTargetProvider();
        
        if (!membership.isMember(provider)) {
            return false;
        }
        
        if (!membership.canPerformOperation(operation)) {
            return false;
        }
        
        return roleManager.isUserAuthorized(user.getId(), operation);
    }

    @Override
    public List<String> listMembers() throws Exception {
        return membership.listMembers();
    }
}
