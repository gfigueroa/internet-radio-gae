/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.http.*;

import datastore.SystemManager;
import datastore.User;
import exceptions.MissingRequiredFieldsException;

/**
 * This servlet class is used manage system variables.
 * 
 */

@SuppressWarnings("serial")
public class ManageSystemServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageUserServlet.class.getName());
    
    // JSP file locations
    private static final String editSystemJSP = "/admin/editSystem.jsp";

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        
        // Check that an administrator is carrying out the action
	    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("update")) {
        	
            char typeChar = req.getParameter("type").charAt(0);
            
            switch (typeChar) {
            	// System
                case 'S':
                	String oavs1String = req.getParameter("oavs1");
                	Integer oavs1 = null;
                	if (!oavs1String.isEmpty()) {
                		oavs1 = Integer.parseInt(oavs1String);
                	}
                	String oavs2String = req.getParameter("oavs2");
                	Integer oavs2 = null;
                	if (!oavs2String.isEmpty()) {
                		oavs2 = Integer.parseInt(oavs2String);
                	}
                	String oavs3String = req.getParameter("oavs3");
                	Integer oavs3 = null;
                	if (!oavs3String.isEmpty()) {
                		oavs3 = Integer.parseInt(oavs3String);
                	}

                    try {
                        SystemManager.updateSystemAttributes(oavs1, oavs2, oavs3);

                        resp.sendRedirect(editSystemJSP + "?msg=success&action=update");
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editSystemJSP + "?etype=MissingInfo");
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    return;
                default:
                    assert(false); // no other types
            }
        }
    }
}
