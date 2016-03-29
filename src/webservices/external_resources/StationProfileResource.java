/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.ChannelSimple;
import webservices.datastore_simple.StationSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Channel;
import datastore.ChannelManager;
import datastore.RegionManager;
import datastore.Station;
import datastore.StationManager;

/**
 * This class represents the list of station
 * as a Resource with only one representation
 */

public class StationProfileResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(StationProfileResource.class.getName());
	
	/**
	 * Returns station as a JSON object.
	 * @return A station in JSON format
	 */
    @Get("json")
    public StationSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	        
	    Key stationKey = KeyFactory.stringToKey(searchString);
	    log.info("Query: " + searchBy + "=" + searchString);

	    // Get station
	    Station station = StationManager.getStation(stationKey);
	    
    	// First, get and create the channels
    	ArrayList<ChannelSimple> simpleChannels = new ArrayList<ChannelSimple>();
    	List<Channel> channels = ChannelManager.getStationChannels(station.getKey());
    	for (Channel channel : channels) {
    		Key channelKey = channel.getKey();	// TODO: Remove?
    		BlobKey firstSlideBlobKey =// new BlobKey("");
    				ChannelManager.getFirstSlideBlobKey(channel);
    		ChannelSimple channelSimple = new ChannelSimple(
    				KeyFactory.keyToString(channel.getKey()),
    				channel.getChannelName(),
    				channel.getChannelNumber(),
    				firstSlideBlobKey
    				);
    		simpleChannels.add(channelSimple);
    	}
	    
    	// Then create the station simple
    	StationSimple stationSimple = new StationSimple(
    			KeyFactory.keyToString(station.getKey()),
    			station.getStationType(),
    			station.getStationPrivilegeLevel(),
    			station.getStationName(),
    			station.getStationNumber(),
    			station.getStationDescription(),
    			RegionManager.getRegion(station.getRegion()).getRegionName(),
    			station.getStationAddress() != null ? 
    					station.getStationAddress().getAddress() : "",
    			station.getStationWebsite() != null ? 
    					station.getStationWebsite().getValue() : "",
    			station.getUser().getUserEmail().getEmail(),
    			station.getStationLogo() != null ? 
    					station.getStationLogo() : new BlobKey(""),
    			station.getStationComments() != null ?
    					station.getStationComments() : "",
    			simpleChannels);

    	return stationSimple;
    }

}