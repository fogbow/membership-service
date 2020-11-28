package cloud.fogbow.ms.core.models.operation;

import com.fasterxml.jackson.annotation.JsonValue;

// FIXME this is a copy of the Operation enum in RAS
// We use this in MS to avoid referencing RAS code here
// Maybe we should move these enums to common
public enum OperationType {
    CREATE("create"),
    GET_ALL("getAll"),
    GET("get"),
    DELETE("delete"),
    GET_USER_ALLOCATION("getUserAllocation"),
    TAKE_SNAPSHOT("takeSnapshot"),
    RELOAD("reload"),
    PAUSE("pause"),
    HIBERNATE("hibernate"),
    RESUME("resume");

    private String value;

    OperationType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
    
    public static OperationType fromString(String value) {
        for (OperationType operationValue : values()) {
            if (operationValue.getValue().equals(value)) { 
                return operationValue;
            }
        }
        throw new IllegalArgumentException();
    }
}
