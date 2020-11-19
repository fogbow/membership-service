package cloud.fogbow.ms.core.plugins.authorization;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ms.core.models.operation.OperationType;
import cloud.fogbow.ms.core.models.operation.RasAuthorizableOperation;
import cloud.fogbow.ms.core.service.RoleAttributionManager;
import cloud.fogbow.ms.core.service.WhiteList;

public class DefaultAuthorizationPluginTest {

    private DefaultAuthorizationPlugin plugin;
    private WhiteList membershipService;
    private RoleAttributionManager roleManager;
    
    private String member1 = "member1";
    private String member2 = "member2";
    private String member3 = "member3";
    private String notmember1 = "notmember1";
    
    private String userId1 = "userId1";
    private String userName1 = "userName1";
    private String identityProviderId1 = "idProvider1";
    
    private List<String> membersList = Arrays.asList(member1,
                                                     member2, 
                                                     member3);
    
    // TODO documentation
    @Test
    public void testListMembers() throws Exception {
        this.membershipService = Mockito.mock(WhiteList.class);
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        
        Mockito.when(this.membershipService.listMembers()).thenReturn(membersList);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager);
                
        List<String> returnedMembersList = this.plugin.listMembers();
        
        Assert.assertTrue(returnedMembersList.contains(member1));
        Assert.assertTrue(returnedMembersList.contains(member2));
        Assert.assertTrue(returnedMembersList.contains(member3));
        
        Mockito.verify(this.membershipService, Mockito.atLeastOnce()).listMembers();
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedProviderIsNotMember() {
        this.membershipService = Mockito.mock(WhiteList.class);
        
        Mockito.when(this.membershipService.isMember(notmember1)).thenReturn(false);
        
        SystemUser user = new SystemUser(userId1, userName1, identityProviderId1);
        RasAuthorizableOperation operation = new RasAuthorizableOperation(notmember1, OperationType.CREATE);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager);
        
        Assert.assertFalse(this.plugin.isAuthorized(user, operation));
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedProviderHasWrongPermission() {
        SystemUser user = new SystemUser(userId1, userName1, identityProviderId1);
        RasAuthorizableOperation operation = new RasAuthorizableOperation(member1, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(operation)).thenReturn(false);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager);
        
        Assert.assertFalse(this.plugin.isAuthorized(user, operation));
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedUserIsNotAuthorized() {
        SystemUser user = new SystemUser(userId1, userName1, identityProviderId1);
        RasAuthorizableOperation operation = new RasAuthorizableOperation(member1, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(operation)).thenReturn(true);
        
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, operation)).thenReturn(false);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager);
        
        Assert.assertFalse(this.plugin.isAuthorized(user, operation));
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedUserIsAuthorized() {
        SystemUser user = new SystemUser(userId1, userName1, identityProviderId1);
        RasAuthorizableOperation operation = new RasAuthorizableOperation(member1, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(operation)).thenReturn(true);
        
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, operation)).thenReturn(true);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager);
        
        Assert.assertTrue(this.plugin.isAuthorized(user, operation));
    }

}
