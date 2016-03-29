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

/**
 * This servlet class is used to add, delete and update
 * different objects related to programs.
 * 
 */

@SuppressWarnings("serial")
public class ManageProgramServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageProgramServlet.class.getName());
    
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
    
    // JSP file locations
    private static final String editProgramJSP = "/station/editProgram.jsp";
    private static final String listProgramJSP = "/station/listProgram.jsp";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	
    	// Check that an actual user is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null || user.getUserType() != User.Type.STATION) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("delete")) {
        	
        	String type = req.getParameter("type");
        	
        	// Key of object to delete
        	Key key = KeyFactory.stringToKey(req.getParameter("k"));

        	String redirectURL = "?msg=success&action=" + action;
        	
            // Secondary Tracks
            if (type.equalsIgnoreCase("st")) {
            	SecondaryTrackManager.deleteSecondaryTrack(key);
            	
            	redirectURL = editProgramJSP + redirectURL + "&k=" + 
            			KeyFactory.keyToString(key.getParent());
            }
            // Slides
            else if (type.equalsIgnoreCase("slide")) {
            	SlideManager.deleteSlide(key);
            	
            	redirectURL = editProgramJSP + redirectURL + "&k=" + 
            			KeyFactory.keyToString(key.getParent());
            }
            else {
    	    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
	            		"Invalid type.");
    	    	return;
            }
            
            // If success
            resp.sendRedirect(redirectURL);
        }
    }

}
