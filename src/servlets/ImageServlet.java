/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet class is used to serve images in the system.
 * 
 */

@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		BlobKey blobKey = new BlobKey(req.getParameter("blobkey"));
        String maxLengthString = req.getParameter("s");
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);

        if (maxLengthString == null || maxLengthString.isEmpty()) {
        	//res.sendRedirect(imagesService.getServingUrl(blobKey));
        	res.sendRedirect(imagesService.getServingUrl(servingUrlOptions));
        }
        else {
        	int maxLength = Integer.parseInt(maxLengthString);
        	//res.sendRedirect(imagesService.getServingUrl(blobKey, maxLength, false));
        	res.sendRedirect(imagesService.getServingUrl(servingUrlOptions.imageSize(maxLength)));
        }
    }

}