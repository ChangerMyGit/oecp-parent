/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 业务流程节点的配置信息
 * @author yangtao
 * @date 2011-9-5下午02:23:18
 * @version 1.0
 */
@Entity
@Table(name="OECP_BPM_BIZ_CONFIG")
public class BizConfigEo  extends StringPKEO{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4513179381590064515L;

	//业务流程节点
	private BizProActivity bizProActivity;
	
	//该业务流程节点要执行的web服务的路径
	private String webServiceURL;

	/**
	 * @return the bizProActivity
	 */
	@ManyToOne
	public BizProActivity getBizProActivity() {
		return bizProActivity;
	}

	/**
	 * @param bizProActivity the bizProActivity to set
	 */
	public void setBizProActivity(BizProActivity bizProActivity) {
		this.bizProActivity = bizProActivity;
	}

	/**
	 * @return the webServiceURL
	 */
	public String getWebServiceURL() {
		return webServiceURL;
	}

	/**
	 * @param webServiceURL the webServiceURL to set
	 */
	public void setWebServiceURL(String webServiceURL) {
		this.webServiceURL = webServiceURL;
	}

	
}
