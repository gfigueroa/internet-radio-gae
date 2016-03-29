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

import webservices.datastore_simple.StationImageSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.StationImage;
import datastore.StationImageManager;

/**
 * This class represents the list of StationImage
 * as a Resource with only one representation
 */

public class StationImagesResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(StationImagesResource.class.getName());
	
	/**
	 * Returns the stationImage list as a JSON object.
	 * @return An ArrayList of stationImage in JSON format
	 */
    @Get("json")
    public ArrayList<StationImageSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);   
	    
	    log.info("Query: " + searchBy + "=" + searchString);

	    List<StationImage> stationImageList = null;
	    // Get all the stationAudios from every station
	    if (searchBy == 's') {
	    	Key stationKey = KeyFactory.stringToKey(searchString);
		    stationImageList = 
		    		StationImageManager.getAllStationImagesFromStation(stationKey, true);
	    }
		// Get a single stationAudio
		else if (searchBy == 'k') {
			Key stationImageKey = KeyFactory.stringToKey(searchString);
			StationImage stationImage = StationImageManager.getStationImage(stationImageKey);
			stationImageList = new ArrayList<>();
			stationImageList.add(stationImage);
		}
		else {
			return null;
		}

        ArrayList<StationImageSimple> stationImageListSimple = 
        		new ArrayList<StationImageSimple>();
        for (StationImage stationImage : stationImageList) {
        	
        	StationImageSimple stationImageSimple = new StationImageSimple(
        			KeyFactory.keyToString(stationImage.getKey()),
        			stationImage.getStationImageName(),
        			stationImage.getStationImageMultimediaContent(),
        			stationImage.getStationImageFormat() != null ?
        					stationImage.getStationImageFormat() : ""
        			);
        	
        	stationImageListSimple.add(stationImageSimple);
        }
        
        return stationImageListSimple;
    }

}