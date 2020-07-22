package com.example.mymodule.datalayer.userProfile;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.userProfile.EmailChangeRequest;
import com.example.mymodule.businessObject.userProfile.EmailVerifyOTPRequest;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by yassir on 4/5/17.
 */

public class DLEmail {

    public static ApiResponse changeEmail(EmailChangeRequest req)
    {
        ApiResponse res = new ApiResponse();
        try{
            if(req.emailId == null || req.emailId.trim().equals("")){
                res.status = 0;
                res.message = "Entered Email is empty";
            }
            else{
                Connection conn = ConnectionManager.getConnection();
                CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.USER_EMAIL_CHANGE);
                cs.setString(1, req.deviceSession);
                cs.setString(2, req.emailId.trim());
                cs.executeUpdate();
                cs.close();
                conn.close();

                res.status = 1;
                res.message = "success";
            }

        }catch (Exception ex){
            res.status = 0;
            res.message = ex.getMessage();
        }

        return res;
    }

    public static ApiResponse verifyEmail(DeviceSessionEntity req)
    {
        ApiResponse res = new ApiResponse();

        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.USER_EMAIL_VERIFY);
            cs.setString(1, req.deviceSession);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.executeUpdate();

            String userEmail = cs.getString(2);
            String otp = cs.getString(3);

            cs.close();
            conn.close();

            //send otp to userEmail
            sendMailForOTP(userEmail.trim(),otp);

            res.status = 1;
            res.message = "success";

        }
        catch (Exception ex){
            res.status = 0;
            res.message = ex.getMessage();

        }
        return res;
    }

    public static ApiResponse verifyEmailOTP(EmailVerifyOTPRequest req)
    {
        ApiResponse res = new ApiResponse();
        try{
            if(req.otp == null || req.otp.trim().equals("")){
                res.status = 0;
                res.message = "Entered OTP is empty";
            }
            else{
                Connection conn = ConnectionManager.getConnection();
                CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.USER_EMAIL_OTP_VERIFY);
                cs.setString(1, req.deviceSession);
                cs.setString(2, req.otp.trim());
                cs.execute();
                cs.close();
                conn.close();

                res.status = 1;
                res.message = "success";
            }

        }catch (Exception ex){
            res.status = 0;
            res.message = ex.getMessage();
        }

        return res;
    }

    private static void sendMailForOTP(String mailID, String otp) throws Exception
    {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(Constants.SMTP_MAIL_ADD, "StopNSolve.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailID));
            msg.setSubject("mi-Circle : OTP for email verification");

            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart();

            // Create the html part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String htmlMessage = ("Your One time password (OTP) for mi-Circle email verification is <b>" + otp + "</b>");
            messageBodyPart.setContent(htmlMessage, "text/html");


            // Add html part to multi part
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            msg.setContent(multipart);

            Transport.send(msg);

        } catch (AddressException e) {
            throw e;
        }
    }

}
