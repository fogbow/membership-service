package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.List;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PropertiesHolder;

import cloud.fogbow.ms.MembershipService;

public class WhiteList implements MembershipService {

    private static final String SEPARATOR = ",";

    private List<String> membersList;

    public WhiteList() {
        this.membersList = readMembers();
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
}
