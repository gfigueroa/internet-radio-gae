/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
//import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

/**
 * This class is used to manage GAE Datastore operations that are general to all
 * datastore objects.
 * 
 */

public class DatastoreManager {

	//private static final Logger log = Logger.getLogger(DatastoreManager.class.getName());

	/**
	 * Checks if the given entity already exists in the datastore.
	 * 
	 * @param entityClass
	 *            : the entity class to be checked
	 * @param key
	 *            : the key of the entity class to checked
	 * @return true if the entity already exists in the datastore, false otherwise
	 */
	public static boolean entityExists(Class<?> entityClass, Key key) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(entityClass);
			query.setFilter("key == keyParameter");
			query.declareParameters(Key.class.getName() + " keyParameter");
			List<?> keys = (List<?>) query.execute(key);
			//log.info("Query: " + query.toString());
			return keys.size() > 0;
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			if (pm != null) {
				pm.close();
			}
		}
	}
	
	/**
	 * Checks if the given entity already exists in the datastore given
	 * a specific unique field name and field value.
	 * 
	 * @param entityClass
	 *            	: the entity class to be checked
	 * @param fieldType
	 * 				: the type of the field to check for uniqueness
	 * @param fieldName
	 * 				: the name of the field to check for uniqueness
	 * @param fieldValue
	 * 				: the value of the field to check for uniqueness
	 * @return true if the entity already exists in the datastore, false otherwise
	 */
	public static boolean entityExists(Class<?> entityClass, 
			String fieldType, String fieldName, String fieldValue) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(entityClass);
			query.setFilter(fieldName + " == fieldValue");
			query.declareParameters(fieldType + " fieldValue");
			List<?> records = (List<?>) query.execute(fieldValue);
			//log.info("Query: " + query.toString());
			return records.size() > 0;
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			if (pm != null) {
				pm.close();
			}
		}
	}

}