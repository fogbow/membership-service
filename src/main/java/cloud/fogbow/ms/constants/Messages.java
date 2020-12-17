package cloud.fogbow.ms.constants;

public class Messages {
    public static class Exception {
        public static final String DEFAULT_ROLE_NAME_IS_INVALID = "Default role name is invalid.";
        public static final String GENERIC_EXCEPTION_S = "Operation returned error: %s.";
        public static final String INVALID_MEMBER_NAME = "Invalid member name in configuration.";
        public static final String MEMBER_IS_NOT_REQUESTER = "Member is not requester.";
        public static final String MEMBER_IS_NOT_TARGET = "Member is not target.";
        public static final String PROVIDER_IS_ALREADY_A_MEMBER = "Provider is already a member";
        public static final String UNABLE_TO_FIND_CLASS_S = "Unable to find class %s.";
    }

    public static class Log {
        public static final String INTERNAL_SERVER_ERROR = "Internal server error.";
        public static final String GET_PUBLIC_KEY = "Get public key received.";
    }
}
