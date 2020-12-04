package cloud.fogbow.ms.core.plugins;

import java.util.List;

// TODO documentation
public interface AuthorizationPlugin {
    List<String> listMembers() throws Exception;
    boolean isAuthorized(String provider);
}
