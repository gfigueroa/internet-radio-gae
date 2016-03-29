/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.List;
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

import datastore.Genre;
import datastore.GenreManager;
import datastore.MusicFile;
import datastore.MusicFileManager;
import datastore.Region;
import datastore.RegionManager;
import datastore.StationType;
import datastore.StationTypeManager;
import datastore.SystemManager;
import datastore.User;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This servlet class is used to add, delete and update
 * global objects in the system.
 * 
 */

@SuppressWarnings("serial")
public class ManageGlobalObjectsServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageGlobalObjectsServlet.class.getName());
    
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
    
    // JSP file locations
    private static final String addGenreJSP = "/admin/addGenre.jsp";
    private static final String editGenreJSP = "/admin/editGenre.jsp";
    private static final String listGenreJSP = "/admin/listGenre.jsp";
    private static final String addMusicFileJSP = "/admin/addMusicFile.jsp";
    private static final String listMusicFileJSP = "/admin/listMusicFile.jsp";
    private static final String addRegionJSP = "/admin/addRegion.jsp";
    private static final String editRegionJSP = "/admin/editRegion.jsp";
    private static final String listRegionJSP = "/admin/listRegion.jsp";
    private static final String addStationTypeJSP = "/admin/addStationType.jsp";
    private static final String editStationTypeJSP = "/admin/editStationType.jsp";
    private static final String listStationTypeJSP = "/admin/listStationType.jsp";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        
        // Check that an administrator is carrying out the action
	    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
    	
    	// Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("delete")) {
            // retrieve the key     
        	String keyString = req.getParameter("k");
      		Long keyLong = Long.parseLong(keyString);

            // deleting a user
            switch(req.getParameter("type").charAt(0)){
            	case 'G':
            		GenreManager.deleteGenre(keyLong);
            		resp.sendRedirect(listGenreJSP + "?msg=success&action=delete");
                	break;
            	case 'M':
            		MusicFileManager.deleteMusicFile(keyLong);
            		SystemManager.updateMusicLibraryVersion();
            		resp.sendRedirect(listMusicFileJSP + "?msg=success&action=delete");
                	break;
                case 'R':
                	RegionManager.deleteRegion(keyLong);
                	resp.sendRedirect(listRegionJSP + "?msg=success&action=delete");
                	break;
                case 'T':
                	StationTypeManager.deleteStationType(keyLong);
                	SystemManager.updateStationTypeListVersion();
                	resp.sendRedirect(listStationTypeJSP + "?msg=success&action=delete");
                	break; 
                default:
                    assert(false); // there is no other type
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        
        // Check that an administrator is carrying out the action
	    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
    	
        // Lets check the action and type required by the jsp
        String action = req.getParameter("action");
        String type = req.getParameter("type");

        String successURL = "";
        String failURL = "";
        BlobKey blobKey = null;
        List<BlobKey> blobKeys = null;
        if (action.equals("add")) {
            try {
	            // Genres
	        	if (type.equalsIgnoreCase("genre")) {
		            String genreEnglishName = req.getParameter("g_e_name");
		            String genreChineseName = req.getParameter("g_c_name");
             
	            	successURL = listGenreJSP;
	            	failURL = addGenreJSP;
		            
                    Genre genre = new Genre(
                    		genreEnglishName,
                    		genreChineseName
                    		);
                    GenreManager.putGenre(genre);
	            }
		        // Music files
	        	else if (type.equalsIgnoreCase("musicFile")) {
            		BlobInfoFactory bif = new BlobInfoFactory();
            		
            		BlobKey musicFileKey = 
            				BlobUtils.assignBlobKey(req, "file", blobstoreService);
            		
            		String genreKeyString = req.getParameter("genre");
            		Long genreKey = null;
            		if (genreKeyString != null && !genreKeyString.isEmpty()) {
            			genreKey = Long.parseLong(genreKeyString);
            		}
            		
            		String musicFileTitle = req.getParameter("m_title");
            		String musicFileName = null;
            		String musicFileFormat = null;
            		if (musicFileKey != null) {
            			musicFileName = bif.loadBlobInfo(musicFileKey).getFilename();
            			musicFileFormat = 
            					musicFileName.substring(musicFileName.indexOf('.') + 1);
            		}
            		
	        		successURL = listMusicFileJSP;
	        		failURL = addMusicFileJSP;
	        		blobKey = musicFileKey;

                    MusicFile musicFile = new MusicFile(
                    		genreKey,
                    		musicFileKey,
                    		musicFileTitle,
                    		"Artist",
                    		"Album",
                    		"Album Artist",
                    		2013,
                    		"Composer",
                    		"Publisher",
                    		0.0,
                    		musicFileFormat,
                    		"Comments"
                    		);
                    MusicFileManager.putMusicFile(musicFile);
                    SystemManager.updateMusicLibraryVersion();
	        	}
	        	// Multiple music files
	        	else if (type.equalsIgnoreCase("multipleMusic")) {
            		BlobInfoFactory bif = new BlobInfoFactory();
            		
            		List<BlobKey> musicFileKeys = 
            				BlobUtils.assignBlobKeys(req, "file", blobstoreService);
            		blobKeys = musicFileKeys;
            		
            		String genreKeyString = req.getParameter("genre");
            		Long genreKey = Long.parseLong(genreKeyString);
            		
	        		successURL = listMusicFileJSP;
	        		failURL = listMusicFileJSP;
            		
            		for (BlobKey musicFileKey : musicFileKeys) {
	            		String musicFileTitle = bif.loadBlobInfo(musicFileKey).getFilename();
	            		String musicFileFormat = 
	            				musicFileTitle.substring(musicFileTitle.indexOf('.') + 1);
	
	                    MusicFile musicFile = new MusicFile(
	                    		genreKey,
	                    		musicFileKey,
	                    		musicFileTitle,
	                    		"Artist",
	                    		"Album",
	                    		"Album Artist",
	                    		2013,
	                    		"Composer",
	                    		"Publisher",
	                    		0.0,
	                    		musicFileFormat,
	                    		"Comments"
	                    		);
	                    MusicFileManager.putMusicFile(musicFile);
            		}
	        	}
	            // Regions
                else if (type.equalsIgnoreCase("region")) {      	
                    String regionName = req.getParameter("r_name");
                    String regionComments = req.getParameter("r_comments");
                    
                	successURL = listRegionJSP;
                	failURL = addRegionJSP;
      
                    Region neoRegion = new Region(regionName, regionComments);
                    RegionManager.putRegion(neoRegion);
                }
	            // Station types
                else if (type.equalsIgnoreCase("stationType")) {
	                String stationTypeName = req.getParameter("st_name");
	                String stationTypeDescription = req.getParameter("st_description");
            
	                successURL = listStationTypeJSP;
	                failURL = addStationTypeJSP;
	                
                    StationType neoStationType = 
                    		new StationType(stationTypeName, 
                    				stationTypeDescription);
                    StationTypeManager.putStationType(neoStationType);
	            }
	        	
	        	// If success
	        	resp.sendRedirect(successURL + "?msg=success&action=" + action);
            }
            catch (MissingRequiredFieldsException mrfe) {
            	if (blobKey != null)
            		blobstoreService.delete(blobKey);
            	if (blobKeys != null) {
            		for (BlobKey key : blobKeys) {
            			blobstoreService.delete(key);
            		}
            	}
                resp.sendRedirect(failURL + "?etype=MissingInfo");
                return;
            }
            catch (ObjectExistsInDatastoreException oeide) {
            	if (blobKey != null)
            		blobstoreService.delete(blobKey);
            	if (blobKeys != null) {
            		for (BlobKey key : blobKeys) {
            			blobstoreService.delete(key);
            		}
            	}
                resp.sendRedirect(failURL + "?etype=ObjectExists");
                return;
            }
            catch (Exception ex) {
            	if (blobKey != null)
            		blobstoreService.delete(blobKey);
            	if (blobKeys != null) {
            		for (BlobKey key : blobKeys) {
            			blobstoreService.delete(key);
            		}
            	}
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
	    }
	    else if (action.equals("update")) {
	    	String keyString = req.getParameter("k");
	    	Long keyLong = Long.parseLong(keyString);
	    	try {
            	// Genres
            	if (type.equalsIgnoreCase("genre")) {
	            	String genreEnglishName = req.getParameter("g_e_name");
	            	String genreChineseName = req.getParameter("g_c_name");
	            	
	            	successURL = editGenreJSP;
	            	failURL = editGenreJSP;

                    GenreManager.updateGenreAttributes(
                    		keyLong, 
                    		genreEnglishName, 
                    		genreChineseName);
            	}
	            // Regions
            	else if (type.equalsIgnoreCase("region")) {
                	String regionName = req.getParameter("r_name");
                	String regionComments = req.getParameter("r_comments");

                	successURL = editRegionJSP;
                	failURL = editRegionJSP;
                	
                    RegionManager.updateRegionAttributes(
                    		keyLong,
                            regionName,
                            regionComments);
            	}
                // Station types
            	else if (type.equalsIgnoreCase("stationType")) {
                	String stationTypeName = req.getParameter("st_name");
                	String stationTypeDescription = req.getParameter("st_description");
                	
                	successURL = editStationTypeJSP;
                	failURL = editStationTypeJSP;

                    StationTypeManager.updateStationTypeAttributes(
                    		keyLong,
                    		stationTypeName,
                    		stationTypeDescription);
                    SystemManager.updateStationTypeListVersion();
            	}
            	
    	    	// If success
                resp.sendRedirect(successURL + "?k=" + keyString + 
                		"&readonly=true&msg=success&action=" + action);
            }
            catch (MissingRequiredFieldsException mrfe) {
                resp.sendRedirect(failURL + "?etype=MissingInfo&k="
                        + keyString);
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}
