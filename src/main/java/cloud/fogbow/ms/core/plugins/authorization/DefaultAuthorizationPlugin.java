package cloud.fogbow.ms.core.plugins.authorization;

import java.util.List;

import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;

public class DefaultAuthorizationPlugin implements AuthorizationPlugin {

    private MembershipService membership;
    
    public DefaultAuthorizationPlugin(MembershipService membershipService) {
        this.membership = membershipService;
    }
    
    @Override
    public List<String> listMembers() throws Exception {
        return membership.listMembers();
    }

    @Override
    public boolean isAuthorized(String provider) {
        return membership.isMember(provider);
    }
}
