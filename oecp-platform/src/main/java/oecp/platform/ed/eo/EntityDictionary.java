/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.ed.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 数据字典EO
 * 
 * @author liujt
 * @date 2011-9-28 下午02:11:11
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_ENTITYDICTIONARY")
public class EntityDictionary extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 实体类名 **/
	private String entityname;
	/** 对应表名 **/
	private String tablename;
	/** 实体属性信息 **/
	private List<EntityAttribute> entityAttributes;

	public String getEntityname() {
		return entityname;
	}

	public void setEntityname(String entityname) {
		this.entityname = entityname;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, mappedBy = "entityDictionary")
	public List<EntityAttribute> getEntityAttributes() {
		return entityAttributes;
	}

	public void setEntityAttributes(List<EntityAttribute> entityAttributes) {
		this.entityAttributes = entityAttributes;
	}

}
