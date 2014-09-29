/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.ed.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 实体属性信息
 * @author liujt
 * @date 2011-9-28 下午02:26:50
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_ENTITYATTRIBUTE")
public class EntityAttribute extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 属性名 **/
	private String attrname;
	/** 显示名 **/
	private String dispname;
	/** 属性类型 **/
	private String attrclass;
	/** 是否可空 **/
	private boolean nullable;
	/** 长度 **/
	private String maxlength;
	/** ext编辑控件 **/
	private String exteditor;
	/** 表信息 **/
	private EntityDictionary entityDictionary;
	
	public String getAttrname() {
		return attrname;
	}

	public void setAttrname(String attrname) {
		this.attrname = attrname;
	}

	public String getDispname() {
		return dispname;
	}

	public void setDispname(String dispname) {
		this.dispname = dispname;
	}

	public String getAttrclass() {
		return attrclass;
	}

	public void setAttrclass(String attrclass) {
		this.attrclass = attrclass;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	@Column(length=4000)
	public String getExteditor() {
		return exteditor;
	}

	public void setExteditor(String exteditor) {
		this.exteditor = exteditor;
	}

	@ManyToOne
	public EntityDictionary getEntityDictionary() {
		return entityDictionary;
	}

	public void setEntityDictionary(EntityDictionary entityDictionary) {
		this.entityDictionary = entityDictionary;
	}

}
