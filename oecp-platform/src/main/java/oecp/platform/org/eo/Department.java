package oecp.platform.org.eo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 部门实体
 * 
 * @author slx
 * @date 2011 4 11 10:34:22
 * @version 1.0
 */
@Entity
@Table(name="OECP_T_DEPT")
public class Department extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 部门编号 **/
	private String code;
	/** 部门名称 **/
	private String name;
	/** 所属组织 **/
	private Organization org;
	/** 上级部门 **/
	private Department parent;
	/** 下级级组织 **/
	private List<Department> childDepts;
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
	@ManyToOne
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	@ManyToOne
	public Department getParent() {
		return parent;
	}
	public void setParent(Department parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy="parent")
	public List<Department> getChildDepts() {
		return childDepts;
	}
	public void setChildDepts(List<Department> childDepts) {
		this.childDepts = childDepts;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	
}
