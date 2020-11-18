package cloud.fogbow.ms.core.models;

import cloud.fogbow.ms.core.models.operation.OperationType;

public interface AuthorizableOperation {
    String getTargetProvider();
    OperationType getOperationType();
}
