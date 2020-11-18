package cloud.fogbow.ms.core.models;

public interface Role {
    String getName();
    boolean canPerformOperation(AuthorizableOperation operation);
}
