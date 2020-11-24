package cloud.fogbow.ms.api.http.response;

public class AuthorizationResponse {

    private Boolean authorized;
    private String token;
    
    public AuthorizationResponse(Boolean authorized, String token) {
        this.authorized = authorized;
        this.token = token;
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
