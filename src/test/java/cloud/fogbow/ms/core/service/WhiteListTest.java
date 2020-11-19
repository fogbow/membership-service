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

import cloud.fogbow.ms.MembershipService;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.operation.OperationType;
import cloud.fogbow.ms.core.models.operation.RasAuthorizableOperation;
import cloud.fogbow.ms.core.models.permission.AllowOnlyPermission;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertiesHolder.class})
public class WhiteListTest {

    private MembershipService service;

    private String member1 = "member1";
    private String member2 = "member2";
    private String member3 = "member3";
    private String notMember1 = "notMember1";
    
    private String permissionName1 = "permissionName1";
    private String permissionType1 = "permissionType1";
    
    private String permissionName2 = "permissionName2";
    private String permissionType2 = "permissionType2";
    
    private String membersListString = String.join(",", member1, member2, member3);
    
    private AllowOnlyPermission permission1;
    private AllowOnlyPermission permission2;
    
    private RasAuthorizableOperation operationGetMember1;
    private RasAuthorizableOperation operationCreateMember1;
    private RasAuthorizableOperation operationGetMember2;
    private RasAuthorizableOperation operationCreateMember2;
    private RasAuthorizableOperation operationGetMember3;
    private RasAuthorizableOperation operationCreateMember3;
    private RasAuthorizableOperation operationGetNotMember1;
    private RasAuthorizableOperation operationCreateNotMember1;
    
    @Before
    public void setUp() {
        setUpConfigurationInfo();
        setUpOperations();
        setUpAuthorization();
        
        PermissionInstantiator instantiator = Mockito.mock(PermissionInstantiator.class);
        
        Mockito.when(instantiator.getPermissionInstance(permissionType1, permissionName1)).thenReturn(permission1);
        Mockito.when(instantiator.getPermissionInstance(permissionType2, permissionName2)).thenReturn(permission2);
        
        this.service = new WhiteList(instantiator);
    }

    private void setUpConfigurationInfo() {
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        // member1 has permission1
        // member2 and member3 have permission2
        Mockito.doReturn(permissionName1).when(propertiesHolder).getProperty(member1);
        Mockito.doReturn(permissionName2).when(propertiesHolder).getProperty(member2);
        Mockito.doReturn(permissionName2).when(propertiesHolder).getProperty(member3);
        Mockito.doReturn(permissionType1).when(propertiesHolder).getProperty(permissionName1);
        Mockito.doReturn(permissionType2).when(propertiesHolder).getProperty(permissionName2);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
    }

    private void setUpOperations() {
        this.operationGetMember1 = new RasAuthorizableOperation(member1, OperationType.GET);
        this.operationCreateMember1 = new RasAuthorizableOperation(member1, OperationType.CREATE);
        this.operationGetMember2 = new RasAuthorizableOperation(member2, OperationType.GET);
        this.operationCreateMember2 = new RasAuthorizableOperation(member2, OperationType.CREATE);
        this.operationGetMember3 = new RasAuthorizableOperation(member3, OperationType.GET);
        this.operationCreateMember3 = new RasAuthorizableOperation(member3, OperationType.CREATE);
        this.operationGetNotMember1 = new RasAuthorizableOperation(notMember1, OperationType.GET);
        this.operationCreateNotMember1 = new RasAuthorizableOperation(notMember1, OperationType.CREATE);
    }
    
    private void setUpAuthorization() {
        // members with permission1 can only perform get operations
        // members with permission2 can perform get and create operations
        this.permission1 = Mockito.mock(AllowOnlyPermission.class);
        this.permission2 = Mockito.mock(AllowOnlyPermission.class);
        
        Mockito.when(permission1.isAuthorized(this.operationGetMember1)).thenReturn(true);
        Mockito.when(permission1.isAuthorized(this.operationCreateMember1)).thenReturn(false);
        Mockito.when(permission2.isAuthorized(this.operationGetMember2)).thenReturn(true);
        Mockito.when(permission2.isAuthorized(this.operationCreateMember2)).thenReturn(true);
        Mockito.when(permission2.isAuthorized(this.operationGetMember3)).thenReturn(true);
        Mockito.when(permission2.isAuthorized(this.operationCreateMember3)).thenReturn(true);
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
    
    // test case: When invoking the canPerformOperation method, it must return 
    // whether or not the given operation can be performed, based on configuration 
    // files.
    //
    // In this case, member2 and member3 are allowed to perform get and create 
    // operations, since they have permission2. member1 can only perform get operations,
    // since it has permission1. notmember1 cannot perform any operation.
    @Test
    public void testCanPerformOperation() {
        Assert.assertTrue(this.service.canPerformOperation(operationGetMember1));
        Assert.assertTrue(this.service.canPerformOperation(operationGetMember2));
        Assert.assertTrue(this.service.canPerformOperation(operationGetMember3));
        Assert.assertFalse(this.service.canPerformOperation(operationCreateMember1));
        Assert.assertTrue(this.service.canPerformOperation(operationCreateMember2));
        Assert.assertTrue(this.service.canPerformOperation(operationCreateMember3));
        // Maybe we should throw an exception if this happens
        Assert.assertFalse(this.service.canPerformOperation(operationGetNotMember1));
        Assert.assertFalse(this.service.canPerformOperation(operationCreateNotMember1));
    }
}
