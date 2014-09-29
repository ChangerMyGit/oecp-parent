package oecp.platform.org.eo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKGen4EO;

/**
 * 组织实体
 * 
 * @author slx
 * @date 2011 4 11 09:37:25
 * @version 1.0
 */
@Entity
@Table(name="OECP_T_ORG")
public class Organization extends StringPKGen4EO {

	private static final long serialVersionUID = 1L;

	/** 组织编号 **/
	private String code;
	/** 组织名称 **/
	private String name;
	/** 上级组织 **/
	private Organization parent;
	/** 下级级组织 **/
	private List<Organization> childOrgs;
	/** 排序号 **/
	private Integer idx;
	/** 是否封存 **/
	private Boolean lock;
	/** 组织配置信息 **/
	private OrganizationConfig organizationConfig;
	
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
	public Organization getParent() {
		return parent;
	}
	public void setParent(Organization parent) {
		this.parent = parent;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	@OneToMany(mappedBy="parent")
	public List<Organization> getChildOrgs() {
		return childOrgs;
	}
	public void setChildOrgs(List<Organization> childOrgs) {
		this.childOrgs = childOrgs;
	}
	@Column(name="locked")
	public Boolean getLock() {
		return lock==null?false:lock;
	}
	public void setLock(Boolean lock) {
		this.lock = lock;
	}
	@OneToOne
	public OrganizationConfig getOrganizationConfig() {
		return organizationConfig;
	}
	public void setOrganizationConfig(OrganizationConfig organizationConfig) {
		this.organizationConfig = organizationConfig;
	}
	
}
