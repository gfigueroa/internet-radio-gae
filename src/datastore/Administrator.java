/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class represents the Administrator table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class Administrator {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(dependent = "true", defaultFetchGroup = "true")
    private User user;
    
    @Persistent
    private String administratorName;

    @Persistent
    private String administratorComments;

    /**
     * Administrator constructor.
     * @param user
     * 			: user for this administrator
     * @param administratorName
     * 			: administrator name
     * @param administratorComments
     * 			: administrator comments
     * @throws MissingRequiredFieldsException
     */
    public Administrator(User user, String administratorName, String administratorComments) 
    		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (user == null || administratorName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	if (administratorName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	
    	this.user = user;
    	
    	// Create key with user email
    	this.key = KeyFactory.createKey(Administrator.class.getSimpleName(), user.getUserEmail().getEmail());
    	
    	this.administratorName = administratorName;
        this.administratorComments = administratorComments;
    }

    /**
     * Get Administrator key.
     * @return administrator key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Administrator user.
     * @return administrator user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Get Administrator name.
     * @return administrator name
     */
    public String getAdministratorName() {
        return administratorName;
    }

    /**
     * Get Administrator comments.
     * @return administrator comments
     */
    public String getAdministratorComments() {
    	return administratorComments;
    }
    
    /**
     * Set Administrator name.
     * @param administratorName
     * 			: administrator name
     * @throws MissingRequiredFieldsException
     */
    public void setAdministratorName(String administratorName) 
    		throws MissingRequiredFieldsException {
    	
    	if (administratorName == null || administratorName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Administrator name is missing.");
    	}
    	this.administratorName = administratorName;
    }
    
    /**
     * Set Administrator comments.
     * @param administratorComments
     * 			: administrator comments
     */
    public void setAdministratorComments(String administratorComments) {
    	this.administratorComments = administratorComments;
    }
    
    /**
     * Return Administrator information as a String.
     * @return String representation of this Administrator
     */
    public String toString() {
    	String information = "";
    	
    	this.getUser();
    	
    	information += this.user.toString() + "\n";
    	if (this.key == null) {
    		information += "Administrator key: NULL (object is transient) \n";
    	}
    	else {
    		information += "Administrator key: " + this.key.toString() + "\n";
    	}
    	information += "Administrator name: " + this.administratorName + "\n";
    	information += "Administrator comments: " + this.administratorComments;
    	
    	return information;
    }
    
}