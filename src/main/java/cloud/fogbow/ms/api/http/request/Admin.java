package cloud.fogbow.ms.api.http.request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.ms.api.http.CommonKeys;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.ApplicationFacade;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping(value = Admin.ADMIN_ENDPOINT)
// TODO documentation
public class Admin {
    public static final String ADMIN_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "admin";
    public static final String RELOAD_ENDPOINT = ADMIN_ENDPOINT + "/reload";
	
    // TODO documentation
    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    public ResponseEntity<Boolean> reload(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken) throws FogbowException {
        ApplicationFacade.getInstance().reload(systemUserToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
