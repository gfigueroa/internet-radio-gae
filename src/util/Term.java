/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.io.Serializable;

/**
 * Utility class for storing a terminology in two or
 * more languages.
 */

public class Term  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 662329679797144424L;
	public String englishTerm;
	public String chineseTerm;

	/**
	 * Term constructor
	 * @param englishTerm
	 * 			: the term to be stored in English
	 * @param chineseTerm
	 * 			: the term to be stored in Chinese
	 */
	public Term(String englishTerm, String chineseTerm) {
		this.englishTerm = englishTerm;
		this.chineseTerm = chineseTerm;
	}
}
