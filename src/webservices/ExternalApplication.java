/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import webservices.external_resources.CustomerProfileResource;
import webservices.external_resources.GenresResource;
import webservices.external_resources.MusicLibraryResource;
import webservices.external_resources.PlaylistsResource;
import webservices.external_resources.ProgramsResource;
import webservices.external_resources.MyFavoritesResource;
import webservices.external_resources.StationAudiosResource;
import webservices.external_resources.StationImagesResource;
import webservices.external_resources.StationProfileResource;
import webservices.external_resources.StationsResource;
import webservices.external_resources.StationTypesResource;
import webservices.external_resources.SystemResource;
import webservices.external_resources.UserGroupsResource;
import webservices.external_resources.UserRecommendationsResource;

/**
 * This class represents an instance of the Restlet Application
 */

public class ExternalApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls
     * @return A router instance
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of each Resource
        Router router = new Router(getContext());
        
        // Define route to SystemResource 1.1
        router.attach("/system", SystemResource.class);
        
        // Define route to CustomerProfileResource 1.2
        router.attach("/customerProfile/{queryinfo}", 
        		CustomerProfileResource.class);
        
        // Define route to StationTypesResource 1.3
        router.attach("/stationTypes", StationTypesResource.class);
        
        // Define route to StationsResource 1.4
        router.attach("/stations", StationsResource.class);
        
        // Define route to ProgramsResource 1.5
        router.attach("/programs/{queryinfo}", ProgramsResource.class);
        
        // Define route to MusicLibraryResource 1.6
        router.attach("/musicLibrary", MusicLibraryResource.class);
        router.attach("/musicLibrary/{queryinfo}", MusicLibraryResource.class);
        
        // Define route to PlaylistsResource 1.7
        router.attach("/playlists/{queryinfo}", PlaylistsResource.class);
        
        // Define route to RadioProfileResource 1.8
        router.attach("/stationProfile/{queryinfo}", StationProfileResource.class);
        
        // Define route to StationAudioResource 1.11
        router.attach("/stationAudio/{queryinfo}", StationAudiosResource.class);
        
        // Define route to StationImageResource 1.12
        router.attach("/stationImages/{queryinfo}", StationImagesResource.class);
        
        // Define route to Genres 1.12
        router.attach("/genres", GenresResource.class);
        
        /////////////////////////////// BETA VERSION ///////////////////////
        
        // Define route to UserGroupResource
        router.attach("/userGroups/{queryinfo}", UserGroupsResource.class);

        // Define route to UserRecommendationsResource
        router.attach("/userRecommendations/{queryinfo}", 
        		UserRecommendationsResource.class);
        
        // Define route to MyFavoritesResource
        router.attach("/myFavorites/{queryinfo}", 
        		MyFavoritesResource.class);
        
        return router;
    }

}
