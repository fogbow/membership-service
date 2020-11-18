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

import cloud.fogbow.ms.MembershipService;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.permission.AllowOnlyPermission;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertiesHolder.class})
public class WhiteListTest {

    private MembershipService service;

    private String member1 = "member1";
    private String member2 = "member2";
    private String member3 = "member3";
    
    private String permissionName1 = "permissionName1";
    private String permissionType1 = "permissionType1";
    
    private String permissionName2 = "permissionName2";
    private String permissionType2 = "permissionType2";
    
    private String membersListString = String.join(",", member1, member2, member3);
    
    private AllowOnlyPermission permission1;
    private AllowOnlyPermission permission2;
    
    // test case: When invoking the listMembers method from an instance created with
    // the MembershipService class constructor with a valid parameter, it must list
    // the configured membership in the file passed by parameter.
    @Test
    public void testListMembers() throws Exception {
        // set up PropertiesHolder 
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(membersListString).when(propertiesHolder).getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
        
        this.permission1 = Mockito.mock(AllowOnlyPermission.class);
        this.permission2 = Mockito.mock(AllowOnlyPermission.class);
        
        PermissionInstantiator instantiator = Mockito.mock(PermissionInstantiator.class);
        
        Mockito.when(instantiator.getPermissionInstance(permissionType1, permissionName1)).thenReturn(permission1);
        Mockito.when(instantiator.getPermissionInstance(permissionType2, permissionName2)).thenReturn(permission2);
        
        // exercise
        this.service = new WhiteList(instantiator);
        List<String> membersId = this.service.listMembers();

        // verify
        Assert.assertTrue(membersId.contains(member1));
        Assert.assertTrue(membersId.contains(member2));
        Assert.assertTrue(membersId.contains(member3));
    }
}
