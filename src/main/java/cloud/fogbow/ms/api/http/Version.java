package cloud.fogbow.ms.api.http;

import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.ms.constants.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@CrossOrigin
@RestController
@RequestMapping(value = Version.VERSION_ENDPOINT)
@Api(description = ApiDocumentation.Version.API)
public class Version {

    public static final String VERSION_ENDPOINT = "version";

    private final Logger LOGGER = Logger.getLogger(Version.class);

    @ApiOperation(value = ApiDocumentation.Version.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getVersion() throws FileNotFoundException {
        String versionNumber = getVersionNumber();
        return new ResponseEntity<>(versionNumber, HttpStatus.OK);
    }

    public String getVersionNumber() throws FileNotFoundException {
        return getVersionNumber(SystemConstants.CONF_FILE_NAME);
    }

    // Used in tests only
    protected String getVersionNumber(String filePath) throws FileNotFoundException {
        return SystemConstants.API_VERSION_NUMBER + "-" + readBuildFromFile(filePath);
    }

    private String readBuildFromFile(String membershipConfPath) throws FileNotFoundException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(HomeDir.getPath() + membershipConfPath);
        String build = "empty";

        try {
            properties.load(input);

            build = properties.getProperty(ConfigurationPropertyKeys.BUILD_NUMBER,
                    ConfigurationPropertyDefaults.BUILD_NUMBER);
        } catch (IOException e) {
            LOGGER.warn(String.format(Messages.Warn.ERROR_READING_CONF_FILE, membershipConfPath), e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                LOGGER.warn(String.format(Messages.Warn.ERROR_CLOSING_CONF_FILE, membershipConfPath), e);
            }
        }

        return build;
    }
}
