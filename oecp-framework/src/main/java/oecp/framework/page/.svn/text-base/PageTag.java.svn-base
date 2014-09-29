/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2008 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2008-11-4
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */    

package oecp.framework.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 自定义分页标签结构或属性
 * @author yongtree
 * @date 2008-11-4 下午03:00:40
 * @version 1.0
 */
public class PageTag extends ComponentTagSupport {   
    
    private static final long serialVersionUID = 4708505947220186594L;
	private String pageNo;   
    private String total;   
    private String styleClass;   
    private String theme;
    private String url;
    private String urlType;
       
    public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setTheme(String theme) {   
        this.theme = theme;   
    }       
    public void setStyleClass(String styleClass) {   
        this.styleClass = styleClass;   
    }   
    public void setPageNo(String pageNo) {   
        this.pageNo = pageNo;   
    }   
    public void setTotal(String total) {   
        this.total = total;   
    }   
  
   
	@Override  
    public Component getBean(ValueStack arg0, HttpServletRequest arg1, HttpServletResponse arg2) {   
        return new Pages(arg0, arg1);   
    }   
  
    protected void populateParams() {   
        super.populateParams();   
           
        Pages pages = (Pages)component;   
        pages.setPageNo(pageNo);     
        pages.setTotal(total);   
        pages.setStyleClass(styleClass);   
        pages.setTheme(theme);   
        pages.setUrl(url);
        pages.setUrlType(urlType);
  
    }   
}   
