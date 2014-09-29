package oecp.platform.bcfunction.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.FunctionField;

/**
 * 
 * @author liujingtao
 * @date 2011-6-29 下午01:59:02
 * @version 1.0
 */
@Service("functionFieldService")
public class FunctionFieldServiceImpl extends
		PlatformBaseServiceImpl<FunctionField> implements FunctionFieldService {

	@Override
	public List<FunctionField> getFunctionFields(String funcCode) {
		return getDao().queryByWhere(FunctionField.class, "o.function.code=?",
				new Object[] { funcCode });
	}

	@Override
	public List<FunctionField> getFunctionFieldsByFunId(String funId) {
		return getDao().queryByWhere(FunctionField.class, "o.function.id=?",
				new Object[] { funId });
	}

}
