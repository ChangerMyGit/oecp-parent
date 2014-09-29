package oecp.framework.web.ext;

import java.io.Serializable;
import java.util.List;

/**
 * 带复选框的
 * json 树形结构VO
 * 
 * @author liujingtao
 */
public class JsonCheckboxTreeVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String text;
	// 是否叶子节点
	private Boolean leaf;
	// 子节点
	private List<JsonCheckboxTreeVO> children;
	// 是否叶子节点
	private Boolean checked;
	
	private String type;

	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the leaf
	 */
	public Boolean getLeaf() {
		return leaf;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the children
	 */
	public List<JsonCheckboxTreeVO> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<JsonCheckboxTreeVO> children) {
		this.children = children;
	}
	
}
