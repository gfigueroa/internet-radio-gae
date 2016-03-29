/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

/**
 * This class is an exception indicating that the JDO object intended
 * to be created is missing one or more required fields.
 * 
 */

@SuppressWarnings("serial")
public class MissingRequiredFieldsException extends Exception {
	
	public MissingRequiredFieldsException(Object object, String message) {
		super("Object " + object.getClass().getSimpleName() + ". " + message);
	}
}