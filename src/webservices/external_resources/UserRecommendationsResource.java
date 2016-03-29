/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.ProgramSimple;
import webservices.datastore_simple.ProgramSimple.MainTrackSimple;
import webservices.datastore_simple.ProgramSimple.SecondaryTrackSimple;
import webservices.datastore_simple.ProgramSimple.SlideSimple;
import webservices.datastore_simple.UserRecommendationsSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.Program;
import datastore.ProgramManager;
import datastore.SecondaryTrack;
import datastore.SecondaryTrackManager;
import datastore.Slide;
import datastore.SlideManager;
import datastore.Station;
import datastore.StationManager;
import datastore.sheep.UserRecommendation;
import datastore.sheep.UserRecommendationManager;

/**
 * This class represents the list of UserRecommendations
 * as a Resource with only one representation
 */

public class UserRecommendationsResource extends ServerResource {

	private static final Logger log = 
	        Logger.getLogger(UserRecommendationsResource.class.getName());
	
	/**
	 * Returns the simple user recommendation list as a JSON object.
	 * @return An instance of UserRecommendationSimple in JSON format
	 */
    @Get("json")
    public UserRecommendationsSimple toJson() {
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
        
    	int indexOfSecondParameter = queryInfo.indexOf('&');
    	int indexOfThirdParameter = queryInfo.lastIndexOf('&');
    	String customerEmail = "";
    	String kindString = "";
    	String dateString = "";
    	
    	// If one or less parameters were given
    	if (indexOfSecondParameter == -1) {
    		return null;
    	}
    	// If only two parameters were given
    	else if (indexOfSecondParameter == indexOfThirdParameter) {
    		
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, indexOfSecondParameter);
        	char searchByAttribute2 = queryInfo.charAt(indexOfSecondParameter + 1);
        	String searchByValue2 = queryInfo.substring(indexOfSecondParameter + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2);

        	if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'k') {
        		kindString = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 'c') {
        		customerEmail = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'k') {
        			kindString = searchByValue2;
        	}
        	else {
        		return null;
        	}
    	}
    	else {
    		
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, indexOfSecondParameter);
        	char searchByAttribute2 = queryInfo.charAt(indexOfSecondParameter + 1);
        	String searchByValue2 = queryInfo.substring(indexOfSecondParameter + 3,
        			indexOfThirdParameter);
        	char searchByAttribute3 = queryInfo.charAt(indexOfThirdParameter + 1);
        	String searchByValue3 = queryInfo.substring(indexOfThirdParameter + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2 +
        			" --- " + searchByAttribute3 + "=" + searchByValue3);

        	if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'k') {
        		kindString = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'd') {
        		dateString = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 'c') {
        		customerEmail = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'k') {
        			kindString = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'd') {
    			dateString = searchByValue2;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute3) == 'c') {
        		customerEmail = searchByValue3;
        	}
        	else if (Character.toLowerCase(searchByAttribute3) == 'k') {
        			kindString = searchByValue3;
        	}
        	else if (Character.toLowerCase(searchByAttribute3) == 'd') {
    			dateString = searchByValue3;
        	}
        	else {
        		return null;
        	}
    		
    	}
    	
    	Key customerKey = KeyFactory.createKey(
    			Customer.class.getSimpleName(), customerEmail);
    	// TODO: Date parameter not working yet
    	Date date = !dateString.isEmpty() ? 
    			DateManager.getSimpleDateValue(dateString) : null;

    	ArrayList<UserRecommendationsSimple.UserRecommendationSimple> 
    			sharedRecommendations = 
    			new ArrayList<UserRecommendationsSimple.UserRecommendationSimple>();
    	ArrayList<UserRecommendationsSimple.UserRecommendationSimple> 
				receivedRecommendations = 
				new ArrayList<UserRecommendationsSimple.UserRecommendationSimple>();
    			
    	if (kindString.equalsIgnoreCase("SHARED") ||
    			kindString.equalsIgnoreCase("BOTH")) {
    		
    		List<UserRecommendation> ownedRecommendations = 
    				UserRecommendationManager.getOwnedRecommendations(customerKey, date);
    		for (UserRecommendation userRecommendation : ownedRecommendations) {
    			
    			// For Program Recommendations
            	ProgramSimple programSimple = null;
            	if (userRecommendation.getUserRecommendationType() ==
            			UserRecommendation.UserRecommendationType.MESSAGE) {
            		
            		Program program = 
            				ProgramManager.getProgram(
            						userRecommendation.getUserRecommendationItemKey());
            		
            		// Create main track
                	//MainTrack mainTrack = program.getMainTrack();
                	MainTrackSimple mainTrackSimple = null;

                	// Create secondary tracks
                	List<SecondaryTrack> secondaryTracks = 
                			SecondaryTrackManager.getAllSecondaryTracksFromProgram(
                					program.getKey(), true);
                	ArrayList<SecondaryTrackSimple> secondaryTracksSimple = 
                			new ArrayList<SecondaryTrackSimple>();
                	for (SecondaryTrack secondaryTrack : secondaryTracks) {
                		SecondaryTrackSimple secondaryTrackSimple = null;
                		secondaryTrack.getClass();
                		secondaryTracksSimple.add(secondaryTrackSimple);
                	}
                	
                	// Create slides
                	List<Slide> slides = 
                			SlideManager.getAllSlidesFromProgram(program.getKey(), true);
                	ArrayList<SlideSimple> slidesSimple = new ArrayList<SlideSimple>();
                	for (Slide slide : slides) {
                		SlideSimple slideSimple = new SlideSimple(
                				KeyFactory.keyToString(slide.getKey()),
                				KeyFactory.keyToString(slide.getStationImage()),
                				null,
                				slide.getSlideStartingTime(),
                				null
                				);
                		
                		slidesSimple.add(slideSimple);
                	}
                	
                	// Look for station logo
                	Station station = 
                			StationManager.getStation(
                					program.getKey().getParent().getParent());
                	BlobKey stationLogo = station.getStationLogo();
                	
                	programSimple = new ProgramSimple(
                			KeyFactory.keyToString(program.getKey()),
                			KeyFactory.keyToString(program.getKey().getParent()),
                			program.getProgramName(),
                			program.getProgramDescription() != null ?
                					program.getProgramDescription() : "",
                			program.getProgramBanner() != null ? 
                					program.getProgramBanner() : "",
                			program.getProgramSequenceNumber(),
                			program.getProgramTotalDurationTime(),
                			program.getProgramOverlapDuration(),
                			DateManager.printDateAsString(program.getProgramStartingDate()),
                			DateManager.printDateAsString(program.getProgramEndingDate()),
                			stationLogo != null ? stationLogo : new BlobKey(""),
                			mainTrackSimple,
                			secondaryTracksSimple,
                			slidesSimple
                			);
            	}
    			
    			UserRecommendationsSimple.UserRecommendationSimple userRecommendationSimple =
    					new UserRecommendationsSimple.UserRecommendationSimple(
    							KeyFactory.keyToString(userRecommendation.getKey()), 
    							customerEmail, 
    							userRecommendation.getUserRecommendationTypeString(), 
    							DateManager.printDateAsString(
    									userRecommendation.getUserRecommendationCreationDate()),
    							userRecommendation.getUserRecommendationComment(), 
    							KeyFactory.keyToString(
    									userRecommendation.getUserRecommendationItemKey()),
    							programSimple
    							);
    			sharedRecommendations.add(userRecommendationSimple);
    		}
    	}
    	if (kindString.equalsIgnoreCase("RECEIVED") ||
    			kindString.equalsIgnoreCase("BOTH")) {
    		
    		List<UserRecommendation> followedRecommendations = 
    				UserRecommendationManager.getFollowedRecommendations(customerKey, date);
    		for (UserRecommendation userRecommendation : followedRecommendations) {
    			Customer owner = 
    					CustomerManager.getCustomer(userRecommendation.getKey().getParent());
    			
    			// For Program Recommendations
            	ProgramSimple programSimple = null;
            	if (userRecommendation.getUserRecommendationType() ==
            			UserRecommendation.UserRecommendationType.MESSAGE) {
            		
            		Program program = 
            				ProgramManager.getProgram(
            						userRecommendation.getUserRecommendationItemKey());
            		
            		// Create main track
                	//MainTrack mainTrack = program.getMainTrack();
                	MainTrackSimple mainTrackSimple = null;

                	// Create secondary tracks
                	List<SecondaryTrack> secondaryTracks = 
                			SecondaryTrackManager.getAllSecondaryTracksFromProgram(
                					program.getKey(), true);
                	ArrayList<SecondaryTrackSimple> secondaryTracksSimple = 
                			new ArrayList<SecondaryTrackSimple>();
                	for (SecondaryTrack secondaryTrack : secondaryTracks) {
                		SecondaryTrackSimple secondaryTrackSimple = null;
                		secondaryTrack.getKey();
                		secondaryTracksSimple.add(secondaryTrackSimple);
                	}
                	
                	// Create slides
                	List<Slide> slides = 
                			SlideManager.getAllSlidesFromProgram(program.getKey(), true);
                	ArrayList<SlideSimple> slidesSimple = new ArrayList<SlideSimple>();
                	for (Slide slide : slides) {
                		SlideSimple slideSimple = new SlideSimple(
                				KeyFactory.keyToString(slide.getKey()),
                				KeyFactory.keyToString(slide.getStationImage()),
                				null,
                				slide.getSlideStartingTime(),
                				null
                				);
                		
                		slidesSimple.add(slideSimple);
                	}
                	
                	// Look for station logo
                	Station station = 
                			StationManager.getStation(
                					program.getKey().getParent().getParent());
                	BlobKey stationLogo = station.getStationLogo();
                	
                	programSimple = new ProgramSimple(
                			KeyFactory.keyToString(program.getKey()),
                			KeyFactory.keyToString(program.getKey().getParent()),
                			program.getProgramName(),
                			program.getProgramDescription() != null ?
                					program.getProgramDescription() : "",
                			program.getProgramBanner() != null ? 
                					program.getProgramBanner() : "",
                			program.getProgramSequenceNumber(),
                			program.getProgramTotalDurationTime(),
                			program.getProgramOverlapDuration(),
                			DateManager.printDateAsString(program.getProgramStartingDate()),
                			DateManager.printDateAsString(program.getProgramEndingDate()),
                			stationLogo != null ? stationLogo : new BlobKey(""),
                			mainTrackSimple,
                			secondaryTracksSimple,
                			slidesSimple
                			);
            	}
    			
    			UserRecommendationsSimple.UserRecommendationSimple userRecommendationSimple =
    					new UserRecommendationsSimple.UserRecommendationSimple(
    							KeyFactory.keyToString(userRecommendation.getKey()), 
    							owner.getUser().getUserEmail().getEmail(), 
    							userRecommendation.getUserRecommendationTypeString(), 
    							DateManager.printDateAsString(
    									userRecommendation.getUserRecommendationCreationDate()),
    							userRecommendation.getUserRecommendationComment(), 
    							KeyFactory.keyToString(
    									userRecommendation.getUserRecommendationItemKey()),
    							programSimple
    							);
    			receivedRecommendations.add(userRecommendationSimple);
    		}
    	}
    	
    	UserRecommendationsSimple userRecommendationsSimple = 
    			new UserRecommendationsSimple(
    					customerEmail,
    					sharedRecommendations,
    					receivedRecommendations);
    	
    	return userRecommendationsSimple;
    }

}