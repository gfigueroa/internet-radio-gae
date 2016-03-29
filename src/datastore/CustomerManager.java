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
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.sheep.CloudSyncCommand;

import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UserValidationException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the Customer class.
 */

public class CustomerManager {
	
	private static final Logger log = 
        Logger.getLogger(CustomerManager.class.getName());

	/**
     * Get a Customer instance from the datastore given its user entity.
     * The method uses the user's email field to obtain the Customer key.
     * @param user
     * 			: the user belonging to this customer
     * @return customer instance, null if customer is not found
     */
	public static Customer getCustomer(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Customer.class.getSimpleName(), 
                                       user.getUserEmail().getEmail());
		Customer customer;
		try  {
			customer = pm.getObjectById(Customer.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return customer;
	}
	
	/**
     * Get a Customer instance from the datastore given the user's email.
     * The method uses this email to obtain the Customer key.
     * @param email
     * 			: the customer's email address
     * @return customer instance, null if customer is not found
     */
	public static Customer getCustomer(Email email) {		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Customer.class.getSimpleName(), 
                                       email.getEmail());
		
		Customer customer;
		try  {
			customer = pm.getObjectById(Customer.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return customer;
	}
	
	/**
     * Get a Customer instance from the datastore given the Customer key.
     * @param key
     * 			: the customer's key
     * @return customer instance, null if customer is not found
     */
	public static Customer getCustomer(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Customer customer;
		try  {
			customer = pm.getObjectById(Customer.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return customer;
	}
	
	/**
     * Get ALL the Customers in the database and return them
     * in a List structure
     * @return all customers in the datastore
     * TODO: Make more efficient "touching" of the users
     */
    @SuppressWarnings("unchecked")
	public static List<Customer> getAllCustomers() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Customer.class);

        try {
        	List<Customer> customers = (List<Customer>) query.execute();
        	// Touch the user to keep in memory
        	for (Customer customer : customers) {
        		customer.getUser();
        	}
        	return customers;
            // return (List<Customer>) query.execute();
        } finally {
        	pm.close();
            query.closeAll();
        }
    }
	
	/**
     * Put Customer into datastore.
     * Stores the given customer instance in the datastore calling the PersistenceManager's
     * makePersistent() method.
     * @param customer
     * 			: the customer instance to store
     * @throws ObjectExistsInDatastoreException 
     */
	public static void putCustomer(Customer customer) throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if the user already exists in the datastore
		Email email = customer.getUser().getUserEmail();
		Key administratorKey = KeyFactory.createKey(Administrator.class.getSimpleName(), email.getEmail());
		Key restaurantKey = KeyFactory.createKey(Station.class.getSimpleName(), email.getEmail());
		if (DatastoreManager.entityExists(Customer.class, customer.getKey()) || 
				DatastoreManager.entityExists(Administrator.class, administratorKey) ||
				DatastoreManager.entityExists(Station.class, restaurantKey)) {
			throw new ObjectExistsInDatastoreException(customer, "User \"" + 
					customer.getUser().getUserEmail().getEmail() + 
					"\" already exists in the datastore.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(customer);
			tx.commit();
			log.info("Customer \"" + customer.getUser().getUserEmail().getEmail() + 
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
    * Delete Customer from datastore.
    * Deletes the given customer from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param customer
    * 			: the customer instance to delete
    */
	public static void deleteCustomer(Customer customer) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			customer = pm.getObjectById(Customer.class, customer.getKey());
			String email = customer.getUser().getUserEmail().getEmail();
			tx.begin();
			pm.deletePersistent(customer);
			tx.commit();
			log.info("Customer \"" + email + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete Customer from datastore.
    * Deletes the customer corresponding to the given email 
    * from the datastore calling the PersistenceManager's deletePersistent() method.
    * @param email
    * 			: the email of the customer instance to delete
    */
	public static void deleteCustomer(Email email) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Customer.class.getSimpleName(), email.getEmail());
			Customer customer = pm.getObjectById(Customer.class, key);
			tx.begin();
			pm.deletePersistent(customer);
			tx.commit();
			log.info("Customer \"" + email.getEmail() + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Customer password in datastore.
    * Update's the customer's password in the datastore.
    * @param email
    * 			: the email of the customer whose password will be changed
    * @param currentPassword
    * 			: the current password of this customer
    * @param newPassword
    * 			: the new password for this customer
    * @throws UserValidationException 
	* @throws MissingRequiredFieldsException 
    */
	public static void updateCustomerPassword(Email email, String currentPassword,
			String newPassword) throws UserValidationException, MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Customer.class.getSimpleName(), email.getEmail());
			Customer customer = pm.getObjectById(Customer.class, key);
			tx.begin();
			if (customer.getUser().validateUser(email, currentPassword)) {
				customer.getUser().setUserPassword(newPassword);
				tx.commit();
				log.info("Customer \"" + email.getEmail() + "\"'s password updated in datastore.");
			}
			else {
				tx.rollback();
				throw new UserValidationException(customer.getUser(), "User email and/or password are incorrect.");
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
    * Update Customer password in datastore.
    * Update's the customer's password in the datastore.
    * @param email
    * 			: the email of the customer whose password will be changed
    * @param newPassword
    * 			: the new password for this customer
	* @throws MissingRequiredFieldsException 
    */
	public static void updateCustomerPassword(Email email, String newPassword) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Customer.class.getSimpleName(), email.getEmail());
			Customer customer = pm.getObjectById(Customer.class, key);
			tx.begin();
			customer.getUser().setUserPassword(newPassword);
			tx.commit();
			log.info("Customer \"" + email.getEmail() + "\"'s password updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Customer status in datastore.
    * Update's the customer's status in the datastore.
    * @param email
    * 			: the email of the customer whose status will be changed
    * @param status
    * 			: the status to be assigned to this customer
	* @throws MissingRequiredFieldsException
    */
	public static void updateCustomerStatus(Email email, Customer.Status status) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Customer.class.getSimpleName(), email.getEmail());
			Customer customer = pm.getObjectById(Customer.class, key);
			tx.begin();
			customer.setCustomerStatus(status);
			tx.commit();
			log.info("Customer \"" + email.getEmail() + "\"'s status updated in datastore.");
			
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Customer attributes.
    * Update's the given customer's attributes in the datastore.
    * @param email
    * 			: the email of the customer whose attributes will be updated
    * @param customerName
    * 			: the new name to give to the customer
    * @param customerPhone
    * 			: the new phone to give to the customer
    * @param customerGender
    * 			: the new gender to give to the customer
    * @param customerAddress
    * 			: the new address to give to the customer
    * @param customerComments
    * 			: the new comments to give to the customer
	* @throws MissingRequiredFieldsException 
	* @throws InvalidFieldFormatException 
    */
	public static void updateCustomerAttributes(Email email, String customerName, 
			PhoneNumber customerPhone, Customer.Gender customerGender, PostalAddress customerAddress,
			String customerComments) throws MissingRequiredFieldsException, InvalidFieldFormatException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Customer.class.getSimpleName(), email.getEmail());
			Customer customer = pm.getObjectById(Customer.class, key);
			tx.begin();
			customer.setCustomerName(customerName);
			customer.setCustomerPhone(customerPhone);
			customer.setCustomerGender(customerGender);
			customer.setCustomerAddress(customerAddress);
			customer.setCustomerComments(customerComments);
			tx.commit();
			log.info("Customer \"" + email.getEmail() + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Customer cloud sync command.
    * Update's the given customer's cloud sync command in the datastore.
    * @param email
    * 			: the email of the customer whose attributes will be updated
    * @param cloudSyncCommand
    * 			: the cloud sync message to set
    */
	public static void updateCustomerCloudSyncCommand(Email email, 
			CloudSyncCommand cloudSyncCommand)  {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Customer.class.getSimpleName(), 
					email.getEmail());
			Customer customer = pm.getObjectById(Customer.class, key);
			CloudSyncCommand previousCloudSyncCommand = customer.getCloudSyncCommand();
			tx.begin();
			pm.deletePersistent(previousCloudSyncCommand);
			customer.setCloudSyncCommand(cloudSyncCommand);
			tx.commit();
			log.info("Customer \"" + email.getEmail() + 
					"\"'s cloud sync command updated.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
