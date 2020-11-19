package cloud.fogbow.ms.core;

import java.util.List;

import cloud.fogbow.ms.core.models.AuthorizableOperation;

// TODO move this to core package
public interface MembershipService {

    /**
     * This method returns a list of XMPP members ID.
     *
     * @return List of string with XMPP members ID.
     * @throws Exception
     */
    public List<String> listMembers() throws Exception;
    
    public boolean isMember(String provider);
    
    public boolean canPerformOperation(AuthorizableOperation operation);
}
