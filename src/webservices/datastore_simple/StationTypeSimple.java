/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the StationType table.
 * It is kept simple to return only some information to mobile Apps.
 * 
 */

@SuppressWarnings("serial")
public class StationTypeSimple implements Serializable {
    
	public Long key;
	public String stationTypeName;
	public String stationTypeDescription;
	public String stationTypeCreationTime;
	public String stationTypeModificationTime;
	public Integer stationTypeVersion;
    
    /**
     * StationTypeSimple constructor.
     * @param key
     * 			: StationType key
     * @param stationTypeName
     * 			: The name of the station type
     * @param stationTypeDescription
     * 			: The description of the station type
     * @param stationTypeCreationTime
     * 			: The creation time of the station type
     * @param stationTypeModificationTime
     * 			: The modification time of the station type
     * @param stationTypeVersion
     * 			: The version number for this station type's stations
     */
    public StationTypeSimple(Long key, 
    		String stationTypeName,
    		String stationTypeDescription,
    		String stationTypeCreationTime,
    		String stationTypeModificationTime,
    		Integer stationTypeVersion) {

    	this.key = key;
    	this.stationTypeName = stationTypeName;
    	this.stationTypeDescription = stationTypeDescription;
    	this.stationTypeCreationTime = stationTypeCreationTime;
    	this.stationTypeModificationTime = stationTypeModificationTime;
    	this.stationTypeVersion = stationTypeVersion;
    }
    
    /**
     * Compare this station type with another StationType
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this StationType, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StationTypeSimple ) ) return false;
        StationTypeSimple r = (StationTypeSimple) o;
        return this.key.equals(r.key);
    }
    
}
