package cloud.fogbow.ms.core;

import java.util.List;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.api.parameters.ProviderPermission;

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

    // TODO documentation
    public boolean isTargetAuthorized(String provider);

    // TODO documentation
    public boolean isRequesterAuthorized(String provider);

    // TODO documentation
    public void addMember(ProviderPermission permission) throws ConfigurationErrorException;
    
    // TODO documentation
    public void updateMember(ProviderPermission provider) throws ConfigurationErrorException;
   
    // TODO documentation
	public void removeMember(String provider) throws ConfigurationErrorException;
}
