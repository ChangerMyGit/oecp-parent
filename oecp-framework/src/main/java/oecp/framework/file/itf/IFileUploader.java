/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-11-17
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

package oecp.framework.file.itf;

import java.io.File;

import oecp.framework.file.enums.FileSource;


/**
 * 文件上传接口
 * <br> 文件上传Action实现此接口
 * @author slx
 * @date 2009-11-17 下午04:38:58
 * @version 1.0
 */
public interface IFileUploader {

	/**
	 * 返回上传的文件
	 * @author slx
	 * @date 2009-11-17 下午04:43:13
	 * @modifyNote
	 * @return
	 */
	public File[] getUpload() ;
	
	/**
	 * 返回上传文件的类型
	 * @author slx
	 * @date 2009-11-17 下午04:43:23
	 * @modifyNote
	 * @return
	 */
	public String[] getUploadContentType();
	
	/**
	 * 返回上传文件的文件名
	 * @author slx
	 * @date 2009-11-17 下午04:43:33
	 * @modifyNote
	 * @return
	 */
	public String[] getUploadFileName();
	
	/**
	 * 返回现实名数组,如果为空则与文件名相同
	 * @author slx
	 * @date 2009-11-17 下午04:43:44
	 * @modifyNote
	 * @return
	 */
	public String[] getDisplayName();
	
	/**
	 * 上传用户
	 * @author slx
	 * @date 2009-11-17 下午04:44:42
	 * @modifyNote
	 * @return
	 */
	public String getUid();
	
	/**
	 * 文件保存相对路径,没有的话按照用户名保存.
	 * @author slx
	 * @date 2009-11-17 下午04:44:50
	 * @modifyNote
	 * @return
	 */
	public String getSavePath();
	
	public FileSource getFileSource();
	
	
}
