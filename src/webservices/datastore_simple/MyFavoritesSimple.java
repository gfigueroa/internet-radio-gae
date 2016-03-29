/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the My Favorites list.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class MyFavoritesSimple implements Serializable {

	public String userEmail;
	public ArrayList<UserRecommendationsSimple.UserRecommendationSimple> 
			myFavorites;
    
    /**
     * UserRecommendationsSimple constructor.
     * @param userEmail
     * 			: the email of the user
	 * @param myFavorites
     */
    public MyFavoritesSimple(String userEmail, 
    		ArrayList<UserRecommendationsSimple.UserRecommendationSimple> 
    		myFavorites) {
    	this.userEmail = userEmail;
    	this.myFavorites = myFavorites;
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
        if ( !(o instanceof MyFavoritesSimple ) ) return false;
        MyFavoritesSimple ur = (MyFavoritesSimple) o;
        return this.userEmail.equalsIgnoreCase(ur.userEmail);
    }
    
}