package cloud.fogbow.ms.core.service;

import cloud.fogbow.ms.MembershipService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class WhiteListTest {

    private MembershipService validMembershipService;
    private MembershipService invalidMembershipService;

    private String VALID_CONF = "ms-test.conf";
    private String INVALID_CONF = "invalid.conf";

    @Before
    public void setUp() throws FileNotFoundException {
        this.validMembershipService = new WhiteList(VALID_CONF);
    }

    @Test
    public void testListMembers() throws Exception {
        List<String> membersId = this.validMembershipService.listMembers();

        List<String> result = new ArrayList<>();
        result.add("xmpp-id1");
        result.add("xmpp-id2");
        result.add("xmpp-id3");
        result.add("xmpp-id4");

        Assert.assertTrue(membersId.contains(result.get(0)));
        Assert.assertTrue(membersId.contains(result.get(1)));
        Assert.assertTrue(membersId.contains(result.get(2)));
        Assert.assertTrue(membersId.contains(result.get(3)));
    }

    @Test(expected = Exception.class)
    public void testListMembersWithInvalidConfPath() throws Exception {
        this.invalidMembershipService = new WhiteList(INVALID_CONF);
        this.invalidMembershipService.listMembers();
    }
}
