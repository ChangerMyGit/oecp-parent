package oecp.platform.bcinfo.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 服务实体
 * 
 * @author wangliang
 * @date 2011-8-10上午10:57:55
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_BIZSERVICE")
public class BizService extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 服务名称 */
	private String serviceName;

	/** 服务描述 */
	private String description;
	/** 组件 */
	private BizComponent bc;
	/** 方法 */
	private List<OperationDescription> operations;

	@OneToMany(mappedBy = "service", cascade = { CascadeType.ALL })
	public List<OperationDescription> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationDescription> operations) {
		this.operations = operations;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@ManyToOne
	public BizComponent getBc() {
		return bc;
	}

	public void setBc(BizComponent bc) {
		this.bc = bc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
