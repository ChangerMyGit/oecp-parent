/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.framework.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 处理zip文件的工具类
 * 
 * @author yongtree
 * @date 2011-6-23 上午11:15:55
 * @version 1.0
 */
public class ZipUtil {

	// 调用该方法生成zip文件
	public static void saveZip(String fileName, Map<String, InputStream> dataMap) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			JarOutputStream jos = new JarOutputStream(fos);
			byte buf[] = new byte[256];
			if (dataMap != null) {
				Set<Entry<String, InputStream>> entrySet = dataMap.entrySet();
				int len = -1;
				for (Entry<String, InputStream> entry : entrySet) {
					String name = entry.getKey();
					InputStream inputStream = entry.getValue();
					if (name != null && inputStream != null) {
						BufferedInputStream bis = new BufferedInputStream(
								inputStream);
						JarEntry jarEntry = new JarEntry(name);
						jos.putNextEntry(jarEntry);

						while ((len = bis.read(buf)) >= 0) {
							jos.write(buf, 0, len);
						}

						bis.close();
						jos.closeEntry();
					}
				}
			}
			jos.flush();
			jos.close();
			fos.flush();
			fos.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void zip(String inputFileName, String outputFileName)
			throws Exception {
		// String zipFileName = "d:\\test1\\test.zip";// 打包后文件名字
		zip(outputFileName, new File(inputFileName));
	}

	public static void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		zip(out, inputFile, "");
		System.out.println("zip done");
		out.close();
	}

	public static void zip(ZipOutputStream out, File f, String base)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			// out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	public static void main(String[] args) {
		try {
			ZipUtil.zip("d:/9b817ce8395u2ef2", "d:/9b817ce8395u2ef2.zip");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}