package cloud.fogbow.ms.api.http.response;

import java.util.Map;

public class AuthorizationResponse {
    public static final String AUTHORIZATION_RESPONSE_AUTHORIZED_FIELD = "authorized";
    public static final String AUTHORIZATION_RESPONSE_TOKEN_FIELD = "token";
    private Boolean authorized;
    private String token;
    
    public AuthorizationResponse(Boolean authorized, String token) {
        this.authorized = authorized;
        this.token = token;
    }
    
    public AuthorizationResponse(Map<String, Object> requestResponse) {
        this.token = (String) requestResponse.get(AUTHORIZATION_RESPONSE_TOKEN_FIELD);
        this.authorized = (boolean) requestResponse.get(AUTHORIZATION_RESPONSE_AUTHORIZED_FIELD);
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
