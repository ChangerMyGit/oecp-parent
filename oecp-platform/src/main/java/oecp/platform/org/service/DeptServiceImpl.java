package oecp.platform.org.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.eo.Department;
import oecp.platform.org.eo.Organization;

/**
 * 组织部门服务实现
 * 
 * @author liujingtao
 * @date 2011 6 13 14:05:13
 * @version 1.0
 */
@Service("deptService")
public class DeptServiceImpl extends PlatformBaseServiceImpl<Department> implements DeptService {

	@Override
	public List<Department> getDeptByorgId(String orgid) {
		return getDao().queryByWhere(Department.class, "o.org.id=?", new Object[]{orgid});
	}

	@Override
	public List<Department> getTopDepts(String orgid) {
		List<Department> depts = getDao().queryByWhere(Department.class, "o.org.id=? AND o.parent IS NULL  ORDER BY o.idx,o.code", new Object[]{orgid});
		return depts;
	}
	
}
