/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
*/                                                                
  

package oecp.framework.util;

import org.apache.commons.lang.StringUtils;

/** 
 * 提供org.apache.commons.lang.StringUtils额外的操作
 * @author yongtree  
 * @date 2011-12-3 上午11:02:04 
 * @version 1.0
 *  
 */
public class StringUtil extends StringUtils {


	/**
	 * 将字符串数组用指定连接符拼接成字符串
	 * @author yongtree
	 * @date 2011-12-3上午11:08:25
	 * @param arr 数组
	 * @param sep 连接符（比如：, |） 
	 * @return
	 */
	public static String arr2str(String[] arr,String sep){
		if(arr.length>0){
			StringBuffer sb = new StringBuffer();			
			for(String s : arr){
				sb.append(s).append(sep);
			}
			return sb.toString().substring(0, sb.toString().lastIndexOf(sep));
		}else{
			return null;
		}
	}
	
	public static void main(String[] args) {
		String[] arr = {"123","456","789"};
		System.out.println(arr2str(arr, ","));
	}
	
	
}
