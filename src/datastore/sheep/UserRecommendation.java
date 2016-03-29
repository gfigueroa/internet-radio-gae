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

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the UserRecommendation table.
 * It is managed as a JDO to be stored in and retrieved 
 * from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class UserRecommendation implements Serializable {
	
	public static enum UserRecommendationSuperType {
		RECOMMENDATION, FAVORITE
	}
	
	public static enum UserRecommendationType {
		PRODUCT_ITEM, SET, STORE, NEWS, OPINION_POLL, SURVEY, MESSAGE
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private UserRecommendationSuperType userRecommendationSuperType;
    
    @Persistent
    private UserRecommendationType userRecommendationType;
    
    @Persistent
    private Date userRecommendationCreationDate;
    
    @Persistent
    private String userRecommendationComment;
    
    @Persistent
    private Key userRecommendationItemKey;
    
    @Persistent
    private ArrayList<UserRecommendationRecipient> userRecommendationRecipients;

    /**
     * UserRecommendation constructor.
     * @param userRecommendationSuperType
     * 			: the user recommendation super type
     * @param userRecommendationType
     * 			: the user recommendation type
     * @param userRecommendationComment
     * 			: the user recommendation comment
     * @param userRecommendationItemKey
     * 			: the key of the item being recommended
     * @param userRecommendationRecipients
     * 			: the recipients of this recommendation
     * @throws MissingRequiredFieldsException
     */
    public UserRecommendation(UserRecommendationSuperType userRecommendationSuperType,
    		UserRecommendationType userRecommendationType,
    		String userRecommendationComment, Key userRecommendationItemKey,
    		ArrayList<UserRecommendationRecipient> userRecommendationRecipients) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (userRecommendationSuperType == null ||
    			userRecommendationType == null || userRecommendationItemKey == null ||
    			userRecommendationRecipients == null || 
    			userRecommendationRecipients.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.userRecommendationSuperType = userRecommendationSuperType;
    	this.userRecommendationType = userRecommendationType;
    	this.userRecommendationCreationDate = new Date();
    	this.userRecommendationComment = userRecommendationComment;
    	this.userRecommendationItemKey = userRecommendationItemKey;
    	this.userRecommendationRecipients = userRecommendationRecipients;
    }

    /**
     * Compare this UserRecommendation with another UserRecommendation
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this UserRecommendation, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof UserRecommendation ) ) return false;
        UserRecommendation userRecommendation = (UserRecommendation) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(userRecommendation.getKey()));
    }

    /**
     * Get UserRecommendation key.
     * @return UserRecommendation key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get userRecommendationSuperType
     * @return userRecommendationSuperType
     */
	public UserRecommendationSuperType getUserRecommendationSuperType() {
		return userRecommendationSuperType;
	}
    
	/**
	 * Return userRecommendationSuperType as String
	 * @return a string representation of the userRecommendationSuperType
	 */
	public String getUserRecommendationSuperTypeString() {
		
		switch (userRecommendationSuperType) {
			case RECOMMENDATION:
				return "Recommendation";
			case FAVORITE:
				return "Favorite";
			default:
				return null;
		}
	}
	
	/**
	 * Return UserRecommendationSuperType from a String representation
	 * @param userRecommendationSuperTypeString
	 * 			: the string representation of the userRecommendationSuperType
	 * @return userRecommendationSuperType
	 */
	public static UserRecommendationSuperType 
			getUserRecommendationSuperTypeFromString(
					String userRecommendationSuperTypeString) {
		
		if (userRecommendationSuperTypeString == null) {
			return null;
		}
		else if (userRecommendationSuperTypeString.equalsIgnoreCase("recommendation")) {
			return UserRecommendationSuperType.RECOMMENDATION;
		}
		else if (userRecommendationSuperTypeString.equalsIgnoreCase("favorite")) {
			return UserRecommendationSuperType.FAVORITE;
		}
		else {
			return null;
		}
	}
	
    /**
     * Get userRecommendationType
     * @return userRecommendationType
     */
	public UserRecommendationType getUserRecommendationType() {
		return userRecommendationType;
	}
	
	/**
	 * Return userRecommendationType as String
	 * @return a string representation of the userRecommendationType
	 */
	public String getUserRecommendationTypeString() {
		
		switch (userRecommendationType) {
			case PRODUCT_ITEM:
				return "Product Item";
			case SET:
				return "Set";
			case STORE:
				return "Store";
			case NEWS:
				return "News";
			case OPINION_POLL:
				return "Opinion Poll";
			case SURVEY:
				return "Survey";
			case MESSAGE:
				return "Program";
			default:
				return null;
		}
	}
	
	/**
	 * Return UserRecommendationType from a String representation
	 * @param userRecommendationTypeString
	 * 			: the string representation of the userRecommendationType
	 * @return userRecommendationType
	 */
	public static UserRecommendationType getUserRecommendationTypeFromString(
			String userRecommendationTypeString) {
		
		if (userRecommendationTypeString == null) {
			return null;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("product_item")) {
			return UserRecommendationType.PRODUCT_ITEM;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("set")) {
			return UserRecommendationType.SET;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("store")) {
			return UserRecommendationType.STORE;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("news")) {
			return UserRecommendationType.NEWS;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("opinion_poll")) {
			return UserRecommendationType.OPINION_POLL;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("survey")) {
			return UserRecommendationType.SURVEY;
		}
		else if (userRecommendationTypeString.equalsIgnoreCase("message")) {
			return UserRecommendationType.MESSAGE;
		}
		else {
			return null;
		}
	}

	/**
	 * Get userRecommendationCreationDate
	 * @return the userRecommendationCreationDate
	 */
	public Date getUserRecommendationCreationDate() {
		return userRecommendationCreationDate;
	}

	/**
	 * Get userRecommendationComment
	 * @return userRecommendationComment
	 */
	public String getUserRecommendationComment() {
		return userRecommendationComment;
	}
	
	/**
	 * Get userRecommendationItemKey
	 * @return userRecommendationItemKey
	 */
	public Key getUserRecommendationItemKey() {
		return userRecommendationItemKey;
	}
	
	/**
	 * Get userRecommendationRecipients
	 * @return userRecommendationRecipients
	 */
	public ArrayList<UserRecommendationRecipient> getUserRecommendationRecipients() {
		return userRecommendationRecipients;
	}
		
}