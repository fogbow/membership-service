package cloud.fogbow.ms.core.plugins.authorization;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cloud.fogbow.ms.core.service.WhiteList;

public class DefaultAuthorizationPluginTest {

    private DefaultAuthorizationPlugin plugin;
    private WhiteList membershipService;
    
    private String member1 = "member1";
    private String member2 = "member2";
    private String member3 = "member3";
    private String notMember1 = "notmember1";
    
    private List<String> membersList = Arrays.asList(member1,
                                                     member2, 
                                                     member3);
    
    // test case: the method listMembers must call the listMembers method
    // of a MembershipService instance, and then return a list containing 
    // the same members.
    @Test
    public void testListMembers() throws Exception {
        this.membershipService = Mockito.mock(WhiteList.class);
        
        Mockito.when(this.membershipService.listMembers()).thenReturn(membersList);
        
        this.plugin = new DefaultAuthorizationPlugin(membershipService);
                
        List<String> returnedMembersList = this.plugin.listMembers();
        
        Assert.assertTrue(returnedMembersList.contains(member1));
        Assert.assertTrue(returnedMembersList.contains(member2));
        Assert.assertTrue(returnedMembersList.contains(member3));
        
        Mockito.verify(this.membershipService, Mockito.atLeastOnce()).listMembers();
    }
    
    // test case: the method isMember must call the isMember method of a 
    // MembershipService instance, and then return the same boolean.
    @Test
    public void testIsAuthorizedProviderIsMember() {
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(member1)).thenReturn(true);

        this.plugin = new DefaultAuthorizationPlugin(membershipService);
        
        Assert.assertTrue(this.plugin.isAuthorized(member1));
    }
    
    // test case: the method isMember must call the isMember method of a 
    // MembershipService instance, and then return the same boolean.
    @Test
    public void testIsAuthorizedProviderIsNotMember() {
        this.membershipService = Mockito.mock(WhiteList.class);
        Mockito.when(this.membershipService.isMember(notMember1)).thenReturn(false);

        this.plugin = new DefaultAuthorizationPlugin(membershipService);
        Assert.assertFalse(this.plugin.isAuthorized(notMember1));
    }
}
