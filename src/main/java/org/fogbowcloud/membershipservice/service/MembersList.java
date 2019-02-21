package org.fogbowcloud.membershipservice.service;

import java.util.List;

public class MembersList {
    private List<String> members;

    public MembersList() {}

    public MembersList(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
