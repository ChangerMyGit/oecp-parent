/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.framework.fs;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import oecp.framework.fs.db.xeo.UploadFile;
import oecp.framework.fs.db.xservice.UploadFileService;
import oecp.framework.fs.gridfs.GridxFS;
import oecp.framework.util.DateUtil;
import oecp.framework.util.Path;
import oecp.framework.util.ResourceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.document.mongodb.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * 上传文件bean
 * 
 * @author yongtree
 * @date 2011-6-29 上午11:17:08
 * @version 1.0
 */
public class Uploader {

	private final static Logger logger = Logger.getLogger(Uploader.class);

	protected String originalName;

	protected String folder;

	protected Map<String, Object> params;

	protected FSType fsType;

	protected String path;

	private UploadFileService uploadFileService;

	protected String contentType;

	protected File file;

	protected HttpServletRequest request;

	private GridxFS gridxFS;

	public void setStrutsUpload(StrutsUpload strutsUpload) {
		this.file = strutsUpload.getUpload();
		this.contentType = strutsUpload.getUploadContentType();
		this.originalName = strutsUpload.getUploadFileName();
	}

	public void setFile(File file) {
		try {
			this.file = file;
			this.originalName = file.getName();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 在多数据源或管理多个应用的项目中，一个数据源建议创建一个文件夹，以方便按照文件夹去查找相关数据源<br>
	 * 在单应用和非集群环境下，可以随意的设置
	 * 
	 * @author yongtree
	 * @date 2011-12-28上午11:10:25
	 * @param folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	public void setAttribute(String key, Object value) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put(key, value);
	}

	public void save() throws Exception {
		saveInputStream(this.file);
	}

	/**
	 * 根据流上传文件到文件服务器
	 * 
	 * @author chengzl
	 * @date 2012-12-7上午10:25:29
	 * @param in
	 * @param fileName
	 * @return
	 */
	public String save(InputStream in, String originalName) {
		this.originalName = originalName;
		String ext = getExtention();
		Map<String, Object> params = new HashMap();
		params.put("originalName", originalName);
		params.put("contentType", getContentTypeByExt(ext));
		GridFSInputFile gfs = gridxFS.save(in, UUID.randomUUID().toString(),
				null, params);
		String newPath = "/" + FSType.gridfs.name() + "/" + getExtention()
				+ "/" + gfs.getFilename() + "." + getExtention();
		return newPath;
	}

	protected void saveInputStream(File file) throws Exception {
		if (FSType.dbfile.equals(fsType)) {
			UploadFile uf = this.uploadFileService.save(file, contentType,
					originalName);
			path = "/"
					+ FSType.dbfile.name()
					+ "/"
					+ (StringUtils.isNotEmpty(folder) ? folder : uf
							.getExtention()) + "/" + uf.getFilename();
		} else if (FSType.upload.equals(fsType)) {
			String root = ResourceUtils.getString("fs", "upload.root");
			if (StringUtils.isBlank(root)) {
				root = Path.getProjectPath();
			}
			path = "/"
					+ FSType.upload.name()
					+ "/"
					+ (StringUtils.isNotBlank(folder) ? folder : getExtention())
					+ "/" + DateUtil.getThisYearMonth() + "/"
					+ UUID.randomUUID().toString() + "." + getExtention();
			root = root + path;
			FileUtil.uploadFile(file, root);
		} else if (FSType.gridfs.equals(fsType)) {
			GridFSInputFile gfs = gridxFS.save(file);
			path = "/" + FSType.gridfs.name() + "/"
					+ FileUtil.getExtention(file.getName()) + "/"
					+ gfs.getFilename() + "." + getExtention();
		}
	}

	private String getContentTypeByExt(String ext) {
		String type = null;
		if (ext == null) {
			type = "application/octet-stream";
		} else if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("jpg")
				|| ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png")
				|| ext.equalsIgnoreCase("bmp")) {
			type = "image/" + ext;
		} else if (ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("htm")) {
			type = "text/html";
		} else if (ext.equalsIgnoreCase("swf")) {
			type = "application/x-shockwave-flash";
		} else if (ext.equalsIgnoreCase("mp3")) {
			type = "audio/x-mpeg";
		} else if (ext.equalsIgnoreCase("mp4")) {
			type = "video/mp4";
		} else if (ext.equalsIgnoreCase("pdf")) {
			type = "application/pdf";
		} else if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx")) {
			type = "application/vnd.ms-excel";
		} else if (ext.equalsIgnoreCase("doc") || ext.equalsIgnoreCase("docx")) {
			type = "application/msword";
		} else {
			type = "application/octet-stream";
		}
		return type;
	}

	/**
	 * @return the extension name, such as doc, png, jpeg
	 */
	protected String getExtention() {
		String ext = "";
		int index = originalName.lastIndexOf(".");
		if (index > 0) {
			ext = originalName.substring(index + 1);
		}
		return ext;
	}

	/**
	 * 设置文件系统类型，在集群的环境下最好采用db和gridfs的文件系统，不建议采用本地磁盘（upload）的方式。<br>
	 * 在单应用和非集群环境下，可以采用本地磁盘的方式
	 * 
	 * @author yongtree
	 * @date 2011-10-26下午04:30:51
	 * @param fsType
	 */
	public void setFsType(FSType fsType) {
		this.fsType = fsType;
	}

	protected FSType getFsType() {
		return fsType == null ? FSType.upload : fsType;
	}

	public String getPath() {
		return path;
	}

	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public GridxFS getGridxFS() {
		return gridxFS;
	}

	public void setGridxFS(GridxFS gridxFS) {
		this.gridxFS = gridxFS;
	}

	public static void main(String[] args) throws Exception {
		MongoTemplate mt = new MongoTemplate(new Mongo("192.168.3.246", 27017),
				"fs");
		GridxFS gfs = new GridxFS();
		gfs.setMongoTemplate(mt);
		gfs.init();
		Uploader ul = new Uploader();
		ul.setGridxFS(gfs);
		ul.setFile(new File("/home/wl/图片/test.png"));
		ul.setFsType(FSType.gridfs);
		ul.save();
		System.out.println(ul.getPath());
	}
}
