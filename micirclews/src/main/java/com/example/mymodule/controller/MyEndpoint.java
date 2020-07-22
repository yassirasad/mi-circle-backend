package com.example.mymodule.controller;

import com.example.mymodule.businessObject.City;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);
        return response;
    }

    //http://localhost:8080/_ah/api/myApi/v1/getCities
    @ApiMethod(
            name = "getCities.list",
            path = "getCities",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<com.example.mymodule.businessObject.City> list() {

        List<com.example.mymodule.businessObject.City> cities = new ArrayList<City>();
        cities.add(new City("gurgaon","111"));
        cities.add(new City("gurgaon","222"));
        cities.add(new City("gurgaon","333"));
        //return TestConnection.getCities();
        return  cities;
    }
}