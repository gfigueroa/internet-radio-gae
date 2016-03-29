/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;


/**
 * This class represents a simple version of the Station table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class StationSimple implements Serializable {
    
	public String key;
	public Long stationType;
	public Integer stationPrivilegeLevel;
	public String stationName;
	public String stationNumber;
	public String stationDescription;
	public String regionName;
	public String stationAddress;
	public String stationWebsite;
	public String stationEmail;
	public BlobKey stationLogo;
	public String stationComments;
	public ArrayList<ChannelSimple> channels;
    
    /**
     * StationSimple constructor.
     * @param key
     * 			: station key
     * @param stationType
     * 			: station type key
     * @param stationPrivilegeLevel
     * 			: station privilege level
     * @param stationName
     * 			: station name
     * @param stationNumber
     * 			: station number
     * @param stationDescription
     * 			: station description
     * @param regionName
     * 			: region name
     * @param stationAddress
     * 			: station address
     * @param stationWebsite
     * 			: station website
     * @param stationEmail
     * 			: station email
     * @param stationLogo
     * 			: station logo blob key
     * @param stationComments
     * 			: station comments
     * @param channels
     * 			: station channels
     */
    public StationSimple(String key, Long stationType,
    		Integer stationPrivilegeLevel, String stationName, 
    		String stationNumber, String stationDescription,
    		String regionName, String stationAddress,
    		String stationWebsite, String stationEmail,
    		BlobKey stationLogo, String stationComments,
    		ArrayList<ChannelSimple> channels) {

    	this.key = key;
    	this.stationType = stationType;
    	this.stationPrivilegeLevel = stationPrivilegeLevel;
    	this.stationName = stationName;
    	this.stationNumber = stationNumber;
    	this.stationDescription = stationDescription;
    	this.regionName = regionName;
    	this.stationAddress = stationAddress;
        this.stationWebsite = stationWebsite;
        this.stationEmail = stationEmail;
        this.stationLogo = stationLogo;
        this.stationComments = stationComments;
        this.channels = channels;
    }
    
    /**
     * Compare this station with another Station
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Station, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StationSimple ) ) return false;
        StationSimple r = (StationSimple) o;
        return this.key.equals(r.key);
    }
    
}
