/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the Playlist table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class PlaylistSimple implements Serializable {
	
	public static class PlaylistMusicFileSimple {
		public Long musicFileKey;
		public Integer playlistMusicFileSequenceNumber;
		
		public PlaylistMusicFileSimple(Long musicFileKey, 
				Integer playlistMusicFileSequenceNumber) {
			this.musicFileKey= musicFileKey;
			this.playlistMusicFileSequenceNumber = 
					playlistMusicFileSequenceNumber;
		}
	}
	
	public String playlistKey;
	public String playlistName;
	public ArrayList<PlaylistMusicFileSimple> playlistMusicFiles;
	public String playlistCreationTime;
	public String playlistModificationTime;

    
    /**
     * PlaylistSimple constructor.
     * @param playlistKey
     * 			: playlist key string
     * @param playlistName
     * 			: playlist name
     * @param playlistMusicFiles
     * 			: playlist music files
     * @param playlistCreationTime
     * 			: playlist creation time
     * @param playlistModificationTime
     * 			: playlist modification time
     */
    public PlaylistSimple(String playlistKey, 
    		String playlistName,
    		ArrayList<PlaylistMusicFileSimple> playlistMusicFiles,
    		String playlistCreationTime,
    		String playlistModificationTime) {

    	this.playlistKey = playlistKey;
    	this.playlistName = playlistName;
    	this.playlistMusicFiles = playlistMusicFiles;
    	this.playlistCreationTime = playlistCreationTime;
    	this.playlistModificationTime = playlistModificationTime;
    }
    
    /**
     * Compare this playlist with another Playlist
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Playlist, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof PlaylistSimple ) ) return false;
        PlaylistSimple m = (PlaylistSimple) o;
        return this.playlistKey.equals(m.playlistKey);
    }
    
}
