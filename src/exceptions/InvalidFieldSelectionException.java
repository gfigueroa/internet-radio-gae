/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

/**
 * This class is an exception indicating that there is an error
 * in the selection combination of some fields.
 * 
 */

@SuppressWarnings("serial")
public class InvalidFieldSelectionException extends Exception {
	
	public InvalidFieldSelectionException(Object object, String message) {
		super("Object " + object.getClass().getSimpleName() + ". " + message);
	}
}