/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.logging.Logger;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the StationType class.
 * 
 */

public class StationTypeManager {
	
	private static final Logger log = 
        Logger.getLogger(StationTypeManager.class.getName());
	
	/**
     * Get a StationType instance from the datastore given the StationType key.
     * @param key
     * 			: the StationType's key
     * @return StationType instance, null if StationType is not found
     */
	public static StationType getStationType(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StationType stationType;
		try  {
			stationType = pm.getObjectById(StationType.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return stationType;
	}
	
	/**
     * Get all StationType instances from the datastore.
     * @return All StationType instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<StationType> getAllStationTypes() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(StationType.class);

        List<StationType> types = null;
        try {
        	types = (List<StationType>) query.execute();
            // touch all elements
            for (StationType t : types)
                t.getStationTypeName();
        } finally {
        	pm.close();
            query.closeAll();
        }

        return types;
    }
	
	/**
     * Put StationType into datastore.
     * Stations the given StationType instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param stationType
     * 			: the StationType instance to store
     */
	public static void putStationType(StationType stationType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(stationType);
			tx.commit();
			log.info("StationType \"" + stationType.getStationTypeName() + 
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
    * Delete StationType from datastore.
    * Deletes the StationType corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the StationType instance to delete
    */
	public static void deleteStationType(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			StationType stationType = pm.getObjectById(StationType.class, key);
			String StationTypeName = stationType.getStationTypeName();
			tx.begin();
			pm.deletePersistent(stationType);
			tx.commit();
			log.info("StationType \"" + StationTypeName + 
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
    * Update StationType attributes.
    * Update's the given StationType's attributes in the datastore.
    * @param key
    * 			: the key of the StationType whose attributes will be updated
    * @param stationTypeName
    * 			: the new name to give to the StationType
    * @param stationTypeDescription
    * 			: the new description to give to the StationType
	* @throws MissingRequiredFieldsException 
    */
	public static void updateStationTypeAttributes(Long key,
			String stationTypeName, String stationTypeDescription) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			StationType stationType = pm.getObjectById(StationType.class, key);
			tx.begin();
			stationType.setStationTypeName(stationTypeName);
			stationType.setStationTypeDescription(stationTypeDescription);
			tx.commit();
			log.info("StationType \"" + stationTypeName + 
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
	 * Updates the station type version by 1.
	 * @param key
	 * 			: the key of the station type whose version will be updated
	 */
	public static void updateStationTypeVersion(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			StationType stationType = pm.getObjectById(StationType.class, key);
			tx.begin();
			stationType.updateStationTypeVersion();
			tx.commit();
			log.info("StationType \"" + stationType.getStationTypeName() + 
                     "\"'s version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
