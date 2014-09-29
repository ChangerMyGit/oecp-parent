/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.query.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import oecp.framework.exception.BizException;
import oecp.framework.util.enums.EnumDescription;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.query.setting.annotation.OperatorDescription;
import oecp.platform.query.setting.enums.Operator;
import oecp.platform.query.setting.eo.QueryScheme;

/**
 * 
 * @author wangliang
 * @date 2012-5-11 下午3:28:22
 * @version 1.0
 * 
 */
@Service("querySchemeService")
public class QuerySchemeServiceImpl extends
		PlatformBaseServiceImpl<QueryScheme> implements QuerySchemeService {

	@Override
	public QueryScheme getQuerySchemeByCode(String code) {
		QueryScheme eo = this.getDao().findByWhere(QueryScheme.class,
				"o.code=?", new Object[] { code });
		// eo.loadLazyAttributes();
		return eo;
	}
	
	@Override
	public QueryScheme getQuerySchemeByCode_full(String code) {
		QueryScheme eo = this.getDao().findByWhere(QueryScheme.class,
				"o.code=?", new Object[] { code });
		 eo.loadLazyAttributes();
		return eo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getOperator() throws NoSuchFieldException, SecurityException {
		Map operator = new HashMap();
		for (Operator e : Operator.class.getEnumConstants()) {
			String enumName = ((Enum) e).name();
			Field field = e.getClass().getField(enumName);
			String desValue = field.getAnnotation(EnumDescription.class)
					.value();
			String opeValue = field.getAnnotation(OperatorDescription.class)
					.operator();
			Map m = new HashMap();
			m.put("description", desValue);
			m.put("operator", opeValue);
			operator.put(enumName, m);
		}
		return operator;
	}

	@Override
	public void delete(Serializable id) throws BizException {
		try {
			super.delete(id);
		} catch (Exception e) {
			throw new BizException("删除查询方案失败！可能此方案正在被使用！");
		}
	}
}
