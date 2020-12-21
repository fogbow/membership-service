package cloud.fogbow.ms.api.parameters;

public class ProviderPermission {
    private String provider;
    private boolean target;
    private boolean requester;
    
    public String getProvider() {
        return provider;
    }
    
    public boolean getTarget() {
        return target;
    }
    
    public boolean getRequester() {
        return requester;
    }
}
