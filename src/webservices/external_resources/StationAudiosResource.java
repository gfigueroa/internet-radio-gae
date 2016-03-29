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

import webservices.datastore_simple.StationAudioSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.StationAudio;
import datastore.StationAudioManager;

/**
 * This class represents the list of StationAudio
 * as a Resource with only one representation
 */

public class StationAudiosResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(StationAudiosResource.class.getName());
	
	/**
	 * Returns the stationAudio list as a JSON object.
	 * @return An ArrayList of stationAudio in JSON format
	 */
    @Get("json")
    public ArrayList<StationAudioSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);   
	    
	    log.info("Query: " + searchBy + "=" + searchString);

	    List<StationAudio> stationAudioList = null;
	    // Get all the stationAudios from every station
	    if (searchBy == 's') {
	    	Key stationKey = KeyFactory.stringToKey(searchString);
		    stationAudioList = 
		    		StationAudioManager.getAllStationAudiosFromStation(stationKey, true);
	    }
		// Get a single stationAudio
		else if (searchBy == 'k') {
			Key stationAudioKey = KeyFactory.stringToKey(searchString);
			StationAudio stationAudio = StationAudioManager.getStationAudio(stationAudioKey);
			stationAudioList = new ArrayList<>();
			stationAudioList.add(stationAudio);
		}
		else {
			return null;
		}

        ArrayList<StationAudioSimple> stationAudioListSimple = 
        		new ArrayList<StationAudioSimple>();
        for (StationAudio stationAudio : stationAudioList) {
        	
        	StationAudioSimple stationAudioSimple = new StationAudioSimple(
        			KeyFactory.keyToString(stationAudio.getKey()),
        			stationAudio.getStationAudioTypeString(),
        			stationAudio.getStationAudioName(),
        			stationAudio.getStationAudioMultimediaContent(),
        			stationAudio.getStationAudioDuration() != null ?
        					stationAudio.getStationAudioDuration() : 0,
        			stationAudio.getStationAudioFormat() != null ?
        					stationAudio.getStationAudioFormat() : ""
        			);
        	
        	stationAudioListSimple.add(stationAudioSimple);
        }
        
        return stationAudioListSimple;
    }

}