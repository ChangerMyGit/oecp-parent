/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 6 13
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.platform.maindata.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 主数据资源类型
 * 
 * @author slx
 * @date 2011 6 13 11:44:22
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_MDRESOURCE")
public class MDResource extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 编码 **/
	private String code;
	/** 名称 **/
	private String name;
	/** 对应实体名 **/
	private String eoClassName;
	/** 对应表名 **/
	private String tableName;
	/**
	 * 用于分配数据权限的EXT类（可以是OECP.ui.xxx.XXX({xxx : xxx})的形式） 此处的EXT类必须是Window的子类。
	 * **/
	private String jsClassName;

	private List<MDResourceField> fields;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEoClassName() {
		return eoClassName;
	}

	public void setEoClassName(String eoClassName) {
		this.eoClassName = eoClassName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the jsClassName
	 */
	public String getJsClassName() {
		return jsClassName;
	}

	/**
	 * @param jsClassName
	 *            the jsClassName to set
	 */
	public void setJsClassName(String jsClassName) {
		this.jsClassName = jsClassName;
	}
	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "md")
	public List<MDResourceField> getFields() {
		return fields;
	}

	public void setFields(List<MDResourceField> fields) {
		this.fields = fields;
	}

}
