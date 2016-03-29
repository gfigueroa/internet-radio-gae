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
 * This class represents the Channel table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Channel implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
    @Persistent
    private String channelName;
    
    @Persistent
    private Integer channelNumber;
    
    @Persistent
    private Date channelCreationDate;
    
    @Persistent
    private Date  channelModificationDate;
    
    @Persistent
    private Integer programVersion;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Program> programs;

    /**
     * Channel constructor.
     * @param channelName
     * 			: channel name
     * @param channelNumber
     * 			: channel number
     * @throws MissingRequiredFieldsException
     */
    public Channel(String channelName, Integer channelNumber) 
		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (channelName == null || channelNumber == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (channelName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

    	this.channelName = channelName;
    	this.channelNumber = channelNumber;
    	
    	this.programs = new ArrayList<Program>();
    	
    	Date now = new Date();
    	this.channelCreationDate = now;
    	this.channelModificationDate = now;
    	
    	this.programVersion = 0; // Initialize the version in 0
    }

    /**
     * Get Channel key.
     * @return channel key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Channel name.
     * @return channel name
     */
    public String getChannelName() {
        return channelName;
    }
    
    /**
     * Get Channel number.
     * @return channel number
     */
    public Integer getChannelNumber() {
        return channelNumber;
    }

    /**
     * Get channel creation date.
     * @return the time this channel was created
     */
    public Date getChannelCreationDate() {
        return channelCreationDate;
    }

    /**
     * Get channel modification date.
     * @return the time this channel was last modified
     */
    public Date getChannelModificationDate() {
        return channelModificationDate;
    }
    
    /**
     * Get Station's program version.
     * The program version increases by 1 each time a modification
     * is made to the programs.
     * @return program version
     */
    public Integer getProgramVersion() {
        return programVersion;
    }
    
    /**
     * Get Program list.
     * @return programs
     */
    public ArrayList<Program> getPrograms() {
        return programs;
    }
    
    /**
     * Compare this channel with another channel
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Channel, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Channel ) ) return false;
        Channel channel = (Channel) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(channel.getKey()));
    }
     
    /**
     * Set Channel name.
     * @param channelName
     * 			: channel name
     * @throws MissingRequiredFieldsException
     */
    public void setChannelName(String channelName)
    		throws MissingRequiredFieldsException {
    	if (channelName == null || channelName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Channel name is missing.");
    	}
    	this.channelName = channelName;
    	this.channelModificationDate = new Date();
    }
    
    /**
     * Set Channel number.
     * @param channelNumber
     * 			: channel number
     * @throws MissingRequiredFieldsException
     */
    public void setChannelNumber(Integer channelNumber)
    		throws MissingRequiredFieldsException {
    	if (channelNumber == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Channel number is missing.");
    	}
    	this.channelNumber = channelNumber;
    	this.channelModificationDate = new Date();
    }
    
    /**
     * Update the Program Version number by 1.
     */
    public void updateProgramVersion() {
    	programVersion++;
    }
    
    /**
     * Add a new program to this channel.
     * @param program
     * 			: new program to be added
     */
    public void addProgram(Program program) {
    	this.programs.add(program);
    }
    
    /**
     * Remove program from the channel.
     * @param program
     * 			: program to be removed
     * @throws InexistentObjectException
     */
    public void removeProgram(Program program) 
    		throws InexistentObjectException {
    	if (!this.programs.remove(program)) {
    		throw new InexistentObjectException
    				(Program.class, "Program not found!");
    	}
    }
}