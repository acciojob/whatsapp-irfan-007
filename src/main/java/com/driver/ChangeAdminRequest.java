package com.driver;

public class ChangeAdminRequest {
    private User approver;
    private User user;
    private Group group;

    public ChangeAdminRequest() {
    }

    public ChangeAdminRequest(User approver, User user, Group group) {
        this.approver = approver;
        this.user = user;
        this.group = group;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
