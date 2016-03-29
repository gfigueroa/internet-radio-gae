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

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Region class.
 * 
 */

public class RegionManager {
	
	private static final Logger log = Logger.getLogger(RegionManager.class.getName());
	
	/**
     * Get a Region instance from the datastore given the Region key.
     * @param key
     * 			: the region's key
     * @return region instance, null if region is not found
     */
	public static Region getRegion(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Region region;
		try  {
			region = pm.getObjectById(Region.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return region;
	}
	
	/**
     * Get all Region instances from the datastore.
     * @return All Region instances
     * TODO: Make "touching" of regions more efficient
     */
	@SuppressWarnings("unchecked")
	public static List<Region> getAllRegions() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Region.class);

        List<Region> regions = null;
        try {
        	regions = (List<Region>) query.execute();
            // touch all elements
            for (Region r : regions)
                r.getRegionName();
        } finally {
        	pm.close();
            query.closeAll();
        }

        return regions;
    }
	
	/**
     * Put Region into datastore.
     * Stores the given region instance in the datastore calling the PersistenceManager's
     * makePersistent() method.
     * @param region
     * 			: the region instance to store
     */
	public static void putRegion(Region region) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(region);
			tx.commit();
			log.info("Region \"" + region.getRegionName() + 
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
    * Delete Region from datastore.
    * Deletes the given region from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param region
    * 			: the region instance to delete
    * TODO: Inefficient method (use deleteRegion(key) instead)
    */
	/*public static void deleteRegion(Region region) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			region = pm.getObjectById(Region.class, region.getKey());
			String regionName = region.getRegionName();
			tx.begin();
			pm.deletePersistent(region);
			tx.commit();
			log.info("Region \"" + regionName + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}*/
	
	/**
    * Delete Region from datastore.
    * Deletes the region corresponding to the given key
    * from the datastore calling the PersistenceManager's deletePersistent() method.
    * @param key
    * 			: the key of the region instance to delete
    */
	public static void deleteRegion(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Region region = pm.getObjectById(Region.class, key);
			String regionName = region.getRegionName();
			tx.begin();
			pm.deletePersistent(region);
			tx.commit();
			log.info("Region \"" + regionName + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update Region attributes.
    * Update's the given region's attributes in the datastore.
    * @param key
    * 			: the key of the region whose attributes will be updated
    * @param regionName
    * 			: the new name to give to the region
    * @param regionComments
    * 			: the new comments to give to the region
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRegionAttributes(Long key, String regionName,
			String regionComments) throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Region region = pm.getObjectById(Region.class, key);
			tx.begin();
			region.setRegionName(regionName);
			region.setRegionComments(regionComments);
			tx.commit();
			log.info("Region \"" + regionName + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
