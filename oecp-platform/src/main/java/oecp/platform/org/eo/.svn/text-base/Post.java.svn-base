package oecp.platform.org.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 岗位实体
 * 
 * @author slx
 * @date 2011 4 11 10:35:45
 * @version 1.0
 */
@Entity
@Table(name="OECP_T_POST")
public class Post extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 岗位编码 **/
	private String code;
	/** 岗位名称 **/
	private String name;
	/** 是否部门管理岗位 **/
	private Boolean charge;
	/** 上级岗位 **/
	private Post parent;
	/** 所属部门 **/
	private Department dept;
	/** 所属组织 **/
	private Organization org;
	/** 排序号 **/
	private Integer idx;
	
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
	public Boolean getCharge() {
		return charge;
	}
	public void setCharge(Boolean charge) {
		this.charge = charge;
	}
	
	@ManyToOne
	public Post getParent() {
		return parent;
	}
	public void setParent(Post parent) {
		this.parent = parent;
	}
	@ManyToOne
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	
	@ManyToOne
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	
}
