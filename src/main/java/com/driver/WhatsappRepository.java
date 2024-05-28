package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private HashSet<Message> messageSet;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String name, String mobile) throws Exception{
        if(userMobile.contains(mobile))
            throw new Exception("User already exists");
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){
//        for(User user:users){
//            createUser(user.getName(),user.getMobile());
//        }
        Group group=new Group();
        if(users.size()>2){
            // it is a new group
            customGroupCount++;
            group.setName("Group "+customGroupCount);
            group.setNumberOfParticipants(users.size());
        }
        else{
            // it is new personal chat
            group.setName(users.get(1).getName());
            group.setNumberOfParticipants(2);
        }
        groupUserMap.put(group,users);
        adminMap.put(group,users.get(0));

        return group;
    }
    public int createMessage(String content){
        messageId++;
        Message message=new Message();
        message.setContent(content);
        message.setId(messageId);
        message.setTimestamp(new Date());
        messageSet.add(message);
        return message.getId();
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group))
            throw new Exception("Group does not exist");
        if(!groupUserMap.get(group).contains(sender))
            throw new Exception("You are not allowed to send message");
        if(!groupMessageMap.containsKey(group)){
            List<Message> messageList=new ArrayList<>();
            messageList.add(message);
            groupMessageMap.put(group,messageList);
            senderMap.put(message,sender);
            return messageList.size();
        }
        List<Message> messageList=groupMessageMap.get(group);
        messageList.add(message);
        groupMessageMap.put(group,messageList);
        senderMap.put(message,sender);
        return messageList.size();
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group))
            throw new Exception("Group does not exist");
        if(!groupUserMap.get(group).contains(user))
            throw new Exception("User is not a participant");
        User admin=adminMap.get(group);
        if(admin.getMobile().equals(user.getMobile()) && admin.getName().equals(user.getName())){
            adminMap.put(group,user);
            return "SUCCESS";
        }
        else
            throw new Exception("Approver does not have rights");
    }

    public int removeUser(User user) throws Exception{
        int flag=0;
        Group thisGroup=null;
        for(Group group:groupUserMap.keySet()){
            if(groupUserMap.get(group).contains(user)){
                flag=1;
                thisGroup=group;
            }
        }
        if(flag==0)
            throw new Exception("User not found");
        if(adminMap.get(thisGroup).getMobile().equals(user.getMobile()))
            throw new Exception("Cannot remove admin");

        // removing user
        int res=0;
        thisGroup.setNumberOfParticipants(thisGroup.getNumberOfParticipants()-1);
        List<User> userList=groupUserMap.get(thisGroup);
        userList.remove(user);
        res+=userList.size();
        groupUserMap.put(thisGroup,userList);
        userMobile.remove(user.getMobile());
        List<Message> messageList=groupMessageMap.get(thisGroup);
        for(Message message:messageList){
            if(senderMap.get(message).getMobile().equals(user.getMobile())){
                senderMap.remove(message);
                messageSet.remove(message);
                messageList.remove(message);
            }
        }
        groupMessageMap.put(thisGroup,messageList);
        res+=messageList.size();
        res+=messageSet.size();
        return res;
    }
    public String findMessage(Date start, Date end, int K) throws Exception{
        PriorityQueue<Message> pq=new PriorityQueue<>((a,b)->{
            if(a.getTimestamp().after(b.getTimestamp()))
                return 0;
            else return 1;
        });
        for(Message message:messageSet){
            pq.add(message);
        }
        for(int i=1;i<K;i++)
            pq.poll();
        return pq.poll().getContent();
    }
}
