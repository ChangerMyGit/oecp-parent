/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.framework.fs;


/** 
 *
 * 图片文件属性  
 * @author yongtree
 * @date 2011-7-8 上午11:31:23 
 * @version 1.0
 *  
 */
public class ImageProperty extends FileProperty {

	private int width;
	
	private int height;

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
}
