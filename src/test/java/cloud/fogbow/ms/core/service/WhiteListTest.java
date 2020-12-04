package cloud.fogbow.ms.core.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.PropertiesHolder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertiesHolder.class})
public class WhiteListTest {

    private MembershipService service;

    private String member1 = "member1";
    private String member2 = "member2";
    private String member3 = "member3";
    private String notMember1 = "notMember1";
    private String membersListString = String.join(",", member1, member2, member3);
    
    @Before
    public void setUp() {
        setUpConfigurationInfo();
        this.service = new WhiteList();
    }

    private void setUpConfigurationInfo() {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
    }
    
    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        List<String> membersId = this.service.listMembers();

        // verify
        Assert.assertTrue(membersId.contains(member1));
        Assert.assertTrue(membersId.contains(member2));
        Assert.assertTrue(membersId.contains(member3));
    }
    
    // test case: When invoking the isMember method, it must return whether or 
    // not the provider passed as argument is member, based on the configuration file.
    @Test
    public void testIsMember() {
        Assert.assertTrue(this.service.isMember(member1));
        Assert.assertTrue(this.service.isMember(member2));
        Assert.assertTrue(this.service.isMember(member3));
        Assert.assertFalse(this.service.isMember(notMember1));
    }
}
