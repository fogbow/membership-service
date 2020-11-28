package cloud.fogbow.ms.core.models;

// TODO documentation
public interface Permission {

    boolean isAuthorized(AuthorizableOperation operation);
}
