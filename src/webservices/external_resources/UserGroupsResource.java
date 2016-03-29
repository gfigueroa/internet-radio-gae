/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.UserGroupsSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.sheep.UserGroup;
import datastore.sheep.UserGroupManager;

/**
 * This class represents the list of userGroups
 * as a Resource with only one representation
 */

public class UserGroupsResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(UserGroupsResource.class.getName());
	
	/**
	 * Returns the userGroups list as a JSON object
	 * @return An ArrayList of userGroups in JSON format
	 */
    @Get("json")
    public UserGroupsSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	        
	    String customerEmail = searchString;
	    Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
	    		customerEmail);
	    log.info("Query: " + searchBy + "=" + searchString);

        List<UserGroup> ownedUserGroupList = UserGroupManager.getAllOwnedGroups(customerKey);
        List<UserGroup> followedUserGroupList = UserGroupManager.getAllFollowedGroups(customerKey);

        ArrayList<UserGroupsSimple.UserGroupSimple> ownedUserGroupSimpleList =
        		new ArrayList<UserGroupsSimple.UserGroupSimple>();
        ArrayList<UserGroupsSimple.UserGroupSimple> followedUserGroupSimpleList =
        		new ArrayList<UserGroupsSimple.UserGroupSimple>();
        
        // Create owned groups
        for (UserGroup userGroup : ownedUserGroupList) { 	
        	UserGroupsSimple.UserGroupSimple userGroupSimple =
        			new UserGroupsSimple.UserGroupSimple(
        					KeyFactory.keyToString(userGroup.getKey()),
        					customerEmail,
        					userGroup.getUserGroupName(),
        					userGroup.getUserGroupDescription() != null ?
        							userGroup.getUserGroupDescription() : "",
        					userGroup.getUserGroupMemberEmails()
        					);
        	ownedUserGroupSimpleList.add(userGroupSimple);
        }
        
        // Create followed groups
        for (UserGroup userGroup : followedUserGroupList) {  	
        	// Get owner's email
        	Customer owner = CustomerManager.getCustomer(userGroup.getKey().getParent());
        	
        	UserGroupsSimple.UserGroupSimple userGroupSimple =
        			new UserGroupsSimple.UserGroupSimple(
        					KeyFactory.keyToString(userGroup.getKey()),
        					owner.getUser().getUserEmail().getEmail(),
        					userGroup.getUserGroupName(),
        					userGroup.getUserGroupDescription() != null ?
        							userGroup.getUserGroupDescription() : "",
        					userGroup.getUserGroupMemberEmails()
        					);
        	followedUserGroupSimpleList.add(userGroupSimple);
        }
        
        // Create final list
        UserGroupsSimple userGroupsSimple = new UserGroupsSimple(
        		customerEmail,
        		ownedUserGroupSimpleList,
        		followedUserGroupSimpleList
        		);
        
        return userGroupsSimple;
    }

}