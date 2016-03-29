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
 * This class represents the SecondaryTrack table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class SecondaryTrack implements Serializable {

	public static enum SecondaryTrackType {
		FILE_UPLOAD, MUSIC_FILE
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private SecondaryTrackType secondaryTrackType;

    @Persistent
    private Key stationAudio;

    @Persistent
    private Long musicFile;
    
    @Persistent
    private Double secondaryTrackStartingTime;
    
    @Persistent
    private Double secondaryTrackDuration;
    
    @Persistent
    private Integer secondaryTrackFadeInSteps;
    
    @Persistent
    private Double secondaryTrackFadeInDuration;
    
    @Persistent
    private Double secondaryTrackFadeInPercentage;
    
    @Persistent
    private Integer secondaryTrackFadeOutSteps;
    
    @Persistent
    private Double secondaryTrackFadeOutDuration;
    
    @Persistent
    private Double secondaryTrackFadeOutPercentage;
    
    @Persistent
    private Double secondaryTrackOffset;

    /**
     * SecondaryTrack constructor.
     * @param secondaryTrackType
     * 			: secondaryTrack type
     * @param stationAudio
     * 			: stationAudio key
     * @param musicFile
     * 			: the ID of the music file
     * @param secondaryTrackStartingTime
     * 			: the starting time of the secondary track
     * @param secondaryTrackDuration
     * 			: secondaryTrack duration
     * @param secondaryTrackFadeInSteps
     * 			: secondaryTrack number of steps in fade-In
     * @param secondaryTrackFadeInDuration
     * 			: secondaryTrack duration of fade-In effect
     * @param secondaryTrackFadeInPercentage
     * 			: secondaryTrack percentage of fade-In effect
     * @param secondaryTrackFadeOutSteps
     * 			: secondaryTrack number of steps in fade-out
     * @param secondaryTrackFadeOutDuration
     * 			: secondaryTrack duration of fade-out effect
     * @param secondaryTrackFadeOutPercentage
     * 			: secondaryTrack percentage of fade-out effect
     * @param secondaryTrackOffset
     * 			: secondaryTrack offset
     * @throws MissingRequiredFieldsException
     */
    public SecondaryTrack(SecondaryTrackType secondaryTrackType,
    		Key stationAudio, Long musicFile,
    		Double secondaryTrackStartingTime,
    		Double secondaryTrackDuration, Integer secondaryTrackFadeInSteps,
    		Double secondaryTrackFadeInDuration, Double secondaryTrackFadeInPercentage,
    		Integer secondaryTrackFadeOutSteps, Double secondaryTrackFadeOutDuration, 
    		Double secondaryTrackFadeOutPercentage, Double secondaryTrackOffset) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (secondaryTrackType == null ||
    			secondaryTrackStartingTime == null ||
    			secondaryTrackDuration == null || secondaryTrackFadeInSteps == null ||
    			secondaryTrackFadeInDuration == null || 
    			secondaryTrackFadeInPercentage == null ||
    			secondaryTrackFadeOutSteps == null ||
    			secondaryTrackFadeOutDuration == null ||
    			secondaryTrackFadeOutPercentage == null ||
    			secondaryTrackOffset == null
    			) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check "required field" constraints per type
    	if ((secondaryTrackType == SecondaryTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(secondaryTrackType == SecondaryTrackType.MUSIC_FILE &&
    			musicFile == null)) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	    	
    	this.secondaryTrackType = secondaryTrackType;
        this.stationAudio = stationAudio;
        this.musicFile = musicFile;
        this.secondaryTrackStartingTime = secondaryTrackStartingTime;
        this.secondaryTrackDuration = secondaryTrackDuration;
        this.secondaryTrackFadeInSteps = secondaryTrackFadeInSteps;
        this.secondaryTrackFadeInDuration = secondaryTrackFadeInDuration;
        this.secondaryTrackFadeInPercentage = secondaryTrackFadeInPercentage;
        this.secondaryTrackFadeOutSteps = secondaryTrackFadeOutSteps;
        this.secondaryTrackFadeOutDuration = secondaryTrackFadeOutDuration;
        this.secondaryTrackFadeOutPercentage = secondaryTrackFadeOutPercentage;
        this.secondaryTrackOffset = secondaryTrackOffset;
    }

    /**
     * Get SecondaryTrack key.
     * @return SecondaryTrack key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get secondaryTrack type.
     * @return SecondaryTrack type
     */
    public SecondaryTrackType getSecondaryTrackType() {
    	return secondaryTrackType;
    }
    
    /**
     * Get the secondaryTrack type's string representation
     * @return The secondaryTrack type as a string
     */
    public String getSecondaryTrackTypeString() {
    	switch (secondaryTrackType) {
    		case FILE_UPLOAD:
    			return "File Upload";
    		case MUSIC_FILE:
    			return "Music File";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the secondaryTrack type from its string representation.
     * @param secondaryTrackTypeString
     * 			: the secondaryTrack type string to convert
     * @return secondaryTrackType
     */
    public static SecondaryTrackType getSecondaryTrackTypeFromString(
    		String secondaryTrackTypeString) {
    	
    	if (secondaryTrackTypeString == null) {
    		return null;
    	}
    	
    	if (secondaryTrackTypeString.equalsIgnoreCase("file_upload") ||
    			secondaryTrackTypeString.equalsIgnoreCase("file upload")) {
    		return SecondaryTrackType.FILE_UPLOAD;
    	}
    	else if (secondaryTrackTypeString.equalsIgnoreCase("music_file") ||
    			secondaryTrackTypeString.equalsIgnoreCase("music file")) {
    		return SecondaryTrackType.MUSIC_FILE;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get stationAudio.
     * @return stationAudio key
     */
    public Key getStationAudio() {
        return stationAudio;
    }
    
    /**
	 * @return the musicFile
	 */
	public Long getMusicFile() {
		return musicFile;
	}

	/**
	 * @return the secondaryTrackStartingTime
	 */
	public Double getSecondaryTrackStartingTime() {
		return secondaryTrackStartingTime;
	}

	/**
	 * @return the secondaryTrackDuration
	 */
	public Double getSecondaryTrackDuration() {
		return secondaryTrackDuration;
	}

	/**
	 * @return the secondaryTrackFadeInSteps
	 */
	public Integer getSecondaryTrackFadeInSteps() {	
		return secondaryTrackFadeInSteps;
	}

	/**
	 * @return the secondaryTrackFadeInDuration
	 */
	public Double getSecondaryTrackFadeInDuration() {
		return secondaryTrackFadeInDuration;
	}

	/**
	 * @return the secondaryTrackFadeInPercentage
	 */
	public Double getSecondaryTrackFadeInPercentage() {
		return secondaryTrackFadeInPercentage;
	}

	/**
	 * @return the secondaryTrackOffset
	 */
	public Double getSecondaryTrackOffset() {
		return secondaryTrackOffset;
	}

	/**
	 * @return the secondaryTrackFadeOutSteps
	 */
	public Integer getSecondaryTrackFadeOutSteps() {
		return secondaryTrackFadeOutSteps;
	}

	/**
	 * @return the secondaryTrackFadeOutDuration
	 */
	public Double getSecondaryTrackFadeOutDuration() {
		return secondaryTrackFadeOutDuration;
	}

	/**
	 * @return the secondaryTrackFadeOutPercentage
	 */
	public Double getSecondaryTrackFadeOutPercentage() {
		return secondaryTrackFadeOutPercentage;
	}
    
    /**
     * Compare this SecondaryTrack with another SecondaryTrack
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this SecondaryTrack, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SecondaryTrack ) ) return false;
        SecondaryTrack secondaryTrack = (SecondaryTrack) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(secondaryTrack.getKey()));
    }
    
    /**
     * Set SecondaryTrack type.
     * @param secondaryTrackType
     * 			: the type of this secondaryTrack
     * @throws MissingRequiredFieldsException
     */
    public void setSecondaryTrackType(SecondaryTrackType secondaryTrackType)
    		throws MissingRequiredFieldsException {
    	if (secondaryTrackType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"SecondaryTrack Type is missing.");
    	}
    	this.secondaryTrackType = secondaryTrackType;
    }
    
    /**
     * Set stationAudio.
     * @param stationAudio
     * 			: the stationAudio key
     * @throws MissingRequiredFieldsException
     */
    public void setStationAudio(
    		Key stationAudio,
    		Long musicFile)
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints per type
    	if ((secondaryTrackType == SecondaryTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(secondaryTrackType == SecondaryTrackType.MUSIC_FILE &&
    			musicFile == null)) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.stationAudio = stationAudio;
    }

	/**
	 * @param musicFile the musicFile to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMusicFile(Long musicFile, 
			Key stationAudio) 
			throws MissingRequiredFieldsException {
    	
		// Check "required field" constraints per type
    	if ((secondaryTrackType == SecondaryTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(secondaryTrackType == SecondaryTrackType.MUSIC_FILE &&
    			musicFile == null)) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		
		this.musicFile = musicFile;
	}
	
	/**
	 * @param secondaryTrackStartingTime 
	 * the secondaryTrackStartingTime to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackStartingTime(
			Double secondaryTrackStartingTime) 
					throws MissingRequiredFieldsException {
		
		if (secondaryTrackStartingTime == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackStartingTime = secondaryTrackStartingTime;
	}

	/**
	 * @param secondaryTrackDuration the secondaryTrackDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackDuration(Double secondaryTrackDuration) 
			throws MissingRequiredFieldsException {
		
		if (secondaryTrackDuration == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackDuration = secondaryTrackDuration;
	}
	
	/**
	 * @param secondaryTrackFadeInSteps the 
	 * secondaryTrackFadeInSteps to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackFadeInSteps(
			Integer secondaryTrackFadeInSteps) 
					throws MissingRequiredFieldsException {
		
		if (secondaryTrackFadeInSteps == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackFadeInSteps = 
				secondaryTrackFadeInSteps;
	}

	/**
	 * @param secondaryTrackFadeInDuration the 
	 * secondaryTrackFadeInDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackFadeInDuration(
			Double secondaryTrackFadeInDuration) 
					throws MissingRequiredFieldsException {
		
		if (secondaryTrackFadeInDuration == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackFadeInDuration = 
				secondaryTrackFadeInDuration;
	}

	/**
	 * @param secondaryTrackFadeInPercentage the 
	 * secondaryTrackFadeInPercentage to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackFadeInPercentage(
			Double secondaryTrackFadeInPercentage) 
					throws MissingRequiredFieldsException {
		
		if (secondaryTrackFadeInPercentage == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackFadeInPercentage = 
				secondaryTrackFadeInPercentage;
	}

	/**
	 * @param secondaryTrackFadeOutSteps the secondaryTrackFadeOutSteps to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackFadeOutSteps(Integer secondaryTrackFadeOutSteps) 
			throws MissingRequiredFieldsException {
		
		if (secondaryTrackFadeOutSteps == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackFadeOutSteps = secondaryTrackFadeOutSteps;
	}

	/**
	 * @param secondaryTrackFadeOutDuration the secondaryTrackFadeOutDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackFadeOutDuration(Double secondaryTrackFadeOutDuration) 
			throws MissingRequiredFieldsException {
		
		if (secondaryTrackFadeOutDuration == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackFadeOutDuration = secondaryTrackFadeOutDuration;
	}

	/**
	 * @param secondaryTrackFadeOutPercentage the secondaryTrackFadeOutPercentage to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackFadeOutPercentage(Double secondaryTrackFadeOutPercentage) 
			throws MissingRequiredFieldsException {
		
		if (secondaryTrackFadeOutPercentage == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackFadeOutPercentage = secondaryTrackFadeOutPercentage;
	}
	
	/**
	 * @param secondaryTrackOffset the secondaryTrackOffset to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setSecondaryTrackOffset(
			Double secondaryTrackOffset) throws MissingRequiredFieldsException {
		
		if (secondaryTrackOffset == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.secondaryTrackOffset = secondaryTrackOffset;
	}
}