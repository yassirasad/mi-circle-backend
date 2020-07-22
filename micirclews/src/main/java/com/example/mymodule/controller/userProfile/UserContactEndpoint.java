package com.example.mymodule.controller.userProfile;

import com.example.mymodule.businessObject.UserContactPackage.UserContact;
import com.example.mymodule.businessObject.UserContactPackage.UserContactList;
import com.example.mymodule.businessObject.UserContactPackage.UserContactRequest;
import com.example.mymodule.businessObject.userProfile.SignUp;
import com.example.mymodule.businessObject.userProfile.SignUpResponse;
import com.example.mymodule.datalayer.userProfile.DLContact;
import com.example.mymodule.datalayer.userProfile.DLUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamaldua on 08/10/2015.
 */
@Api(name = "userContactApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))

public class UserContactEndpoint {

    @ApiMethod(name = "getRegisteredContacts",
            path = "getRegisteredContacts",
            httpMethod = ApiMethod.HttpMethod.POST)
    /*
        Request :
        {
            "deviceSession" : "123",
            "contacts" : ["111","qeweqwe","qweqwewqwe"]
        }

        Response :
        {
          "items" : [ {
            "Contact" : "11111",
            "ImageUrl" : "www.google.com"
          }, {
            "Contact" : "222222",
            "ImageUrl" : "www.google.com"
          }, {
            "Contact" : "33333",
            "ImageUrl" : "www.google.com"
          } ]
        }
     */
    public UserContactList getRegisteredContacts(UserContactRequest req ) { //CollectionResponse<Quote>

        return DLContact.getRegisterdContacts(req.deviceSession, req.contacts);
    }

}
