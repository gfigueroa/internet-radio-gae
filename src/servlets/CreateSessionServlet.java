/*
 Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.http.*;

import util.Dictionary;
import util.Printer;

import datastore.User;
import datastore.UserManager;

/**
 * This servlet class is used to create and destroy sessions.
 * 
 */

@SuppressWarnings("serial")
public class CreateSessionServlet extends HttpServlet {
    
	private static final Logger log = 
        Logger.getLogger(CreateSessionServlet.class.getName());
	
	// JSP file locations
    private static final String loginJSP = "/login.jsp";
    private static final String listAdminJSP = "/admin/listAdmin.jsp";
    private static final String djInterfaceJSP = "/station/djInterface.jsp";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    		throws IOException {
        
    	String action = req.getParameter("action");
    	
    	if (action == null) {
    		resp.sendRedirect(loginJSP);
    	}
    	
    	// are we destroying the session?
        if (action.equals("destroy")) {
            HttpSession session = req.getSession(true);
            assert(session != null);

            Printer printer = (Printer) session.getAttribute("printer");
            Dictionary.Language language;
            if (printer != null) {
            	language = printer.getLanguage();
            }
            else {
            	language = Dictionary.Language.ENGLISH;
            }
            
            session.setAttribute("user", null);
            session.setAttribute("printer", null);
            
            resp.sendRedirect(loginJSP + "?lang=" + 
            		Dictionary.getLanguageString(language) + 
            		"&msg=success&action=" + action);
        }
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
    		throws IOException {
        
    	// lets start by checking the user and password fields
        String username = req.getParameter("username");
        String hashedPass = req.getParameter("password");
        String language = req.getParameter("language") != null ?
        		req.getParameter("language") : "CH";
	
        // read our users' data
        
        User user = UserManager.getUser(username, hashedPass);
        
        Printer printer; 
	    if (language.equals("EN")) {
	    	printer = new Printer(Dictionary.Language.ENGLISH);
	    }
	    else {
	    	printer = new Printer(Dictionary.Language.CHINESE);
	    }
        
        if (user == null) {
        	resp.sendRedirect(loginJSP + "?etype=InvalidInfo" +
        			"&lang=" + language);
        }
        else {
        	// There is no customer login in SIR
    		if (user.getUserType() == User.Type.CUSTOMER) {
    			resp.sendRedirect(loginJSP + "?etype=CustomerLogin");
    		}
    		else {
	            // create session information
	            HttpSession session = req.getSession(true);
	            assert(session != null);
	
	            session.setAttribute("user", user);
	            session.setAttribute("printer", printer);
	            // we check the user type to send him to his/her own main page
	            switch(user.getUserType()) {
	                case ADMINISTRATOR:
	                	log.info("User logged in as Administrator");
	                    resp.sendRedirect(listAdminJSP + 
	                    		"?msg=success&action=login");
	                    break;
	                case STATION:
	                	log.info("User logged in as Station");
	                    resp.sendRedirect(djInterfaceJSP + 
	                    		"?msg=success&action=login");
	                    break;
	                default:
	                    // there should be no other
	                    // type of user
	                    assert(false);
	            }
    		}
        }
    }
    
}