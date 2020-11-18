package cloud.fogbow.ms.core.models.operation;

import com.fasterxml.jackson.annotation.JsonValue;

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
