/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
 */

package oecp.framework.fs.db.xservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import oecp.framework.dao.DAO;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import oecp.framework.fs.FileUtil;
import oecp.framework.fs.db.xeo.UploadFile;

/**
 * 
 * @author yongtree
 * @date 2011-12-27 上午09:51:20
 * @version 1.0
 * 
 */
public class UploadFileServiceImpl extends BaseServiceImpl<UploadFile>
		implements UploadFileService {

	private DAO dao;

	@Override
	protected DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	@Transactional
	public UploadFile find_full(Serializable id) throws BizException {
		UploadFile uf = find(id);
		uf.getContent();
		return uf;
	}

	// TODO 根据md5判断是否存在，如果存在就不添加记录了
	@Transactional
	public UploadFile save(File file, String contentType,String filename) throws BizException {
		try {
			UploadFile uf = new UploadFile();
			uf.setId(UUID.randomUUID().toString());
			uf.setContentType(contentType);
			FileInputStream fin = new FileInputStream(file);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] temp = new byte[fin.available()];
			int i = 0;
			MessageDigest md = MessageDigest.getInstance("MD5");
			while ((i = fin.read(temp, 0, temp.length)) != -1) {
				os.write(temp, 0, temp.length);
				md.update(temp, 0, i);
			}
			String md5 = bytesToString(md.digest());
			os.close();
			fin.close();
			uf.setContent(temp);
			uf.setOriginalName(filename);
			uf.setUploadTime(new Date());
			uf.setExtention(FileUtil.getExtention(filename));
			uf.setMd5(md5);
			uf.setSize(file.length());
			uf.setLastUploadTime(new Date());
			uf.setFilename(uf.getId() + "." + uf.getExtention());
			this.save(uf);
			return uf;
		} catch (FileNotFoundException e) {
			throw new BizException(e);
		} catch (IOException e) {
			throw new BizException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new BizException(e);
		}

	}

	@Override
	public UploadFile save(File file) throws BizException {
		return save(file, null,null);
	}

	private String bytesToString(byte[] data) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char[] temp = new char[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
			temp[i * 2 + 1] = hexDigits[b & 0x0f];
		}
		return new String(temp);

	}

	@Override
	public byte[] findFileBytes(String id) {
		return (byte[]) this.getDao().getHibernateSession().createQuery(
				"SELECT o.content FROM UploadFile o WHERE o.id=:id")
				.setParameter("id", id).uniqueResult();
	}

	public File findFile(String id) throws BizException {
		UploadFile uf = this.find(id);
		if (uf != null) {
			File file = FileUtil.bytes2file(uf.getContent(), StringUtils
					.isNotBlank(uf.getOriginalName()) ? uf.getOriginalName()
					: uf.getFilename());
			return file;
		} else {
			return null;
		}
	}

}
