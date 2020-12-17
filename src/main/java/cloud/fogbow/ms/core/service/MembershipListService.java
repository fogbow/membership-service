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
	
    protected List<String> readMembers() {
        List<String> membersList = new ArrayList<>();

        String membersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        for (String member : membersListStr.split(SEPARATOR)) {
            member = member.trim();
            membersList.add(member);
        }

        return membersList;
    }
    
    protected List<String> readTargetMembers(String targetMembersListPropertyKey) throws ConfigurationErrorException {
        List<String> authorizedTargetMembers = new ArrayList<String>();
        
        String authorizedTargetMembersListStr = PropertiesHolder.getInstance().getProperty(targetMembersListPropertyKey);
        
        if (!authorizedTargetMembersListStr.isEmpty()) {
            for (String member : authorizedTargetMembersListStr.split(SEPARATOR)) {
                member = member.trim();
                
                if (!this.membersList.contains(member)) {
                    throw new ConfigurationErrorException(Messages.Exception.INVALID_MEMBER_NAME);
                }
                
                authorizedTargetMembers.add(member);
            }
        }
        
        return authorizedTargetMembers;
    }
    
    protected List<String> readRequesterMembers(String requesterMembersListPropertyKey) throws ConfigurationErrorException {
        List<String> authorizedRequesterMembers = new ArrayList<String>();
        
        String authorizedRequesterMembersListStr = PropertiesHolder.getInstance().getProperty(requesterMembersListPropertyKey);
        
        if (!authorizedRequesterMembersListStr.isEmpty()) {
            for (String member : authorizedRequesterMembersListStr.split(SEPARATOR)) {
                member = member.trim();
                
                if (!this.membersList.contains(member)) {
                    throw new ConfigurationErrorException(Messages.Exception.INVALID_MEMBER_NAME);
                }
                
                authorizedRequesterMembers.add(member);
            }
        }
        
        return authorizedRequesterMembers;
    }
    
    @Override
    public void addMember(String provider) {
    	// TODO add provider name validation
    	membersList.add(provider);
    	
    	String newMembersString = String.join(SEPARATOR, membersList);
    	
    	PropertiesHolder.getInstance().setProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY, 
    			newMembersString);
    	
    	PropertiesHolder.getInstance().updatePropertiesFile();
    }
    
    protected void addTargetMember(String provider, String targetMembersListPropertyKey) throws ConfigurationErrorException {
    	
        if (!this.membersList.contains(provider)) {
            throw new ConfigurationErrorException(Messages.Exception.INVALID_MEMBER_NAME);
        }
        
        targetMembers.add(provider);
        
        String newTargetMembersList = String.join(SEPARATOR, targetMembers);
        
    	PropertiesHolder.getInstance().setProperty(targetMembersListPropertyKey, 
    			newTargetMembersList);
    	
    	PropertiesHolder.getInstance().updatePropertiesFile();
    }
    
    protected void addRequesterMember(String provider, String requesterMembersListPropertyKey) throws ConfigurationErrorException {
    	
        if (!this.membersList.contains(provider)) {
            throw new ConfigurationErrorException(Messages.Exception.INVALID_MEMBER_NAME);
        }
        
        requesterMembers.add(provider);
        
        String newRequesterMembersList = String.join(SEPARATOR, requesterMembers);
        
    	PropertiesHolder.getInstance().setProperty(requesterMembersListPropertyKey, 
    			newRequesterMembersList);
    	
    	PropertiesHolder.getInstance().updatePropertiesFile();
    }

}
