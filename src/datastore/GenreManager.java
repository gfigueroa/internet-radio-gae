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

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Genre class.
 * 
 */

public class GenreManager {
	
	private static final Logger log = Logger.getLogger(GenreManager.class.getName());
	
	/**
     * Get a Genre instance from the datastore given the Genre key.
     * @param key
     * 			: the genre's key
     * @return genre instance, null if genre is not found
     */
	public static Genre getGenre(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Genre genre;
		try  {
			genre = pm.getObjectById(Genre.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return genre;
	}
	
	/**
     * Get all Genre instances from the datastore.
     * @return All Genre instances
     * TODO: Make "touching" of genres more efficient
     */
	@SuppressWarnings("unchecked")
	public static List<Genre> getAllGenres() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Genre.class);

        List<Genre> genres = null;
        try {
        	genres = (List<Genre>) query.execute();
            // touch all elements
            for (Genre r : genres)
                r.getGenreEnglishName();
        } finally {
        	pm.close();
            query.closeAll();
        }

        return genres;
    }
	
	/**
     * Put Genre into datastore.
     * Stores the given genre instance in the datastore calling the PersistenceManager's
     * makePersistent() method.
     * @param genre
     * 			: the genre instance to store
     */
	public static void putGenre(Genre genre) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(genre);
			tx.commit();
			log.info("Genre \"" + genre.getGenreEnglishName() + 
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
    * Delete Genre from datastore.
    * Deletes the genre corresponding to the given key
    * from the datastore calling the PersistenceManager's deletePersistent() method.
    * @param key
    * 			: the key of the genre instance to delete
    */
	public static void deleteGenre(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Genre genre = pm.getObjectById(Genre.class, key);
			String genreName = genre.getGenreEnglishName();
			tx.begin();
			pm.deletePersistent(genre);
			tx.commit();
			log.info("Genre \"" + genreName + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update Genre attributes.
    * Update's the given genre's attributes in the datastore.
    * @param key
    * 			: the key of the genre whose attributes will be updated
    * @param genreEnglishName
    * 			: the English name of the Genre
    * @param genreChineseName
    * 			: the Chinese name of the Genre
	* @throws MissingRequiredFieldsException 
    */
	public static void updateGenreAttributes(Long key,
			String genreEnglishName, String genreChineseName) 
    				throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Genre genre = pm.getObjectById(Genre.class, key);
			tx.begin();
			genre.setGenreEnglishName(genreEnglishName);
			genre.setGenreChineseName(genreChineseName);
			tx.commit();
			log.info("Genre \"" + genreEnglishName + 
					"\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
