package cloud.fogbow.ms.core.service;

import java.util.List;

import org.junit.Assert;
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
    private String emptyNotAllowedTargetsList = "";
    private String emptyNotAllowedRequestersList = "";

    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        setUpBlackListWithDefaultLists();
        
        List<String> membersId = this.service.listMembers();

        // verify
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsRequester));
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsTarget));
    }
    
    // test case: When invoking the isMember method, it must return whether or 
    // not the provider passed as argument is member, based on the configuration file.
    @Test
    public void testIsMember() throws ConfigurationErrorException {
        setUpBlackListWithDefaultLists();
        
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsRequester));
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isMember(notMember1));
        Assert.assertFalse(this.service.isMember(""));
    }
    
    // TODO: documentation
    @Test
    public void testIsTargetAuthorized() throws ConfigurationErrorException {
        setUpBlackListWithDefaultLists();
        
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // TODO: documentation
    @Test
    public void testIsRequesterAuthorized() throws ConfigurationErrorException {
        setUpBlackListWithDefaultLists();
        
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO: documentation
    @Test
    public void testIsTargetAuthorizedEmptyNotAllowedTargetsList() throws ConfigurationErrorException {
        setUpBlackListWithEmptyNotAllowedTargetsList();
        
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // TODO: documentation
    @Test
    public void testIsTargetAuthorizedEmptyNotAllowedRequestersList() throws ConfigurationErrorException {
        setUpBlackListWithEmptyNotAllowedRequestersList();
        
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    private void setUpBlackList(String membersListString, String notAllowedRequestersListString, String notAllowedTargetsListString) throws ConfigurationErrorException {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        Mockito.doReturn(notAllowedRequestersListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.NOT_AUTHORIZED_REQUESTER_MEMBERS_LIST_KEY);
        Mockito.doReturn(notAllowedTargetsListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.NOT_AUTHORIZED_TARGET_MEMBERS_LIST_KEY);
        
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
        
        this.service = new BlackList();
    }
    
    private void setUpBlackListWithDefaultLists() throws ConfigurationErrorException {
        setUpBlackList(membersListString, notAllowedRequestersList, notAllowedTargetsList);
    }
    
    private void setUpBlackListWithEmptyNotAllowedTargetsList() throws ConfigurationErrorException {
        setUpBlackList(membersListString, notAllowedRequestersList, emptyNotAllowedTargetsList);
    }
    
    private void setUpBlackListWithEmptyNotAllowedRequestersList() throws ConfigurationErrorException {
        setUpBlackList(membersListString, emptyNotAllowedRequestersList, notAllowedTargetsList);
    }
}
