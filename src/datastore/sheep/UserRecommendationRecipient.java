/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package datastore.sheep;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the UserRecommendationRecipient table.
 * It is managed as a JDO to be stored in and retrieved 
 * from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class UserRecommendationRecipient implements Serializable {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key customerKey;
    
    @Persistent
    private Key groupKey;

    /**
     * UserRecommendation constructor.
     * @param customerKey
     * 			: the key of the recipient customer
     * @param groupKey
     * 			: the key of the recipient group
     * @throws MissingRequiredFieldsException
     */
    public UserRecommendationRecipient(Key customerKey,
    		Key groupKey) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (customerKey == null && groupKey == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.customerKey = customerKey;
    	this.groupKey = groupKey;
    }

    /**
     * Compare this UserRecommendationRecipient with another 
     * UserRecommendationRecipient
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this 
     * 			UserRecommendationRecipient, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof UserRecommendationRecipient ) ) return false;
        UserRecommendationRecipient userRecommendationRecipient =
        		(UserRecommendationRecipient) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(
                		userRecommendationRecipient.getKey()));
    }

    /**
     * Get UserRecommendation key.
     * @return UserRecommendation key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get customer key
     * @return customerKey
     */
    public Key getCustomerKey() {
    	return customerKey;
    }
    
    /**
     * Get group key
     * @return groupKey
     */
    public Key getGroupKey() {
    	return groupKey;
    }
	
}