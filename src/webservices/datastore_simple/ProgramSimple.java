/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * This class represents a simple version of the Program table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class ProgramSimple implements Serializable {
    
	public static class MainTrackSimple {
		public String mainTrackType;
		public String stationAudio;
		public BlobKey stationAudioBlobKey;
		public String stationAudioName;
		public String stationAudioFormat;
		public Double stationAudioDuration;
		public Long mainTrackMusicFileKey;
		public BlobKey mainTrackMusicFileBlobKey;
		public String mainTrackMusicFileName;
		public String mainTrackMusicFileFormat;
		public Double mainTrackMusicFileDuration;
		public String mainTrackPlaylistKey;
		public Double mainTrackDuration;
		public Integer mainTrackFadeInSteps;
		public Double mainTrackFadeInDuration;
		public Double mainTrackFadeInPercentage;
		public Integer mainTrackFadeOutSteps;
		public Double mainTrackFadeOutDuration;
		public Double mainTrackFadeOutPercentage;
		
		public MainTrackSimple(String mainTrackType,
				String stationAudio,
				BlobKey stationAudioBlobKey,
				String stationAudioName,
				String stationAudioFormat,
				Double stationAudioDuration,
				Long mainTrackMusicFileKey,
				BlobKey mainTrackMusicFileBlobKey,
				String mainTrackMusicFileName,
				String mainTrackMusicFileFormat,
				Double mainTrackMusicFileDuration,
				String mainTrackPlaylistKey,
				Double mainTrackDuration,
				Integer mainTrackFadeInSteps,
				Double mainTrackFadeInDuration,
				Double mainTrackFadeInPercentage,
				Integer mainTrackFadeOutSteps,
				Double mainTrackFadeOutDuration,
				Double mainTrackFadeOutPercentage) {
			
			this.mainTrackType = mainTrackType;
			this.stationAudio = stationAudio;
			this.stationAudioBlobKey = stationAudioBlobKey;
			this.stationAudioName = stationAudioName;
			this.stationAudioFormat = stationAudioFormat;
			this.stationAudioDuration = stationAudioDuration;
			this.mainTrackMusicFileKey = mainTrackMusicFileKey;
			this.mainTrackMusicFileBlobKey = mainTrackMusicFileBlobKey;
			this.mainTrackMusicFileName = mainTrackMusicFileName;
			this.mainTrackMusicFileFormat = mainTrackMusicFileFormat;
			this.mainTrackMusicFileDuration = mainTrackMusicFileDuration;
			this.mainTrackPlaylistKey = mainTrackPlaylistKey;
			this.mainTrackDuration = mainTrackDuration;
			this.mainTrackFadeInSteps = mainTrackFadeInSteps;
			this.mainTrackFadeInDuration = mainTrackFadeInDuration;
			this.mainTrackFadeInPercentage = mainTrackFadeInPercentage;
			this.mainTrackFadeOutSteps = mainTrackFadeOutSteps;
			this.mainTrackFadeOutDuration = mainTrackFadeOutDuration;
			this.mainTrackFadeOutPercentage = mainTrackFadeOutPercentage;
		}
	}
	
	public static class SecondaryTrackSimple {
		public String secondaryTrackKey;
		public String secondaryTrackType;
		public String stationAudio;
		public BlobKey stationAudioBlobKey;
		public String stationAudioName;
		public String stationAudioFormat;
		public Double stationAudioDuration;
		public Long secondaryTrackMusicFileKey;
		public BlobKey secondaryTracMusicFileBlobKey;
		public String secondaryTrackMusicFileName;
		public String secondaryTrackMusicFileFormat;
		public Double secondaryTrackMusicFileDuration;
		public Double secondaryTrackStartingTime;
		public Double secondaryTrackDuration;
		public Integer secondaryTrackFadeInSteps;
		public Double secondaryTrackFadeInDuration;
		public Double secondaryTrackFadeInPercentage;
		public Integer secondaryTrackFadeOutSteps;
		public Double secondaryTrackFadeOutDuration;
		public Double secondaryTrackFadeOutPercentage;
		public Double secondaryTrackOffset;
		
		public SecondaryTrackSimple(String secondaryTrackKey,
				String secondaryTrackType,
				String stationAudio,
				BlobKey stationAudioBlobKey,
				String stationAudioName,
				String stationAudioFormat,
				Double stationAudioDuration,
				Long secondaryTrackMusicFileKey,
				BlobKey secondaryTrackMusicFileBlobKey,
				String secondaryTrackMusicFileName,
				String secondaryTrackMusicFileFormat,
				Double secondaryTrackMusicFileDuration,
				Double secondaryTrackStartingTime,
				Double secondaryTrackDuration,
				Integer secondaryTrackFadeInSteps,
				Double secondaryTrackFadeInDuration,
				Double secondaryTrackFadeInPercentage,
				Integer secondaryTrackFadeOutSteps,
				Double secondaryTrackFadeOutDuration,
				Double secondaryTrackFadeOutPercentage,
				Double secondaryTrackOffset) {
			
			this.secondaryTrackKey = secondaryTrackKey;
			this.secondaryTrackType = secondaryTrackType;
			this.stationAudio = stationAudio;
			this.stationAudioBlobKey = stationAudioBlobKey;
			this.stationAudioName = stationAudioName;
			this.stationAudioFormat = stationAudioFormat;
			this.stationAudioDuration = stationAudioDuration;
			this.secondaryTrackMusicFileKey = secondaryTrackMusicFileKey;
			this.secondaryTracMusicFileBlobKey = secondaryTrackMusicFileBlobKey;
			this.secondaryTrackMusicFileName = secondaryTrackMusicFileName;
			this.secondaryTrackMusicFileFormat = secondaryTrackMusicFileFormat;
			this.secondaryTrackDuration = secondaryTrackMusicFileDuration;
			this.secondaryTrackStartingTime = secondaryTrackStartingTime;
			this.secondaryTrackDuration = secondaryTrackDuration;
			this.secondaryTrackFadeInSteps = secondaryTrackFadeInSteps;
			this.secondaryTrackFadeInDuration = secondaryTrackFadeInDuration;
			this.secondaryTrackFadeInPercentage = secondaryTrackFadeInPercentage;
			this.secondaryTrackFadeOutSteps = secondaryTrackFadeOutSteps;
			this.secondaryTrackFadeOutDuration = secondaryTrackFadeOutDuration;
			this.secondaryTrackFadeOutPercentage = secondaryTrackFadeOutPercentage;
			this.secondaryTrackOffset = secondaryTrackOffset;
		}
	}
	
	public static class SlideSimple {
		public String slideKey;
		public String stationImage;
		public BlobKey stationImageBlobKey;
		public Double slideStartingTime;
		public Double slideDuration; //TODO: Remove
		
		public SlideSimple(String slideKey,
				String stationImage,
				BlobKey stationImageBlobKey,
				Double slideStartingTime,
				Double slideDuration/* TODO: Remove */) {
			
			this.slideKey = slideKey;
			this.stationImage = stationImage;
			this.stationImageBlobKey = stationImageBlobKey;
			this.slideStartingTime = slideStartingTime;
			this.slideDuration = slideDuration;//TODO: Remove
		}
	}
	
	public String programKey;
	public String channelKey;
	public String programName;
	public String programDescription;
	public String programBanner;
	public Integer programSequenceNumber;
	public Double programTotalDurationTime;
	public Double programOverlapDuration;
	public String programStartingDate;
	public String programEndingDate;
	public BlobKey stationLogo;
	public MainTrackSimple mainTrack;
	public ArrayList<SecondaryTrackSimple> secondaryTracks;
	public ArrayList<SlideSimple> slides;
    
    /**
     * ProgramSimple constructor.
     * @param programKey
     * 			: Program key string
     * @param channelKey
     * 			: Channel key string
     * @param programName
     * 			: The name of the program
     * @param programDescription
     * 			: The description of the program
     * @param programBanner
     * 			: The banner of the program
     * @param programSequenceNumber
     * 			: The sequence number of the program
     * @param programTotalDurationTime
     * 			: The total duration time of the program
     * @param programOverlapDuration
     * 			: The overlap duration time of the program
     * @param programStartingDate
     * 			: The date this program will finish
     * @param programEndingDate
     * 			: The date this program will finish
     * @param stationLogo
     * 			: The logo image of the station to which 
     * 			this program belongs
     * @param mainTrack
     * 			: The main track
     * @param secondaryTrack
     * 			: List of secondary tracks
     * @param slides
     * 			: List of slides
     */
    public ProgramSimple(String programKey, 
    		String channelKey,
    		String programName, 
    		String programDescription,
    		String programBanner, 
    		Integer programSequenceNumber,
    		Double programTotalDurationTime,
    		Double programOverlapDuration,
    		String programStartingDate, 
    		String programEndingDate,
    		BlobKey stationLogo,
    		MainTrackSimple mainTrack, 
    		ArrayList<SecondaryTrackSimple> secondaryTracks,
    		ArrayList<SlideSimple> slides) {

    	this.programKey = programKey;
    	this.channelKey = channelKey;
    	this.programName = programName;
    	this.programDescription = programDescription;
    	this.programBanner = programBanner;
    	this.programSequenceNumber = programSequenceNumber;
    	this.programTotalDurationTime = programTotalDurationTime;
    	this.programOverlapDuration = programOverlapDuration;
    	this.programStartingDate = programStartingDate;
    	this.programEndingDate = programEndingDate;
    	this.stationLogo = stationLogo;
    	this.mainTrack = mainTrack;
    	this.secondaryTracks = secondaryTracks;
    	this.slides = slides;
    }
    
    /**
     * Compare this program with another Program
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Program, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof ProgramSimple ) ) return false;
        ProgramSimple m = (ProgramSimple) o;
        return this.programKey.equals(m.programKey);
    }
    
}
