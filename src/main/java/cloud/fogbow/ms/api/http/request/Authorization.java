package cloud.fogbow.ms.api.http.request;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.ms.api.http.response.Authorized;
import cloud.fogbow.ms.api.parameters.Provider;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.ApplicationFacade;

@CrossOrigin
@RestController
@RequestMapping(value = Authorization.AUTHORIZED_ENDPOINT)
public class Authorization {
    public static final String AUTHORIZED_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "authorized";

    private static final Logger LOGGER = Logger.getLogger(Authorization.class);

    // TODO add documentation
    @PostMapping
    public ResponseEntity<Authorized> isAuthorized(
            @RequestBody Provider provider) {
        boolean authorized = ApplicationFacade.getInstance().isAuthorized(provider.getProvider());
        return new ResponseEntity<Authorized>(new Authorized(authorized),
                HttpStatus.OK);
    }

}
