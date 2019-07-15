package cloud.fogbow.ms.api.http.request;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.common.constants.ApiDocumentation;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.ms.api.http.response.VersionNumber;
import cloud.fogbow.ms.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.constants.Messages;
import cloud.fogbow.ms.constants.SystemConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = Version.VERSION_ENDPOINT)
@Api(description = ApiDocumentation.Version.API)
public class Version {

	private static final Logger LOGGER = Logger.getLogger(Version.class);

	public static final String VERSION_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "version";

	@ApiOperation(value = ApiDocumentation.Version.GET_OPERATION)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<VersionNumber> getVersion() throws FileNotFoundException {
		String versionNumber = getVersionNumber();
		return new ResponseEntity<VersionNumber>(new VersionNumber(versionNumber), HttpStatus.OK);
	}

	public String getVersionNumber() throws FileNotFoundException {
		String path = HomeDir.getPath();
		return getVersionNumber(path + SystemConstants.CONF_FILE_NAME);
	}

	// Used in tests only
	protected String getVersionNumber(String filePath) throws FileNotFoundException {
		return SystemConstants.API_VERSION_NUMBER + "-" + readBuildFromFile(filePath);
	}

	private String readBuildFromFile(String membershipConfPath) throws FileNotFoundException {
		InputStream input = loadFileInputStream(membershipConfPath);
		Properties properties = new Properties();
		String build = "empty";

		try {
			properties.load(input);

			build = properties.getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
					ConfigurationPropertyDefaults.BUILD_NUMBER);
		} catch (Exception e) {
			LOGGER.warn(String.format(Messages.Warn.ERROR_READING_CONF_FILE, membershipConfPath), e);
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				LOGGER.warn(String.format(Messages.Warn.ERROR_CLOSING_CONF_FILE, membershipConfPath), e);
			}
		}
		return build;
	}

	private InputStream loadFileInputStream(String membershipConfPath) throws FileNotFoundException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(membershipConfPath);
		} catch (Exception e) {
			LOGGER.error(String.format(Messages.Error.CONFIGURATION_FILE_NOT_FOUND, membershipConfPath), e);
		}
		return inputStream;
	}
}
