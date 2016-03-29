/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

import datastore.User;

/**
 * This class is an exception indicating that the user wasn't
 * validated correctly (either email or password are incorrect)
 * 
 */

@SuppressWarnings("serial")
public class UserValidationException extends Exception {
	
	public UserValidationException(User user, String message) {
		super("User \"" + user.getUserEmail().getEmail() + "\". " + message);
	}
}