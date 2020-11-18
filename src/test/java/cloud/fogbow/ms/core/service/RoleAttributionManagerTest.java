package cloud.fogbow.ms.core.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.operation.OperationType;
import cloud.fogbow.ms.core.models.operation.RasAuthorizableOperation;
import cloud.fogbow.ms.core.models.permission.AllowOnlyPermission;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertiesHolder.class})
public class RoleAttributionManagerTest {
 
    /*
     * user1 has role1
     * role1 has permission1
     * user2 has role2
     * role2 has permission2
     */
    private String permissionName1 = "permissionName1";
    private String permissionType1 = "permissionType1";
    
    private String permissionName2 = "permissionName2";
    private String permissionType2 = "permissionType2";
    
    private String roleName1 = "role1";
    private String roleName2 = "role2";
    private String rolesNames = String.format("%s,%s", roleName1, roleName2);
    
    private String role1Permissions = permissionName1;
    private String role2Permissions = permissionName2;
    
    private String userName1 = "user1";
    private String userName2 = "user2";
    private String userNames = String.format("%s,%s", userName1, userName2);
    
    private String rolesUser1 = roleName1;
    private String rolesUser2 = String.format("%s,%s", roleName1, roleName2);
    
    private RoleAttributionManager manager;
    private PropertiesHolder propertiesHolder;
    
    private AllowOnlyPermission permission1;
    private AllowOnlyPermission permission2;
    
    private String provider = "member1";
    
    @Before
    public void setUp() {
     // set up PropertiesHolder 
        PowerMockito.mockStatic(PropertiesHolder.class);
        this.propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(rolesNames).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.ROLES_NAMES_KEY);
        Mockito.doReturn(role1Permissions).when(propertiesHolder).getProperty(roleName1);
        Mockito.doReturn(role2Permissions).when(propertiesHolder).getProperty(roleName2);
        Mockito.doReturn(permissionType1).when(propertiesHolder).getProperty(permissionName1);
        Mockito.doReturn(permissionType2).when(propertiesHolder).getProperty(permissionName2);
        Mockito.doReturn(userNames).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.USER_NAMES_KEY);
        Mockito.doReturn(rolesUser1).when(propertiesHolder).getProperty(userName1);
        Mockito.doReturn(rolesUser2).when(propertiesHolder).getProperty(userName2);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
        
        this.permission1 = Mockito.mock(AllowOnlyPermission.class);
        this.permission2 = Mockito.mock(AllowOnlyPermission.class);
        
        PermissionInstantiator instantiator = Mockito.mock(PermissionInstantiator.class);
        
        Mockito.when(instantiator.getPermissionInstance(permissionType1, permissionName1)).thenReturn(permission1);
        Mockito.when(instantiator.getPermissionInstance(permissionType2, permissionName2)).thenReturn(permission2);
        
        this.manager = new RoleAttributionManager(instantiator);
    }
    
    // TODO documentation
    @Test
    public void constructorReadsRolesInformationCorrectly() {
        // Reads correctly roles names
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(ConfigurationPropertyKeys.ROLES_NAMES_KEY);
        // Reads correctly roles permissions
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(roleName1);
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(roleName2);
        // Reads correctly permission types
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(permissionName1);
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(permissionName2);
        // Reads correctly user names
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(ConfigurationPropertyKeys.USER_NAMES_KEY);
        // Reads correctly user roles
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(userName1);
        Mockito.verify(propertiesHolder, Mockito.times(1)).getProperty(userName2);
        
        PowerMockito.verifyStatic(PropertiesHolder.class, Mockito.atLeastOnce());
    }
    
    // TODO documentation
    @Test
    public void testIsAuthorized() {
        RasAuthorizableOperation operationGet = new RasAuthorizableOperation(provider, OperationType.GET);
        RasAuthorizableOperation operationCreate = new RasAuthorizableOperation(provider, OperationType.CREATE);
        RasAuthorizableOperation operationReload = new RasAuthorizableOperation(provider, OperationType.RELOAD);
        
        Mockito.when(this.permission1.isAuthorized(operationGet)).thenReturn(true);
        Mockito.when(this.permission2.isAuthorized(operationGet)).thenReturn(true);
        
        Mockito.when(this.permission1.isAuthorized(operationCreate)).thenReturn(false);
        Mockito.when(this.permission2.isAuthorized(operationCreate)).thenReturn(true);

        Mockito.when(this.permission1.isAuthorized(operationReload)).thenReturn(false);
        Mockito.when(this.permission2.isAuthorized(operationReload)).thenReturn(false);

        // user1 has role1
        // role1 has permission1
        // permission1 allows only get operations
        assertTrue(this.manager.isUserAuthorized(userName1, operationGet));
        assertFalse(this.manager.isUserAuthorized(userName1, operationCreate));
        assertFalse(this.manager.isUserAuthorized(userName1, operationReload));

        // user2 has role2
        // role2 has permission2
        // permission2 allows only get and create operations
        assertTrue(this.manager.isUserAuthorized(userName2, operationGet));
        assertTrue(this.manager.isUserAuthorized(userName2, operationCreate));
        assertFalse(this.manager.isUserAuthorized(userName2, operationReload));
    }
}
