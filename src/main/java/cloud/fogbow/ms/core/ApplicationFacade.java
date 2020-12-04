package cloud.fogbow.ms.core;

import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.ms.core.plugins.AuthorizationPlugin;

public class ApplicationFacade {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;
    private AuthorizationPlugin authorizationPlugin;
    
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
    
    public List<String> listMembers() throws Exception {
        return this.authorizationPlugin.listMembers();
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

    public boolean isAuthorized(String provider) {
        return this.authorizationPlugin.isAuthorized(provider);
    }
}
