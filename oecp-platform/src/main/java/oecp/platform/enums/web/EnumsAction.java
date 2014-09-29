package oecp.platform.enums.web;

import oecp.framework.util.enums.EnumList;
import oecp.framework.web.JsonResult;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 枚举档案 Action
 * 
 * @author wangliang
 * @date 2011-7-19上午10:06:01
 * @version 1.0
 */
@Controller("enumsAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/enums")
public class EnumsAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;

	/** Enum ClassName */
	private String className;
	/** 排除的数据 */
	private String [] exclude;
	/**
	 * 获取枚举参照Json
	 * 
	 * @author wangliang
	 * @date 2011-7-19上午10:51:53
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "ref")
	public String ref() throws ClassNotFoundException {
		JsonResult jr;
		if (!StringUtils.isEmpty(className)) {
			Class<?extends Enum> enumObj = (Class<? extends Enum>) Class.forName(className);
			EnumList list = new EnumList(enumObj);
			if (list.size() > 0) {
				if(exclude != null && exclude.length > 0){
					for(int i=0; i<exclude.length; i++){
						for(int j=0; j<list.size(); j++){
							if( list.get(j).getValue().name().equals(exclude[i])){//list.get(j).getName().equals(exclude[i]) ||
								list.remove(j)	;
								break;
							}
						}
					}
				}
				jr = new JsonResult(list);
				setJsonString(jr.toJSONString());
			}
		}else{
			jr = new JsonResult(false, "辅助编码类名错误!");
			setJsonString(jr.toJSONString());
		}
		return SUCCESS;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String[] getExclude() {
		return exclude;
	}

	public void setExclude(String[] exclude) {
		this.exclude = exclude;
	}
}
