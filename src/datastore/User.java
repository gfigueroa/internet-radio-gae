/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.UnauthorizedUserOperationException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import util.FieldValidator;

/**
 * This class represents the User table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class User implements Serializable {

	private static final long serialVersionUID = -7411976669138107287L;

	// Enumerator for user types
    public static enum Type {
    	ADMINISTRATOR, CUSTOMER, STATION
    };
    private Type type;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Email userEmail;
    
    @Persistent
    private String userPassword;
    
    @Persistent
    private String userType;
    
    @Persistent
    private Date userCreationDate;
    
    @Persistent
    private Date userModificationDate;

    /**
     * User constructor.
     * @param userEmail
     * 			: user email
     * @param userPassword
     * 			: user password
     * @param userType
     * 			: user type
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     */
    public User(Email userEmail, String userPassword, Type userType) 
    		throws MissingRequiredFieldsException, InvalidFieldFormatException {
        
    	// Check "required field" constraints
    	if (userEmail == null || userPassword == null || userType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	if (userEmail.getEmail().trim().isEmpty() || userPassword.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	
    	// Check email format
    	if (!FieldValidator.isValidEmailAddress(userEmail.getEmail())) {
    		throw new InvalidFieldFormatException(this.getClass(), "Invalid email address.");
    	}
    	
    	this.userEmail = new Email(userEmail.getEmail().toLowerCase());
        this.userPassword = userPassword;
        
        this.type = userType;
        switch(userType) {
        	case ADMINISTRATOR:
        		this.userType = "administrator";
        		break;
        	case CUSTOMER:
        		this.userType = "customer";
        		break;
        	case STATION:
        		this.userType = "station";
        }

        Date now = new Date();   
        this.userCreationDate = now;
        this.userModificationDate = now;
    }

    /**
     * Get User key.
     * @return user key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get User email.
     * @return user email
     */
    public Email getUserEmail() {
        return userEmail;
    }

    /**
     * Get User password.
     * @return user password
     */
    public String getUserPassword() {
    	return userPassword;
    }
    
    /**
     * Get User type.
     * @return user type
     */
    public Type getUserType() {
    	return type;
    }
    
    /**
     * Get User type as stored in datastore.
     * @return user type string
     */
    public String getUserTypeString() {
    	return userType;
    }
    
    /**
     * Get User creation date.
     * @return user creation date
     */
    public Date getUserCreationDate() {
    	return userCreationDate;
    }
    
    /**
     * Get User modification date.
     * @return user modification date
     */
    public Date getUserModificationDate() {
    	return userModificationDate;
    }
    
    /**
     * Set User email.
     * Only ADMINISTRATOR and RESTAURANT users can change email address.
     * @param userEmail
     * 			: user email
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     * @throws UnauthorizedUserOperationException
     */
    public void setUserEmail(Email userEmail) throws UnauthorizedUserOperationException,
    		MissingRequiredFieldsException, InvalidFieldFormatException {
    	if (userEmail == null || userEmail.getEmail().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
					"User email is missing.");
    	}
    	
    	// Check email format
    	if (!FieldValidator.isValidEmailAddress(userEmail.getEmail())) {
    		throw new InvalidFieldFormatException(this.getClass(), "Invalid email address.");
    	}
    	
    	if (this.type == Type.ADMINISTRATOR || this.type == Type.STATION) {
    		this.userEmail = userEmail;
    		this.userModificationDate = new Date();
    	}
    	else {
    		throw new UnauthorizedUserOperationException(this, 
    				"Setting a user's email can only be performed by the Station or " +
    				"Administrator types.");
    	}
    }
    
    /**
     * Set User password.
     * @param userPassword
     * 			: user password
     * @throws MissingRequiredFieldsException
     */
    public void setUserPassword(String userPassword) 
    		throws MissingRequiredFieldsException {
    	if (userPassword == null || userPassword.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
					"User password is missing.");
    	}
    	this.userPassword = userPassword;
    	this.userModificationDate = new Date();
    }
    
    /**
     * Validate user with given email address and password.
     * @param userEmail
     * 			: user email
     * @param userPassword
     * 			: user password
     * @return true if the user email and password are correct
     */
    public boolean validateUser(Email userEmail, String userPassword) {
    	if (userEmail.getEmail().equalsIgnoreCase(this.userEmail.getEmail()) && 
    			userPassword.equals(this.userPassword)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Return User information as a String.
     * @return String representation of this User
     */
    public String toString() {
    	String information = "";
    	
    	if (this.key == null) {
    		information += "User key: NULL (object is transient) \n";
    	}
    	else {
    		information += "User key: " + this.key.toString() + "\n";
    	}
    	information += "User e-mail: " + this.userEmail.getEmail() + "\n";
    	information += "User password: " + this.userPassword + "\n";
    	information += "User type: " + this.userType + "\n";
    	information += "User creation date: " + this.userCreationDate;
    	information += "User modification date: " + this.userModificationDate;
    	
    	return information;
    }
    
}
