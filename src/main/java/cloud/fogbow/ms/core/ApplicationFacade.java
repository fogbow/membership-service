package cloud.fogbow.ms.core;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.ms.api.http.response.AuthorizationResponse;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;

public class ApplicationFacade {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;
    
    private RSAPublicKey asPublicKey;
    private AuthorizationPlugin authorizationPlugin;
    private RSAPublicKey rasPublicKey;
    
    public static ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    private ApplicationFacade() {

    }
    
    public void setAuthorizationPlugin(AuthorizationPlugin authorizationPlugin) {
        this.authorizationPlugin = authorizationPlugin;
    }
    
    public AuthorizationResponse isAuthorized(String systemUserToken, AuthorizableOperation operation) throws FogbowException {
        SystemUser user = authenticate(systemUserToken);
        boolean authorized = this.authorizationPlugin.isAuthorized(user, operation);
        RSAPrivateKey privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        RSAPublicKey rasPublicKey = getRasPublicKey();
        String token = AuthenticationUtil.createFogbowToken(user, privateKey, rasPublicKey);
        return new AuthorizationResponse(authorized, token);
    }
    
    public List<String> listMembers() throws Exception {
        return this.authorizationPlugin.listMembers();
    }
    
    private SystemUser authenticate(String userToken) throws FogbowException {
        RSAPublicKey keyRSA = getAsPublicKey();
        return AuthenticationUtil.authenticate(keyRSA, userToken);
    }
    
    private RSAPublicKey getAsPublicKey() throws FogbowException {
        if (this.asPublicKey == null) {
            this.asPublicKey = MSPublicKeysHolder.getInstance().getAsPublicKey();
        }
        return this.asPublicKey;
    }
    
    private RSAPublicKey getRasPublicKey() throws FogbowException {
        if (this.rasPublicKey == null) {
            this.rasPublicKey = MSPublicKeysHolder.getInstance().getRASPublicKey();
        }
        return this.rasPublicKey;
    }

    public String getPublicKey() throws InternalServerErrorException {
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException(e.getMessage());
        } 
    }
    
}
