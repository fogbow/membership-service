package cloud.fogbow.ms.constants;

public class Messages {
    public static class Exception {
    }

    public static class Fatal {
    }

    public static class Warn {
        public static final String ERROR_READING_CONF_FILE = "Error trying to read configuration file: %s.";
        public static final String ERROR_CLOSING_CONF_FILE = "Error trying to close configuration file: %s.";
    }

    public static class Info {
    }

    public static class Error {
        public static final String CONFIGURATION_FILE_NOT_FOUND = "Configuration file not found.";
        public static final String INTERNAL_SERVER_ERROR = "Internal server error.";
    }
}
