package oecp.platform.bcfunction.service;

import java.util.List;

import oecp.framework.service.BaseService;
import oecp.platform.bcfunction.eo.FunctionField;

/**
 *
 * @author liujingtao
 * @date 2011-6-29 上午11:46:48
 * @version 1.0
 */
public interface FunctionFieldService extends BaseService<FunctionField>{
	/**
	 * 获取指定功能下的所有FunctionField
	 * 
	 * @return
	 */
	public List<FunctionField> getFunctionFields(String funcCode);
	/**
	 * 获取指定功能下的所有FunctionField
	 * 
	 * @return
	 */
	public List<FunctionField> getFunctionFieldsByFunId(String funId);
}
