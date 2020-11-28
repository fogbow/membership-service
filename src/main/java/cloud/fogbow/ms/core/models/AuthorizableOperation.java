package cloud.fogbow.ms.core.models;

import cloud.fogbow.ms.core.models.operation.OperationType;

// TODO documentation
public interface AuthorizableOperation {
    String getTargetProvider();
    OperationType getOperationType();
}
