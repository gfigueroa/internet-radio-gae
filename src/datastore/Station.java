/*
 Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PostalAddress;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Station table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Station implements Serializable {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(dependent = "true", defaultFetchGroup = "true")
    private User user;
    
    @Persistent
    private Long stationType;
    
    @Persistent
    private Integer stationPrivilegeLevel;
    
    @Persistent
    private String stationName;
    
    @Persistent
    private String stationNumber;
    
    @Persistent
    private String stationDescription;
    
    @Persistent
    private Long region;
    
    @Persistent
    private PostalAddress stationAddress;
    
    @Persistent
    private Link stationWebsite;
    
    @Persistent
    private BlobKey stationLogo;
    
    @Persistent
    private String stationComments;
    
    @Persistent
    private Integer playlistVersion;
    
    @Persistent
    private Integer stationImageVersion;
    
    @Persistent
    private Integer stationAudioVersion;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Channel> channels;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Playlist> playlists;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<StationImage> stationImages;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<StationAudio> stationAudios;
    
    /**
     * Station constructor.
     * @param user
     * 			: the user for this station
     * @param stationType
     * 			: station type key
     * @param stationPrivilegeLevel
     * 			: station privilege level
     * @param stationName
     * 			: station name
     * @param stationNumber
     * 			: station number
     * @param stationDescription
     * 			: station description
     * @param region
     * 			: station region
     * @param stationAddress
     * 			: station address
     * @param stationWebsite
     * 			: station website
     * @param stationLogo
     * 			: station logo blob key
     * @param stationComments
     * 			: station comments
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException 
     */
    public Station(User user, 
    		Long stationType, 
    		Integer stationPrivilegeLevel, 
    		String stationName, 
    		String stationNumber, 
    		String stationDescription, 
    		Long region, 
    		PostalAddress stationAddress,
    		Link stationWebsite, 
    		BlobKey stationLogo, 
    		String stationComments) 
    		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (user == null || stationType == null || stationPrivilegeLevel == null ||
    			stationName == null || stationNumber == null ||
    			stationDescription == null) {
    		throw new MissingRequiredFieldsException(
    				this, "One or more required fields are missing.");
    	}
    	if (stationName.trim().isEmpty() || stationNumber.trim().isEmpty() ||
    			stationDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(
    				this, "One or more required fields are missing.");
    	}
    	
    	this.user = user;
    	
    	// Create key with user email
    	this.key = KeyFactory.createKey(Station.class.getSimpleName(), user.getUserEmail().getEmail());
    	
    	this.stationType = stationType;
    	this.stationPrivilegeLevel = stationPrivilegeLevel;
    	this.stationName = stationName;
    	this.stationNumber = stationNumber;
    	this.stationDescription = stationDescription;
    	this.region = region;
    	this.stationAddress = stationAddress;
        this.stationWebsite = stationWebsite;
        this.stationLogo = stationLogo;
        this.stationComments = stationComments;
        
    	// Create empty lists
    	this.channels = new ArrayList<Channel>();
    	this.playlists = new ArrayList<Playlist>();
    	this.stationImages = new ArrayList<StationImage>();
    	this.stationAudios = new ArrayList<StationAudio>();
    	
    	// Initialize the versions in 0
    	this.playlistVersion = 0;
    	this.stationImageVersion = 0;
    	this.stationAudioVersion = 0;
    }
    
    /**
     * Get Station key.
     * @return station key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Station user.
     * @return station user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Get Station Type key.
     * @return station type key
     */
    public Long getStationType() {
        return stationType;
    }
    
    /**
     * Get Station Channel list.
     * @return station channels
     */
    public ArrayList<Channel> getChannels() {
        return channels;
    }
    
    /**
     * Get Playlist list.
     * @return playlists
     */
    public ArrayList<Playlist> getPlaylists() {
    	return playlists;
    }
    
    /**
     * Get StationImage list.
     * @return stationImages
     */
    public ArrayList<StationImage> getStationImages() {
    	if (stationImages == null) {
    		stationImages = new ArrayList<>();
    	}
    	return stationImages;
    }
    
    /**
     * Get StationAudio list.
     * @return stationAudios
     */
    public ArrayList<StationAudio> getStationAudios() {
    	if (stationAudios == null) {
    		stationAudios = new ArrayList<>();
    	}
    	return stationAudios;
    }
    
    /**
     * Get Station's playlist version.
     * The playlist version increases by 1 each time a modification
     * is made to the playlists.
     * @return playlist version
     */
    public Integer getPlaylistVersion() {
        return playlistVersion;
    }
    
    /**
     * Get Station's StationImage version.
     * The StationImage version increases by 1 each time a modification
     * is made to the StationImages.
     * @return StationImage version
     */
    public Integer getStationImageVersion() {
    	if (stationImageVersion == null) {
    		stationImageVersion = 0;
    	}
        return stationImageVersion;
    }
    
    /**
     * Get Station's StationAudio version.
     * The StationAudio version increases by 1 each time a modification
     * is made to the StationAudios.
     * @return StationAudio version
     */
    public Integer getStationAudioVersion() {
    	if (stationAudioVersion == null) {
    		stationAudioVersion = 0;
    	}
        return stationAudioVersion;
    }
    
    /**
     * Get Station privilege level
     * @return privilege level
     */
    public Integer getStationPrivilegeLevel() {
    	return stationPrivilegeLevel;
    }
    
    /**
     * Get Station name.
     * @return station name
     */
    public String getStationName() {
        return stationName;
    }
    
    /**
     * Get Station number.
     * @return Station number
     */
    public String getStationNumber() {
        return stationNumber;
    }
    
    /**
     * Get Station description.
     * @return station description
     */
    public String getStationDescription() {
        return stationDescription;
    }
    
    /**
     * Get Station region.
     * @return region ID
     */
    public Long getRegion() {
    	return region;
    }
    
    /**
     * Get Station address.
     * @return station address
     */
    public PostalAddress getStationAddress() {
    	return stationAddress;
    }
    
    /**
     * Get Station web site.
     * @return station web site
     */
    public Link getStationWebsite() {
        return stationWebsite;
    }
    
    /**
     * Get Station logo.
     * @return station logo blobkey
     */
    public BlobKey getStationLogo() {
        return stationLogo;
    }
    
    /**
     * Get Station comments.
     * @return station comments
     */
    public String getStationComments() {
    	return stationComments;
    }
    
    /**
     * Compare this station with another Station
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Station, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof Station ) ) return false;
        Station r = (Station) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(r.getKey()));
    }
    
    /**
     * Add a new channel to the station.
     * @param channel
     * 			: new channel to be added
     */
    public void addChannel(Channel channel) {
    	this.channels.add(channel);
    }
    
    /**
     * Add a new playlist to this station.
     * @param playlist
     * 			: new playlist to be added
     */
    public void addPlaylist(Playlist playlist) {
    	this.playlists.add(playlist);
    }
    
    /**
     * Add a new StationImage to this station.
     * @param stationImage
     * 			: new StationImage to be added
     */
    public void addStationImage(StationImage stationImage) {
    	if (stationImages == null) {
    		stationImages = new ArrayList<>();
    	}
    	this.stationImages.add(stationImage);
    }
    
    /**
     * Add a new StationAudio to this station.
     * @param stationAudio
     * 			: new StationAudio to be added
     */
    public void addStationAudio(StationAudio stationAudio) {
    	if (stationAudios == null) {
    		stationAudios = new ArrayList<>();
    	}
    	this.stationAudios.add(stationAudio);
    }
    
    /**
     * Remove a channel from the station.
     * @param channel
     * 			: channel to be removed
     * @throws InexistentObjectException
     */
    public void removeChannel(Channel channel) 
    		throws InexistentObjectException {
    	if (!channels.remove(channel)) {
    		throw new InexistentObjectException(
    				Channel.class, "Channel not found!");
    	}
    }
    
    /**
     * Remove playlist from the station.
     * @param playlist
     * 			: playlist to be removed
     * @throws InexistentObjectException
     */
    public void removePlaylist(Playlist playlist) 
    		throws InexistentObjectException {
    	if (!this.playlists.remove(playlist)) {
    		throw new InexistentObjectException
    				(Playlist.class, "Playlist not found!");
    	}
    }
    
    /**
     * Remove StationImage from the station.
     * @param stationImage
     * 			: StationImage to be removed
     * @throws InexistentObjectException
     */
    public void removeStationImage(StationImage stationImage) 
    		throws InexistentObjectException {
    	if (!this.stationImages.remove(stationImage)) {
    		throw new InexistentObjectException
    				(StationImage.class, "StationImage not found!");
    	}
    }
    
    /**
     * Remove StationAudio from the station.
     * @param stationAudio
     * 			: StationAudioto be removed
     * @throws InexistentObjectException
     */
    public void removeStationAudio(StationAudio stationAudio) 
    		throws InexistentObjectException {
    	if (!this.stationAudios.remove(stationAudio)) {
    		throw new InexistentObjectException
    				(StationAudio.class, "StationAudio not found!");
    	}
    }
    
    /**
     * Update the Playlist Version number by 1.
     */
    public void updatePlaylistVersion() {
    	playlistVersion++;
    }
    
    /**
     * Update the StationImage Version number by 1.
     */
    public void updateStationImageVersion() {
    	if (stationImageVersion == null) {
    		stationImageVersion = 0;
    	}
    	stationImageVersion++;
    }
    
    /**
     * Update the StationAudio Version number by 1.
     */
    public void updateStationAudioVersion() {
    	if (stationAudioVersion == null) {
    		stationAudioVersion = 0;
    	}
    	stationAudioVersion++;
    }
    
    /**
     * Set Station type.
     * @param stationType
     * 			: station type key
     * @throws MissingRequiredFieldsException
     */
    public void setStationType(Long stationType) 
    		throws MissingRequiredFieldsException {
    	if (stationType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Station type is missing.");
    	}
    	this.stationType = stationType;
    }
    
    /**
     * Set Station privilege level.
     * @param stationPrivilegeLevel
     * 			: station privilege level
     * @throws MissingRequiredFieldsException
     */
    public void setPrivilegeLevel(Integer stationPrivilegeLevel) 
    		throws MissingRequiredFieldsException {
    	if (stationPrivilegeLevel == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Privilege Level is missing.");
    	}
    	this.stationPrivilegeLevel = stationPrivilegeLevel;
    }
     
    /**
     * Set Station name.
     * @param stationName
     * 			: station name
     * @throws MissingRequiredFieldsException
     */
    public void setStationName(String stationName)
    		throws MissingRequiredFieldsException {
    	if (stationName == null || stationName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Station name is missing.");
    	}
    	this.stationName = stationName;
    }
    
    /**
     * Set Station number.
     * @param stationNumber
     * 			: stationNumber
     * @throws MissingRequiredFieldsException
     */
    public void setStationNumber(String stationNumber)
    		throws MissingRequiredFieldsException {
    	if (stationNumber == null || stationNumber.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Station number is missing.");
    	}
    	this.stationNumber = stationNumber;
    }
    
    /**
     * Set Station description.
     * @param stationDescription
     * 			: station description
     * @throws MissingRequiredFieldsException
     */
    public void setStationDescription(String stationDescription)
    		throws MissingRequiredFieldsException {
    	if (stationDescription == null || 
    			stationDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Station Description is missing.");
    	}
    	this.stationDescription = stationDescription;
    }
    
    /**
     * Set Station region
     * @param region
     * 			: region ID
     */
    public void setRegion(Long region) {
    	this.region = region;
    }
    
    /**
     * Set Station address
     * @param stationAddress
     * 			: station address
     */
    public void setStationAddress(PostalAddress stationAddress) {
    	this.stationAddress = stationAddress;
    }
    
    /**
     * Set Station web site.
     * @param stationWebsite
     * 			: station web site
     */
    public void setStationWebsite(Link stationWebsite) {
    	this.stationWebsite = stationWebsite;
    }
    
    /**
     * Set Station logo.
     * @param stationLogo
     * 			: station logo blob key
     */
    public void setStationLogo(BlobKey stationLogo) {
    	this.stationLogo = stationLogo;
    }
    
    /**
     * Set Station comments.
     * @param stationComments
     * 			: station comments
     */
    public void setStationComments(String stationComments) {
    	this.stationComments = stationComments;
    }
    
}
