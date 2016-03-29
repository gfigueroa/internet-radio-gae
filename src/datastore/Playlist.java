/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Playlist table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Playlist implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
    @Persistent
    private String playlistName;
    
    @Persistent
    private Date playlistCreationDate;
    
    @Persistent
    private Date  playlistModificationDate;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<PlaylistMusicFile> playlistMusicFiles;

    /**
     * Playlist constructor.
     * @param playlistName
     * 			: playlist name
     * @param playlistMusicFiles
     * 			: playlist music files
     * @throws MissingRequiredFieldsException
     */
    public Playlist(String playlistName,
    		ArrayList<PlaylistMusicFile> playlistMusicFiles) 
		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (playlistName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (playlistName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

    	this.playlistName = playlistName;
    	
    	this.playlistMusicFiles = playlistMusicFiles;
    	
    	Date now = new Date();
    	this.playlistCreationDate = now;
    	this.playlistModificationDate = now;
    }

    /**
     * Get Playlist key.
     * @return playlist key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Playlist name.
     * @return playlist name
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Get playlist creation date.
     * @return the time this playlist was created
     */
    public Date getPlaylistCreationDate() {
        return playlistCreationDate;
    }

    /**
     * Get playlist modification date.
     * @return the time this playlist was last modified
     */
    public Date getPlaylistModificationDate() {
        return playlistModificationDate;
    }
    
    /**
     * Get playlist music files
     * @return the list of playlist music files
     */
    public ArrayList<PlaylistMusicFile> getPlaylistMusicFiles() {
    	return playlistMusicFiles;
    }
    
    /**
     * Compare this playlist with another playlist
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Playlist, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Playlist ) ) return false;
        Playlist playlist = (Playlist) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(playlist.getKey()));
    }
     
    /**
     * Set Playlist name.
     * @param playlistName
     * 			: playlist name
     * @throws MissingRequiredFieldsException
     */
    public void setPlaylistName(String playlistName)
    		throws MissingRequiredFieldsException {
    	if (playlistName == null || playlistName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Playlist name is missing.");
    	}
    	this.playlistName = playlistName;
    	this.playlistModificationDate = new Date();
    }
    
    /**
     * Add PlaylistMusicFile.
     * @param playlistMusicFile
     * 			: the PlaylistMusicFile to add
     */
    public void addPlaylistMusicFile(PlaylistMusicFile playlistMusicFile) {
    	this.playlistMusicFiles.add(playlistMusicFile);
    	this.playlistModificationDate = new Date();
    }
    
    /**
     * Remove a PlaylistMusicFile.
     * @param playlistMusicFile
     * 			: PlaylistMusicFile to be removed
     * @throws InexistentObjectException
     */
    public void removePlaylistMusicFile(PlaylistMusicFile playlistMusicFile) 
    		throws InexistentObjectException {
    	if (!playlistMusicFiles.remove(playlistMusicFile)) {
    		throw new InexistentObjectException(
    				PlaylistMusicFile.class, "PlaylistMusicFile not found!");
    	}
    	this.playlistModificationDate = new Date();
    }
}