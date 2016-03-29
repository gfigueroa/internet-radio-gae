/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

/**
 * This class represents a simple version of the Station table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class CustomerSimple implements Serializable {
    
	public String key;
	public String customerName;
	public PhoneNumber customerPhone;
	public String customerGender;
	public PostalAddress customerAddress;
    
    /**
     * CustomerSimple constructor.
     * @param key
     * 			: restaurant key
     * @param customerName
     * 			: customer name
     * @param customerPhone
     * 			: customer phone
     * @param customerGender
     * 			: customer gender
     * @param customerAddress
     * 			: customer address
     */
    public CustomerSimple(String key, String customerName, 
    		PhoneNumber customerPhone, String customerGender,
    		PostalAddress customerAddress) {

    	this.key = key;
    	this.customerName = customerName;
    	this.customerPhone = customerPhone;
    	this.customerGender = customerGender;
    	this.customerAddress = customerAddress;
    }
    
    /**
     * Compare this customer with another Customer
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Customer, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof CustomerSimple ) ) return false;
        CustomerSimple c = (CustomerSimple) o;
        return this.key.equals(c.key);
    }
    
}
