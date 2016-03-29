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
import javax.servlet.http.HttpSession;

import util.BlobUtils;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.Administrator;
import datastore.AdministratorManager;
import datastore.Customer;
import datastore.CustomerManager;
import datastore.Station;
import datastore.StationManager;
import datastore.StationTypeManager;
import datastore.SystemManager;
import datastore.User;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This servlet class is used to add, delete and update
 * users (Admin, Station or Customer) in the system.
 * 
 */

@SuppressWarnings("serial")
public class ManageUserServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageUserServlet.class.getName());
    
    private static final BlobstoreService blobstoreService = 
    	BlobstoreServiceFactory.getBlobstoreService();
    
    // JSP file locations
    private static final String addAdminJSP = "/admin/addAdmin.jsp";
    private static final String addCustomerJSP = "/admin/addCustomer.jsp";
    private static final String addStationJSP = "/admin/addStation.jsp";
    private static final String autoCustomerJSP = "/customer/autoCustomer.jsp";
    private static final String editAdminJSP = "/admin/editAdmin.jsp";
    private static final String editAdminPasswordJSP = "/admin/editAdminPassword.jsp";
    private static final String editCustomerJSPAdmin = "/admin/editCustomer.jsp";
    private static final String editCustomerJSPCustomer = 
    		"/customer/editCustomer.jsp";
    private static final String editCustomerPasswordJSPAdmin = 
    		"/admin/editCustomerPassword.jsp";
    private static final String editCustomerPasswordJSPCustomer = 
    		"/customer/editCustomerPassword.jsp";
    private static final String editStationJSPAdmin = "/admin/editStation.jsp";
    private static final String editStationJSPStation = 
    		"/station/editStation.jsp";
    private static final String editStationPasswordJSP = 
    		"/station/editStationPassword.jsp";
    private static final String listAdminJSP = "/admin/listAdmin.jsp";
    private static final String listCustomerJSP = "/admin/listCustomer.jsp";
    private static final String listStationJSP = "/admin/listStation.jsp";  
    private static final String loginJSP = "/login.jsp";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
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

        if (action.equals("delete")) {
        	
            // retrieve the key     
        	String keyString = req.getParameter("k");
      		Key key = KeyFactory.stringToKey(keyString);

            // deleting a user
            switch(req.getParameter("u_type").charAt(0)){
                case 'A':
                    Administrator admin = 
                        AdministratorManager.getAdministrator(key);
                    AdministratorManager.deleteAdministrator(admin);
                    resp.sendRedirect(listAdminJSP + "?msg=success&action=" + action);
                    return;
                case 'S':
                    Station station = StationManager.getStation(key);
                    SystemManager.updateStationListVersion();
                    SystemManager.updateStationListVersion();
                    StationTypeManager.updateStationTypeVersion(station.getStationType());
                    StationManager.deleteStation(station);
                    resp.sendRedirect(listStationJSP + "?msg=success&action=" + action);
                    break;
                case 'C':
                    Customer customer = CustomerManager.getCustomer(key);
                    CustomerManager.deleteCustomer(customer);
                    resp.sendRedirect(listCustomerJSP + "?msg=success&action=" + action);
                    break;
                default:
                    assert(false); // there is no other type
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	// Get the current session
    	HttpSession session = req.getSession(true);
    	User user = (User) session.getAttribute("user");
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("add")) {
        	
            // User fields
            User.Type type;
            char typeChar = req.getParameter("u_type").charAt(0);
            User neoUser;
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");
            
            switch(typeChar){
                case 'A':
                	
                    // Bypass security
                    if (req.getParameter("bypass") != null && req.getParameter("bypass").equals("true")) {
                    	log.info("Security bypassed");
                    }
                    else {
	                	// Check that an administrator is carrying out the action
	                    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	                        return;
	                    }
                    }
                	
                    type = User.Type.ADMINISTRATOR;
                    String administratorName = req.getParameter("a_name");
                    String administratorComments = req.getParameter("a_comments");
                           
                    try {                
                        neoUser = new User(userEmail,
                        		userPassword,
                                type);
                        Administrator admin = new Administrator(neoUser,
                                administratorName,
                                administratorComments);
                        AdministratorManager.putAdministrator(admin);
                        
                        resp.sendRedirect(listAdminJSP + "?msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addAdminJSP  + "?etype=MissingInfo");
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                        resp.sendRedirect(addAdminJSP + "?etype=EmailFormat");
                        return;
                    }
                    catch (ObjectExistsInDatastoreException oede) {
                        resp.sendRedirect(addAdminJSP + "?etype=AlreadyExists");
                        return;
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                case 'S':
                	
                	// Check that an administrator is carrying out the action
                    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                	
                    type = User.Type.STATION;
                    
                    String stationTypeString = req.getParameter("s_type");
            		Long stationType = null;
            		if (!stationTypeString.isEmpty()) {
            			stationType = Long.parseLong(stationTypeString);
            		}
            		
            		String stationPrivilegeLevelString = req.getParameter("s_privilege");
            		Integer stationPrivilegeLevel = 
            				Integer.parseInt(stationPrivilegeLevelString);
            		
            		String stationName = req.getParameter("s_name");	
            		String stationNumber = req.getParameter("s_number");
            		String stationDescription = req.getParameter("s_description");
            		
            		String regionString = req.getParameter("s_region");
            		Long region = null;
            		if (!regionString.isEmpty()) {
            			region = Long.parseLong(regionString);
            		}
            		
                    String stationAddress1 = req.getParameter("s_address1");
            		String stationAddress2 = req.getParameter("s_address2");
            		PostalAddress stationAddress = null;
            		if (stationAddress2.trim().isEmpty()) {
            			stationAddress = new PostalAddress(stationAddress1);
            		}
            		else {
            			stationAddress = new PostalAddress(
            					stationAddress1 + " " + stationAddress2);
            		}
            		
                    Link website = new Link(req.getParameter("s_website"));
                    BlobKey logoKey = BlobUtils.assignBlobKey(req, "s_logo", blobstoreService);
                    String stationComments = req.getParameter("s_comments");
                    
                    try {                
                        neoUser = new User(userEmail,
                                userPassword,
                                type);

                        Station station = new Station(
                        		neoUser,
                        		stationType,
                        		stationPrivilegeLevel,
                                stationName,
                                stationNumber,
                                stationDescription,
                                region,
                                stationAddress,
                                website,
                                logoKey,
                                stationComments);
                        
                        StationManager.putStation(station);
                        SystemManager.updateStationListVersion();
                        StationTypeManager.updateStationTypeVersion(stationType);
                        
                        resp.sendRedirect(listStationJSP + "?msg=success&action=" + action);
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        resp.sendRedirect(addStationJSP + "?etype=MissingInfo");
                        return;
                    } 
                    catch (InvalidFieldFormatException iffe) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        resp.sendRedirect(addStationJSP + "?etype=Format");
                        return;
                    } 
                    catch (ObjectExistsInDatastoreException oede) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        resp.sendRedirect(addStationJSP + "?etype=AlreadyExists");
                        return;
                    } 
                    catch (Exception ex) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                case 'C':

                    // Check who is carrying out the action
                    String jsp;
                    String language = req.getParameter("lang");
                    if (user == null) {
                        jsp = autoCustomerJSP;
                    }
                    else if (user.getUserType() == User.Type.ADMINISTRATOR){
                        jsp = addCustomerJSP;
                    }
                    else {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    
                    type = User.Type.CUSTOMER;
                    
                    String customerName = req.getParameter("c_name");
                    PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
                    
                    Customer.Gender gender =
                            req.getParameter("c_gender").charAt(0) == 'M' 
                            ? Customer.Gender.MALE : Customer.Gender.FEMALE;
                    
                    PostalAddress address = null;
                    String customerAddress1 = req.getParameter("c_address1");
            		String customerAddress2 = req.getParameter("c_address2");
            		if (customerAddress2.trim().isEmpty()) {
            			address = new PostalAddress(customerAddress1);
            		}
            		else {
            			address = new PostalAddress(
                            customerAddress1 + " " + customerAddress2);
            		}

                    String customerComments = req.getParameter("c_comments");
                    
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
                        
                        if (user != null)
                            resp.sendRedirect(listCustomerJSP + "?msg=success&action=" + action);
                        else
                            resp.sendRedirect(loginJSP + "?msg=success&action=" + action);
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(
                                jsp + "?etype=MissingInfo" + 
                                (language != null ? "&lang=" + language : ""));
                        return;
                    } 
                    catch (InvalidFieldFormatException iffe) {
                        resp.sendRedirect(
                                jsp + "?etype=Format" +
                                (language != null ? "&lang=" + language : ""));
                        return;
                    } 
                    catch (ObjectExistsInDatastoreException oede) {
                        resp.sendRedirect(
                                jsp + "?etype=AlreadyExists" +
                                (language != null ? "&lang=" + language : ""));
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                default:
                    assert(false); // there are no other types
            }
        } 
        else if (action.equals("update")) {
        	
            char typeChar = req.getParameter("u_type").charAt(0);
            char updateTypeChar = req.getParameter("update_type").charAt(0);
            Email userEmail = new Email(req.getParameter("u_email"));
            
            // Check that a user is carrying out the action
            if (user == null) {
            	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            switch (typeChar) {
                case 'A':
                	
                	// Check that an administrator is carrying out the action
                    if (user.getUserType() != User.Type.ADMINISTRATOR) {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                	
                	String administratorKeyString = req.getParameter("k");
                	
                	// Check if we want to update the information or password
                	if (updateTypeChar == 'I') {
                	
	                	String administratorName = req.getParameter("a_name");
	                	String administratorComments = req.getParameter("a_comments");
	                	
	                    try {
	                        AdministratorManager.updateAdministratorAttributes(
	                                userEmail,
	                                administratorName,
	                                administratorComments);
	
	                        resp.sendRedirect(editAdminJSP + "?k=" + administratorKeyString + "&readonly=true&msg=success&action=" + action + 
	                        		"&update_type=" + updateTypeChar);
	                    } 
	                    catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(editAdminJSP + "?etype=MissingInfo&k="
	                                + administratorKeyString);
	                    }
	                    catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	else if (updateTypeChar == 'P') {
                		String newPassword = req.getParameter("u_password");
	                	
	                    try {
	                        AdministratorManager.updateAdministratorPassword(
	                                userEmail,
	                                newPassword
	                                );
	
	                        resp.sendRedirect(editAdminJSP + "?k=" + administratorKeyString +
	                        		"&msg=success&action=" + action + 
	                        		"&update_type=" + updateTypeChar);
	                    } 
	                    catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(editAdminPasswordJSP + "?etype=MissingInfo&k="
	                                + administratorKeyString);
	                    }
	                    catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	
                    return;
                case 'S':
                    
                	String stationKeyString = req.getParameter("k");
                	
                    // Check that an administrator or a station are carrying out the action
                    String editStationJSP;
                    String successUpdateStationJSP;
                    String successUpdateStationPasswordJSP;
                    if (user.getUserType() == User.Type.ADMINISTRATOR) {
                    	editStationJSP = editStationJSPAdmin;
                    	successUpdateStationJSP = editStationJSP   + 
                    			"?k=" + stationKeyString+ "&readonly=true&msg=success&action=" + action + "&update_type=" + updateTypeChar;
                    	successUpdateStationPasswordJSP = editStationJSPAdmin + "?k=" +
                    			stationKeyString + "&msg=success&action=" + action + 
                    			"&update_type=" + updateTypeChar;
                    }
                    else if (user.getUserType() == User.Type.STATION) {
                    	editStationJSP = editStationJSPStation;
                    	successUpdateStationJSP = editStationJSPStation  + 
                    			"?msg=success&action=" + action + "&update_type=" + updateTypeChar;
                    	successUpdateStationPasswordJSP = editStationJSPStation + 
                    			"?msg=success&action=" + action + "&update_type=" + updateTypeChar;
                    }
                    else {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    	return;
                    }
                	
                	// Check if we want to update the information or password
                	if (updateTypeChar == 'I') {
                	
	                	String stationTypeString = req.getParameter("s_type");
	            		Long stationType = null;
	            		Long originalStationType;
	            		
	            		Station station = StationManager.getStation(
            					KeyFactory.stringToKey(stationKeyString));
	            		originalStationType = station.getStationType();
	            		
	            		// If user type is ADMINISTRATOR (only admin can change type)
	            		if (stationTypeString != null) {
		            		if (!stationTypeString.isEmpty()) {
		            			stationType = Long.parseLong(stationTypeString);
		            		}
	            		}
	            		// If user type is STATION
	            		else {
	            			stationType = station.getStationType();
	            		}
	            		
	            		String stationPrivilegeLevelString = req.getParameter("s_privilege");
	            		Integer stationPrivilegeLevel = 
	            				stationPrivilegeLevelString.equalsIgnoreCase("basic") ?
	            				0 : 1;
	            		
	            		String stationName = req.getParameter("s_name");
	            		String stationNumber = req.getParameter("s_number");	
	            		String stationDescription = req.getParameter("s_description");
	            		
	            		String regionString = req.getParameter("s_region");
	            		Long region = null;
	            		if (!regionString.isEmpty()) {
	            			region = Long.parseLong(regionString);
	            		}
	            		
	                    String stationAddress1 = req.getParameter("s_address1");
	            		String stationAddress2 = req.getParameter("s_address2");
	            		PostalAddress stationAddress = null;
	            		if (stationAddress2.trim().isEmpty()) {
	            			stationAddress = new PostalAddress(stationAddress1);
	            		}
	            		else {
	            			stationAddress = new PostalAddress(
	            					stationAddress1 + " " + stationAddress2);
	            		}
	            		
	                    Link website = new Link(req.getParameter("s_website"));

	                    BlobKey logoKey = BlobUtils.assignBlobKey(req, "s_logo", blobstoreService);
	                    boolean sameLogo = false;
	                    if (logoKey == null) {
	                    	logoKey = station.getStationLogo();
	                    	sameLogo = true;
	                    	log.info("No logo uploaded in station \"" + req.getParameter("u_email") + 
	                    			"\". Using previous logo.");
	                    }

	                    String stationComments = req.getParameter("s_comments");
	                	
	                	try {
	                        
	                        StationManager.updateStationAttributes(
	                                userEmail,
	                                stationType,
	                                stationPrivilegeLevel,
	                                stationName,
	                                stationNumber,
	                                stationDescription,
	                                region,
	                                stationAddress,
	                                website,
	                                logoKey,
	                                stationComments);
	                        SystemManager.updateStationListVersion();
	                        StationTypeManager.updateStationTypeVersion(stationType);
	                        // Update original Station Type Version only if it changed
	                        if (!stationType.equals(originalStationType)) {
	                        	StationTypeManager.updateStationTypeVersion(originalStationType);
	                        }
	
	                        resp.sendRedirect(successUpdateStationJSP);
	                        
	                    }
	                	catch (MissingRequiredFieldsException mrfe) {
	                    	if (!sameLogo) {
	                    		blobstoreService.delete(logoKey);
	                    	}
	                        resp.sendRedirect(
	                                editStationJSP + "?etype=MissingInfo&k="
	                                + stationKeyString);
	                    } 
	                	catch (Exception ex) {
	                		if (!sameLogo) {
	                    		blobstoreService.delete(logoKey);
	                    	}
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	else if (updateTypeChar == 'P') {
                		String newPassword = req.getParameter("u_password");
	                	
	                	try {
	                        
	                        StationManager.updateStationPassword(
	                                userEmail,
	                                newPassword);
	
	                        resp.sendRedirect(successUpdateStationPasswordJSP);
	                        
	                    }
	                	catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(
	                        		editStationPasswordJSP + "?etype=MissingInfo&r_key="
	                                + stationKeyString);
	                    } 
	                	catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	
                    return;
                case 'C':
                    
                	String customerKeyString = req.getParameter("k");
                	
                	// Check that an administrator or a customer are carrying out the action
                    String editCustomerJSP;
                    String editCustomerPasswordJSP;
                    String successUpdateCustomerJSP;
                    String successUpdateCustomerPasswordJSP;
                    if (user.getUserType() == User.Type.ADMINISTRATOR) {
                    	editCustomerJSP = editCustomerJSPAdmin;
                    	editCustomerPasswordJSP = editCustomerPasswordJSPAdmin;
                    	successUpdateCustomerJSP = editCustomerJSP  + 
                    			"?k=" + customerKeyString + "&readonly=true&msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    	successUpdateCustomerPasswordJSP = editCustomerJSPAdmin  + "?k=" +
                    			customerKeyString + "&msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    			
                    }
                    else if (user.getUserType() == User.Type.CUSTOMER) {
                    	editCustomerJSP = editCustomerJSPCustomer;
                    	editCustomerPasswordJSP = editCustomerPasswordJSPCustomer;
                    	successUpdateCustomerJSP = editCustomerJSPCustomer  + 
                    			"?msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    	successUpdateCustomerPasswordJSP = editCustomerJSPCustomer  + 
                    			"?msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    }
                    else {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    	return;
                    }
                	
                	// Check if we want to update the information or password
                	if (updateTypeChar == 'I') {
	                	String customerName = req.getParameter("c_name");
	                	PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
	                	
	                	PostalAddress address = null;
	                	String customerAddress1 = req.getParameter("c_address1");
	            		String customerAddress2 = req.getParameter("c_address2");
	            		if (customerAddress2.trim().isEmpty()) {
	            			address = new PostalAddress(customerAddress1);
	            		}
	            		else {
	            			address = new PostalAddress(
	                            customerAddress1 + " " + customerAddress2);
	            		}
	                    
	                    String customerComments = req.getParameter("c_comments");
	                    
	                	try {
	                        Customer.Gender gender =
	                            req.getParameter("c_gender").charAt(0) == 'M' 
	                            ? Customer.Gender.MALE : Customer.Gender.FEMALE;
	
	                        CustomerManager.updateCustomerAttributes(
	                                userEmail,
	                                customerName,
	                                customerPhone,
	                                gender,
	                                address,
	                                customerComments);
	
	                        resp.sendRedirect(successUpdateCustomerJSP);
	                    } 
	                	catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(
	                                editCustomerJSP + "?etype=MissingInfo&k="
	                                + customerKeyString);
	                    } 
	                	catch (InvalidFieldFormatException iffe) {
	                        resp.sendRedirect(
	                                editCustomerJSP + "?etype=Format&k="
	                                + customerKeyString);
	                    } 
	                	catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	else if (updateTypeChar == 'P') {
                		String newPassword = req.getParameter("u_password");
	                    
	                	try {
	                        CustomerManager.updateCustomerPassword(
	                                userEmail,
	                                newPassword);
	
	                        resp.sendRedirect(successUpdateCustomerPasswordJSP);
	                    } 
	                	catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(
	                                editCustomerPasswordJSP + "?etype=MissingInfo&k="
	                                + customerKeyString);
	                    }
	                	catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
	                	
                    return;
                
                default:
                    assert(false); // no other types
            }
        }
    }
}
