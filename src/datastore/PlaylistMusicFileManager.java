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

import com.google.appengine.api.datastore.Key;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the PlaylistMusicFile class.
 * 
 */

public class PlaylistMusicFileManager {
	
	private static final Logger log = 
        Logger.getLogger(PlaylistMusicFileManager.class.getName());
	
	/**
     * Get a PlaylistMusicFile instance from the datastore given the PlaylistMusicFile key.
     * @param key
     * 			: the PlaylistMusicFile's key
     * @return PlaylistMusicFile instance, null if PlaylistMusicFile is not found
     */
	public static PlaylistMusicFile getPlaylistMusicFile(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		PlaylistMusicFile playlistMusicFile;
		try  {
			playlistMusicFile = pm.getObjectById(PlaylistMusicFile.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return playlistMusicFile;
	}
	
	/**
     * Get ALL the playlistMusicFiles in the datastore from a specific playlist
     * and returns them in a List structure
     * @param playlistKey: 
     * 				the key of the playlist whose playlistMusicFiles will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all playlistMusicFiles in the datastore belonging to the given playlist
     * TODO: Fix "touching" of playlistMusicFiles
     */
	public static List<PlaylistMusicFile> getAllPlaylistMusicFilesFromPlaylist(Key playlistKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Playlist playlist = pm.getObjectById(Playlist.class, playlistKey);
		
        List<PlaylistMusicFile> result = null;
        try {
            result = playlist.getPlaylistMusicFiles();
            // Touch each branch
            for (PlaylistMusicFile playlistMusicFile : result) {
            	playlistMusicFile.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        result = sortPlaylistMusicFiles(result, ascendingOrder);
        return result;
    }
	
	/**
     * Put PlaylistMusicFile into datastore.
     * Stores the given PlaylistMusicFile instance in the datastore for this
     * playlist.
     * @param playlistKey
     * 			: the key of the Playlist where the playlistMusicFile will be added
     * @param playlistMusicFile
     * 			: the PlaylistMusicFile instance to playlist
     */
	public static void putPlaylistMusicFile(Key playlistKey, PlaylistMusicFile playlistMusicFile) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Playlist playlist = 
					pm.getObjectById(Playlist.class, playlistKey);
			Station station =
					pm.getObjectById(Station.class, playlistKey.getParent());
			tx.begin();
			playlist.addPlaylistMusicFile(playlistMusicFile);
			station.updatePlaylistVersion();
			tx.commit();
			log.info("PlaylistMusicFile \"" + 
					playlistMusicFile.getPlaylistMusicFileSequenceNumber() + 
					"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete PlaylistMusicFile from datastore.
    * Deletes the PlaylistMusicFile corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the PlaylistMusicFile instance to delete
    */
	public static void deletePlaylistMusicFile(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Playlist playlist = pm.getObjectById(Playlist.class, key.getParent());
			Station station =
					pm.getObjectById(Station.class, key.getParent().getParent());
			
			PlaylistMusicFile playlistMusicFile = pm.getObjectById(PlaylistMusicFile.class, key);
			Integer playlistMusicFileName = playlistMusicFile.getPlaylistMusicFileSequenceNumber();
			tx.begin();
			playlist.removePlaylistMusicFile(playlistMusicFile);
			station.updatePlaylistVersion();
			tx.commit();
			log.info("PlaylistMusicFile \"" + playlistMusicFileName + 
                     "\" deleted successfully from datastore.");
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
    * Update PlaylistMusicFile attributes.
    * Update's the given PlaylistMusicFile's attributes in the datastore.
    * @param key
    * 			: the key of the PlaylistMusicFile whose attributes will be updated
    * @param playlistMusicFileSequenceNumber
    * 			: the sequence number of this playlist music file
	* @throws MissingRequiredFieldsException 
    */
	public static void updatePlaylistMusicFileAttributes(
			Key key, 
			Integer playlistMusicFileSequenceNumber) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Station station = pm.getObjectById(Station.class, key.getParent().getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			PlaylistMusicFile playlistMusicFile = 
					pm.getObjectById(PlaylistMusicFile.class, key);
			tx.begin();
			playlistMusicFile.setPlaylistMusicFileSequenceNumber(
					playlistMusicFileSequenceNumber);
			station.updatePlaylistVersion();
			tx.commit();
			log.info("PlaylistMusicFile \"" + 
			playlistMusicFile.getPlaylistMusicFileSequenceNumber() + 
                     "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
	 * Sort the given playlistMusicFile list by release date using the
	 * BubbleSort algorithm.
	 * @param playlistMusicFiles:
	 * 				the list of playlistMusicFiles to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of playlistMusicFiles sorted by release date
	 */
	public static List<PlaylistMusicFile> sortPlaylistMusicFiles(
			List<PlaylistMusicFile> playlistMusicFiles, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < playlistMusicFiles.size(); i++) {
			for (int j = 1; j < (playlistMusicFiles.size() - i); j++) {
				Integer playlistMusicFile1SequenceNumber = 
						playlistMusicFiles.get(j - 1).getPlaylistMusicFileSequenceNumber();
				Integer playlistMusicFile2SequenceNumber = 
						playlistMusicFiles.get(j).getPlaylistMusicFileSequenceNumber();
				if (ascendingOrder) {
					if (playlistMusicFile1SequenceNumber.compareTo(
							playlistMusicFile2SequenceNumber) > 0) {
						PlaylistMusicFile tempPlaylistMusicFile = 
								playlistMusicFiles.get(j - 1);
						playlistMusicFiles.set(j - 1, playlistMusicFiles.get(j));
						playlistMusicFiles.set(j, tempPlaylistMusicFile);
					}
				}
				else {
					if (playlistMusicFile1SequenceNumber.compareTo(
							playlistMusicFile2SequenceNumber) < 0) {
						PlaylistMusicFile tempPlaylistMusicFile = 
								playlistMusicFiles.get(j - 1);
						playlistMusicFiles.set(j - 1, playlistMusicFiles.get(j));
						playlistMusicFiles.set(j, tempPlaylistMusicFile);
					}
				}
			}
		}
		
		return playlistMusicFiles;
	}
	
}
