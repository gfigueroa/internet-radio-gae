/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
 */

package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

import datastore.Channel;
import datastore.ChannelManager;
import datastore.MainTrack;
import datastore.MainTrackManager;
import datastore.Playlist;
import datastore.PlaylistManager;
import datastore.PlaylistMusicFile;
import datastore.Program;
import datastore.ProgramManager;
import datastore.SecondaryTrack;
import datastore.SecondaryTrackManager;
import datastore.Slide;
import datastore.SlideManager;
import datastore.Station;
import datastore.StationAudio;
import datastore.StationAudioManager;
import datastore.StationManager;
import datastore.User;
import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UnauthorizedUserOperationException;

/**
 * This servlet is used to manage upload requests from Stations.
 * 
 */
@SuppressWarnings("serial")
public class StationUploadServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(StationUploadServlet.class
			.getCanonicalName());
	private static final long MAX_FILE_SIZE = 10485760;

    // JSP file locations
    private static final String thisServlet = "/stationUpload";
    
    /**
     * Respond to servlet GET requests
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
	    // Lets check the action required by the jsp
	    String message = req.getParameter("msg");
	
	    if (message.equals("success")) {
	    	resp.getWriter().println("success");
	    }
	    else if (message.equals("failure")){
	    	resp.getWriter().println("failure");
	    }
	}
    
	/**
	 * Respond to servlet POST requests
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        
        // Let's check the action and type required by the station
        String action = req.getParameter("action");
        String type = req.getParameter("type");
		
        if (action.equalsIgnoreCase("add")) {
        	carryOutAddOrEdit_File(req, resp, action, type);
        }
        else if (action.equalsIgnoreCase("addJson") || 
        		action.equalsIgnoreCase("edit")) {
			carryOutAddOrEdit_Object(req, resp, action, type);
        }
        else if (action.equalsIgnoreCase("delete")) {
        	carryOutDelete(req, resp, type);
        }
        else {
	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
	            		"Invalid action.");
        }
	}
	
	/**
	 * Carry out an add or an edit of a Json OBJECT
	 * @param req
	 * @param resp
	 * @param action
	 * @param type
	 * @throws IOException 
	 * @throws JSONException
	 */
	private void carryOutAddOrEdit_Object(HttpServletRequest req, 
			HttpServletResponse resp, String action, String type) 
					throws IOException {
		
    	// Check that a station is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        Station station = null;
        if (user == null) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (user.getUserType() != User.Type.STATION) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        else {
        	station = StationManager.getStation(user.getUserEmail());
        }
		
		try {
			JSONObject jsonObject = new JSONObject(req.getParameter("jsonobject"));
			Iterator<String> iterator = jsonObject.keys();

	    	// Check action
	    	if (action.equalsIgnoreCase("addJson")) {
	    		// Check type
	    		if (type.equalsIgnoreCase("program")) {
	    			addProgram_Object(jsonObject, iterator, station);
	    		}
	    		else {
	    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
	    		}
	    	}
	    	else if (action.equalsIgnoreCase("edit")) {
	    		// Check type
	    		if (type.equalsIgnoreCase("program")) {
	    			editProgram_Object(jsonObject, iterator, station);
	    		}
	    		else if (type.equalsIgnoreCase("audio")) {
	    			//editStationAudio_File(reader);
	    		}
	    		else {
	    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
	    		}
	    	}
	    	else {
    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
	            		"Invalid action.");
    	    	return;
    		}

	    	String redirectTo = req.getParameter("redirectTo");
	    	if (redirectTo != null && redirectTo.equals("listProgram")) {
	    		resp.sendRedirect("/station/listProgram.jsp?msg=success&action=" + action);
	    	}
	    	else {
	    		resp.sendRedirect(thisServlet + "?msg=success&action=" + action);
	    	}
		}
        catch (MissingRequiredFieldsException mrfe) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            		"One or more required fields are missing.");
        }
		catch (UnauthorizedUserOperationException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
		catch (JSONException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
        catch (Exception e) {
            log.log(Level.SEVERE, e.toString());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
           		"Internal server error.");
        }
	}
	
	/**
	 * Carry out an add or an edit of a Json FILE.
	 */
	private void carryOutAddOrEdit_File(HttpServletRequest req, 
			HttpServletResponse resp, String action, String type)
			throws ServletException, IOException {
		
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (!isMultipart) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            		"No file upload request found.");
            return;
		}
		
        // Set up file upload
		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(MAX_FILE_SIZE);
		
        // Start iterating upload request
        JsonReader reader = null;
		try {
			FileItemIterator iter = upload.getItemIterator(req);
			InputStream inputStream = null;
			// Upload request only has one file
			if (iter.hasNext()) {			
				FileItemStream item = iter.next();
				// If field is a file
				if (!item.isFormField()) {
                	inputStream = item.openStream();
                }
				// If field is a form field
				else {
					resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
		            		"Only file accepted, no form parameters.");
		            return;
				}
			}
			
	        // Open file
			BufferedReader br = new BufferedReader( 
                    new InputStreamReader(inputStream, "UTF-8"));
	        reader = new JsonReader(br);
	    	reader.beginObject();
	    	
	        // Check if user and password are correct
			Email userEmail = null;
			String userPassword = "";
			while (reader.hasNext()) {
	    		String name = reader.nextName();
	    		if (name.equals("userEmail")) {
	    			String userEmailString  = reader.nextString();
	    			userEmail = new Email(userEmailString);
	    		}
	    		else if (name.equals("userPassword")) {
	    			userPassword = reader.nextString();
	    			break;
	    		}
			}
	        Station station = StationManager.getStation(userEmail);
	        if (!station.getUser().validateUser(userEmail, userPassword)) {
	            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
	                	"User e-mail and password don't match.");
	            return;
	        }

	    	// Check action
	    	if (action.equalsIgnoreCase("add")) {
	    		// Check type
	    		if (type.equalsIgnoreCase("program")) {
	    			addProgram_File(reader, station);
	    		}
	    		else if (type.equalsIgnoreCase("playlist")) {
	    			addPlaylist_File(reader, station);
	    		}
	    		else if (type.equalsIgnoreCase("channel")) {
	    			addChannel_File(reader, station);
	    		}
	    		else {
	    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
	    		}
	    	}
	    	else if (action.equalsIgnoreCase("edit")) {
	    		// Check type
	    		if (type.equalsIgnoreCase("audio")) {
	    			editStationAudio_File(reader);
	    		}
	    	}

	    	String redirectTo = req.getParameter("redirectTo");
	    	if (redirectTo != null && redirectTo.equals("listProgram")) {
	    		resp.sendRedirect("/station/listProgram.jsp?msg=success&action=" + action);
	    	}
	    	else {
	    		resp.sendRedirect(thisServlet + "?msg=success&action=" + action);
	    	}
		}
        catch (MissingRequiredFieldsException mrfe) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            		"One or more required fields are missing.");
        }
		catch (MalformedJsonException e) {
			log.log(Level.SEVERE, "Could not save file", e);
			String reason = "Json file is not well formed. " + e.getMessage();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		reason);
		}
        catch (IOException e) {
            log.log(Level.SEVERE, "Could not save file", e);
            
            Throwable cause = e.getCause();
            String reason;
            if (cause instanceof FileSizeLimitExceededException) {
            	reason = "File too large";
            	log.log(Level.SEVERE, reason, e);
            }
            else {
            	reason = "Technical error";
            	log.log(Level.SEVERE, reason, e);
            }
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		reason);
        } 
		catch (FileUploadException e) {
			log.log(Level.SEVERE, "Could not save file", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could not save file");
		}
		catch (UnauthorizedUserOperationException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
        catch (Exception e) {
            log.log(Level.SEVERE, e.toString());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		"Internal server error.");
        }
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	/**
	 * Carry out a delete.
	 */
	private void carryOutDelete(HttpServletRequest req, HttpServletResponse resp,
			String type)
			throws ServletException, IOException {
		
		// Check user email and password
		String userEmailString = req.getParameter("userEmail");
		Email userEmail = new Email(userEmailString);
		String userPassword = req.getParameter("userPassword");
        Station station = StationManager.getStation(userEmail);
        if (!station.getUser().validateUser(userEmail, userPassword)) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                	"User e-mail and password don't match.");
            return;
        }
        
        // Check type
        String keyString = req.getParameter("key");
        Key key = KeyFactory.stringToKey(keyString);
        if (type.equalsIgnoreCase("program")) {
        	ProgramManager.deleteProgram(key);
        }
        else if (type.equalsIgnoreCase("playlist")) {
        	PlaylistManager.deletePlaylist(key);
        }
        else if (type.equalsIgnoreCase("channel")) {
        	ChannelManager.deleteChannel(key);
        }
		else {
	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
            		"Invalid type.");
	    	return;
		}
        
        resp.sendRedirect(thisServlet + "?msg=success&action=delete");
	}
	
	/**
	 * Add a program to a channel through a JSON Object.
	 * @param jsonObject
	 * 			: the JSONObject instance
	 * @param iterator
	 * 			: the string iterator of the JSON object
	 * @param station
	 * 			: the station where the program(s) will be added
	 * @throws IOException 
	 * @throws MissingRequiredFieldsException 
	 * @throws UnauthorizedUserOperationException
	 * @throws JSONException 
	 */
	private void addProgram_Object(JSONObject jsonObject, 
			Iterator<String> iterator, Station station) 
			throws IOException, MissingRequiredFieldsException,
			UnauthorizedUserOperationException, JSONException {
    	
		// Program attributes
		Key channelKey = null;
		String programName = null;
		String programDescription = null;
		String programBanner = null;
		Integer programSequenceNumber = null;
		Double programTotalDurationTime = null;
		Double programOverlapDuration = null;
		
		// Main track
		MainTrack mainTrack = null;
		
		// Secondary tracks
		ArrayList<SecondaryTrack> secondaryTracks = new ArrayList<>();
		
		// Slides
		ArrayList<Slide> slides = new ArrayList<>();
		
		try {
			while (iterator.hasNext()) {
			
				String fieldName = iterator.next();
				if (fieldName.equals("channelKey")) {
					String channelKeyString = jsonObject.getString(fieldName);
					channelKey = KeyFactory.stringToKey(channelKeyString);
					
					// Check if this channel belongs to this station
					if (!ChannelManager.channelBelongsToStation(
							channelKey, station.getKey())) {
			    		throw new UnauthorizedUserOperationException(
			    				station.getUser(), 
			    				"This channel does not belong to this station.");
					}
				}
				else if (fieldName.equals("programName")) {
					programName = jsonObject.getString(fieldName);
				}
				else if (fieldName.equals("programDescription")) {
					programDescription = jsonObject.getString(fieldName);
				}
				else if (fieldName.equals("programBanner")) {
					programBanner = jsonObject.getString(fieldName);
				}
				else if (fieldName.equals("programSequenceNumber")) {
					programSequenceNumber = jsonObject.getInt(fieldName);
				}
				else if (fieldName.equals("programTotalDurationTime")) {
					programTotalDurationTime = jsonObject.getDouble(fieldName);
				}
				else if (fieldName.equals("programOverlapDuration")) {
					programOverlapDuration = jsonObject.getDouble(fieldName);
				}
				// Main Track
				else if (fieldName.equals("mainTrack")) {
					
					// Main Track attributes
					MainTrack.MainTrackType mainTrackType = null;
					Key stationAudio = null;
					Long mainTrackMusicFileKey = null;
					Key mainTrackPlaylistKey = null;
					Double mainTrackDuration = null;
					Integer mainTrackFadeOutSteps = null;
					Double mainTrackFadeOutDuration = null;
					Double mainTrackFadeOutPercentage = null;
					Integer mainTrackFadeInSteps = null;
					Double mainTrackFadeInDuration = null;
					Double mainTrackFadeInPercentage = null;
					
					// Read object
					JSONObject mainTrackObject = jsonObject.getJSONObject(fieldName);
					Iterator<String> mainTrackIterator = 
							mainTrackObject.keys();
					while (mainTrackIterator.hasNext()) {
						fieldName = mainTrackIterator.next();
						if (fieldName.equals("mainTrackType")) {
							String mainTrackTypeString = 
									mainTrackObject.getString(fieldName);
							mainTrackType = 
									MainTrack.getMainTrackTypeFromString(
											mainTrackTypeString);
						}
						else if (fieldName.equals("stationAudio")){
							String stationAudioString = mainTrackObject.getString(fieldName);
							if (stationAudioString != null && !stationAudioString.isEmpty()) {
								stationAudio = KeyFactory.stringToKey(stationAudioString);
							}
						}
						else if (fieldName.equals("mainTrackMusicFileKey")){
							mainTrackMusicFileKey = mainTrackObject.getLong(fieldName);
						}
						else if (fieldName.equals("mainTrackPlaylistKey")){
							String mainTrackPlayListKeyString = 
									mainTrackObject.getString(fieldName);
							if (mainTrackPlayListKeyString != null && 
									!mainTrackPlayListKeyString.isEmpty()) {
								mainTrackPlaylistKey = 
										KeyFactory.stringToKey(mainTrackPlayListKeyString);
							}
						}
						else if (fieldName.equals("mainTrackDuration")){
							mainTrackDuration = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeInSteps")){
							mainTrackFadeInSteps = mainTrackObject.getInt(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeInDuration")){
							mainTrackFadeInDuration = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeInPercentage")){
							mainTrackFadeInPercentage = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeOutSteps")){
							mainTrackFadeOutSteps = mainTrackObject.getInt(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeOutDuration")){
							mainTrackFadeOutDuration = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeOutPercentage")){
							mainTrackFadeOutPercentage = mainTrackObject.getDouble(fieldName);
						}
					}
	
					mainTrack = new MainTrack(
							mainTrackType,
							stationAudio,
							mainTrackMusicFileKey,
							mainTrackPlaylistKey,
							mainTrackDuration,
							mainTrackFadeInSteps,
							mainTrackFadeInDuration,
							mainTrackFadeInPercentage,
							mainTrackFadeOutSteps,
							mainTrackFadeOutDuration,
							mainTrackFadeOutPercentage
							);
				}
				// Secondary Tracks
				else if (fieldName.equals("secondaryTracks")) {
	
					// Read array
					JSONArray secondaryTrackArray = 
							jsonObject.getJSONArray(fieldName);

					for (int i = 0; i < secondaryTrackArray.length(); i++) {
						JSONObject secondaryTrackObject = 
								secondaryTrackArray.getJSONObject(i);
						Iterator<String> secondaryTrackIterator = 
								secondaryTrackObject.keys();
	
						SecondaryTrack.SecondaryTrackType secondaryTrackType = null;
						Key stationAudio = null;
						Long secondaryTrackMusicFileKey = null;
						Double secondaryTrackStartingTime = null;
						Double secondaryTrackDuration = null;
						Integer secondaryTrackFadeInSteps = null;
						Double secondaryTrackFadeInDuration = null;
						Double secondaryTrackFadeInPercentage = null;
						Integer secondaryTrackFadeOutSteps = null;
						Double secondaryTrackFadeOutDuration = null;
						Double secondaryTrackFadeOutPercentage = null;
						Double secondaryTrackOffset = null;

						while (secondaryTrackIterator.hasNext()) {
							fieldName = secondaryTrackIterator.next();
							if (fieldName.equals("secondaryTrackType")) {
								String secondaryTrackTypeString = 
										secondaryTrackObject.getString(fieldName);
								secondaryTrackType = 
										SecondaryTrack.getSecondaryTrackTypeFromString(
												secondaryTrackTypeString);
							}
							else if (fieldName.equals("stationAudio")) {
								String stationAudioString = 
										secondaryTrackObject.getString(fieldName);
								if (stationAudioString != null && !stationAudioString.isEmpty()) {
									stationAudio = KeyFactory.stringToKey(stationAudioString);
								}
							}
							else if (fieldName.equals("secondaryTrackMusicFileKey")) {
								secondaryTrackMusicFileKey = 
										secondaryTrackObject.getLong(fieldName);
							}
							else if (fieldName.equals("secondaryTrackStartingTime")) {
								secondaryTrackStartingTime = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackDuration")) {
								secondaryTrackDuration = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeInSteps")) {
								secondaryTrackFadeInSteps = 
										secondaryTrackObject.getInt(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeInDuration")) {
								secondaryTrackFadeInDuration = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeInPercentage")) {
								secondaryTrackFadeInPercentage = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeOutSteps")) {
								secondaryTrackFadeOutSteps =
										secondaryTrackObject.getInt(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeOutDuration")) {
								secondaryTrackFadeOutDuration = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeOutPercentage")) {
								secondaryTrackFadeOutPercentage = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackOffset")) {
								secondaryTrackOffset = 
										secondaryTrackObject.getDouble(fieldName);
							}
						}
						
						SecondaryTrack secondaryTrack = new SecondaryTrack(
								secondaryTrackType,
								stationAudio,
								secondaryTrackMusicFileKey,
								secondaryTrackStartingTime,
								secondaryTrackDuration,
								secondaryTrackFadeInSteps,
								secondaryTrackFadeInDuration,
								secondaryTrackFadeInPercentage,
								secondaryTrackFadeOutSteps,
								secondaryTrackFadeOutDuration,
								secondaryTrackFadeOutPercentage,
								secondaryTrackOffset
								);
						secondaryTracks.add(secondaryTrack);
					}
				}
				// Slides
				else if (fieldName.equals("slides")) {
	
					// Read array
					JSONArray slideArray = jsonObject.getJSONArray(fieldName);
					for (int i = 0; i < slideArray.length(); i++) {
						JSONObject slideObject = slideArray.getJSONObject(i);
						Iterator<String> slideIterator = slideObject.keys();
	
						Key stationImage = null;
						Double slideStartingTime = null;

						while (slideIterator.hasNext()) {
							fieldName = slideIterator.next();
							if (fieldName.equals("stationImage")) {
								String stationImageString = slideObject.getString(fieldName);
								stationImage = KeyFactory.stringToKey(stationImageString);
							}
							else if (fieldName.equals("slideStartingTime")) {
								slideStartingTime = slideObject.getDouble(fieldName);
							}
						}
						
						Slide slide = new Slide(
								stationImage,
								slideStartingTime
								);
						slides.add(slide);
					}
				}
		    }
		}
		catch (JSONException e) {
			throw e;
		}

		// Automatically generated fields
		Date programStartingDate = new Date();
		Calendar date = Calendar.getInstance();
		date.add(Calendar.YEAR, 1);
		Date programEndingDate = date.getTime();
		
		Program program = new Program(
				programName,
				programDescription,
				programBanner,
				programSequenceNumber,
				programTotalDurationTime,
				programOverlapDuration,
				programStartingDate,
				programEndingDate,
				mainTrack,
				secondaryTracks,
				slides
				);
		ProgramManager.putProgram(channelKey, program);
	}

	/**
	 * Edit a program through a JSON Object.
	 * @param jsonObject
	 * 			: the JSONObject instance
	 * @param iterator
	 * 			: the string iterator of the JSON object
	 * @param station
	 * 			: the station where the program(s) will be edited
	 * @throws IOException 
	 * @throws MissingRequiredFieldsException 
	 * @throws UnauthorizedUserOperationException
	 * @throws JSONException 
	 * @throws InexistentObjectException 
	 */
	private void editProgram_Object(JSONObject jsonObject, 
			Iterator<String> iterator, Station station) 
			throws IOException, MissingRequiredFieldsException,
			UnauthorizedUserOperationException, JSONException, 
			InexistentObjectException {
    	
		// Program attributes
		Key programKey = null;
		Key channelKey = null;
		String programName = null;
		String programDescription = null;
		String programBanner = null;
		Integer programSequenceNumber = null;
		Double programTotalDurationTime = null;
		Double programOverlapDuration = null;
		
		// Program
		String programKeyString = (String) jsonObject.get("programKey");
		programKey = KeyFactory.stringToKey(programKeyString);
		Program program = ProgramManager.getProgram(programKey);
		
		// Main track
		MainTrack mainTrack = null;
		
		// Secondary tracks
		ArrayList<SecondaryTrack> secondaryTracks = new ArrayList<>();
		
		// Slides
		ArrayList<Slide> slides = new ArrayList<>();
		
		try {
			while (iterator.hasNext()) {
			
				String fieldName = iterator.next();
				if (fieldName.equals("channelKey")) {
					String channelKeyString = jsonObject.getString(fieldName);
					channelKey = KeyFactory.stringToKey(channelKeyString);
					
					// Check if this channel belongs to this station
					if (!ChannelManager.channelBelongsToStation(
							channelKey, station.getKey())) {
			    		throw new UnauthorizedUserOperationException(
			    				station.getUser(), 
			    				"This channel does not belong to this station.");
					}
				}
				else if (fieldName.equals("programName")) {
					programName = jsonObject.getString(fieldName);
				}
				else if (fieldName.equals("programDescription")) {
					programDescription = jsonObject.getString(fieldName);
				}
				else if (fieldName.equals("programBanner")) {
					programBanner = jsonObject.getString(fieldName);
				}
				else if (fieldName.equals("programSequenceNumber")) {
					programSequenceNumber = jsonObject.getInt(fieldName);
				}
				else if (fieldName.equals("programTotalDurationTime")) {
					programTotalDurationTime = jsonObject.getDouble(fieldName);
				}
				else if (fieldName.equals("programOverlapDuration")) {
					programOverlapDuration = jsonObject.getDouble(fieldName);
				}
				// Main Track
				else if (fieldName.equals("mainTrack")) {
					
					// Main Track attributes
					MainTrack.MainTrackType mainTrackType = null;
					Key stationAudio = null;
					Long mainTrackMusicFileKey = null;
					Key mainTrackPlaylistKey = null;
					Double mainTrackDuration = null;
					Integer mainTrackFadeOutSteps = null;
					Double mainTrackFadeOutDuration = null;
					Double mainTrackFadeOutPercentage = null;
					Integer mainTrackFadeInSteps = null;
					Double mainTrackFadeInDuration = null;
					Double mainTrackFadeInPercentage = null;
					
					// Read object
					JSONObject mainTrackObject = jsonObject.getJSONObject(fieldName);
					Iterator<String> mainTrackIterator = 
							mainTrackObject.keys();
					while (mainTrackIterator.hasNext()) {
						fieldName = mainTrackIterator.next();
						if (fieldName.equals("mainTrackType")) {
							String mainTrackTypeString = 
									mainTrackObject.getString(fieldName);
							mainTrackType = 
									MainTrack.getMainTrackTypeFromString(
											mainTrackTypeString);
						}
						else if (fieldName.equals("stationAudio")){
							String stationAudioString = mainTrackObject.getString(fieldName);
							if (stationAudioString != null && !stationAudioString.isEmpty()) {
								stationAudio = KeyFactory.stringToKey(stationAudioString);
							}
						}
						else if (fieldName.equals("mainTrackMusicFileKey")){
							mainTrackMusicFileKey = mainTrackObject.getLong(fieldName);
						}
						else if (fieldName.equals("mainTrackPlaylistKey")){
							String mainTrackPlayListKeyString = 
									mainTrackObject.getString(fieldName);
							if (mainTrackPlayListKeyString != null && 
									!mainTrackPlayListKeyString.isEmpty()) {
								mainTrackPlaylistKey = 
										KeyFactory.stringToKey(mainTrackPlayListKeyString);
							}
						}
						else if (fieldName.equals("mainTrackDuration")){
							mainTrackDuration = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeInSteps")){
							mainTrackFadeInSteps = mainTrackObject.getInt(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeInDuration")){
							mainTrackFadeInDuration = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeInPercentage")){
							mainTrackFadeInPercentage = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeOutSteps")){
							mainTrackFadeOutSteps = mainTrackObject.getInt(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeOutDuration")){
							mainTrackFadeOutDuration = mainTrackObject.getDouble(fieldName);
						}
						else if (fieldName.equals("mainTrackFadeOutPercentage")){
							mainTrackFadeOutPercentage = mainTrackObject.getDouble(fieldName);
						}
					}
	
					mainTrack = program.getMainTrack();
					MainTrackManager.updateMainTrackAttributes(mainTrack.getKey(), 
							mainTrackType, 
							stationAudio, 
							mainTrackMusicFileKey,
							mainTrackPlaylistKey, 
							mainTrackDuration, 
							mainTrackFadeOutSteps, 
							mainTrackFadeOutDuration, 
							mainTrackFadeOutPercentage, 
							mainTrackFadeInSteps, 
							mainTrackFadeInDuration, 
							mainTrackFadeInPercentage);
				}
				// Secondary Tracks
				else if (fieldName.equals("secondaryTracks")) {
	
					// Read array
					JSONArray secondaryTrackArray = 
							jsonObject.getJSONArray(fieldName);

					for (int i = 0; i < secondaryTrackArray.length(); i++) {
						JSONObject secondaryTrackObject = 
								secondaryTrackArray.getJSONObject(i);
						Iterator<String> secondaryTrackIterator = 
								secondaryTrackObject.keys();
	
						Key secondaryTrackKey = null;
						SecondaryTrack.SecondaryTrackType secondaryTrackType = null;
						Key stationAudio = null;
						Long secondaryTrackMusicFileKey = null;
						Double secondaryTrackStartingTime = null;
						Double secondaryTrackDuration = null;
						Integer secondaryTrackFadeInSteps = null;
						Double secondaryTrackFadeInDuration = null;
						Double secondaryTrackFadeInPercentage = null;
						Integer secondaryTrackFadeOutSteps = null;
						Double secondaryTrackFadeOutDuration = null;
						Double secondaryTrackFadeOutPercentage = null;
						Double secondaryTrackOffset = null;

						while (secondaryTrackIterator.hasNext()) {
							fieldName = secondaryTrackIterator.next();
							if (fieldName.equals("secondaryTrackKey")) {
								String secondaryTrackKeyString =
										secondaryTrackObject.getString(fieldName);
								if (!secondaryTrackKeyString.isEmpty()) {
									secondaryTrackKey = 
											KeyFactory.stringToKey(secondaryTrackKeyString);
								}
							}
							else if (fieldName.equals("secondaryTrackType")) {
								String secondaryTrackTypeString = 
										secondaryTrackObject.getString(fieldName);
								secondaryTrackType = 
										SecondaryTrack.getSecondaryTrackTypeFromString(
												secondaryTrackTypeString);
							}
							else if (fieldName.equals("stationAudio")) {
								String stationAudioString = 
										secondaryTrackObject.getString(fieldName);
								if (stationAudioString != null && !stationAudioString.isEmpty()) {
									stationAudio = KeyFactory.stringToKey(stationAudioString);
								}
							}
							else if (fieldName.equals("secondaryTrackMusicFileKey")) {
								secondaryTrackMusicFileKey = 
										secondaryTrackObject.getLong(fieldName);
							}
							else if (fieldName.equals("secondaryTrackStartingTime")) {
								secondaryTrackStartingTime = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackDuration")) {
								secondaryTrackDuration = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeInSteps")) {
								secondaryTrackFadeInSteps = 
										secondaryTrackObject.getInt(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeInDuration")) {
								secondaryTrackFadeInDuration = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeInPercentage")) {
								secondaryTrackFadeInPercentage = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeOutSteps")) {
								secondaryTrackFadeOutSteps =
										secondaryTrackObject.getInt(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeOutDuration")) {
								secondaryTrackFadeOutDuration = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackFadeOutPercentage")) {
								secondaryTrackFadeOutPercentage = 
										secondaryTrackObject.getDouble(fieldName);
							}
							else if (fieldName.equals("secondaryTrackOffset")) {
								secondaryTrackOffset = 
										secondaryTrackObject.getDouble(fieldName);
							}
						}
						
						if (secondaryTrackKey != null) {
							SecondaryTrackManager.updateSecondaryTrackAttributes(
									secondaryTrackKey, 
									secondaryTrackType, 
									stationAudio, 
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
						else {
							SecondaryTrack secondaryTrack = new SecondaryTrack(
									secondaryTrackType,
									stationAudio,
									secondaryTrackMusicFileKey,
									secondaryTrackStartingTime,
									secondaryTrackDuration,
									secondaryTrackFadeInSteps,
									secondaryTrackFadeInDuration,
									secondaryTrackFadeInPercentage,
									secondaryTrackFadeOutSteps,
									secondaryTrackFadeOutDuration,
									secondaryTrackFadeOutPercentage,
									secondaryTrackOffset
									);
							SecondaryTrackManager.putSecondaryTrack(
									programKey, secondaryTrack);
						}
					}
				}
				// Slides
				else if (fieldName.equals("slides")) {
	
					// Read array
					JSONArray slideArray = jsonObject.getJSONArray(fieldName);
					for (int i = 0; i < slideArray.length(); i++) {
						JSONObject slideObject = slideArray.getJSONObject(i);
						Iterator<String> slideIterator = slideObject.keys();
	
						Key slideKey = null;
						Key stationImage = null;
						Double slideStartingTime = null;

						while (slideIterator.hasNext()) {
							fieldName = slideIterator.next();
							if (fieldName.equals("slideKey")) {
								String slideKeyString = slideObject.getString(fieldName);
								if (!slideKeyString.isEmpty()) {
									slideKey = KeyFactory.stringToKey(slideKeyString);
								}
							}
							else if (fieldName.equals("stationImage")) {
								String stationImageString = slideObject.getString(fieldName);
								stationImage = KeyFactory.stringToKey(stationImageString);
							}
							else if (fieldName.equals("slideStartingTime")) {
								slideStartingTime = slideObject.getDouble(fieldName);
							}
						}
						
						if (slideKey != null) {
							SlideManager.updateSlideAttributes(
									slideKey, 
									stationImage, 
									slideStartingTime);
						}
						else {
							Slide slide = new Slide(
									stationImage,
									slideStartingTime
									);
							SlideManager.putSlide(programKey, slide);
						}
					}
				}
		    }
		}
		catch (JSONException e) {
			throw e;
		}

		// Automatically generated fields
		Date programStartingDate = new Date();
		Calendar date = Calendar.getInstance();
		date.add(Calendar.YEAR, 1);
		Date programEndingDate = date.getTime();
		
		ProgramManager.updateProgramAttributes(
				programKey, 
				programName, 
				programDescription, 
				programBanner, 
				programSequenceNumber, 
				programTotalDurationTime, 
				programOverlapDuration, 
				programStartingDate, 
				programEndingDate);
	}
	
	/**
	 * Add a program to a channel through a JSON file.
	 * @param reader
	 * 			: the JsonReader instance
	 * @param station
	 * 			: the station where the program(s) will be added
	 * @throws IOException 
	 * @throws MissingRequiredFieldsException 
	 * @throws UnauthorizedUserOperationException
	 * @throws MalformedJsonException
	 */
	private void addProgram_File(JsonReader reader, Station station) 
			throws IOException, MissingRequiredFieldsException,
			UnauthorizedUserOperationException, MalformedJsonException {
    	
		// Program attributes
		Key channelKey = null;
		String programName = null;
		String programDescription = null;
		String programBanner = null;
		Integer programSequenceNumber = null;
		Double programTotalDurationTime = null;
		Double programOverlapDuration = null;
		
		// Main track
		MainTrack mainTrack = null;
		
		// Secondary tracks
		ArrayList<SecondaryTrack> secondaryTracks = new ArrayList<>();
		
		// Slides
		ArrayList<Slide> slides = new ArrayList<>();
		
		try {
			while (reader.hasNext()) {   
			
				String fieldName = reader.nextName();
				if (fieldName.equals("channelKey")) {
					String channelKeyString = reader.nextString();
					channelKey = KeyFactory.stringToKey(channelKeyString);
					
					// Check if this channel belongs to this station
					if (!ChannelManager.channelBelongsToStation(
							channelKey, station.getKey())) {
			    		throw new UnauthorizedUserOperationException(
			    				station.getUser(), 
			    				"This channel does not belong to this station.");
					}
				}
				else if (fieldName.equals("programName")) {
					programName = reader.nextString();
				}
				else if (fieldName.equals("programDescription")) {
					programDescription = reader.nextString();
				}
				else if (fieldName.equals("programBanner")) {
					programBanner = reader.nextString();
				}
				else if (fieldName.equals("programSequenceNumber")) {
					programSequenceNumber = reader.nextInt();
				}
				else if (fieldName.equals("programTotalDurationTime")) {
					programTotalDurationTime = reader.nextDouble();
				}
				else if (fieldName.equals("programOverlapDuration")) {
					programOverlapDuration = reader.nextDouble();
				}
				// Main Track
				else if (fieldName.equals("mainTrack")) {
					
					// Main Track attributes
					MainTrack.MainTrackType mainTrackType = null;
					Key stationAudio = null;
					Long mainTrackMusicFileKey = null;
					Key mainTrackPlaylistKey = null;
					Double mainTrackDuration = null;
					Integer mainTrackFadeOutSteps = null;
					Double mainTrackFadeOutDuration = null;
					Double mainTrackFadeOutPercentage = null;
					Integer mainTrackFadeInSteps = null;
					Double mainTrackFadeInDuration = null;
					Double mainTrackFadeInPercentage = null;
					
					// Read object
					reader.beginObject();
					while (reader.hasNext()) {
						fieldName = reader.nextName();
						if (fieldName.equals("mainTrackType")) {
							String mainTrackTypeString = reader.nextString();
							mainTrackType = 
									MainTrack.getMainTrackTypeFromString(
											mainTrackTypeString);
						}
						else if (fieldName.equals("stationAudio") &&
								mainTrackType == MainTrack.MainTrackType.FILE_UPLOAD){
							String stationAudioString = reader.nextString();
							stationAudio = KeyFactory.stringToKey(stationAudioString);
						}
						else if (fieldName.equals("mainTrackMusicFileKey") &&
								mainTrackType == MainTrack.MainTrackType.MUSIC_FILE){
							mainTrackMusicFileKey = reader.nextLong();
						}
						else if (fieldName.equals("mainTrackPlaylistKey") &&
								mainTrackType == MainTrack.MainTrackType.PLAYLIST){
							String mainTrackPlayListKeyString = reader.nextString();
							mainTrackPlaylistKey = 
									KeyFactory.stringToKey(mainTrackPlayListKeyString);
						}
						else if (fieldName.equals("mainTrackDuration")){
							mainTrackDuration = reader.nextDouble();
						}
						else if (fieldName.equals("mainTrackFadeInSteps")){
							mainTrackFadeInSteps = reader.nextInt();
						}
						else if (fieldName.equals("mainTrackFadeInDuration")){
							mainTrackFadeInDuration = reader.nextDouble();
						}
						else if (fieldName.equals("mainTrackFadeInPercentage")){
							mainTrackFadeInPercentage = reader.nextDouble();
						}
						else if (fieldName.equals("mainTrackFadeOutSteps")){
							mainTrackFadeOutSteps = reader.nextInt();
						}
						else if (fieldName.equals("mainTrackFadeOutDuration")){
							mainTrackFadeOutDuration = reader.nextDouble();
						}
						else if (fieldName.equals("mainTrackFadeOutPercentage")){
							mainTrackFadeOutPercentage = reader.nextDouble();
						}
						else {
							reader.skipValue(); //avoid some unhandled events
						}
					}
					reader.endObject();
	
					mainTrack = new MainTrack(
							mainTrackType,
							stationAudio,
							mainTrackMusicFileKey,
							mainTrackPlaylistKey,
							mainTrackDuration,
							mainTrackFadeInSteps,
							mainTrackFadeInDuration,
							mainTrackFadeInPercentage,
							mainTrackFadeOutSteps,
							mainTrackFadeOutDuration,
							mainTrackFadeOutPercentage
							);
				}
				// Secondary Tracks
				else if (fieldName.equals("secondaryTracks")) {
	
					// Read array
					reader.beginArray();
					while (reader.hasNext()) {
	
						SecondaryTrack.SecondaryTrackType secondaryTrackType = null;
						Key stationAudio = null;
						Long secondaryTrackMusicFileKey = null;
						Double secondaryTrackStartingTime = null;
						Double secondaryTrackDuration = null;
						Integer secondaryTrackFadeInSteps = null;
						Double secondaryTrackFadeInDuration = null;
						Double secondaryTrackFadeInPercentage = null;
						Integer secondaryTrackFadeOutSteps = null;
						Double secondaryTrackFadeOutDuration = null;
						Double secondaryTrackFadeOutPercentage = null;
						Double secondaryTrackOffset = null;
						
						reader.beginObject();
						while (reader.hasNext()) {
							fieldName = reader.nextName();
							if (fieldName.equals("secondaryTrackType")) {
								String secondaryTrackTypeString = reader.nextString();
								secondaryTrackType = 
										SecondaryTrack.getSecondaryTrackTypeFromString(
												secondaryTrackTypeString);
							}
							else if (fieldName.equals("stationAudio") &&
									secondaryTrackType == SecondaryTrack.SecondaryTrackType.FILE_UPLOAD) {
								String stationAudioString = reader.nextString();
								stationAudio = KeyFactory.stringToKey(stationAudioString);
							}
							else if (fieldName.equals("secondaryTrackMusicFileKey") &&
									secondaryTrackType == SecondaryTrack.SecondaryTrackType.MUSIC_FILE) {
								secondaryTrackMusicFileKey = reader.nextLong();
							}
							else if (fieldName.equals("secondaryTrackStartingTime")) {
								secondaryTrackStartingTime = reader.nextDouble();
							}
							else if (fieldName.equals("secondaryTrackDuration")) {
								secondaryTrackDuration = reader.nextDouble();
							}
							else if (fieldName.equals("secondaryTrackFadeInSteps")) {
								secondaryTrackFadeInSteps = reader.nextInt();
							}
							else if (fieldName.equals("secondaryTrackFadeInDuration")) {
								secondaryTrackFadeInDuration = reader.nextDouble();
							}
							else if (fieldName.equals("secondaryTrackFadeInPercentage")) {
								secondaryTrackFadeInPercentage = reader.nextDouble();
							}
							else if (fieldName.equals("secondaryTrackFadeOutSteps")) {
								secondaryTrackFadeOutSteps = reader.nextInt();
							}
							else if (fieldName.equals("secondaryTrackFadeOutDuration")) {
								secondaryTrackFadeOutDuration = reader.nextDouble();
							}
							else if (fieldName.equals("secondaryTrackFadeOutPercentage")) {
								secondaryTrackFadeOutPercentage = reader.nextDouble();
							}
							else if (fieldName.equals("secondaryTrackOffset")) {
								secondaryTrackOffset = reader.nextDouble();
							}
							else {
								reader.skipValue(); //avoid some unhandled events
							}
						}
						reader.endObject();
						
						SecondaryTrack secondaryTrack = new SecondaryTrack(
								secondaryTrackType,
								stationAudio,
								secondaryTrackMusicFileKey,
								secondaryTrackStartingTime,
								secondaryTrackDuration,
								secondaryTrackFadeInSteps,
								secondaryTrackFadeInDuration,
								secondaryTrackFadeInPercentage,
								secondaryTrackFadeOutSteps,
								secondaryTrackFadeOutDuration,
								secondaryTrackFadeOutPercentage,
								secondaryTrackOffset
								);
						secondaryTracks.add(secondaryTrack);
					}
					reader.endArray();
				}
				// Slides
				else if (fieldName.equals("slides")) {
	
					// Read array
					reader.beginArray();
					while (reader.hasNext()) {
	
						Key stationImage = null;
						Double slideStartingTime = null;
						
						reader.beginObject();
						while (reader.hasNext()) {
							fieldName = reader.nextName();
							if (fieldName.equals("stationImage")) {
								String stationImageString = reader.nextString();
								stationImage = KeyFactory.stringToKey(stationImageString);
							}
							else if (fieldName.equals("slideStartingTime")) {
								slideStartingTime = reader.nextDouble();
							}
							else {
								reader.skipValue(); //avoid some unhandled events
							}
						}
						reader.endObject();
						
						Slide slide = new Slide(
								stationImage,
								slideStartingTime
								);
						slides.add(slide);
					}
					reader.endArray();
				}
				else {
					reader.skipValue(); //avoid some unhandled events
				}
		    }
		}
		catch (MalformedJsonException e) {
			throw e;
		}

		// Automatically generated fields
		Date programStartingDate = new Date();
		Calendar date = Calendar.getInstance();
		date.add(Calendar.YEAR, 1);
		Date programEndingDate = date.getTime();
		
		Program program = new Program(
				programName,
				programDescription,
				programBanner,
				programSequenceNumber,
				programTotalDurationTime,
				programOverlapDuration,
				programStartingDate,
				programEndingDate,
				mainTrack,
				secondaryTracks,
				slides
				);
		ProgramManager.putProgram(channelKey, program);
	}

	/**
	 * Add a playlist to a station.
	 * @param reader
	 * 			: the JsonReader instance
	 * @param station
	 * 			: the station where the playlist(s) will be added
	 * @throws IOException 
	 * @throws MissingRequiredFieldsException 
	 */
	private void addPlaylist_File(JsonReader reader, Station station)
			throws IOException, MissingRequiredFieldsException {
    		
		// Playlist attributes
		String playlistName = null;
		
		// Secondary tracks
		ArrayList<PlaylistMusicFile> playlistMusicFiles = new ArrayList<>();
		
		while (reader.hasNext()) {
		
			String fieldName = reader.nextName();
			if (fieldName.equals("playlistName")) {
				playlistName = reader.nextString();
			}
			// Playlist Music Files
			else if (fieldName.equals("playlistMusicFiles")) {

				// Read array
				reader.beginArray();
				while (reader.hasNext()) {

					Long musicFileKey = null;
					Integer playlistMusicFileSequenceNumber = null;
					
					reader.beginObject();
					while (reader.hasNext()) {
						fieldName = reader.nextName();
						if (fieldName.equals("musicFileKey")) {
							musicFileKey = reader.nextLong();
						}
						else if (fieldName.equals("playlistMusicFileSequenceNumber")) {
							playlistMusicFileSequenceNumber = reader.nextInt();
						}
						else {
							reader.skipValue(); //avoid some unhandled events
						}
					}
					reader.endObject();
					
					PlaylistMusicFile playlistMusicFile = new PlaylistMusicFile(
							musicFileKey,
							playlistMusicFileSequenceNumber
							);
					playlistMusicFiles.add(playlistMusicFile);
				}
				reader.endArray();
			}
			else {
				reader.skipValue(); //avoid some unhandled events
			}
	    }
		
		Playlist playlist = new Playlist(
				playlistName,
				playlistMusicFiles
				);
		PlaylistManager.putPlaylist(station.getKey(), playlist);
	}
	
	/**
	 * Add a channel to a station.
	 * @param reader
	 * 			: the JsonReader instance
	 * @param station
	 * 			: the station where the channel(s) will be added
	 * @throws IOException 
	 * @throws MissingRequiredFieldsException 
	 */
	private void addChannel_File(JsonReader reader, Station station)
			throws IOException, MissingRequiredFieldsException {
    		
		// Channel attributes
		String channelName = null;
		Integer channelNumber = null;
		
		while (reader.hasNext()) {
		
			String fieldName = reader.nextName();
			if (fieldName.equals("channelName")) {
				channelName = reader.nextString();
			}
			else if (fieldName.equals("channelNumber")) {
				channelNumber = reader.nextInt();
			}
			else {
				reader.skipValue(); //avoid some unhandled events
			}
	    }
		
		Channel channel = new Channel(
				channelName,
				channelNumber
				);
		ChannelManager.putChannel(station.getKey(), channel);
	}
	
	/**
	 * Edit a station audio.
	 * @param reader
	 * 			: the JsonReader instance
	 * @throws IOException 
	 * @throws MissingRequiredFieldsException 
	 * @throws ObjectExistsInDatastoreException 
	 */
	private void editStationAudio_File(JsonReader reader)
			throws IOException, MissingRequiredFieldsException, 
			ObjectExistsInDatastoreException {

		// Station audio attributes
		StationAudio stationAudio = null;
		Key stationAudioKey = null;
		StationAudio.StationAudioType stationAudioType = null;
		String stationAudioName = null;
		Double stationAudioDuration = null;
		
		while (reader.hasNext()) {
		
			String fieldName = reader.nextName();
			if (fieldName.equals("stationAudioKey")) {
				String stationAudioKeyString = reader.nextString();
				stationAudioKey = KeyFactory.stringToKey(stationAudioKeyString);
				stationAudio = StationAudioManager.getStationAudio(stationAudioKey);
			}
			else if (fieldName.equals("stationAudioType")) {
				String stationAudioTypeString = reader.nextString();
				stationAudioType = 
						StationAudio.getStationAudioTypeFromString(stationAudioTypeString);
			}
			else if (fieldName.equals("stationAudioName")) {
				stationAudioName = reader.nextString();
			}
			else if (fieldName.equals("stationAudioDuration")) {
				stationAudioDuration = reader.nextDouble();
			}
			else {
				reader.skipValue(); //avoid some unhandled events
			}
	    }
		
		StationAudioManager.updateStationAudioAttributes(
				stationAudioKey, 
				stationAudioType, 
				stationAudioName, 
				stationAudio.getStationAudioMultimediaContent(), 
				stationAudioDuration,
				stationAudio.getStationAudioFormat());
	}
	
}
