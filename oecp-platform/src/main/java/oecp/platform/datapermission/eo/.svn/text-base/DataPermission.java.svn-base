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

package oecp.platform.datapermission.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.maindata.eo.MDResourceField;
import oecp.platform.org.eo.Post;

/**
 * 数据权限分配EO
 * 
 * @author slx
 * @date 2011 6 13 14:20:55
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_DATAPERMISSION")
public class DataPermission extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 岗位 **/
	private Post post;
	/** 主数据字段 **/
	private MDResourceField mdField;
	/** 操作符 **/
	private String operator;
	/** 值列表，多只的用逗号隔开 **/
	private String value;
	/** 所属功能 **/
	private Function function;

	@ManyToOne()
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	@ManyToOne()
	public MDResourceField getMdField() {
		return mdField;
	}

	public void setMdField(MDResourceField mdField) {
		this.mdField = mdField;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

}
