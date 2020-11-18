package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;
import cloud.fogbow.ms.core.models.operation.OperationType;
import cloud.fogbow.ms.core.models.permission.AllowOnlyPermission;
import cloud.fogbow.ms.MembershipService;

public class WhiteList implements MembershipService {

    private static final String SEPARATOR = ",";

    private List<String> membersList;
    private Map<String, Permission> membersPermissions;
    
    public WhiteList() {
        this.membersPermissions = new HashMap<String, Permission>();
        this.membersList = readMembers();
    }

    /**
     * Read list of XMPP members ID from membership config file.
     */
    @Override
    public List<String> listMembers() {
        return this.membersList;
    }

    private List<String> readMembers() {
        List<String> membersList = new ArrayList<>();

        String membersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        for (String member : membersListStr.split(SEPARATOR)) {
            member = member.trim();
            membersList.add(member);
        }

        for (String member : membersList) {
            String permissionName = PropertiesHolder.getInstance().getProperty(member);
            String permissionType = PropertiesHolder.getInstance().getProperty(permissionName);
            // TODO create a permission instantiator
            if (permissionType.equals("AllowOnly")) {
                // TODO move this to constructor
                Set<OperationType> operations = new HashSet<OperationType>();
                String operationTypesString = PropertiesHolder.getInstance().getProperty(permissionName + "_operations");
                
                for (String operationString : operationTypesString.split(",")) {
                    operations.add(OperationType.fromString(operationString.trim()));
                }
                
                Permission p = new AllowOnlyPermission(operations);
                this.membersPermissions.put(member, p);
            }
        }

        return membersList;
    }

    @Override
    public boolean isMember(String provider) {
        return this.membersList.contains(provider);
    }

    @Override
    public boolean canPerformOperation(String provider, AuthorizableOperation operation) {
        // TODO check contains
        return this.membersPermissions.get(provider).isAuthorized(operation);
    }
}
