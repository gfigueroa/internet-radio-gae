/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the Genre table.
 * It is kept simple to return only some information to mobile Apps.
 * 
 */

@SuppressWarnings("serial")
public class GenreSimple implements Serializable {
    
	public Long key;
	public String genreEnglishName;
	public String genreChineseName;
    
    /**
     * GenreSimple constructor.
     * @param key
     * 			: Genre key
     * @param genreEnglishName
     * 			: The English name of the genre
     * @param genreChineseName
     * 			: The Chinese name of the genre
     */
    public GenreSimple(Long key, 
    		String genreEnglishName,
    		String genreChineseName) {

    	this.key = key;
    	this.genreEnglishName = genreEnglishName;
    	this.genreChineseName = genreChineseName;
    }
    
    /**
     * Compare this genre with another Genre
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Genre, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof GenreSimple ) ) return false;
        GenreSimple r = (GenreSimple) o;
        return this.key.equals(r.key);
    }
    
}
