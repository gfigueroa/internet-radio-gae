/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the MusicFile class.
 * 
 */

public class MusicFileManager {
	
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
	private static final Logger log = Logger.getLogger(MusicFileManager.class.getName());
	
	/**
     * Get a MusicFile instance from the datastore given the MusicFile key.
     * @param key
     * 			: the musicFile's key
     * @return musicFile instance, null if musicFile is not found
     */
	public static MusicFile getMusicFile(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MusicFile musicFile;
		try  {
			musicFile = pm.getObjectById(MusicFile.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return musicFile;
	}
	
	/**
     * Get all MusicFile instances from the datastore.
     * @return All MusicFile instances
     * TODO: Make "touching" of musicFiles more efficient
     */
	@SuppressWarnings("unchecked")
	public static List<MusicFile> getAllMusicFiles() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(MusicFile.class);

        List<MusicFile> musicFiles = null;
        try {
        	musicFiles = (List<MusicFile>) query.execute();
            // touch all elements
            for (MusicFile r : musicFiles)
                r.getMusicFileTitle();
        } finally {
        	pm.close();
            query.closeAll();
        }

        return musicFiles;
    }
	
    /**
     * Get ALL MusicFile instances in the datastore that belong to
     * the given genre and returns them in a List structure
     * @parameter genre:
     * 			the key of the genre
     * @return all music files belonging to the genre
     * TODO: make more efficient "touching" of the station
     */
	@SuppressWarnings("unchecked")
	public static List<MusicFile> getMusicFilesByGenre(Long genre) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(MusicFile.class);
        query.setFilter("genre == type");
        query.declareParameters("Long type");

        List<MusicFile> result = null;
        try {
            result = (List<MusicFile>) query.execute(genre);
            // touch
            for (MusicFile musicFile : result)
            	musicFile.getMusicFileTitle();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put MusicFile into datastore.
     * Stores the given musicFile instance in the datastore calling the PersistenceManager's
     * makePersistent() method.
     * @param musicFile
     * 			: the musicFile instance to store
	 * @throws ObjectExistsInDatastoreException 
     */
	public static void putMusicFile(MusicFile musicFile) 
			throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if music file already exists in datastore
		if (DatastoreManager.entityExists(MusicFile.class, "String", 
				"musicFileTitle", musicFile.getMusicFileTitle())) {
			throw new ObjectExistsInDatastoreException(musicFile, "A music file " +
					"with this title has already been added to the Music Library.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(musicFile);
			tx.commit();
			log.info("MusicFile \"" + musicFile.getMusicFileTitle() + 
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
    * Delete MusicFile from datastore.
    * Deletes the musicFile corresponding to the given key
    * from the datastore calling the PersistenceManager's deletePersistent() method.
    * @param key
    * 			: the key of the musicFile instance to delete
    */
	public static void deleteMusicFile(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			MusicFile musicFile = pm.getObjectById(MusicFile.class, key);
			BlobKey musicFileFileKey = musicFile.getMusicFileFile();
			String musicFileName = musicFile.getMusicFileTitle();
			
			tx.begin();
			pm.deletePersistent(musicFile);
			tx.commit();
			
			if (musicFileFileKey != null) {
				blobstoreService.delete(musicFileFileKey);
			}
			
			log.info("MusicFile \"" + musicFileName + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update MusicFile attributes.
    * Update's the given musicFile's attributes in the datastore.
    * @param key
    * 			: the key of the musicFile whose attributes will be updated
     * @param musicFileFile
     * 			: musicFile file
     * @param musicFileTitle
     * 			: musicFile title
     * @param musicFileArtist
     * 			: musicFile artist
     * @param musicFileAlbum
     * 			: musicFile album
     * @param musicFileAlbumArtist
     * 			: musicFile album artist
     * @param musicFileYear
     * 			: musicFile year
     * @param musicFileComposer
     * 			: musicFile composer
     * @param musicFilePublisher
     * 			: musicFile publisher
     * @param musicFileDuration
     * 			: musicFile duration
     * @param musicFileFormat
     * 			: musicFile format
     * @param musicFileComments
     * 			: musicFile comments
	* @throws MissingRequiredFieldsException 
	 * @throws ObjectExistsInDatastoreException 
    */
	public static void updateMusicFileAttributes(Long key, 
			Long genre, BlobKey musicFileFile,
    		String musicFileTitle, String musicFileArtist,
    		String musicFileAlbum, String musicFileAlbumArtist,
    		Integer musicFileYear, String musicFileComposer,
    		String musicFilePublisher, Double musicFileDuration,
    		String musicFileFormat, String musicFileComments) 
    				throws MissingRequiredFieldsException, 
    				ObjectExistsInDatastoreException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			MusicFile musicFile = pm.getObjectById(MusicFile.class, key);
			
			// Check if music file already exists in datastore
			if (DatastoreManager.entityExists(MusicFile.class, "String", 
					"musicFileTitle", musicFileTitle) &&
					!musicFile.getMusicFileTitle().equals(musicFileTitle)) {
				throw new ObjectExistsInDatastoreException(musicFile, "A music file " +
						"with this title has already been added to the Music Library.");
			}
			
			BlobKey oldMusicFileFileKey = musicFile.getMusicFileFile();
			tx.begin();
			musicFile.setGenre(genre);
			musicFile.setMusicFileFile(musicFileFile);
			musicFile.setMusicFileTitle(musicFileTitle);
			musicFile.setMusicFileArtist(musicFileArtist);
			musicFile.setMusicFileAlbum(musicFileAlbum);
			musicFile.setMusicFileAlbumArtist(musicFileAlbumArtist);
			musicFile.setMusicFileYear(musicFileYear);
			musicFile.setMusicFileComposer(musicFileComposer);
			musicFile.setMusicFilePublisher(musicFilePublisher);
			musicFile.setMusicFileDuration(musicFileDuration);
			musicFile.setMusicFileFormat(musicFileFormat);
			musicFile.setMusicFileComments(musicFileComments);
			tx.commit();
			
			// Delete old Customer Logo
			if (!oldMusicFileFileKey.equals(musicFileFile) && oldMusicFileFileKey != null) {
				blobstoreService.delete(oldMusicFileFileKey);
			}
			log.info("MusicFile \"" + musicFileTitle + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
