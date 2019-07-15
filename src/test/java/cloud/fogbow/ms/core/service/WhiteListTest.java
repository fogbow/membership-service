package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cloud.fogbow.ms.MembershipService;

public class WhiteListTest {

	private MembershipService service;

	private static final String VALID_CONF = "ms-test.conf";
	private static final String INVALID_CONF = "invalid.conf";

	// test case: When invoking the listMembers method from an instance created with
	// the MembershipService class constructor with a valid parameter, it must list
	// the configured membership in the file passed by parameter.
	@Test
	public void testListMembersWithValidConfPath() throws Exception {
		// set up
		List<String> result = new ArrayList<>();
		result.add("xmpp-id1");
		result.add("xmpp-id2");
		result.add("xmpp-id3");
		result.add("xmpp-id4");

		// exercise
		this.service = new WhiteList(VALID_CONF);
		List<String> membersId = this.service.listMembers();

		// verify
		Assert.assertTrue(membersId.contains(result.get(0)));
		Assert.assertTrue(membersId.contains(result.get(1)));
		Assert.assertTrue(membersId.contains(result.get(2)));
		Assert.assertTrue(membersId.contains(result.get(3)));
	}

	// test case: When invoking the listMembers method from an instance created with
	// the MembershipService class constructor with an invalid parameter, it must
	// return an empty list.
	@Test
	public void testListMembersWithInvalidConfPath() throws Exception {
		// exercise
		this.service = new WhiteList(INVALID_CONF);

		// verify
		Assert.assertTrue(this.service.listMembers().isEmpty());
	}
	
	// test case: When invoking the listMembers method from an instance created with
	// the default constructor of the MembershipService class with no parameters, it
	// must list the configured membership in the default file.
	@Test
	public void testListMembersWithDefaultConstructor() throws Exception {
		// set up
		List<String> result = new ArrayList<>();
		result.add("member1");
		result.add("member2");
		result.add("member3");

		// exercise
		this.service = new WhiteList();
		List<String> membersId = this.service.listMembers();

		// verify
		Assert.assertTrue(membersId.contains(result.get(0)));
		Assert.assertTrue(membersId.contains(result.get(1)));
		Assert.assertTrue(membersId.contains(result.get(2)));
	}
	
}
