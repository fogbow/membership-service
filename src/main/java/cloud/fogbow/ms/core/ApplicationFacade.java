package cloud.fogbow.ms.core;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.ms.core.authorization.AdminOperation;

// TODO add tests
public class ApplicationFacade {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;
    private MembershipService membershipService;
    private AuthorizationPlugin<AdminOperation> authorizationPlugin;
	private long onGoingRequests;
	private boolean reloading;
    
    public static ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    private ApplicationFacade() {
        this.onGoingRequests = 0;
        this.reloading = false;
    }
    
    public List<String> listMembers() throws Exception {
    	startOperation();
    	try {
    		return this.membershipService.listMembers();    		
    	} finally {
    		finishOperation();
    	}
    }

    public String getPublicKey() throws InternalServerErrorException {
    	startOperation();
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
        	finishOperation();
        }
    }

    public boolean isTargetAuthorized(String provider) {
    	startOperation();
    	try {
    		return this.membershipService.isTargetAuthorized(provider);    		
    	} finally {
    		finishOperation();
    	}
    }

    public boolean isRequesterAuthorized(String provider) {
    	startOperation();
    	try {
    		return this.membershipService.isRequesterAuthorized(provider);    		
    	} finally {
    		finishOperation();
    	}
    }

    public void setMembershipService(MembershipService membershipService) {
        this.membershipService = membershipService;
    }
    
    public void setAuthorizationPlugin(AuthorizationPlugin<AdminOperation> authorizationPlugin) {
    	this.authorizationPlugin = authorizationPlugin;
    }
    
    public void addProvider(String userToken, String provider) throws FogbowException {
    	RSAPublicKey asPublicKey = MSPublicKeysHolder.getInstance().getAsPublicKey();
    	SystemUser systemUser = AuthenticationUtil.authenticate(asPublicKey, userToken);
    	this.authorizationPlugin.isAuthorized(systemUser, new AdminOperation());
    	
    	// TODO add logging
    	setAsReloading();
    	
    	try {
    		this.membershipService.addMember(provider);
    	} finally {
    		finishReloading();    		
    	}
    }
    
	public void addTargetProvider(String userToken, String provider) throws FogbowException {
    	RSAPublicKey asPublicKey = MSPublicKeysHolder.getInstance().getAsPublicKey();
    	SystemUser systemUser = AuthenticationUtil.authenticate(asPublicKey, userToken);
    	this.authorizationPlugin.isAuthorized(systemUser, new AdminOperation());
		
    	// TODO add logging
    	setAsReloading();
    	
    	try {
    		this.membershipService.addTarget(provider);
    	} finally {
    		finishReloading();    		
    	}
    	
	}

	public void addRequesterProvider(String userToken, String provider) throws FogbowException {
    	RSAPublicKey asPublicKey = MSPublicKeysHolder.getInstance().getAsPublicKey();
    	SystemUser systemUser = AuthenticationUtil.authenticate(asPublicKey, userToken);
    	this.authorizationPlugin.isAuthorized(systemUser, new AdminOperation());
		
    	// TODO add logging
    	setAsReloading();
    	
    	try {
    		this.membershipService.addRequester(provider);
    	} finally {
    		finishReloading();    		
    	}
		
	}
    
    public void reload(String userToken) throws FogbowException {
    	RSAPublicKey asPublicKey = MSPublicKeysHolder.getInstance().getAsPublicKey();
    	SystemUser systemUser = AuthenticationUtil.authenticate(asPublicKey, userToken);
    	this.authorizationPlugin.isAuthorized(systemUser, new AdminOperation());
    	
    	doReload();
    }

	private void doReload() throws ConfigurationErrorException {
		setAsReloading();
		
        while (this.onGoingRequests != 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		
        // TODO add logging
		PropertiesHolder.reset();
        MSPublicKeysHolder.reset();
        
        // TODO add logging
        String publicKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PUBLIC_KEY_FILE_PATH);
        String privateKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PRIVATE_KEY_FILE_PATH);
        ServiceAsymmetricKeysHolder.reset(publicKeyFilePath, privateKeyFilePath);
		
        // TODO add logging
		this.authorizationPlugin = PluginInstantiator.getAuthorizationPlugin();
		this.membershipService = PluginInstantiator.getMembershipService();
		
		finishReloading();
	}
	
    private void setAsReloading() {
        this.reloading = true;
    }
    
    private void finishReloading() {
    	this.reloading = false;
    }
	
    private void startOperation() {
        while (reloading)
            ;
        synchronized (this) {
            this.onGoingRequests++;
        }
    }
    
    private synchronized void finishOperation() {
        this.onGoingRequests--;
    }

}
