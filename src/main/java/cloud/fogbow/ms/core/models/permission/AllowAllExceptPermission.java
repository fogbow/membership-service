package cloud.fogbow.ms.core.models.permission;

import java.util.HashSet;
import java.util.Set;

import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.operation.OperationType;

public class AllowAllExceptPermission implements Permission {

    private Set<OperationType> notAllowedOperationTypes;
    
    public AllowAllExceptPermission(Set<OperationType> notAllowedOperationTypes) {
        this.notAllowedOperationTypes = notAllowedOperationTypes;
    }
    
    public AllowAllExceptPermission(String permissionName) {
        this.notAllowedOperationTypes = new HashSet<OperationType>();
        
        // TODO check how this works with empty operation list
        String operationTypesString = PropertiesHolder.getInstance().getProperty(permissionName + 
                SystemConstants.OPERATIONS_LIST_KEY_SUFFIX);
        
        for (String operationString : operationTypesString.split(SystemConstants.OPERATION_NAME_SEPARATOR)) {
            this.notAllowedOperationTypes.add(OperationType.fromString(operationString.trim()));
        }
    }

    @Override
    public boolean isAuthorized(AuthorizableOperation operation) {
        return !this.notAllowedOperationTypes.contains(operation.getOperationType());
    }
}
