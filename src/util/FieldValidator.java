/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that validates field values.
 * 
 */

public class FieldValidator {

	/**
	 * Validates if email address is correctly formatted.
	 * 
	 * @param emailAddress
	 *            : email address to be validated
	 * @return true if email address is valid, false otherwise
	 */
	public static boolean isValidEmailAddress(String emailAddress) {
		
		// Accept empty email addresses
		if (emailAddress.isEmpty()) {
			return true;
		}
		
		String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = emailAddress;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

	/**
	 * Phone Number formats: (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn ^\\(? :
	 * May start with an option "(" . (\\d{3}): Followed by 3 digits. \\)? :
	 * May have an optional ")" [- ]? : May have an optional "-" after the
	 * first 3 digits or after optional ) character. (\\d{3}) : Followed by
	 * 3 digits. [- ]? : May have another optional "-" after numeric digits.
	 * (\\d{4})$ : ends with four digits.
	 * 
	 * Examples: Matches following phone numbers: (123)456-7890,
	 * 123-456-7890, 1234567890, (123)-456-7890
	 * 
	 * @param phoneNumber
	 * 			: phone number to be validated
	 * @return true if phone number is valid, false otherwise
	 */
	public static boolean isValidPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		//String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		String expression = "^[0-9]*$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Validates if password fulfills the required formatting
	 * 
	 * @param password
	 * 			: password to be validated
	 * @return true if password is valid, false otherwise
	 */
	public static boolean isValidPassword(String password) {
		boolean isValid = false;
		final int minChars = 6;
		final int maxChars = 14;
		
		if (password.length() >= minChars && password.length() <= maxChars) {
			isValid = true;
		}
		else {
			isValid = false;
		}

		return isValid;
	}

}
