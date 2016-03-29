/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the UserGroup table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class UserGroupsSimple implements Serializable {
    
	public static class UserGroupSimple {
		
		public String key;
		public String groupOwnerEmail;
		public String groupName;
		public String groupDescription;
		public ArrayList<String> groupMembers;
		
		/**
		 * UserGroupSimple constructor.
		 * @param key
		 * @param groupOwnerEmail
		 * @param groupName
		 * @param groupDescription
		 * @param groupMembers
		 * 			: the e-mails of the users that belong to this group
		 */
		public UserGroupSimple(String key, String groupOwnerEmail,
				String groupName, String groupDescription,
				ArrayList<String> groupMembers) {
			
			this.key = key;
			this.groupOwnerEmail = groupOwnerEmail;
			this.groupName = groupName;
			this.groupDescription = groupDescription;
			this.groupMembers = groupMembers;
		}
	}
	
	public String userEmail;
	public ArrayList<UserGroupSimple> ownedGroups;
	public ArrayList<UserGroupSimple> followedGroups;
    
    /**
     * UserGroupsSimple constructor.
     * @param userEmail
     * 			: the email of the user
	 * @param ownedGroups
	 * 			: the groups owned by this user
	 * @param followedGroups
	 * 			: the groups followed by this user
     */
    public UserGroupsSimple(String userEmail, 
    		ArrayList<UserGroupSimple> ownedGroups, 
    		ArrayList<UserGroupSimple> followedGroups) {
    	this.userEmail = userEmail;
    	this.ownedGroups = ownedGroups;
    	this.followedGroups = followedGroups;
    }
    
    /**
     * Compare this userGroup with another UserGroup
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this UserGroup, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof UserGroupsSimple ) ) return false;
        UserGroupsSimple ug = (UserGroupsSimple) o;
        return this.userEmail.equalsIgnoreCase(ug.userEmail);
    }
    
}
