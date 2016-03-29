/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package datastore.sheep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import datastore.Customer;
import datastore.PMF;
import exceptions.InexistentObjectException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the UserRecommendation class.
 */

public class UserRecommendationManager {
	
	private static final Logger log = 
        Logger.getLogger(UserRecommendationManager.class.getName());
	
	/**
     * Get a UserRecommendation instance from the datastore given the UserRecommendation key.
     * @param key
     * 			: the userRecommendation's key
     * @return userRecommendation instance, null if userRecommendation is not found
     */
	public static UserRecommendation getUserRecommendation(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserRecommendation userRecommendation;
		try  {
			userRecommendation = pm.getObjectById(UserRecommendation.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return userRecommendation;
	}
	
	/**
     * Get the UserRecommendations in the database that belong to a customer
     * and return them in a List structure
     * @param customerKey
     * @param date
     * 			: the earliest date to retrieve recommendations from
     * @return userRecommendations in the datastore that belong to the given 
     * 			customer
     */
    public static List<UserRecommendation> getOwnedRecommendations(Key customerKey, 
    		Date date) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
        	Customer customer = pm.getObjectById(Customer.class, customerKey);
        	
        	List<UserRecommendation> allRecommendations = customer.getUserRecommendations();
        	ArrayList<UserRecommendation> userRecommendations = 
        			new ArrayList<UserRecommendation>();
        	// Check that recommendations are not Favorites
        	for (UserRecommendation userRecommendation : allRecommendations) {
        		if (userRecommendation.getUserRecommendationSuperType() ==
        				UserRecommendation.UserRecommendationSuperType.RECOMMENDATION) {
        			
        			// Check date
        			if (date != null) {
        				if (userRecommendation.getUserRecommendationCreationDate().
        						compareTo(date) >= 0) {
        					userRecommendations.add(userRecommendation);
        				}
        			}
        			else {
        				userRecommendations.add(userRecommendation);
        			}
        		}
        	}

        	return userRecommendations;
        } 
        catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        finally {
        	pm.close();
        }
    }
    
    /**
     * Get the UserRecommendations in the database that a customer
     * belongs to (is following)
     * @param customerKey
     * 				: the key of the customer
     * @param date
     * 				: the earliest date to retrieve recommendations from
     * @return userRecommendations in the datastore that the given customer
     * is following
     */
    @SuppressWarnings("unchecked")
	public static List<UserRecommendation> getFollowedRecommendations(
			Key customerKey, Date date) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		ArrayList<UserRecommendation> userRecommendations = 
				new ArrayList<UserRecommendation>();
		Query query = pm.newQuery(UserRecommendationRecipient.class);
        try {
        	// First get the recommendation recipients for this customer
        	query.setFilter("customerKey == ckey");
            query.declareParameters(Key.class.getName() + " ckey");
            
            List<UserRecommendationRecipient> userRecommendationRecipients = 
            		(List<UserRecommendationRecipient>) query.execute(customerKey);

            // Now get the user recommendations that are not Favorites
            for (UserRecommendationRecipient userRecommendationRecipient : 
            	userRecommendationRecipients) {
            	
            	UserRecommendation userRecommendation = 
            			pm.getObjectById(UserRecommendation.class, 
            					userRecommendationRecipient.getKey().getParent());
            	
            	// Check that it is not a Favorite
            	if (userRecommendation.getUserRecommendationSuperType() ==
        				UserRecommendation.UserRecommendationSuperType.RECOMMENDATION) {
            		
            		// Check date
        			if (date != null) {
        				if (userRecommendation.getUserRecommendationCreationDate().
        						compareTo(date) >= 0) {
        					userRecommendations.add(userRecommendation);
        				}
        			}
        			else {
        				userRecommendations.add(userRecommendation);
        			}
        		}
            }
            
        	return userRecommendations;
        }
        catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        finally {
        	pm.close();
        	query.closeAll();
        }
    }
	
    /**
     * Get the UserRecommendations in the database that belong to the 
     * My Favorites list of a customer and return them in a List structure
     * @param customerKey
     * @return userRecommendations in the datastore that belong to the given 
     * 			customer that are part of the My Favorites list
     */
    public static List<UserRecommendation> getMyFavorites(Key customerKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
        	Customer customer = pm.getObjectById(Customer.class, customerKey);
        	
        	List<UserRecommendation> allRecommendations = customer.getUserRecommendations();
        	ArrayList<UserRecommendation> userRecommendations = 
        			new ArrayList<UserRecommendation>();
        	// Check that recommendations are Favorites
        	for (UserRecommendation userRecommendation : allRecommendations) {
        		if (userRecommendation.getUserRecommendationSuperType() ==
        				UserRecommendation.UserRecommendationSuperType.FAVORITE) {
        			userRecommendations.add(userRecommendation);
        		}
        	}

        	return userRecommendations;
        } 
        catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        finally {
        	pm.close();
        }
    }
    
	/**
     * Put UserRecommendation into datastore.
     * Stores the given userRecommendation instance for a Customer in the datastore
     * calling the PersistenceManager's makePersistent() method.
     * @param customerKey
     * 			: the key of the customer to which the recommendation belongs
     * @param userRecommendation
     * 			: the userRecommendation instance to store
     */
	public static void putUserRecommendation(Key customerKey, 
			UserRecommendation userRecommendation) 
			throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Customer customer = pm.getObjectById(Customer.class, customerKey);
			tx.begin();
			customer.addUserRecommendation(userRecommendation);
			tx.commit();
			log.info("UserRecommendation \"" + 
					userRecommendation.getUserRecommendationCreationDate() + 
					"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Delete UserRecommendation from datastore.
    * Deletes the given userRecommendation from the datastore 
    * calling the PersistenceManager's deletePersistent() method.
    * @param userRecommendationKey
    * 			: the key of the userRecommendation instance to delete
    */
	public static void deleteUserRecommendation(Key userRecommendationKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Customer customer = pm.getObjectById(Customer.class, 
					userRecommendationKey.getParent());
			UserRecommendation userRecommendation = pm.getObjectById(UserRecommendation.class, 
					userRecommendationKey);
			Date userRecommendationDate = 
					userRecommendation.getUserRecommendationCreationDate();
			tx.begin();
			customer.removeUserRecommendation(userRecommendation);
			tx.commit();
			log.info("UserRecommendation \"" + userRecommendationDate + 
					"\" deleted successfully from datastore.");
		}
		catch (InexistentObjectException e) {
			e.printStackTrace();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
