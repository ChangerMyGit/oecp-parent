/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
 */

package oecp.framework.fs.db.xservice;

import java.io.File;

import oecp.framework.exception.BizException;
import oecp.framework.fs.db.xeo.UploadFile;
import oecp.framework.service.BaseService;

/**
 * 上传文件到DB的服务类
 * 
 * @author yongtree
 * @date 2011-12-27 上午09:47:21
 * @version 1.0
 * 
 */
public interface UploadFileService extends BaseService<UploadFile> {

	public UploadFile save(File file,String contentType,String filename)throws BizException;
	
	public UploadFile save(File file)throws BizException;
	
	public byte[] findFileBytes(String id)throws BizException;
	
	public File findFile(String id) throws BizException;
	
	
}
