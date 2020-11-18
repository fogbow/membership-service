package cloud.fogbow.ms.api.http.request;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.ms.api.http.response.MembersList;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.ApplicationFacade;
import cloud.fogbow.ms.core.models.operation.RasAuthorizableOperation;

@CrossOrigin
@RestController
@RequestMapping(value = Authorization.ENDPOINT)
public class Authorization {
    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "authorized";

    private static final Logger LOGGER = Logger.getLogger(Authorization.class);
    
    // TODO add documentation
    // TODO create a constant for header value
    @GetMapping
    public ResponseEntity<Boolean> isAuthorized(
            @RequestHeader(required = false, value = "Fogbow-User-Token") String systemUserToken, 
            @RequestBody RasAuthorizableOperation operation) throws FogbowException {
        Boolean authorized = ApplicationFacade.getInstance().isAuthorized(systemUserToken, operation);
        return new ResponseEntity<Boolean>(authorized, HttpStatus.OK);
    }

}
