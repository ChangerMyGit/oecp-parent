/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
 */

package oecp.framework.fs;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 读取上传文件的servlet
 * @author yongtree
 * @date 2011-10-27 下午03:14:14
 * @version 1.0
 * 
 */
public class UploadedFileServlet extends HttpServlet {

	private static final long serialVersionUID = -4944148746840614469L;
	
	private final static Logger logger = Logger	.getLogger(UploadedFileServlet.class);


	private void processRequest(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException, IOException {
		String url = request.getRequestURI();
		String contextPath = request.getContextPath();
		StorageService storageService = (StorageService) WebApplicationContextUtils
				.getRequiredWebApplicationContext((request)
				.getSession().getServletContext())
				.getBean(getInitParameter("storageServiceName"));
		Map<String, Object> f = null;
		String path=null;
		if(!"/".equals(contextPath)){
			path = url.substring(contextPath.length());
		}else{
			path=url;
		}
		try {
			f = storageService.find2client(path);
		} catch (Exception e1) {
			return;
		}
		if (f != null) {
			String ext = null;
			String filename = (String) f.get("filename");
			ext = filename.substring(filename.lastIndexOf(".") + 1);
			String contentType=(String)f.get("contentType");
			response.setContentType(contentType);
			if (needCache(ext)) {
				String modifiedSince = request.getHeader("If-Modified-Since");
				DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.CHINA);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date uploadDate = (Date) f.get("uploadTime");
				String lastModified = df.format(uploadDate);
				if (modifiedSince != null) {
					Date modifiedDate = null;
					Date sinceDate = null;
					try {
						modifiedDate = df.parse(lastModified);
						sinceDate = df.parse(modifiedSince);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					if (modifiedDate.compareTo(sinceDate) <= 0) {
						response.setStatus(304); // Not Modified
						return;
					}
				}
//数据过大 nginx调用时报错
//				long maxAge = 10L * 365L * 24L * 60L * 60L; // ten years, in seconds
//				response.setHeader("Cache-Control", "max-age=" + maxAge);
//				response.setHeader("Last-Modified", lastModified);
//				response.setDateHeader("Expires", uploadDate.getTime() + maxAge	* 1000L);
			} else {
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setHeader( "Content-disposition ", "attachment;filename="+filename);
				response.setHeader( "Content-Length",  f.get("size")+"");
				response.setHeader("Accept-Ranges", "bytes");
			}
			response.getOutputStream().write((byte[]) f.get("content"));
		}
	}

	private boolean needCache(String ext) {
		if (ext == null) {
			return false;
		}
		boolean need = false;
		String[] arr = { "jpg", "jpeg", "png", "gif", "bmp", "html", "htm","swf", "mp3", "mp4", "pdf" };
		for (String s : arr) {
			if (s.equals(ext)) {
				need = true;
				break;
			}
		}
		return need;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
