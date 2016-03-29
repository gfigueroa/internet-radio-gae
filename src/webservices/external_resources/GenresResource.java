/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.GenreSimple;
import datastore.Genre;
import datastore.GenreManager;

/**
 * This class represents the list of genres 
 * as a Resource with only one representation
 */

public class GenresResource extends ServerResource {

	/**
	 * Returns the genre list as a JSON object.
	 * @return An ArrayList of Genre in JSON format
	 */
    @Get("json")
    public ArrayList<GenreSimple> toJson() {
        
    	List<Genre> genres = GenreManager.getAllGenres();
    	
    	// We will be returning a list of elements
    	ArrayList<GenreSimple> genresSimple =
        		new ArrayList<GenreSimple>();
    	for (Genre genre : genres) {
    		GenreSimple genreSimple 
    				= new GenreSimple(genre.getKey(),
    						genre.getGenreEnglishName(),
    						genre.getGenreChineseName() != null ?
    								genre.getGenreChineseName() : ""
    						);
    		genresSimple.add(genreSimple);
    	}

        return genresSimple;
    }

}