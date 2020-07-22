package com.example.mymodule.controller.feed;

import com.example.mymodule.datalayer.feed.DLFeed;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Yassir on 24-Aug-18.
 */

public class LoginFeed extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session == null){
            RequestDispatcher rd = req.getRequestDispatcher("/feed_login.jsp");
            rd.forward(req, resp);
        }
        else
            resp.sendRedirect("/feed");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //if(username.equals("mi-Circle"))
        if(DLFeed.loginFeed(username, password)){
            req.getSession(true);   //Create new session.
            resp.sendRedirect("/feed");
        }
        else{
            req.setAttribute("error", "Invalid Username or Password");
            RequestDispatcher rd = req.getRequestDispatcher("/feed_login.jsp");
            rd.include(req, resp);
        }
    }

}
