package oecp.framework.entity.base;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 业务实体bean，包括了业务数据标中的必须字段
 * 
 * @author slx
 * 
 */
@MappedSuperclass
public abstract class ManualPKEO extends BaseEO<String> {

	private static final long serialVersionUID = 5995463429089959965L;

	private String id;

	@Id
	@Column(name = "pk", length = 36)
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	
}
