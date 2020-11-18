package cloud.fogbow.ms.core.models.permission;

import java.util.Set;

import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.operation.OperationType;

public class AllowOnlyPermission implements Permission {

    private Set<OperationType> allowedOperationTypes;
    
    public AllowOnlyPermission(Set<OperationType> allowedOperationTypes) {
        this.allowedOperationTypes = allowedOperationTypes;
    }
    
    @Override
    public boolean isAuthorized(AuthorizableOperation operation) {
        return this.allowedOperationTypes.contains(operation.getOperationType());
    }
}
