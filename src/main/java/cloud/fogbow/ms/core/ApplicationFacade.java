package cloud.fogbow.ms.core;

import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;

public class ApplicationFacade {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;
    private MembershipService membershipService;
    
    public static ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    private ApplicationFacade() {

    }
    
    public List<String> listMembers() throws Exception {
        return this.membershipService.listMembers();
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

    public boolean isTargetAuthorized(String provider) {
        return this.membershipService.isTargetAuthorized(provider);
    }

    public boolean isRequesterAuthorized(String provider) {
        return this.membershipService.isRequesterAuthorized(provider);
    }

    public void setMembershipService(MembershipService membershipService) {
        this.membershipService = membershipService;
    }
}
