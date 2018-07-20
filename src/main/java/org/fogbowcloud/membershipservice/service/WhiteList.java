package org.fogbowcloud.membershipservice.service;

import org.fogbowcloud.membershipservice.MembershipService;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WhiteList implements MembershipService {

    private static final Logger LOGGER = Logger.getLogger(WhiteList.class);

    private final String defaultMembershipConfPath = "membership.conf";

    private List<String> membersList;
    
    public static final String MEMBERS_LIST = "members_list";

    public WhiteList(String membershipConfPath) throws FileNotFoundException {
        this.membersList = readMembersFromFile(membershipConfPath);
    }

    public WhiteList() throws FileNotFoundException {
        this.membersList = readMembersFromFile(this.defaultMembershipConfPath);
    }

    /**
     * Read list of XMPP members ID from membership config file.
     *
     */
    @Override
    public List<String> listMembers() {
        return this.membersList;
    }

    private List<String> readMembersFromFile(String membershipConfPath) throws FileNotFoundException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(membershipConfPath);
        List<String> membersList = new ArrayList<>();

        try {
            properties.load(input);

            String membersListStr = properties.getProperty(MEMBERS_LIST);
            for (String member : membersListStr.split(",")) {
                member = member.trim();
                membersList.add(member);
            }
        } catch (IOException e) {
            LOGGER.warn("Error trying to read configuration file found: " + membershipConfPath, e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                LOGGER.warn("Could not close configuration file: " + membershipConfPath, e);
            }
        }

        return membersList;
    }
}
