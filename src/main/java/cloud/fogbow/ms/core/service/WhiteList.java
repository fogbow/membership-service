package cloud.fogbow.ms.core.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.ms.MembershipService;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.constants.Messages;
import cloud.fogbow.ms.constants.SystemConstants;

public class WhiteList implements MembershipService {

    private static final Logger LOGGER = Logger.getLogger(WhiteList.class);
    private static final String SEPARATOR = ",";

    private List<String> membersList;

    public WhiteList(String membershipConfPath) throws FileNotFoundException {
        this.membersList = readMembersFromFile(membershipConfPath);
    }

    public WhiteList() throws FileNotFoundException {
        this.membersList = readMembersFromFile(SystemConstants.CONF_FILE_NAME);
    }

    /**
     * Read list of XMPP members ID from membership config file.
     */
    @Override
    public List<String> listMembers() {
        return this.membersList;
    }

    private List<String> readMembersFromFile(String membershipConfPath) throws FileNotFoundException {
        InputStream input = loadFileInputStream(membershipConfPath);
        Properties properties = new Properties();
        List<String> membersList = new ArrayList<>();

        try {
            properties.load(input);

            String membersListStr = properties.getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
            for (String member : membersListStr.split(SEPARATOR)) {
                member = member.trim();
                membersList.add(member);
            }
        } catch (Exception e) {
            LOGGER.warn(String.format(Messages.Warn.ERROR_READING_CONF_FILE, membershipConfPath), e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                LOGGER.warn(String.format(Messages.Warn.ERROR_CLOSING_CONF_FILE, membershipConfPath), e);
            }
        }
        return membersList;
    }

    private InputStream loadFileInputStream(String membershipConfPath) throws FileNotFoundException {
        InputStream inputStream = null;
        String path = HomeDir.getPath();
        try {
            inputStream = new FileInputStream(path + membershipConfPath);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format(Messages.Error.CONFIGURATION_FILE_NOT_FOUND, membershipConfPath), e);
        }
        return inputStream;
    }
}
