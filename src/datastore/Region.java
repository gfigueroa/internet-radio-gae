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
 * This class represents the Region table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class Region {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private String regionName;
    
    @Persistent
    private String regionComments;
    
    @Persistent
    private Date regionCreationTime;

	@Persistent
    private Date regionModificationTime;

    /**
     * Region constructor.
     * @param regionName
     * 			: region name
     * @param regionComments
     * 			: region comments
     * @throws MissingRequiredFieldsException
     */
    public Region(String regionName, String regionComments) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (regionName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (regionName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.regionName = regionName;
        this.regionComments = regionComments;
        
        Date now = new Date();
        this.regionCreationTime = now;
        this.regionModificationTime = now;
    }

    /**
     * Get Region key.
     * @return region key
     */
    public Long getKey() {
        return key;
    }

    /**
     * Get Region name.
     * @return restaurant region name
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * Get Region comments.
     * @return restaurant region comments
     */
    public String getRegionComments() {
    	return regionComments;
    }
    
    /**
	 * @return the regionCreationTime
	 */
	public Date getRegionCreationTime() {
		return regionCreationTime;
	}

	/**
	 * @return the regionModificationTime
	 */
	public Date getRegionModificationTime() {
		return regionModificationTime;
	}
    
    /**
     * Set Region name.
     * @param regionName
     * 			: region name
     * @throws MissingRequiredFieldsException
     */
    public void setRegionName(String regionName)
    		throws MissingRequiredFieldsException {
    	if (regionName == null || regionName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Region name is missing.");
    	}
    	this.regionName = regionName;
        this.regionModificationTime = new Date();
    }
    
    /**
     * Set Region comments.
     * @param regionComments
     * 			: region comments
     */
    public void setRegionComments(String regionComments) {
    	this.regionComments = regionComments;
    	this.regionModificationTime = new Date();
    }
}