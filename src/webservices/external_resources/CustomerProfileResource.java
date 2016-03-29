/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.CustomerSimple;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;

/**
 * This class represents the list of orders
 * as a Resource with only one representation
 */

public class CustomerProfileResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(CustomerProfileResource.class.getName());

	/**
	 * Returns the customer profile information as a JSON object.
	 * @return the customer profile in JSON format
	 */
    @Get("json")
    public CustomerSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
        char searchBy = queryInfo.charAt(0);
        String searchEmailString = queryInfo.substring(2);
        Email searchEmail = new Email(searchEmailString);
        
        log.info("Query: " + searchBy + "=" + searchEmailString);
    	
        Customer customer = CustomerManager.getCustomer(searchEmail);
        CustomerSimple customerSimple = new CustomerSimple(KeyFactory.keyToString(customer.getKey()),
        		customer.getCustomerName(), customer.getCustomerPhone(), customer.getCustomerGenderString(),
        		customer.getCustomerAddress());
        
        return customerSimple;
    }

}