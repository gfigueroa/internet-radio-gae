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
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.InvalidFieldSelectionException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the Channel class.
 */
public class ChannelManager {
	
	private static final Logger log = 
        Logger.getLogger(ChannelManager.class.getName());

	/**
	 * Get a channel using its complex key (includes the Station key as well)
	 * @param key
	 *        : The channel's key
	 * @return Channel 
	 */
	public static Channel getChannel(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Channel channel;
		try  {
			channel = pm.getObjectById(Channel.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return channel;
	}
	
	/**
     * Get ALL the Channels in the datastore from a specific station
     * and returns them in a List structure
     * @param stationKey: 
     * 				the key of the station whose channels will be retrieved
     * @return all channels in the datastore belonging to the given station
     * TODO: Fix "touching" of channels
     */
	public static List<Channel> getStationChannels(Key stationKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Station station = pm.getObjectById(Station.class, stationKey);
		
        List<Channel> result = null;
        try {
            result = station.getChannels();
            // Touch each channel
            for (Channel channel : result) {
            	channel.getChannelName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Get the BlobKey of the first slide of the first program in this channel. 
     * The server will search sequentially for all programs until it finds one 
     * with a slide.
     * @param channelKey: 
     * 				the key of the channel to be searched
     * @return an image BlobKey
     */
	public static BlobKey getFirstSlideBlobKey(Key channelKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
        List<Program> programs = channel.getPrograms();
        programs = ProgramManager.sortPrograms(programs, true); // sort
        BlobKey firstSlideBlobKey = null;
        try {
            for (Program program : programs) {
            	List<Slide> slides = program.getSlides();
            	// Check if it has slides
            	if (slides.isEmpty()) {
            		continue;
            	}
            	
            	// Get first slide
            	double lowestStartingTime = Double.MAX_VALUE;
            	Slide firstSlide = null;
            	for (Slide slide : slides) {
            		if (slide.getSlideStartingTime() < lowestStartingTime) {
            			lowestStartingTime = slide.getSlideStartingTime();
            			firstSlide = slide;
            		}
            	}
            	
            	// If it has slides
            	if (firstSlide != null) {
	        		Key stationImageKey = 
	        				firstSlide.getStationImage();
	        		StationImage stationImage = 
	        				pm.getObjectById(StationImage.class, 
	        						stationImageKey);
	        		firstSlideBlobKey = 
	        				stationImage.getStationImageMultimediaContent();
	        		break;
            	}
            }
            
            // If there are no programs with any slides, use station logo
            if (firstSlideBlobKey == null) {
            	Station station = 
            			pm.getObjectById(Station.class, channelKey.getParent());
            	if (station.getStationLogo() != null) {
            		firstSlideBlobKey = station.getStationLogo();
            	}
            	// If there is no station logo, just send a blank BlobKey
            	else {
            		firstSlideBlobKey = new BlobKey("");
            	}
            }
        } 
        finally {
        	pm.close();
        }

        return firstSlideBlobKey;
    }
	
	/**
     * Get the BlobKey of the first slide of the first program in this channel. 
     * The server will search sequentially for all programs until it finds one 
     * with a slide.
     * @param channelKey: 
     * 				the key of the channel to be searched
     * @return an image BlobKey
     */
	public static BlobKey getFirstSlideBlobKey(Channel channel) {
		
        List<Program> programs = 
        		ProgramManager.getAllProgramsFromChannel(
        				channel.getKey(), true);
        BlobKey firstSlideBlobKey = null;
        
        for (Program program : programs) {
        	List<Slide> slides = 
        			SlideManager.getAllSlidesFromProgram(
        					program.getKey(), true);
        	// Check if it has slides
        	if (slides.isEmpty()) {
        		continue;
        	}
        	
        	// Get first slide
        	Slide firstSlide = slides.get(0);

    		Key stationImageKey = 
    				firstSlide.getStationImage();
    		StationImage stationImage = 
    				StationImageManager.getStationImage(stationImageKey);
    		firstSlideBlobKey = 
    				stationImage.getStationImageMultimediaContent();
    		break;
        }
        
        // If there are no programs with any slides, use station logo
        if (firstSlideBlobKey == null) {
        	Station station = 
        			StationManager.getStation(channel.getKey().getParent());
        	if (station.getStationLogo() != null) {
        		firstSlideBlobKey = station.getStationLogo();
        	}
        	// If there is no station logo, just send a blank BlobKey
        	else {
        		firstSlideBlobKey = new BlobKey("");
        	}
        }

        return firstSlideBlobKey;
    }
	
	/**
     * Checks if the given channel belongs to the given station.
     * @param channelKey:
     * 				the key of the channel to check
     * @param stationKey: 
     * 				the key of the station to check
     * @return True if given Channel belongs to given Station, False otherwise
     * TODO: Fix "touching" of channels
     */
	public static boolean channelBelongsToStation(Key channelKey, Key stationKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Station station = pm.getObjectById(Station.class, stationKey);
		
        List<Channel> result = null;
        try {
            result = station.getChannels();
            for (Channel channel : result) {
            	if (channel.getKey().equals(channelKey)) {
            		return true;
            	}
            }
        } 
        finally {
        	pm.close();
        }

        return false;
    }
	
	/**
    * Add channel to a Station.
    * Add a new channel in the datastore for this Station.
    * @param email
    * 			: the email of the Station where the channel will be added
    * @param channel
    * 			: the channel to be added
    */
	public static void putChannel(Email stationEmail, Channel channel) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Station.class.getSimpleName(), 
					stationEmail.getEmail());
			Station station = pm.getObjectById(Station.class, key);
			tx.begin();
			station.addChannel(channel);
			tx.commit();
			log.info("Channel \"" + channel.getChannelName() + "\" added to Station \"" + 
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
    * Add channel to a Station.
    * Add a new channel in the datastore for this Station.
    * @param stationKey
    * 			: the key of the Station where the channel will be added
    * @param channel
    * 			: the channel to be added
    */
	public static void putChannel(Key stationKey, Channel channel) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = pm.getObjectById(Station.class, stationKey);
			tx.begin();
			station.addChannel(channel);
			tx.commit();
			log.info("Channel \"" + channel.getChannelName() + "\" added to Station \"" + 
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
    * Delete channel.
    * Delete a channel in the datastore.
    * @param key
    * 			: the key of the channel to delete (includes Station key)
    */
	public static void deleteChannel(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Station station = pm.getObjectById(Station.class, key.getParent());
			Channel channel = pm.getObjectById(Channel.class, key);
			String channelName = channel.getChannelName();
			tx.begin();
			station.removeChannel(channel);
			tx.commit();
			log.info("Channel \"" + channelName + "\" deleted from Station \"" + 
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
    * Update Channel attributes.
    * Updates the given Channel's attributes in the datastore.
    * @param key
    * 			: the key of the Channel whose attributes will be updated
    * @param region
    * 			: the key of the region
    * @param channelName
    * 			: the new name to give to the Channel
    * @param channelAddress
    * 			: the new address to give to the Channel
    * @param channelPhone
    * 			: the new phone to give to the Channel
    * @param channelEmail
    * 			: the contact email of this channel
	* @throws MissingRequiredFieldsException
	* @throws InvalidFieldFormatException
	* @throws InvalidFieldSelectionException 
    */
	public static void updateChannelAttributes(Key key, String channelName,
			Integer channelNumber)
			throws MissingRequiredFieldsException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Channel channel = pm.getObjectById(Channel.class, key);
			tx.begin();
			channel.setChannelName(channelName);
			channel.setChannelNumber(channelNumber);
			tx.commit();
			log.info("Channel \"" + channelName + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Channel Program Version.
    * Updates the given Channel's Program Version by 1 in the datastore.
    * @param channelKey
    * 			: the key of the Channel whose attributes will be updated
    */
	public static void updateChannelProgramVersion(Key channelKey) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Channel channel = pm.getObjectById(Channel.class, channelKey);
			tx.begin();
			channel.updateProgramVersion();
			tx.commit();
			log.info("Channel \"" + channel.getChannelName() + 
					"\"'s Program version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
