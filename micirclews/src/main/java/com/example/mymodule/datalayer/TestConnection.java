package com.example.mymodule.datalayer;

import com.example.mymodule.businessObject.City;
import com.google.appengine.api.utils.SystemProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by kamaldua on 07/22/2015.
 */
public class TestConnection {

    public TestConnection()
    {

    }

    public static ArrayList<City> getCities()
    {
        ArrayList<City> cities = new ArrayList<City>();
        try {

            String url = null;
            Connection conn =  null;
            if (SystemProperty.environment.value() ==
                    SystemProperty.Environment.Value.Production) {
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
                url = "jdbc:google:mysql://mi-circle:micircledb/db_micircle?user=root";
                conn = DriverManager.getConnection(url);
            } else {
                // Local MySQL instance to use during development.
                Class.forName("com.mysql.jdbc.Driver");
                //url = "jdbc:mysql://127.0.0.1:3306/world";
                url = "jdbc:mysql://173.194.84.198:3306/db_micircle";
                conn = DriverManager.getConnection(url,"admin","admin");
            }
            String query = "SELECT  Name FROM city;";
            ResultSet rs = conn.createStatement().executeQuery(query);

            //Name, District, Population
            while (rs.next()) {
                cities.add(new City(rs.getString("Name"),""));
            }
        } catch (Exception ex)
        {
            cities.add(new City(ex.getMessage(),""));
            //System.out.println("error");
        }
        return cities;

    }
}
