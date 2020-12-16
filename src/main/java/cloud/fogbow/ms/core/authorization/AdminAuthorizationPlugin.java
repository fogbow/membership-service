package cloud.fogbow.ms.core.authorization;

import java.util.HashSet;
import java.util.Set;

import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PropertiesHolder;

public class AdminAuthorizationPlugin implements AuthorizationPlugin<AdminOperation> {

	private Set<String> adminsIds;
	
	public AdminAuthorizationPlugin() throws ConfigurationErrorException {
		adminsIds = new HashSet<String>();
		String adminsIdsString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ADMINS_IDS);
		
		if (adminsIdsString.isEmpty()) {
			// TODO add message
			throw new ConfigurationErrorException();
		}
		
		// TODO constant
		for (String adminId : adminsIdsString.split(",")) {
			adminsIds.add(adminId);
		}
	}
	
	
	@Override
	public boolean isAuthorized(SystemUser systemUser, AdminOperation operation) throws UnauthorizedRequestException {
		String userId = systemUser.getId();
		
		if (!adminsIds.contains(userId)) {
			// TODO add message
			throw new UnauthorizedRequestException();
		}
		
		return true;
	}

}
