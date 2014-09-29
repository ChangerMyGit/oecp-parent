/**
 * 
 */
package oecp.platform.datapermission.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.org.eo.Post;

/**
 * 离散数据权限对象
 * 
 * @author liujingtao
 * @date 2011-6-30 下午08:21:52
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_DataDisPermission")
public class DataDiscretePermission extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 主数据类型 **/
	private MDResource mdType;
	/** 岗位 **/
	private Post post;
	/** 数据主键值 **/
	private String dataid;
	/** 所属功能 **/
	private Function function;

	@ManyToOne
	public MDResource getMdType() {
		return mdType;
	}

	public void setMdType(MDResource mdType) {
		this.mdType = mdType;
	}

	@ManyToOne
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

}
