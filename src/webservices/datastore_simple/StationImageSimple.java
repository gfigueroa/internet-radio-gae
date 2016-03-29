/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * This class represents a simple version of the StationImage table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class StationImageSimple implements Serializable {
    
	public String stationImageKey;
	public String stationImageName;
	public BlobKey stationImageMultimediaContent;
	public String stationImageFormat;
    
    /**
     * StationImageSimple constructor.
     * @param stationImageKey
     * 			: stationImage key
     * @param stationImageName
     * 			: stationImage name
     * @param stationImageMultimediaContent
     * 			: stationImage Multimedia Content BlobKey
     * @param stationImageFormat
     * 			: stationImage format
     */
    public StationImageSimple(String stationImageKey, 
    		String stationImageName, 
    		BlobKey stationImageMultimediaContent,
    		String stationImageFormat
    		) {

    	this.stationImageKey = stationImageKey;
    	this.stationImageName = stationImageName;
    	this.stationImageMultimediaContent = stationImageMultimediaContent;
    	this.stationImageFormat = stationImageFormat;
    }
    
    /**
     * Compare this stationImage with another StationImage
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this StationImage, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StationImageSimple ) ) return false;
        StationImageSimple r = (StationImageSimple) o;
        return this.stationImageKey.equals(r.stationImageKey);
    }
    
}
