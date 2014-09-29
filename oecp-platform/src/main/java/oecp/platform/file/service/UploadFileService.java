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

package oecp.platform.file.service;

import java.io.Serializable;
import java.util.List;

import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.file.itf.IFileUploader;
import oecp.framework.service.BaseService;
import oecp.platform.file.eo.UploadFile;




/**
 * 文件上传服务接口
 * 
 * @author slx
 * @date 2009-11-17 下午05:10:16
 * @version 1.0
 */
public interface UploadFileService extends BaseService<UploadFile> {

	/**
	 * 保存要上传的文件
	 * 
	 * @author slx
	 * @date 2009-11-17 下午05:11:57
	 * @modifyNote
	 * @param uploader
	 * @throws BizException
	 */
	public List<UploadFile> saveFiles(IFileUploader uploader)
			throws BizException;
	/**
	 * 保存上传文件，但是文件信息不存入数据库
	 * @author wangliang
	 * @date 2010-12-15 下午02:25:40
	 * @modifyNote
	 * @param uploader
	 * @return
	 * @throws BizException
	 */
	public List<UploadFile> saveFilesButNoSaveEO(IFileUploader uploader) 
			throws BizException;
	/**
	 * 删除文件
	 * 
	 * @author yongtree
	 * @date 2009-11-18 下午09:26:34
	 * @modifyNote
	 * @param files
	 *            //文件EO
	 * @param deleteDiskFile
	 *            //是否删除磁盘的文件
	 * @throws BizException
	 */
	public void deleteFiles(List<UploadFile> files, boolean deleteDiskFile) throws BizException;
	
	/**
	 * 根据文件路径删除文件(物理删除)
	 * @author slx
	 * @date 2010-8-13 下午01:43:09
	 * @modifyNote
	 * @param filePath
	 */
	public void deleteFileByFilePath(String filePath);
	
	/**
	 * 删除文件
	 * 
	 * @author yongtree
	 * @date 2009-11-18 下午09:26:34
	 * @modifyNote
	 * @param files
	 *            //文件EO
	 * @param deleteDiskFile
	 *            //是否删除磁盘的文件
	 * @throws BizException
	 */
	public void deleteFiles(Serializable[] ids, boolean deleteDiskFile) throws BizException;
	
	public QueryResult<UploadFile> findAll(QueryObject qo,int start,int limit) throws BizException;
	/**
	 * 是否生成路径
	 * @author Administrator
	 * @date 2010-5-31 下午03:42:58
	 * @modifyNote
	 * @param buildPath
	 */
	public void setBuildPath(boolean buildPath) throws BizException;
}
