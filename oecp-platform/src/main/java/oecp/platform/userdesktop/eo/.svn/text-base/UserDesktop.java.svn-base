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

package oecp.platform.userdesktop.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.user.eo.User;

/** 
 * 用户桌面
 * @author slx  
 * @date 2012-6-12 下午3:09:51 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_SYS_USER_DESKTOP")
public class UserDesktop extends StringPKEO {

	private static final long serialVersionUID = 1L;
	
	private User user;
	
	private Function function;
	
	private Integer idx;
	
	private String alias;
	
	private String ico;

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
