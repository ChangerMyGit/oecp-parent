package oecp.platform.org.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.org.eo.Department;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.eo.Organization;

/**
 * 组织部门服务接口
 * 
 * @author liujingtao
 * @date 2011 6 13 14:05:13
 * @version 1.0
 */
public interface DeptService extends BaseService<Department>{
	/**
	 * 得到指定组织的所有部门
	 * @author liujingtao
	 * @date 2011 6 13 14:07:03
	 * @param orgid
	 * 		组织id
	 * @return
	 * 		该组织的部门列表
	 */
	public List<Department> getDeptByorgId(String orgid);
	
	/**
	 * 返回指定公司所有顶级部门
	 * @author songlixiao
	 * @date 2014年1月13日上午10:03:13
	 * @param orgid
	 * @return
	 */
	public List<Department> getTopDepts(String orgid);
	
}
