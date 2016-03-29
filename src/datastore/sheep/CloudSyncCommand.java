/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package datastore.sheep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import util.DateManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the CloudSyncCommand table.
 * It is managed as a JDO to be stored in and retrieved 
 * from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class CloudSyncCommand implements Serializable {
	
	public static int MESSAGE_DURATION = 5; // In minutes
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key cloudSyncCommandMaster;
    
    @Persistent
    private ArrayList<Key> cloudSyncCommandGroups;
    
    @Persistent
    private String cloudSyncCommandMessage;
    
    @Persistent
    private Date cloudSyncCommandCreationDate;
    
    @Persistent
    private Date cloudSyncCommandEndingDate;

    /**
     * CloudSyncCommand constructor.
     * @param cloudSyncCommandMaster
     * 			: cloudSyncCommand master
     * @param cloudSyncCommandMessage
     * 			: cloudSyncCommand message
     * @throws MissingRequiredFieldsException
     */
    public CloudSyncCommand(Key cloudSyncCommandMaster,
    		ArrayList<Key> cloudSyncCommandGroups,
    		String cloudSyncCommandMessage) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (cloudSyncCommandMaster == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	    	
    	this.cloudSyncCommandMaster = cloudSyncCommandMaster;
    	this.cloudSyncCommandGroups = cloudSyncCommandGroups;
    	this.cloudSyncCommandMessage = cloudSyncCommandMessage;
    	
    	this.cloudSyncCommandCreationDate = new Date();
    	this.cloudSyncCommandEndingDate = 
    			DateManager.addMinutesToDate(cloudSyncCommandCreationDate, 
    					MESSAGE_DURATION);
    }

    /**
     * Compare this CloudSyncCommand with another CloudSyncCommand
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this CloudSyncCommand, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof CloudSyncCommand ) ) return false;
        CloudSyncCommand cloudSyncCommand = (CloudSyncCommand) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(cloudSyncCommand.getKey()));
    }

    /**
     * Get CloudSyncCommand key.
     * @return CloudSyncCommand key
     */
    public Key getKey() {
        return key;
    }
    
	/**
	 * @return the cloudSyncCommandMaster
	 */
	public Key getCloudSyncCommandMaster() {
		return cloudSyncCommandMaster;
	}

	/**
	 * @return the cloudSyncCommandGroups
	 */
	public ArrayList<Key> getCloudSyncCommandGroups() {
		return cloudSyncCommandGroups;
	}

	/**
	 * @return the cloudSyncCommandMessage
	 */
	public String getCloudSyncCommandMessage() {
		return cloudSyncCommandMessage;
	}

	/**
	 * @return the cloudSyncCommandCreationDate
	 */
	public Date getCloudSyncCommandCreationDate() {
		return cloudSyncCommandCreationDate;
	}

	/**
	 * @return the cloudSyncCommandEndingDate
	 */
	public Date getCloudSyncCommandEndingDate() {
		return cloudSyncCommandEndingDate;
	}
	
	/**
	 * @param cloudSyncCommandMaster 
	 * the cloudSyncCommandMaster to set
	 */
	public void setCloudSyncCommandMaster(
			Key cloudSyncCommandMaster) {
		this.cloudSyncCommandMaster = cloudSyncCommandMaster;
	}
	
	/**
	 * @param cloudSyncCommandGroups 
	 * the cloudSyncCommandGroups to set
	 */
	public void setCloudSyncCommandGroups(
			ArrayList<Key> cloudSyncCommandGroups) {
		this.cloudSyncCommandGroups = cloudSyncCommandGroups;
	}
	
	/**
	 * @param cloudSyncCommandMessage 
	 * the cloudSyncCommandMessage to set
	 */
	public void setCloudSyncCommandMessage(
			String cloudSyncCommandMessage) {
		this.cloudSyncCommandMessage = 
				cloudSyncCommandMessage;
	}
	
	/**
	 * @param cloudSyncCommandCreationDate 
	 * the cloudSyncCommandCreationDate to set
	 */
	public void setCloudSyncCommandCreationDate() {
		
		this.cloudSyncCommandCreationDate = new Date();
		
		this.cloudSyncCommandEndingDate = 
    			DateManager.addMinutesToDate(
    					cloudSyncCommandCreationDate, 
    					MESSAGE_DURATION);
	}
}