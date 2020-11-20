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
    private String userId2 = "userId2";
    private String userName2 = "userName2";
    
    private String localProviderId = "localProvider";
    
    private List<String> membersList = Arrays.asList(member1,
                                                     member2, 
                                                     member3);
    
    // TODO documentation
    @Test
    public void testListMembers() throws Exception {
        this.membershipService = Mockito.mock(WhiteList.class);
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        
        Mockito.when(this.membershipService.listMembers()).thenReturn(membersList);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager, localProviderId);
                
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
        
        SystemUser localUser = new SystemUser(userId1, userName1, localProviderId);
        SystemUser remoteUser = new SystemUser(userId2, userName2, notmember1);
        
        RasAuthorizableOperation remoteOperation = new RasAuthorizableOperation(notmember1, OperationType.CREATE);
        RasAuthorizableOperation localOperation = new RasAuthorizableOperation(localProviderId, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(remoteOperation)).thenReturn(false);
        Mockito.when(this.membershipService.canPerformOperation(localOperation)).thenReturn(false);
        
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, remoteOperation)).thenReturn(false);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, localOperation)).thenReturn(false);
        Mockito.when(this.roleManager.isUserAuthorized(userId2, localOperation)).thenReturn(false);

        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager, localProviderId);
        
        Assert.assertFalse(this.plugin.isAuthorized(localUser, remoteOperation));
        Assert.assertFalse(this.plugin.isAuthorized(remoteUser, localOperation));
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedProviderHasWrongPermission() {
        SystemUser localUser = new SystemUser(userId1, userName1, localProviderId);
        SystemUser remoteUser = new SystemUser(userId1, userName1, member1);
        
        RasAuthorizableOperation remoteOperation = new RasAuthorizableOperation(member1, OperationType.CREATE);
        RasAuthorizableOperation localOperation = new RasAuthorizableOperation(localProviderId, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(remoteOperation)).thenReturn(false);
        Mockito.when(this.membershipService.canPerformOperation(localOperation)).thenReturn(false);
        
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, remoteOperation)).thenReturn(false);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, localOperation)).thenReturn(false);
        Mockito.when(this.roleManager.isUserAuthorized(userId2, localOperation)).thenReturn(false);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager, localProviderId);
        
        Assert.assertFalse(this.plugin.isAuthorized(localUser, remoteOperation));
        Assert.assertFalse(this.plugin.isAuthorized(remoteUser, localOperation));
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedUserIsNotAuthorized() {
        SystemUser localUser = new SystemUser(userId1, userName1, localProviderId);
        SystemUser remoteUser = new SystemUser(userId2, userName2, member1);
        RasAuthorizableOperation localOperation = new RasAuthorizableOperation(localProviderId, OperationType.CREATE);
        RasAuthorizableOperation remoteOperation = new RasAuthorizableOperation(member1, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(remoteOperation)).thenReturn(true);
        
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, remoteOperation)).thenReturn(false);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, localOperation)).thenReturn(false);
        Mockito.when(this.roleManager.isUserAuthorized(userId2, localOperation)).thenReturn(false);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager, localProviderId);
        
        Assert.assertFalse(this.plugin.isAuthorized(localUser, remoteOperation));
        Assert.assertFalse(this.plugin.isAuthorized(localUser, localOperation));
        Assert.assertFalse(this.plugin.isAuthorized(remoteUser, localOperation));
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorizedUserIsAuthorized() {
        SystemUser localUser = new SystemUser(userId1, userName1, localProviderId);
        SystemUser remoteUser = new SystemUser(userId2, userName2, member1);
        
        RasAuthorizableOperation remoteOperation = new RasAuthorizableOperation(member1, OperationType.CREATE);
        RasAuthorizableOperation localOperation = new RasAuthorizableOperation(localProviderId, OperationType.CREATE);
        
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(localOperation)).thenReturn(true);
        Mockito.when(this.membershipService.canPerformOperation(remoteOperation)).thenReturn(true);
        
        this.roleManager = Mockito.mock(RoleAttributionManager.class);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, remoteOperation)).thenReturn(true);
        Mockito.when(this.roleManager.isUserAuthorized(userId1, localOperation)).thenReturn(true);
        Mockito.when(this.roleManager.isUserAuthorized(userId2, localOperation)).thenReturn(true);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService, roleManager, localProviderId);
        
        Assert.assertTrue(this.plugin.isAuthorized(localUser, remoteOperation));
        Assert.assertTrue(this.plugin.isAuthorized(localUser, localOperation));
        Assert.assertTrue(this.plugin.isAuthorized(remoteUser, localOperation));
    }

}
