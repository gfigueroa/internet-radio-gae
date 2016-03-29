/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the System table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class SystemSimple implements Serializable {

    public Long key;
    public Integer stationListVersion;
    public Integer stationTypeListVersion;
    public Integer musicLibraryVersion;
    public String oldestAppVersionSupported;
    public ArrayList<StationVersions> stationVersions;

    /**
     * MenuItemSimple constructor.
     * @param key:
     * 			: order key
     * @param stationListVersion
     * 			: station list version
     * @param stationTypeListVersion
     * 			: station type list version
     * @param musicLibraryVersion
     * 			: music library version
     * @param oldestAppVersionSupported
     * 			: oldest app version supported by this server version
     * @param stationVersions
     * 			: all station versions
     */
    public SystemSimple(Long key, 
    		Integer stationListVersion,
    		Integer stationTypeListVersion, 
    		Integer musicLibraryVersion,
    		String oldestAppVersionSupported, 
    		ArrayList<StationVersions> stationVersions) {
    	
    	this.key = key;
    	this.stationListVersion = stationListVersion;
    	this.stationTypeListVersion = stationTypeListVersion;
    	this.musicLibraryVersion = musicLibraryVersion;
    	this.oldestAppVersionSupported = oldestAppVersionSupported;
    	this.stationVersions = stationVersions;
    }

    /**
     * Compare this system with another system
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this System, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SystemSimple ) ) return false;
        SystemSimple system = (SystemSimple) o;
        return key.equals(system.key);
    }

}