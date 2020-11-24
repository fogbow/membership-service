package cloud.fogbow.ms.core.models.operation;

import java.util.HashMap;
import java.util.Map;

import cloud.fogbow.ms.core.models.AuthorizableOperation;

public class RasAuthorizableOperation implements AuthorizableOperation {

    public static final String OPERATION_TYPE_REQUEST_KEY = "operationType";
    public static final String TARGET_PROVIDER_REQUEST_KEY = "targetProvider";
    private String targetProvider;
    private OperationType operationType;
    
    public RasAuthorizableOperation() {
        
    }
    
    public RasAuthorizableOperation(String provider, String operationType) {
        this(provider, OperationType.fromString(operationType));
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
    
    public Map<String, String> asRequestBody() {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put(TARGET_PROVIDER_REQUEST_KEY, this.targetProvider);
        body.put(OPERATION_TYPE_REQUEST_KEY, this.operationType.getValue());
        return body;
    }

}
