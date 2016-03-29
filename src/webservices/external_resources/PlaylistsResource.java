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
import webservices.datastore_simple.PlaylistSimple;
import webservices.datastore_simple.PlaylistSimple.PlaylistMusicFileSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Playlist;
import datastore.PlaylistManager;
import datastore.PlaylistMusicFile;
import datastore.PlaylistMusicFileManager;

/**
 * This class represents the list of playlist
 * as a Resource with only one representation
 */

public class PlaylistsResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(PlaylistsResource.class.getName());
	
	/**
	 * Returns the playlist list as a JSON object.
	 * @return An ArrayList of playlist in JSON format
	 */
    @Get("json")
    public ArrayList<PlaylistSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	        
	    Key stationKey = KeyFactory.stringToKey(searchString);
	    log.info("Query: " + searchBy + "=" + searchString);

	    // Get all the playlists from every station
	    List<Playlist> playlistList = PlaylistManager.getStationPlaylists(stationKey);

        ArrayList<PlaylistSimple> playlistListSimple = new ArrayList<PlaylistSimple>();
        for (Playlist playlist : playlistList) {

        	// Create PlaylistMusicFiles
        	List<PlaylistMusicFile> playlistMusicFiles = 
        			PlaylistMusicFileManager.getAllPlaylistMusicFilesFromPlaylist(
        					playlist.getKey(), true);
        	ArrayList<PlaylistMusicFileSimple> playlistMusicFilesSimple = 
        			new ArrayList<PlaylistMusicFileSimple>();
        	for (PlaylistMusicFile playlistMusicFile : playlistMusicFiles) {
        		PlaylistMusicFileSimple playlistMusicFileSimple = 
        				new PlaylistMusicFileSimple(
        						playlistMusicFile.getMusicFileKey(),
        						playlistMusicFile.getPlaylistMusicFileSequenceNumber()
        						);
        		playlistMusicFilesSimple.add(playlistMusicFileSimple);
        	}
        	
        	PlaylistSimple playlistSimple = new PlaylistSimple(
        			KeyFactory.keyToString(playlist.getKey()),
        			playlist.getPlaylistName(),
        			playlistMusicFilesSimple,
        			DateManager.printDateAsString(
        					playlist.getPlaylistCreationDate()),
        			DateManager.printDateAsString(
        					playlist.getPlaylistModificationDate())
        			);
        	
        	playlistListSimple.add(playlistSimple);
        }
        
        return playlistListSimple;
    }

}