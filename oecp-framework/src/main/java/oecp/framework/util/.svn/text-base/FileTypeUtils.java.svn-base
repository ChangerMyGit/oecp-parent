/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-11-18
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */    

package oecp.framework.util;

import oecp.framework.file.enums.FileType;

/**
 * 文件类型工具类
 * @author slx
 * @date 2009-11-18 上午10:28:59
 * @version 1.0
 */
public class FileTypeUtils {
	
	 public static boolean isPicture(String filename){
			// 文件名称为空的场合
			if (filename == null || "".equals(filename)) {
				// 返回不和合法
				return false;
			}
			// 获得文件后缀名
			String tmpName = filename.substring(filename.lastIndexOf(".") + 1, filename
					.length());
			// 声明图片后缀名数组
			String imgeArray[] = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg",
					"jpg", "png", "tif", "tiff", "ico" };
			// 遍历名称数组
			for (int i = 0; i < imgeArray.length; i++) {
				// 判断单个类型文件的场合
				if (filename != null && !"".equals(filename)
						&& imgeArray[i].equals(tmpName.toLowerCase())) {
					return true;
				}

			}
			return false;
		}

		public static boolean isBrowseFile(String filename) {
			// 文件名称为空的场合
			if (filename == null || "".equals(filename)) {
				// 返回不和合法
				return false;
			}
			// 获得文件后缀名
			String tmpName = filename.substring(filename.lastIndexOf(".") + 1, filename
					.length());
			// 声明图片后缀名数组
			String imgeArray[] = { "pdf", "doc", "xls", "ppt" ,"swf"};
			// 遍历名称数组
			for (int i = 0; i < imgeArray.length; i++) {
				// 判断单个类型文件的场合
				if (filename != null && !"".equals(filename)
						&& imgeArray[i].equals(tmpName.toLowerCase())) {
					return true;
				}

			}
			return false;
		}

		/**
		 * 得到文件的类型
		 * <br> 暂时根据后缀名判断,以后再升级
		 * @author slx
		 * @date 2009-11-18 上午10:33:38
		 * @modifyNote
		 * @param filename
		 * @return
		 */
		public static FileType getFileType(String filename){
			if(isPicture(filename)){
				return FileType.IMAGE;
			}
			if(isBrowseFile(filename)){
				return FileType.OPENFILE;
			}
			
			return FileType.DOWNFILE;
		}
}
