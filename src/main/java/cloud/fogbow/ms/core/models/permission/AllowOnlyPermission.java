package cloud.fogbow.ms.core.models.permission;

import java.util.HashSet;
import java.util.Set;

import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.operation.OperationType;

public class AllowOnlyPermission implements Permission {

    private Set<OperationType> allowedOperationTypes;
    
    public AllowOnlyPermission(Set<OperationType> allowedOperationTypes) {
        this.allowedOperationTypes = allowedOperationTypes;
    }
    
    public AllowOnlyPermission(String permissionName) {
        this.allowedOperationTypes = new HashSet<OperationType>();
        
        // TODO check how this works with empty operation list
        String operationTypesString = PropertiesHolder.getInstance().getProperty(permissionName + 
                SystemConstants.OPERATIONS_LIST_KEY_SUFFIX);
        
        for (String operationString : operationTypesString.split(SystemConstants.OPERATION_NAME_SEPARATOR)) {
            this.allowedOperationTypes.add(OperationType.fromString(operationString.trim()));
        }
    }

    @Override
    public boolean isAuthorized(AuthorizableOperation operation) {
        return this.allowedOperationTypes.contains(operation.getOperationType());
    }
}
