/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

/**
 * This class is an exception indicating that there is a formatting
 * error in an input field (emails, urls, phone numbers, etc.)
 * 
 */

@SuppressWarnings("serial")
public class InvalidFieldFormatException extends Exception {
	
	public InvalidFieldFormatException(Object object, String message) {
		super("Object " + object.getClass().getSimpleName() + ". " + message);
	}
}