/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Slide table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Slide implements Serializable {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key stationImage;
    
    @Persistent
    private Double slideStartingTime;

    /**
     * Slide constructor.
     * @param stationImage
     * 			: stationImage key
     * @param slideStartingTime
     * 			: the starting time of the secondary track
     * @throws MissingRequiredFieldsException
     */
    public Slide(Key stationImage,
    		Double slideStartingTime) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationImage == null ||
    			slideStartingTime == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

        this.stationImage = stationImage;
        this.slideStartingTime = slideStartingTime;
    }

    /**
     * Get Slide key.
     * @return Slide key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get stationImage.
     * @return stationImage key
     */
    public Key getStationImage() {
        return stationImage;
    }

	/**
	 * @return the slideStartingTime
	 */
	public Double getSlideStartingTime() {
		return slideStartingTime;
	}
    
    /**
     * Compare this Slide with another Slide
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this Slide, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Slide ) ) return false;
        Slide slide = (Slide) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(slide.getKey()));
    }
    
    /**
     * Set stationImage
     * @param stationImage
     * 			: the stationImage key of this slide
     * @throws MissingRequiredFieldsException
     */
    public void setStationImage(
    		Key stationImage)
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationImage == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.stationImage = stationImage;
    }

	/**
	 * @param slideStartingTime 
	 * the slideStartingTime to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSlideStartingTime(
			Double slideStartingTime) 
					throws MissingRequiredFieldsException {
		
		if (slideStartingTime == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.slideStartingTime = slideStartingTime;
	}
}