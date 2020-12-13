package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.List;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.constants.Messages;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.PropertiesHolder;

public class AllowList implements MembershipService {

    private static final String SEPARATOR = ",";

    private List<String> membersList;
    private List<String> authorizedTargetMembers;
    private List<String> authorizedRequesterMembers;
    
    public AllowList() throws ConfigurationErrorException {
        this.membersList = readMembers();
        this.authorizedTargetMembers = readAuthorizedTargetMembers();
        this.authorizedRequesterMembers = readAuthorizedRequesterMembers();
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
    
    private List<String> readAuthorizedTargetMembers() throws ConfigurationErrorException {
        List<String> authorizedTargetMembers = new ArrayList<String>();
        
        String authorizedTargetMembersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AUTHORIZED_TARGET_MEMBERS_LIST_KEY);
        
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
    
    private List<String> readAuthorizedRequesterMembers() throws ConfigurationErrorException {
        List<String> authorizedRequesterMembers = new ArrayList<String>();
        
        String authorizedRequesterMembersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AUTHORIZED_REQUESTER_MEMBERS_LIST_KEY);
        
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
    public boolean isMember(String provider) {
        return this.membersList.contains(provider);
    }

    @Override
    public boolean isTargetAuthorized(String provider) {
        return this.authorizedTargetMembers.contains(provider);
    }

    @Override
    public boolean isRequesterAuthorized(String provider) {
        return this.authorizedRequesterMembers.contains(provider);
    }
}
