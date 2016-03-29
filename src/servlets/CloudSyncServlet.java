/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.DatastoreManager;
import datastore.sheep.CloudSyncCommand;
import datastore.sheep.UserGroup;
import datastore.sheep.UserGroupManager;
import datastore.sheep.UserRecommendation;
import datastore.sheep.UserRecommendationManager;
import datastore.sheep.UserRecommendationRecipient;
import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;
import exceptions.UnauthorizedUserOperationException;

/**
 * This servlet class is used to serve upload requests
 * from a Sync-Master user in the Cloud Sync application
 * 
 */

@SuppressWarnings("serial")
public class CloudSyncServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(CloudSyncServlet.class.getName());
    
    // JSP file locations
    private static final String thisServlet = "/cloudSync";
    
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
        
        // User fields
        Email userEmail = new Email(req.getParameter("u_email"));
        String userPassword = req.getParameter("u_password");
        
        // Check if user and password are correct
        Customer customer = CustomerManager.getCustomer(userEmail);
        if (!customer.getUser().validateUser(userEmail, userPassword)) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                	"User e-mail and password don't match.");
            return;
        }
        
        try {
	        // Check the action to be executed
        	if (action.equals("syncMasterPost")) {
	        	syncMasterPost(req, customer); 
	        }
	        else if (action.equals("addUserGroup")) {
	        	addUserGroup(req, customer);
	        }
	        else if (action.equals("deleteUserGroup")) {
	        	deleteUserGroup(req, customer);
	        }
	        else if (action.equals("addGroupMember")) {
	        	manageGroupMember(req, customer, true);
	        }
	        else if (action.equals("deleteGroupMember")) {
	        	manageGroupMember(req, customer, false);
	        }
	        else if (action.equals("userRecommendation")) {
	        	addUserRecommendation(req, customer);
	        }
	        else if (action.equals("addToFavorites")) {
	        	addToFavorites(req, customer);
	        }
	        else {
	        	resp.sendError(HttpServletResponse.SC_NOT_FOUND,
	            		"Action does not exist.");
	            return;
	        }
	        
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.sendRedirect(thisServlet + "?status=success");
        }
        catch (MissingRequiredFieldsException mrfe) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            		"One or more required fields are missing.");
            return;
        }
        catch (UnauthorizedUserOperationException uuoe) {
        	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            		"Unauthorized user operation.");
            return;
        }
        catch (InexistentObjectException ioe) {
        	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            		"Object not found in datastore.");
            return;
        }
        catch (Exception ex) {
            log.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		"Internal server error.");
            return;
        }
    }
    
    /**
     * Executes the sync master post upload function by
     * a mobile device
     * @param req:
     * 			the HTTP request
     * @param customer:
     * 			the customer who is executing the action
     * @throws Exception
     * @throws IOException 
     * @throws MissingRequiredFieldsException 
     */
    public void syncMasterPost(HttpServletRequest req,
    		Customer customer) 
    				throws Exception, IOException, 
    				MissingRequiredFieldsException {
    	
    	String groupKeysString = req.getParameter("g_keys");
        String[] groupKeyTokens = groupKeysString.split(",");
        ArrayList<Key> groupKeys = new ArrayList<Key>();
        if (!groupKeysString.isEmpty()) {
            for (String keyToken : groupKeyTokens) {
            	Key groupKey = KeyFactory.stringToKey(keyToken);
            	groupKeys.add(groupKey);
            }
        }
        
        String messageText = req.getParameter("m_text");
   	
        CloudSyncCommand cloudSyncCommand = new CloudSyncCommand(
        		customer.getKey(), // TODO: Master is owner by default
        		groupKeys,
        		messageText
        		);
        
        CustomerManager.updateCustomerCloudSyncCommand(
        		customer.getUser().getUserEmail(), 
        		cloudSyncCommand);
    }
    
    /**
     * Add a User Group from a mobile device
     * @param req:
     * 			the HTTP request
     * @param customer:
     * 			the customer who is executing the action
     * @throws Exception
     * @throws IOException 
     * @throws MissingRequiredFieldsException 
     */
    public void addUserGroup(HttpServletRequest req,
    		Customer customer) 
    				throws Exception, IOException, 
    				MissingRequiredFieldsException {
    	
    	String userGroupName = req.getParameter("g_name");
    	String userGroupDescription = req.getParameter("g_description");
   	
        UserGroup userGroup = new UserGroup(
        		userGroupName,
        		userGroupDescription
        		);
        
        UserGroupManager.putUserGroup(customer.getKey(), userGroup);
    }
    
    /**
     * Delete a User Group from a mobile device
     * @param req:
     * 			the HTTP request
     * @param customer:
     * 			the customer who is executing the action
     * @throws Exception
     * @throws IOException 
     * @throws UnauthorizedUserOperationException
     */
    public void deleteUserGroup(HttpServletRequest req, 
    		Customer customer) 
    				throws Exception, IOException,
    				UnauthorizedUserOperationException {
    	
    	String userGroupKeyString = req.getParameter("g_key");
    	Key userGroupKey = KeyFactory.stringToKey(userGroupKeyString);
        
    	// Check that the group belongs to the user
        if (!userGroupKey.getParent().equals(customer.getKey())) {
            throw new UnauthorizedUserOperationException(customer.getUser(), 
            		"This group does not belong to the given user.");
        }
    	
    	UserGroupManager.deleteUserGroup(userGroupKey);
    }
    
    /**
     * Manage a User in a User Group from a mobile device.
     * Users can be either added or deleted with this function
     * depending on the "add" parameter.
     * @param req:
     * 			the HTTP request
     * @param customer:
     * 			the customer who is executing the action
     * @param add:
     * 			whether the action to perform is add or not
     * @throws Exception
     * @throws IOException 
     * @throws UnauthorizedUserOperationException
     * @throws InexistentObjectException
     */
    public void manageGroupMember(HttpServletRequest req,
    		Customer customer, boolean add) 
    				throws Exception, IOException, 
    				UnauthorizedUserOperationException,
    				InexistentObjectException {
    	
    	String userGroupKeyString = req.getParameter("g_key");
    	Key userGroupKey = KeyFactory.stringToKey(userGroupKeyString);
        
    	// Check that the group belongs to the user
        if (!userGroupKey.getParent().equals(customer.getKey())) {
            throw new UnauthorizedUserOperationException(customer.getUser(), 
            		"This group does not belong to the given user.");
        }
        
    	String memberEmail = req.getParameter("m_email");
    	
    	// Check if the user to add/delete exists in the datastore
    	Key memberKey = KeyFactory.createKey(
    			Customer.class.getSimpleName(), memberEmail);
		if (!DatastoreManager.entityExists(Customer.class, memberKey)) {
			throw new InexistentObjectException(customer, "User \"" + 
					customer.getUser().getUserEmail().getEmail() + 
					"\" doesn't exist in the datastore.");
		}
		
		if (add) {
			UserGroupManager.addMemeberToUserGroup(userGroupKey, memberEmail);
		}
		else {
			UserGroupManager.removeMemberFromUserGroup(userGroupKey, memberEmail);
		}
    }
    
    /**
     * Add a UserRecommendation from a mobile device
     * @param req:
     * 			the HTTP request
     * @param customer:
     * 			the customer who is executing the action
     * @throws Exception
     * @throws IOException 
     * @throws MissingRequiredFieldsException 
     */
    public void addUserRecommendation(HttpServletRequest req,
    		Customer customer) 
    				throws Exception, IOException, 
    				MissingRequiredFieldsException {
    	
    	UserRecommendation.UserRecommendationSuperType 
    			userRecommendationSuperType = 
    			UserRecommendation.UserRecommendationSuperType.RECOMMENDATION;
    	
    	String userRecommendationTypeString = req.getParameter("r_type");
    	UserRecommendation.UserRecommendationType userRecommendationType =
    			UserRecommendation.getUserRecommendationTypeFromString(
    					userRecommendationTypeString);
    	
    	String userRecommendationComment = req.getParameter("r_comment");
    	
    	String itemKeyString = req.getParameter("i_key");
    	Key itemKey = KeyFactory.stringToKey(itemKeyString);
    	
		ArrayList<UserRecommendationRecipient> userRecommendationRecipients =
				new ArrayList<UserRecommendationRecipient>();
    	
    	String groupKeyStrings = req.getParameter("g_keys");
    	if (groupKeyStrings != null && !groupKeyStrings.isEmpty()) {
	    	String[] groupKeyStringTokens = groupKeyStrings.split(",");
	    	for (String groupKeyString : groupKeyStringTokens) {
	    		Key groupKey = KeyFactory.stringToKey(groupKeyString);
	    		
	    		// Check that the groups belong to the user
	            if (!groupKey.getParent().equals(customer.getKey())) {
	                throw new UnauthorizedUserOperationException(customer.getUser(), 
	                		"One or more groups do not belong to the user.");
	            }
	    		
	            // Get the group
	            UserGroup userGroup = UserGroupManager.getUserGroup(groupKey);
	            
	            for (String userEmail : userGroup.getUserGroupMemberEmails()) {
		    		Key customerKey = 
		    				KeyFactory.createKey(Customer.class.getSimpleName(), userEmail);
		    		
		    		UserRecommendationRecipient userRecommendationRecipient =
		    				new UserRecommendationRecipient(
		    						customerKey,
		    						null
		    						);
		    		userRecommendationRecipients.add(userRecommendationRecipient);
		    	}
	            
	            // TODO: Is this a better way?
	            /*
	    		UserRecommendationRecipient userRecommendationRecipient =
	    				new UserRecommendationRecipient(
	    						null,
	    						groupKey
	    						);
	    		userRecommendationRecipients.add(userRecommendationRecipient);
	    		*/
	    	}
    	}
    	
    	String userEmails = req.getParameter("u_emails");
    	if (userEmails != null && !userEmails.isEmpty()) {
	    	String[] userEmailTokens = userEmails.split(",");
	    	for (String userEmail : userEmailTokens) {
	    		Key customerKey = 
	    				KeyFactory.createKey(Customer.class.getSimpleName(), userEmail);
	    		
	    		UserRecommendationRecipient userRecommendationRecipient =
	    				new UserRecommendationRecipient(
	    						customerKey,
	    						null
	    						);
	    		userRecommendationRecipients.add(userRecommendationRecipient);
	    	}
    	}
    	
		UserRecommendation userRecommendation = 
    			new UserRecommendation(
    					userRecommendationSuperType,
    					userRecommendationType,
    					userRecommendationComment,
    					itemKey,
    					userRecommendationRecipients
    					);
		
		UserRecommendationManager.putUserRecommendation(
				customer.getKey(), userRecommendation);
    }
    
    /**
     * Add to Favorites from a mobile device
     * @param req:
     * 			the HTTP request
     * @param customer:
     * 			the customer who is executing the action
     * @throws Exception
     * @throws IOException 
     * @throws MissingRequiredFieldsException 
     */
    public void addToFavorites(HttpServletRequest req,
    		Customer customer) 
    				throws Exception, IOException, 
    				MissingRequiredFieldsException {
    	
    	UserRecommendation.UserRecommendationSuperType 
    			userRecommendationSuperType = 
    			UserRecommendation.UserRecommendationSuperType.FAVORITE;
    	
    	String userRecommendationTypeString = req.getParameter("r_type");
    	UserRecommendation.UserRecommendationType userRecommendationType =
    			UserRecommendation.getUserRecommendationTypeFromString(
    					userRecommendationTypeString);
    	
    	String userRecommendationComment = req.getParameter("r_comment");
    	
    	String itemKeyString = req.getParameter("i_key");
    	Key itemKey = KeyFactory.stringToKey(itemKeyString);
    	
		ArrayList<UserRecommendationRecipient> userRecommendationRecipients =
				new ArrayList<UserRecommendationRecipient>();
    	
    	userRecommendationRecipients.add(new UserRecommendationRecipient(
    			customer.getKey(),
    			null
    			));
    	
		UserRecommendation userRecommendation = 
    			new UserRecommendation(
    					userRecommendationSuperType,
    					userRecommendationType,
    					userRecommendationComment,
    					itemKey,
    					userRecommendationRecipients
    					);
		
		UserRecommendationManager.putUserRecommendation(
				customer.getKey(), userRecommendation);
    }
    
}