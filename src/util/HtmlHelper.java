/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

/**
 * Utility class that helps escape html
 * 
 */

public class HtmlHelper {
	
	public static String escapeQuotes(String original) {
		return original.replaceAll("\"", "&quot;");
	}
}
