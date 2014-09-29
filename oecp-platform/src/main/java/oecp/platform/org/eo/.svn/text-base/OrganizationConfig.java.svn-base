/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.org.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKGen4EO;

/** 
 * 组织配置
 * @author yangtao  
 * @date 2012-7-30 上午10:18:09 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_T_ORG_CONFIG")
public class OrganizationConfig  extends StringPKGen4EO {
	/**每个组织可以配置一个特色的logo**/
	private String logoUrl;

	@Column(name="logo_url",length=200)
	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	
}
