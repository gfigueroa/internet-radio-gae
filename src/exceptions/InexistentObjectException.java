/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

/**
 * This class is an exception indicating that the object
 * was not found
 * 
 */

@SuppressWarnings("serial")
public class InexistentObjectException extends Exception {
	
	public InexistentObjectException(Object object, String message) {
		super("Object " + object.getClass().getSimpleName() + ". " + message);
	}
}