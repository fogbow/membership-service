package cloud.fogbow.ms.core.models.permission;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cloud.fogbow.ms.constants.SystemConstants;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.operation.OperationType;
import cloud.fogbow.ms.core.models.operation.RasAuthorizableOperation;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertiesHolder.class})
public class AllowAllExceptPermissionTest {

    private AllowAllExceptPermission permission;
    private String permissionName = "permission1";
    private String operationsNamesString;
    private List<OperationType> notAllowedOperations = Arrays.asList(OperationType.RELOAD, 
                                                                  OperationType.CREATE);
    private List<OperationType> noOperation = new ArrayList<OperationType>();
    
    private String provider = "member1";
    
    private void setUpVariables(List<OperationType> operations) {
        operationsNamesString = generateOperationNamesString(operations);
        
        // set up PropertiesHolder 
        PowerMockito.mockStatic(PropertiesHolder.class);
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.doReturn(operationsNamesString).when(propertiesHolder).getProperty(permissionName + 
                SystemConstants.OPERATIONS_LIST_KEY_SUFFIX);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolder);
        
        permission = new AllowAllExceptPermission(permissionName);
    }
    
    private String generateOperationNamesString(List<OperationType> operations) {
        ArrayList<String> operationsNames = new ArrayList<String>();
        for (OperationType type : operations) {
            operationsNames.add(type.getValue());
        }
        return String.join(SystemConstants.OPERATION_NAME_SEPARATOR, operationsNames);
    }
    
    //
    // TODO documentation
    //
    @Test
    public void testIsAuthorized() {
        setUpVariables(notAllowedOperations);
        
        for (OperationType type : OperationType.values()) {
            RasAuthorizableOperation operation = new RasAuthorizableOperation(provider, type);
            
            if (notAllowedOperations.contains(type)) {
                assertFalse(permission.isAuthorized(operation));                
            } else {
                assertTrue(permission.isAuthorized(operation));
            }
        }
    }
    
    //
    // TODO documentation
    //
    @Test
    public void testIsAuthorizedAllOperationsAreAuthorized() {
        setUpVariables(noOperation);
        
        for (OperationType type : OperationType.values()) {
            RasAuthorizableOperation operation = new RasAuthorizableOperation(provider, type);
            assertTrue(permission.isAuthorized(operation));
        }
    }
}