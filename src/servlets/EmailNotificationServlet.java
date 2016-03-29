/*
 Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

//Copyright 2011, Google Inc. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package servlets;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datastore.User;


/**
* This servlet responds to the request corresponding to orders. The Class
* places the order.
* 
* @author
*/
@SuppressWarnings("serial")
public class EmailNotificationServlet extends HttpServlet {

private static final String editOrderJSP = "/restaurant/editOrder.jsp";

public void doGet(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException {
	
	HttpSession ssession = req.getSession(true);
    User user = (User) ssession.getAttribute("user");
    if (user == null) {
    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }
	
    String orderNumber= req.getParameter("order");
    
	Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    String rEmail= req.getParameter("email");
    String rName= req.getParameter("name");
    String sEmail= "admin@smasrv.com";
    String sName= "SMASRV Admin";
   
    String msgBody = "Dear Mr/Ms" + rName + ": \n" +
    		"Your order is being processed. You will receive a notification" +
    		"when your order is ready";
    String subject= "Your SMASRV Order #" + orderNumber;
    String redirect_link = editOrderJSP + "?k=" + req.getParameter("k") + "&readonly=true";
    
    

    try {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sEmail, sName));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(rEmail, rName));
        msg.setSubject(subject);
        msg.setText(msgBody);
        Transport.send(msg);
        resp.sendRedirect(redirect_link);
    } catch (AddressException e) {
        // ...
    } catch (MessagingException e) {
        // ...
    }
	 
}

public void doPost(HttpServletRequest req, HttpServletResponse resp)
		   throws ServletException, IOException {
			
	HttpSession ssession = req.getSession(true);
    User user = (User) ssession.getAttribute("user");
    if (user == null) {
    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }
	
    String orderNumber= req.getParameter("order");
    
	Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    String rEmail= req.getParameter("email");
    String rName= req.getParameter("name");
    String sEmail= "admin@smasrv.com";
    String sName= "SMASRV Admin";
   
    String msgBody = "Dear " + rName + ": \n" +
    		"View Order: \n" +
    		editOrderJSP+ "?k=" + req.getParameter("k") + "&readonly=true";
    	
    String subject= "SMASRV Order #" + orderNumber;
    String redirect_link = editOrderJSP + "?k=" + req.getParameter("k") + "&readonly=true";
    
    

    try {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sEmail, sName));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(rEmail, rName));
        msg.setSubject(subject);
        msg.setText(msgBody);
        Transport.send(msg);
        resp.sendRedirect(redirect_link);
    } catch (AddressException e) {
        // ...
    } catch (MessagingException e) {
        // ...
    }
}
	 
}
