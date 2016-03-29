/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

/**
 * This class is an exception indicating that the JDO object intended
 * to be stored in the datastore already exists and cannot be created.
 * 
 */

@SuppressWarnings("serial")
public class ObjectExistsInDatastoreException extends Exception {
	
	public ObjectExistsInDatastoreException(Object object, String message) {
		super("Object " + object.getClass().getSimpleName() + ". " + message);
	}
}
