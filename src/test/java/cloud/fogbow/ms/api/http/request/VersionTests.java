package cloud.fogbow.ms.api.http.request;

import cloud.fogbow.ms.constants.SystemConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class VersionTests {
    private String VALID_CONF = "ms-test.conf";
    private String VALID_CONF_WITHOUT_BUILD_PROPERTY = "ms-test-nobuild.conf";
    private String INVALID_CONF = "invalid.conf";

    @Test
    public void testVersion() throws Exception {
        Version version = new Version();

        String build = version.getVersionNumber(this.VALID_CONF);

        Assert.assertEquals(SystemConstants.API_VERSION_NUMBER + "-" + "abcd", build);
    }

    @Test
    public void testVersionWithoutBuildProperty() throws Exception {
        Version version = new Version();

        String build = version.getVersionNumber(this.VALID_CONF_WITHOUT_BUILD_PROPERTY);

        Assert.assertTrue(build.equals(SystemConstants.API_VERSION_NUMBER + "-" + "[testing mode]"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testListMembersWithInvalidConfPath() throws FileNotFoundException {
        Version version = new Version();
        version.getVersionNumber(this.INVALID_CONF);
    }
}
