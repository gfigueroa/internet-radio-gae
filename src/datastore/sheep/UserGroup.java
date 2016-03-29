/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package datastore.sheep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the UserGroup table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class UserGroup implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String userGroupName;
    
    @Persistent
    private String userGroupDescription;
    
    @Persistent
    private ArrayList<String> userGroupMemberEmails;
    
    @Persistent
    private Date userGroupCreationDate;

    /**
     * UserGroup constructor.
     * @param userGroupName
     * 			: the user group name
     * @param userGroupDescription
     * 			: the user group description
     * @throws MissingRequiredFieldsException
     */
    public UserGroup(String userGroupName, String userGroupDescription) 
    		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (userGroupName == null || userGroupName.isEmpty()) {
    		throw new MissingRequiredFieldsException(
    				this.getClass(), "One or more required fields are missing.");
    	}
    	
    	this.userGroupName = userGroupName;
    	this.userGroupDescription = userGroupDescription;
    	
    	this.userGroupMemberEmails = new ArrayList<String>();

        Date now = new Date();   
        this.userGroupCreationDate = now;
    }

    /**
     * Compare this UserGroup with another UserGroup
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this UserGroup, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof UserGroup ) ) return false;
        UserGroup userGroup = (UserGroup) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(userGroup.getKey()));
    }
    
    /**
     * Get UserGroup key.
     * @return userGroup key
     */
    public Key getKey() {
        return key;
    }

	/**
	 * @return the userGroupName
	 */
	public String getUserGroupName() {
		return userGroupName;
	}

	/**
	 * @param userGroupName the userGroupName to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setUserGroupName(String userGroupName) 
			throws MissingRequiredFieldsException {
		// Check "required field" constraints
    	if (userGroupName == null || userGroupName.isEmpty()) {
    		throw new MissingRequiredFieldsException(
    				this.getClass(), "User group name is missing.");
    	}
		
		this.userGroupName = userGroupName;
	}

	/**
	 * @return the userGroupDescription
	 */
	public String getUserGroupDescription() {
		return userGroupDescription;
	}

	/**
	 * @param userGroupDescription the userGroupDescription to set
	 */
	public void setUserGroupDescription(String userGroupDescription) {
		this.userGroupDescription = userGroupDescription;
	}

	/**
	 * @return the userGroupMembers
	 */
	public ArrayList<String> getUserGroupMemberEmails() {
		return userGroupMemberEmails;
	}
	
	/**
	 * Add member to group.
	 * @param memberEmail
	 * 			: the email of the member to add to this group
	 */
	public void addUserGroupMember(String memberEmail) {
		if (!userGroupMemberEmails.contains(memberEmail)) {
			this.userGroupMemberEmails.add(memberEmail);
		}
	}
	
	/**
	 * Remove member from group.
	 * @param memberEmail
	 * 			: the email of the member to remove from this group
	 * @throws InexistentObjectException 
	 */
	public void removeUserGroupMember(String memberEmail) 
			throws InexistentObjectException {
		
		if (!this.userGroupMemberEmails.remove(memberEmail)) {
			throw new InexistentObjectException(Customer.class, 
    				"User not found!");
		}
		
	}

	/**
	 * @return the userGroupCreationDate
	 */
	public Date getUserGroupCreationDate() {
		return userGroupCreationDate;
	}

}
