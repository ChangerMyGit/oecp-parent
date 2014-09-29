/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.framework.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import oecp.framework.util.ResourceUtils;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 文件操作工具类
 * 
 * @author chengzl
 * @date 2011-11-24 上午08:40:51
 * @version 1.0
 * 部分功能以迁移到module oecp-util中
 */
public class FileUtil {

	/**
	 * 上传文件
	 * 
	 * @author yongtree
	 * @date 2011-12-28上午10:40:29
	 * @param file
	 * @param filePath
	 * @throws Exception
	 */
	public static void uploadFile(File file, String filePath) throws Exception {
		FileInputStream fin = new FileInputStream(file);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] temp = new byte[fin.available()];
		while ((fin.read(temp, 0, temp.length)) != -1) {
			os.write(temp, 0, temp.length);
		}
		FileUtil.uploadFile(temp, filePath);
		os.close();
		fin.close();
	}

	/**
	 * 写文件：传入文件流和文件路径
	 * 
	 * @author chengzl
	 * @date 2011-11-24上午08:41:16
	 * @param filePath
	 */
	public static void uploadFile(InputStream is, String filePath)
			throws IOException {

		BufferedInputStream bis = null;
		byte[] buf = new byte[1000];
		int size = 0;
		bis = new BufferedInputStream(is);
		String foder = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1,
				filePath.length());
		File file = new File(foder);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream fosfile = new FileOutputStream(new File(foder,
				fileName));
		while ((size = bis.read(buf)) != -1)
			fosfile.write(buf, 0, size);
		fosfile.flush();
		is.close();
		bis.close();
		fosfile.close();

	}

	/**
	 * 写文件：传入文件流和文件路径
	 * 
	 * @author chengzl
	 * @date 2011-11-24上午08:41:16
	 * @param filePath
	 */
	public static void uploadFile(byte[] bytes, String filePath) {
		try {
			String foder = filePath.substring(0, filePath.lastIndexOf("/") + 1);
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1,
					filePath.length());
			File file = new File(foder);
			if (!file.exists()) {
				file.mkdirs();
			}
			FileOutputStream fosfile = new FileOutputStream(new File(foder,
					fileName));
			fosfile.write(bytes);
			fosfile.flush();
			fosfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 写xml文件
	 * 
	 * @author chengzl
	 * @date 2011-11-24下午02:06:53
	 * @param document
	 * @param filePath
	 * @param encode
	 * @throws IOException
	 */
	public static void writeXML(Document document, String filePath,
			String encode) throws IOException {
		OutputFormat format = null;
		XMLWriter writer = null;
		format = OutputFormat.createPrettyPrint();
		format.setEncoding(encode);
		FileOutputStream fos = new FileOutputStream(filePath);
		writer = new XMLWriter(fos, format);
		writer.write(document);
		fos.close();
		writer.close();
	}

	/**
	 * 通过文件名得到后缀
	 * 
	 * @author yongtree
	 * @date 2011-12-28上午10:36:22
	 * @param filename
	 * @return
	 */
	public static String getExtention(String filename) {
		String ext = "";
		int index = filename.lastIndexOf(".");
		if (index > 0) {
			ext = filename.substring(index + 1);
		}
		return ext;
	}

	public static byte[] file2byte(File file) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			while ((fis.read(b, 0, b.length)) != -1) {
				baos.write(b, 0, b.length);
			}
			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 把字节数组保存为一个文件
	 * 
	 * @author yongtree
	 * @date 2011-12-31下午03:01:03
	 * @param b
	 * @param outputFile
	 * @return
	 */
	public static File bytes2file(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 从字节数组获取对象
	 * 
	 * @author yongtree
	 * @date 2011-12-31下午03:01:41
	 * @param objBytes
	 * @return
	 * @throws Exception
	 */
	public static Object bytes2obj(byte[] objBytes) throws Exception {
		if (objBytes == null || objBytes.length == 0) {
			return null;
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		return oi.readObject();
	}

	/**
	 * 从对象获取一个字节数组
	 * 
	 * @author yongtree
	 * @date 2011-12-31下午03:02:26
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static byte[] object2bytes(Serializable obj) throws Exception {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		return bo.toByteArray();
	}

	public static String getFileFullPath(String filepath) {
		if (StringUtils.isBlank(filepath))
			filepath = ResourceUtils.getString("fs", "default.pic.path");
		if (filepath.contains("http://"))
			return filepath;
		String domain = ResourceUtils.getString("fs", "fs.domain");
		if (StringUtils.isNotBlank(domain)) {
			if (domain.charAt(domain.length() - 1) == '/')
				domain = domain.substring(0, domain.length() - 1);
			return domain + filepath;
		} else {
			return filepath;
		}
	}

}
