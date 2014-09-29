/**
 * oecp-platform - BaseMasArchivesEO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午2:04:56		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.acbase.eo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import oecp.framework.entity.base.BaseEO;

import org.hibernate.annotations.GenericGenerator;

/**
 * 档案主档父类EO
 * 
 * @author luanyoubo
 * @date 2014年2月25日 下午2:04:56
 * @version 1.0
 * 
 */
@MappedSuperclass
public class BaseMasArchivesEO extends BaseEO<String> {
	private static final long	serialVersionUID	= 1L;
	private String				id;
	/** 创建人 **/
	private String				creater;
	/** 创建时间 **/
	private Date				createdate;
	/** 最后修改人 **/
	private String				changer;
	/** 最后修改时间 **/
	private Date				changedate;

	@Id
	@Column(name = "pk", length = 20)
	@GeneratedValue(generator = "acmpk")
	@GenericGenerator(strategy = "oecp.framework.entity.pkgen.ExistString16PKGenerater", name = "acmpk")
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	public Date getChangedate() {
		return changedate;
	}

	public void setChangedate(Date changedate) {
		this.changedate = changedate;
	}
}
