package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.List;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.PropertiesHolder;

public class BlackList implements MembershipService {

    private static final String SEPARATOR = ",";

    private List<String> membersList;
    private List<String> notAuthorizedTargetMembers;
    private List<String> notAuthorizedRequesterMembers;
    
    public BlackList() throws ConfigurationErrorException {
        this.membersList = readMembers();
        this.notAuthorizedTargetMembers = readNotAuthorizedTargetMembers();
        this.notAuthorizedRequesterMembers = readNotAuthorizedRequesterMembers();
    }

    /**
     * Read list of XMPP members ID from membership config file.
     */
    @Override
    public List<String> listMembers() {
        return this.membersList;
    }

    private List<String> readMembers() {
        List<String> membersList = new ArrayList<>();

        String membersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        for (String member : membersListStr.split(SEPARATOR)) {
            member = member.trim();
            membersList.add(member);
        }

        return membersList;
    }
    
    private List<String> readNotAuthorizedTargetMembers() throws ConfigurationErrorException {
        List<String> notAuthorizedTargetMembers = new ArrayList<String>();
        
        String notAuthorizedTargetMembersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.NOT_AUTHORIZED_TARGET_MEMBERS_LIST_KEY);
        for (String member : notAuthorizedTargetMembersListStr.split(SEPARATOR)) {
            member = member.trim();
            
            if (!this.membersList.contains(member)) {
                // TODO add message
                throw new ConfigurationErrorException();
            }
            
            notAuthorizedTargetMembers.add(member);
        }
        
        return notAuthorizedTargetMembers;
    }
    
    private List<String> readNotAuthorizedRequesterMembers() throws ConfigurationErrorException {
        List<String> notAuthorizedTargetMembers = new ArrayList<String>();
        
        String notAuthorizedRequesterMembersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.NOT_AUTHORIZED_REQUESTER_MEMBERS_LIST_KEY);
        for (String member : notAuthorizedRequesterMembersListStr.split(SEPARATOR)) {
            member = member.trim();
            
            if (!this.membersList.contains(member)) {
                // TODO add message
                throw new ConfigurationErrorException();
            }
            
            notAuthorizedTargetMembers.add(member);
        }
        
        return notAuthorizedTargetMembers;
    }

    @Override
    public boolean isMember(String provider) {
        return this.membersList.contains(provider);
    }

    @Override
    public boolean isTargetAuthorized(String provider) {
        return isMember(provider) && !this.notAuthorizedTargetMembers.contains(provider);
    }

    @Override
    public boolean isRequesterAuthorized(String provider) {
        return isMember(provider) && !this.notAuthorizedRequesterMembers.contains(provider);
    }

}
