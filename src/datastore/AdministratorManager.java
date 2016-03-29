/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UserValidationException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the Administrator class.
 */

public class AdministratorManager {
	
	private static final Logger log = 
        Logger.getLogger(AdministratorManager.class.getName());

	/**
     * Get an Administrator instance from the datastore given its user entity.
     * The method uses the user's email field to obtain the Administrator key.
     * @param user
     * 			: the user belonging to this administrator
     * @return administrator instance, null if administrator is not found
     */
	public static Administrator getAdministrator(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Administrator.class.getSimpleName(), 
                                       user.getUserEmail().getEmail());
		Administrator administrator;
		try  {
			administrator = pm.getObjectById(Administrator.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return administrator;
	}
	
	/**
     * Get a Administrator instance from the datastore given the user's email.
     * The method uses this email to obtain the Administrator key.
     * @param email
     * 			: the administrator's email address
     * @return administrator instance, null if administrator is not found
     */
	public static Administrator getAdministrator(Email email) {		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Administrator.class.getSimpleName(), 
                                       email.getEmail());
		
		Administrator administrator;
		try  {
			administrator = pm.getObjectById(Administrator.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return administrator;
	}
	
	/**
     * Get a Administrator instance from the datastore given the Administrator key.
     * @param key
     * 			: the administrator's key
     * @return administrator instance, null if administrator is not found
     */
	public static Administrator getAdministrator(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Administrator administrator;
		try  {
			administrator = pm.getObjectById(Administrator.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return administrator;
	}
	
	/**
     * Get ALL the Administrators in the database and return them
     * in a List structure
     * @return all administrators in the datastore
     * TODO: Make more efficient "touching" of the users
     */
    @SuppressWarnings("unchecked")
	public static List<Administrator> getAllAdministrators() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Administrator.class);

        try {
        	List<Administrator> administrators = (List<Administrator>) query.execute();
        	// Touch the user to keep in memory
        	for (Administrator admin : administrators) {
        		admin.getUser();
        	}
        	return administrators;
            //return (List<Administrator>) query.execute();
        } finally {
        	pm.close();
            query.closeAll();
        }
    }
	
	/**
     * Put Administrator into datastore.
     * Stores the given administrator instance in the datastore calling the PersistenceManager's
     * makePersistent() method.
     * @param administrator
     * 			: the administrator instance to store
     * @throws ObjectExistsInDatastoreException
     */
	public static void putAdministrator(Administrator administrator) throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if the user already exists in the datastore
		Email email = administrator.getUser().getUserEmail();
		Key restaurantKey = KeyFactory.createKey(Station.class.getSimpleName(), email.getEmail());
		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), email.getEmail());
		if (DatastoreManager.entityExists(Administrator.class, administrator.getKey()) || 
				DatastoreManager.entityExists(Station.class, restaurantKey) ||
				DatastoreManager.entityExists(Customer.class, customerKey)) {
			throw new ObjectExistsInDatastoreException(administrator, "User \"" + 
					administrator.getUser().getUserEmail().getEmail() + 
					"\" already exists in the datastore.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(administrator);
			tx.commit();
			log.info("Administrator \"" + administrator.getUser().getUserEmail().getEmail() + 
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
    * Delete Administrator from datastore.
    * Deletes the given administrator from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param administrator
    * 			: the administrator instance to delete
    */
	public static void deleteAdministrator(Administrator administrator) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			administrator = pm.getObjectById(Administrator.class, administrator.getKey());
			String email = administrator.getUser().getUserEmail().getEmail();
			tx.begin();
			pm.deletePersistent(administrator);
			tx.commit();
			log.info("Administrator \"" + email + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete Administrator from datastore.
    * Deletes the administrator corresponding to the given email 
    * from the datastore calling the PersistenceManager's deletePersistent() method.
    * @param email
    * 			: the email of the administrator instance to delete
    */
	public static void deleteAdministrator(Email email) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Administrator.class.getSimpleName(), email.getEmail());
			Administrator administrator = pm.getObjectById(Administrator.class, key);
			tx.begin();
			pm.deletePersistent(administrator);
			tx.commit();
			log.info("Administrator \"" + email.getEmail() + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Administrator password in datastore.
    * Update's the administrator's password in the datastore.
    * @param email
    * 			: the email of the administrator whose password will be changed
    * @param currentPassword
    * 			: the current password of this administrator
    * @param newPassword
    * 			: the new password for this administrator
	* @throws UserValidationException
	* @throws MissingRequiredFieldsException
    */
	public static void updateAdministratorPassword(Email email, String currentPassword,
			String newPassword) throws UserValidationException, MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Administrator.class.getSimpleName(), email.getEmail());
			Administrator administrator = pm.getObjectById(Administrator.class, key);
			tx.begin();
			if (administrator.getUser().validateUser(email, currentPassword)) {
				administrator.getUser().setUserPassword(newPassword);
				tx.commit();
				log.info("Administrator \"" + email.getEmail() + "\"'s password updated in datastore.");
			}
			else {
				tx.rollback();
				throw new UserValidationException(administrator.getUser(), "User email and/or password are incorrect.");
			}
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Administrator password in datastore.
    * Update's the administrator's password in the datastore.
    * @param email
    * 			: the email of the administrator whose password will be changed
    * @param newPassword
    * 			: the new password for this administrator
	* @throws MissingRequiredFieldsException
    */
	public static void updateAdministratorPassword(Email email, String newPassword) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Administrator.class.getSimpleName(), email.getEmail());
			Administrator administrator = pm.getObjectById(Administrator.class, key);
			tx.begin();
			administrator.getUser().setUserPassword(newPassword);
			tx.commit();
			log.info("Administrator \"" + email.getEmail() + "\"'s password updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update Administrator attributes.
    * Update's the given administrator's attributes in the datastore.
    * @param email
    * 			: the email of the administrator whose attributes will be updated
    * @param administratorName
    * 			: the new name to give to the administrator
    * @param administratorComments
    * 			: the new comments to give to the administrator
	 * @throws MissingRequiredFieldsException 
    */
	public static void updateAdministratorAttributes(Email email, String administratorName,
			String administratorComments) throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Administrator.class.getSimpleName(), email.getEmail());
			Administrator administrator = pm.getObjectById(Administrator.class, key);
			tx.begin();
			administrator.setAdministratorName(administratorName);
			administrator.setAdministratorComments(administratorComments);
			tx.commit();
			log.info("Administrator \"" + email.getEmail() + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}