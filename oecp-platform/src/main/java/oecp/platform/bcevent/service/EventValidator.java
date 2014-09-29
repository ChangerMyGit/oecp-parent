
package oecp.platform.bcevent.service;

import oecp.framework.dao.DAO;
import oecp.platform.bcevent.eo.EventInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.springframework.validation.Errors;

/**
 * 事件信息校验器
 * @author slx
 * @date 2011-8-15 15:30:50
 * @version 1.0
 */
public class EventValidator {

	
	public static boolean valueExist(Object bean, ValidatorAction va, Field field, Errors errors,DAO dao){
		String fieldname = field.getProperty();
		String orgCode = extractValue(bean, field);
		EventInfo event = (EventInfo)bean;
		String bcid = event.getBc().getId();
		String id = event.getId();
		boolean exist ;
		if(StringUtils.isEmpty(id)){
			exist = dao.isExistedByWhere(EventInfo.class, " o."+fieldname+"=? AND o.bc.id=?", new Object[]{orgCode,bcid});
		}else{
			exist = dao.isExistedByWhere(EventInfo.class, " o."+fieldname+"=? AND o.bc.id=? AND o.id<>? ", new Object[]{orgCode,bcid,id});
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
