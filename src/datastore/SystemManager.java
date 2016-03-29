/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the System class.
 * 
 */

public class SystemManager {
	
	private static final Logger log = Logger.getLogger(SystemManager.class.getName());
	
	/**
     * Get System instance from the datastore.
     * @return The only System instance there should be
     */
	public static System getSystem() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        System system = null;
			
        try {
        	List<System> systems = SystemManager.getAllSystems();
        	// Create system if it hasn't been created yet
        	if (systems == null || systems.isEmpty()) {
        		system = new System();
        		pm.makePersistent(system);
        	}
        	else {
    			system = systems.get(0);
    		}
        } 
        finally {
        	pm.close();
        }

        return system;
    }
	
	/**
     * Get all System instances from the datastore.
     * @return All System instances
     * TODO: Make "touching" of systems more efficient
     */
	@SuppressWarnings("unchecked")
	public static List<System> getAllSystems() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(System.class);

        List<System> systems = null;
        try {
        	systems = (List<System>) query.execute();
            // touch all elements
            for (System s : systems)
                s.getSystemTime();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return systems;
    }
	
	/**
     * Get Station List Version from the system.
     * @return station list version
     */
	public static int getStationListVersion() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        int stationListVersion = 0;
			
        try {
        	List<System> systems = SystemManager.getAllSystems();
    		if (systems != null && !systems.isEmpty()) {
    			stationListVersion = systems.get(0).getStationListVersion();
    		}
        } 
        finally {
        	pm.close();
        }

        return stationListVersion;
    }
	
	/**
     * Get Station Type List Version from the system.
     * @return Station type list version
     */
	public static int getStationTypeListVersion() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        int stationTypeListVersion = 0;
			
        try {
        	List<System> systems = SystemManager.getAllSystems();
    		if (systems != null && !systems.isEmpty()) {
    			stationTypeListVersion = systems.get(0).getStationTypeListVersion();
    		}
        } 
        finally {
        	pm.close();
        }

        return stationTypeListVersion;
    }
	
	/**
    * Update Station List Version.
    * Updates the station list version (add 1)
    */
	public static void updateStationListVersion() {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateStationListVersion();
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Station List Version updated to version " +
					system.getStationListVersion() + " in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Station Type List Version.
    * Updates the station type list version (add 1)
    */
	public static void updateStationTypeListVersion() {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateStationTypeListVersion();
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Station Type List Version updated to version " +
					system.getStationTypeListVersion() + " in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Music Library Version.
    * Updates the station type list version (add 1)
    */
	public static void updateMusicLibraryVersion() {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateMusicLibraryVersion();
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Music Library Version updated to version " +
					system.getMusicLibraryVersion() + " in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update System attributes.
    * Update the different System variables in the datastore.
    * @param oldestAppVersionSupported
    * 			: the new oldestAppVersionSupported to be updated
	 * @throws MissingRequiredFieldsException 
    */
	public static void updateSystemAttributes(Integer oldestAppVersionSupported1,
			Integer oldestAppVersionSupported2, Integer oldestAppVersionSupported3) 
			throws MissingRequiredFieldsException {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.setOldestAppVersionSupported1(oldestAppVersionSupported1);
			system.setOldestAppVersionSupported2(oldestAppVersionSupported2);
			system.setOldestAppVersionSupported3(oldestAppVersionSupported3);
			tx.commit();
			log.info("System \"" + system.getKey() + "\": oldestAppVersionSupported updated" +
					" in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}

