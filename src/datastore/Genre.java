/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Genre table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class Genre {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private String genreEnglishName;
    
    @Persistent
    private String genreChineseName;

    /**
     * Genre constructor.
	 * @param genreEnglishName
     * 			: genre English name
     * @param genreChineseName
     * 			: genre Chinese name
     * @throws MissingRequiredFieldsException
     */
    public Genre(String genreEnglishName, String genreChineseName) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (genreEnglishName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (genreEnglishName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.genreEnglishName = genreEnglishName;
    	this.genreChineseName = genreChineseName;
    }

    /**
     * Get Genre key.
     * @return genre key
     */
    public Long getKey() {
        return key;
    }

	/**
	 * @return the genreEnglishName
	 */
	public String getGenreEnglishName() {
		return genreEnglishName;
	}

	/**
	 * @return the genreChineseName
	 */
	public String getGenreChineseName() {
		return genreChineseName;
	}

	/**
	 * @param genreEnglishName the genreEnglishName to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setGenreEnglishName(String genreEnglishName) 
			throws MissingRequiredFieldsException {
    	if (genreEnglishName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		this.genreEnglishName = genreEnglishName;
	}

	/**
	 * @param genreChineseName the genreChineseName to set
	 */
	public void setGenreChineseName(String genreChineseName) {
		this.genreChineseName = genreChineseName;
	}

    
}
