/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * This is a singleton wrapper class to manage the PersistenceManagerFactory.
 * This forces the app to reuse a single instance of the PersistenceManagerFactory.
 * 
 */

public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    /**
     * Get single instance of PersistenceManagerFactory
     * @return PMF instance
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}