/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.io.Serializable;

/**
 * Utility class for printing terminology on the web server
 * pages in a specific language.
 * This class will be handled as a session variable.
 * 
 */

public class Printer  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1600191285135648317L;
	private Dictionary.Language language;
	private Dictionary dictionary;
	
	/**
	 * Printer constructor.
	 * @param language
	 * 			: the language used for this printer
	 */
	public Printer(Dictionary.Language language) {
		this.language = language;
		this.dictionary = Dictionary.getInstance();
	}
	
	/**
	 * Get language.
	 * @return this printer's language
	 */
	public Dictionary.Language getLanguage() {
		return language;
	}
	
	/**
	 * Print this English term in the printer's
	 * language.
	 * @param englishTerm
	 * 			: the term in English to be printed
	 * @return the term in the printer's language
	 */
	public String print(String englishTerm) {
		englishTerm=englishTerm.toUpperCase().trim();
		if (dictionary.translate(englishTerm, language).length() > 0) {
			return dictionary.translate(englishTerm, language);
		}
		else {
			return englishTerm.concat("*");
		}
		
	}
	
	public void setLanguage(Dictionary.Language language) {
		this.language=language;
	}
	
	public String getLanguageString() {
		return language.toString();
	}
}
