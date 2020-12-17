package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.List;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.constants.Messages;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.PropertiesHolder;

public abstract class MembershipListService implements MembershipService {

    protected static final String SEPARATOR = ",";

    protected List<String> membersList;
    protected List<String> targetMembers;
    protected List<String> requesterMembers;

    public abstract List<String> listMembers() throws Exception;
    public abstract boolean isMember(String provider);
    public abstract boolean isTargetAuthorized(String provider);
    public abstract boolean isRequesterAuthorized(String provider);
    
    protected List<String> readMembers() {
        List<String> membersList = new ArrayList<>();

        String membersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        for (String member : membersListStr.split(SEPARATOR)) {
            member = member.trim();
            membersList.add(member);
        }

        return membersList;
    }
    
    protected List<String> readTargetMembers() throws ConfigurationErrorException {
        List<String> authorizedTargetMembers = new ArrayList<String>();
        
        String authorizedTargetMembersListStr = PropertiesHolder.getInstance().
        		getProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY);
        
        if (!authorizedTargetMembersListStr.isEmpty()) {
            for (String member : authorizedTargetMembersListStr.split(SEPARATOR)) {
                member = member.trim();
                
                checkProviderIsMember(member);
                authorizedTargetMembers.add(member);
            }
        }
        
        return authorizedTargetMembers;
    }
    
    protected List<String> readRequesterMembers() throws ConfigurationErrorException {
        List<String> authorizedRequesterMembers = new ArrayList<String>();
        
        String authorizedRequesterMembersListStr = PropertiesHolder.getInstance().
        		getProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY);
        
        if (!authorizedRequesterMembersListStr.isEmpty()) {
            for (String member : authorizedRequesterMembersListStr.split(SEPARATOR)) {
                member = member.trim();
                
                checkProviderIsMember(member);
                authorizedRequesterMembers.add(member);
            }
        }
        
        return authorizedRequesterMembers;
    }

	private void checkProviderIsMember(String member) throws ConfigurationErrorException {
		if (!isMember(member)) {
		    throw new ConfigurationErrorException(Messages.Exception.INVALID_MEMBER_NAME);
		}
	}
    
    @Override
    public void addMember(String provider) throws ConfigurationErrorException {
    	if (isMember(provider)) {
    		throw new ConfigurationErrorException(Messages.Exception.PROVIDER_IS_ALREADY_A_MEMBER);
    	}
    	
    	membersList.add(provider);
    	
    	String newMembersString = String.join(SEPARATOR, membersList);
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY, 
    			newMembersString);
    	PropertiesHolder.getInstance().updatePropertiesFile();
    }
    
	@Override
	public void removeMember(String provider) throws ConfigurationErrorException {
		checkProviderIsMember(provider);
		
		targetMembers.remove(provider);
		requesterMembers.remove(provider);
		membersList.remove(provider);
		
		String newTargetMembersList = String.join(SEPARATOR, targetMembers);
		String newRequesterMembersList = String.join(SEPARATOR, requesterMembers);
		String newMembersString = String.join(SEPARATOR, membersList);
		
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY, 
    			newTargetMembersList);
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY, 
    			newRequesterMembersList);
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY, 
    			newMembersString);
    	
    	PropertiesHolder.getInstance().updatePropertiesFile();
	}

	@Override
	public void addTarget(String provider) throws ConfigurationErrorException {
        checkProviderIsMember(provider);
        
        if (targetMembers.contains(provider)) {
        	throw new ConfigurationErrorException(Messages.Exception.PROVIDER_IS_ALREADY_A_TARGET);
        }
        
        targetMembers.add(provider);
        updateTargetsListOnPropertiesFile();
    }

	@Override
	public void addRequester(String provider) throws ConfigurationErrorException {
        checkProviderIsMember(provider);
        
        if (requesterMembers.contains(provider)) {
        	throw new ConfigurationErrorException(Messages.Exception.PROVIDER_IS_ALREADY_A_REQUESTER);
        }
        
        requesterMembers.add(provider);
        updateRequestersListOnPropertiesFile();
    }
	
	@Override
	public void removeTarget(String provider) throws ConfigurationErrorException {
		if (!targetMembers.contains(provider)) {
			throw new ConfigurationErrorException(Messages.Exception.MEMBER_IS_NOT_TARGET);
		}
		
		targetMembers.remove(provider);
        updateTargetsListOnPropertiesFile();
	}

	@Override
	public void removeRequester(String provider) throws ConfigurationErrorException {
		if (!requesterMembers.contains(provider)) {
			throw new ConfigurationErrorException(Messages.Exception.MEMBER_IS_NOT_REQUESTER);
		}
		
		requesterMembers.remove(provider);
        updateRequestersListOnPropertiesFile();
	}
	
	private void updateTargetsListOnPropertiesFile() {
		String newTargetMembersList = String.join(SEPARATOR, targetMembers);
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY, 
    			newTargetMembersList);
    	PropertiesHolder.getInstance().updatePropertiesFile();
	}
	
	private void updateRequestersListOnPropertiesFile() {
		String newRequesterMembersList = String.join(SEPARATOR, requesterMembers);
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY, 
    			newRequesterMembersList);
    	PropertiesHolder.getInstance().updatePropertiesFile();
	}

}
