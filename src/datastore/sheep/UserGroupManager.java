/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package datastore.sheep;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.PMF;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the UserGroup class.
 */

public class UserGroupManager {
	
	private static final Logger log = 
        Logger.getLogger(UserGroupManager.class.getName());
	
	/**
     * Get a UserGroup instance from the datastore given the UserGroup key.
     * @param key
     * 			: the userGroup's key
     * @return userGroup instance, null if userGroup is not found
     */
	public static UserGroup getUserGroup(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserGroup userGroup;
		try  {
			userGroup = pm.getObjectById(UserGroup.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return userGroup;
	}
	
	/**
     * Get ALL the UserGroups in the database that belong to a customer
     * and return them in a List structure
     * @param customerKey
     * @return all userGroups in the datastore that belong to the given 
     * 			customer
     * TODO: Make more efficient "touching" of the user groups
     */
    public static List<UserGroup> getAllOwnedGroups(Key customerKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
        	Customer customer = pm.getObjectById(Customer.class, customerKey);
        	
        	List<UserGroup> userGroups = customer.getOwnedUserGroups();
        	// Touch the user to keep in memory
        	for (UserGroup userGroup : userGroups) {
        		userGroup.getKey();
        	}

        	return userGroups;
        } 
        finally {
        	pm.close();
        }
    }
    
    /**
     * Get ALL the UserGroups in the database that a customer
     * belongs to (is following)
     * @param customerKey
     * 				: the key of the customer
     * @return all userGroups in the datastore that the given customer
     * is following
     * TODO: Make more efficient "touching" of the user groups
     */
    public static List<UserGroup> getAllFollowedGroups(Key customerKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<UserGroup> userGroups = new ArrayList<UserGroup>();
		
        try {
        	Customer customer = pm.getObjectById(Customer.class, customerKey);
        	
        	List<Key> userGroupKeys = customer.getFollowedUserGroups();
        	
        	for (Key userGroupKey : userGroupKeys) {
        		
        		try {
        			UserGroup userGroup = pm.getObjectById(UserGroup.class, userGroupKey);
        			userGroups.add(userGroup);
        		}
        		// If user group was deleted
                catch (JDOObjectNotFoundException jdoonfe) {
                	customer.removeFollowedUserGroup(userGroupKey);
                }
        	}

        	return userGroups;
        }
        catch (InexistentObjectException e) {
			e.printStackTrace();
			return null;
		}
        finally {
        	pm.close();
        }
    }
		
	/**
     * Put UserGroup into datastore.
     * Stores the given userGroup instance for a Customer in the datastore
     * calling the PersistenceManager's makePersistent() method.
     * @param customerKey
     * 			: the key of the customer to which the group belongs
     * @param userGroup
     * 			: the userGroup instance to store
     */
	public static void putUserGroup(Key customerKey, UserGroup userGroup) 
			throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Customer customer = pm.getObjectById(Customer.class, customerKey);
			tx.begin();
			customer.addOwnedUserGroup(userGroup);
			tx.commit();
			log.info("UserGroup \"" + userGroup.getUserGroupName() + 
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
    * Delete UserGroup from datastore.
    * Deletes the given userGroup from the datastore 
    * calling the PersistenceManager's deletePersistent() method.
    * @param userGroupKey
    * 			: the key of the userGroup instance to delete
    */
	public static void deleteUserGroup(Key userGroupKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Customer customer = pm.getObjectById(Customer.class, 
					userGroupKey.getParent());
			UserGroup userGroup = pm.getObjectById(UserGroup.class, 
					userGroupKey);
			String userGroupName = userGroup.getUserGroupName();
			tx.begin();
			customer.removeOwnedUserGroup(userGroup);
			tx.commit();
			log.info("UserGroup \"" + userGroupName + 
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
	
	/**
    * Update UserGroup attributes.
    * Update's the given userGroup's attributes in the datastore.
    * @param userGroupKey
    * 			: the key of the userGroup whose attributes will be updated
    * @param userGroupName
    * 			: the new name to give to the userGroup
    * @param userGroupDescription
    * 			: the description to give to the userGRoup
	* @throws MissingRequiredFieldsException 
    */
	public static void updateUserGroupAttributes(Key userGroupKey,
			String userGroupName, String userGroupDescription) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			UserGroup userGroup = pm.getObjectById(UserGroup.class, userGroupKey);
			tx.begin();
			userGroup.setUserGroupName(userGroupName);
			userGroup.setUserGroupDescription(userGroupDescription);
			tx.commit();
			log.info("UserGroup \"" + userGroup.getUserGroupName() + 
					"\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Add member to user group.
    * @param userGroupKey
    * 			: the key of the userGroup whose attributes will be updated
    * @param memberEmail
    * 			: the email of the member (customer) to add to this group
	* @throws MissingRequiredFieldsException 
    */
	public static void addMemeberToUserGroup(Key userGroupKey,
			String memberEmail) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Transaction tx = pm.currentTransaction();
		try {
			UserGroup userGroup = pm.getObjectById(UserGroup.class, userGroupKey);
			
			Key memberKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
                    memberEmail);
			Customer member = pm.getObjectById(Customer.class, memberKey);
			
			//tx.begin();
			userGroup.addUserGroupMember(memberEmail);
			member.addFollowedUserGroup(userGroupKey);
			//tx.commit();
		}
		finally {
			//if (tx.isActive()) {
			//	tx.rollback();
			//}
			pm.close();
		}
	}
	
	/**
    * Remove member from user group.
    * @param userGroupKey
    * 			: the key of the userGroup whose attributes will be updated
    * @param memberEmail
    * 			: the email of the member (customer) to remove from this group
	* @throws MissingRequiredFieldsException 
    */
	public static void removeMemberFromUserGroup(Key userGroupKey,
			String memberEmail) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Transaction tx = pm.currentTransaction();
		try {
			UserGroup userGroup = pm.getObjectById(UserGroup.class, userGroupKey);
			
			Key memberKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
                    memberEmail);
			Customer member = pm.getObjectById(Customer.class, memberKey);
			
			//tx.begin();
			userGroup.removeUserGroupMember(memberEmail);
			member.removeFollowedUserGroup(userGroupKey);
			//tx.commit();
		}
		catch (InexistentObjectException e) {
			e.printStackTrace();
		}
		finally {
			//if (tx.isActive()) {
			//	tx.rollback();
			//}
			pm.close();
		}
	}
}
