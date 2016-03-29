/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import datastore.MainTrack.MainTrackType;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the MainTrack class.
 * 
 */

public class MainTrackManager {

	private static final Logger log = 
        Logger.getLogger(MainTrackManager.class.getName());
	
	/**
     * Get a MainTrack instance from the datastore given the MainTrack key.
     * @param key
     * 			: the MainTrack's key
     * @return MainTrack instance, null if MainTrack is not found
     */
	public static MainTrack getMainTrack(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MainTrack mainTrack;
		try  {
			mainTrack = pm.getObjectById(MainTrack.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return mainTrack;
	}
	
	/**
     * Get the mainTracks in the datastore from a specific program
     * @param programKey: 
     * 				the key of the program whose mainTrack will be retrieved
     * @return mainTrack in the datastore belonging to the given program
     */
	public static MainTrack getMainTrackFromProgram(Key programKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Program program = pm.getObjectById(Program.class, programKey);
		
        MainTrack mainTrack = null;
        try {
            mainTrack = program.getMainTrack();
        }
        finally {
        	pm.close();
        }

        return mainTrack;
    }
	
	/**
    * Delete MainTrack from datastore.
    * Deletes the MainTrack corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the MainTrack instance to delete
    */
	public static void deleteMainTrack(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = pm.getObjectById(Program.class, key.getParent());
			Channel channel =
					pm.getObjectById(Channel.class, key.getParent().getParent());
			
			MainTrack mainTrack = pm.getObjectById(MainTrack.class, key);
			
			tx.begin();
			pm.deletePersistent(mainTrack);
			program.setMainTrack(null);
			channel.updateProgramVersion();
			tx.commit();
			
			log.info("MainTrack deleted successfully from datastore.");
		}
		catch (Exception e) {
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
    * Update MainTrack attributes.
    * Update's the given MainTrack's attributes in the datastore.
    * @param key
    * 			: the key of the MainTrack whose attributes will be updated
     * @param mainTrackType
     * 			: mainTrack type
     * @param stationAudio
     * 			: stationAudio key
     * @param musicFile
     * 			: the ID of the music file
     * @param playlist
     * 			: the ID of the playlist
     * @param mainTrackDuration
     * 			: mainTrack duration
     * @param mainTrackFadeOutSteps
     * 			: mainTrack number of steps in fade-out
     * @param mainTrackFadeOutDuration
     * 			: mainTrack duration of fade-out effect
     * @param mainTrackFadeOutPercentage
     * 			: mainTrack percentage of fade-out effect
     * @param mainTrackFadeInSteps
     * 			: mainTrack number of steps in fade-In
     * @param mainTrackFadeInDuration
     * 			: mainTrack duration of fade-In effect
     * @param mainTrackFadeInPercentage
     * 			: mainTrack percentage of fade-In effect
	* @throws MissingRequiredFieldsException 
    */
	public static void updateMainTrackAttributes(
			Key key, MainTrackType mainTrackType,
    		Key stationAudio, Long musicFile,
    		Key playlist, Double mainTrackDuration,
    		Integer mainTrackFadeOutSteps, 
    		Double mainTrackFadeOutDuration, 
    		Double mainTrackFadeOutPercentage,
    		Integer mainTrackFadeInSteps, 
    		Double mainTrackFadeInDuration, 
    		Double mainTrackFadeInPercentage) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Channel channel = pm.getObjectById(Channel.class, key.getParent().getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			MainTrack mainTrack = pm.getObjectById(MainTrack.class, key);
			
			tx.begin();
			mainTrack.setMainTrackType(mainTrackType);
			mainTrack.setStationAudio(stationAudio, musicFile, playlist);
			mainTrack.setMusicFile(musicFile, stationAudio, playlist);
			mainTrack.setPlaylist(playlist, stationAudio, musicFile);
			mainTrack.setMainTrackDuration(mainTrackDuration);
			mainTrack.setMainTrackFadeOutSteps(mainTrackFadeOutSteps);
			mainTrack.setMainTrackFadeOutDuration(mainTrackFadeOutDuration);
			mainTrack.setMainTrackFadeOutPercentage(mainTrackFadeOutPercentage);
			mainTrack.setMainTrackFadeInSteps(mainTrackFadeInSteps);
			mainTrack.setMainTrackFadeInDuration(mainTrackFadeInDuration);
			mainTrack.setMainTrackFadeInPercentage(mainTrackFadeInPercentage);
			channel.updateProgramVersion();
			tx.commit();
			
			log.info("MainTrack attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
