package oecp.framework.web.struts2;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oecp.framework.web.JsonResult;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.DefaultActionSupport;

import com.alibaba.fastjson.JSON;

/**
 * 基于EXT的base action
 * 
 * @author yongtree
 * 
 */
public class BaseExtJsAction extends DefaultActionSupport {

	/**
	 * 结合Ext的分页功能： dir DESC limit 25 sort id start 50
	 */
	/**
	 * 当前是升序还是降序排数据
	 */
	protected String dir;
	/**
	 * 排序的字段
	 */
	protected String sort;
	/**
	 * 每页的大小
	 */
	protected Integer limit = 25;
	/**
	 * 开始取数据的索引号
	 */
	protected Integer start = 0;

	protected String jsonString;

	protected transient final Log logger = LogFactory.getLog(getClass());

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getJsonString() {
		return jsonString;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String CANCEL = "cancel";

	public final String VIEW = "view";

	/**
	 * Convenience method to get the request
	 * 
	 * @return current request
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * Convenience method to get the response
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	protected void returnErrorMsg(String msg) {
		this.jsonString = JSON.toJSONString(new JsonResult(false, msg));
		return;
	}

	public String getDir() {
		if (StringUtils.isEmpty(dir))
			return "ASC";
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public LinkedHashMap<String, String> getOrderBy() {
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		if (StringUtils.isNotEmpty(sort)) {
			orderby.put(sort, dir);
		}
		return orderby;
	}

}
