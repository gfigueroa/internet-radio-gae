/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.Date;
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
 * made on the Program class.
 * 
 */

public class ProgramManager {
	
	private static final Logger log = 
        Logger.getLogger(ProgramManager.class.getName());
	
	/**
     * Get a Program instance from the datastore given the Program key.
     * @param key
     * 			: the Program's key
     * @return Program instance, null if Program is not found
     */
	public static Program getProgram(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Program program;
		try  {
			program = pm.getObjectById(Program.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
		return program;
	}
	
	/**
     * Get ALL the programs in the datastore from a specific station
     * and returns them in a List structure
     * @param stationKey: 
     * 				the key of the station whose program will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all programs in the datastore belonging to the given station
     * TODO: Fix "touching" of programs
     */
	public static List<Program> getAllProgramsFromStation(Key stationKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
        ArrayList<Program> result = new ArrayList<>();
        try {
        	// Retrieve all channels first
        	List<Channel> channels = 
        			ChannelManager.getStationChannels(stationKey);
        	
        	for (Channel channel : channels) {
        		Key channelKey = channel.getKey();
        		List<Program> programs = 
        				ProgramManager.getAllProgramsFromChannel(
        						channelKey, ascendingOrder);

        		result.addAll(programs);
        		
	            // Touch each program
	            for (Program program : result) {
	            	program.getKey();
	            }
        	}
        } 
        finally {
        	pm.close();
        }

        result = (ArrayList<Program>) sortPrograms(result, ascendingOrder);
        return result;
    }
	
	/**
     * Get the first program (by sequence numbeR) in the datastore 
     * from a specific channel
     * @param channelKey: 
     * 				the key of the channel whose program will be retrieved
     * @return the first program of this channel
     * TODO: Fix "touching" of programs
     */
	public static Program getFirstProgramFromChannel(Key channelKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
        Program program = null;
        try {
            List<Program> programs = channel.getPrograms();
            int lowestSequenceNo = Integer.MAX_VALUE;
            for (Program p : programs) {
            	int currentSequenceNo = p.getProgramSequenceNumber();
            	if (currentSequenceNo < lowestSequenceNo) {
            		lowestSequenceNo = currentSequenceNo;
            		program = p;
            	}
            }
        } 
        finally {
        	pm.close();
        }

        return program;
    }
	
	/**
     * Get ALL the programs in the datastore from a specific channel
     * and returns them in a List structure
     * @param channelKey: 
     * 				the key of the channel whose program will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all programs in the datastore belonging to the given channel
     * TODO: Fix "touching" of programs
     */
	public static List<Program> getAllProgramsFromChannel(Key channelKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
        List<Program> result = null;
        try {
            result = channel.getPrograms();
            // Touch each program
            for (Program program : result) {
            	program.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        result = sortPrograms(result, ascendingOrder);
        return result;
    }
	
	/**
     * Get inactive programs in the datastore from a specific channel
     * and returns them in a List structure
     * @param channelKey: 
     * 				the key of the channel whose programs will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all programs that are "INACTIVE" belonging to the given channel
     */
	public static List<Program> getInactiveProgramsFromChannel(Key channelKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
        List<Program> result = null;
        ArrayList<Program> finalResult = new ArrayList<Program>();
        try {
            result = channel.getPrograms();
            for (Program program : result) {
            	if (program.getCurrentStatus() == Program.Status.INACTIVE) {
            		finalResult.add(program);
            	}
            }
        }
        finally {
        	pm.close();
        }

        finalResult = (ArrayList<Program>) sortPrograms(finalResult, ascendingOrder);
        return finalResult;
    }
	
	/**
     * Get active programs in the datastore from a specific channel
     * and returns them in a List structure
     * @param channelKey: 
     * 				the key of the channel whose programs will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all programs that are "ACTIVE" belonging to the given channel
     */
	public static List<Program> getActiveProgramsFromChannel(Key channelKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
        List<Program> result = null;
        ArrayList<Program> finalResult = new ArrayList<Program>();
        try {
            result = channel.getPrograms();
            for (Program program : result) {
            	if (program.getCurrentStatus() == Program.Status.ACTIVE) {
            		finalResult.add(program);
            	}
            }
        }
        finally {
        	pm.close();
        }

        finalResult = (ArrayList<Program>) sortPrograms(finalResult, ascendingOrder);
        return finalResult;
    }
	
	/**
     * Get expired program in the datastore from a specific channel
     * and returns them in a List structure
     * @param channelKey: 
     * 				the key of the channel whose program will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all programs that are "EXPIRED" belonging to the given channel
     */
	public static List<Program> getExpiredProgramsFromChannel(Key channelKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
        List<Program> result = null;
        ArrayList<Program> finalResult = new ArrayList<Program>();
        try {
            result = channel.getPrograms();
            for (Program program : result) {
            	if (program.getCurrentStatus() == Program.Status.EXPIRED) {
            		finalResult.add(program);
            	}
            }
        }
        finally {
        	pm.close();
        }

        finalResult = (ArrayList<Program>) sortPrograms(finalResult, ascendingOrder);
        return finalResult;
    }
	
	/**
     * Put Program into datastore.
     * Stores the given Program instance in the datastore for this
     * channel.
     * @param channelKey
     * 			: the key of the Channel where the program will be added
     * @param program
     * 			: the Program instance to channel
     */
	public static void putProgram(Key channelKey, Program program) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Channel channel = 
					pm.getObjectById(Channel.class, channelKey);
			tx.begin();
			channel.addProgram(program);
			channel.updateProgramVersion();
			tx.commit();
			log.info("Program \"" + program.getProgramName() + 
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
    * Delete Program from datastore.
    * Deletes the Program corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the Program instance to delete
    */
	public static void deleteProgram(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Channel channel = pm.getObjectById(Channel.class, key.getParent());
			Program program = pm.getObjectById(Program.class, key);
			String programContent = program.getProgramName();
			tx.begin();
			channel.removeProgram(program);
			channel.updateProgramVersion();
			tx.commit();
			log.info("Program \"" + programContent + 
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
    * Update Program attributes.
    * Update's the given Program's attributes in the datastore.
    * @param key
    * 			: the key of the Program that will be updated
    * @param channelKey
    * 			: the key of the channel this program will belong to
    * @param programName
    * 			: the Name of the program
    * @param programDescription
    * 			: the description of the program
    * @param programBanner
    * 			: the banner of the program
    * @param programSequenceNumber
    * 			: the sequence number of the program
    * @param programTotalDurationTime
    * 			: the total duration time of the program
    * @param programOverlapDuration
    * 			: the overlap duration of the program
    * @param programStartingDate
    * 			: the starting date of this program
    * @param programEndingDate
    * 			: the ending date of this program
    * @param secondaryTracks
    * 			: the secondary tracks of this program
    * @param slides
    * 			: the slides of this program
	* @throws MissingRequiredFieldsException 
	 * @throws InexistentObjectException 
    */
	public static void updateProgramAttributes(
			Key key,
			Key channelKey,
			String programName,
			String programDescription,
			String programBanner,
			Integer programSequenceNumber,
			Double programTotalDurationTime,
			Double programOverlapDuration,
			Date programStartingDate, 
			Date programEndingDate,
			MainTrack mainTrack,
			ArrayList<SecondaryTrack> secondaryTracks,
			ArrayList<Slide> slides
			) 
                       throws MissingRequiredFieldsException, 
                       InexistentObjectException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, channelKey);
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = pm.getObjectById(Program.class, key);
			
			// Check if channel changed
			if (channelKey.equals(program.getKey().getParent())) {
				tx.begin();
				program.setProgramName(programName);
				program.setProgramDescription(programDescription);
				program.setProgramBanner(programBanner);
				program.setProgramSequenceNumber(programSequenceNumber);
				program.setProgramTotalDurationTime(programTotalDurationTime);
				program.setProgramOverlapDuration(programOverlapDuration);
				program.setProgramStartingDate(programStartingDate);
				program.setProgramEndingDate(programEndingDate);
				program.removeAllSecondaryTracks();
				program.addSecondaryTracks(secondaryTracks);
				program.removeAllSlides();
				program.addSlides(slides);
				channel.updateProgramVersion();
				tx.commit();
			}
			// If channel is different, create a new program and delete the old one
			else {
				MainTrack newMainTrack = 
						new MainTrack(mainTrack);
				tx.begin();
				channel.removeProgram(program); // Delete old program
				program = new Program(programName, 
						programDescription,
			    		programBanner, 
			    		programSequenceNumber,
			    		programTotalDurationTime,
			    		programOverlapDuration,
			    		programStartingDate, 
			    		programEndingDate,
			    		newMainTrack,
			    		secondaryTracks,
			    		slides);
				channel.updateProgramVersion();
				tx.commit();
			}
			log.info("Program \"" + program.getProgramName() + 
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
    * Update Program attributes.
    * Update's the given Program's attributes in the datastore.
    * @param key
    * 			: the key of the Program whose attributes will be updated
    * @param programName
    * 			: the Name of the program
    * @param programDescription
    * 			: the description of the program
    * @param programBanner
    * 			: the banner of the program
    * @param programSequenceNumber
    * 			: the sequence number of the program
    * @param programTotalDurationTime
    * 			: the total duration time of the program
    * @param programOverlapDuration
    * 			: the overlap duration of the program
    * @param programStartingDate
    * 			: the starting date of this program
    * @param programEndingDate
    * 			: the ending date of this program
	* @throws MissingRequiredFieldsException 
    */
	public static void updateProgramAttributes(
			Key key, 
			String programName, 
			String programDescription,
			String programBanner, 
			Integer programSequenceNumber,
			Double programTotalDurationTime,
			Double programOverlapDuration,
			Date programStartingDate, 
			Date programEndingDate
			) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Channel channel = pm.getObjectById(Channel.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			Program program = pm.getObjectById(Program.class, key);
			tx.begin();
			program.setProgramName(programName);
			program.setProgramDescription(programDescription);
			program.setProgramBanner(programBanner);
			program.setProgramSequenceNumber(programSequenceNumber);
			program.setProgramTotalDurationTime(programTotalDurationTime);
			program.setProgramOverlapDuration(programOverlapDuration);
			program.setProgramStartingDate(programStartingDate);
			program.setProgramEndingDate(programEndingDate);
			channel.updateProgramVersion();
			tx.commit();
			log.info("Program \"" + program.getProgramName() + 
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
	 * Sort the given program list by release date using the
	 * BubbleSort algorithm.
	 * @param programs:
	 * 				the list of programs to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of programs sorted by sequence number
	 */
	public static List<Program> sortPrograms(List<Program> programs, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < programs.size(); i++) {
			for (int j = 1; j < (programs.size() - i); j++) {
				int program1Sequence = programs.get(j - 1).getProgramSequenceNumber();
				int program2Sequence = programs.get(j).getProgramSequenceNumber();
				if (ascendingOrder) {
					if (program1Sequence > program2Sequence) {
						Program tempProgram = programs.get(j - 1);
						programs.set(j - 1, programs.get(j));
						programs.set(j, tempProgram);
					}
				}
				else {
					if (program1Sequence < program2Sequence) {
						Program tempProgram = programs.get(j - 1);
						programs.set(j - 1, programs.get(j));
						programs.set(j, tempProgram);
					}
				}
			}
		}
		
		return programs;
	}
	
}
