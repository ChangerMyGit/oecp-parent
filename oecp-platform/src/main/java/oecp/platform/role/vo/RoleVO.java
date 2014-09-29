package oecp.platform.role.vo;

import java.io.Serializable;

public class RoleVO implements Serializable {
	private String id;// 主键
	private String code;// 编码
	private String name;// 名称
	private Boolean locked;// 是否锁定
	private Boolean isEdit;// 是否可编辑,本公司创建的为true,非本公司创见的为false
	private String orgRoleId;// 组织角色Id
	private String createOrgName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getOrgRoleId() {
		return orgRoleId;
	}

	public void setOrgRoleId(String orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public String getCreateOrgName() {
		return createOrgName;
	}

	public void setCreateOrgName(String createOrgName) {
		this.createOrgName = createOrgName;
	}

	

}
