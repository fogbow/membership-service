package org.fogbowcloud.membershipservice.http;

import org.fogbowcloud.membershipservice.constants.ApiDocumentation;
import org.fogbowcloud.membershipservice.constants.SystemConstants;
import org.fogbowcloud.membershipservice.http.Version;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class VersionTests {
    private String VALID_PATH_CONF = "src/test/resources/ms-test.conf";
    private String VALID_PATH_CONF_WITHOUT_BUILD_PROPERTY = "src/test/resources/ms-test-nobuild.conf";
    private String INVALID_PATH_CONF = "invalid.conf";

    @Test
    public void testVersion() throws Exception {
        Version version = new Version();

        String build = version.getVersionNumber(this.VALID_PATH_CONF);

        Assert.assertEquals(SystemConstants.API_VERSION_NUMBER + "-" + "abcd", build);
    }

    @Test
    public void testVersionWithoutBuildProperty() throws Exception {
        Version version = new Version();

        String build = version.getVersionNumber(this.VALID_PATH_CONF_WITHOUT_BUILD_PROPERTY);

        Assert.assertTrue(build.equals(SystemConstants.API_VERSION_NUMBER + "-" + "[testing mode]"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testListMembersWithInvalidConfPath() throws FileNotFoundException {
        Version version = new Version();
        version.getVersionNumber(this.INVALID_PATH_CONF);
    }
}
