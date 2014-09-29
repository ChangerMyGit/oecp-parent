/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-11-4
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

package oecp.platform.file.eo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.file.enums.FileSource;
import oecp.framework.file.enums.FileType;
import oecp.framework.util.FileTypeUtils;


/**
 * 上传文件
 * 
 * @author slx
 * @date 2009-11-4 上午11:05:37
 * @version 1.0
 */
@Entity
@Table(name = "t_uploadfile")
public class UploadFile extends StringPKEO {

	private static final long serialVersionUID = 4340636251950698199L;

	private String name;

	private String sName;// 文件原始名称

	private Double size;// 文件的大小

	private String filepath;// 文件存放的相对路径

	private Date uploadTime;

	private int downNum;// 下载次数

	private String contentType;// 文件的格式,ex:application/word

	private String uploadUser;// 上传用户

	private FileSource source;// 文件来源

	@Enumerated(EnumType.STRING)
	public FileSource getSource() {
		return source;
	}

	public void setSource(FileSource source) {
		this.source = source;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Transient
	public FileType getFileType() {
		return FileTypeUtils.getFileType(sName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSName() {
		return sName;
	}

	public void setSName(String name) {
		sName = name;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public int getDownNum() {
		return downNum;
	}

	public void setDownNum(int downNum) {
		this.downNum = downNum;
	}

}
