package org.fogbowcloud.ms.api.http.response;

public class VersionNumber {
    private String version;

    public VersionNumber() {
    }

    public VersionNumber(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
