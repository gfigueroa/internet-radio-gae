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

import datastore.SecondaryTrack.SecondaryTrackType;
import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the SecondaryTrack class.
 * 
 */

public class SecondaryTrackManager {

	private static final Logger log = 
        Logger.getLogger(SecondaryTrackManager.class.getName());
	
	/**
     * Get a SecondaryTrack instance from the datastore given the SecondaryTrack key.
     * @param key
     * 			: the SecondaryTrack's key
     * @return SecondaryTrack instance, null if SecondaryTrack is not found
     */
	public static SecondaryTrack getSecondaryTrack(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SecondaryTrack secondaryTrack;
		try  {
			secondaryTrack = pm.getObjectById(SecondaryTrack.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return secondaryTrack;
	}
	
	/**
     * Get ALL the secondaryTracks in the datastore from a specific program
     * and returns them in a List structure
     * @param programKey: 
     * 				the key of the program whose secondaryTracks will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all secondaryTracks in the datastore belonging to the given program
     * TODO: Fix "touching" of secondaryTracks
     */
	public static List<SecondaryTrack> getAllSecondaryTracksFromProgram(Key programKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Program program = pm.getObjectById(Program.class, programKey);
		
        List<SecondaryTrack> result = null;
        try {
            result = program.getSecondaryTracks();
            // Touch each secondary track
            for (SecondaryTrack secondaryTrack : result) {
            	secondaryTrack.getKey();
            }
        }
        finally {
        	pm.close();
        }

        result = sortSecondaryTracks(result, ascendingOrder);
        return result;
    }
	
	/**
     * Put SecondaryTrack into datastore.
     * Stores the given SecondaryTrack instance in the datastore for this
     * program.
     * @param programKey
     * 			: the key of the Program where the secondaryTrack will be added
     * @param secondaryTrack
     * 			: the SecondaryTrack instance to program
     */
	public static void putSecondaryTrack(Key programKey, SecondaryTrack secondaryTrack) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = pm.getObjectById(Program.class, programKey);
			Channel channel =
					pm.getObjectById(Channel.class, program.getKey().getParent());
			tx.begin();
			program.addSecondaryTrack(secondaryTrack);
			channel.updateProgramVersion();
			tx.commit();
			log.info("SecondaryTrack stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete SecondaryTrack from datastore.
    * Deletes the SecondaryTrack corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the SecondaryTrack instance to delete
    */
	public static void deleteSecondaryTrack(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = pm.getObjectById(Program.class, key.getParent());
			Channel channel =
					pm.getObjectById(Channel.class, key.getParent().getParent());
			
			SecondaryTrack secondaryTrack = pm.getObjectById(SecondaryTrack.class, key);
			
			tx.begin();
			program.removeSecondaryTrack(secondaryTrack);
			channel.updateProgramVersion();
			tx.commit();
			
			log.info("SecondaryTrack deleted successfully from datastore.");
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
    * Update SecondaryTrack attributes.
    * Update's the given SecondaryTrack's attributes in the datastore.
    * @param key
    * 			: the key of the SecondaryTrack whose attributes will be updated
     * @param secondaryTrackType
     * 			: secondaryTrack type
     * @param stationAudio
     * 			: stationAudio key
     * @param musicFile
     * 			: the ID of the music file
     * @param secondaryTrackStartingTime
     * 			: the starting time of the secondary track
     * @param secondaryTrackDuration
     * 			: secondaryTrack duration
     * @param secondaryTrackFadeInSteps
     * 			: secondaryTrack number of steps in fade-In
     * @param secondaryTrackFadeInDuration
     * 			: secondaryTrack duration of fade-In effect
     * @param secondaryTrackFadeInPercentage
     * 			: secondaryTrack percentage of fade-In effect
     * @param secondaryTrackFadeOutSteps
     * 			: secondaryTrack number of steps in fade-out
     * @param secondaryTrackFadeOutDuration
     * 			: secondaryTrack duration of fade-out effect
     * @param secondaryTrackFadeOutPercentage
     * 			: secondaryTrack percentage of fade-out effect
     * @param secondaryTrackOffset
     * 			: secondaryTrack offset
	* @throws MissingRequiredFieldsException 
    */
	public static void updateSecondaryTrackAttributes(
			Key key, SecondaryTrackType secondaryTrackType,
    		Key stationAudio, Long musicFile,
    		Double secondaryTrackStartingTime,
    		Double secondaryTrackDuration, Integer secondaryTrackFadeInSteps,
    		Double secondaryTrackFadeInDuration, Double secondaryTrackFadeInPercentage,
    		Integer secondaryTrackFadeOutSteps, Double secondaryTrackFadeOutDuration, 
    		Double secondaryTrackFadeOutPercentage, Double secondaryTrackOffset) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Channel channel = pm.getObjectById(Channel.class, key.getParent().getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			SecondaryTrack secondaryTrack = pm.getObjectById(SecondaryTrack.class, key);
			
			tx.begin();
			secondaryTrack.setSecondaryTrackType(secondaryTrackType);
			secondaryTrack.setStationAudio(stationAudio, musicFile);
			secondaryTrack.setMusicFile(musicFile, stationAudio);
			secondaryTrack.setSecondaryTrackStartingTime(secondaryTrackStartingTime);
			secondaryTrack.setSecondaryTrackDuration(secondaryTrackDuration);
			secondaryTrack.setSecondaryTrackFadeInSteps(secondaryTrackFadeInSteps);
			secondaryTrack.setSecondaryTrackFadeInDuration(secondaryTrackFadeInDuration);
			secondaryTrack.setSecondaryTrackFadeInPercentage(secondaryTrackFadeInPercentage);
			secondaryTrack.setSecondaryTrackFadeOutSteps(secondaryTrackFadeOutSteps);
			secondaryTrack.setSecondaryTrackFadeOutDuration(secondaryTrackFadeOutDuration);
			secondaryTrack.setSecondaryTrackFadeOutPercentage(secondaryTrackFadeOutPercentage);
			secondaryTrack.setSecondaryTrackOffset(secondaryTrackOffset);
			channel.updateProgramVersion();
			tx.commit();
			
			log.info("SecondaryTrack attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
	 * Sort the given secondaryTrack list by release date using the
	 * BubbleSort algorithm.
	 * @param secondaryTracks:
	 * 				the list of secondaryTracks to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of secondaryTracks sorted by release date
	 */
	public static List<SecondaryTrack> sortSecondaryTracks(
			List<SecondaryTrack> secondaryTracks, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < secondaryTracks.size(); i++) {
			for (int j = 1; j < (secondaryTracks.size() - i); j++) {
				Double secondaryTrack1Date = secondaryTracks.get(j - 1).getSecondaryTrackStartingTime();
				Double secondaryTrack2Date = secondaryTracks.get(j).getSecondaryTrackStartingTime();
				if (ascendingOrder) {
					if (secondaryTrack1Date.compareTo(secondaryTrack2Date) > 0) {
						SecondaryTrack tempSecondaryTrack = secondaryTracks.get(j - 1);
						secondaryTracks.set(j - 1, secondaryTracks.get(j));
						secondaryTracks.set(j, tempSecondaryTrack);
					}
				}
				else {
					if (secondaryTrack1Date.compareTo(secondaryTrack2Date) < 0) {
						SecondaryTrack tempSecondaryTrack = secondaryTracks.get(j - 1);
						secondaryTracks.set(j - 1, secondaryTracks.get(j));
						secondaryTracks.set(j, tempSecondaryTrack);
					}
				}
			}
		}
		
		return secondaryTracks;
	}
	
}
