/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

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
 * This class represents the list of stations 
 * as a Resource with only one representation
 */

public class StationsResource extends ServerResource {
	
	/**
	 * Returns the simple station list as a JSON object.
	 * @return An ArrayList of StationSimple in JSON format
	 */
    @Get("json")
    public ArrayList<StationSimple> toJson() {
        
        List<Station> stations = StationManager.getAllStations();
        
        ArrayList<StationSimple> simpleStations = new ArrayList<StationSimple>();
        for (Station station : stations) {
        	
        	// First, get and create the channels
        	ArrayList<ChannelSimple> simpleChannels = new ArrayList<ChannelSimple>();
        	List<Channel> channels = ChannelManager.getStationChannels(station.getKey());
        	for (Channel channel : channels) {
        		Key channelKey = channel.getKey();
        		BlobKey firstSlideBlobKey = 
        				ChannelManager.getFirstSlideBlobKey(channelKey);
        		ChannelSimple channelSimple = new ChannelSimple(
        				KeyFactory.keyToString(channel.getKey()),
        				channel.getChannelName(),
        				channel.getChannelNumber(),
        				firstSlideBlobKey
        				);
        		simpleChannels.add(channelSimple);
        	}
        	
        	// Then create the stations
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
        	simpleStations.add(stationSimple);
        }
        
        return simpleStations;
    }

}