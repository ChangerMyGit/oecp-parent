/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.event.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 事件监听器注解<br>
 * <br>
 * 
 * @author wangliang  
 * @date 2012-2-8 下午3:53:29 
 * @version 1.0
 *  
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Listener {
	/**
	 * 监听的事件源实体类完整类名<br>
	 */
	public String source();
}
