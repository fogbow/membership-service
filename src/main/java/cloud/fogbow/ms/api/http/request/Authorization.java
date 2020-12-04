package cloud.fogbow.ms.api.http.request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.ms.api.http.response.Authorized;
import cloud.fogbow.ms.api.parameters.Provider;
import cloud.fogbow.ms.constants.ApiDocumentation;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.ApplicationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = Authorization.AUTHORIZED_ENDPOINT)
@Api(description = ApiDocumentation.Authorization.API)
public class Authorization {
    public static final String AUTHORIZED_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "authorized";

    @ApiOperation(value = ApiDocumentation.Authorization.DESCRIPTION)
    @PostMapping
    public ResponseEntity<Authorized> isAuthorized(
            @RequestBody Provider provider) {
        boolean authorized = ApplicationFacade.getInstance().isAuthorized(provider.getProvider());
        return new ResponseEntity<Authorized>(new Authorized(authorized), HttpStatus.OK);
    }

}
