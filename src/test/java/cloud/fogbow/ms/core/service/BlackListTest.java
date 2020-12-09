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

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.PropertiesHolder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertiesHolder.class})
public class BlackListTest {

    private MembershipService service;

    private String memberNotAuthorizedAsRequesterAndTarget = "requesterAndTarget";
    private String memberNotAuthorizedAsRequester = "requester";
    private String memberNotAuthorizedAsTarget = "target";
    private String notMember1 = "notMember1";
    private String membersListString = String.join(",", memberNotAuthorizedAsRequesterAndTarget, 
                                        memberNotAuthorizedAsRequester, memberNotAuthorizedAsTarget);
    private String notAllowedRequestersList = String.join(",", memberNotAuthorizedAsRequester, 
                                        memberNotAuthorizedAsRequesterAndTarget);
    private String notAllowedTargetsList = String.join(",", memberNotAuthorizedAsTarget, 
                                        memberNotAuthorizedAsRequesterAndTarget);
    
    @Before
    public void setUp() throws ConfigurationErrorException {
        setUpConfigurationInfo();
        this.service = new BlackList();
    }

    private void setUpConfigurationInfo() {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        Mockito.doReturn(notAllowedRequestersList).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.NOT_AUTHORIZED_REQUESTER_MEMBERS_LIST_KEY);
        Mockito.doReturn(notAllowedTargetsList).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.NOT_AUTHORIZED_TARGET_MEMBERS_LIST_KEY);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
    }
    
    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        List<String> membersId = this.service.listMembers();

        // verify
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsRequester));
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsTarget));
    }
    
    // test case: When invoking the isMember method, it must return whether or 
    // not the provider passed as argument is member, based on the configuration file.
    @Test
    public void testIsMember() {
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsRequester));
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isMember(notMember1));
    }
    
    // TODO: documentation
    @Test
    public void testIsTargetAuthorized() {
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
    }
    
    // TODO: documentation
    @Test
    public void testIsRequesterAuthorized() {
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
    }

}
