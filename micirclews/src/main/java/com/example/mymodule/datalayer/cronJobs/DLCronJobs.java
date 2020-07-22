package com.example.mymodule.datalayer.cronJobs;

import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;
import com.example.mymodule.businessObject.common.Sendgrid;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by kamaldua on 06/14/2016.
 */
public class DLCronJobs {
    public static void deleteCircle() {

        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.CIRCLE_DELETE_AFTER_AGE_CROBJOB);

            cs.executeQuery();

            cs.close();
            conn.close();
        } catch (Exception ex) {

        }
    }

    public static void emailToInactiveUsers()
    {
        int userId;
        String name, emailId;
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.INACTIVE_USERS_CRONJOB);
            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                userId = rs.getInt(1);
                name = rs.getString(2).trim();
                emailId = rs.getString(3).trim();
                sendMailToInactiveUsers(userId,name,emailId);
            }
            cs.close();
            conn.close();
        }
        catch (Exception ex){
            ex.getMessage();
        }
    }

    private static void sendMailToInactiveUsers(int userId, String name, String emailId) throws Exception
    {
        try{
            // set credentials
            Sendgrid mail = new Sendgrid(Constants.SMTP_USER_NAME,Constants.SMTP_USER_PASS);

            // Set Group Unsubscribes
            //JSONObject header = new JSONObject();
            //header.put("asm_group_id",2893);
            //mail.setHeaders(header);

            // set email data
            mail.setCategory("mail_to_inactive_users");
            mail.setSubject("mi-Circle community is waiting for you");
            mail.setFrom(Constants.SMTP_MAIL_ADD);
            mail.setFromName(Constants.SMTP_MAIL_NAME);
            mail.addTo(emailId,name);

            /*for (String emailId : emailIds)
                mail.addTo(emailId);

            String[] namesArray = new String[names.size()];
            namesArray = names.toArray(namesArray);
            mail.addSubstitution("-name-",namesArray); */

            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.REGISTERED_CONTACTS_SUGGESTION);
            cs.setInt(1, userId);
            ResultSet rs = cs.executeQuery();
            StringBuilder sub = new StringBuilder("");
            int count = 0;

            while(rs.next())
            {
                String fullName = rs.getString(1);
                String contactNo = rs.getString(2);
                String imageUrl = (rs.getString(3).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(3))
                        : "http://portalconexaoti.com.br/core/assets/conexaoti/img/icons/vagas.png");
                ++count;
                if(count==1){
                    sub.append("<br/><br/>People you may know from your contacts:<br/>" +
                            "<ul style=\"margin-left: 20%; list-style-type: none; \">");
                }
                if(count<=5){
                    sub.append("<li style=\"margin-bottom: 25px;\">" +
                            "<img src=\""+imageUrl+"\" style=\"float:left; margin-right:10px; width:48px; height:48px; \">" +
                            "<div style=\"line-height: 7px;\"><p style=\"padding-top: 8px;\">"+fullName+"</p><p>"+contactNo+"</p></div>" +
                            "</li>");
                }
                else {
                    rs.last();
                    count = rs.getRow();
                    sub.append("<li>and "+(count-5)+" more..</li>");
                    break;
                }
            }

            if(count>0){
                sub.append("</ul>");
            }

            cs.close();
            conn.close();

            String htmlMessage = ("<html><div style=\"background-color: #3D4F63; color: #EEEEEE;padding: 12px; max-width: 800px;\">" +
                    "<br/><img src=\"http://www.mi-circle.com/img/logo.png\" style=\"display: block; margin: auto;\" alt=\"mi-Circle logo\">" +
                    "<br/><br/>Hi "+name+",<br/><br/> We have been improving every day, lot of changes happened since you last logged in. " +
                    "Login to mi-Circle and stay connected with your family, friends and community." + sub +
                    "<br/><br/>Regards,<br/>mi-Circle Team<br/><br/><br/>" +
                    "<a href=\"https://play.google.com/store/apps/details?id=com.sns.micircle\"><img src=\"https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png\" style=\"width: 215px; height: 83px; display: block; margin: auto;\"></a></div></html>");
            mail.setText("").setHtml(htmlMessage);

            // send your message
            mail.send();

        } catch (Exception e) {
            throw e;
        }
    }

}
