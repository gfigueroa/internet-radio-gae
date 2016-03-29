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
 * This class represents the MainTrack table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class MainTrack implements Serializable {

	public static enum MainTrackType {
		FILE_UPLOAD, MUSIC_FILE, PLAYLIST
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private MainTrackType mainTrackType;

    @Persistent
    private Key stationAudio;

    @Persistent
    private Long musicFile;

    @Persistent
    private Key playlist;
    
    @Persistent
    private Double mainTrackDuration;
    
    @Persistent
    private Integer mainTrackFadeInSteps;
    
    @Persistent
    private Double mainTrackFadeInDuration;
    
    @Persistent
    private Double mainTrackFadeInPercentage;
    
    @Persistent
    private Integer mainTrackFadeOutSteps;
    
    @Persistent
    private Double mainTrackFadeOutDuration;
    
    @Persistent
    private Double mainTrackFadeOutPercentage;

    /**
     * MainTrack constructor.
     * @param mainTrack
     */
    public MainTrack(MainTrack mainTrack) 
    		throws MissingRequiredFieldsException {
    	    	
    	this.mainTrackType = mainTrack.mainTrackType;
        this.stationAudio = mainTrack.stationAudio;
        this.musicFile = mainTrack.musicFile;
        this.playlist = mainTrack.playlist;
        this.mainTrackDuration = mainTrack.mainTrackDuration;
        this.mainTrackFadeInSteps = mainTrack.mainTrackFadeInSteps;
        this.mainTrackFadeInDuration = mainTrack.mainTrackFadeInDuration;
        this.mainTrackFadeInPercentage = mainTrack.mainTrackFadeInPercentage;
        this.mainTrackFadeOutSteps = mainTrack.mainTrackFadeOutSteps;
        this.mainTrackFadeOutDuration = mainTrack.mainTrackFadeOutDuration;
        this.mainTrackFadeOutPercentage = mainTrack.mainTrackFadeOutPercentage;
    }
    
    /**
     * MainTrack constructor.
     * @param mainTrackType
     * 			: mainTrack type
     * @param stationAudio
     * 			: stationAudio key
     * @param musicFile
     * 			: the ID of the music file
     * @param playlist
     * 			: the ID of the playlist
     * @param mainTrackDuration
     * 			: mainTrack duration
     * @param mainTrackFadeInSteps
     * 			: mainTrack number of steps in fade-In
     * @param mainTrackFadeInDuration
     * 			: mainTrack duration of fade-In effect
     * @param mainTrackFadeInPercentage
     * 			: mainTrack percentage of fade-In effect
     * @param mainTrackFadeOutSteps
     * 			: mainTrack number of steps in fade-out
     * @param mainTrackFadeOutDuration
     * 			: mainTrack duration of fade-out effect
     * @param mainTrackFadeOutPercentage
     * 			: mainTrack percentage of fade-out effect
     * @throws MissingRequiredFieldsException
     */
    public MainTrack(MainTrackType mainTrackType, 
    		Key stationAudio, Long musicFile,
    		Key playlist, Double mainTrackDuration, 
    		Integer mainTrackFadeInSteps, Double mainTrackFadeInDuration, 
    		Double mainTrackFadeInPercentage, Integer mainTrackFadeOutSteps,
    		Double mainTrackFadeOutDuration, Double mainTrackFadeOutPercentage
    		) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (mainTrackType == null ||
    			mainTrackDuration == null || mainTrackFadeOutSteps == null ||
    			mainTrackFadeOutDuration == null || mainTrackFadeOutPercentage == null ||
    			mainTrackFadeInSteps == null ||
    			mainTrackFadeInDuration == null || mainTrackFadeInPercentage == null
    			) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check "required field" constraints per type
    	if ((mainTrackType == MainTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(mainTrackType == MainTrackType.MUSIC_FILE &&
    			musicFile == null) ||
    			(mainTrackType == MainTrackType.PLAYLIST &&
    			playlist == null)) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	    	
    	this.mainTrackType = mainTrackType;
        this.stationAudio = stationAudio;
        this.musicFile = musicFile;
        this.playlist = playlist;
        this.mainTrackDuration = mainTrackDuration;
        this.mainTrackFadeInSteps = mainTrackFadeInSteps;
        this.mainTrackFadeInDuration = mainTrackFadeInDuration;
        this.mainTrackFadeInPercentage = mainTrackFadeInPercentage;
        this.mainTrackFadeOutSteps = mainTrackFadeOutSteps;
        this.mainTrackFadeOutDuration = mainTrackFadeOutDuration;
        this.mainTrackFadeOutPercentage = mainTrackFadeOutPercentage;
    }

    /**
     * Get MainTrack key.
     * @return MainTrack key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get mainTrack type.
     * @return MainTrack type
     */
    public MainTrackType getMainTrackType() {
    	return mainTrackType;
    }
    
    /**
     * Get the mainTrack type's string representation
     * @return The mainTrack type as a string
     */
    public String getMainTrackTypeString() {
    	switch (mainTrackType) {
    		case FILE_UPLOAD:
    			return "File Upload";
    		case MUSIC_FILE:
    			return "Music File";
    		case PLAYLIST:
    			return "Playlist";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the mainTrack type from its string representation.
     * @param mainTrackTypeString
     * 			: the mainTrack type string to convert
     * @return mainTrackType
     */
    public static MainTrackType getMainTrackTypeFromString(
    		String mainTrackTypeString) {
    	
    	if (mainTrackTypeString == null) {
    		return null;
    	}
    	
    	if (mainTrackTypeString.equalsIgnoreCase("file_upload") ||
    			mainTrackTypeString.equalsIgnoreCase("file upload")) {
    		return MainTrackType.FILE_UPLOAD;
    	}
    	else if (mainTrackTypeString.equalsIgnoreCase("music_file") ||
    			mainTrackTypeString.equalsIgnoreCase("music file")) {
    		return MainTrackType.MUSIC_FILE;
    	}
    	else if (mainTrackTypeString.equalsIgnoreCase("playlist")) {
    		return MainTrackType.PLAYLIST;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get stationAudio content.
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
	 * @return the playlist
	 */
	public Key getPlaylist() {
		return playlist;
	}

	/**
	 * @return the mainTrackDuration
	 */
	public Double getMainTrackDuration() {
		return mainTrackDuration;
	}

    /**
	 * @return the mainTrackFadeInSteps
	 */
	public Integer getMainTrackFadeInSteps() {
		if (mainTrackFadeInSteps == null) {
			return 0;
		}
		return mainTrackFadeInSteps;
	}

	/**
	 * @return the mainTrackFadeInDuration
	 */
	public Double getMainTrackFadeInDuration() {
		if (mainTrackFadeInDuration == null) {
			return 0.0;
		}
		return mainTrackFadeInDuration;
	}

	/**
	 * @return the mainTrackFadeInPercentage
	 */
	public Double getMainTrackFadeInPercentage() {
		if (mainTrackFadeInPercentage == null) {
			return 0.0;
		}
		return mainTrackFadeInPercentage;
	}
	
	/**
	 * @return the mainTrackFadeOutSteps
	 */
	public Integer getMainTrackFadeOutSteps() {
		return mainTrackFadeOutSteps;
	}

	/**
	 * @return the mainTrackFadeOutDuration
	 */
	public Double getMainTrackFadeOutDuration() {
		return mainTrackFadeOutDuration;
	}

	/**
	 * @return the mainTrackFadeOutPercentage
	 */
	public Double getMainTrackFadeOutPercentage() {
		return mainTrackFadeOutPercentage;
	}

	/**
     * Compare this MainTrack with another MainTrack
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this MainTrack, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof MainTrack ) ) return false;
        MainTrack mainTrack = (MainTrack) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(mainTrack.getKey()));
    }
    
    /**
     * Set MainTrack type.
     * @param mainTrackType
     * 			: the type of this mainTrack
     * @throws MissingRequiredFieldsException
     */
    public void setMainTrackType(MainTrackType mainTrackType)
    		throws MissingRequiredFieldsException {
    	if (mainTrackType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MainTrack Type is missing.");
    	}
    	this.mainTrackType = mainTrackType;
    }
    
    /**
     * Set stationAudio.
     * @param stationAudio
     * 			: the stationAudio key
     * @throws MissingRequiredFieldsException
     */
    public void setStationAudio(Key stationAudio,
    		Long musicFile, Key playlist)
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints per type
    	if ((mainTrackType == MainTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(mainTrackType == MainTrackType.MUSIC_FILE &&
    			musicFile == null) ||
    			(mainTrackType == MainTrackType.PLAYLIST &&
    			playlist == null)) {
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
			Key stationAudio, Key playlist) 
			throws MissingRequiredFieldsException {
    	
		// Check "required field" constraints per type
    	if ((mainTrackType == MainTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(mainTrackType == MainTrackType.MUSIC_FILE &&
    			musicFile == null) ||
    			(mainTrackType == MainTrackType.PLAYLIST &&
    			playlist == null)) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		
		this.musicFile = musicFile;
	}

	/**
	 * @param playlist the playlist to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setPlaylist(Key playlist, 
			Key stationAudio, 
			Long musicFile) 
			throws MissingRequiredFieldsException {
		
    	// Check "required field" constraints per type
    	if ((mainTrackType == MainTrackType.FILE_UPLOAD && 
    			stationAudio == null) ||
    			(mainTrackType == MainTrackType.MUSIC_FILE &&
    			musicFile == null) ||
    			(mainTrackType == MainTrackType.PLAYLIST &&
    			playlist == null)) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
		
		this.playlist = playlist;
	}

	/**
	 * @param mainTrackDuration the mainTrackDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackDuration(Double mainTrackDuration) 
			throws MissingRequiredFieldsException {
		
		if (mainTrackDuration == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackDuration = mainTrackDuration;
	}

	/**
	 * @param mainTrackFadeInSteps the mainTrackFadeInSteps to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackFadeInSteps(Integer mainTrackFadeInSteps) 
			throws MissingRequiredFieldsException {
		if (mainTrackFadeInSteps == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackFadeInSteps = mainTrackFadeInSteps;
	}

	/**
	 * @param mainTrackFadeInDuration the mainTrackFadeInDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackFadeInDuration(Double mainTrackFadeInDuration) 
			throws MissingRequiredFieldsException {
		if (mainTrackFadeInDuration == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackFadeInDuration = mainTrackFadeInDuration;
	}

	/**
	 * @param mainTrackFadeInPercentage the mainTrackFadeInPercentage to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackFadeInPercentage(Double mainTrackFadeInPercentage) 
			throws MissingRequiredFieldsException {
		if (mainTrackFadeInPercentage == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackFadeInPercentage = mainTrackFadeInPercentage;
	}
	
	/**
	 * @param mainTrackFadeOutSteps the mainTrackFadeOutSteps to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackFadeOutSteps(Integer mainTrackFadeOutSteps) 
			throws MissingRequiredFieldsException {
		
		if (mainTrackFadeOutSteps == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackFadeOutSteps = mainTrackFadeOutSteps;
	}

	/**
	 * @param mainTrackFadeOutDuration the mainTrackFadeOutDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackFadeOutDuration(Double mainTrackFadeOutDuration) 
			throws MissingRequiredFieldsException {
		
		if (this.mainTrackFadeOutDuration == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackFadeOutDuration = mainTrackFadeOutDuration;
	}

	/**
	 * @param mainTrackFadeOutPercentage the mainTrackFadeOutPercentage to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMainTrackFadeOutPercentage(Double mainTrackFadeOutPercentage) 
			throws MissingRequiredFieldsException {
		
		if (mainTrackFadeOutPercentage == null) {
			throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
		}
		
		this.mainTrackFadeOutPercentage = mainTrackFadeOutPercentage;
	}
}