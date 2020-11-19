package cloud.fogbow.ms.core.models.operation;

import cloud.fogbow.ms.core.models.AuthorizableOperation;

public class RasAuthorizableOperation implements AuthorizableOperation {

    private String targetProvider;
    private OperationType operationType;
    
    public RasAuthorizableOperation() {
        
    }
    
    public RasAuthorizableOperation(String provider, OperationType operationType) {
        this.targetProvider = provider;
        this.operationType = operationType;
    }

    @Override
    public String getTargetProvider() {
        return targetProvider;
    }

    @Override
    public OperationType getOperationType() {
        return operationType;
    }

}
