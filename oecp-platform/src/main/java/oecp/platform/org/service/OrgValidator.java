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

package oecp.platform.org.service;

import oecp.framework.dao.DAO;
import oecp.framework.entity.base.BaseEO;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.eo.Organization;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.springframework.validation.Errors;

/**
 * 组织机构自定义校验器
 * @author slx
 * @date 2011 5 19 14:36:23
 * @version 1.0
 */
public class OrgValidator {

	/**
	 * 检查组织保证其无下级
	 * @author slx
	 * @date 2011 5 19 15:05:45
	 * @modifyNote
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean orgNoChild(Object bean, ValidatorAction va, Field field, Errors errors ,DAO dao){
		String orgid = extractValue(bean, field);
		Organization org = dao.find(Organization.class, orgid);
		
		if(org.getChildOrgs()!=null && org.getChildOrgs().size() > 0)
			return false;
		else
			return true;
	}
	
	/**
	 * 检查公司保证其未启用业务组件
	 * @author slx
	 * @date 2011 5 19 15:06:23
	 * @modifyNote
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean orgUsedBC(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		String orgid = extractValue(bean, field);
		boolean exist = dao.isExistedByWhere(OrgUseBC.class, " o.org.id=? ", new Object[]{orgid});
		return !exist;
	}
	
	public static boolean orgCode(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		String orgCode = extractValue(bean, field);
		String id = (String)((BaseEO)bean).getId();
		boolean exist ;
		if(StringUtils.isEmpty(id)){
			exist = dao.isExistedByWhere(Organization.class, " o.code=? ", new Object[]{orgCode});
		}else{
			exist = dao.isExistedByWhere(Organization.class, " o.code=? AND o.id<>? ", new Object[]{orgCode,id});
		}
		return !exist;
	}
	
	public static boolean orgName(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		String orgCode = extractValue(bean, field);
		String id = (String)((BaseEO)bean).getId();
		boolean exist ;
		if(StringUtils.isEmpty(id)){
			exist = dao.isExistedByWhere(Organization.class, " o.name=? ", new Object[]{orgCode});
		}else{
			exist = dao.isExistedByWhere(Organization.class, " o.name=? AND o.id<>? ", new Object[]{orgCode,id});
		}
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
