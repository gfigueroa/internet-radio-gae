/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the StationType table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class StationType {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private String stationTypeName;
    
    @Persistent
    private String stationTypeDescription;
    
    @Persistent
    private Integer stationTypeVersion;
    
    @Persistent
    private Date stationTypeCreationDate;
    
    @Persistent
    private Date stationTypeModificationDate;

    /**
     * StationType constructor.
     * @param stationTypeName
     * 			: station type name
     * @param stationTypeDescription
     * 			: station type description
     * @throws MissingRequiredFieldsException
     */
    public StationType(String stationTypeName, String stationTypeDescription) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (stationTypeName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (stationTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

        this.stationTypeName = stationTypeName;
        this.stationTypeDescription = stationTypeDescription;
        this.stationTypeVersion = 0; // Initialize the version in 0
        
        Date now = new Date();
        this.stationTypeCreationDate = now;
        this.stationTypeModificationDate = now;
    }

    /**
     * Get StationType key.
     * @return station type key
     */
    public Long getKey() {
        return key;
    }
    
    /**
     * Get StationType name.
     * @return station type name
     */
    public String getStationTypeName() {
        return stationTypeName;
    }

    /**
     * Get StationType description.
     * @return station type description
     */
    public String getStationTypeDescription() {
    	return stationTypeDescription;
    }
    
    /**
     * Get StationType version.
     * @return station type version
     */
    public Integer getStationTypeVersion() {
    	if (stationTypeVersion == null) {
    		stationTypeVersion = 0;
    	}
    	return stationTypeVersion;
    }
    
    /**
     * Get StationType creation date.
     * @return station type creation date
     */
    public Date getStationTypeCreationDate() {
    	return stationTypeCreationDate;
    }
    
    /**
     * Get StationType modification date.
     * @return station type modification date
     */
    public Date getStationTypeModificationDate() {
    	return stationTypeModificationDate;
    }
    
    /**
     * Set StationType name.
     * @param stationTypeName
     * 			: station type name
     * @throws MissingRequiredFieldsException
     */
    public void setStationTypeName(String stationTypeName)
    		throws MissingRequiredFieldsException {
    	if (stationTypeName == null || stationTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item type name is missing.");
    	}
    	this.stationTypeName = stationTypeName;
    	this.stationTypeModificationDate = new Date();
    }
    
    /**
     * Set StationType description.
     * @param stationTypeDescription
     * 			: station type description
     */
    public void setStationTypeDescription(String stationTypeDescription) {
    	this.stationTypeDescription = stationTypeDescription;
    	this.stationTypeModificationDate = new Date();
    }
    
    /**
     * Update station type version by 1.
     * The station type version is updated every time a station
     * belonging to this station type is added, deleted or modified.
     */
    public void updateStationTypeVersion() {
    	if (stationTypeVersion == null) {
    		stationTypeVersion = 0;
    	}
    	this.stationTypeVersion++;
    }
}