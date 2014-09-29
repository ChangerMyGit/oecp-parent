/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */    

package oecp.framework.cache;

/**
 * 
 * @author yongtree
 * @date 2011-4-26 下午04:56:01
 * @version 1.0
 */
public interface CacheProvider {



	/**

	 * Configure the cache

	 *

	 * @param regionName the name of the cache region

	 * @param autoCreate autoCreate settings

	 * @throws CacheException

	 */

	public Cache buildCache(String regionName, boolean autoCreate) throws CacheException;



	/**

	 * Callback to perform any necessary initialization of the underlying cache implementation

	 * during SessionFactory construction.

	 *

	 * @param properties current configuration settings.

	 */

	public void start() throws CacheException;



	/**

	 * Callback to perform any necessary cleanup of the underlying cache implementation

	 * during SessionFactory.close().

	 */

	public void stop();

	

}
