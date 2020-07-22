package com.example.mymodule.micirclefileupload.dataLayer.common;

import com.google.appengine.api.utils.SystemProperty;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by kamaldua on 08/09/2015.
 */
public class ConnectionManager {
    public static Connection getConnection() throws Exception
    {
        String url = null;
        Connection conn =  null;
        try
        {
            if (SystemProperty.environment.value() ==
                    SystemProperty.Environment.Value.Production) {
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
                url = "jdbc:google:mysql://mi-circle:micircledb/db_micircle?user=root";
                conn = DriverManager.getConnection(url);
            } else {
                // Local MySQL instance to use during development.
                Class.forName("com.mysql.jdbc.Driver");
                //url = "jdbc:mysql://127.0.0.1:3306/db_micircle";
                //conn = DriverManager.getConnection(url,"root","root");
                url = "jdbc:mysql://173.194.84.198:3306/db_micircle";
                conn = DriverManager.getConnection(url,"admin","admin");
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        return conn;
    }
}
