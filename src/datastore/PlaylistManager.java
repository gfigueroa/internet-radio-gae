/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the Playlist class.
 */
public class PlaylistManager {
	
	private static final Logger log = 
        Logger.getLogger(PlaylistManager.class.getName());

	/**
	 * Get a playlist using its complex key (includes the Station key as well)
	 * @param key
	 *        : The playlist's key
	 * @return Playlist 
	 */
	public static Playlist getPlaylist(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Playlist playlist;
		try  {
			playlist = pm.getObjectById(Playlist.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return playlist;
	}
	
	/**
     * Get ALL the Playlists in the datastore from a specific station
     * and returns them in a List structure
     * @param stationKey: 
     * 				the key of the station whose playlists will be retrieved
     * @return all playlists in the datastore belonging to the given station
     * TODO: Fix "touching" of playlists
     */
	public static List<Playlist> getStationPlaylists(Key stationKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Station station = pm.getObjectById(Station.class, stationKey);
		
        List<Playlist> result = null;
        try {
            result = station.getPlaylists();
            // Touch each playlist
            for (Playlist playlist : result) {
            	playlist.getPlaylistName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
    * Add playlist to a Station.
    * Add a new playlist in the datastore for this Station.
    * @param email
    * 			: the email of the Station where the playlist will be added
    * @param playlist
    * 			: the playlist to be added
    */
	public static void putPlaylist(Email stationEmail, Playlist playlist) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
					stationEmail.getEmail());
			Station station = pm.getObjectById(Station.class, key);
			tx.begin();
			station.addPlaylist(playlist);
			tx.commit();
			log.info("Playlist \"" + playlist.getPlaylistName() + "\" added to Station \"" + 
					stationEmail.getEmail() + "\" in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Add playlist to a Station.
    * Add a new playlist in the datastore for this Station.
    * @param stationKey
    * 			: the key of the Station where the playlist will be added
    * @param playlist
    * 			: the playlist to be added
    */
	public static void putPlaylist(Key stationKey, Playlist playlist) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = pm.getObjectById(Station.class, stationKey);
			tx.begin();
			station.addPlaylist(playlist);
			station.updatePlaylistVersion();
			tx.commit();
			log.info("Playlist \"" + playlist.getPlaylistName() + "\" added to Station \"" + 
					station.getStationName() + "\" in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete playlist.
    * Delete a playlist in the datastore.
    * @param key
    * 			: the key of the playlist to delete (includes Station key)
    */
	public static void deletePlaylist(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = pm.getObjectById(Station.class, key.getParent());
			Playlist playlist = pm.getObjectById(Playlist.class, key);
			String playlistName = playlist.getPlaylistName();
			tx.begin();
			station.removePlaylist(playlist);
			station.updatePlaylistVersion();
			tx.commit();
			log.info("Playlist \"" + playlistName + "\" deleted from Station \"" + 
					station.getUser().getUserEmail().getEmail() + "\" in datastore.");
		} 
		catch (InexistentObjectException e) {
			e.printStackTrace();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Playlist attributes.
    * Updates the given Playlist's attributes in the datastore.
    * @param key
    * 			: the key of the Playlist whose attributes will be updated
    * @param playlistName
    * 			: the new name to give to the Playlist
	* @throws MissingRequiredFieldsException
    */
	public static void updatePlaylistAttributes(Key key, String playlistName)
			throws MissingRequiredFieldsException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Playlist playlist = pm.getObjectById(Playlist.class, key);
			Station station = pm.getObjectById(Station.class, key.getParent());
			tx.begin();
			playlist.setPlaylistName(playlistName);
			station.updatePlaylistVersion();
			tx.commit();
			log.info("Playlist \"" + playlistName + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
