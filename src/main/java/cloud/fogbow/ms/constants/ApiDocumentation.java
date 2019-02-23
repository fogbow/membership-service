package cloud.fogbow.ms.constants;

public class ApiDocumentation {

    public static class ApiInfo {
        public static final String API_TITLE = "Fogbow Membership Service (MS) API";
        public static final String API_DESCRIPTION = "This API introduces readers to Fogbow MS REST API, "
                + "provides guidelines on how to use it, and describes the available features accessible from it.";
        public static final String CONTACT_NAME = "Fogbow";
        public static final String CONTACT_URL = "http://www.fogbow.cloud";
        public static final String CONTACT_EMAIL = "contact@fogbow.cloud";
    }

    public static class Version {
        public static final String API = "Queries the version of the service's API.";
        public static final String GET_OPERATION = "Returns the version of the API.";
    }

    public static class Membership {
        public static final String API = "Queries the members currently belonging to the federation.";
        public static final String DESCRIPTION = "Lists the ids of the member that belong to the federation.";
    }

}