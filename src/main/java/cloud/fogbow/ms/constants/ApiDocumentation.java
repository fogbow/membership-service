package cloud.fogbow.ms.constants;

public class ApiDocumentation {

    public static class ApiInfo {
        public static final String API_TITLE = "Fogbow Membership Service (MS) API";
        public static final String API_DESCRIPTION = "This API allows users to retrieve the list of "
                + "other known service providers.";
    }

    public static class Membership {
        public static final String API = "Queries the members currently known.";
        public static final String DESCRIPTION = "Lists the ids of the known members.";
    }

    public static class Model {
        public static final String MEMBERS_LIST = "[\n" +
                "    \"provider1.domain1\",\n" +
                "    \"provider2.domain2\"\n" +
                "  ]\n";
    }
}
