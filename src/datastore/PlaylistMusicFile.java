/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the PlaylistMusicFile table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class PlaylistMusicFile implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Long musicFileKey;
    
    @Persistent
    private Integer playlistMusicFileSequenceNumber;

    /**
     * PlaylistMusicFile constructor.
     * @param musicFile
     * 			: the music file key
     * @param playlistMusicFileSequenceNumber
     * 			: the playback sequence number for this playlist music file
     * @throws MissingRequiredFieldsException
     */
    public PlaylistMusicFile(Long musicFileKey, 
    		Integer playlistMusicFileSequenceNumber) 
		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (playlistMusicFileSequenceNumber == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

    	this.musicFileKey = musicFileKey;
    	this.playlistMusicFileSequenceNumber = playlistMusicFileSequenceNumber;
    }

    /**
     * Get PlaylistMusicFile key.
     * @return playlistMusicFile key
     */
    public Key getKey() {
        return key;
    }

	/**
	 * @return the musicFileKey
	 */
	public Long getMusicFileKey() {
		return musicFileKey;
	}

	/**
	 * @return the playlistMusicFileSequenceNumber
	 */
	public Integer getPlaylistMusicFileSequenceNumber() {
		return playlistMusicFileSequenceNumber;
	}

	/**
     * Compare this playlistMusicFile with another playlistMusicFile
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this PlaylistMusicFile, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof PlaylistMusicFile ) ) return false;
        PlaylistMusicFile playlistMusicFile = (PlaylistMusicFile) o;
        return this.getKey().equals(playlistMusicFile.getKey());
    }

	/**
	 * @param playlistMusicFileSequenceNumber the playlistMusicFileSequenceNumber to set
	 */
	public void setPlaylistMusicFileSequenceNumber(
			Integer playlistMusicFileSequenceNumber) {
		this.playlistMusicFileSequenceNumber = playlistMusicFileSequenceNumber;
	}
}