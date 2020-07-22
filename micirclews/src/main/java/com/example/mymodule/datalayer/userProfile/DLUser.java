package com.example.mymodule.datalayer.userProfile;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.ImageURLRequest;
import com.example.mymodule.businessObject.common.ImageURLResponse;
import com.example.mymodule.businessObject.userProfile.ContactNoRequest;
import com.example.mymodule.businessObject.userProfile.FCMGetRequest;
import com.example.mymodule.businessObject.userProfile.FCMInfo;
import com.example.mymodule.businessObject.userProfile.FCMSetRequest;
import com.example.mymodule.businessObject.userProfile.ImageDeleteRequest;
import com.example.mymodule.businessObject.userProfile.LoginUser;
import com.example.mymodule.businessObject.userProfile.LoginUserResponse;
import com.example.mymodule.businessObject.userProfile.MemberProfileCircle;
import com.example.mymodule.businessObject.userProfile.MemberProfileRequest;
import com.example.mymodule.businessObject.userProfile.MemberProfileResponse;
import com.example.mymodule.businessObject.userProfile.PassResetOTPVerificationRequest;
import com.example.mymodule.businessObject.userProfile.PasswordChangeRequest;
import com.example.mymodule.businessObject.userProfile.PasswordForgetRequest;
import com.example.mymodule.businessObject.userProfile.PasswordForgetRequestResponse;
import com.example.mymodule.businessObject.userProfile.PasswordResetRequest;
import com.example.mymodule.businessObject.userProfile.ResetPasswordRequest;
import com.example.mymodule.businessObject.userProfile.SignUp;
import com.example.mymodule.businessObject.userProfile.SignUpResponse;
import com.example.mymodule.businessObject.userProfile.StatusTextResponse;
import com.example.mymodule.businessObject.userProfile.StatusTextSetRequest;
import com.example.mymodule.businessObject.userProfile.UpdateUserProfileRequest;
import com.example.mymodule.businessObject.userProfile.UserProfileResponse;
import com.example.mymodule.businessObject.userProfile.ValidateReferral;
import com.example.mymodule.businessObject.userProfile.ValidateReferralResponse;
import com.example.mymodule.businessObject.userProfile.VerifyContactNoRequest;
import com.example.mymodule.businessObject.userProfile.VerifyUser;
import com.example.mymodule.businessObject.userProfile.VerifyUserResponse;
import com.example.mymodule.controller.openfireService.restAPI;
import com.example.mymodule.datalayer.OTP;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/*
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

*/

/**
 * Created by kamaldua on 07/24/2015.
 */
public class DLUser {

    public static ApiResponse checkContactNo(ContactNoRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            String countryCode = req.countryCode.trim();
            String contactNo = req.contactNo.trim();
            String otp = OTP.generateOTP();

            Connection conn = ConnectionManager.getConnection();
            String checkContact = StoreProcedures.UserProfile.USER_CONTACT_NO_REGISTER;
            CallableStatement cs = conn.prepareCall(checkContact);
            cs.setString(1, countryCode);
            cs.setString(2, contactNo);
            cs.setString(3, otp);

            cs.executeUpdate();

            cs.close();
            conn.close();

            // Sending OTP to mobile
            OTP.sendOTP(countryCode+contactNo, otp, OTP.REGISTRATION);

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static ApiResponse verifyContactNo(VerifyContactNoRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            String countryCode = req.countryCode.trim();
            String contactNo = req.contactNo.trim();
            String otp = req.otp.trim();

            Connection conn = ConnectionManager.getConnection();
            String verifyContact = StoreProcedures.UserProfile.USER_CONTACT_NO_VERIFY;
            CallableStatement cs = conn.prepareCall(verifyContact);
            cs.setString(1, countryCode);
            cs.setString(2, contactNo);
            cs.setString(3, otp);

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static SignUpResponse signUp(SignUp details)
    {
        SignUpResponse res = new SignUpResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_SIGNUP;
            CallableStatement cs = conn.prepareCall(signUp);

            cs.setString(1,details.countryCode.trim());
            cs.setString(2,details.phoneNumber.trim());
            cs.setString(3,details.name.trim());
            cs.setString(4,details.password.trim());
            cs.setString(5,details.email.trim());
            cs.setDate(6, new java.sql.Date(details.dob.getTime()) );
            cs.setString(7,details.zipCode.trim());
            cs.setInt(8, details.gender.trim().equals("M") ? 1 : 2);
            cs.setString(9, details.deviceId.trim());
            cs.setString(10, details.deviceDesc.trim());
            cs.setString(11, details.referralCode);

            cs.registerOutParameter(12, java.sql.Types.VARCHAR);

            cs.executeUpdate();

            res.deviceSession = cs.getString(12);

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "registration successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static ValidateReferralResponse validateReferralCode(ValidateReferral referral){
        ValidateReferralResponse res = new ValidateReferralResponse();

        try{
            if(referral.referralCode==null || referral.referralCode.trim()==""){
                res.isReferralCodeValid = false;
            }
            else {
                Connection conn = ConnectionManager.getConnection();
                CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.REFERRAL_CODE_VALIDATION);

                cs.setString(1, referral.referralCode.trim());
                cs.registerOutParameter(2, Types.BOOLEAN);
                cs.execute();
                res.isReferralCodeValid = cs.getBoolean(2);

                cs.close();
                conn.close();
            }
            res.status = 1;
            res.message = "success";
        }
        catch (Exception ex){
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static VerifyUserResponse verifyUser(VerifyUser userDetails)
    {
        VerifyUserResponse res = new VerifyUserResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_VERIFICATION;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1,userDetails.deviceSession.trim());
            cs.setInt(2, userDetails.deviceOTP);
            cs.registerOutParameter(3, java.sql.Types.BIT);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            cs.registerOutParameter(7, java.sql.Types.VARCHAR);
            cs.registerOutParameter(8, java.sql.Types.VARCHAR);

            cs.executeUpdate();

            res.isUserVerified = cs.getBoolean(3);
            res.deviceType = cs.getString(4);

            String contactNo = cs.getString(5);
            String name = cs.getString(6);

            String xmppCircle = cs.getString(7);
            String xmppPass = cs.getString(8);

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "";

            if(res.status == 1 && res.isUserVerified)
            {
                try
                {
                    restAPI.createUser(contactNo, name, "", userDetails.deviceSession.trim());
                    ArrayList<String> owners = new ArrayList<String>();
                    owners.add(contactNo);
                    ArrayList<String> members = new ArrayList<String>();
                    restAPI.createCircle(xmppCircle, xmppPass, owners, members);
                }
                catch (Exception e)
                {}
            }
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static LoginUserResponse loginUser(LoginUser loginDetails)
    {
        LoginUserResponse res = new LoginUserResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_LOGIN;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1,loginDetails.deviceId.trim());
            cs.setString(2, loginDetails.phoneNumber);
            cs.setString(3, loginDetails.password);
            cs.setString(4, loginDetails.deviceDesc);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            cs.registerOutParameter(7, java.sql.Types.INTEGER);
            cs.registerOutParameter(8, Types.BIT);
            cs.executeUpdate();

            res.deviceSession = cs.getString(5);
            res.deviceType = cs.getString(6);
            res.panicID = cs.getInt(7);
            boolean isDeviceLogin = cs.getBoolean(8);

            cs.close();
            conn.close();

            if(isDeviceLogin)
            {
                restAPI.updateUserPassword(loginDetails.phoneNumber, res.deviceSession);
            }
            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static ApiResponse forgotPassword(ContactNoRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            String countryCode = req.countryCode.trim();
            String contactNo = req.contactNo.trim();
            String otp = OTP.generateOTP();

            Connection conn = ConnectionManager.getConnection();
            String forgotSP = StoreProcedures.UserProfile.USER_PASSWORD_FORGOT;
            CallableStatement cs = conn.prepareCall(forgotSP);
            cs.setString(1, countryCode);
            cs.setString(2, contactNo);
            cs.setString(3, otp);

            cs.executeUpdate();

            cs.close();
            conn.close();

            // Sending OTP to mobile
            OTP.sendOTP(countryCode+contactNo, otp, OTP.PASSWORD);

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static ApiResponse resetPassword(ResetPasswordRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            String countryCode = req.countryCode.trim();
            String contactNo = req.contactNo.trim();
            String password = req.password.trim();
            String otp = req.otp.trim();

            Connection conn = ConnectionManager.getConnection();
            String resetSP = StoreProcedures.UserProfile.USER_PASSWORD_RESET;
            CallableStatement cs = conn.prepareCall(resetSP);
            cs.setString(1, countryCode);
            cs.setString(2, contactNo);
            cs.setString(3, password);
            cs.setString(4, otp);

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


    public static ApiResponse setFCMId(FCMSetRequest req)
    {
        FCMInfo res = new FCMInfo();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_GCM_SET;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, req.deviceSession.trim());
            cs.setString(2, req.fcmId.trim());

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static FCMInfo getFCMId(FCMGetRequest req)
    {
        FCMInfo res = new FCMInfo();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_GCM_GET;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, req.deviceSession.trim());
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
            cs.executeUpdate();

            res.fcmId = cs.getString(2);
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static UserProfileResponse getProfileInfo(DeviceSessionEntity req)
    {
        UserProfileResponse res = new UserProfileResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String profileInfo = StoreProcedures.UserProfile.USER_GET_PROFILE_INFO;
            CallableStatement cs = conn.prepareCall(profileInfo);
            cs.setString(1,req.deviceSession.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                res.userId = rs.getInt(1);
                res.firstName = rs.getString(2);
                res.lastName = rs.getString(3);
                res.likes = rs.getString(4);
                res.dislikes = rs.getString(5);
                res.workplace = rs.getString(6);
                res.isInActive = rs.getBoolean(7);
                res.imageThumbnail = rs.getString(8).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(8))
                        : rs.getString(8).trim();;
                res.image = rs.getString(8).trim().length() > 0
                        ? (Constants.IMAGE_URL + rs.getString(8))
                        : rs.getString(8).trim();
                res.countryCode = rs.getString(9);
                res.contactNo = rs.getString(10);
                res.emailID = rs.getString(11);
                res.dob = rs.getDate(12);
                res.zip = rs.getString(13);
                res.statusText = rs.getString(14);
                res.gender = rs.getInt(15) == 1 ? "Male" : "Female";
                res.passwordChangedOn = rs.getString(16);
                res.isEmailVerified = rs.getBoolean(17);
                res.referralCode = rs.getString(18);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse updateProfileInfo(UpdateUserProfileRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String profileInfo = StoreProcedures.UserProfile.USER_SET_PROFILE_INFO;
            CallableStatement cs = conn.prepareCall(profileInfo);

            cs.setString(1,req.deviceSession.trim());
            cs.setString(2,req.firstName.trim());
            cs.setString(3,req.lastName.trim());
            cs.setInt(4, req.gender.trim().equals("M") ? 1 : 2);
            cs.setString(5,req.likes.trim());
            cs.setString(6,req.dislikes.trim());
            cs.setString(7,req.workplace.trim());
            cs.setBoolean(8,req.isInActive);
            cs.setDate(9, new java.sql.Date(req.dob.getTime()));
            cs.setString(10, req.zip);
            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static PasswordForgetRequestResponse passwordForget(PasswordForgetRequest req)
    {
        PasswordForgetRequestResponse res = new PasswordForgetRequestResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_PASSWORD_FORGET;
            CallableStatement cs = conn.prepareCall(sp);
            cs.setString(1, req.contactNo.trim());
            cs.setString(2, req.emailID.trim());

            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);

            cs.executeUpdate();

            res.tempSession = cs.getString(4);
            String otp = cs.getString(3);

            cs.close();
            conn.close();

            //send password to mail
            sendMailForOTP(req.emailID.trim(),otp);

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse passwordResetOTPVerification(PassResetOTPVerificationRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_PASSWORD_RESET_OTP_VER;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1, req.tempSession.trim());
            cs.setString(2,req.pin.trim());

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse passwordReset(PasswordResetRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_PASSWORD_RESET_NEW_PASS;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.tempSession.trim());
            cs.setString(2,req.newPassword.trim());

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    /*private static void sendMailForOTP(String mailID, String otp) throws Exception
    {
        // Create the email addresses involved
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", true); // added this line
            props.put("mail.smtp.host", Constants.SMTP_HOST);
            props.put("mail.smtp.user", Constants.SMTP_USER_NAME);
            props.put("mail.smtp.password", Constants.SMTP_USER_PASS);
            props.put("mail.smtp.port", Constants.SMTP_PORT);
            props.put("mail.smtp.auth", true);

            Session session = Session.getInstance(props,null);
            MimeMessage message = new MimeMessage(session);

            InternetAddress from = new InternetAddress(Constants.SMTP_MAIL_ADD);

            message.setSubject("MiCircle password reset");
            message.setFrom(from);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailID));

            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");

            // Create your text message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("some text to send");

            // Add the text part to the multipart
            multipart.addBodyPart(messageBodyPart);

            // Create the html part
            messageBodyPart = new MimeBodyPart();
            String htmlMessage = ("OTP : " + otp);
            messageBodyPart.setContent(htmlMessage, "text/html");


            // Add html part to multi part
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            message.setContent(multipart);

            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(Constants.SMTP_HOST, Constants.SMTP_USER_NAME, Constants.SMTP_USER_PASS);
            System.out.println("Transport: "+transport.toString());
            transport.sendMessage(message, message.getAllRecipients());


        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
*/
    private static void sendMailForOTP(String mailID, String otp) throws Exception
    {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "...";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(Constants.SMTP_MAIL_ADD, "StopNSolve.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mailID));
            msg.setSubject("Your mi-Circle account OTP for reset Password");
            //msg.setText(msgBody);
            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");

            // Create your text message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("some text to send");

            // Add the text part to the multipart
            multipart.addBodyPart(messageBodyPart);

            // Create the html part
            messageBodyPart = new MimeBodyPart();
            String htmlMessage = ("OTP : " + otp);
            messageBodyPart.setContent(htmlMessage, "text/html");


            // Add html part to multi part
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            msg.setContent(multipart);
            Transport.send(msg);

        } catch (AddressException e) {
            throw e;
        } catch (MessagingException e) {
            throw e;
        }

    }

    public static MemberProfileResponse getMemberProfile(MemberProfileRequest req)
    {
        MemberProfileResponse res = new MemberProfileResponse();
        res.circles = new ArrayList<MemberProfileCircle>();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.MEMBER_GET_PROFILE;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.deviceSession);
            cs.setInt(2, req.memberId);

            boolean results = cs.execute();
            int rsCount = 0;

            while (results) {
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {
                    if(rsCount == 0)
                    {
                        res.firstName = rs.getString(1);
                        res.lastName = rs.getString(2);
                        res.countryCode = rs.getString(3);
                        res.imageUrl = rs.getString(4).trim().length() > 0
                                ? (Constants.IMAGE_URL + rs.getString(4))
                                : rs.getString(4).trim();
                        res.gender = rs.getInt(5) == 1 ? "Male" : "Female";
                        res.dob = rs.getDate(6);
                        res.zip = rs.getString(7);
                        res.statusText = rs.getString(8);
                    }
                    else if(rsCount == 1)
                    {
                        MemberProfileCircle circle = new MemberProfileCircle();
                        circle.cId = rs.getInt(1);
                        circle.cName = rs.getString(2);

                        res.circles.add(circle);
                    }
                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse setNewPassword(PasswordChangeRequest req)
    {
        ApiResponse res = new ApiResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_PASSWORD_NEW;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.deviceSession.trim());
            cs.setString(2,req.oldPassword.trim());
            cs.setString(3,req.newPassword.trim());

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ImageURLResponse getImage(ImageURLRequest req)
    {
        ImageURLResponse res = new ImageURLResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_IMAGE_GET;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2, req.idType);
            cs.setInt(3, req.id);

            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.executeUpdate();

            res.imageThumbnail = cs.getString(4).trim().length() > 0
                    ? (Constants.IMAGE_SHORT_URL + cs.getString(4))
                    : cs.getString(4).trim();;
            res.image = cs.getString(4).trim().length() > 0
                    ? (Constants.IMAGE_URL + cs.getString(4))
                    : cs.getString(4).trim();


            cs.close();
            conn.close();

            res.status = 1;
            res.message = "";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse deleteUserImage(ImageDeleteRequest req)
    {
        ApiResponse res = new ApiResponse();
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_IMAGE_DELETE;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2,req.requestId);
            cs.setInt(3,req.paramId);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);

            cs.executeUpdate();

            String blobKey = cs.getString(4);

            if(blobKey.trim().length() > 0)
            {
                //blobstoreService.delete(new BlobKey(blobKey));
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static StatusTextResponse getUserTextStatus(DeviceSessionEntity req)
    {
        StatusTextResponse res = new StatusTextResponse();
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_STATUS_TEXT_GET;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.deviceSession.trim());
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);

            cs.executeUpdate();

            res.statusText =  cs.getString(2);

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse setUserTextStatus(StatusTextSetRequest req)
    {
        ApiResponse res = new ApiResponse();
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.USER_STATUS_TEXT_SET;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.deviceSession.trim());
            cs.setString(2,req.statusText);

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }
}
