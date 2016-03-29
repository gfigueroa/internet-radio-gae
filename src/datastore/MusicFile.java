/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the MusicFile table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class MusicFile {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private Long genre;
    
    @Persistent
    private BlobKey musicFileFile;
    
    @Persistent
    private String musicFileTitle;
    
    @Persistent
    private String musicFileArtist;
    
    @Persistent
    private String musicFileAlbum;
    
    @Persistent
    private String musicFileAlbumArtist;
    
    @Persistent
    private Integer musicFileYear;
    
    @Persistent
    private String musicFileComposer;
    
    @Persistent
    private String musicFilePublisher;
    
    @Persistent
    private Double musicFileDuration;
    
    @Persistent
    private String musicFileFormat;
    
    @Persistent
    private String musicFileComments;
    
    @Persistent
    private Date musicFileCreationTime;
    
    @Persistent
    private Date musicFileModificationTime;

    /**
     * MusicFile constructor.
     * @param genre
     * 			: musicFile genre key
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
     * @param musicFileComments
     * 			: musicFile comments
     * @throws MissingRequiredFieldsException
     */
    public MusicFile(Long genre, BlobKey musicFileFile,
    		String musicFileTitle, String musicFileArtist,
    		String musicFileAlbum, String musicFileAlbumArtist,
    		Integer musicFileYear, String musicFileComposer,
    		String musicFilePublisher, Double musicFileDuration,
    		String musicFileFormat, String musicFileComments) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (genre == null || musicFileFile == null || musicFileTitle == null ||
    			musicFileArtist == null || musicFileDuration == null ||
    			musicFileFormat == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (musicFileTitle.trim().isEmpty() || musicFileArtist.trim().isEmpty() ||
    			musicFileFormat.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.genre = genre;
    	this.musicFileFile = musicFileFile;
        this.musicFileTitle = musicFileTitle;
        this.musicFileArtist = musicFileArtist;
        this.musicFileAlbum = musicFileAlbum;
        this.musicFileAlbumArtist = musicFileAlbumArtist;
        this.musicFileYear = musicFileYear;
        this.musicFileComposer = musicFileComposer;
        this.musicFilePublisher = musicFilePublisher;
        this.musicFileDuration = musicFileDuration;
        this.musicFileFormat = musicFileFormat;
        this.musicFileComments = musicFileComments;
        
        Date now = new Date();
        this.musicFileCreationTime = now;
        this.musicFileModificationTime = now;
    }

    /**
     * Get MusicFile key.
     * @return musicFile key
     */
    public Long getKey() {
        return key;
    }

    /**
	 * @return the genre
	 */
	public Long getGenre() {
		return genre;
	}

	/**
	 * @return the musicFileFile
	 */
	public BlobKey getMusicFileFile() {
		return musicFileFile;
	}
	
	/**
     * Get MusicFile title.
     * @return restaurant musicFile title
     */
    public String getMusicFileTitle() {
        return musicFileTitle;
    }

	/**
	 * @return the musicFileArtist
	 */
	public String getMusicFileArtist() {
		return musicFileArtist;
	}

	/**
	 * @return the musicFileAlbum
	 */
	public String getMusicFileAlbum() {
		return musicFileAlbum;
	}

	/**
	 * @return the musicFileAlbumArtist
	 */
	public String getMusicFileAlbumArtist() {
		return musicFileAlbumArtist;
	}

	/**
	 * @return the musicFileYear
	 */
	public Integer getMusicFileYear() {
		return musicFileYear;
	}

	/**
	 * @return the musicFileComposer
	 */
	public String getMusicFileComposer() {
		return musicFileComposer;
	}

	/**
	 * @return the musicFilePublisher
	 */
	public String getMusicFilePublisher() {
		return musicFilePublisher;
	}

	/**
	 * @return the musicFileDuration
	 */
	public Double getMusicFileDuration() {
		return musicFileDuration;
	}

	/**
	 * @return the musicFileFormat
	 */
	public String getMusicFileFormat() {
		return musicFileFormat;
	}
	
    /**
     * Get MusicFile comments.
     * @return restaurant musicFile comments
     */
    public String getMusicFileComments() {
    	return musicFileComments;
    }

	/**
	 * @return the musicFileCreationTime
	 */
	public Date getMusicFileCreationTime() {
		return musicFileCreationTime;
	}

	/**
	 * @return the musicFileModificationTime
	 */
	public Date getMusicFileModificationTime() {
		return musicFileModificationTime;
	}
	
	/**
	 * @param genre the genre to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setGenre(Long genre) 
			throws MissingRequiredFieldsException {
		
		if (genre == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MusicFile file is missing.");
    	}
		
		this.genre = genre;
	}

	/**
	 * @param musicFileFile the musicFileFile to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMusicFileFile(BlobKey musicFileFile) 
			throws MissingRequiredFieldsException {
		
    	if (musicFileFile == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MusicFile file is missing.");
    	}
		
		this.musicFileFile = musicFileFile;
		this.musicFileModificationTime = new Date();
	}
	
    /**
     * Set MusicFile title.
     * @param musicFileTitle
     * 			: musicFile title
     * @throws MissingRequiredFieldsException
     */
    public void setMusicFileTitle(String musicFileTitle)
    		throws MissingRequiredFieldsException {
    	if (musicFileTitle == null || musicFileTitle.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MusicFile title is missing.");
    	}
    	this.musicFileTitle = musicFileTitle;
        this.musicFileModificationTime = new Date();
    }

	/**
	 * @param musicFileArtist the musicFileArtist to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMusicFileArtist(String musicFileArtist) 
			throws MissingRequiredFieldsException {
		
    	if (musicFileArtist == null || musicFileArtist.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MusicFile artist is missing.");
    	}
		
		this.musicFileArtist = musicFileArtist;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFileAlbum the musicFileAlbum to set
	 */
	public void setMusicFileAlbum(String musicFileAlbum) {
		this.musicFileAlbum = musicFileAlbum;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFileAlbumArtist the musicFileAlbumArtist to set
	 */
	public void setMusicFileAlbumArtist(String musicFileAlbumArtist) {
		this.musicFileAlbumArtist = musicFileAlbumArtist;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFileYear the musicFileYear to set
	 */
	public void setMusicFileYear(Integer musicFileYear) {
		this.musicFileYear = musicFileYear;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFileComposer the musicFileComposer to set
	 */
	public void setMusicFileComposer(String musicFileComposer) {
		this.musicFileComposer = musicFileComposer;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFilePublisher the musicFilePublisher to set
	 */
	public void setMusicFilePublisher(String musicFilePublisher) {
		this.musicFilePublisher = musicFilePublisher;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFileDuration the musicFileDuration to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMusicFileDuration(Double musicFileDuration) 
			throws MissingRequiredFieldsException {
		
    	if (musicFileDuration == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MusicFile duration is missing.");
    	}
		
		this.musicFileDuration = musicFileDuration;
		this.musicFileModificationTime = new Date();
	}

	/**
	 * @param musicFileFormat the musicFileFormat to set
	 * @throws MissingRequiredFieldsException 
	 */
	public void setMusicFileFormat(String musicFileFormat) 
			throws MissingRequiredFieldsException {
		
    	if (musicFileFormat == null || musicFileFormat.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"MusicFile format is missing.");
    	}
		
		this.musicFileFormat = musicFileFormat;
		this.musicFileModificationTime = new Date();
	}
    
    /**
     * Set MusicFile comments.
     * @param musicFileComments
     * 			: musicFile comments
     */
    public void setMusicFileComments(String musicFileComments) {
    	this.musicFileComments = musicFileComments;
    	this.musicFileModificationTime = new Date();
    }
}
