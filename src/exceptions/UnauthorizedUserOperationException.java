/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package exceptions;

import datastore.User;

/**
 * This class is an exception indicating that an operation was
 * attempted to be executed on/by an unauthorized user
 * 
 */

@SuppressWarnings("serial")
public class UnauthorizedUserOperationException extends Exception {
	
	public UnauthorizedUserOperationException(User user, String message) {
		super("User \"" + user.getUserEmail().getEmail() + "\", Type " + user.getUserTypeString() + ". " + message);
	}
}
