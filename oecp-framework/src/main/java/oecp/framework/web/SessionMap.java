/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.framework.web;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 自定义的sessionmap，主要是想Cache中同步数据
 * 
 * @author yongtree
 * @date 2011-5-9 上午10:48:20
 * @version 1.0
 */
public class SessionMap extends HashMap<String, Serializable> {

	private static final long serialVersionUID = 1L;

}
