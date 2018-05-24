package org.fogbowcloud.membershipservice;

import java.io.IOException;
import java.util.List;

public interface MembershipService {

    public List<String> listMembers() throws IOException;
}
