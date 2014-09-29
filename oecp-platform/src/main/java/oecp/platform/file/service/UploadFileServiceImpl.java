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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.pkgen.String16PKGenerater;
import oecp.framework.exception.BizException;
import oecp.framework.file.itf.IFileUploader;
import oecp.framework.util.DateUtil;
import oecp.framework.util.PropertiesUtil;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.file.eo.UploadFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;




/**
 * 文件上传服务实现类
 * 
 * @author slx
 * @date 2009-11-17 下午05:12:43
 * @version 1.0
 */
@Service("uploadFileService2")
public class UploadFileServiceImpl extends PlatformBaseServiceImpl<UploadFile>
		implements UploadFileService {

	private IFileUploader uploader;
	private File[] files = null;
	private List<UploadFile> fileEOs;
	private static String diskFilePath;
	private boolean buildPath=true;
	private PropertiesUtil propertiesUtil = new PropertiesUtil("/app.properties");
	
	public UploadFileServiceImpl() {
		if(diskFilePath == null){
			try {
				diskFilePath = propertiesUtil.readValue("file.root");
			} catch (IOException e) {
				e.printStackTrace();
			}
			diskFilePath = filtRootPath(diskFilePath);
		}
	}
	
	/**
	 * 将没有配置和配置为.或/的设置为默认值--webroot目录
	 * @author slx
	 * @date 2010-8-13 上午09:18:37
	 * @modifyNote
	 * @param rootPath
	 * @return
	 */
	private String filtRootPath(String rootPath){
		if(StringUtils.isEmpty(rootPath) 
				|| ".".equals(rootPath)
				|| "/".equals(rootPath)){
			rootPath = getWebRootRealPath();
		}
		return rootPath;
	}
	

	@Override
	public List<UploadFile> saveFiles(IFileUploader uploader)
			throws BizException {
		this.uploader = uploader;
		files = uploader.getUpload();
		if (files == null || files.length == 0 || files[0] == null)
			return null;
		

		// 保存EO
		List<UploadFile> files = saveFileEOs();
		// 保存文件到磁盘
		try {
			putFilesOnDisk();
			// TODO 更新用户空间容量统计
		} catch (IOException e) {
			e.printStackTrace();
			throw new BizException("文件保存失败:" + e);
		}
		return files;
	}
	
	@Override
	public List<UploadFile>saveFilesButNoSaveEO(IFileUploader uploader){
		this.uploader = uploader;
		files = uploader.getUpload();
		if (files == null || files.length == 0 || files[0] == null)
			return null;
		
		// 获取EO
		List<UploadFile> files_list = buildFileEOs();
		// 保存文件到磁盘
		try {
			putFilesOnDisk();
			// TODO 更新用户空间容量统计
		} catch (IOException e) {
			e.printStackTrace();
//			throw new BizException("文件保存失败:" + e);
		}
		return files_list;
	}
	
	/**
	 * 得到网站的根目录据对路径
	 * @author slx
	 * @date 2010-8-13 上午08:52:23
	 * @modifyNote
	 * @return
	 */
	protected String getWebRootRealPath(){
		// classpath 向上2级
		return Thread.currentThread().getContextClassLoader().getResource("/../..").getPath();
	}

	protected List<UploadFile> saveFileEOs() throws BizException {
		fileEOs = buildFileEOs();
		getDao().createBatch(fileEOs);
		return fileEOs;
	}

	protected void putFilesOnDisk() throws IOException {
		int len = files.length;
		for (int i = 0; i < len; i++) {
			String filepath = fileEOs.get(i).getFilepath();
			filepath = diskFilePath + "/" + filepath;
			FileUtils.copyFile(files[i], new File(filepath));
		}
	}

	protected List<UploadFile> buildFileEOs() {
		fileEOs = new ArrayList<UploadFile>();
		int len = files.length;
		for (int i = 0; i < len; i++) {
			UploadFile fileEO = new UploadFile();
			fileEO.setDownNum(0);
			fileEO.setUploadTime(new Date());
			fileEO.setContentType(uploader.getUploadContentType()[i]);
			fileEO.setName(getDisplayName(i));
			fileEO.setSName(uploader.getUploadFileName()[i]);
			fileEO.setSize(Double.longBitsToDouble(files[i].length()));
			fileEO.setUploadUser(uploader.getUid());

			if (getSavePath().lastIndexOf(".") > -1) {// 如果保存的路径存在"."，则savepath即为文件的存储路径，则不需要重新生成路径。@author
				// yongtree
				fileEO.setFilepath(getSavePath());
			} else {
				if(buildPath){//需要重新生成路径
					fileEO.setFilepath(buildFilePath(uploader.getUploadFileName()[i]));
				}else{
					fileEO.setFilepath(getSavePath()+"/"+filenameGen.generate(null, null)+ getExt(uploader.getUploadFileName()[i]));
				}
			}

			fileEO.setSource(uploader.getFileSource());
			fileEOs.add(fileEO);
		}
		return fileEOs;
	}

	private String getDisplayName(int i) {
		String name = null;
		if (uploader.getDisplayName() == null
				|| uploader.getDisplayName().length == 0) {
			name = uploader.getUploadFileName()[i];
			return name;
		}

		name = uploader.getDisplayName()[i];
		if (name == null || name.trim().length() == 0) {
			name = uploader.getUploadFileName()[i];
			return name;
		}
		return name;
	}

	private String16PKGenerater filenameGen = new String16PKGenerater();

	protected String buildFilePath(String filename) {

		return getUserPath() + "/" + filenameGen.generate(null, null)
				+ getExt(filename);
	}

	protected String getUserPath() {
		return getSavePath() + "/"
				+ DateUtil.getDateStr(new Date(), "yyyy-MM-dd");
	}

	protected String getExt(String filename) {
		int pos = filename.lastIndexOf(".");
		if (pos == -1)
			return "";

		return filename.substring(pos, filename.length());
	}

	private String getSavePath() {
		String p = uploader.getSavePath();
		if (p == null || p.trim().length() == 0) {
			p = uploader.getUid();
		}

		return p;
	}

	@Override
	public void deleteFiles(List<UploadFile> files, boolean deleteDiskFile) {
		try {
			for (UploadFile ulfile : files) {
				if (deleteDiskFile) {// 删除磁盘文件
					String filepath = ulfile.getFilepath();
					filepath = diskFilePath + filepath;
					filepath=filepath.replaceAll("//", "/");
					File file = new File(filepath);
					file.delete();
				}
				this.getDao().delete(UploadFile.class, ulfile.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFiles(Serializable[] ids, boolean deleteDiskFile) throws BizException {
		List<UploadFile> list = new ArrayList<UploadFile>();
		for (Serializable id : ids) {
			UploadFile file = this.find(id);
			list.add(file);
		}
		deleteFiles(list, deleteDiskFile);
	}
	
	@Override
	public void deleteFileByFilePath(String filePath) {
		List<UploadFile> files = getDao().queryByWhere(UploadFile.class, " o.filepath=? ", new Object[]{filePath});
		
		deleteFiles(files, true);
	}

	@Override
	public QueryResult<UploadFile> findAll(QueryObject qo, int start, int limit) {
		return this.getDao().getScrollData(UploadFile.class, start, limit,
				qo == null ? null : qo.getWhereQL(), qo == null ? null : qo
						.getQueryParams(), new LinkedHashMap<String, String>() {
					{
						put("uploadTime", "desc");
					}
				});
	}

	public void setBuildPath(boolean buildPath) {
		this.buildPath = buildPath;
	}
	
}
