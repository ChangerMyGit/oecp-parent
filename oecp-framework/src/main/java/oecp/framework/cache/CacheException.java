/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */    

package oecp.framework.cache;

/**
 * 缓存异常
 * @author yongtree
 * @date 2011-4-26 下午04:54:03
 * @version 1.0
 */
	public class CacheException extends RuntimeException {



		public CacheException(String s) {

			super(s);

		}



		public CacheException(String s, Throwable e) {

			super(s, e);

		}



		public CacheException(Throwable e) {

			super(e);

		}

		

	}

