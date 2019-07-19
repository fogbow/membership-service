package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cloud.fogbow.ms.MembershipService;

public class WhiteListTest {

    private MembershipService service;

    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        // set up
        List<String> result = new ArrayList<>();
        result.add("xmpp-id1");
        result.add("xmpp-id2");
        result.add("xmpp-id3");
        result.add("xmpp-id4");

        // exercise
        this.service = new WhiteList();
        List<String> membersId = this.service.listMembers();

        // verify
        Assert.assertTrue(membersId.contains(result.get(0)));
        Assert.assertTrue(membersId.contains(result.get(1)));
        Assert.assertTrue(membersId.contains(result.get(2)));
        Assert.assertTrue(membersId.contains(result.get(3)));
    }
}
