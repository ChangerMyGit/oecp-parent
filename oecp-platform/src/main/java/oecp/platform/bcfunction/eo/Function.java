package oecp.platform.bcfunction.eo;

import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlType;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcinfo.eo.BizComponent;

/**
 * 组件功能实体
 * 
 * @author wangliang
 * @date 2011 4 11 11:31:00
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_FUNCTION")
@XmlType(namespace = "http://www.oecp.cn")
public class Function extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 功能编号 */
	private String code;
	/** 功能名称 */
	private String name;
	/** 功能描述 */
	private String description;
	/** 是否可运行 */
	private Boolean runable;
	/** 是否使用审批流 */
	private Boolean wsuserd;
	/** 流程中调用业务的服务 */
	private String bizServiceForBpm;
	/** 上级功能编码 */
	private Function parent;
	/** 功能界面 */
	private List<FunctionUI> uis;
	/** 所属组件 */
	private BizComponent bc;
	/** 显示顺序 */
	private Double displayOrder;
	/** 子元素 */
	private List<Function> children;
	/** 数据权限可控字段 */
	private List<FunctionField> functionFields;
	/**主实体**/
	private String mainEntity;

	public Function() {
		super();
	}

	public Function(String id, String name) {
		setId(id);
		this.name = name;
	}

	public Double getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Double displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * 得到默认的UI
	 * 
	 * @author yongtree
	 * @date 2011-5-13 下午05:02:27
	 * @return
	 */
	@Transient
	public FunctionUI findDefaultUI() {
		for (FunctionUI ui : getUis()) {
			if (ui.getIsDefault()) {
				return ui;
			}
		}
		return null;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public Function getParent() {
		return parent;
	}

	public void setParent(Function parent) {
		this.parent = parent;
	}

	public Boolean getRunable() {
		return runable;
	}

	public void setRunable(Boolean runable) {
		this.runable = runable;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE },fetch = FetchType.LAZY, mappedBy = "function")
	public List<FunctionUI> getUis() {
		return uis;
	}

	public void setUis(List<FunctionUI> uis) {
		this.uis = uis;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public BizComponent getBc() {
		return bc;
	}

	public void setBc(BizComponent bc) {
		this.bc = bc;
	}

	public Boolean getWsuserd() {
		return wsuserd;
	}

	public void setWsuserd(Boolean wsuserd) {
		this.wsuserd = wsuserd;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, mappedBy = "parent")
	@OrderBy("displayOrder asc")
	public List<Function> getChildren() {
		return children;
	}

	public void setChildren(List<Function> children) {
		this.children = children;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "function")
	public List<FunctionField> getFunctionFields() {
		return functionFields;
	}

	public void setFunctionFields(List<FunctionField> functionFields) {
		this.functionFields = functionFields;
	}

	/**
	 * @return the bizServiceForBpm
	 */
	public String getBizServiceForBpm() {
		return bizServiceForBpm;
	}

	/**
	 * @param bizServiceForBpm
	 *            the bizServiceForBpm to set
	 */
	public void setBizServiceForBpm(String bizServiceForBpm) {
		this.bizServiceForBpm = bizServiceForBpm;
	}

	public String getMainEntity() {
		return mainEntity;
	}

	public void setMainEntity(String mainEntity) {
		this.mainEntity = mainEntity;
	}

	
}
