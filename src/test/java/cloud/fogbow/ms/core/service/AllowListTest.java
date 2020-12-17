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
public class AllowListTest {

    private MembershipService service;

    private String memberAuthorizedAsRequesterAndTarget = "requesterAndTarget";
    private String memberAuthorizedAsRequester = "requester";
    private String memberAuthorizedAsTarget = "target";
    private String notMember1 = "notMember1";
    private String newMember = "newMember";
    
    private String membersListString = String.join(",", memberAuthorizedAsRequesterAndTarget, 
                                        memberAuthorizedAsRequester, memberAuthorizedAsTarget);
    private String allowedRequestersList = String.join(",", memberAuthorizedAsRequester, 
                                        memberAuthorizedAsRequesterAndTarget);
    private String allowedTargetsList = String.join(",", memberAuthorizedAsTarget, 
                                        memberAuthorizedAsRequesterAndTarget);
    
    private String updatedMembersListString = String.join(",", memberAuthorizedAsRequesterAndTarget, 
            							memberAuthorizedAsRequester, memberAuthorizedAsTarget, 
            							newMember);
    
    private String targetsListBeforeAdd = memberAuthorizedAsRequesterAndTarget;
    private String targetsListAfterAdd = String.join(",", memberAuthorizedAsRequesterAndTarget, 
    									memberAuthorizedAsTarget);
    
    private String requestersListBeforeAdd = memberAuthorizedAsRequesterAndTarget;
    private String requestersListAfterAdd = String.join(",", memberAuthorizedAsRequesterAndTarget, 
    									memberAuthorizedAsRequester);
    
    private String targetsListBeforeRemove = String.join(",", memberAuthorizedAsTarget, 
            							memberAuthorizedAsRequesterAndTarget);
    private String targetsListAfterRemove = memberAuthorizedAsRequesterAndTarget;
    
    private String requestersListBeforeRemove = String.join(",", memberAuthorizedAsRequester, 
            							memberAuthorizedAsRequesterAndTarget);
    private String requestersListAfterRemove = memberAuthorizedAsRequesterAndTarget;
    
    private String emptyAllowedTargetsList = "";
    private String emptyAllowedRequestersList = "";
    
    private PropertiesHolder propertiesHolder;

    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        setUpAllowListWithDefaultLists();
        
        List<String> membersId = this.service.listMembers();

        // verify
        Assert.assertTrue(membersId.contains(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(membersId.contains(memberAuthorizedAsRequester));
        Assert.assertTrue(membersId.contains(memberAuthorizedAsTarget));
    }
    
    // test case: When invoking the isMember method, it must return whether or 
    // not the provider passed as argument is member, based on the configuration file.
    @Test
    public void testIsMember() throws ConfigurationErrorException {
        setUpAllowListWithDefaultLists();
        
        Assert.assertTrue(this.service.isMember(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isMember(memberAuthorizedAsRequester));
        Assert.assertTrue(this.service.isMember(memberAuthorizedAsTarget));
        Assert.assertFalse(this.service.isMember(notMember1));
    }
    
    // test case: When invoking the isTargetAuthorized method, it must return 
    // whether or not local users are allowed to perform operations in the 
    // remote provider passed as argument. In the case of AllowList implementation,
    // it must return whether or not the provider is in the "allowed" list.
    @Test
    public void testIsTargetAuthorized() throws ConfigurationErrorException {
        setUpAllowListWithDefaultLists();
        
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // test case: When invoking the isRequesterAuthorized method, it must return
    // whether or not the remote provider passed as argument is allowed to 
    // perform local operations. In the case of AllowList implementation,
    // it must return whether or not the provider is in the "allowed" list.
    @Test
    public void testIsRequesterAuthorized() throws ConfigurationErrorException {
        setUpAllowListWithDefaultLists();
        
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // test case: When invoking the isTargetAuthorized method on an AllowList instance with empty
    // allowed target lists, it must return false for all known members and false to unknown providers.
    @Test
    public void testIsTargetAuthorizedEmptyNotAllowedTargetsList() throws ConfigurationErrorException {
        setUpAllowListWithEmptyAllowedTargetsList();
        
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // test case: When invoking the isRequesterAuthorized method on an AllowList instance with empty
    // allowed requester lists, it must return false for all known members and false to unknown providers.
    @Test
    public void testIsTargetAuthorizedEmptyNotAllowedRequestersList() throws ConfigurationErrorException {
        setUpAllowListWithEmptyAllowedRequestersList();
        
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO add documentation
    @Test
    public void testAddMember() throws Exception {
    	setUpAllowListWithDefaultLists();
    	
    	List<String> membersId = this.service.listMembers();
    	
        // verify before adding member
        Assert.assertEquals(3, membersId.size());
        Assert.assertTrue(membersId.contains(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(membersId.contains(memberAuthorizedAsRequester));
        Assert.assertTrue(membersId.contains(memberAuthorizedAsTarget));
        Assert.assertFalse(membersId.contains(newMember));
    	
    	this.service.addMember(newMember);
    	
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY, updatedMembersListString);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
    	
        List<String> updateMembersId = this.service.listMembers();

        // verify after adding member
        Assert.assertEquals(4, updateMembersId.size());
        Assert.assertTrue(updateMembersId.contains(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(updateMembersId.contains(memberAuthorizedAsRequester));
        Assert.assertTrue(updateMembersId.contains(memberAuthorizedAsTarget));
        Assert.assertTrue(updateMembersId.contains(newMember));
    }
    
    // TODO add documentation
    @Test
    public void testAddTarget() throws Exception {
    	setUpAllowListWithTargetListBeforeAdd();
    	
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
        
        this.service.addTarget(memberAuthorizedAsTarget);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY, targetsListAfterAdd);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testAddingNotKnownTargetMustFail() throws Exception {
    	setUpAllowListWithTargetListBeforeAdd();
        
        this.service.addTarget(notMember1);
    }
    
    // TODO add documentation   
    @Test(expected = ConfigurationErrorException.class)
    public void testAddingDuplicateTargetMustFail() throws Exception {
    	setUpAllowListWithTargetListBeforeAdd();
    	
    	try {
	        this.service.addTarget(memberAuthorizedAsTarget);
		} catch (Exception e) {
			Assert.fail("This call should not throw exception.");
		}
        
        this.service.addTarget(memberAuthorizedAsTarget);
    }
    
    // TODO add documentation
    @Test
    public void testAddRequester() throws ConfigurationErrorException {
    	setUpAllowListWithRequesterListBeforeAdd();
    	
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
        
        this.service.addRequester(memberAuthorizedAsRequester);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY, 
    			requestersListAfterAdd);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testAddingNotKnownRequesterMustFail() throws Exception {
    	setUpAllowListWithRequesterListBeforeAdd();
        
        this.service.addRequester(notMember1);
    }
    
    // TODO add documentation   
    @Test(expected = ConfigurationErrorException.class)
    public void testAddingDuplicateRequesterMustFail() throws Exception {
    	setUpAllowListWithRequesterListBeforeAdd();
        
    	try {
    		this.service.addRequester(memberAuthorizedAsRequester);
    	} catch (Exception e) {
    		Assert.fail("This call should not throw exception.");
    	}
    	
        this.service.addRequester(memberAuthorizedAsRequester);
    }
    
    // TODO add documentation
    @Test
    public void testRemoveTarget() throws Exception {
    	setUpAllowListWithTargetListBeforeRemove();
    	
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
        
        this.service.removeTarget(memberAuthorizedAsTarget);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY, targetsListAfterRemove);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isTargetAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isTargetAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isTargetAuthorized(notMember1));
        Assert.assertFalse(this.service.isTargetAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownTargetMustFail() throws Exception {
    	setUpAllowListWithTargetListBeforeRemove();
    	
        this.service.removeTarget(memberAuthorizedAsRequester);
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownProviderFromTargetsMustFail() throws Exception {
    	setUpAllowListWithTargetListBeforeRemove();
    	
        this.service.removeTarget(notMember1);
    }
    
    // TODO add documentation
    @Test
    public void testRemoveRequester() throws Exception {
    	setUpAllowListWithRequestersListBeforeRemove();
    	
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
        
        this.service.removeRequester(memberAuthorizedAsRequester);
        
    	// verify configuration is update
    	Mockito.verify(propertiesHolder, Mockito.times(1)).setProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY, requestersListAfterRemove);
    	Mockito.verify(propertiesHolder, Mockito.times(1)).updatePropertiesFile();
        
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsTarget));
        Assert.assertTrue(this.service.isRequesterAuthorized(memberAuthorizedAsRequesterAndTarget));
        Assert.assertFalse(this.service.isRequesterAuthorized(memberAuthorizedAsRequester));
        Assert.assertFalse(this.service.isRequesterAuthorized(notMember1));
        Assert.assertFalse(this.service.isRequesterAuthorized(""));
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownRequesterMustFail() throws Exception {
    	setUpAllowListWithRequestersListBeforeRemove();
    	
        this.service.removeRequester(memberAuthorizedAsTarget);
    }
    
    // TODO add documentation
    @Test(expected = ConfigurationErrorException.class)
    public void testRemovingUnknownProviderFromRequestersMustFail() throws Exception {
    	setUpAllowListWithRequestersListBeforeRemove();
    	
        this.service.removeRequester(notMember1);
    }
    
    private void setUpAllowList(String membersListString, String allowedRequestersListString, String allowedTargetsListString) throws ConfigurationErrorException {
        PowerMockito.mockStatic(PropertiesHolder.class);
        this.propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(this.propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        Mockito.doReturn(allowedRequestersListString).when(this.propertiesHolder).getProperty(ConfigurationPropertyKeys.REQUESTER_MEMBERS_LIST_KEY);
        Mockito.doReturn(allowedTargetsListString).when(this.propertiesHolder).getProperty(ConfigurationPropertyKeys.TARGET_MEMBERS_LIST_KEY);
        
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(this.propertiesHolder);
        
        this.service = new AllowList();
    }
    
    private void setUpAllowListWithDefaultLists() throws ConfigurationErrorException {
        setUpAllowList(membersListString, allowedRequestersList, allowedTargetsList);
    }
    
    private void setUpAllowListWithEmptyAllowedTargetsList() throws ConfigurationErrorException {
        setUpAllowList(membersListString, allowedRequestersList, emptyAllowedTargetsList);
    }
    
    private void setUpAllowListWithEmptyAllowedRequestersList() throws ConfigurationErrorException {
        setUpAllowList(membersListString, emptyAllowedRequestersList, allowedTargetsList);
    }
    
    private void setUpAllowListWithTargetListBeforeAdd() throws ConfigurationErrorException {
    	setUpAllowList(membersListString, allowedRequestersList, targetsListBeforeAdd);
    }
    
    private void setUpAllowListWithRequesterListBeforeAdd() throws ConfigurationErrorException {
    	setUpAllowList(membersListString, requestersListBeforeAdd, allowedTargetsList);
    }
    
    private void setUpAllowListWithTargetListBeforeRemove() throws ConfigurationErrorException {
    	setUpAllowList(membersListString, allowedRequestersList, targetsListBeforeRemove);
    }
    
    private void setUpAllowListWithRequestersListBeforeRemove() throws ConfigurationErrorException {
    	setUpAllowList(membersListString, requestersListBeforeRemove, allowedTargetsList);
    }
}
