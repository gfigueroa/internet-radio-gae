/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.StationTypeSimple;

import datastore.StationType;
import datastore.StationTypeManager;

/**
 * This class represents the list of station types 
 * as a Resource with only one representation
 */

public class StationTypesResource extends ServerResource {

	/**
	 * Returns the station type list as a JSON object.
	 * @return An ArrayList of StationType in JSON format
	 */
    @Get("json")
    public ArrayList<StationTypeSimple> toJson() {
        
    	List<StationType> stationTypes = StationTypeManager.getAllStationTypes();
    	
    	// We will be returning a list of elements
    	ArrayList<StationTypeSimple> stationTypesSimple =
        		new ArrayList<StationTypeSimple>();
    	for (StationType stationType : stationTypes) {
    		StationTypeSimple stationTypeSimple 
    				= new StationTypeSimple(stationType.getKey(),
    						stationType.getStationTypeName(),
    						stationType.getStationTypeDescription() != null ? 
    								stationType.getStationTypeDescription() : "",
    						DateManager.printDateAsString(
    								stationType.getStationTypeCreationDate()),
    						DateManager.printDateAsString(
    								stationType.getStationTypeModificationDate()),
    						stationType.getStationTypeVersion()
    						);
    		stationTypesSimple.add(stationTypeSimple);
    	}

        return stationTypesSimple;
    }

}