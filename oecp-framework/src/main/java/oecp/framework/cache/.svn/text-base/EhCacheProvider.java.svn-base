/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */    

package oecp.framework.cache;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author yongtree
 * @date 2011-4-26 下午05:00:41
 * @version 1.0
 */
public class EhCacheProvider implements CacheProvider {



    private static final Log log = LogFactory.getLog(EhCacheProvider.class);



	private net.sf.ehcache.CacheManager manager;

	private Hashtable<String, EhCache> _CacheManager ;



    /**

     * Builds a Cache.

     * &lt;p&gt;

     * Even though this method provides properties, they are not used.

     * Properties for EHCache are specified in the ehcache.xml file.

     * Configuration will be read from ehcache.xml for a cache declaration

     * where the name attribute matches the name parameter in this builder.

     *

     * @param name the name of the cache. Must match a cache configured in ehcache.xml

     * @param properties not used

     * @return a newly built cache will be built and initialised

     * @throws CacheException inter alia, if a cache of the same name already exists

     */

    public EhCache buildCache(String name, boolean autoCreate) throws CacheException {

    	EhCache ehcache = _CacheManager.get(name);

    	if(ehcache == null && autoCreate){

		    try {

	            net.sf.ehcache.Cache cache = manager.getCache(name);

	            if (cache == null) {

	                log.warn("Could not find configuration [" + name + "]; using defaults.");

	                manager.addCache(name);

	                cache = manager.getCache(name);

	                log.debug("started EHCache region: " + name);                

	            }

	            synchronized(_CacheManager){

		            ehcache = new EhCache(cache);

		            _CacheManager.put(name, ehcache);

		            return ehcache ;

	            }

		    }

	        catch (net.sf.ehcache.CacheException e) {

	            throw new CacheException(e);

	        }

    	}

        return ehcache;

    }



	/**

	 * Callback to perform any necessary initialization of the underlying cache implementation

	 * during SessionFactory construction.

	 *

	 * @param properties current configuration settings.

	 */

	public void start() throws CacheException {

		if (manager != null) {

            log.warn("Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() " +

                    " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider." +

                    " If this behaviour is required, consider using net.sf.ehcache.hibernate.SingletonEhCacheProvider.");

            return;

        }

        manager = new net.sf.ehcache.CacheManager();

        _CacheManager = new Hashtable<String,EhCache>();

	}



	/**

	 * Callback to perform any necessary cleanup of the underlying cache implementation

	 * during SessionFactory.close().

	 */

	public void stop() {

		if (manager != null) {

            manager.shutdown();

            manager = null;

        }

	}



}

