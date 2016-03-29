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
 * made on the Slide class.
 * 
 */

public class SlideManager {

	private static final Logger log = 
        Logger.getLogger(SlideManager.class.getName());
	
	/**
     * Get a Slide instance from the datastore given the Slide key.
     * @param key
     * 			: the Slide's key
     * @return Slide instance, null if Slide is not found
     */
	public static Slide getSlide(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Slide slide;
		try  {
			slide = pm.getObjectById(Slide.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return slide;
	}
	
	/**
     * Get ALL the slides in the datastore from a specific program
     * and returns them in a List structure
     * @param programKey: 
     * 				the key of the program whose slides will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all slides in the datastore belonging to the given program
     * TODO: Fix "touching" of slides
     */
	public static List<Slide> getAllSlidesFromProgram(Key programKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Program program = pm.getObjectById(Program.class, programKey);
		
        List<Slide> result = null;
        try {
            result = program.getSlides();
            // Touch each slide
            for (Slide slide : result) {
            	slide.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        result = sortSlides(result, ascendingOrder);
        return result;
    }
	
	/**
     * Put Slide into datastore.
     * Stores the given Slide instance in the datastore for this
     * program.
     * @param programKey
     * 			: the key of the Program where the slide will be added
     * @param slide
     * 			: the Slide instance to program
     */
	public static void putSlide(Key programKey, Slide slide) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = 
					pm.getObjectById(Program.class, programKey);
			Channel channel =
					pm.getObjectById(Channel.class, programKey.getParent());
			tx.begin();
			program.addSlide(slide);
			channel.updateProgramVersion();
			tx.commit();
			log.info("Slide stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete Slide from datastore.
    * Deletes the Slide corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the Slide instance to delete
    */
	public static void deleteSlide(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = pm.getObjectById(Program.class, key.getParent());
			Channel channel =
					pm.getObjectById(Channel.class, key.getParent().getParent());
			
			Slide slide = pm.getObjectById(Slide.class, key);
			
			tx.begin();
			program.removeSlide(slide);
			channel.updateProgramVersion();
			tx.commit();
			
			log.info("Slidedeleted successfully from datastore.");
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
    * Update Slide attributes.
    * Update's the given Slide's attributes in the datastore.
    * @param key
    * 			: the key of the Slide whose attributes will be updated
     * @param slideName
     * 			: slide name
     * @param slideMultimediaContent
     * 			: slide multimedia content
     * @param slideStartingTime
     * 			: the starting time of the slide
	* @throws MissingRequiredFieldsException 
    */
	public static void updateSlideAttributes(
			Key key,
    		Key stationImage,
    		Double slideStartingTime) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Channel channel = pm.getObjectById(Channel.class, key.getParent().getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			Slide slide = pm.getObjectById(Slide.class, key);
			
			tx.begin();
			slide.setStationImage(stationImage);
			slide.setSlideStartingTime(slideStartingTime);
			channel.updateProgramVersion();
			tx.commit();
			
			log.info("Slide attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
	 * Sort the given slide list by starting time date using the
	 * BubbleSort algorithm.
	 * @param slides:
	 * 				the list of slides to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of slides sorted by starting time
	 */
	public static List<Slide> sortSlides(
			List<Slide> slides, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < slides.size(); i++) {
			for (int j = 1; j < (slides.size() - i); j++) {
				Double slide1Date = slides.get(j - 1).getSlideStartingTime();
				Double slide2Date = slides.get(j).getSlideStartingTime();
				if (ascendingOrder) {
					if (slide1Date.compareTo(slide2Date) > 0) {
						Slide tempSlide = slides.get(j - 1);
						slides.set(j - 1, slides.get(j));
						slides.set(j, tempSlide);
					}
				}
				else {
					if (slide1Date.compareTo(slide2Date) < 0) {
						Slide tempSlide = slides.get(j - 1);
						slides.set(j - 1, slides.get(j));
						slides.set(j, tempSlide);
					}
				}
			}
		}
		
		return slides;
	}
	
}
