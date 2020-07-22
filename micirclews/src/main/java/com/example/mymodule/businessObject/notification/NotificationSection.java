package com.example.mymodule.businessObject.notification;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamaldua on 01/10/2016.
 */
public class NotificationSection extends ApiResponse{

    public Timestamp lastUpdatedTime; //lastTimestamp

    public class FriendMessage
    {
        public int msgId;

        public FriendMessage(int id)
        {
            msgId = id;
        }
    }
    public class CircleMessage
    {
        public int msgId;

        public CircleMessage(int id)
        {
            msgId = id;
        }
    }
    public class FriendDelete
    {
        public int frId;

        public FriendDelete(int id)
        {
            frId = id;
        }
    }
    private List<FriendMessage> frsMessages = new ArrayList<FriendMessage>();
    private List<CircleMessage> cleMessages = new ArrayList<CircleMessage>();
    private List<FriendDelete> deletedFrnds = new ArrayList<FriendDelete>();

    public void setFrndMessages(int id)
    {
        frsMessages.add(new FriendMessage(id));
    }
    public List<FriendMessage> getFrndMessages()
    {
        return frsMessages;
    }

    public void setCrclMessages(int id)
    {
        cleMessages.add(new CircleMessage(id));
    }
    public List<CircleMessage> getCrclMessages()
    {
        return cleMessages;
    }

    public void setDelFriend(int id)
    {
        deletedFrnds.add(new FriendDelete(id));
    }
    public List<FriendDelete> getDelFriend()
    {
        return deletedFrnds;
    }

    public int[] frMessIds;
    //friendMessageIds
    public int[] cleMessIds;
    //circleMessageIds

    // Friend list section
    public class FriendRequest
    {
        public int fReqID; //friendRequestId
        public int fUserID; //friendUserId
        public String cyCode; //countryCode
        public String contNo; //contactNo
        public String name; //name
        public String image; //imageUrl
        public String status; //status

    }

    public FriendRequest getFriendRequestObj()
    {
        return new FriendRequest();
    }
    //1. new friend request
    public List<FriendRequest> newFrsReq = new ArrayList<FriendRequest>();
    //2. someone respond his friend request
    public List<FriendRequest> resFrsReq = new ArrayList<FriendRequest>();

    //3. Someone deleted you as a friend
    public int[] deleteYouFromFriendList;

    public class FriendImage
    {
        public int frUserID; //friendUserID
        public String thumb; //thumbImage
        public String img; //fullImage
    }
    //4. Friends Image Changed
    public List<FriendImage> frImages = new ArrayList<FriendImage>();

    public void addFriendImage(int frUserID, String thumb, String img)
    {
        FriendImage obj = new FriendImage();
        obj.frUserID = frUserID;
        obj.img = img;
        obj.thumb = thumb;

        frImages.add(obj);
    }

    //Circle List Update

    public class CircleRequest
    {
        public int reqID; //CircleMemberRequestID
        public String cCode; //AdminCountryCode
        public String contNo; //AdminContactNo
        public String thumb; //CircleImageUrl
        public String img; //CircleImageUrl
        public String cName; //CircleName
    }
    //1. Someone send add into Circle request
    public List<CircleRequest> newCleReq = new ArrayList<CircleRequest>();

    public void addCircleRequest(int reqID, String cCode, String contNo, String thumb, String img, String cName )
    {
        CircleRequest obj = new CircleRequest();
        obj.reqID = reqID;
        obj.cCode = cCode;
        obj.contNo = contNo;
        obj.thumb = thumb;
        obj.img = img;
        obj.cName = cName;

        newCleReq.add(obj);
    }

    public class NewMemberInCircle
    {
        public int crcID;
        public int userID;
        public String cCode;
        public String conNo;
        public String thumb;
        public String img;
        public boolean isAdmin;
    }
    //2. member respond to Circle add request
    public List<NewMemberInCircle> newMembersInCircle = new ArrayList<NewMemberInCircle>();

    public void addNewMemberInCircle(int crcID, int userID, String cCode, String conNo, String thumb, String img, boolean isAdmin )
    {
        NewMemberInCircle obj = new NewMemberInCircle();
        obj.crcID = crcID;
        obj.userID = userID;
        obj.cCode = cCode;
        obj.conNo = conNo;
        obj.thumb = thumb;
        obj.img = img;
        obj.isAdmin = isAdmin;

        newMembersInCircle.add(obj);
    }

    public class DeletedMemberFromCircle
    {
        public int crcID;
        public int userID;
    }
    //3.Circle member deleted
    public List<DeletedMemberFromCircle> deletedMembersInCircle = new ArrayList<DeletedMemberFromCircle>();

    public void delMemberInCircle(int crcID, int userID)
    {
        DeletedMemberFromCircle obj = new DeletedMemberFromCircle();
        obj.crcID = crcID;
        obj.userID = userID;

        deletedMembersInCircle.add(obj);
    }
    //4.Circle deleted by Admin
    public int[] deletedCircles;

    public class DeletedCircle
    {
        public int cId;

        public DeletedCircle(int id)
        {
            cId = id;
        }
    }
    private List<DeletedCircle> delCircles = new ArrayList<DeletedCircle>();

    public void setDeletedCircle(int id)
    {
        delCircles.add(new DeletedCircle(id));
    }
    public List<DeletedCircle> getDelCircles()
    {
        return delCircles;
    }
    public class CircleImages
    {
        public int cleID;
        public String thumb;
        public String img;
    }
    //5. Circles Image changed
    public List<CircleImages> clImgs = new ArrayList<CircleImages>();

    public void addCircleImage(int cleID, String thumb, String img)
    {
        CircleImages obj = new CircleImages();
        obj.cleID = cleID;
        obj.thumb = thumb;
        obj.img = img;

        clImgs.add(obj);
    }

    public class CircleMemberImageChange
    {
        public int cleID;
        public int userID;
        public String thumb;
        public String img;
    }

    // 6. Circle Member image changed
    public List<CircleMemberImageChange> clMemberImgChange = new ArrayList<CircleMemberImageChange>();

    public void addCircleMemberImageChange(int cleID, int userID, String thumb, String img)
    {
        CircleMemberImageChange obj = new CircleMemberImageChange();
        obj.cleID = cleID;
        obj.userID = userID;
        obj.thumb = thumb;
        obj.img = img;

        clMemberImgChange.add(obj);
    }
}
