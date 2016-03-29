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
 * This class represents the StationAudio table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class StationAudio implements Serializable {
	
	public static enum StationAudioType {
		MUSIC, VOICE
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private StationAudioType stationAudioType;
    
    @Persistent
    private String stationAudioName;

    @Persistent
    private BlobKey stationAudioMultimediaContent;
    
    @Persistent
    private Double stationAudioDuration;
    
    @Persistent
    private String	stationAudioFormat;

	@Persistent
    private Date stationAudioCreationDate;
    
    @Persistent
    private Date stationAudioModificationDate;

    /**
     * StationAudio constructor.
     * @param stationAudioType
     * 			: stationAudio type
     * @param stationAudioName
     * 			: stationAudio name
     * @param stationAudioMultimediaContent
     * 			: stationAudio multimedia content
     * @param stationAudioDuration
     * 			: stationAudio duration
     * @param stationAudioFormat
     * 			: stationAudio format
     * @throws MissingRequiredFieldsException
     */
    public StationAudio(StationAudioType stationAudioType,
    		String stationAudioName, 
    		BlobKey stationAudioMultimediaContent,
    		Double stationAudioDuration,
    		String stationAudioFormat) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationAudioType== null || stationAudioName == null ||
    			stationAudioMultimediaContent == null ||
    			stationAudioDuration == null ||
    			stationAudioFormat == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (stationAudioName.trim().isEmpty() ||
    			stationAudioFormat.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	    	
    	this.stationAudioType = stationAudioType;
    	this.stationAudioName = stationAudioName;
        this.stationAudioMultimediaContent = stationAudioMultimediaContent;
        this.stationAudioDuration = stationAudioDuration;
        this.stationAudioFormat = stationAudioFormat;
        
        Date now = new Date();
        this.stationAudioCreationDate = now;
        this.stationAudioModificationDate = now;
    }

    /**
     * Get StationAudio key.
     * @return StationAudio key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get StationAudio type.
     * @return StationAudio type
     */
    public StationAudioType getStationAudioType() {
        return stationAudioType;
    }
    
    /**
     * Get StationAudio type string
     * @return A string representation of the type
     */
    public String getStationAudioTypeString() {
    	
    	switch (stationAudioType) {
    		case MUSIC:
    			return "Music";
    		case VOICE:
    			return "Voice";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the station audio type from a string representation
     * @param stationAudioTypeString
     * 			: the string representation of the type to return
     * @return StationAudio type
     */
    public static StationAudioType getStationAudioTypeFromString(
    		String stationAudioTypeString) {
    	
    	if (stationAudioTypeString == null) {
    		return null;
    	}
    	
    	if (stationAudioTypeString.equalsIgnoreCase("music")) {
    		return StationAudioType.MUSIC;
    	}
    	else if (stationAudioTypeString.equalsIgnoreCase("voice")) {
    		return StationAudioType.VOICE;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get StationAudio name.
     * @return StationAudio name
     */
    public String getStationAudioName() {
        return stationAudioName;
    }
    
    /**
     * Get StationAudio multimedia content.
     * @return StationAudio multimedia content
     */
    public BlobKey getStationAudioMultimediaContent() {
        return stationAudioMultimediaContent;
    }
    
    /**
	 * @return the stationAudioDuration
	 */
	public Double getStationAudioDuration() {
		return stationAudioDuration;
	}
    
    /**
	 * @return the stationAudioFormat
	 */
	public String getStationAudioFormat() {
		return stationAudioFormat;
	}

	/**
	 * @return the stationAudioCreationDate
	 */
	public Date getStationAudioCreationDate() {
		return stationAudioCreationDate;
	}

	/**
	 * @return the stationAudioModificationDate
	 */
	public Date getStationAudioModificationDate() {
		return stationAudioModificationDate;
	}

	/**
     * Compare this StationAudio with another StationAudio
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this StationAudio, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof StationAudio ) ) return false;
        StationAudio stationAudio = (StationAudio) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(stationAudio.getKey()));
    }
    
    /**
     * Set StationAudio type.
     * @param stationAudioType
     * 			: the type of this stationAudio
     * @throws MissingRequiredFieldsException
     */
    public void setStationAudioType(StationAudioType stationAudioType)
    		throws MissingRequiredFieldsException {
    	if (stationAudioType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"StationAudio Type is missing.");
    	}
    	this.stationAudioType = stationAudioType;
    	this.stationAudioModificationDate = new Date();
    }
    
    /**
     * Set StationAudio name.
     * @param stationAudioName
     * 			: the name of this stationAudio
     * @throws MissingRequiredFieldsException
     */
    public void setStationAudioName(String stationAudioName)
    		throws MissingRequiredFieldsException {
    	if (stationAudioName == null || stationAudioName.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"StationAudio name is missing.");
    	}
    	this.stationAudioName = stationAudioName;
    	this.stationAudioModificationDate = new Date();
    }
    
    /**
     * Set StationAudio Multimedia Content.
     * @param stationAudioMultimediaContent
     * 			: the Multimedia Content of this stationAudio
     * @throws MissingRequiredFieldsException
     */
    public void setStationAudioMultimediaContent(
    		BlobKey stationAudioMultimediaContent)
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationAudioMultimediaContent == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.stationAudioMultimediaContent = stationAudioMultimediaContent;
    	this.stationAudioModificationDate = new Date();
    }
    
	/**
	 * @param stationAudioDuration the stationAudioDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setStationAudioDuration(Double stationAudioDuration) 
			throws MissingRequiredFieldsException {
		
    	// Check "required field" constraints
    	if (stationAudioDuration == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		this.stationAudioDuration = stationAudioDuration;
	}
	
	/**
	 * @param stationAudioFormat the stationAudioFormat to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setStationAudioFormat(String stationAudioFormat) 
			throws MissingRequiredFieldsException {
		
		// Check "required field" constraints
    	if (stationAudioFormat == null || stationAudioFormat.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		this.stationAudioFormat = stationAudioFormat;
	}
}