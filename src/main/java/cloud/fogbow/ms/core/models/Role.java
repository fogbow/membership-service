package cloud.fogbow.ms.core.models;

// TODO documentation
public interface Role {
    String getName();
    boolean canPerformOperation(AuthorizableOperation operation);
}
