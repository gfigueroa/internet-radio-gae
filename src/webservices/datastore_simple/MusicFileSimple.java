/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;


/**
 * This class represents a simple version of the MusicFile table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class MusicFileSimple implements Serializable {
    
	public Long key;
	public Long genreKey;
    public String genreEnglishName;
    public String genreChineseName;
    public BlobKey musicFileFile;
    public String musicFileTitle;
    public String musicFileArtist;
    public String musicFileAlbum;
    public String musicFileAlbumArtist;
    public Integer musicFileYear;
    public String musicFileComposer;
    public String musicFilePublisher;
    public Double musicFileDuration;
    public String musicFileFormat;
    public String musicFileComments;
    public String musicFileCreationTime;
    public String musicFileModificationTime;
    
    /**
     * MusicFileSimple constructor.
     * @param key
     * 			: musicFile key
     * @param genreKey
     * 			: the key of the genre
     * @param genreEnglishName
     * 			: musicFile genre English name
     * @param genreChineseName
     * 			: musicFile genre Chinese name
     * @param musicFileFile
     * 			: musicFile file
     * @param musicFileTitle
     * 			: musicFile title
     * @param musicFileArtist
     * 			: musicFile artist
     * @param musicFileAlbum
     * 			: musicFile album
     * @param musicFileAlbumArtist
     * 			: musicFile album artist
     * @param musicFileYear
     * 			: musicFile year
     * @param musicFileComposer
     * 			: musicFile composer
     * @param musicFilePublisher
     * 			: musicFile publisher
     * @param musicFileDuration
     * 			: musicFile duration
     * @param musicFileFormat
     * 			: musicFile format
     * @param musicFileCreationTime
     * 			: musicFile creation time
     * @param musicFileModificationTime
     * 			: musicFile modification time
     */
    public MusicFileSimple(Long key, 
    		Long genreKey, String genreEnglishName,
    		String genreChineseName, BlobKey musicFileFile,
    		String musicFileTitle, String musicFileArtist,
    		String musicFileAlbum, String musicFileAlbumArtist,
    		Integer musicFileYear, String musicFileComposer,
    		String musicFilePublisher, Double musicFileDuration,
    		String musicFileFormat, String musicFileCreationTime,
    		String musicFileModificationTime) {

    	this.key = key;
    	this.genreKey = genreKey;
    	this.genreEnglishName = genreEnglishName;
    	this.genreChineseName = genreChineseName;
    	this.musicFileFile = musicFileFile;
    	this.musicFileTitle = musicFileTitle;
    	this.musicFileArtist = musicFileArtist;
    	this.musicFileYear = musicFileYear;
    	this.musicFileComposer = musicFileComposer;
    	this.musicFilePublisher = musicFilePublisher;
    	this.musicFileDuration = musicFileDuration;
    	this.musicFileFormat = musicFileFormat;
    	this.musicFileCreationTime = musicFileCreationTime;
    	this.musicFileModificationTime = musicFileModificationTime;
    }
    
    /**
     * Compare this musicFile with another MusicFile
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MusicFile, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof MusicFileSimple ) ) return false;
        MusicFileSimple r = (MusicFileSimple) o;
        return this.key.equals(r.key);
    }
    
}
