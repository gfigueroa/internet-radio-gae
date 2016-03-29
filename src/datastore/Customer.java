/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.sheep.CloudSyncCommand;
import datastore.sheep.UserGroup;
import datastore.sheep.UserRecommendation;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import util.FieldValidator;

/**
 * This class represents the Customer table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class Customer {

	// Enumerator for gender
	public static enum Gender {
		MALE, FEMALE
	}
	private Gender gender;
	
	// Enumerator for status
	public static enum Status {
		UNCONFIRMED, ACTIVE, INACTIVE, DISABLED
	}
	private Status status;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(dependent = "true", defaultFetchGroup = "true")
    private User user;
    
    @Persistent
    private String customerName;
    
    @Persistent
    private PhoneNumber customerPhone;
    
    @Persistent
    private String customerGender;
    
    @Persistent
    private PostalAddress customerAddress;
    
    @Persistent
    private String customerStatus;
    
    @Persistent
    private String customerComments;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<UserGroup> ownedUserGroups;
    
    @Persistent
    private ArrayList<Key> followedUserGroups;
    
    @Persistent(dependent = "true", defaultFetchGroup = "true")
    private CloudSyncCommand cloudSyncCommand;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<UserRecommendation> userRecommendations;

	/**
     * Customer constructor.
     * @param user
     * 			: user for this customer
     * @param customerName
     * 			: customer name
     * @param customerPhone
     * 			: customer phone
     * @param customerGender
     * 			: customer gender
     * @param customerAddress
     * 			: customer address
     * @param customerComments
     * 			: customer comments
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     */
    public Customer(User user, String customerName, 
                    PhoneNumber customerPhone, Gender customerGender,
    		        PostalAddress customerAddress, String customerComments) 
    		throws MissingRequiredFieldsException, InvalidFieldFormatException {
        
    	// Check "required field" constraints
    	if (user == null || customerName == null || customerPhone == null || customerGender == null ||
    			customerAddress == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	if (customerName.trim().isEmpty() || customerPhone.getNumber().trim().isEmpty() || 
    			customerAddress.getAddress().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	
    	// Check phone number format
    	if (!FieldValidator.isValidPhoneNumber(customerPhone.getNumber())) {
    		throw new InvalidFieldFormatException(this.getClass(), "Invalid phone number.");
    	}
    	
    	this.user = user;
    	
    	// Create key with user email
    	this.key = KeyFactory.createKey(Customer.class.getSimpleName(), user.getUserEmail().getEmail());
    	
    	this.customerName = customerName;
        this.customerPhone = customerPhone;
        
        this.gender = customerGender;
        switch(customerGender) {
        	case MALE:
        		this.customerGender = "male";
        		break;
        	case FEMALE:
        		this.customerGender = "female";
        		break;
        }

        this.customerAddress = customerAddress;
        this.status = Status.UNCONFIRMED;
        this.customerStatus = "unconfirmed";
        this.customerComments = customerComments;
        
        this.ownedUserGroups = new ArrayList<UserGroup>();
        this.followedUserGroups = new ArrayList<Key>();
        this.userRecommendations = new ArrayList<UserRecommendation>();
    }

    /**
     * Get Customer key.
     * @return customer key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Customer user.
     * @return customer user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Get Customer name.
     * @return customer name
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * Get Customer phone.
     * @return customer phone
     */
    public PhoneNumber getCustomerPhone() {
        return customerPhone;
    }
    
    /**
     * Get Customer gender.
     * @return customer gender
     */
    public Gender getCustomerGender() {
        return gender;
    }
    
    /**
     * Get Customer gender as stored in datastore.
     * @return customer gender as string
     */
    public String getCustomerGenderString() {
        return customerGender;
    }

    /**
     * Get Customer address.
     * @return customer address
     */
    public PostalAddress getCustomerAddress() {
        return customerAddress;
    }
    
    /**
     * Get Customer status.
     * @return customer status
     */
    public Status getCustomerStatus() {
        return status;
    }
    
    /**
     * Get Customer status as stored in datastore.
     * @return customer status as string
     */
    public String getCustomerStatusString() {
        return customerStatus;
    }
    
    /**
     * Get Customer comments.
     * @return customer comments
     */
    public String getCustomerComments() {
    	return customerComments;
    }
    
    /**
     * Get Customer owned user groups.
     * @return user groups this customer owns
     */
    public ArrayList<UserGroup> getOwnedUserGroups() {
    	return ownedUserGroups;
    }
    
    /**
     * Get Customer folowed user groups.
     * @return user groups this customer follows
     */
    public ArrayList<Key> getFollowedUserGroups() {
    	return followedUserGroups;
    }
    
    /**
     * Get Customer cloud sync command
	 * @return the cloudSyncCommand
	 */
	public CloudSyncCommand getCloudSyncCommand() {
		return cloudSyncCommand;
	}
	
	/**
	 * Get Customer owned user recommendations.
	 * @return userRecommendations
	 */
	public ArrayList<UserRecommendation> getUserRecommendations() {
		return userRecommendations;
	}
    
    /**
     * Set Customer name.
     * @param customerName
     * 			: customer name
     * @throws MissingRequiredFieldsException 
     */
    public void setCustomerName(String customerName) 
    		throws MissingRequiredFieldsException {
    	
    	if (customerName == null || customerName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Customer name is missing.");
    	}
    	this.customerName = customerName;
    }
    
    /**
     * Set Customer phone.
     * @param customerPhone
     * 			: customer phone
     * @throws InvalidFieldFormatException 
     */
    public void setCustomerPhone(PhoneNumber customerPhone) 
    		throws MissingRequiredFieldsException, InvalidFieldFormatException {
    	if (customerPhone == null || customerPhone.getNumber().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
					"Customer phone is missing.");
    	}
    	
    	// Check phone number format
    	if (!FieldValidator.isValidPhoneNumber(customerPhone.getNumber())) {
    		throw new InvalidFieldFormatException(this.getClass(), "Invalid phone number.");
    	}
    	
    	this.customerPhone = customerPhone;
    }
    
    /**
     * Set Customer gender.
     * @param customerGender
     * 			: customer gender
     * @throws MissingRequiredFieldsException 
     */
    public void setCustomerGender(Gender customerGender) 
    		throws MissingRequiredFieldsException {
    	if (customerGender == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
					"Customer gender is missing.");
    	}
    	this.gender = customerGender;
    	switch(customerGender) {
    		case MALE:
    			this.customerGender = "male";
    			break;
    		case FEMALE:
    			this.customerGender = "female";
    			break;
    	}
    }
    
    /**
     * Set Customer address line 1.
     * @param customerAddressLine1
     * 			: customer address line 1
     * @throws MissingRequiredFieldsException 
     */
    public void setCustomerAddress(PostalAddress customerAddress)
    		throws MissingRequiredFieldsException {
    	if (customerAddress == null || customerAddress.getAddress().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
					"Customer address is missing.");
    	}
    	this.customerAddress = customerAddress;
    }
    
    /**
     * Set Customer status.
     * @param customerStatus
     * 			: customer status
     * @throws MissingRequiredFieldsException 
     */
    public void setCustomerStatus(Status customerStatus)
    		throws MissingRequiredFieldsException {
    	if (customerStatus == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
					"Customer status is missing.");
    	}   	
    	this.status = customerStatus;
    	switch(customerStatus) {
    		case UNCONFIRMED:
    			this.customerStatus = "unconfirmed";
    			break;
    		case ACTIVE:
    			this.customerStatus = "active";
    			break;
    		case INACTIVE:
    			this.customerStatus = "inactive";
    			break;
    		case DISABLED:
    			this.customerStatus = "disabled";
    			break;
    	}
    }
    
    /**
     * Set Customer comments.
     * @param customerComments
     * 			: customer comments
     */
    public void setCustomerComments(String customerComments) {
    	this.customerComments = customerComments;
    }
    
    /**
     * Add owned user group
     * @param userGroup
     * 			: the user group to add
     */
    public void addOwnedUserGroup(UserGroup userGroup) {
    	if (ownedUserGroups == null) {
    		ownedUserGroups = new ArrayList<UserGroup>();
    	}
    	
    	this.ownedUserGroups.add(userGroup);
    }
    
    /**
     * Remove owned user group
     * @param userGroup
     * 			: the user group to remove
     * @throws InexistentObjectException 
     */
    public void removeOwnedUserGroup(UserGroup userGroup) 
    		throws InexistentObjectException {
    	
    	if (!ownedUserGroups.remove(userGroup)) {
    		throw new InexistentObjectException(UserGroup.class, 
    				"User Group not found!");
    	}
    }
    
    /**
     * Add followed user group
     * @param userGroupKey
     * 			: the key of the user group to add
     */
    public void addFollowedUserGroup(Key userGroupKey) {
    	if (followedUserGroups == null) {
    		followedUserGroups = new ArrayList<Key>();
    	}
    	
    	if (!this.followedUserGroups.contains(userGroupKey)) {
    		this.followedUserGroups.add(userGroupKey);
    	}
    }
    
    /**
     * Remove followed user group
     * @param userGroupKey
     * 			: the key of the user group to remove
     * @throws InexistentObjectException 
     */
    public void removeFollowedUserGroup(Key userGroupKey) 
    		throws InexistentObjectException {
    	
    	if (!followedUserGroups.remove(userGroupKey)) {
    		throw new InexistentObjectException(UserGroup.class, 
    				"User Group Key not found!");
    	}
    }
    
	/**
	 * Set customer cloud sync command
	 * @param cloudSyncCommand 
	 * 			: the cloudSyncCommand to set
	 */
	public void setCloudSyncCommand(
			CloudSyncCommand cloudSyncCommand) {
		this.cloudSyncCommand = cloudSyncCommand;
	}
	
	/**
	 * Add user recommendation
	 * @param userRecommendation
	 * 			: the user recommendation to add
	 */
	public void addUserRecommendation(UserRecommendation userRecommendation) {
		if (userRecommendations == null) {
			userRecommendations = new ArrayList<UserRecommendation>();
		}
		
		this.userRecommendations.add(userRecommendation);
	}
	
	/**
	 * Remove user recommendation
	 * @param userRecommendation
	 * 			: the user recommendation to remove
	 * @throws InexistentObjectException 
	 */
	public void removeUserRecommendation(UserRecommendation userRecommendation) 
			throws InexistentObjectException {
		if (!userRecommendations.remove(userRecommendation)) {
    		throw new InexistentObjectException(UserRecommendation.class, 
    				"UserRecommendation not found!");
    	}
	}
    
    /**
     * Return Customer information as a String.
     * @return String representation of this Customer
     */
    public String toString() {
    	String information = "";
    	
    	this.getUser();
    	
    	information += this.user.toString() + "\n";
    	if (this.key == null) {
    		information += "Customer key: NULL (object is transient) \n";
    	}
    	else {
    		information += "Customer key: " + this.key.toString() + "\n";
    	}
    	information += "Customer name: " + this.customerName + "\n";
    	information += "Customer phone: " + this.customerPhone.getNumber() + "\n";
    	information += "Customer gender: " + this.gender + "\n";
    	information += "Customer address: " + this.customerAddress.getAddress() + "\n";
    	information += "Customer status: " + this.status + "\n";
    	information += "Customer comments: " + this.customerComments;
    	
    	return information;
    }
    
}
