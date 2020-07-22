package com.example.mymodule.controller.cronJobs;

import com.example.mymodule.datalayer.cronJobs.DLCronJobs;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by YASSIR on 22-Jun-17.
 */

public class EmailToInactiveCron extends HttpServlet {

    private static final Logger _logger = Logger.getLogger(EmailToInactiveCron.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        _logger.info("EmailToInactive cron job has been executed.");
        DLCronJobs.emailToInactiveUsers();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doGet(req, res);
    }
}
