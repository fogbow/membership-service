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
    
    // TODO documentation
    public boolean isMember(String provider);
}
