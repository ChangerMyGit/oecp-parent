/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 19
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.bcfunction.service;

import oecp.framework.dao.DAO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.eo.PermissionFuncUI;
import oecp.platform.permission.eo.PermissionUIElement;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.springframework.validation.Errors;

/**
 * 功能注册数据校验器
 * 
 * @author slx
 * @date 2011 5 31 16:29:33
 * @version 1.0
 */
public class FunctionValidator {
	
	/**
	 * 检查编号在数据库中是否重复。
	 * 更新操作时，查询不包含自己的编号。
	 * @author slx
	 * @date 2011 5 31 15:53:13
	 * @modifyNote
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean checkFuncCode(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		String code = extractValue(bean, field);
		String id = (String)((Function)bean).getId();
		boolean exist ;
		if(id == null){
			exist = dao.isExistedByWhere(Function.class, " o.code=? ", new Object[]{code});
		}else{
			exist = dao.isExistedByWhere(Function.class, " o.code=? AND o.id<>? ", new Object[]{code,id});
		}
		return !exist;
	}
	
	/**
	 * 检查功能是否被分配
	 * @author slx
	 * @date 2011 5 31 16:27:12
	 * @modifyNote
	 * @param bean
	 * 		funcitonId
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean isfunctionUsed(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		boolean exist = dao.isExistedByWhere(Permission.class, " o.function.id = ? ", new Object[]{bean});
		return !exist;
	}
	
	/**
	 * 检查功能是否有下级
	 * @author slx
	 * @date 2011 5 31 16:27:47
	 * @modifyNote
	 * @param bean
	 * 		functionId
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean isfunctionHasChild(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		boolean exist = dao.isExistedByWhere(Function.class, " o.parent.id = ? ", new Object[]{bean});
		return !exist;
	}
	
	/**
	 * 检查功能UI是否被权限分配
	 * @author slx
	 * @date 2011 6 1 17:02:36
	 * @modifyNote
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean isUIsUsed(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		String[] ids = (String[])bean;
		StringBuffer where = new StringBuffer("o.functionUI.id IN (");
		int len = ids.length;
		for(int i=0 ; i<len ;i++){
			if(i==0){
				where.append(" ?");
			}else{
				where.append(" , ?");
			}
		}
		where.append(")");
		boolean exist = dao.isExistedByWhere(PermissionFuncUI.class, where.toString(), ids);
		return !exist;
	}
	
	protected static String extractValue(Object bean, Field field) {
        String value = null;

        if (bean == null) {
            return null;
        } else if (bean instanceof String) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        return value;
    }
}
