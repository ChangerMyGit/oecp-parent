/**
 * oecp-platform - BaseDelArchivesEO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午2:06:53		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.acbase.eo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import oecp.framework.entity.base.BaseEO;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 档案明细父类EO
 * 
 * @author luanyoubo
 * @date 2014年2月25日 下午2:06:53
 * @version 1.0
 * 
 */
@MappedSuperclass
public class BaseDelArchivesEO extends BaseEO<String> {
	private static final long	serialVersionUID	= 1L;
	private String				id;

	@Id
	@Column(name = "pk", length = 20)
	@GeneratedValue(generator = "acdpk")
	@GenericGenerator(strategy = "oecp.framework.entity.pkgen.ExistString16PKGenerater", name = "acdpk")
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
