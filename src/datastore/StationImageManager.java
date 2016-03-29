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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.ReferentialIntegrityException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the StationImage class.
 * 
 */

public class StationImageManager {
	
    private static final BlobstoreService blobstoreService = 
        	BlobstoreServiceFactory.getBlobstoreService();
	private static final Logger log = 
        Logger.getLogger(StationImageManager.class.getName());
	
	/**
     * Get a StationImage instance from the datastore given the StationImage key.
     * @param key
     * 			: the StationImage's key
     * @return StationImage instance, null if StationImage is not found
     */
	public static StationImage getStationImage(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StationImage stationImage;
		try  {
			stationImage = pm.getObjectById(StationImage.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return stationImage;
	}
	
	/**
     * Get ALL the stationImages in the datastore from a specific station
     * and returns them in a List structure
     * @param stationKey: 
     * 				the key of the station whose stationImages will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all stationImages in the datastore belonging to the given station
     * TODO: Fix "touching" of stationImages
     */
	public static List<StationImage> getAllStationImagesFromStation(Key stationKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Station station = pm.getObjectById(Station.class, stationKey);
		
        List<StationImage> result = null;
        try {
            result = station.getStationImages();
            // Touch each branch
            for (StationImage stationImage : result) {
            	stationImage.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        result = sortStationImages(result, ascendingOrder);
        return result;
    }
	
	/**
     * Put StationImage into datastore.
     * Stores the given StationImage instance in the datastore for this
     * station.
     * @param stationKey
     * 			: the key of the Station where the stationImage will be added
     * @param stationImage
     * 			: the StationImage instance to station
	 * @throws ObjectExistsInDatastoreException 
     */
	public static void putStationImage(Key stationKey, StationImage stationImage) 
			throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if station image already exists in datastore
		if (stationImageExists(stationKey, stationImage.getStationImageName())) {
			throw new ObjectExistsInDatastoreException(stationImage, "A station image " +
					"with this title has already been added.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = 
					pm.getObjectById(Station.class, stationKey);
			tx.begin();
			station.addStationImage(stationImage);
			station.updateStationImageVersion();
			tx.commit();
			log.info("StationImage \"" + stationImage.getStationImageName() + 
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
	 * Checks if a station image with a particular name already exists
	 * in the given station
	 * @param stationKey
	 * 			: the key of the station to check
	 * @param stationImageName
	 * 			: the stationImage name
	 * @return true if a station image with this name exists in the
	 * 			given station, false otherwise
	 */
	public static boolean stationImageExists(Key stationKey, 
			String stationImageName) {
		
		List<StationImage> stationImages = 
				StationImageManager.getAllStationImagesFromStation(stationKey, true);
		for (StationImage stationImage : stationImages) {
			if (stationImage.getStationImageName().equals(stationImageName)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
    * Delete StationImage from datastore.
    * Deletes the StationImage corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the StationImage instance to delete
	 * @throws ReferentialIntegrityException 
    */
	public static void deleteStationImage(Key key) 
			throws ReferentialIntegrityException {	
		
		if (stationImageIsReferenced(key)) {
			throw new ReferentialIntegrityException(key, "This station image is " +
					"being referenced by a program, and cannot be deleted.");
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = pm.getObjectById(Station.class, key.getParent());
			
			StationImage stationImage = pm.getObjectById(StationImage.class, key);
			String stationImageName = stationImage.getStationImageName();
			BlobKey stationImageMultimediaContent = stationImage.getStationImageMultimediaContent();
			
			tx.begin();
			station.removeStationImage(stationImage);
			station.updateStationImageVersion();
			tx.commit();
			
			if (stationImageMultimediaContent != null) {
				blobstoreService.delete(stationImageMultimediaContent);
			}
			
			log.info("StationImage \"" + stationImageName + 
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
	 * Check whether this station image is being referenced (used)
	 * in any program in the given station.
	 * @param stationImageKey:
	 * 			The stationImage key to check
	 * @return True if the stationImage is being used in any program,
	 * 			False otherwise
	 */
	public static boolean stationImageIsReferenced(Key stationImageKey) {
		
		List<Program> programs = 
				ProgramManager.getAllProgramsFromStation(
						stationImageKey.getParent(), true);
		for (Program program : programs) {
			
			// Check Slides
			List<Slide> slides =
					SlideManager.getAllSlidesFromProgram(
							program.getKey(), true);
			for (Slide slide : slides) {
				Key slideStationImageKey = slide.getStationImage();
				if (slideStationImageKey != null &&
						slideStationImageKey.equals(stationImageKey)) {
					return true;
				}
			}
		}
		
		// If stationImage not found in any program, then return false
		return false;
	}

	/**
    * Update StationImage attributes.
    * Update's the given StationImage's attributes in the datastore.
    * @param key
    * 			: the key of the StationImage whose attributes will be updated
    * @param stationImageName
    * 			: stationImage name
    * @param stationImageMultimediaContent
    * 			: stationImage multimedia content
    * @param stationImageFormat
    * 			: stationImage format
	* @throws MissingRequiredFieldsException 
	 * @throws ObjectExistsInDatastoreException 
    */
	public static void updateStationImageAttributes(
			Key key,
			String stationImageName, 
    		BlobKey stationImageMultimediaContent,
    		String stationImageFormat) 
                       throws MissingRequiredFieldsException, 
                       ObjectExistsInDatastoreException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Station station = pm.getObjectById(Station.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			StationImage stationImage = pm.getObjectById(StationImage.class, key);
			
			// Check if station image already exists in datastore
			if (stationImageExists(key.getParent(), stationImageName) &&
					!stationImage.getStationImageName().equals(stationImageName)) {
				throw new ObjectExistsInDatastoreException(stationImage, "A station image " +
						"with this title has already been added.");
			}
			
			BlobKey oldStationImageMultimediaContent = 
					stationImage.getStationImageMultimediaContent();
			
			tx.begin();
			stationImage.setStationImageName(stationImageName);
			stationImage.setStationImageMultimediaContent(stationImageMultimediaContent);
			stationImage.setStationImageFormat(stationImageFormat);
			station.updateStationImageVersion();
			if (!oldStationImageMultimediaContent.equals(stationImageMultimediaContent) &&
					oldStationImageMultimediaContent != null) {
				blobstoreService.delete(oldStationImageMultimediaContent);
			}
			tx.commit();
			

			
			log.info("StationImage \"" + stationImage.getStationImageName() + 
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
	 * Sort the given stationImage list by name using the
	 * BubbleSort algorithm.
	 * @param stationImages:
	 * 				the list of stationImages to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of stationImages sorted by release date
	 */
	public static List<StationImage> sortStationImages(
			List<StationImage> stationImages, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < stationImages.size(); i++) {
			for (int j = 1; j < (stationImages.size() - i); j++) {
				String stationImage1Name = stationImages.get(j - 1).getStationImageName();
				String stationImage2Name = stationImages.get(j).getStationImageName();
				if (ascendingOrder) {
					if (stationImage1Name.compareTo(stationImage2Name) > 0) {
						StationImage tempStationImage = stationImages.get(j - 1);
						stationImages.set(j - 1, stationImages.get(j));
						stationImages.set(j, tempStationImage);
					}
				}
				else {
					if (stationImage1Name.compareTo(stationImage2Name) < 0) {
						StationImage tempStationImage = stationImages.get(j - 1);
						stationImages.set(j - 1, stationImages.get(j));
						stationImages.set(j, tempStationImage);
					}
				}
			}
		}
		
		return stationImages;
	}
	
}
