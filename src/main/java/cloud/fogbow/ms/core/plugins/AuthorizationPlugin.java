package cloud.fogbow.ms.core.plugins;

import java.util.List;

import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ms.core.models.AuthorizableOperation;

public interface AuthorizationPlugin {

    boolean isAuthorized(SystemUser user, AuthorizableOperation operation);
    List<String> listMembers() throws Exception;
}
