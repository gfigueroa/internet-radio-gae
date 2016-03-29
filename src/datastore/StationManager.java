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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PostalAddress;

import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UserValidationException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Station class.
 * 
 */

public class StationManager {
	
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
	private static final Logger log = 
	        Logger.getLogger(StationManager.class.getName());

	/**
     * Get a Station instance from the datastore given its user entity.
     * The method uses the user's email field to obtain the Station key.
     * @param user
     * 			: the user belonging to this Station
     * @return Station instance, null if Station is not found
     */
	public static Station getStation(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
                                       user.getUserEmail().getEmail());
		Station station;
		try  {
			station = pm.getObjectById(Station.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return station;
	}
	
	/**
     * Get a Station instance from the datastore given the user's email.
     * The method uses this email to obtain the Station key.
     * @param email
     * 			: the Station's email address
     * @return Station instance, null if Station is not found
     */
	public static Station getStation(Email email) {		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
                                       email.getEmail());
		
		Station station;
		try  {
			station = pm.getObjectById(Station.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return station;
	}
	
	/**
     * Get a Station instance from the datastore given the Station key.
     * @param key
     * 			: the Station's key
     * @return Station instance, null if Station is not found
     */
	public static Station getStation(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Station station;
		try  {
			station = pm.getObjectById(Station.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return station;
	}
	
	/**
     * Get ALL the Stations in the database and return them
     * in a List structure
     * @return all stations in the datastore
     * TODO: Make more efficient "touching" of the users
     */
    @SuppressWarnings("unchecked")
	public static List<Station> getAllStations() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Station.class);

        try {
        	List<Station> stations = (List<Station>) query.execute();
        	// Touch the user to keep in memory
        	for (Station station : stations) {
        		station.getUser();
        	}
        	return stations;
            //return (List<Station>) query.execute();
        } finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Station in the datastore that belong to
     * the given station type and returns them in a List structure
     * @parameter stationType:
     * 			the station type filter
     * @return all station in the datastore corresponding to
     * 			the given station type
     * TODO: make more efficient "touching" of the station
     */
	@SuppressWarnings("unchecked")
	public static List<Station> getAllStationsByType(Long stationType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Station.class);
        query.setFilter("stationType == type");
        query.declareParameters("Long type");

        List<Station> result = null;
        try {
            result = (List<Station>) query.execute(stationType);
            // touch
            for (Station station : result)
                station.getStationName();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put Station into datastore.
     * Stations the given Station instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param StationSimple
     * 			: the Station instance to station
     * @throws ObjectExistsInDatastoreException
     */
	public static void putStation(Station station) 
           throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if the user already exists in the datastore
		Email email = station.getUser().getUserEmail();
		Key administratorKey = KeyFactory.createKey(
				Administrator.class.getSimpleName(), email.getEmail());
		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
				email.getEmail());
		if (DatastoreManager.entityExists(Station.class, 
				station.getKey()) || 
				DatastoreManager.entityExists(Administrator.class, 
						administratorKey) ||
				DatastoreManager.entityExists(Customer.class, customerKey)) {
			throw new ObjectExistsInDatastoreException(station, "User \"" + 
					station.getUser().getUserEmail().getEmail() + 
					"\" already exists in the datastore.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(station);
			tx.commit();
			log.info("Station \"" + 
					station.getUser().getUserEmail().getEmail() + 
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
    * Delete Station from datastore.
    * Deletes the given Station from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param StationSimple
    * 			: the Station instance to delete
    */
	public static void deleteStation(Station station) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			station = pm.getObjectById(Station.class, 
					station.getKey());
			String email = station.getUser().getUserEmail().getEmail();
			BlobKey stationLogo = station.getStationLogo();
			
			tx.begin();
			pm.deletePersistent(station);
			tx.commit();
			
			if (stationLogo != null) {
				blobstoreService.delete(stationLogo);
			}
			
			log.info("Station \"" + email +
					"\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Station password in datastore.
    * Update's the Station's password in the datastore.
    * @param email
    * 			: the email of the Station whose password will be changed
    * @param currentPassword
    * 			: the current password of this Station
    * @param newPassword
    * 			: the new password for this Station
    * @throws UserValidationException
	* @throws MissingRequiredFieldsException 
    */
	public static void updateStationPassword(Email email, 
			String currentPassword, String newPassword) 
			throws UserValidationException, MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
					email.getEmail());
			Station station = pm.getObjectById(Station.class, key);
			tx.begin();
			if (station.getUser().validateUser(email, currentPassword)) {
				station.getUser().setUserPassword(newPassword);
				tx.commit();
				log.info("Station \"" + email.getEmail() + 
						"\"'s password updated in datastore.");
			}
			else {
				tx.rollback();
				throw new UserValidationException(station.getUser(), 
						"User email and/or password are incorrect.");
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
    * Update Station password in datastore.
    * Update's the Station's password in the datastore.
    * @param email
    * 			: the email of the Station whose password will be changed
    * @param newPassword
    * 			: the new password for this Station
	* @throws MissingRequiredFieldsException 
    */
	public static void updateStationPassword(Email email, String newPassword) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
					email.getEmail());
			Station station = pm.getObjectById(Station.class, key);
			tx.begin();
			station.getUser().setUserPassword(newPassword);
			tx.commit();
			log.info("Station \"" + email.getEmail() + 
					"\"'s password updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Station attributes.
    * Update's the given Station's attributes in the datastore.
    * @param email
    * 			: the email of the Station whose attributes will be updated
    * @param stationType
    * 			: the new station type's key to give to the Station
    * @param stationPrivilegeLevel
    * 			: the new privilege level of the station
    * @param stationName
    * 			: the new name to give to the Station
    * @param stationNumber
    * 			: the new number to give to the Station
    * @param stationDescription
    * 			: the new description to give to the Station
    * @param region
    * 			: the new region of the Station
    * @param stationAddress
    * 			: the new address of the Station
    * @param stationWebsite
    * 			: Station website
    * @param stationLogo
    * 			: Station logo blob key
    * @param stationComments
    * 			: the new comments to give to the Station
	* @throws MissingRequiredFieldsException 
    */
	public static void updateStationAttributes(
			Email email,
			Long stationType,
			Integer stationPrivilegeLevel,
            String stationName,
            String stationNumber,
            String stationDescription,
            Long region,
            PostalAddress stationAddress,
            Link stationWebsite, 
            BlobKey stationLogo, 
            String stationComments) 
            		throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
					email.getEmail());
			Station station = pm.getObjectById(Station.class, key);
			
			BlobKey oldStationLogo = station.getStationLogo();
			
			tx.begin();
			station.setStationType(stationType);
			station.setPrivilegeLevel(stationPrivilegeLevel);
			station.setStationName(stationName);
			station.setStationNumber(stationNumber);
			station.setStationDescription(stationDescription);
			station.setRegion(region);
			station.setStationAddress(stationAddress);
			station.setStationWebsite(stationWebsite);
			station.setStationLogo(stationLogo);
			station.setStationComments(stationComments);
			tx.commit();
			
			if (!oldStationLogo.equals(stationLogo) &&
					oldStationLogo != null) {
				blobstoreService.delete(oldStationLogo);
			}
			
			log.info("Station \"" + email.getEmail() + 
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
    * Update Station Playlist Version.
    * Updates the given Station's Playlist Version by 1 in the datastore.
    * @param email
    * 			: the email of the Station whose attributes will be updated
    */
	public static void updateStationPlaylistVersion(Email email) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
					email.getEmail());
			Station station = pm.getObjectById(Station.class, key);
			tx.begin();
			station.updatePlaylistVersion();
			tx.commit();
			log.info("Station \"" + email.getEmail() + 
					"\"'s Playlist version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
