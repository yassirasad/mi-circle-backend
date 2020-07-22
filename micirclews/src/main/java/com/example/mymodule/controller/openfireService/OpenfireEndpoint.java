package com.example.mymodule.controller.openfireService;

import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.GCMSender;
import com.example.mymodule.businessObject.common.NotificationType;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;

import javax.inject.Named;

/**
 * Created by yassir on 30/5/17.
 */

@Api(name = "openfireApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com",  ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class OpenfireEndpoint {

    @ApiMethod(name = "callbackOnOffline", path = "callbackOnOffline", httpMethod = ApiMethod.HttpMethod.GET)
    public void callbackOnOffline(@Named("from")String from, @Named("to")String to)
    {
        try {
            String contactNo = to.split("@")[0];
            if (contactNo.trim().length()>0)
            {
                Connection conn = ConnectionManager.getConnection();
                CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.OPENFIRE_CALLBACK_OFFLINE);
                cs.setString(1,from);
                cs.setString(2,contactNo.trim());
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.executeUpdate();
                String gcmId = cs.getString(3).trim();
                cs.close();
                conn.close();

                if(gcmId.length()>0){
                    ArrayList<String> deviceList = new ArrayList<>();
                    deviceList.add(gcmId);

                    JSONObject data = new JSONObject();
                    data.put("message","reconnect the mi-Circle app");

                    GCMSender.sendData(Constants.API_KEY, NotificationType.OPENFIRE_CALLBACK_ON_OFFLINE,deviceList,data);
                }
             }
        }
        catch (Exception ex){
            ex.getMessage();
        }
    }
}
