package com.example.mymodule.controller.openfireService;

import com.example.mymodule.businessObject.common.Constants;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import org.igniterealtime.restclient.entity.GroupEntity;
import org.igniterealtime.restclient.entity.MUCRoomEntity;
import org.igniterealtime.restclient.entity.UserEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by kamaldua on 06/05/2016.
 */
public class restAPI {

    public static void createUser(String userName, String name, String mailId, String password)
    {
        // Set Shared secret key
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        // Create a new user (username, name, email, passowrd). There are more user settings available.
        UserEntity userEntity = new UserEntity(userName, name, mailId, password);
        javax.ws.rs.core.Response response = restApiClient.createUser(userEntity);

        GroupEntity gpEntity = new GroupEntity();

        String gpName = Constants.getGroupName(userName);
        gpEntity.setName(gpName);
        response = restApiClient.createGroup(gpEntity);

        restApiClient.addUserToGroup(userName,gpName);
    }

    public static void createCircle(String circleName, String circlePass,  ArrayList<String> owners, ArrayList<String> members)
    {
        // Set Shared secret key
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);


        MUCRoomEntity chatRoom = new MUCRoomEntity();

        //chatRoom.setAdmins(owners);
        chatRoom.setOwners(owners);
        chatRoom.setMembers(members);
        chatRoom.setRoomName(circleName);
        chatRoom.setNaturalName(circleName);
        chatRoom.setDescription(circleName);
        chatRoom.setPublicRoom(false);
        chatRoom.setPassword(circlePass);
        /** @Edited by Yassir */
        chatRoom.setPersistent(true);
        chatRoom.setModerated(false);
        chatRoom.setMembersOnly(false);
        chatRoom.setCanAnyoneDiscoverJID(true);
        chatRoom.setCanOccupantsInvite(true);
        chatRoom.setCanOccupantsChangeSubject(true);
        chatRoom.setLoginRestrictedToNickname(false);
        chatRoom.setCanChangeNickname(true);
        chatRoom.setRegistrationEnabled(true);
        chatRoom.setLogEnabled(true);

        List<String> broadcastPresenceRoles = new ArrayList<>();
        broadcastPresenceRoles.add("moderator");
        broadcastPresenceRoles.add("participant");
        broadcastPresenceRoles.add("visitor");
        chatRoom.setBroadcastPresenceRoles(broadcastPresenceRoles);

        javax.ws.rs.core.Response response = restApiClient.createChatRoom(chatRoom);
    }

    public static void addNewFriend(String userContact, String friendContact)
    {
        // Set Shared secret key
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        String gpName = Constants.getGroupName(userContact);
        restApiClient.addUserToGroup(friendContact,gpName);

        gpName = Constants.getGroupName(friendContact);
        restApiClient.addUserToGroup(userContact,gpName);
    }

    public static void deleteExistingFriend(String userContact, String friendContact)
    {
        // Set Shared secret key
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        String gpName = Constants.getGroupName(userContact);
        restApiClient.deleteUserFromGroup(friendContact, gpName);

        gpName = Constants.getGroupName(friendContact);
        restApiClient.deleteUserFromGroup(userContact, gpName);
    }

    public static void removeMemberFromCircle(String xmppCircle, String friendContact)
    {
        // Set Shared secret key
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        MUCRoomEntity room = restApiClient.getChatRoom(xmppCircle);
        List<String> mems = room.getMembers();

        Iterator<String> i = mems.iterator();
        while (i.hasNext()) {
            String member = i.next();
            if(member.equals(Constants.getMemberWithURL(friendContact))) {
                i.remove();
            }
        }
        room.setMembers(mems);

        restApiClient.updateChatRoom(room);
    }


    public static void addMemberToCircle(String xmppCircle, String friendContact)
    {
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        restApiClient.addMember(xmppCircle,Constants.getMemberWithURL(friendContact));
    }

    public static void deleteCircle(String xmppCircle) {
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        restApiClient.deleteChatRoom(xmppCircle);
    }

    public static void updateUserPassword(String userName, String newPassword) {
        AuthenticationToken authenticationToken = new AuthenticationToken(Constants.XMPP_AUTH_TOKEN);
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient(Constants.XMPP_URL, Constants.XMPP_PORT, authenticationToken);

        UserEntity entity = restApiClient.getUser(userName);
        if(newPassword.length()>0)
        {
            entity.setPassword(newPassword);
            javax.ws.rs.core.Response response = restApiClient.updateUser(entity);
        }
    }

}
