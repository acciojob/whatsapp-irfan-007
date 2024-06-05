package com.driver;

public class SendMessageRequest {
    private Message message;
    private User sender;
    private Group group;

    public SendMessageRequest() {
    }

    public SendMessageRequest(Message message, User sender, Group group) {
        this.message = message;
        this.sender = sender;
        this.group = group;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
