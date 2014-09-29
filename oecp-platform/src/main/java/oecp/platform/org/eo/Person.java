package oecp.platform.org.eo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 人员实体
 * 
 * @author slx
 * @date 2011 4 11 11:01:20
 * @version 1.0
 */
@Entity
@Table(name="OECP_T_PERSON")
public class Person extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 人员编号 **/
	private String no;
	/** 人员姓名 **/
	private String name;
	/** email **/
	private String email;
	/** 手机号 **/
	private String mobile;
	/** 任职岗位 **/
	private Post post;
	/** 兼职岗位 **/
	private List<Post> otherPosts;
	
	@Column(unique=true)
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@ManyToOne
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	@ManyToMany
	@JoinTable(name="OECP_T_PERSON_POST")
	public List<Post> getOtherPosts() {
		return otherPosts;
	}
	public void setOtherPosts(List<Post> otherPosts) {
		this.otherPosts = otherPosts;
	}
}
