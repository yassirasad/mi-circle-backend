package com.example.mymodule.controller.notfication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.restlet.engine.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by kamaldua on 01/21/2016.
 */
public class CustomHttpRequest {

    public class Response{

        private int status;
        private String uploadUrl;
        private String message;

        public int getStatus() {
            return status;
        }

        public String getUploadUrl() {
            return uploadUrl;
        }
        public String getMessage() {
            return message;
        }
    }

    public static HttpResponse sendGet(String url, String body) throws IOException {

        try  {
            //CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("content-type", "application/json");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String json = response.toString();
            //com.google.gson.Gson gson = new com.google.gson.Gson();
            //Response respuesta = gson.fromJson(json, Response.class);

            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(json);

            String  uploadUrl = jo.get("uploadUrl").getAsString();
            int status = Integer.parseInt(jo.get("status").getAsString());

            //int a =respuesta.getStatus();
            //String b   =respuesta.getUploadUrl();
            //print result
            /*System.out.println(response.toString());

            HttpClient httpClient = HttpClientBuilder.create()
                    .setUserAgent("MyAgent")
                    .setMaxConnPerRoute(4)
                    .build();
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(body);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            org.apache.http.HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            com.google.gson.Gson gson = new com.google.gson.Gson();
            Response respuesta = gson.fromJson(json, Response.class);

            int a =respuesta.getStatus();
            String b   =respuesta.getUploadUrl();
            System.out.println(respuesta.getStatus());
            System.out.println(respuesta.getUploadUrl());
            */
        } catch (IOException ex) {
            throw ex;
        }
        return null;
    }
}
