package cloud.fogbow.ms.core;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;
import cloud.fogbow.ms.core.plugins.authorization.DefaultAuthorizationPlugin;

public class ApplicationFacade {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;
    
    private RSAPublicKey asPublicKey;
    private AuthorizationPlugin authorizationPlugin;
    
    public static ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    public ApplicationFacade() {
        // TODO replace this by the second constructor
        // or create an authorization plugin instantiator
        authorizationPlugin = new DefaultAuthorizationPlugin();
        // TODO move this initialization to main
        String publicKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PUBLIC_KEY_FILE_PATH);
        String privateKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PRIVATE_KEY_FILE_PATH);
        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(publicKeyFilePath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privateKeyFilePath);
    }
    
    public Boolean isAuthorized(String systemUserToken, AuthorizableOperation operation) throws FogbowException {
        SystemUser user = authenticate(systemUserToken);
        return this.authorizationPlugin.isAuthorized(user, operation);
    }
    
    public List<String> listMembers() throws Exception {
        return this.authorizationPlugin.listMembers();
    }
    
    protected SystemUser authenticate(String userToken) throws FogbowException {
        RSAPublicKey keyRSA = getAsPublicKey();
        return AuthenticationUtil.authenticate(keyRSA, userToken);
    }
    
    protected RSAPublicKey getAsPublicKey() throws FogbowException {
        if (this.asPublicKey == null) {
            this.asPublicKey = MSPublicKeysHolder.getInstance().getAsPublicKey();
        }
        return this.asPublicKey;
    }
    
}
