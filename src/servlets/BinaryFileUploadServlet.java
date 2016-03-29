/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
 */

package servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.BlobUtils;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Station;
import datastore.StationAudio;
import datastore.StationAudioManager;
import datastore.StationImage;
import datastore.StationImageManager;
import datastore.StationManager;
import datastore.User;

/**
 * This servlet is used to manage JSON uploads from Stations.
 * 
 */
@SuppressWarnings("serial")
public class BinaryFileUploadServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(BinaryFileUploadServlet.class
			.getCanonicalName());

    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
	
    // JSP file locations
    private static final String thisServlet = "/binaryFileUpload";
    
    /**
     * Respond to servlet GET requests
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
	    // Lets check the action required by the jsp
	    String message = req.getParameter("msg");
	    String action = req.getParameter("action");
	
	    if (message.equals("success")) {
	    	
	    	// We return information about the file in JSON format
	    	if (action.equalsIgnoreCase("upload")) {
	    		
	    		String type = req.getParameter("type");
	    		String datastoreObjectKeyString = 
	    				req.getParameter("datastoreObjectKey");
	    		Key datastoreObjectKey = KeyFactory.stringToKey(datastoreObjectKeyString);
	    		BlobKey fileKey = null;
	    		String fileKeyString = null;
	    		String jsonString = null;
	    		
	    		// Check type
	    		if (type.equalsIgnoreCase("audio_music") || 
	    				type.equalsIgnoreCase("audio_voice")) {
	    			
	    			StationAudio stationAudio = 
	    					StationAudioManager.getStationAudio(datastoreObjectKey);
	    			
	    			fileKey = stationAudio.getStationAudioMultimediaContent();
	    		}
	    		else if (type.equalsIgnoreCase("image")) {
	    			StationImage stationImage = 
	    					StationImageManager.getStationImage(datastoreObjectKey);
	    			
	    			fileKey = stationImage.getStationImageMultimediaContent();
	    		}
	    		
	    		fileKeyString = fileKey.getKeyString();
		    	BlobInfoFactory bif = new BlobInfoFactory();
		    	BlobInfo blobInfo = bif.loadBlobInfo(fileKey);
		    	String fileName = blobInfo.getFilename();
		    	
		    	jsonString = "{" + "\n" +
		    			"\"datastoreObjectKey\":" + "\"" + datastoreObjectKeyString + "\"," + "\n" +
		    			"\"fileKey\":" + "\"" + fileKeyString + "\"," + "\n" +
		    			"\"fileName\":" + "\"" + fileName + "\"\n" +
		    			"}";
		    	
		    	log.info("jsonString: " + jsonString);
		    	
		    	resp.setContentType("application/json;charset=UTF-8");
		    	resp.setCharacterEncoding("UTF-8");
		    	resp.getWriter().println(jsonString);
	    	}
	    	else if (action.equals("delete")){
	    		resp.getWriter().println("File deleted successfully.");
	    	}
	    }
	}
    
	/**
	 * Respond to servlet POST requests
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        
		// Only stations can upload binary files
//        Email userEmail = new Email(req.getParameter("userEmail"));
//        String userPassword = req.getParameter("userPassword");
//        Station station = StationManager.getStation(userEmail);
//        if (!station.getUser().validateUser(userEmail, userPassword)) {
//            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
//                	"User e-mail and password don't match.");
//            return;
//        }
        
    	// Check that an ADMIN or a STATION user is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (user.getUserType() != User.Type.STATION) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        Station station = StationManager.getStation(user);
        
        BlobKey fileKey = null;
        try {
        	String action = req.getParameter("action");
        	
        	if (action.equalsIgnoreCase("upload")) {
        		// Get the type of file to upload
        		String type = req.getParameter("type");
        		
		        // Saves binary file in Blobstore and returns key
		        fileKey = BlobUtils.assignBlobKey(req, "file", blobstoreService);
		        String fileName = null;
		        String fileFormat = null;
		        
		        BlobInfoFactory bif = new BlobInfoFactory();
		        if (fileKey != null) {
		        	fileName = bif.loadBlobInfo(fileKey).getFilename();
        			fileFormat = 
        					fileName.substring(fileName.lastIndexOf('.') + 1);
        		}
		        
		        // Save corresponding type
	        	StationAudio.StationAudioType stationAudioType = null;
	        	Double stationAudioDuration = null;

	        	Key datastoreObjectKey = null;
	        	// AUDIO_MUSIC
		        if (type.equalsIgnoreCase("audio_music")) {
		        	stationAudioType = StationAudio.StationAudioType.MUSIC;
		        	String stationAudioDurationString = "0.0";
		        			//req.getParameter("a_duration"); // TODO: Enable this later
		        	
		        	if (!stationAudioDurationString.isEmpty()) {
		        		stationAudioDuration = 
		        				Double.parseDouble(stationAudioDurationString);
		        	}
		        	
		        	StationAudio stationAudio = new StationAudio(
		        			stationAudioType,
		        			fileName,
		        			fileKey,
		        			stationAudioDuration,
		        			fileFormat
		        			);
		        	StationAudioManager.putStationAudio(
		        			station.getKey(), stationAudio);
		        	
		        	datastoreObjectKey = stationAudio.getKey();
		        }
		        // AUDIO_VOICE
		        else if (type.equalsIgnoreCase("audio_voice")) {
		        	stationAudioType = StationAudio.StationAudioType.VOICE;
		        	String stationAudioDurationString = "0.0";
		        			//req.getParameter("a_duration"); // TODO: Enable this later
		        	
		        	if (!stationAudioDurationString.isEmpty()) {
		        		stationAudioDuration = 
		        				Double.parseDouble(stationAudioDurationString);
		        	}
		        	
		        	StationAudio stationAudio = new StationAudio(
		        			stationAudioType,
		        			fileName,
		        			fileKey,
		        			stationAudioDuration,
		        			fileFormat
		        			);
		        	StationAudioManager.putStationAudio(
		        			station.getKey(), stationAudio);
		        	
		        	datastoreObjectKey = stationAudio.getKey();
		        }
		        // IMAGE
		        else if (type.equalsIgnoreCase("image")) {
		        	StationImage stationImage = new StationImage(
		        			fileName,
		        			fileKey,
		        			fileFormat
		        			);
		        	StationImageManager.putStationImage(
		        			station.getKey(), stationImage);
		        	
		        	datastoreObjectKey = stationImage.getKey();
		        }
		        else {
		        	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
		            		"Invalid type.");
	    	    	return;
		        }
		        
		        // Return key of uploaded file
		        resp.sendRedirect(thisServlet + "?msg=success&action=" + action + 
		        		"&type=" + type +
		        		"&datastoreObjectKey=" + KeyFactory.keyToString(datastoreObjectKey));
//		        resp.sendRedirect(thisServlet + "?msg=success&action=" + action + 
//		        		"&type=" + type +
//		        		"&fileKey=" + fileKey.getKeyString());
        	}
        	else if (action.equalsIgnoreCase("delete")) {
        		String fileKeyString = req.getParameter("fileKey");
        		blobstoreService.delete(new BlobKey(fileKeyString));
        		
		        // Return key of uploaded file
		        resp.sendRedirect(thisServlet + "?msg=success&action=" + action);
        	}
    		else {
    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
	            		"Invalid action.");
    		}
        }
        catch (Exception e) {
        	if (fileKey != null) {
        		blobstoreService.delete(fileKey);
        	}
            log.log(Level.SEVERE, e.toString());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		"Internal server error.");
        }
	}
	
}
