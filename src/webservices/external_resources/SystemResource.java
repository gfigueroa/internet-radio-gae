/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.StationVersions;
import webservices.datastore_simple.StationVersions.ChannelVersions;
import webservices.datastore_simple.SystemSimple;

import com.google.appengine.api.datastore.KeyFactory;

import datastore.Channel;
import datastore.ChannelManager;
import datastore.Station;
import datastore.StationManager;
import datastore.System;
import datastore.SystemManager;

/**
 * This class represents the System
 * JDO Object which contains some version numbers
 * required by the mobile app.
 */

public class SystemResource extends ServerResource {

	/**
	 * Returns the System table instance as a JSON object.
	 * @return The instance of the System object in JSON format
	 */
    @Get("json")
    public SystemSimple toJson() {
        
    	// Get Station Versions
    	List<Station> stations = StationManager.getAllStations();
    	ArrayList<StationVersions> stationVersionList = new ArrayList<StationVersions>();
    	for (Station station : stations) {
    		List<Channel> channels = ChannelManager.getStationChannels(station.getKey());
    		
    		ArrayList<ChannelVersions> channelVersionList = new ArrayList<ChannelVersions>();
    		for (Channel channel : channels) {
    			ChannelVersions channelVersions = new ChannelVersions(
    					KeyFactory.keyToString(channel.getKey()),
    					channel.getProgramVersion());
    			channelVersionList.add(channelVersions);
    		}
    		
            StationVersions stationVersions = new StationVersions(
            		KeyFactory.keyToString(station.getKey()),
            		station.getPlaylistVersion(),
            		station.getStationImageVersion(),
            		station.getStationAudioVersion(),
            		channelVersionList);
            stationVersionList.add(stationVersions);
    	}
    	
        System system = SystemManager.getSystem();
        SystemSimple systemSimple = new SystemSimple(
        		system.getKey(),
        		system.getStationListVersion(),
        		system.getStationTypeListVersion(),
        		system.getMusicLibraryVersion(),
        		system.getOldestAppVersionSupportedString() != null ? 
        				system.getOldestAppVersionSupportedString() : "",
        		stationVersionList
        		);
        return systemSimple;
    }

}
