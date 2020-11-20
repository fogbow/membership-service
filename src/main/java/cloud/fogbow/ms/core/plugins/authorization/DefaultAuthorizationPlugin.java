package cloud.fogbow.ms.core.plugins.authorization;

import java.util.List;

import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.RoleManager;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;

public class DefaultAuthorizationPlugin implements AuthorizationPlugin {

    private MembershipService membership;
    private RoleManager roleManager;
    private String localProvider;
    
    public DefaultAuthorizationPlugin(MembershipService membershipService, 
            RoleManager roleManager, String localProviderId) {
        this.membership = membershipService;
        this.roleManager = roleManager;
        this.localProvider = localProviderId;
    }
    
    @Override
    public boolean isAuthorized(SystemUser user, AuthorizableOperation operation) {
        String targetProvider = operation.getTargetProvider();
        
        Boolean isProviderAuthorized = false;
        Boolean isUserAuthorized = false;
        
        if (userIsLocal(user)) {
            isProviderAuthorized = isLocalProviderAuthorizedToRequestFromTarget(targetProvider, operation);
            isUserAuthorized = roleManager.isUserAuthorized(user.getId(), operation);
            // TODO add roles to user token
        } else {
            if (providerIsLocal(targetProvider)) {
                isProviderAuthorized = isProviderAuthorized(user.getIdentityProviderId(), operation);
                isUserAuthorized = roleManager.isUserAuthorized(user.getId(), operation);
            } else {
                // TODO should throw an exception
            }
        }

        return isProviderAuthorized && isUserAuthorized;
    }

    private boolean userIsLocal(SystemUser user) {
        return providerIsLocal(user.getIdentityProviderId());
    }
    
    private boolean providerIsLocal(String provider) {
        return provider.equals(this.localProvider);
    }

    private boolean isLocalProviderAuthorizedToRequestFromTarget(String targetProvider, AuthorizableOperation operation) {
        if (!providerIsLocal(targetProvider)) {
            return isProviderAuthorized(targetProvider, operation);
        }
        
        return true;
    }
    
    private boolean isProviderAuthorized(String provider, AuthorizableOperation operation) {
        return membership.isMember(provider) && membership.canPerformOperation(operation);
    }
    
    @Override
    public List<String> listMembers() throws Exception {
        return membership.listMembers();
    }
}
