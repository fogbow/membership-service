package cloud.fogbow.ms.core.models;

public interface Permission {

    boolean isAuthorized(AuthorizableOperation operation);
}
