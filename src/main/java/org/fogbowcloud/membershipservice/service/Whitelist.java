package org.fogbowcloud.membershipservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Whitelist implements MembershipService {

    private static final String PATH_CONF = "membership.conf";

    @Override
    public List<String> listMembers() throws IOException {
        Properties properties = new Properties();
        InputStream input = null;
        List<String> membersId = new ArrayList<>();

        try {
            input = new FileInputStream(PATH_CONF);
            properties.load(input);

            for (String memberName : properties.stringPropertyNames()) {
                String memberId = properties.getProperty(memberName);
                membersId.add(memberId);
            }
        } catch (IOException e) {
            throw new IOException("Conf file was not found.");
        } finally {
            if (input != null) {
                input.close();
            }
        }

        return membersId;
    }
}
