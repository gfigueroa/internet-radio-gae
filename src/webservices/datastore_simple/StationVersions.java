/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the Station Versions.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class StationVersions implements Serializable {
    
	/**
	 * This class represents a simple version of the Channel Versions.
	 * It is kept simple to return only some information to mobile apps.
	 * 
	 */
	public static class ChannelVersions {
		public String channelKey;
		public Integer programVersion;
		
		/**
	     * ChannelVersionsSimple constructor.
	     * @param channelKey
	     * 			: channel key
	     * @param programVersion
	     * 			: program version
	     */
	    public ChannelVersions(String channelKey, 
	    		Integer programVersion) {

	    	this.channelKey = channelKey;
	    	this.programVersion = programVersion;
	    }
	}
	
	public String stationKey;
	public Integer playlistVersion;
	public Integer stationImageVersion;
	public Integer stationAudioVersion;
	public ArrayList<ChannelVersions> channelVersions;
    
    /**
     * StationVersionsSimple constructor.
     * @param stationKey
     * 			: station key
     * @param playlistVersion
     * 			: playlist version
     * @param stationImageVersion
     * 			: station image version
     * @param stationAudioVersion
     * 			: station audio version
     * @param channelVersions
     * 			: the channel versions of this station
     */
    public StationVersions(String stationKey, 
    		Integer playlistVersion, Integer stationImageVersion,
    		Integer stationAudioVersion,
    		ArrayList<ChannelVersions> channelVersions) {

    	this.stationKey = stationKey;
    	this.playlistVersion = playlistVersion;
    	this.stationImageVersion = stationImageVersion;
    	this.stationAudioVersion = stationAudioVersion;
    	this.channelVersions = channelVersions;
    }
    
    /**
     * Compare this stationVersions with another StationVersions
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this StationVersions, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StationVersions ) ) return false;
        StationVersions r = (StationVersions) o;
        return this.stationKey.equals(r.stationKey);
    }
    
}
