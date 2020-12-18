package cloud.fogbow.ms.api.http.request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.ms.api.http.CommonKeys;
import cloud.fogbow.ms.api.parameters.Provider;
import cloud.fogbow.ms.constants.ApiDocumentation;
import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.ApplicationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping(value = Admin.ADMIN_ENDPOINT)
@Api(description = ApiDocumentation.Admin.API)
public class Admin {
    public static final String ADMIN_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "admin";
    // TODO use these endpoint values
    public static final String RELOAD_ENDPOINT = ADMIN_ENDPOINT + "/reload";
    public static final String ADD_PROVIDER_ENDPOINT = ADMIN_ENDPOINT + "/provider";
	
    @ApiOperation(value = ApiDocumentation.Admin.RELOAD)
    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    public ResponseEntity<Boolean> reload(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken) throws FogbowException {
        ApplicationFacade.getInstance().reload(systemUserToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = ApiDocumentation.Admin.ADD_PROVIDER)
    @RequestMapping(value = "/provider", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addProvider(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken,
    				@RequestBody Provider provider) throws FogbowException {
    	ApplicationFacade.getInstance().addProvider(systemUserToken, provider.getProvider());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = ApiDocumentation.Admin.REMOVE_PROVIDER)
    @RequestMapping(value = "/provider", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> removeProvider(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken,
    				@RequestBody Provider provider) throws FogbowException {
    	ApplicationFacade.getInstance().removeProvider(systemUserToken, provider.getProvider());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = ApiDocumentation.Admin.ADD_TARGET)
    @RequestMapping(value = "/target", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addTarget(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken,
    				@RequestBody Provider provider) throws FogbowException {
    	ApplicationFacade.getInstance().addTargetProvider(systemUserToken, provider.getProvider());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = ApiDocumentation.Admin.ADD_REQUESTER)
    @RequestMapping(value = "/requester", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addRequester(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken,
    				@RequestBody Provider provider) throws FogbowException {
    	ApplicationFacade.getInstance().addRequesterProvider(systemUserToken, provider.getProvider());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = ApiDocumentation.Admin.REMOVE_TARGET)
    @RequestMapping(value = "/target", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> removeTarget(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken,
    				@RequestBody Provider provider) throws FogbowException {
    	ApplicationFacade.getInstance().removeTargetProvider(systemUserToken, provider.getProvider());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = ApiDocumentation.Admin.REMOVE_REQUESTER)
    @RequestMapping(value = "/requester", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> removeRequester(
    				@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    				@RequestHeader(required = false, value = CommonKeys.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken,
    				@RequestBody Provider provider) throws FogbowException {
    	ApplicationFacade.getInstance().removeRequesterProvider(systemUserToken, provider.getProvider());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
