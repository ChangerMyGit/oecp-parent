/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.framework.fs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oecp.framework.exception.BizException;
import oecp.framework.fs.db.xeo.UploadFile;
import oecp.framework.fs.db.xservice.UploadFileService;
import oecp.framework.fs.gridfs.GridxFS;
import oecp.framework.util.Path;
import oecp.framework.util.ResourceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danga.MemCached.MemCachedClient;
import com.mongodb.gridfs.GridFSDBFile;


/**
 * 文件存储服务
 * 
 * @author yongtree
 * @date 2011-7-8 上午08:57:42
 * @version 1.0
 * 
 */
public class StorageService {

	private static Log log = LogFactory.getLog(StorageService.class);
	private FsInitParam fsInitParam;
	private UploadFileService uploadFileService;
	//	缓存服务
	private MemCachedClient memcachedClient;
	private GridxFS gridxFS;
	ImageScale imageScale;//缩图工具
	private static String NotFoundImage = "404 Image Not Found";
	public FileExtInfo findOne(String path) {

		return null;
	}
	private String waterImgPath;//水印图片路径
	
	private String waterName;//水印图片名称
	
	private String waterPosition;//水印图片位置
	

	public File findFile(String path) throws BizException {
		FSType fsType = getFsType(path);
		if (FSType.upload.equals(fsType)) {
			return new File(path);
		} else if (FSType.dbfile.equals(fsType)) {
			File file = new File(path);
			if (!file.exists()) {
				// 数据库查询
				return this.uploadFileService.findFile(getFileId(path));
			}
			return file;
		}else if(FSType.gridfs.equals(fsType)){
			GridFSDBFile gfs = gridxFS.findOne(getFileId(path));
			if(gfs != null){
				File f = new File(getLocalFilePath(path));
				try {
					gfs.writeTo(f);
				} catch (IOException e) {
					throw new BizException(e.getMessage());
				}
				return f;
			}
		}
		return null;
	}

	public byte[] findBytes(String path) throws BizException {
		FSType fsType = getFsType(path);
		if (FSType.upload.equals(fsType)) {
			return getLocalFileBytes(path);
		} else if (FSType.dbfile.equals(fsType)) {
			byte[] b = getLocalFileBytes(path);
			if (b == null) {
				// 数据库查询
				return this.uploadFileService.findFileBytes(getFileId(path));
			}
			return b;

		}
		return null;
	}

	/**
	 * 得到文件并附带下载时用到的几个属性
	 * 
	 * @author yongtree
	 * @date 2011-12-29上午08:56:47
	 * @param path
	 *            文件相对路径
	 * @param localCache
	 *            优先查询本地缓存
	 * @return {
	 *         'content':byte[],'contentType':String,'filename':String,'uploadTime':
	 *         D a t e }
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> find2client(String path) throws Exception {
		FSType fsType = getFsType(path);
		Map<String, Object> b = null;
		String contentType = getContentType(path.substring(path.lastIndexOf(".") + 1));//文件类型
		boolean mustCached = mastToCached(contentType,fsInitParam.getCacheTypes());//是需要缓存的类型
		try{
			if(path.toLowerCase().matches("(.*_(\\d+)x(\\d+)|.*_(\\d+)x(\\d+)c).(jpg|gif|jpeg|png|bmp)")){//判断后缀为缩略图格式
				b = toScaleImage(fsType,path,contentType,mustCached);
			}else if(path.toLowerCase().matches("(.*_w).(jpg|gif|jpeg|png|bmp)")){//判断后缀为w的为水印图片
				b=toMarkImage(fsType,path,contentType,mustCached);//获取原图片
			}else{
				b = searchImage4Dbtype(fsType,path,contentType,mustCached);
			}
		}catch(Exception e){
			b = get404Image(contentType);
			throw e;
		}
		return b;
	}
	/**
	 * 获取404图片
	 * @return
	 */
	private Map<String, Object> get404Image(String contentType) {
		Map<String,Object> b = null;
		Object obj = memcachedClient.get(NotFoundImage);
		if(obj != null){//命中文件，类型强转
			b = (Map<String, Object>)obj;
		}else{
			b = new HashMap<String, Object>();
			b.put("content", FileUtil.file2byte(new File("")));//二进制文件流
			b.put("contentType",contentType);
			b.put("filename", "404");
			memcachedClient.add(NotFoundImage, b);
		}
		return null;
	}

	/**
	 * 根据存储方式获取图片
	 * @author wangliang
	 * @date 2012-6-29
	 * @param fsType
	 * @param path
	 * @throws BizException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> searchImage4Dbtype(FSType fsType, String path,String contentType,boolean mustCached) throws BizException, IOException {
		Map<String,Object> b = null;
		if (FSType.upload.equals(fsType)) {//判断为本地文件
			b = getLocalFile2down(path);
		} else if (FSType.dbfile.equals(fsType)) {//判断为数据库文件
			boolean misses=true;//查询缓存文件标志
			if(fsInitParam.getUseMemcached() && mustCached){//使用缓存服务器标记
				Object obj = memcachedClient.get(path);
				if(obj != null){//命中文件，类型强转
					b=(Map<String, Object>)obj;
					misses = false;
				}
			}else{
				if (fsInitParam.getUseLocalCache()) {//使用本地缓存标记
					b = getLocalFile2down(path);
				}
			}
			if (b == null) {
				// 数据库查询
				UploadFile uf = this.uploadFileService.find_full(getFileId(path));
				if (uf == null)
					return null;
				b = new HashMap<String, Object>();
				b.put("content", uf.getContent());
				b.put("contentType",StringUtils.isNotBlank(uf.getContentType()) 	? uf.getContentType() : contentType);
				b.put("filename", StringUtils.isNotBlank(uf.getOriginalName()) ? uf.getOriginalName() : uf.getFilename());
				b.put("uploadTime", uf.getUploadTime());
				b.put("size", uf.getSize());
				if(fsInitParam.getUseMemcached() && mustCached && misses) {//判断要用缓存,并且是要缓存的类型，并且查询文件未在缓存服务器中
					memcachedClient.add(path, b);
				}else{
					if (fsInitParam.getUseLocalCache()) {
						FileUtil.uploadFile(uf.getContent(), getLocalFilePath(path));
					}
				}
			}
		}else if(FSType.gridfs.equals(fsType)){
			b=findFile4GridxFs(path);
		}
		return b;
	}
	/**
	 * 从GridxFx 获取文件
	 * @author wangliang
	 * @date 2012-7-2
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public Map<String,Object> findFile4GridxFs(String path) throws IOException{
		Map<String,Object> b = null;
		GridFSDBFile gf = gridxFS.findOne(getFileId(path));
		if(gf !=null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			gf.writeTo(baos);
			b = new HashMap<String, Object>();
			b.put("content", baos.toByteArray());
			b.put("contentType",StringUtils.isNotBlank(gf.getContentType()) 	? gf.getContentType() : getContentType(path.substring(path.lastIndexOf(".") + 1)));
			b.put("filename", gf.get("originalName"));
			b.put("uploadTime", gf.getUploadDate());
		}
		return b;
	}
	/**
	 * 获取压缩图片
	 * 先从 缓存->gridxFs 依次查询获取缩略图。
	 * 如果没有获取到图片则去掉分辨率后缀，从原始库中获取原图存入本地磁盘，
	 * 根据分辨率生成缩略图后存入gridxFx，根据配置参数判断是否再存入mem缓存中
	 * @author wangliang
	 * @date 2012-6-28
	 * @param contentType
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> toScaleImage(FSType fsType,String path,String contentType,boolean mustCached) throws Exception {
		String filename = getFileName(path);//文件名
		String dpi = filename.substring(filename.lastIndexOf("_")+1,filename.lastIndexOf("."));//分辨率
		if(!checkDpi(dpi,fsInitParam.getDpis())) {//验证分辨率合法
			return null;
		}
		Map<String,Object> b = null;
		//从缓存内获取缩略图
		if(fsInitParam.getUseMemcached() && mustCached){//判断是需要缓存的分辨率
			Object obj = memcachedClient.get(path);
			if(obj != null){//命中文件，类型强转
				return (Map<String, Object>)obj;
			}
		}
		//从gridxFx磁盘获取缩略图
		if(b==null){
			b=findFile4GridxFs(path);
		}
		//获取原图生成缩略图
		if(b == null){
			String originalPath = path.substring(0,path.lastIndexOf("_"))+path.substring(path.lastIndexOf("."));//获取原图路径
			Map<String,Object> sb = searchImage4Dbtype(fsType,originalPath,contentType,mustCached);//获取原图
			if(sb != null){
				String allOriginalPath = getLocalFilePath(originalPath);
				FileUtil.uploadFile((byte[])sb.get("content"), allOriginalPath);//原图缓存到本地磁盘
				File originalFile = new File(allOriginalPath);
				File newScaleImage =  new File(getLocalFilePath(path));
				//加工缩略图
				String[] xy = dpi.toLowerCase().split("x");
				if(StringUtils.isNumeric(xy[1])){
					imageScale.resizeFix(originalFile, newScaleImage, Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));//使用分辨率压缩图片
				}else{
					xy[1]=xy[1].substring(0,xy[1].lastIndexOf("c"));
					imageScale.resizeCutFix(originalFile, newScaleImage, Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));//使用先压缩图片后剪裁图片
				}
				//缩略图存入本地磁盘
				Map<String,Object> fparams = new HashMap();
				fparams.put("originalName", filename);
				String fname = getFileId(path);
				gridxFS.save(newScaleImage,fname, null, fparams);//缩略图文件放入gridxFx
				b=new HashMap();
				b.put("content", FileUtil.file2byte(newScaleImage));
				b.put("contentType",contentType);
				b.put("filename", fname);
				b.put("uploadTime", new Date());
				if(newScaleImage.exists()) newScaleImage.delete();//删除临时缩略图
				if(originalFile.exists()) originalFile.delete();//删掉临时原图
				if(fsInitParam.getUseMemcached() && mustCached) {//判断要用缓存,并且是要缓存的类型
					memcachedClient.add(path, b);
				}
			}
		}
		return b;
	}

	
	/**
	 * 获取水印图片
	 * 先从 缓存->gridxFs 依次查询获取缩略图。
	 * 如果没有获取到图片则去掉分辨率后缀，从原始库中获取原图存入本地磁盘，
	 * 根据分辨率生成缩略图后存入gridxFx，根据配置参数判断是否再存入mem缓存中
	 * @author wangliang
	 * @date 2012-6-28
	 * @param contentType
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> toMarkImage(FSType fsType,String path,String contentType,boolean mustCached) throws Exception {
		String filename = getFileName(path);//文件名
		String dpi = filename.substring(filename.lastIndexOf("_")+1,filename.lastIndexOf("."));//分辨率
		
		Map<String,Object> b = null;
		//从缓存内获取缩略图
		if(fsInitParam.getUseMemcached() && mustCached){//判断是需要缓存的分辨率
			Object obj = memcachedClient.get(path);
			if(obj != null){//命中文件，类型强转
				return (Map<String, Object>)obj;
			}
		}
		//从gridxFx磁盘获取缩略图
		if(b==null){
			b=findFile4GridxFs(path);
		}
		//获取原图生成水印图片
		if(b == null){
			String originalPath = path.substring(0,path.lastIndexOf("_"))+path.substring(path.lastIndexOf("."));//获取原图路径
			Map<String,Object> sb = searchImage4Dbtype(fsType,originalPath,contentType,mustCached);//获取原图
			if(sb != null){
				String allOriginalPath = getLocalFilePath(originalPath);
				FileUtil.uploadFile((byte[])sb.get("content"), allOriginalPath);//原图缓存到本地磁盘
				File originalFile = new File(allOriginalPath);
				File newScaleImage =  new File(getLocalFilePath(path));
				
				imageScale.imageMark(originalFile, newScaleImage, getWaterPosition(), new File(getWaterImgPath()+"/"+getWaterName()), 1f);
				
				
				//缩略图存入本地磁盘
				Map<String,Object> fparams = new HashMap();
				fparams.put("originalName", filename);
				String fname = getFileId(path);
				gridxFS.save(newScaleImage,fname, null, fparams);//缩略图文件放入gridxFx
				b=new HashMap();
				b.put("content", FileUtil.file2byte(newScaleImage));
				b.put("contentType",contentType);
				b.put("filename", fname);
				b.put("uploadTime", new Date());
				if(newScaleImage.exists()) newScaleImage.delete();//删除临时缩略图
				if(originalFile.exists()) originalFile.delete();//删掉临时原图
				if(fsInitParam.getUseMemcached() && mustCached) {//判断要用缓存,并且是要缓存的类型
					memcachedClient.add(path, b);
				}
			}
		}
		return b;
	}
	
	
	
	
	
	
	
	
	/**
	 * 判断文件类型需要缓存
	 * @author wangliang
	 * @date 2012-6-27
	 * @param cacheTypes
	 * @return
	 */
	public boolean mastToCached(String contentType,List<String> cacheTypes) {
		if(cacheTypes == null || cacheTypes.isEmpty())	return false;
		if(StringUtils.isEmpty(contentType))	return false;
		return cacheTypes.contains(contentType.replace("image/", ""));
	}

	public boolean checkDpi(String dpi,List<String>dpis){
		if(dpis == null || dpis.isEmpty())	return false;
		if(StringUtils.isEmpty(dpi)){	
			return false;
		}
		if(StringUtils.contains(dpi.toLowerCase(), "c")){//如果包含字符"c",说明是要对图片进行剪裁处理。
			dpi=dpi.substring(0, dpi.lastIndexOf("c"));
		}
		
		return dpis.contains(dpi);
	}
	
	private byte[] getLocalFileBytes(String path) {
		File file = getLocalFile(path);
		if (file.exists()) {
			return FileUtil.file2byte(file);
		}
		return null;
	}

	private File getLocalFile(String path) {
		File file = new File(getLocalFilePath(path));
		return file;
	}
	
	private String getLocalFilePath(String path){
		String root = ResourceUtils.getString("fs", "upload.root");
		if (StringUtils.isBlank(root)) {
			root = Path.getProjectPath();
		}
		return root+path;
	}

	private Map<String, Object> getLocalFile2down(String path) {
		File content = getLocalFile(path);
		if (content.exists()) {
			String filename = path.substring(path.lastIndexOf("/"));
			String contentType = getContentType(path.substring(path.lastIndexOf(".") + 1));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("content", FileUtil.file2byte(content));
			map.put("filename", filename);
			map.put("contentType", contentType);
			map.put("uploadTime", new Date(content.lastModified()));
			map.put("size", FileUtil.file2byte(content).length);
			return map;
		}
		return null;
	}

	private String getContentType(String ext) {
		String type = null;
		if (ext == null) {
			type = "application/octet-stream";
		} else if (ext.equalsIgnoreCase("gif")
				|| ext.equalsIgnoreCase("jpg") 
				|| ext.equalsIgnoreCase("jpeg")
				|| ext.equalsIgnoreCase("png") 
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
	public static byte[] file2byte(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] b = new byte[is.available()];
			while ((is.read(b, 0, b.length)) != -1) {
				baos.write(b, 0, b.length);
			}
			return b;
		} 
		 catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	private FSType getFsType(String path) {
		return FSType.valueOf(path.substring(1, path.indexOf("/", 1)));
	}
	
	private String getFileName(String path){
		return path.substring(path.lastIndexOf("/") + 1);
	}
	
	private String getFileId(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}
	
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}

	public void setMemcachedClient(MemCachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setGridxFS(GridxFS gridxFS) {
		this.gridxFS = gridxFS;
	}
	
	public void setFsInitParam(FsInitParam fsInitParam) {
		this.fsInitParam = fsInitParam;
	}

	public void setImageScale(ImageScale imageScale) {
		this.imageScale = imageScale;
	}

	public String getWaterImgPath() {
		return waterImgPath;
	}

	public void setWaterImgPath(String waterImgPath) {
		this.waterImgPath = waterImgPath;
	}

	public String getWaterName() {
		return waterName;
	}

	public void setWaterName(String waterName) {
		this.waterName = waterName;
	}

	public String getWaterPosition() {
		return waterPosition;
	}

	public void setWaterPosition(String waterPosition) {
		this.waterPosition = waterPosition;
	}

	public static void main(String[] args) throws BizException, IOException {
		StorageService s=new StorageService();
		String path ="http://192.168.0.29:8099/fs/gridfs/tmp/50dd3279-fcab-4f77-8b3f-0d6912491e9a_w.jpg";
		
		String originalPath = path.substring(0,path.lastIndexOf("_"))+path.substring(path.lastIndexOf("."));//获取原图路径
		
		String allOriginalPath =s.getLocalFilePath(originalPath);
		Map<String,Object> sb = s.searchImage4Dbtype(FSType.gridfs,originalPath,s.getContentType("jpg"),true);//获取原图
		FileUtil.uploadFile((byte[])sb.get("content"), allOriginalPath);//原图缓存到本地磁盘
		File originalFile = new File(allOriginalPath);
		File newScaleImage =  new File(s.getLocalFilePath(path));
		
		AverageImageScale.imageMark(originalFile, newScaleImage, "TOP_RIGHT", new File(newScaleImage, "F:/eclipse3.6/apache-tomcat-6.0.33/apache-tomcat-6.0.33/webapps/fs/upload"+"/"+"water.png"), 1f);
//		String sourcePath = path.substring(0,path.lastIndexOf("_"))+path.substring(path.lastIndexOf(".") + 1);
		System.out.println(originalPath);
//		System.out.println(StorageService.class.getClass().getResource("/").getPath());
	}
}
