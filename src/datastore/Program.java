/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Program table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Program implements Serializable {
	
	public static enum Status {
		INACTIVE, ACTIVE, EXPIRED
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String programName;
    
    @Persistent
    private String programDescription;
    
    @Persistent
    private String programBanner;
    
    @Persistent
    private Integer programSequenceNumber;
    
    @Persistent
    private Double programTotalDurationTime;
    
    @Persistent
    private Double programOverlapDuration;

	@Persistent
    private Date programStartingDate;
    
    @Persistent
    private Date programEndingDate;
    
    @Persistent
    private Date programCreationDate;
    
    @Persistent
    private Date programModificationDate;

    @Persistent(dependent = "true", defaultFetchGroup = "true")
    private MainTrack mainTrack;

    @Persistent//(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<SecondaryTrack> secondaryTracks;
    
    @Persistent//(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<Slide> slides;

    /**
     * Program constructor.
     * @param programName
     * 			: program name
     * @param programDescription
     * 			: program description
     * @param programBanner
     * 			: program banner
     * @param programSequenceNumber
     * 			: program sequence number
     * @param programTotalDurationTime
     * 			: program total duration time in secs
     * @param programStartingDate
     * 			: the date this program will start to be available
     * @param programEndingDate
     * 			: the date this program will finish
     * @param mainTrack
     * 			: the Main Track of the program
     * @param secondaryTracks
     * 			: the Secondary Tracks of the program
     * @param slides
     * 			: the Slides of the program
     * @throws MissingRequiredFieldsException
     */
    public Program(String programName, String programDescription,
    		String programBanner, Integer programSequenceNumber,
    		Double programTotalDurationTime,
    		Double programOverlapDuration,
    		Date programStartingDate, Date programEndingDate,
    		MainTrack mainTrack, ArrayList<SecondaryTrack> secondaryTracks,
    		ArrayList<Slide> slides) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (programName == null || programSequenceNumber == null ||
    			programTotalDurationTime == null ||
    			programOverlapDuration == null ||
    			programStartingDate == null || programEndingDate == null ||
    			mainTrack == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (programName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

    	this.programName = programName;
        this.programDescription = programDescription;
        this.programBanner = programBanner;
        this.programSequenceNumber = programSequenceNumber;
        this.programTotalDurationTime = programTotalDurationTime;
        this.programOverlapDuration = programOverlapDuration;
        this.programStartingDate = programStartingDate;
        this.programEndingDate = programEndingDate;
        
        // Assign main track, secondary tracks and slides
        this.mainTrack = mainTrack;
        this.secondaryTracks = secondaryTracks;
        this.slides = slides;
        
        Date now = new Date();
        this.programCreationDate = now;
        this.programModificationDate = now;
    }

    /**
     * Get Program key.
     * @return Program key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Program Name
     * @return Program Name
     */
    public String getProgramName() {
        return programName;
    }
    
    /**
     * Get Program description.
     * @return Program description
     */
    public String getProgramDescription() {
    	return programDescription;
    }
    
    /**
     * Get Program Banner.
     * @return Program Banner
     */
    public String getProgramBanner() {
    	return programBanner;
    }
    
    /**
     * Get Program sequence number.
     * @return Program sequence number
     */
    public Integer getProgramSequenceNumber() {
    	return programSequenceNumber;
    }
    
    /**
     * Get Program total duration time
     * @return Program total duration time
     */
    public Double getProgramTotalDurationTime() {
    	return programTotalDurationTime;
    }
    
    /**
	 * @return the programOverlapDuration
	 */
	public Double getProgramOverlapDuration() {
		if (programOverlapDuration == null) {
			return 0.0;
		}
		return programOverlapDuration;
	}
    
    /**
     * Get the date when this program will be available
     * @return The date when this program will be available
     */
    public Date getProgramStartingDate() {
        return programStartingDate;
    }
    
    /**
     * Get the date when this program will be stop being available
     * @return The date when this program will expire
     */
    public Date getProgramEndingDate() {
        return programEndingDate;
    }
    
    /**
     * Get the date this program was created
     * @return The date this program was created
     */
    public Date getProgramCreationDate() {
        return programCreationDate;
    }
    
    /**
     * Get the date this program was last modified
     * @return The date this program was last modified
     */
    public Date getProgramModificationDate() {
        return programModificationDate;
    }
    
    /**
     * Get the current status of this program
     * @return The current status of this program
     */
    public Status getCurrentStatus() {
        // INACTIVE, ACTIVE, EXPIRED
    	
    	Date now = new Date();
    	if (now.compareTo(programStartingDate) < 0) {
    		return Status.INACTIVE;
    	}
    	else {
    		if (now.compareTo(programEndingDate) > 0) {
    			return Status.EXPIRED;
    		}
    		else {
    			return Status.ACTIVE;
    		}
    	}
    }
    
    /**
     * Get Main Track
     * @return mainTrack
     */
    public MainTrack getMainTrack() {
    	return mainTrack;
    }
    
    /**
     * Get Secondary Tracks
     * @return secondaryTracks
     */
    public ArrayList<SecondaryTrack> getSecondaryTracks() {
    	return secondaryTracks;
    }
    
    /**
     * Get Slides
     * @return slides
     */
    public ArrayList<Slide> getSlides() {
    	return slides;
    }
    
    /**
     * Compare this Program with another Program
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this Program, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Program ) ) return false;
        Program program = (Program) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(program.getKey()));
    }
    
    /**
     * Set Program Name.
     * @param programName
     * 			: the Name of this program
     * @throws MissingRequiredFieldsException
     */
    public void setProgramName(String programName)
    		throws MissingRequiredFieldsException {
    	if (programName == null || programName.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Program title is missing.");
    	}
    	this.programName = programName;
    	this.programModificationDate = new Date();
    }
    
    /**
     * Set Program Description.
     * @param programDescription
     * 			: the Description of this program
     * @throws MissingRequiredFieldsException
     */
    public void setProgramDescription(String programDescription) {
    	this.programDescription = programDescription;
    	this.programModificationDate = new Date();
    }
    
    /**
     * Set Program Banner.
     * @param programBanner
     * 			: the Banner of this program
     * @throws MissingRequiredFieldsException
     */
    public void setProgramBanner(String programBanner) {
    	this.programBanner = programBanner;
    	this.programModificationDate = new Date();
    }
    
    /**
     * Set Program Sequence Number.
     * @param programSequenceNumber
     * 			: the Program Sequence Number
     * @throws MissingRequiredFieldsException
     */
    public void setProgramSequenceNumber(Integer programSequenceNumber) 
    		throws MissingRequiredFieldsException {
    	if (programSequenceNumber == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Program sequence number is missing.");
    	}
    	this.programSequenceNumber = programSequenceNumber;
    	this.programModificationDate = new Date();
    }

    /**
     * Set Program total duration time
     * @param programTotalDurationTime
     * 			: the program total duration time
     * @throws MissingRequiredFieldsException 
     */
    public void setProgramTotalDurationTime(Double programTotalDurationTime) 
    		throws MissingRequiredFieldsException {
    	if (programTotalDurationTime == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Program Total Duration Time is missing.");
    	}
    	this.programTotalDurationTime = programTotalDurationTime;
    	this.programModificationDate = new Date();
    }
    
	/**
	 * @param programOverlapDuration the programOverlapDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setProgramOverlapDuration(Double programOverlapDuration) 
			throws MissingRequiredFieldsException {
		if (programOverlapDuration == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Program Overlap Duration is missing.");
    	}
		this.programOverlapDuration = programOverlapDuration;
		this.programModificationDate = new Date();
	}
    
    /**
     * Set Program Starting Date.
     * @param programStartingDate
     * 			: the date this program will be available
     * @throws MissingRequiredFieldsException
     */
    public void setProgramStartingDate(Date programStartingDate)
    		throws MissingRequiredFieldsException {
    	if (programStartingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Program starting date is missing.");
    	}
    	this.programStartingDate = programStartingDate;
    	this.programModificationDate = new Date();
    }
    
    /**
     * Set Program Ending Date.
     * @param programEndingDate
     * 			: the date this program will stop (expire)
     * @throws MissingRequiredFieldsException
     */
    public void setProgramEndingDate(Date programEndingDate)
    		throws MissingRequiredFieldsException {
    	if (programEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Program ending date is missing.");
    	}
    	this.programEndingDate = programEndingDate;
    	this.programModificationDate = new Date();
    }
    
    /**
     * Set Main Track.
     * @param mainTrack
     * 			: the main track for this program
     */
    public void setMainTrack(MainTrack mainTrack) {
    	this.mainTrack = mainTrack;
    	this.programModificationDate = new Date();
    }
    
    /**
     * Add Secondary Track.
     * @param secondaryTrack
     * 			: the secondary track to add
     */
    public void addSecondaryTrack(SecondaryTrack secondaryTrack) {
    	this.secondaryTracks.add(secondaryTrack);
    	this.programModificationDate = new Date();
    }
    
    /**
     * Add Secondary Tracks.
     * @param secondaryTracks
     * 			: the secondary tracks to add
     */
    public void addSecondaryTracks(ArrayList<SecondaryTrack> secondaryTracks) {
    	this.secondaryTracks.addAll(secondaryTracks);
    	this.programModificationDate = new Date();
    }
    
    /**
     * Remove a secondary track.
     * @param secondaryTrack
     * 			: Secondary track to be removed
     * @throws InexistentObjectException
     */
    public void removeSecondaryTrack(SecondaryTrack secondaryTrack) 
    		throws InexistentObjectException {
    	if (!secondaryTracks.remove(secondaryTrack)) {
    		throw new InexistentObjectException(
    				SecondaryTrack.class, "Secondary Track not found!");
    	}
    	this.programModificationDate = new Date();
    }
    
    /**
     * Remove all secondary tracks
     */
    public void removeAllSecondaryTracks() {
    	secondaryTracks.clear();
    	this.programModificationDate = new Date();
    }
    
    /**
     * Add slide.
     * @param slide
     * 			: the slide to add
     */
    public void addSlide(Slide slide) {
    	this.slides.add(slide);
    	this.programModificationDate = new Date();
    }
    
    /**
     * Add slides.
     * @param slides
     * 			: the slides to add
     */
    public void addSlides(ArrayList<Slide> slides) {
    	this.slides.addAll(slides);
    	this.programModificationDate = new Date();
    }
    
    /**
     * Remove a slide.
     * @param slide
     * 			: Slideto be removed
     * @throws InexistentObjectException
     */
    public void removeSlide(Slide slide) 
    		throws InexistentObjectException {
    	if (!slides.remove(slide)) {
    		throw new InexistentObjectException(
    				Slide.class, "Slide not found!");
    	}
    	this.programModificationDate = new Date();
    }
    
    /**
     * Remove all slides in the program
     */
    public void removeAllSlides() {
    	this.slides.clear();
    	this.programModificationDate = new Date();
    }
}