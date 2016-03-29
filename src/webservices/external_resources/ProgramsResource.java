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

import util.DateManager;
import webservices.datastore_simple.ProgramSimple;
import webservices.datastore_simple.ProgramSimple.MainTrackSimple;
import webservices.datastore_simple.ProgramSimple.SecondaryTrackSimple;
import webservices.datastore_simple.ProgramSimple.SlideSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Channel;
import datastore.ChannelManager;
import datastore.MainTrack;
import datastore.MainTrack.MainTrackType;
import datastore.MusicFile;
import datastore.MusicFileManager;
import datastore.Program;
import datastore.ProgramManager;
import datastore.SecondaryTrack;
import datastore.SecondaryTrack.SecondaryTrackType;
import datastore.SecondaryTrackManager;
import datastore.Slide;
import datastore.SlideManager;
import datastore.Station;
import datastore.StationAudio;
import datastore.StationAudioManager;
import datastore.StationImage;
import datastore.StationImageManager;
import datastore.StationManager;

/**
 * This class represents the list of program
 * as a Resource with only one representation
 */

public class ProgramsResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(ProgramsResource.class.getName());
	
	/**
	 * Returns the program list as a JSON object.
	 * @return An ArrayList of program in JSON format
	 */
    @Get("json")
    public ArrayList<ProgramSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	    log.info("Query: " + searchBy + "=" + searchString);
	    
	    ArrayList<Program> programList = new ArrayList<Program>();
	    if (searchBy == 's') {
		    // Get all the programs from every channel
	    	Key stationKey = KeyFactory.stringToKey(searchString);
	    	for (Channel channel : ChannelManager.getStationChannels(stationKey)) {
		    	Key channelKey = channel.getKey();
		    	List<Program> channelPrograms = 
		    			//ProgramManager.getActiveProgramsFromChannel(channelKey, false);
		    			ProgramManager.getAllProgramsFromChannel(channelKey, false);
		    	programList.addAll(channelPrograms);
		    }
	    }
	    else if (searchBy == 'c') {
	    	// Get all the programs from a specific channel
	    	Key channelKey = KeyFactory.stringToKey(searchString);
	    	programList = 
	    			(ArrayList<Program>) 
	    			//ProgramManager.getActiveProgramsFromChannel(channelKey, false);
	    			ProgramManager.getAllProgramsFromChannel(channelKey, false);
	    }
	    else {
	    	return null;
	    }

        ArrayList<ProgramSimple> programListSimple = new ArrayList<ProgramSimple>();
        for (Program program : programList) {
        	
        	// Create main track
        	MainTrack mainTrack = program.getMainTrack();
        	StationAudio mainTrackStationAudio = 
        			mainTrack.getMainTrackType() == MainTrackType.FILE_UPLOAD ?
        					StationAudioManager.getStationAudio(mainTrack.getStationAudio()) :
        					null;
        	MusicFile mainTrackMusicFile =
        			mainTrack.getMainTrackType() == MainTrackType.MUSIC_FILE ?
        					MusicFileManager.getMusicFile(mainTrack.getMusicFile()) :
        					null;
        	BlobKey mainTrackStationAudioBlobKey = mainTrackStationAudio != null ?
        			mainTrackStationAudio.getStationAudioMultimediaContent() :
        				new BlobKey("");
        	BlobKey mainTrackMusicFileBlobKey = mainTrackMusicFile != null ?
        			mainTrackMusicFile.getMusicFileFile() :
        				new BlobKey("");
        	MainTrackSimple mainTrackSimple = new MainTrackSimple(
        			mainTrack.getMainTrackType().toString(),
        			mainTrack.getStationAudio() != null ?
        					KeyFactory.keyToString(mainTrack.getStationAudio()) :
        						"",
        			mainTrackStationAudioBlobKey,
        			mainTrackStationAudio != null ?
        					mainTrackStationAudio.getStationAudioName() :
        						"",
        			mainTrackStationAudio != null ?
                			mainTrackStationAudio.getStationAudioFormat() :
                				"",
                	mainTrackStationAudio != null ?
                        	mainTrackStationAudio.getStationAudioDuration() :
                        		0,
        			mainTrack.getMusicFile() != null ?
        					mainTrack.getMusicFile() : 0,
        			mainTrackMusicFileBlobKey,
        			mainTrackMusicFile != null ?
        					mainTrackMusicFile.getMusicFileTitle() :
        						"",
        			mainTrackMusicFile != null ?
        	        		mainTrackMusicFile.getMusicFileFormat() :
        	        			"",
        	        mainTrackMusicFile != null ?
        	        	    mainTrackMusicFile.getMusicFileDuration():
        	        	        0,
        			mainTrack.getPlaylist() != null ?
        					KeyFactory.keyToString(mainTrack.getPlaylist()) : "",
        			mainTrack.getMainTrackDuration(),
        			mainTrack.getMainTrackFadeInSteps(),
        			mainTrack.getMainTrackFadeInDuration(),
        			mainTrack.getMainTrackFadeInPercentage(),
        			mainTrack.getMainTrackFadeOutSteps(),
        			mainTrack.getMainTrackFadeOutDuration(),
        			mainTrack.getMainTrackFadeOutPercentage()
        			);

        	// Create secondary tracks
        	List<SecondaryTrack> secondaryTracks = 
        			SecondaryTrackManager.getAllSecondaryTracksFromProgram(
        					program.getKey(), true);
        	ArrayList<SecondaryTrackSimple> secondaryTracksSimple = 
        			new ArrayList<SecondaryTrackSimple>();
        	for (SecondaryTrack secondaryTrack : secondaryTracks) {
            	StationAudio secondaryTrackStationAudio =
            			secondaryTrack.getSecondaryTrackType() == SecondaryTrackType.FILE_UPLOAD ?
            					StationAudioManager.getStationAudio(secondaryTrack.getStationAudio()) :
            					null;
            	MusicFile secondaryTrackMusicFile =
            			secondaryTrack.getSecondaryTrackType() == SecondaryTrackType.MUSIC_FILE ?
            					MusicFileManager.getMusicFile(secondaryTrack.getMusicFile()) :
            						null;
            	BlobKey secondaryTrackStationAudioBlobKey = secondaryTrackStationAudio != null ?
            			secondaryTrackStationAudio.getStationAudioMultimediaContent() :
            				new BlobKey("");
            	BlobKey secondaryTrackMusicFileBlobKey = secondaryTrackMusicFile != null ?
            			secondaryTrackMusicFile.getMusicFileFile() :
            				new BlobKey("");
        		SecondaryTrackSimple secondaryTrackSimple = new SecondaryTrackSimple(
        				KeyFactory.keyToString(secondaryTrack.getKey()),
        				secondaryTrack.getSecondaryTrackType().toString(),
        				secondaryTrack.getStationAudio() != null ?
        						KeyFactory.keyToString(secondaryTrack.getStationAudio()) :
        							"",
        				secondaryTrackStationAudioBlobKey,
        				secondaryTrackStationAudio != null ?
        						secondaryTrackStationAudio.getStationAudioName() :
            						"",
            			secondaryTrackStationAudio != null ?
            					secondaryTrackStationAudio.getStationAudioFormat() :
                    				"",
                    	secondaryTrackStationAudio != null ?
                            	secondaryTrackStationAudio.getStationAudioDuration() :
                            		0,
            			secondaryTrack.getMusicFile() != null ?
            					secondaryTrack.getMusicFile() : 0,
            			secondaryTrackMusicFileBlobKey,
            			secondaryTrackMusicFile != null ?
            					mainTrackMusicFile.getMusicFileTitle() :
            						"",
            			secondaryTrackMusicFile != null ?
            	        		mainTrackMusicFile.getMusicFileFormat() :
            	        			"",
            	        secondaryTrackMusicFile != null ?
            	        	    mainTrackMusicFile.getMusicFileDuration():
            	        	        0,
        				secondaryTrack.getSecondaryTrackStartingTime(),
        				secondaryTrack.getSecondaryTrackDuration(),
        				secondaryTrack.getSecondaryTrackFadeInSteps(),
        				secondaryTrack.getSecondaryTrackFadeInDuration(),
        				secondaryTrack.getSecondaryTrackFadeInPercentage(),
        				secondaryTrack.getSecondaryTrackFadeOutSteps(),
        				secondaryTrack.getSecondaryTrackFadeOutDuration(),
        				secondaryTrack.getSecondaryTrackFadeOutPercentage(),
        				secondaryTrack.getSecondaryTrackOffset()
        				);
        		
        		secondaryTracksSimple.add(secondaryTrackSimple);
        	}
        	
        	// Create slides
        	List<Slide> slides = 
        			SlideManager.getAllSlidesFromProgram(program.getKey(), true);
        	ArrayList<SlideSimple> slidesSimple = new ArrayList<SlideSimple>();
        	for (Slide slide : slides) {
            	StationImage stationImage = 
            			StationImageManager.getStationImage(slide.getStationImage());
            	BlobKey stationImageBlobKey = stationImage != null ?
            			stationImage.getStationImageMultimediaContent() :
            				new BlobKey("");
        		SlideSimple slideSimple = new SlideSimple(
        				KeyFactory.keyToString(slide.getKey()),
        				KeyFactory.keyToString(slide.getStationImage()),
        				stationImageBlobKey,
        				slide.getSlideStartingTime(),
        				10.0
        				);
        		
        		slidesSimple.add(slideSimple);
        	}
        	
        	// Look for station logo
        	Station station = 
        			StationManager.getStation(
        					program.getKey().getParent().getParent());
        	BlobKey stationLogo = station.getStationLogo();
        	
        	ProgramSimple programSimple = new ProgramSimple(
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
        			DateManager.printDateAsString(
        					program.getProgramStartingDate()),
        			DateManager.printDateAsString(
        					program.getProgramEndingDate()),
        			stationLogo != null ? stationLogo : new BlobKey(""),
        			mainTrackSimple,
        			secondaryTracksSimple,
        			slidesSimple
        			);
        	
        	programListSimple.add(programSimple);
        }
        
        return programListSimple;
    }

}