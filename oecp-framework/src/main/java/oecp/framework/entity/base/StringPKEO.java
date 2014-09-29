package oecp.framework.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

/**
 * 业务实体bean，包括了业务数据标中的必须字段
 * 
 * @author slx
 * 
 */
@MappedSuperclass
public abstract class StringPKEO extends BaseEO<String> {

	private static final long serialVersionUID = 5995463429089959965L;

	private String id;// 业务实体的主键，采用hibernate的UUID的生成策略

	@Id
	@Column(name = "pk", length = 20)
	@GeneratedValue(generator = "string16")
	@GenericGenerator(strategy = "oecp.framework.entity.pkgen.String16PKGenerater", name = "string16")
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		if (StringUtils.isEmpty(id)) {
			id = null;
		}
		this.id = id;
	}

}
