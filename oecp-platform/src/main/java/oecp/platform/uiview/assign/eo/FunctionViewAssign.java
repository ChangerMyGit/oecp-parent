/**
 * oecp-platform - FunctionViewAssign.java
 * copyright OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-1上午11:21:44		版本:v1
 * ============================================
 * 修改人：		修改时间:			版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.assign.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.org.eo.Post;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;

/**
 * 功能视图分配关系
 * @author slx
 * @date 2011-11-1
 */
@Entity
@Table(name="OECP_SYS_UI_FUNCVIEWASSIGN")
public class FunctionViewAssign extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 功能视图 **/
	private FunctionView funcview;
	/** 业务类型 **/
	private BizType biztype;
	/** 用户 **/
	private User user;
	/** 组织角色 **/
	private OrgRole orgrole;
	/** 岗位 **/
	private Post post;
	@ManyToOne
	public FunctionView getFuncview() {
		return funcview;
	}
	public void setFuncview(FunctionView funcview) {
		this.funcview = funcview;
	}
	@ManyToOne
	public BizType getBiztype() {
		return biztype;
	}
	public void setBiztype(BizType biztype) {
		this.biztype = biztype;
	}
	@ManyToOne
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne
	public OrgRole getOrgrole() {
		return orgrole;
	}
	public void setOrgrole(OrgRole orgrole) {
		this.orgrole = orgrole;
	}
	@ManyToOne
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
}
