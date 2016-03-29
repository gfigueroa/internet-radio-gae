/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * This class is an exception indicating that the JDO object to be
 * deleted is being referenced by another object in the datastore, and
 * thus breaking a referential integrity constraint.
 * 
 */

@SuppressWarnings("serial")
public class ReferentialIntegrityException extends Exception {
	
	public ReferentialIntegrityException(Key key, String message) {
		super("Object key: " + KeyFactory.keyToString(key) + ". " + message);
	}
}
