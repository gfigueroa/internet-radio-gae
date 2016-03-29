/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the StationImage table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class StationImage implements Serializable {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private String stationImageName;

    @Persistent
    private BlobKey stationImageMultimediaContent;
    
    @Persistent
    private String stationImageFormat;
    
    @Persistent
    private Date stationImageCreationDate;
    
    @Persistent
    private Date stationImageModificationDate;

    /**
     * StationImage constructor.
     * @param stationImageName
     * 			: stationImage name
     * @param stationImageMultimediaContent
     * 			: stationImage multimedia content
     * @param stationImageFormat
     * 			: stationImage format
     * @throws MissingRequiredFieldsException
     */
    public StationImage(String stationImageName, 
    		BlobKey stationImageMultimediaContent,
    		String stationImageFormat) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationImageName == null ||
    			stationImageMultimediaContent == null ||
    			stationImageFormat == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (stationImageName.trim().isEmpty() ||
    			stationImageFormat.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	    	
    	this.stationImageName = stationImageName;
        this.stationImageMultimediaContent = stationImageMultimediaContent;
        this.stationImageFormat = stationImageFormat;
        
        Date now = new Date();
        this.stationImageCreationDate = now;
        this.stationImageModificationDate = now;
    }

    /**
     * Get StationImage key.
     * @return StationImage key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get StationImage name.
     * @return StationImage name
     */
    public String getStationImageName() {
        return stationImageName;
    }
    
    /**
     * Get StationImage multimedia content.
     * @return StationImage multimedia content
     */
    public BlobKey getStationImageMultimediaContent() {
        return stationImageMultimediaContent;
    }
    
    /**
	 * @return the stationImageFormat
	 */
	public String getStationImageFormat() {
		return stationImageFormat;
	}

	/**
	 * @return the stationImageCreationDate
	 */
	public Date getStationImageCreationDate() {
		return stationImageCreationDate;
	}

	/**
	 * @return the stationImageModificationDate
	 */
	public Date getStationImageModificationDate() {
		return stationImageModificationDate;
	}

	/**
     * Compare this StationImage with another StationImage
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this StationImage, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof StationImage ) ) return false;
        StationImage stationImage = (StationImage) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(stationImage.getKey()));
    }
    
    /**
     * Set StationImage name.
     * @param stationImageName
     * 			: the name of this stationImage
     * @throws MissingRequiredFieldsException
     */
    public void setStationImageName(String stationImageName)
    		throws MissingRequiredFieldsException {
    	if (stationImageName == null || stationImageName.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"StationImage name is missing.");
    	}
    	this.stationImageName = stationImageName;
    	this.stationImageModificationDate = new Date();
    }
    
    /**
     * Set StationImage Multimedia Content.
     * @param stationImageMultimediaContent
     * 			: the Multimedia Content of this stationImage
     * @throws MissingRequiredFieldsException
     */
    public void setStationImageMultimediaContent(
    		BlobKey stationImageMultimediaContent)
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationImageMultimediaContent == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.stationImageMultimediaContent = stationImageMultimediaContent;
    	this.stationImageModificationDate = new Date();
    }
    
	/**
	 * @param stationImageFormat the stationImageFormat to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setStationImageFormat(String stationImageFormat) 
			throws MissingRequiredFieldsException {
		
		// Check "required field" constraints
    	if (stationImageFormat == null ||
    			stationImageFormat.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		this.stationImageFormat = stationImageFormat;
	}
}