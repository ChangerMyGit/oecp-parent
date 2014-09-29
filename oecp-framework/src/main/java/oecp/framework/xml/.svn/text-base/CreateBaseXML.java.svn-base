package oecp.framework.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;



/**
 * 
 * 创建XML基础类
 * @author hailang
 * @date 2011-7-15 11:06:59
 * @version 1.0
 */
public class CreateBaseXML {
	private Log log=LogFactory.getLog(CreateBaseXML.class);
	public ICreateXML createXML;//创建XML文件接口 

	/**
	 * 根据传递的参数，获取名称为fileName的绝对路径
	* @author hailang
	* @date 2011-7-25 下午03:47:49
	* @Title: getAbsolutePath
	* @Description: 
	* @param filePath 文件所在路径 默认为classes文件夹目录下为classes文件夹目录下
	* @param fileName 文件名称    默认为当前时间 如2011-7-15.xml
	* @param fileSuffix 文件明后缀 默认为.xml
	* @return
	 */
	public String getAbsolutePath(String filePath,String fileName,String fileSuffix){
		String absolutePath="";
		if(StringUtils.isEmpty(filePath)){
			filePath= this.getClass().getResource("/").toString().substring(6);
		}else{
			File file=new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
		}
		if(StringUtils.isEmpty(fileName)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			String dateStr=sdf.format(new Date());
			if(StringUtils.isEmpty(fileSuffix)){
				fileName=dateStr+".xml";
			}else{
				fileName=dateStr+"."+fileSuffix;
			}
		}else{
			if(StringUtils.isEmpty(fileSuffix)){
				fileName=fileName+".xml";
			}else{
				fileName=fileName+"."+fileSuffix;
			}
		}
		absolutePath=filePath+"/"+fileName;
		return absolutePath;
	}
	
	/**
	 * 写入XML文件，到本地磁盘
	 * @author hailang
	 * @date 2011-7-15 10:23:52
	 * @param document 文档对象
	 * @param formatEncoding 编码方式 
	 * @param filePath 文件所在路径 默认为classes文件夹目录下为classes文件夹目录下
	 * @param fileName 文件名称    默认为当前时间 如2011-7-15.xml
	 * @param fileSuffix 文件明后缀 默认为.xml
	 */
	public void writeXML(Document document,String formatEncoding,String filePath,String fileName,String fileSuffix){
		String path=this.getAbsolutePath(filePath, fileName, fileSuffix);
		OutputFormat format=null;
		XMLWriter writer=null;
		if(StringUtils.isEmpty(formatEncoding)){
			formatEncoding="UTF-8";
		}else{
			if(StringUtils.equalsIgnoreCase(formatEncoding,"utf-8") || StringUtils.equalsIgnoreCase(formatEncoding,"gbk")){
				formatEncoding="UTF-8";
			}
		}
		format = OutputFormat.createPrettyPrint();
		format.setEncoding(formatEncoding);
		try {
			FileOutputStream fos= new FileOutputStream(path);
			writer = new XMLWriter(fos, format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			log.error(e.getMessage()+"创建名为："+path+"的XML时出错");
		}
	}

	
	/**
	 * 写入XML文件，到本地磁盘
	 * @author hailang
	 * @date 2011-7-15 10:23:52
	 * @param document 文档对象
	 * @param formatEncoding 编码方式 
	 * @param filePath 文件所在路径 默认为classes文件夹目录下为classes文件夹目录下
	 * @param fileName 文件名称    默认为当前时间 如2011-7-15.xml
	 * @param fileSuffix 文件明后缀 默认为.xml
	 */
	public void writeSmil(Document document,String formatEncoding,String filePath,String fileName,String fileSuffix){
		String path=this.getAbsolutePath(filePath, fileName, fileSuffix);
		OutputFormat format=null;
		XMLWriter writer=null;
		if(StringUtils.isEmpty(formatEncoding)){
			formatEncoding="UTF-8";
		}else{
			if(StringUtils.equalsIgnoreCase(formatEncoding,"utf-8") || StringUtils.equalsIgnoreCase(formatEncoding,"gbk")){
				formatEncoding="UTF-8";
			}
		}
		format = OutputFormat.createPrettyPrint();
		format.setEncoding(formatEncoding);
		try {
			FileOutputStream fos= new FileOutputStream(path);
			//writer = new XMLWriter(fos);
			String strXml=document.asXML();
			strXml=strXml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>\n", "");
			System.out.println(strXml);
			fos.write(strXml.getBytes());
			fos.close();
		} catch (IOException e) {
			log.error(e.getMessage()+"创建名为："+path+"的XML时出错");
		}
	}
	/**
	 * 创建XML文件
	 * @author hailang
	 * @date 2011-7-15 11:01:41
	 */
	public Document  createXML(){
		return createXML.createXML();
	}
	
	

	public void setCreateXML(ICreateXML createXML) {
		this.createXML = createXML;
	}
	
	public static void main(String[] args) {
		System.out.println(CreateBaseXML.class.getResource("/").toString().substring(6));
	}
	
}
	
	