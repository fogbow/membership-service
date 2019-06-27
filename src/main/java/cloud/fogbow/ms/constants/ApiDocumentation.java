package cloud.fogbow.ms.constants;

public class ApiDocumentation {

    public static class ApiInfo {
        public static final String API_TITLE = "Fogbow Membership Service API";
        public static final String API_DESCRIPTION = "This API introduces readers to Fogbow MS REST API, "
                + "provides guidelines on how to use it, and describes the available features accessible from it.";
    }

    public static class Model {
        public static final String MEMBERS_LIST = "[\n" +
                "    \"provider1.domain1\",\n" +
                "    \"provider2.domain2\"\n" +
                "  ]\n";
    }

    public static class Membership {
        public static final String API = "Queries the members currently belonging to the federation";
        public static final String DESCRIPTION = "Lists the member ids in the federation.";
    }

}
