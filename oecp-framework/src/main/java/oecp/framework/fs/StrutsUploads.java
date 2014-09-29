/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.framework.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 为Struts2提供上传的页面bean。目的是简化struts2 action类的代码<br>
 * Java文件中：<br>
 * private StrutsUploads sus; <br>
 * JSP上使用<s:file name="sus.upload"/>
 * 
 * @author yongtree
 * @date 2011-6-29 上午11:17:08
 * @version 1.0
 */
public class StrutsUploads {

	private File[] upload;

	private String[] uploadContentType;

	private String[] uploadFileName;

	private String[] displayFileName;

	private String[] description;

	public List<StrutsUpload> getStrutsUplaodList() {
		List<StrutsUpload> list = new ArrayList<StrutsUpload>();
		if (upload.length > 0) {
			for (int i = 0; i < upload.length; i++) {
				StrutsUpload su = new StrutsUpload();
				su.setUpload(upload[i]);
				su.setDescription(description[i]);
				su.setDisplayFileName(displayFileName[i]);
				su.setUploadContentType(uploadContentType[i]);
				su.setUploadFileName(uploadFileName[i]);
				list.add(su);
			}
		}
		return list;
	}

	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getDisplayFileName() {
		return displayFileName;
	}

	public void setDisplayFileName(String[] displayFileName) {
		this.displayFileName = displayFileName;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

}
