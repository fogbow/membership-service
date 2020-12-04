package cloud.fogbow.ms.core;

import java.util.List;

public interface MembershipService {

    /**
     * This method returns a list of XMPP members ID.
     *
     * @return List of string with XMPP members ID.
     * @throws Exception
     */
    public List<String> listMembers() throws Exception;
    
    /**
     * Returns whether or not the given provider is in 
     * the list of known providers.
     * 
     * @param provider
     * @return a boolean value stating whether the given 
     * provider is a member or not.
     */
    public boolean isMember(String provider);
}
