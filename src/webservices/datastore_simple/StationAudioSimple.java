/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * This class represents a simple version of the StationAudio table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class StationAudioSimple implements Serializable {
    
	public String stationAudioKey;
	public String stationAudioType;
	public String stationAudioName;
	public BlobKey stationAudioMultimediaContent;
	public Double stationAudioDuration;
	public String stationAudioFormat;
    
    /**
     * StationAudioSimple constructor.
     * @param stationAudioKey
     * 			: stationAudio key
     * @param stationAudioType
     * 			: stationAudio type
     * @param stationAudioName
     * 			: stationAudio name
     * @param stationAudioMultimediaContent
     * 			: stationAudio Multimedia Content BlobKey
     * @param stationAudioDuration
     * 			: stationAudio duration
     * @param stationAudioFormat
     * 			: stationAudio format
     */
    public StationAudioSimple(String stationAudioKey, 
    		String stationAudioType,
    		String stationAudioName, 
    		BlobKey stationAudioMultimediaContent,
    		Double stationAudioDuration,
    		String stationAudioFormat
    		) {

    	this.stationAudioKey = stationAudioKey;
    	this.stationAudioType = stationAudioType;
    	this.stationAudioName = stationAudioName;
    	this.stationAudioMultimediaContent = stationAudioMultimediaContent;
    	this.stationAudioDuration = stationAudioDuration;
    	this.stationAudioFormat = stationAudioFormat;
    }
    
    /**
     * Compare this stationAudio with another StationAudio
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this StationAudio, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StationAudioSimple ) ) return false;
        StationAudioSimple r = (StationAudioSimple) o;
        return this.stationAudioKey.equals(r.stationAudioKey);
    }
    
}
