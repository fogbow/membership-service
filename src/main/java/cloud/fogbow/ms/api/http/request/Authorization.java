package cloud.fogbow.ms.api.http.request;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.ms.api.http.CommonKeys;
import cloud.fogbow.ms.api.http.response.AuthorizationResponse;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.ApplicationFacade;
import cloud.fogbow.ms.core.models.operation.RasAuthorizableOperation;

@CrossOrigin
@RestController
@RequestMapping(value = Authorization.AUTHORIZED_ENDPOINT)
public class Authorization {
    public static final String AUTHORIZED_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "authorized";

    private static final Logger LOGGER = Logger.getLogger(Authorization.class);

    // TODO add documentation
    @PostMapping
    public ResponseEntity<AuthorizationResponse> isAuthorized(
            @RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken, 
            @RequestBody RasAuthorizableOperation operation) throws FogbowException {
        AuthorizationResponse response = ApplicationFacade.getInstance().isAuthorized(systemUserToken, operation);
        return new ResponseEntity<AuthorizationResponse>(response, HttpStatus.OK);
    }

}
