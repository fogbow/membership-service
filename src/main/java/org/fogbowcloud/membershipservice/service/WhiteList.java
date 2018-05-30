package org.fogbowcloud.membershipservice.service;

import org.fogbowcloud.membershipservice.MembershipService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WhiteList implements MembershipService {

    private String membershipConfPath = "membership.conf";

    public WhiteList(String membershipConfPath) {
        this.membershipConfPath = membershipConfPath;
    }

    public WhiteList() {}

    /**
     * Read list of XMPP members ID from membership config file.
     *
     * @throws IOException Config file not found or badly-formatted file.
     */
    @Override
    public List<String> listMembers() throws IOException {
        Properties properties = new Properties();
        InputStream input = null;
        List<String> membersId = new ArrayList<>();

        try {
            input = new FileInputStream(this.membershipConfPath);
            properties.load(input);

            for (String memberName : properties.stringPropertyNames()) {
                String memberId = properties.getProperty(memberName);
                membersId.add(memberId);
            }
        } catch (IOException e) {
            throw new IOException("Membership config file was not found.");
        } finally {
            if (input != null) {
                input.close();
            }
        }

        return membersId;
    }
}
