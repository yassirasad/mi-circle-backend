package com.example.mymodule.datalayer.userProfile;


import com.example.mymodule.businessObject.UserContactPackage.UserContact;
import com.example.mymodule.businessObject.UserContactPackage.UserContactList;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Created by kamaldua on 08/10/2015.
 */
public class DLContact {


    public static UserContactList getRegisterdContacts(String deviceSession, String[] contacts)
    {
        UserContactList res = new UserContactList();
        res.contacts = new ArrayList<UserContact>();
        String contactsWithComma;

        try
        {
            StringBuilder result = new StringBuilder();
            for(String item : contacts) {
                result.append(item.trim());
                result.append(",");
            }

            contactsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";

            Connection conn = ConnectionManager.getConnection();

            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.REGISTERD_CONTACTS);
            cs.setString(1, deviceSession.trim());
            cs.setString(2, contactsWithComma.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                UserContact con = new UserContact();
                con.Contact = rs.getString(1);
                con.ImageUrl = (rs.getString(2).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(2))
                        : rs.getString(2).trim());
                con.Status = rs.getString(3);
                con.FrienduserID = rs.getInt(4);
                res.contacts.add(con);
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
}
