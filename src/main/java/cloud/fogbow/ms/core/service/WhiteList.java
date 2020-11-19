package cloud.fogbow.ms.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.fogbow.ms.constants.ConfigurationPropertyKeys;
import cloud.fogbow.ms.core.MembershipService;
import cloud.fogbow.ms.core.PermissionInstantiator;
import cloud.fogbow.ms.core.PropertiesHolder;
import cloud.fogbow.ms.core.models.AuthorizableOperation;
import cloud.fogbow.ms.core.models.Permission;

public class WhiteList implements MembershipService {

    private static final String SEPARATOR = ",";

    private List<String> membersList;
    private Map<String, Permission> membersPermissions;
    
    public WhiteList(PermissionInstantiator permissionInstantiator) {
        this.membersPermissions = new HashMap<String, Permission>();
        this.membersList = readMembers(permissionInstantiator);
    }

    /**
     * Read list of XMPP members ID from membership config file.
     */
    @Override
    public List<String> listMembers() {
        return this.membersList;
    }

    private List<String> readMembers(PermissionInstantiator permissionInstantiator) {
        List<String> membersList = new ArrayList<>();

        String membersListStr = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.MEMBERS_LIST_KEY);
        for (String member : membersListStr.split(SEPARATOR)) {
            member = member.trim();
            membersList.add(member);
        }

        for (String member : membersList) {
            String permissionName = PropertiesHolder.getInstance().getProperty(member);
            String permissionType = PropertiesHolder.getInstance().getProperty(permissionName);
            
            Permission permission = permissionInstantiator.getPermissionInstance(permissionType, permissionName);
            this.membersPermissions.put(member, permission);
        }

        return membersList;
    }

    @Override
    public boolean isMember(String provider) {
        return this.membersList.contains(provider);
    }

    @Override
    public boolean canPerformOperation(AuthorizableOperation operation) {
        String targetProvider = operation.getTargetProvider();
        if (!isMember(targetProvider)) {
            return false;
        }
        Permission providerPermission = this.membersPermissions.get(targetProvider);
        return providerPermission.isAuthorized(operation);
    }
}
