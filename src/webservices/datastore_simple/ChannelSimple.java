/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * This class represents a simple version of the Channel table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class ChannelSimple implements Serializable {
    
	public String key;
	public String channelName;
	public Integer channelNumber;
	public BlobKey firstSlideBlobKey;
    
    /**
     * ChannelSimple constructor.
     * @param key
     * 			: channel key
     * @param channelName
     * 			: channel name
     * @param channelNumber
     * 			: channel number
     * @param firstSlideBlobKey
     * 			: the BlobKey of the first slide of the
     * 			first program in this channel.
     */
    public ChannelSimple(String key, String channelName, 
    		Integer channelNumber, BlobKey firstSlideBlobKey) {

    	this.key = key;
    	this.channelName = channelName;
        this.channelNumber = channelNumber;
        this.firstSlideBlobKey = firstSlideBlobKey;
    }
    
    /**
     * Compare this channel with another Channel
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Channel, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof ChannelSimple ) ) return false;
        ChannelSimple r = (ChannelSimple) o;
        return this.key.equals(r.key);
    }
    
}
