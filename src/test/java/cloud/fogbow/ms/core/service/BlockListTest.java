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
public class BlockListTest {

    private MembershipService service;

    private String memberNotAuthorizedAsRequesterAndTarget = "requesterAndTarget";
    private String memberNotAuthorizedAsRequester = "requester";
    private String memberNotAuthorizedAsTarget = "target";
    private String notMember1 = "notMember1";
    private String newMember = "newMember";
    
    private String membersListString = String.join(",", memberNotAuthorizedAsRequesterAndTarget, 
                                        memberNotAuthorizedAsRequester, memberNotAuthorizedAsTarget);
    private String notAllowedRequestersList = String.join(",", memberNotAuthorizedAsRequester, 
                                        memberNotAuthorizedAsRequesterAndTarget);
    private String notAllowedTargetsList = String.join(",", memberNotAuthorizedAsTarget, 
                                        memberNotAuthorizedAsRequesterAndTarget);
    
    private String updatedMembersListString = String.join(",", memberNotAuthorizedAsRequesterAndTarget, 
            memberNotAuthorizedAsRequester, memberNotAuthorizedAsTarget, newMember);
    
    private String targetsListBeforeAdd = memberNotAuthorizedAsRequesterAndTarget;
    private String targetsListAfterAdd = String.join(",", memberNotAuthorizedAsRequesterAndTarget, 
    									memberNotAuthorizedAsTarget);
    
    private String requestersListBeforeAdd = memberNotAuthorizedAsRequesterAndTarget;
    private String requestersListAfterAdd = String.join(",", memberNotAuthorizedAsRequesterAndTarget, 
    									memberNotAuthorizedAsRequester);
    
	private String targetsListBeforeRemove = String.join(",", memberNotAuthorizedAsTarget,
										memberNotAuthorizedAsRequesterAndTarget);
	private String targetsListAfterRemove = memberNotAuthorizedAsRequesterAndTarget;

	private String requestersListBeforeRemove = String.join(",", memberNotAuthorizedAsRequester,
										memberNotAuthorizedAsRequesterAndTarget);
	private String requestersListAfterRemove = memberNotAuthorizedAsRequesterAndTarget;
    
    private String emptyNotAllowedTargetsList = "";
    private String emptyNotAllowedRequestersList = "";

    private PropertiesHolder propertiesHolder;
    
    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        setUpBlockListWithDefaultLists();
        
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
        setUpBlockListWithDefaultLists();
        
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsRequester));
        Assert.assertTrue(this.service.isMember(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isMember(notMember1));
        Assert.assertFalse(this.service.isMember(""));
    }
    
    // test case: When invoking the isTargetAuthorized method, it must return 
    // whether or not local users are allowed to perform operations in the 
    // remote provider passed as argument. In the case of BlockList implementation,
    // it must return whether or not the provider is in the "not-allowed" list.
    @Test
    public void testIsTargetAuthorized() throws ConfigurationErrorException {
        setUpBlockListWithDefaultLists();
        
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // test case: When invoking the isRequesterAuthorized method, it must return
    // whether or not the remote provider passed as argument is allowed to 
    // perform local operations. In the case of BlockList implementation,
    // it must return whether or not the provider is in the "not-allowed" list.
    @Test
    public void testIsRequesterAuthorized() throws ConfigurationErrorException {
        setUpBlockListWithDefaultLists();
        
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // test case: When invoking the isTargetAuthorized method on a BlockList instance with empty
    // not allowed target lists, it must return true for all known members and false to unknown providers.
    @Test
    public void testIsTargetAuthorizedEmptyNotAllowedTargetsList() throws ConfigurationErrorException {
        setUpBlockListWithEmptyNotAllowedTargetsList();
        
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // test case: When invoking the isRequesterAuthorized method on a BlockList instance with empty
    // not allowed requester lists, it must return true for all known members and false to unknown providers.
    @Test
    public void testIsTargetAuthorizedEmptyNotAllowedRequestersList() throws ConfigurationErrorException {
        setUpBlockListWithEmptyNotAllowedRequestersList();
        
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO add documentation
    @Test
    public void testAddMember() throws Exception {
    	setUpBlockListWithDefaultLists();
    	
    	List<String> membersId = this.service.listMembers();
    	
        // verify before adding member
        Assert.assertEquals(3, membersId.size());
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsRequester));
        Assert.assertTrue(membersId.contains(memberNotAuthorizedAsTarget));
        Assert.assertFalse(membersId.contains(newMember));
    	
    	this.service.addMember(newMember);
    	
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY, updatedMembersListString);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
    	
        List<String> updateMembersId = this.service.listMembers();

        // verify after adding member
        Assert.assertEquals(4, updateMembersId.size());
        Assert.assertTrue(updateMembersId.contains(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(updateMembersId.contains(memberNotAuthorizedAsRequester));
        Assert.assertTrue(updateMembersId.contains(memberNotAuthorizedAsTarget));
        Assert.assertTrue(updateMembersId.contains(newMember));
    }
    
    // TODO add documentation
    @Test
    public void testAddTarget() throws Exception {
    	setUpBlockListWithTargetListToBeUpdated();
    	
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
        
        this.service.addTarget(memberNotAuthorizedAsTarget);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY, targetsListAfterAdd);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testAddNotKnownTargetMustFail() throws Exception {
    	setUpBlockListWithTargetListToBeUpdated();
    	
        this.service.addTarget(notMember1);
    }
    
    // TODO add documentation
    @Test
    public void testAddRequester() throws ConfigurationErrorException {
    	setUpBlockListWithRequesterListToBeUpdated();
    	
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
        
        this.service.addRequester(memberNotAuthorizedAsRequester);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY, requestersListAfterAdd);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testAddingNotKnownRequesterMustFail() throws Exception {
    	setUpBlockListWithRequesterListToBeUpdated();
        
        this.service.addRequester(notMember1);
    }

    // TODO add documentation
    @Test
    public void testRemoveTarget() throws Exception {
    	setUpBlockListWithTargetListBeforeRemove();
    	
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
        
        this.service.removeTarget(memberNotAuthorizedAsTarget);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY, targetsListAfterRemove);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownTargetMustFail() throws Exception {
    	setUpBlockListWithTargetListBeforeRemove();
    	
        this.service.removeTarget(memberNotAuthorizedAsRequester);
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownProviderFromTargetsMustFail() throws Exception {
    	setUpBlockListWithTargetListBeforeRemove();
    	
        this.service.removeTarget(notMember1);
    }
    
    // TODO add documentation
    @Test
    public void testRemoveRequester() throws Exception {
    	setUpBlockListWithRequestersListBeforeRemove();
    	
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
        
        this.service.removeRequester(memberNotAuthorizedAsRequester);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY, requestersListAfterRemove);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberNotAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownRequesterMustFail() throws Exception {
    	setUpBlockListWithRequestersListBeforeRemove();
    	
        this.service.removeRequester(memberNotAuthorizedAsTarget);
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownProviderFromRequestersMustFail() throws Exception {
    	setUpBlockListWithRequestersListBeforeRemove();
    	
        this.service.removeRequester(notMember1);
    }
    
    private void setUpBlockList(String membersListString, String notAllowedRequestersListString, String notAllowedTargetsListString) throws ConfigurationErrorException {
        PowerMockito.mockStatic(PropertiesHolder.class);
        this.propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(this.propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        Mockito.doReturn(notAllowedRequestersListString).when(this.propertiesHolder).getProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY);
        Mockito.doReturn(notAllowedTargetsListString).when(this.propertiesHolder).getProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY);
        
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(this.propertiesHolder);
        
        this.service = new BlockList();
    }
    
    private void setUpBlockListWithDefaultLists() throws ConfigurationErrorException {
        setUpBlockList(membersListString, notAllowedRequestersList, notAllowedTargetsList);
    }
    
    private void setUpBlockListWithEmptyNotAllowedTargetsList() throws ConfigurationErrorException {
        setUpBlockList(membersListString, notAllowedRequestersList, emptyNotAllowedTargetsList);
    }
    
    private void setUpBlockListWithEmptyNotAllowedRequestersList() throws ConfigurationErrorException {
        setUpBlockList(membersListString, emptyNotAllowedRequestersList, notAllowedTargetsList);
    }
    
    private void setUpBlockListWithTargetListToBeUpdated() throws ConfigurationErrorException {
    	setUpBlockList(membersListString, notAllowedRequestersList, targetsListBeforeAdd);
    }
    
    private void setUpBlockListWithRequesterListToBeUpdated() throws ConfigurationErrorException {
    	setUpBlockList(membersListString, requestersListBeforeAdd, notAllowedTargetsList);
    }
    
    private void setUpBlockListWithTargetListBeforeRemove() throws ConfigurationErrorException {
    	setUpBlockList(membersListString, notAllowedRequestersList, targetsListBeforeRemove);
    }
    
    private void setUpBlockListWithRequestersListBeforeRemove() throws ConfigurationErrorException {
    	setUpBlockList(membersListString, requestersListBeforeRemove, notAllowedTargetsList);
    }
}
