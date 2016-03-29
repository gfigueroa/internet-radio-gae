/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet class is used to serve files in the system.
 * 
 */

public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -4946232324788441798L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws IOException {
        
		BlobInfoFactory bif = new BlobInfoFactory();
		
		BlobKey blobKey = new BlobKey(req.getParameter("file_id"));
        String fileName = bif.loadBlobInfo(blobKey).getFilename();
        
        res.setContentType("text/plain");
    	res.setHeader("Content-Disposition",
                         "attachment; filename=" + fileName);
        
        blobstoreService.serve(blobKey, res);
        
    }

}