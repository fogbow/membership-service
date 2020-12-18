package cloud.fogbow.ms.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import cloud.fogbow.common.util.PublicKeysHolder;
import cloud.fogbow.ms.core.authorization.AdminAuthorizationPlugin;
import cloud.fogbow.ms.core.authorization.AdminOperation;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthenticationUtil.class, MSPublicKeysHolder.class, 
	PropertiesHolder.class, PublicKeysHolder.class})
public class ApplicationFacadeTest {

	private ApplicationFacade facade;
	private MembershipService membershipService;
	private AuthorizationPlugin<AdminOperation> authorizationPlugin;
	private String member1 = "member1";
	private String member2 = "member2";
	private List<String> members = Arrays.asList(member1, member2);
	
    private String token = "userToken";
    private String userId = "userId";
    private String userName = "userName";
    private String provider = "provider";
	
	private SystemUser systemUser; 
	private RSAPublicKey key;
	private AdminOperation operation;

    @Before
    public void setUp() throws FogbowException {
		this.operation = new AdminOperation();
		this.systemUser = new SystemUser(userId, userName, provider);
		
		// authentication
		this.key = Mockito.mock(RSAPublicKey.class);

		PowerMockito.mockStatic(MSPublicKeysHolder.class);
		MSPublicKeysHolder keysHolder = Mockito.mock(MSPublicKeysHolder.class);
		Mockito.doReturn(key).when(keysHolder).getAsPublicKey();
		BDDMockito.given(MSPublicKeysHolder.getInstance()).willReturn(keysHolder);
		
		PowerMockito.mockStatic(AuthenticationUtil.class);
		BDDMockito.given(AuthenticationUtil.authenticate(key, token)).willReturn(systemUser);
		
		// authorization
		this.authorizationPlugin = Mockito.mock(AdminAuthorizationPlugin.class);
		Mockito.doReturn(true).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.membershipService = Mockito.mock(MembershipService.class);
		
		this.facade = ApplicationFacade.getInstance();
		
		this.facade.setMembershipService(membershipService);
		this.facade.setAuthorizationPlugin(authorizationPlugin);
    }
	
	// TODO documentation
	@Test
	public void testListMembers() throws Exception {
		this.facade = ApplicationFacade.getInstance();
		
		Mockito.doReturn(members).when(this.membershipService).listMembers();
		
		List<String> returnedMembersList = facade.listMembers();
		
		assertEquals(returnedMembersList.size(), 2);
		assertTrue(returnedMembersList.contains(member1));
		assertTrue(returnedMembersList.contains(member2));
	}
	
	// TODO documentation
	@Test
	public void testAddProvider() throws FogbowException {
		this.facade.addProvider(token, provider);
		
		Mockito.verify(authorizationPlugin, Mockito.times(1)).isAuthorized(systemUser, operation);
		Mockito.verify(membershipService, Mockito.times(1)).addMember(provider);
	}
	
	// TODO documentation
	@Test(expected = UnauthorizedRequestException.class)
	public void testAddProviderUnauthorizedOperation() throws FogbowException {
		Mockito.doThrow(new UnauthorizedRequestException()).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.facade.addProvider(token, provider);
	}
	
	// TODO documentation
	@Test
	public void testRemoveProvider() throws FogbowException {
		this.facade.removeProvider(token, provider);
		
		Mockito.verify(authorizationPlugin, Mockito.times(1)).isAuthorized(systemUser, operation);
		Mockito.verify(membershipService, Mockito.times(1)).removeMember(provider);
	}

	// TODO documentation
	@Test(expected = UnauthorizedRequestException.class)
	public void testRemoveProviderUnauthorizedOperation() throws FogbowException {
		Mockito.doThrow(new UnauthorizedRequestException()).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.facade.removeProvider(token, provider);
	}
	
	// TODO documentation
	@Test
	public void testAddTargetProvider() throws FogbowException {
		this.facade.addTargetProvider(token, provider);
		
		Mockito.verify(authorizationPlugin, Mockito.times(1)).isAuthorized(systemUser, operation);
		Mockito.verify(membershipService, Mockito.times(1)).addTarget(provider);
	}
	
	// TODO documentation
	@Test(expected = UnauthorizedRequestException.class)
	public void testAddTargetProviderUnauthorizedOperation() throws FogbowException {
		Mockito.doThrow(new UnauthorizedRequestException()).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.facade.addTargetProvider(token, provider);
	}
	
	// TODO documentation
	@Test
	public void testRemoveTarget() throws FogbowException {
		this.facade.removeTargetProvider(token, provider);
		
		Mockito.verify(authorizationPlugin, Mockito.times(1)).isAuthorized(systemUser, operation);
		Mockito.verify(membershipService, Mockito.times(1)).removeTarget(provider);
	}
	
	// TODO documentation
	@Test(expected = UnauthorizedRequestException.class)
	public void testRemoveTargetProviderUnauthorizedOperation() throws FogbowException {
		Mockito.doThrow(new UnauthorizedRequestException()).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.facade.removeTargetProvider(token, provider);
	}
	
	// TODO documentation
	@Test
	public void testAddRequesterProvider() throws FogbowException {
		this.facade.addRequesterProvider(token, provider);
		
		Mockito.verify(authorizationPlugin, Mockito.times(1)).isAuthorized(systemUser, operation);
		Mockito.verify(membershipService, Mockito.times(1)).addRequester(provider);
	}
	
	// TODO documentation
	@Test(expected = UnauthorizedRequestException.class)
	public void testAddRequesterProviderUnauthorizedOperation() throws FogbowException {
		Mockito.doThrow(new UnauthorizedRequestException()).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.facade.addRequesterProvider(token, provider);
	}
	
	// TODO documentation
	@Test
	public void testRemoveRequesterProvider() throws FogbowException {
		this.facade.removeRequesterProvider(token, provider);
		
		Mockito.verify(authorizationPlugin, Mockito.times(1)).isAuthorized(systemUser, operation);
		Mockito.verify(membershipService, Mockito.times(1)).removeRequester(provider);
	}
	
	// TODO documentation
	@Test(expected = UnauthorizedRequestException.class)
	public void testRemoveRequesterProviderUnauthorizedOperation() throws FogbowException {
		Mockito.doThrow(new UnauthorizedRequestException()).when(this.authorizationPlugin).isAuthorized(systemUser, operation);
		
		this.facade.removeRequesterProvider(token, provider);
	}
}
