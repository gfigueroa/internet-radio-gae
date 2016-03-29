/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a list of given and received user recommendations.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class UserRecommendationsSimple implements Serializable {
    
	public static class UserRecommendationSimple {
		
		public String key;
		public String ownerEmail;
		public String userRecommendationType;
		public String userRecommendationDate;
		public String userRecommendationComment;
		public String itemKey;
		public ProgramSimple message; // TODO: This is only used if the type is MESSAGE
		
		/**
		 * UserRecommendationSimple constructor.
		 * @param key
		 * @param ownerEmail
		 * @param userRecommendationType
		 * @param userRecommendationDate
		 * @param userRecommendationComment
		 * @param itemKey
		 */
		public UserRecommendationSimple(String key, String ownerEmail,
				String userRecommendationType, String userRecommendationDate,
				String userRecommendationComment, String itemKey,
				ProgramSimple message) {
			
			this.key = key;
			this.ownerEmail = ownerEmail;
			this.userRecommendationType = userRecommendationType;
			this.userRecommendationDate = userRecommendationDate;
			this.userRecommendationComment = userRecommendationComment;
			this.itemKey = itemKey;
			this.message = message;
		}
	}
	
	public String userEmail;
	public ArrayList<UserRecommendationSimple> sharedRecommendations;
	public ArrayList<UserRecommendationSimple> receivedRecommendations;
    
    /**
     * UserRecommendationsSimple constructor.
     * @param userEmail
     * 			: the email of the user
	 * @param sharedRecommendations
	 * 			: the recommendations owned by this user
	 * @param receivedRecommendations
	 * 			: the recommendations followed by this user
     */
    public UserRecommendationsSimple(String userEmail, 
    		ArrayList<UserRecommendationSimple> sharedRecommendations, 
    		ArrayList<UserRecommendationSimple> receivedRecommendations) {
    	this.userEmail = userEmail;
    	this.sharedRecommendations = sharedRecommendations;
    	this.receivedRecommendations = receivedRecommendations;
    }
    
    /**
     * Compare this userRecommendation with another UserRecommendation
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this UserRecommendation, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof UserRecommendationsSimple ) ) return false;
        UserRecommendationsSimple ur = (UserRecommendationsSimple) o;
        return this.userEmail.equalsIgnoreCase(ur.userEmail);
    }
    
}