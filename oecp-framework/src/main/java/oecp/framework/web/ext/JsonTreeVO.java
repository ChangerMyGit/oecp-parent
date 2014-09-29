package oecp.framework.web.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * json 树形结构VO
 * 
 * @author wl
 * 
 */
public class JsonTreeVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String text;
	// 是否叶子节点
	private Boolean leaf;
	// 子节点
	private List children;
	
	private Boolean checked;
	
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public List getChildren() {
		if (children == null)
			children = new ArrayList();
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

}
