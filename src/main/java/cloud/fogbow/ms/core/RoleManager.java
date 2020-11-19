package cloud.fogbow.ms.core;

import cloud.fogbow.ms.core.models.AuthorizableOperation;

public interface RoleManager {

    // TODO documentation
    boolean isUserAuthorized(String userId, AuthorizableOperation operation);

}