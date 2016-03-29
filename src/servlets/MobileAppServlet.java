/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.User;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UserValidationException;

/**
 * This servlet class is used to serve upload requests
 * from Mobile Apps, such as login information, registration information,
 * profile update and order information
 * 
 */

@SuppressWarnings("serial")
public class MobileAppServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(MobileAppServlet.class.getName());
    
    // JSP file locations
    private static final String thisServlet = "/mobile";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
	    // Lets check the action required by the jsp
	    String status = req.getParameter("status");
	
	    if (status.equals("success")) {
	    	resp.getWriter().println("success");
	    }
	    else if (status.equals("failure")){
	    	resp.getWriter().println("failure");
	    }
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
        // Let's check the action required by the mobile app
        String action = req.getParameter("action");

        if (action.equals("register")) {
        	
            // User fields
            User.Type type = User.Type.CUSTOMER;
            User neoUser;
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");

            // Customer fields
            String customerName = req.getParameter("c_name");
            PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
            
            Customer.Gender gender =
                    req.getParameter("c_gender").equalsIgnoreCase("male") 
                    ? Customer.Gender.MALE : Customer.Gender.FEMALE;
            
            PostalAddress address = new PostalAddress(req.getParameter("c_address"));

            String customerComments = "";
            
            try {          
                neoUser = 
                    new User(userEmail,
                        userPassword,
                        type);
                Customer cust = new Customer(neoUser,
                        customerName,
                        customerPhone,
                        gender,
                        address,
                        customerComments);
                CustomerManager.putCustomer(cust);
                
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.sendRedirect(thisServlet + "?status=success");
            } 
            catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            } 
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            } 
            catch (ObjectExistsInDatastoreException oede) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"This user email has already been registered in the system.");
                return;
            } 
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("modify_profile")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));

            // Customer fields
            String customerName = req.getParameter("c_name");
            PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
            
            Customer.Gender gender =
                    req.getParameter("c_gender").equalsIgnoreCase("male") 
                    ? Customer.Gender.MALE : Customer.Gender.FEMALE;
            
            PostalAddress address = new PostalAddress(req.getParameter("c_address"));

            String customerComments = "";
            
        	try {

                CustomerManager.updateCustomerAttributes(
                        userEmail,
                        customerName,
                        customerPhone,
                        gender,
                        address,
                        customerComments);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            } 
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            } 
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            }  
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("modify_password")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String currentPassword = req.getParameter("curr_pass");
            String newPassword = req.getParameter("new_pass");
            
        	try {
                CustomerManager.updateCustomerPassword(
                		userEmail, 
                		currentPassword, 
                		newPassword);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            }
        	catch (UserValidationException uve) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"User e-mail and password don't match.");
                return;
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("login")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");
            
        	try {
                Customer customer = CustomerManager.getCustomer(userEmail);
                if (customer.getUser().validateUser(userEmail, userPassword)) {
                	resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.sendRedirect(thisServlet + "?status=success");
                }
                else {
                	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    		"User e-mail and password don't match.");
                    return;
                }
                
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
    }
}
