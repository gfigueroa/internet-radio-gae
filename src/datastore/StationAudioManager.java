/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;

import datastore.StationAudio.StationAudioType;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.ReferentialIntegrityException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the StationAudio class.
 * 
 */

public class StationAudioManager {
	
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
	private static final Logger log = 
        Logger.getLogger(StationAudioManager.class.getName());
	
	/**
     * Get a StationAudio instance from the datastore given the StationAudio key.
     * @param key
     * 			: the StationAudio's key
     * @return StationAudio instance, null if StationAudio is not found
     */
	public static StationAudio getStationAudio(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StationAudio stationAudio;
		try  {
			stationAudio = pm.getObjectById(StationAudio.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return stationAudio;
	}
	
	/**
     * Get ALL the stationAudios in the datastore from a specific station
     * and returns them in a List structure
     * @param stationKey: 
     * 				the key of the station whose stationAudios will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all stationAudios in the datastore belonging to the given station
     * TODO: Fix "touching" of stationAudios
     */
	public static List<StationAudio> getAllStationAudiosFromStation(Key stationKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Station station = pm.getObjectById(Station.class, stationKey);
		
        List<StationAudio> result = null;
        try {
            result = station.getStationAudios();
            // Touch each branch
            for (StationAudio stationAudio : result) {
            	stationAudio.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        result = sortStationAudios(result, ascendingOrder);
        return result;
    }
	
	/**
     * Put StationAudio into datastore.
     * Stores the given StationAudio instance in the datastore for this
     * station.
     * @param stationKey
     * 			: the key of the Station where the stationAudio will be added
     * @param stationAudio
     * 			: the StationAudio instance to station
	 * @throws ObjectExistsInDatastoreException 
     */
	public static void putStationAudio(Key stationKey, StationAudio stationAudio) 
			throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if station audio already exists in datastore
		if (stationAudioExists(stationKey, stationAudio.getStationAudioName())) {
			throw new ObjectExistsInDatastoreException(stationAudio, "A station audio " +
					"with this title has already been added.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = 
					pm.getObjectById(Station.class, stationKey);
			tx.begin();
			station.addStationAudio(stationAudio);
			station.updateStationAudioVersion();
			tx.commit();
			log.info("StationAudio \"" + stationAudio.getStationAudioName() + 
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
	 * Checks if a station audio with a particular name already exists
	 * in the given station
	 * @param stationKey
	 * 			: the key of the station to check
	 * @param stationAudioName
	 * 			: the stationAudio name
	 * @return true if a station audio with this name exists in the
	 * 			given station, false otherwise
	 */
	public static boolean stationAudioExists(Key stationKey, 
			String stationAudioName) {
		
		List<StationAudio> stationAudios = 
				StationAudioManager.getAllStationAudiosFromStation(stationKey, true);
		for (StationAudio stationAudio : stationAudios) {
			if (stationAudio.getStationAudioName().equals(stationAudioName)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
    * Delete StationAudio from datastore.
    * Deletes the StationAudio corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the StationAudio instance to delete
	* @throws ReferentialIntegrityException 
    */
	public static void deleteStationAudio(Key key) 
			throws ReferentialIntegrityException {	
		
		if (stationAudioIsReferenced(key)) {
			throw new ReferentialIntegrityException(key, "This station audio is " +
					"being referenced by a program, and cannot be deleted.");
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = pm.getObjectById(Station.class, key.getParent());
			
			StationAudio stationAudio = pm.getObjectById(StationAudio.class, key);
			String stationAudioName = stationAudio.getStationAudioName();
			BlobKey stationAudioMultimediaContent = stationAudio.getStationAudioMultimediaContent();
			
			tx.begin();
			station.removeStationAudio(stationAudio);
			station.updateStationAudioVersion();
			tx.commit();
			
			if (stationAudioMultimediaContent != null) {
				blobstoreService.delete(stationAudioMultimediaContent);
			}
			
			log.info("StationAudio \"" + stationAudioName + 
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
	 * Check whether this station audio is being referenced (used)
	 * in any program in the given station.
	 * @param stationAudioKey:
	 * 			The stationAudio key to check
	 * @return True if the stationAudio is being used in any program,
	 * 			False otherwise
	 */
	public static boolean stationAudioIsReferenced(Key stationAudioKey) {
		
		List<Program> programs = 
				ProgramManager.getAllProgramsFromStation(
						stationAudioKey.getParent(), true);
		for (Program program : programs) {
			
			// Check MainTrack
			MainTrack mainTrack = program.getMainTrack();
			if (mainTrack != null) {
				Key mainTrackStationAudioKey  = mainTrack.getStationAudio();
				if (mainTrackStationAudioKey != null &&
						mainTrackStationAudioKey.equals(stationAudioKey)) {
					return true;
				}
			}
			
			// Check SecondaryTracks
			List<SecondaryTrack> secondaryTracks =
					SecondaryTrackManager.getAllSecondaryTracksFromProgram(
							program.getKey(), true);
			for (SecondaryTrack secondaryTrack : secondaryTracks) {
				Key secondaryTrackStationAudioKey = secondaryTrack.getStationAudio();
				if (secondaryTrackStationAudioKey != null &&
						secondaryTrackStationAudioKey.equals(stationAudioKey)) {
					return true;
				}
			}
		}
		
		// If stationAudio not found in any program, then return false
		return false;
	}

	/**
    * Update StationAudio attributes.
    * Update's the given StationAudio's attributes in the datastore.
    * @param key
    * 			: the key of the StationAudio whose attributes will be updated
    * @param stationAudioName
    * 			: stationAudio name
    * @param stationAudioMultimediaContent
    * 			: stationAudio multimedia content
    * @param stationAudioDuration
    * 			: stationAudio duration
    * @param stationAudioFormat
    * 			: stationAudio format
	* @throws MissingRequiredFieldsException 
	 * @throws ObjectExistsInDatastoreException 
    */
	public static void updateStationAudioAttributes(
			Key key,
			StationAudio.StationAudioType stationAudioType,
			String stationAudioName, 
    		BlobKey stationAudioMultimediaContent,
    		Double stationAudioDuration,
    		String stationAudioFormat) 
                       throws MissingRequiredFieldsException, 
                       ObjectExistsInDatastoreException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Station station = pm.getObjectById(Station.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			StationAudio stationAudio = pm.getObjectById(StationAudio.class, key);
			
			// Check if station audio already exists in datastore
			if (stationAudioExists(key.getParent(), stationAudioName) && 
					!stationAudio.getStationAudioName().equals(stationAudioName)) {
				throw new ObjectExistsInDatastoreException(stationAudio, "A station audio " +
						"with this title has already been added.");
			}
			
			BlobKey oldStationAudioMultimediaContent = 
					stationAudio.getStationAudioMultimediaContent();
			
			tx.begin();
			stationAudio.setStationAudioType(stationAudioType);
			stationAudio.setStationAudioName(stationAudioName);
			stationAudio.setStationAudioMultimediaContent(stationAudioMultimediaContent);
			stationAudio.setStationAudioDuration(stationAudioDuration);
			stationAudio.setStationAudioFormat(stationAudioFormat);
			station.updateStationAudioVersion();
			tx.commit();
			
			if (!oldStationAudioMultimediaContent.equals(stationAudioMultimediaContent) &&
					oldStationAudioMultimediaContent != null) {
				blobstoreService.delete(oldStationAudioMultimediaContent);
			}
			
			log.info("StationAudio \"" + stationAudio.getStationAudioName() + 
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
	 * Sort the given stationAudio list by type and name using the
	 * BubbleSort algorithm.
	 * @param stationAudios:
	 * 				the list of stationAudios to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of stationAudios sorted by release date
	 */
	public static List<StationAudio> sortStationAudios(
			List<StationAudio> stationAudios, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < stationAudios.size(); i++) {
			for (int j = 1; j < (stationAudios.size() - i); j++) {
				String stationAudio1Name = stationAudios.get(j - 1).getStationAudioName();
				String stationAudio2Name = stationAudios.get(j).getStationAudioName();
				if (ascendingOrder) {
					if (stationAudio1Name.compareTo(stationAudio2Name) > 0) {
						StationAudio tempStationAudio = stationAudios.get(j - 1);
						stationAudios.set(j - 1, stationAudios.get(j));
						stationAudios.set(j, tempStationAudio);
					}
				}
				else {
					if (stationAudio1Name.compareTo(stationAudio2Name) < 0) {
						StationAudio tempStationAudio = stationAudios.get(j - 1);
						stationAudios.set(j - 1, stationAudios.get(j));
						stationAudios.set(j, tempStationAudio);
					}
				}
			}
		}
		
		// Arrange by type
		ArrayList<StationAudio> finalStationAudios = new ArrayList<>();
		ArrayList<StationAudio> musicList = new ArrayList<>();
		ArrayList<StationAudio> voiceList = new ArrayList<>();
		for (StationAudio stationAudio : stationAudios) {
			StationAudioType stationAudioType = stationAudio.getStationAudioType();
			switch (stationAudioType) {
				case MUSIC:
					musicList.add(stationAudio);
					break;
				case VOICE:
					voiceList.add(stationAudio);
					break;
				default:
					;
			}
		}
		finalStationAudios.addAll(musicList);
		finalStationAudios.addAll(voiceList);
		
		return finalStationAudios;
	}
	
}
