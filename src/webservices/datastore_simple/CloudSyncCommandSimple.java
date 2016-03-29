/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the CloudSyncCommand table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class CloudSyncCommandSimple implements Serializable {

	public String key;
	public String cloudSyncCommandMasterEmail;
	public String cloudSyncCommandMessage;
	public String cloudSyncCommandCreationDate;
    
    /**
     * CloudSyncCommandSimple constructor.
     * @param key
     * 			: CloudSyncCommand key string
     * @param cloudSyncCommandMasterEmail
     * 			: the email of the CloudSyncCommand master
     * @param cloudSyncCommandMessage
     * 			: the message to sync
     * @param cloudSyncCommandCreationDate
     * 			: the creation date of the cloud sync command
     */
    public CloudSyncCommandSimple(String key, String cloudSyncCommandMasterEmail,
    		String cloudSyncCommandMessage, String cloudSyncCommandCreationDate) {

    	this.key = key;
    	this.cloudSyncCommandMasterEmail = cloudSyncCommandMasterEmail;
    	this.cloudSyncCommandMessage = cloudSyncCommandMessage;
    	this.cloudSyncCommandCreationDate = cloudSyncCommandCreationDate;
    }
    
    /**
     * Compare this cloudSyncCommand with another CloudSyncCommand
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this 
     * 			CloudSyncCommand, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof CloudSyncCommandSimple ) ) return false;
        CloudSyncCommandSimple m = (CloudSyncCommandSimple) o;
        return this.key.equals(m.key);
    }
    
}
