package oecp.platform.bcinfo.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 服务方法实体
 * 
 * @author wangliang
 * @date 2011-8-10上午10:57:37
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_OPERATIONDESCRIPTION")
public class OperationDescription extends StringPKEO {

	private static final long serialVersionUID = 8009395140829250477L;

	private String operationName;// 方法名
	private String description;// 功能描述
	private BizService service;// 服务

	@ManyToOne
	public BizService getService() {
		return service;
	}

	public void setService(BizService service) {
		this.service = service;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
