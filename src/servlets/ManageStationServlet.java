/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.BlobUtils;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Channel;
import datastore.ChannelManager;
import datastore.MainTrack;
import datastore.MainTrack.MainTrackType;
import datastore.MainTrackManager;
import datastore.Program;
import datastore.ProgramManager;
import datastore.SecondaryTrack;
import datastore.SecondaryTrack.SecondaryTrackType;
import datastore.SecondaryTrackManager;
import datastore.Slide;
import datastore.SlideManager;
import datastore.StationAudio;
import datastore.StationAudioManager;
import datastore.StationImage;
import datastore.StationImageManager;
import datastore.StationManager;
import datastore.SystemManager;
import datastore.User;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.ReferentialIntegrityException;

/**
 * This servlet class is used to add, delete and update
 * different objects related to stations.
 * 
 */

@SuppressWarnings("serial")
public class ManageStationServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageStationServlet.class.getName());
    
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
    
    // JSP file locations
    private static final String addChannelJSP = "/station/addChannel.jsp";
    private static final String addStationAudioJSP = "/station/addStationAudio.jsp";
    private static final String addStationImageJSP = "/station/addStationImage.jsp";
    private static final String editChannelJSP = "/station/editChannel.jsp";
    private static final String editProgramJSP = "/station/editProgram.jsp";
    private static final String editStationAudioJSP = "/station/editStationAudio.jsp";
    private static final String editStationImageJSP = "/station/editStationImage.jsp";
    private static final String listChannelJSP = "/station/listChannel.jsp";
    private static final String listStationAudioJSP = "/station/listStationAudio.jsp";
    private static final String listStationImageJSP = "/station/listStationImage.jsp";
    private static final String listProgramJSP = "/station/listProgram.jsp";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	
    	// Check that an actual user is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("delete")) {
        	
        	String type = req.getParameter("type");
        	
        	// Key of object to delete
        	Key key = KeyFactory.stringToKey(req.getParameter("k"));
        	
        	Key stationKey = null;
        	if (type.equalsIgnoreCase("channel") || 
        			type.equalsIgnoreCase("audio") || 
        			type.equalsIgnoreCase("image")) {
        		stationKey = key.getParent();
        	}
        	else if (type.equalsIgnoreCase("program")) {
        		stationKey = key.getParent().getParent();
        	}
        	String stationKeyString = KeyFactory.keyToString(stationKey);
        	
        	// Check the user type carrying out the action
        	String redirectURL = "";
        	String failURL = "?msg=fail&action=" + action;
        	if (user.getUserType() == User.Type.ADMINISTRATOR) {
        		redirectURL = "?s_key=" +
        				stationKeyString + "&msg=success&action=" + action;
        	}
        	else if (user.getUserType() == User.Type.STATION){
        		redirectURL = "?msg=success&action=" + action;
        	}
        	else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
        	}
        	
        	try {
	            // Channels
	            if (type.equalsIgnoreCase("channel")) {
	            	redirectURL = listChannelJSP + redirectURL;
	            	failURL = listChannelJSP + failURL;
	            	ChannelManager.deleteChannel(key);
	            	SystemManager.updateStationListVersion();
	            }
	            // Programs
	            else if (type.equalsIgnoreCase("program")) {
	            	redirectURL = listProgramJSP + redirectURL;
	            	failURL = listProgramJSP + failURL;
	            	ProgramManager.deleteProgram(key);
	            }
	            // Station Audios
	            else if (type.equalsIgnoreCase("audio")) {
	            	redirectURL = listStationAudioJSP + redirectURL;
	            	failURL = listStationAudioJSP + failURL;
	            	StationAudioManager.deleteStationAudio(key);          	
	            }
	            // Station Images
	            else if (type.equalsIgnoreCase("image")) {
	            	redirectURL = listStationImageJSP + redirectURL;
	            	failURL = listStationImageJSP + failURL;
	            	StationImageManager.deleteStationImage(key);
	            }
	            else {
	    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
	            }
        	}
        	catch (ReferentialIntegrityException rie) {
        		resp.sendRedirect(failURL + "&etype=ReferentialIntegrity");
        		return;
        	}
        	catch (Exception e) {
        		resp.sendRedirect(failURL + "&etype=ServerError");
        		return;
        	}
            
            // If success
            resp.sendRedirect(redirectURL);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	// Check that an actual user is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
    	// Check the user type carrying out the action
    	String stationKeyString;
    	Key stationKey;
    	if (user.getUserType() == User.Type.ADMINISTRATOR) {
    		stationKeyString = req.getParameter("s_key");
    		stationKey = KeyFactory.stringToKey(stationKeyString);
    	}
    	else if (user.getUserType() == User.Type.STATION) {
    		stationKey = StationManager.getStation(user).getKey();
    		stationKeyString = KeyFactory.keyToString(stationKey);
    	}
    	else {
    		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
    	}
    	
        // Let's check the action and type required by the jsp
        String action = req.getParameter("action");
        String type = req.getParameter("type");
        String successURL = "";
        String failURL = "";
        BlobKey blobKey = null;

        if (action.equals("add")) {
        	
        	String keepAddingString = req.getParameter("keep_adding");
        	boolean keepAdding = keepAddingString == null ? false : true;
        	
        	try {
	            // Channels
	            if (type.equalsIgnoreCase("channel")) {
	                String channelName = req.getParameter("c_name");
	                
	                String channelNumberString = req.getParameter("c_number");
	                Integer channelNumber = null;
	                if (!channelNumberString.isEmpty()) {
	                	channelNumber = Integer.parseInt(channelNumberString);
	                }
	       
	                successURL = keepAdding ? addChannelJSP : listChannelJSP;
	                failURL = addChannelJSP;
	                
	                Channel channel = new Channel(
	                        channelName,
	                        channelNumber);
	                ChannelManager.putChannel(stationKey, channel);
	                SystemManager.updateStationListVersion();
	            }
	            // Station Audio
	            else if (type.equalsIgnoreCase("audio")) {
	            	String stationAudioTypeString = req.getParameter("a_type");
	            	StationAudio.StationAudioType stationAudioType =
	            			StationAudio.getStationAudioTypeFromString(stationAudioTypeString);
	            	
	            	String stationAudioName = req.getParameter("a_name");
	            	blobKey = BlobUtils.assignBlobKey(req, "file", blobstoreService);
	            	
	            	Double stationAudioDuration = null;
	            	String stationAudioDurationString = req.getParameter("a_duration");
	            	if (!stationAudioDurationString.isEmpty()) {
	            		stationAudioDuration = Double.parseDouble(stationAudioDurationString);
	            	}
	            	
	            	String fileFormat = null;
	            	BlobInfoFactory bif = new BlobInfoFactory();
			        if (blobKey != null) {
			        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
	        			fileFormat = 
	        					fileName.substring(fileName.lastIndexOf('.') + 1);
	        		}
			          	
	            	successURL = keepAdding ? addStationAudioJSP : listStationAudioJSP;
	                failURL = addStationAudioJSP;
	            	
	            	StationAudio stationAudio = new StationAudio(
	            			stationAudioType,
	            			stationAudioName,
	            			blobKey,
	            			stationAudioDuration,
	            			fileFormat
	            			);
	            	StationAudioManager.putStationAudio(stationKey, stationAudio);
	            }
	            // Station Images
	            else if (type.equalsIgnoreCase("image")) {
	            	String stationImageName = req.getParameter("i_name");
	            	blobKey = BlobUtils.assignBlobKey(req, "file", blobstoreService);
	            	String fileFormat = null;
	            	
	            	BlobInfoFactory bif = new BlobInfoFactory();
			        if (blobKey != null) {
			        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
	        			fileFormat = 
	        					fileName.substring(fileName.lastIndexOf('.') + 1);
	        		}
	            	
	            	successURL = keepAdding ? addStationImageJSP : listStationImageJSP;
	                failURL = addStationImageJSP;
	            	
	            	StationImage stationImage = new StationImage(
	            			stationImageName,
	            			blobKey,
	            			fileFormat
	            			);
	            	StationImageManager.putStationImage(stationKey, stationImage);
	            }
	            else {
	    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
	            }
        	}
            catch (MissingRequiredFieldsException mrfe) {
            	if (blobKey != null) {
            		blobstoreService.delete(blobKey);
            	}
            	resp.sendRedirect(failURL + "?etype=MissingInfo");
                return;
            }
        	catch (ObjectExistsInDatastoreException oeide) {
            	if (blobKey != null) {
            		blobstoreService.delete(blobKey);
            	}
            	resp.sendRedirect(failURL + "?etype=ObjectExists");
                return;
        	}
            catch (Exception ex) {
            	if (blobKey != null) {
            		blobstoreService.delete(blobKey);
            	}
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        	
        	// Success
        	resp.sendRedirect(successURL+ "?s_key=" + 
    				stationKeyString + "&msg=success&action=" + action);
        }
        else if (action.equals("update")) {

        	String objectKeyString = req.getParameter("k");
        	Key objectKey = KeyFactory.stringToKey(objectKeyString);
        	boolean sameBlob = true;
        	
        	try {
        		// Channels
                if (type.equalsIgnoreCase("channel")) {
                    
                    String channelName = req.getParameter("c_name");

                    String channelNumberString = req.getParameter("c_number");
                    Integer channelNumber = null;
                    if (!channelNumberString.isEmpty()) {
                    	channelNumber = Integer.parseInt(channelNumberString);
                    }
                    
                    successURL = editChannelJSP;
                    failURL = editChannelJSP;
                    
                    ChannelManager.updateChannelAttributes(
                    		objectKey, 
                    		channelName,
                    		channelNumber);
                    SystemManager.updateStationListVersion();
                }
                // Programs
                else if (type.equalsIgnoreCase("program")) {
                	
                	successURL = editProgramJSP;
	                failURL = editProgramJSP;
	                
	                Program program = ProgramManager.getProgram(objectKey);
                	
                	String programName = req.getParameter("p_name");
                	String programDescription = req.getParameter("p_description");
                	String programBanner = req.getParameter("p_banner");
                	
                	String programSequenceNumberString = req.getParameter("p_sequence_n");
                	Integer programSequenceNumber = null;
                	if (!programSequenceNumberString.isEmpty()) {
                		programSequenceNumber = Integer.parseInt(programSequenceNumberString);
                	}
                	
                	String programTotalDurationTimeString = req.getParameter("p_duration");
                	Double programTotalDurationTime = null;
                	if (!programTotalDurationTimeString.isEmpty()) {
                		programTotalDurationTime = 
                				Double.parseDouble(programTotalDurationTimeString);
                	}
                	
                	String programOverlapDurationString = req.getParameter("p_o_duration");
                	Double programOverlapDuration = null;
                	if (!programOverlapDurationString.isEmpty()) {
                		programOverlapDuration = 
                				Double.parseDouble(programOverlapDurationString);
                	}
                	
                	Date programStartingDate = program.getProgramStartingDate();
                	Date programEndingDate = program.getProgramEndingDate();
                	
                	ProgramManager.updateProgramAttributes(
                			objectKey, 
                			programName, 
                			programDescription, 
                			programBanner, 
                			programSequenceNumber, 
                			programTotalDurationTime,
                			programOverlapDuration,
                			programStartingDate, 
                			programEndingDate);
                	
                	// Main Track
                	String mainTrackKeyString = req.getParameter("mt_key");
                	Key mainTrackKey = KeyFactory.stringToKey(mainTrackKeyString);
                	MainTrack mainTrack = MainTrackManager.getMainTrack(mainTrackKey);
                	
                	String mainTrackTypeString = req.getParameter("mt_type");
                	MainTrackType mainTrackType = 
                			MainTrack.getMainTrackTypeFromString(mainTrackTypeString);
                	
                	Key mainTrackStationAudioKey = mainTrack.getStationAudio();
                	Long mainTrackMusicFileKey = mainTrack.getMusicFile();
                	Key mainTrackPlaylistKey = mainTrack.getPlaylist();
                	
                	String mainTrackDurationString = req.getParameter("mt_duration");
                	Double mainTrackDuration = null;
                	if (!mainTrackDurationString.isEmpty()) {
                		mainTrackDuration = Double.parseDouble(mainTrackDurationString);
                	}
                	
                	String mainTrackFadeOutStepsString = 
                			req.getParameter("mt_fadeoutsteps");
                	Integer mainTrackFadeOutSteps = null;
                	if (!mainTrackFadeOutStepsString.isEmpty()) {
                		mainTrackFadeOutSteps = 
                				Integer.parseInt(mainTrackFadeOutStepsString);
                	}

                	String mainTrackFadeOutDurationString = 
                			req.getParameter("mt_fadeoutduration");
                	Double mainTrackFadeOutDuration = null;
                	if (!mainTrackFadeOutDurationString.isEmpty()) {
                		mainTrackFadeOutDuration = 
                				Double.parseDouble(mainTrackFadeOutDurationString);
                	}
                	
                	String mainTrackFadeOutPercentageString = 
                			req.getParameter("mt_fadeoutpercentage");
                	Double mainTrackFadeOutPercentage = null;
                	if (!mainTrackFadeOutPercentageString.isEmpty()) {
                		mainTrackFadeOutPercentage = 
                				Double.parseDouble(mainTrackFadeOutPercentageString);
                	}
                	
                	String mainTrackFadeInStepsString = 
                			req.getParameter("mt_fadeinsteps");
                	Integer mainTrackFadeInSteps = null;
                	if (!mainTrackFadeInStepsString.isEmpty()) {
                		mainTrackFadeInSteps = 
                				Integer.parseInt(mainTrackFadeInStepsString);
                	}

                	String mainTrackFadeInDurationString = 
                			req.getParameter("mt_fadeinduration");
                	Double mainTrackFadeInDuration = null;
                	if (!mainTrackFadeInDurationString.isEmpty()) {
                		mainTrackFadeInDuration = 
                				Double.parseDouble(mainTrackFadeInDurationString);
                	}
                	
                	String mainTrackFadeInPercentageString = 
                			req.getParameter("mt_fadeinpercentage");
                	Double mainTrackFadeInPercentage = null;
                	if (!mainTrackFadeInPercentageString.isEmpty()) {
                		mainTrackFadeInPercentage = 
                				Double.parseDouble(mainTrackFadeInPercentageString);
                	}
                	
                	MainTrackManager.updateMainTrackAttributes(
                			mainTrackKey, 
                			mainTrackType, 
                			mainTrackStationAudioKey, 
                			mainTrackMusicFileKey, 
                			mainTrackPlaylistKey, 
                			mainTrackDuration, 
                			mainTrackFadeOutSteps, 
                			mainTrackFadeOutDuration, 
                			mainTrackFadeOutPercentage,
                			mainTrackFadeInSteps, 
                			mainTrackFadeInDuration, 
                			mainTrackFadeInPercentage);
                	
                	// Secondary Tracks
                	int secondaryTrackCount = Integer.parseInt(req.getParameter("st_count"));
                	for (int i = 0; i < secondaryTrackCount; i++) {
                		String secondaryTrackKeyString = req.getParameter("st_key" + i);
                		Key secondaryTrackKey = 
                				KeyFactory.stringToKey(secondaryTrackKeyString);
                		SecondaryTrack secondaryTrack = 
                				SecondaryTrackManager.getSecondaryTrack(secondaryTrackKey);
                		
                		String secondaryTrackTypeString =
                				req.getParameter("st_type" + i);
                		SecondaryTrackType secondaryTrackType =
                				SecondaryTrack.getSecondaryTrackTypeFromString(secondaryTrackTypeString);
                		
                		Key SecondaryTrackStationAudioKey = secondaryTrack.getStationAudio();
                		Long secondaryTrackMusicFileKey = secondaryTrack.getMusicFile();
                		
                		String secondaryTrackStartingTimeString = 
                				req.getParameter("st_stime" + i);
                		Double secondaryTrackStartingTime = null;
                		if (!secondaryTrackStartingTimeString.isEmpty()) {
                			secondaryTrackStartingTime = 
                					Double.parseDouble(secondaryTrackStartingTimeString);
                		}
                		
                		String secondaryTrackDurationString = 
                				req.getParameter("st_duration" + i);
                		Double secondaryTrackDuration = null;
                		if (!secondaryTrackDurationString.isEmpty()) {
                			secondaryTrackDuration = 
                					Double.parseDouble(secondaryTrackDurationString);
                		}
                		
                		String secondaryTrackFadeInStepsString = 
                				req.getParameter("st_fadeinsteps" + i);
                		Integer secondaryTrackFadeInSteps = null;
                		if (!secondaryTrackFadeInStepsString.isEmpty()) {
                			secondaryTrackFadeInSteps = 
                					Integer.parseInt(secondaryTrackFadeInStepsString);
                		}
                		
                		String secondaryTrackFadeInDurationString = 
                				req.getParameter("st_fadeinduration" + i);
                		Double secondaryTrackFadeInDuration = null;
                		if (!secondaryTrackFadeInDurationString.isEmpty()) {
                			secondaryTrackFadeInDuration = 
                					Double.parseDouble(secondaryTrackFadeInDurationString);
                		}
                		
                		String secondaryTrackFadeInPercentageString = 
                				req.getParameter("st_fadeinpercentage" + i);
                		Double secondaryTrackFadeInPercentage = null;
                		if (!secondaryTrackFadeInPercentageString.isEmpty()) {
                			secondaryTrackFadeInPercentage = 
                					Double.parseDouble(secondaryTrackFadeInPercentageString);
                		}
                		
                		String secondaryTrackFadeOutStepsString = 
                				req.getParameter("st_fadeoutsteps" + i);
                		Integer secondaryTrackFadeOutSteps = null;
                		if (!secondaryTrackFadeOutStepsString.isEmpty()) {
                			secondaryTrackFadeOutSteps = 
                					Integer.parseInt(secondaryTrackFadeOutStepsString);
                		}
                		
                		String secondaryTrackFadeOutDurationString = 
                				req.getParameter("st_fadeoutduration" + i);
                		Double secondaryTrackFadeOutDuration = null;
                		if (!secondaryTrackFadeOutDurationString.isEmpty()) {
                			secondaryTrackFadeOutDuration = 
                					Double.parseDouble(secondaryTrackFadeOutDurationString);
                		}
                		
                		String secondaryTrackFadeOutPercentageString = 
                				req.getParameter("st_fadeoutpercentage" + i);
                		Double secondaryTrackFadeOutPercentage = null;
                		if (!secondaryTrackFadeOutPercentageString.isEmpty()) {
                			secondaryTrackFadeOutPercentage = 
                					Double.parseDouble(secondaryTrackFadeOutPercentageString);
                		}
                		
                		String secondaryTrackOffsetString = 
                				req.getParameter("st_offset" + i);
                		Double secondaryTrackOffset = null;
                		if (!secondaryTrackOffsetString.isEmpty()) {
                			secondaryTrackOffset= 
                					Double.parseDouble(secondaryTrackOffsetString);
                		}
                		
                		SecondaryTrackManager.updateSecondaryTrackAttributes(
                				secondaryTrackKey, 
                				secondaryTrackType, 
                				SecondaryTrackStationAudioKey, 
                				secondaryTrackMusicFileKey, 
                				secondaryTrackStartingTime, 
                				secondaryTrackDuration, 
                				secondaryTrackFadeInSteps, 
                				secondaryTrackFadeInDuration, 
                				secondaryTrackFadeInPercentage, 
                				secondaryTrackFadeOutSteps, 
                				secondaryTrackFadeOutDuration, 
                				secondaryTrackFadeOutPercentage, 
                				secondaryTrackOffset);
                	}
                	
                	// Slides
                	int slideCount = Integer.parseInt(req.getParameter("slide_count"));
                	for (int i = 0; i < slideCount; i++) {
                		String slideKeyString = req.getParameter("slide_key" + i);
                		Key slideKey = 
                				KeyFactory.stringToKey(slideKeyString);
                		Slide slide = 
                				SlideManager.getSlide(slideKey);
                		
                		Key stationImageKey = slide.getStationImage();
                		
                		String slideStartingTimeString = req.getParameter("slide_stime" + i);
                		Double slideStartingTime = null;
                		if (!slideStartingTimeString.isEmpty()) {
                			slideStartingTime = 
                					Double.parseDouble(slideStartingTimeString);
                		}
                		
                		SlideManager.updateSlideAttributes(
                				slideKey, 
                				stationImageKey, 
                				slideStartingTime);
                		
                	}
                }
                // Station Audios
	            else if (type.equalsIgnoreCase("audio")) {
	            	StationAudio stationAudio = StationAudioManager.getStationAudio(objectKey);
	            	
	            	String stationAudioTypeString = req.getParameter("a_type");
	            	StationAudio.StationAudioType stationAudioType =
	            			StationAudio.getStationAudioTypeFromString(stationAudioTypeString);
	            	
	            	String stationAudioName = req.getParameter("a_name");
	            	
	            	blobKey = BlobUtils.assignBlobKey(req, "file", blobstoreService);
                    sameBlob = false;
                    if (blobKey == null) {
                    	blobKey = stationAudio.getStationAudioMultimediaContent();
                    	sameBlob = true;
                    }
                    
                    Double stationAudioDuration = null;
	            	String stationAudioDurationString = req.getParameter("a_duration");
	            	if (!stationAudioDurationString.isEmpty()) {
	            		stationAudioDuration = Double.parseDouble(stationAudioDurationString);
	            	}
                    
                    String fileFormat = null;
                    BlobInfoFactory bif = new BlobInfoFactory();
			        if (blobKey != null) {
			        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
	        			fileFormat = 
	        					fileName.substring(fileName.lastIndexOf('.') + 1);
	        		}
	            	
			        String stationAudioFormat = req.getParameter("a_format");
			        if (!stationAudioFormat.isEmpty()) {
			        	fileFormat = stationAudioFormat;
			        }
			        
	            	successURL = editStationAudioJSP;
	                failURL = editStationAudioJSP;
	            	
	            	StationAudioManager.updateStationAudioAttributes(
	            			objectKey, 
	            			stationAudioType, 
	            			stationAudioName, 
	            			blobKey,
	            			stationAudioDuration,
	            			fileFormat);
	            }
	            // Station Images
	            else if (type.equalsIgnoreCase("image")) {
	            	StationImage stationImage = StationImageManager.getStationImage(objectKey);
	            	
	            	String stationImageName = req.getParameter("i_name");
	            	
	            	blobKey = BlobUtils.assignBlobKey(req, "file", blobstoreService);
                    sameBlob = false;
                    if (blobKey == null) {
                    	blobKey = stationImage.getStationImageMultimediaContent();
                    	sameBlob = true;
                    }
                    
                    String fileFormat = null;
                    BlobInfoFactory bif = new BlobInfoFactory();
			        if (blobKey != null) {
			        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
	        			fileFormat = 
	        					fileName.substring(fileName.lastIndexOf('.') + 1);
	        		}
			        
			        String stationImageFormat = req.getParameter("i_format");
			        if (!stationImageFormat.isEmpty()) {
			        	fileFormat = stationImageFormat;
			        }
	            	
	            	successURL = editStationImageJSP;
	                failURL = editStationImageJSP;
	            	
	            	StationImageManager.updateStationImageAttributes(
	            			objectKey, 
	            			stationImageName, 
	            			blobKey,
	            			fileFormat);
	            }
                else {
	    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
                }
        	}
            catch (MissingRequiredFieldsException mrfe) {
            	if (!sameBlob) {
            		blobstoreService.delete(blobKey);
            	}
            	resp.sendRedirect(failURL + "?etype=MissingInfo&k=" +
            			objectKeyString);
                return;
            }
        	catch (ObjectExistsInDatastoreException oeide) {
            	if (!sameBlob) {
            		blobstoreService.delete(blobKey);
            	}
            	resp.sendRedirect(failURL + "?etype=ObjectExists&k=" +
            			objectKeyString);
                return;
        	}
            catch (Exception ex) {
            	if (!sameBlob) {
            		blobstoreService.delete(blobKey);
            	}
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        	
        	// Success
            resp.sendRedirect(successURL + "?k=" + objectKeyString +
            		"&readonly=true&msg=success&action=" + action);
        }
        else {
	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
            		"Invalid action.");
        }
    }
}
