package cloud.fogbow.ms.api.http.request;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.ms.constants.SystemConstants;

@RunWith(SpringRunner.class)
@WebMvcTest
public class VersionTests {

    private static final String BASE_ENDPOINT = "/";
    private static final String BUILD_PROPERTY = "abcd";
    private static final String BUILD_PROPERTY_EMPTY = "empty";
    private static final String INVALID_CONF = "invalid.conf";
    private static final String VERSION_NUMBER_SEPARATOR = "-";
    private static final String TESTING_MODE = "[testing mode]";
    private static final String VALID_CONF = "ms-test.conf";
    private static final String VALID_CONF_WITHOUT_BUILD_PROPERTY = "ms-test-nobuild.conf";

    @Autowired
    private MockMvc mockMvc;

    private Version version;

    @Before
    public void setup() {
        this.version = new Version();
    }

    // test case: When invoking the getVersionNumber method with a parameter
    // containing a valid file path, it must return the version number concatenated
    // with the value of the file build number property passed by parameter.
    @Test
    public void testVersionWithValidConfPath() throws Exception {
        // exercise
        String build = this.version.getVersionNumber(HomeDir.getPath() + VALID_CONF);

        // verify
        Assert.assertEquals(SystemConstants.API_VERSION_NUMBER + VERSION_NUMBER_SEPARATOR + BUILD_PROPERTY, build);
    }

    // test case: When invoking the getVersionNumber method with a parameter
    // containing a valid file path but no build number property in this file,
    // it must return the version of the service in testing mode.
    @Test
    public void testVersionWithoutBuildProperty() throws Exception {
        // exercise
        String build = this.version.getVersionNumber(HomeDir.getPath() + VALID_CONF_WITHOUT_BUILD_PROPERTY);

        // verify
        Assert.assertEquals(SystemConstants.API_VERSION_NUMBER + VERSION_NUMBER_SEPARATOR + TESTING_MODE, build);
    }

    // test case: When invoking the getVersionNumber method with a parameter
    // containing an invalid file path, it must return the version number
    // concatenated with the word empty.
    @Test
    public void testVersionWithInvalidConfPath() throws FileNotFoundException {
        // exercise
        String build = this.version.getVersionNumber(INVALID_CONF);

        // verify
        Assert.assertEquals(SystemConstants.API_VERSION_NUMBER + VERSION_NUMBER_SEPARATOR + BUILD_PROPERTY_EMPTY,
                build);
    }

    // test case: When invoking the getVersionNumber method with no parameters, it
    // must use a default file and return the version number concatenated the value
    // of the default file build number property.
    @Test
    public void testVersionWithoutConfPath() throws Exception {
        // exercise
        String build = this.version.getVersionNumber();

        // verify
        Assert.assertEquals(SystemConstants.API_VERSION_NUMBER + VERSION_NUMBER_SEPARATOR + BUILD_PROPERTY, build);
    }

    // test case: When executing a GET request for the "/ms/version" endpoint, it
    // must invoke the getVersion method by returning the Status OK and the version
    // content in format json.
    @Test
    public void testGetVersion() throws Exception {
        // set up
        String expected = getJsonResponseContent();

        // exercise
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT + Version.VERSION_ENDPOINT))
                // verify
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    private String getJsonResponseContent() {
        return "{\"version\":" + SystemConstants.API_VERSION_NUMBER 
                + VERSION_NUMBER_SEPARATOR 
                + BUILD_PROPERTY 
                + "}";
    }

}
