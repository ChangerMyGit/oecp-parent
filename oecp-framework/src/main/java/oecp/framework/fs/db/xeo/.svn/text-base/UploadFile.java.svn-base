/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
 */

package oecp.framework.fs.db.xeo;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import oecp.framework.entity.base.ManualPKEO;

/**
 * 上传文件
 * 
 * @author yongtree
 * @date 2011-12-26 下午04:24:06
 * @version 1.0
 * 
 */
@Entity
@Table(name = "t_x_uploadfile")
public class UploadFile extends ManualPKEO {

	private static final long serialVersionUID = 5515335356624709164L;

	private byte[] content;

	private String originalName;

	private String filename;

	private String contentType;

	private Date uploadTime;

	private String md5;

	private Long size;

	private String extention;

	private Date lastUploadTime;// 最后上传时间--为什么会有最后上传时间，因为根据MD5值进行验证，可能有些文件重复上传，出现重复上传，则不新增记录，而是更新该记录

	private Integer uploadNum;// 上传次数--在删除时，该数字-1，如果该数字==0，则表明已经没有数据引用，则物理删除。

	@Column(name="last_upload_time")
	public Date getLastUploadTime() {
		return lastUploadTime;
	}

	public void setLastUploadTime(Date lastUploadTime) {
		this.lastUploadTime = lastUploadTime;
	}
	@Column(name="upload_num")
	public Integer getUploadNum() {
		if (uploadNum == null)
			return 1;
		return uploadNum;
	}

	public void setUploadNum(Integer uploadNum) {
		this.uploadNum = uploadNum;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Column(name = "original_name", length = 100)
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	@Column(name = "file_name", length = 100)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "content_type", length = 100)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "upload_time")
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Column(name = "file_size")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

}
