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

package oecp.platform.rpt.setting.service;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.dao.DAO;
import oecp.platform.query.setting.eo.QueryConditionSetting;
import oecp.platform.rpt.setting.eo.Report;
import oecp.platform.uiview.enums.ComponentType;
import oecp.platform.uiview.vo.UIComponentVO;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.hibernate.Query;
import org.springframework.validation.Errors;

/**
 * 报表设置自定义校验器
 * @author slx
 * @date 2012-5-15 14:06:56
 * @version 1.0
 */
public class ReportSettingValidator {

	/**
	 * 检查各种编号是否重复
	 * @author slx
	 * @date 2012-5-15下午3:16:19
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 * @throws OgnlException 
	 */
	public static boolean checkcode(Object bean, ValidatorAction va, Field field, Errors errors ,DAO dao) throws OgnlException{
		// bean的类型是一直都是Report
		String id = ((Report)bean).getId();
		String code = extractValue(bean, field);
		String id_f = "id";
		String code_f = field.getProperty();
		String str_p = "";// 前缀
		Object queryroot = bean;
		if(field.getProperty().indexOf('.')>0){
			str_p = field.getProperty().substring(0,field.getProperty().indexOf('.'));
			String temp_idf = str_p.concat(".id");
			Field idfleld = new Field();
			idfleld.setProperty(temp_idf);
			id = extractValue(bean, idfleld);
			
			code_f = code_f.replaceFirst(str_p+".", "");
			queryroot = Ognl.getValue(str_p, bean);
		}
		String str_query = "SELECT COUNT(o.".concat(id_f).concat(") FROM ")
				.concat(queryroot.getClass().getName()).concat(" o WHERE o.").concat(code_f).concat("=?")
				.concat(StringUtils.isEmpty(id)?"":" AND o.".concat(id_f).concat("<>?"));
		Query query = dao.getHibernateSession().createQuery(str_query);
		query.setParameter(0, code);
		if(StringUtils.isNotEmpty(id)){
			query.setParameter(1, id);
		}
		Object count = query.uniqueResult();
		return !(Long.parseLong(count.toString()) > 0);
	}
	
	/**
	 * 检查Grid和chart是否具有dataroot属性
	 * @author slx
	 * @date 2012-5-15下午3:17:34
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean checkdataroot(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		Report report = ((Report)bean);
		UIComponentVO compvo = report.getView().getMainuivo();
		List<UIComponentVO> comps = getCmpsByType(compvo,ComponentType.Grid);
		comps.addAll(getCmpsByType(compvo,ComponentType.Chart));
		for (UIComponentVO comp : comps) {
			if(comp.getValue("dataRoot")==null){ // 只要有一个图形或者表格的dataroot为空则返回错误
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查字段Field是否设置了dataindex
	 * @author slx
	 * @date 2012-5-15下午3:18:15
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean checkFieldDataindex(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		Report report = ((Report)bean);
		UIComponentVO compvo = report.getView().getMainuivo();
		List<UIComponentVO> comps = getCmpsByType(compvo,ComponentType.Field);
		for (UIComponentVO comp : comps) {
			if(comp.getValue("dataIndex")==null){ // 只要有一个字段的dataindex为空则返回错误
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查固定条件默认值必填
	 * @author slx
	 * @date 2012-5-18下午4:41:37
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param dao
	 * @return
	 */
	public static boolean checkFixConditions(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		Report report = ((Report)bean);
		List<QueryConditionSetting> cons = report.getQueryscheme().getFixedconditions();
		if(cons == null){ // 无固定条件的不校验。
			return true;
		}
		for (QueryConditionSetting con : cons) {
			if(con.getOperators()==null || con.getOperators().size()!=1){
				return false;
			}
			if(StringUtils.isEmpty((con.getDefaultvalue()))){ // 只要有一个字段的dataindex为空则返回错误
				return false;
			}
		}
		return true;
	}
	
	
	
	private static List<UIComponentVO> getCmpsByType(UIComponentVO parentvo , ComponentType type){
		List<UIComponentVO> comps = new ArrayList<UIComponentVO>();
		if(parentvo.getType() == type){
			comps.add(parentvo);
		}
		if(parentvo.getChildren()!=null){
			for (UIComponentVO childvo : parentvo.getChildren()) {
				comps.addAll(getCmpsByType(childvo,type));
			}
		}
		return comps;
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
