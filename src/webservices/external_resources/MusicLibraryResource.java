/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.MusicFileSimple;
import datastore.Genre;
import datastore.GenreManager;
import datastore.MusicFile;
import datastore.MusicFileManager;

/**
 * This class represents the list of music files 
 * as a Resource with only one representation
 */

public class MusicLibraryResource extends ServerResource {

	/**
	 * Returns the music file list as a JSON object.
	 * @return An ArrayList of MusicFile in JSON format
	 */
    @Get("json")
    public ArrayList<MusicFileSimple> toJson() {
        
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

    	List<MusicFile> musicFiles = null;
    	if (queryInfo != null) {
    		char searchBy = queryInfo.charAt(0);
    	    String searchString = queryInfo.substring(2);
    	    if (searchBy == 'g' || searchBy == 'G') {
	    	    Long genreKey = Long.parseLong(searchString);
	    	    musicFiles = MusicFileManager.getMusicFilesByGenre(genreKey);
    		}
    	    else if (searchBy == 'k' || searchBy == 'K') {
    	    	Long musicFileKey = Long.parseLong(searchString);
    	    	musicFiles = new ArrayList<>();
    	    	MusicFile musicFile = MusicFileManager.getMusicFile(musicFileKey);
    	    	musicFiles.add(musicFile);
    	    }
    	    else {
    	    	return null;
    	    }
    	}
    	else {
    		musicFiles = MusicFileManager.getAllMusicFiles();
    	}
    	
    	// We will be returning a list of elements
    	ArrayList<MusicFileSimple> musicFilesSimple =
        		new ArrayList<MusicFileSimple>();
    	for (MusicFile musicFile : musicFiles) {
    		
    		Genre genre = GenreManager.getGenre(musicFile.getGenre());
    		
    		MusicFileSimple musicFileSimple 
    				= new MusicFileSimple(
    						musicFile.getKey(),
    						musicFile.getGenre(),
    						genre.getGenreEnglishName(),
    						genre.getGenreChineseName(),
    						musicFile.getMusicFileFile(),
    						musicFile.getMusicFileTitle(),
    						musicFile.getMusicFileArtist(),
    						musicFile.getMusicFileAlbum() != null ?
    								musicFile.getMusicFileAlbum() : "",
    						musicFile.getMusicFileAlbumArtist() != null ?
    								musicFile.getMusicFileAlbumArtist() : "",
    						musicFile.getMusicFileYear() != null ?
    								musicFile.getMusicFileYear() : 0,
    						musicFile.getMusicFileComposer() != null ?
    								musicFile.getMusicFileComposer() : "",
    						musicFile.getMusicFilePublisher() != null ?
    								musicFile.getMusicFilePublisher() : "",
    						musicFile.getMusicFileDuration(),
    						musicFile.getMusicFileFormat(),
    						DateManager.printDateAsString(
    								musicFile.getMusicFileCreationTime()),
    						DateManager.printDateAsString(
    								musicFile.getMusicFileModificationTime())
    						);
    		musicFilesSimple.add(musicFileSimple);
    	}

        return musicFilesSimple;
    }

}