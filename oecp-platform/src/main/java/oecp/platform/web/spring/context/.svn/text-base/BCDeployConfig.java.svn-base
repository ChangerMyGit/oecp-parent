/**
 * oecp-platform - BCDeployConfig.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.web.spring.context;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/** 
 * 业务组件配置xml对应实体
 * @author slx  
 * @date 2013-11-15 下午4:49:10 
 * @version 1.0
 *  
 */
@XmlRootElement(name="config") 
public class BCDeployConfig{
	private List<BCDeploy> bcs;
	
	@XmlElementWrapper(name="BusinessComponents")
	@XmlElement(name="bc")
	public List<BCDeploy> getBcs() {
		return bcs;
	}
	public void setBcs(List<BCDeploy> bcs) {
		this.bcs = bcs;
	}
	
	private static BCDeployConfig bcDeplopConfig;
	private static HashMap<String,BCDeploy> hm_cfg_Name = new HashMap<String, BCDeploy>();
	static String platformDocBase ; 
	
	/**
	 * OECP平台部署的位置
	 * @author songlixiao
	 * @date 2013-11-18下午1:57:01
	 * @return
	 */
	public static String getPlatformDocBase() {
		return platformDocBase;
	}
	/**
	 * 加载业务组件部署配置文件
	 * @author songlixiao
	 * @date 2013-11-18上午10:29:34
	 * @param configFile
	 * @throws Exception
	 */
	static void loadConfig(File configFile){
        try {  
            JAXBContext jaxbContext = JAXBContext.newInstance(BCDeployConfig.class);  
            Unmarshaller um = jaxbContext.createUnmarshaller();  
            bcDeplopConfig = (BCDeployConfig)um.unmarshal(new FileInputStream(configFile));
            
            hm_cfg_Name.clear();
            for (BCDeploy bc : bcDeplopConfig.bcs) {
            	hm_cfg_Name.put(bc.getName(), bc);
			}
        } catch (Exception e) {  
            throw new RuntimeException("加载业务组件部署信息出错：", e);
        } 
	}
	/**
	 * 使用业务组件名称获取业务组件部署信息
	 * @author songlixiao
	 * @date 2013-11-18上午10:28:24
	 * @param bcname
	 * @return
	 */
	public static BCDeploy getBCDeployInfoByName(String bcname){
		BCDeploy bc = hm_cfg_Name.get(bcname);
		if(bc==null){
			throw new IllegalArgumentException("系统中没有名为:["+bcname+"]的业务组件！");
		}
		return bc;
	}
	
	/**
	 * 获得所有的组件部署配置信息
	 * @author songlixiao
	 * @date 2013-11-18上午10:40:54
	 * @return
	 */
	public static List<BCDeploy> getAllBCDeployInfos(){
		if(bcDeplopConfig==null){
			return null;
		}
		return bcDeplopConfig.getBcs();
	}
}