package cloud.fogbow.ms.core;

import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ms.core.models.AuthorizableOperation;

public interface RoleManager {

    // TODO documentation
    boolean isUserAuthorized(SystemUser user, AuthorizableOperation operation);
    boolean isRemoteUserAuthorized(SystemUser user, AuthorizableOperation operation);
    void setRoles(SystemUser user);

}