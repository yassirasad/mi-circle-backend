package com.example.mymodule.controller.cronJobs;

import com.example.mymodule.datalayer.cronJobs.DLCronJobs;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kamaldua on 06/14/2016.
 */

public class CronEndpoint extends HttpServlet
{

    private static final Logger _logger = Logger.getLogger(CronEndpoint.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try
        {
            _logger.info("Cron Job has been executed");
            DLCronJobs.deleteCircle();
            //Put your logic here
            //BEGIN
            //END
        }
        catch (Exception ex) {
            //Log any exceptions in your Cron Job
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

}
