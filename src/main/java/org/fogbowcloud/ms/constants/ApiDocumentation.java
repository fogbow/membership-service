package org.fogbowcloud.ms.constants;

public class ApiDocumentation {

    public static class ApiInfo {
        public static final String BASE_PACKAGE = "org.fogbowcloud.membershipservice";
        public static final String API_TITLE = "Fogbow Membership Service API";
        public static final String API_DESCRIPTION = "This API introduces readers to Fogbow MS REST API, "
                + "provides guidelines on how to use it, and describe the available features accessible from it.";
        public static final String CONTACT_NAME = "Fogbow";
        public static final String CONTACT_URL = "https://www.fogbowcloud.org";
        public static final String CONTACT_EMAIL = "contact@fogbowcloud.org";
    }

    public static class Version {
        public static final String API = "Queries the version of the service's API.";
        public static final String GET_OPERATION = "Returns the version of the API.";
    }

    public static class Membership {
        public static final String API = "Queries the members currently belonging to the federation";
        public static final String DESCRIPTION = "Lists the member ids in the federation.";
    }

}
