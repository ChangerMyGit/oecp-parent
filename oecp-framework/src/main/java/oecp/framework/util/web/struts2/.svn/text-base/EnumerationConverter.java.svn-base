/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.framework.util.web.struts2;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

/**
 * 枚举值转换器
 * 
 * @author yongtree
 * @date 2011-5-31 下午04:15:18
 * @version 1.0
 */
public class EnumerationConverter extends DefaultTypeConverter {

	private static final Log log = LogFactory
			.getLog(EnumerationConverter.class);

	@Override
	public Object convertValue(Map context, Object value, Class toType) {

		if (toType.isEnum()) {
			log.info("" + toType.getSuperclass());
			log.info("convertValue: " + value + " => " + toType);

			if (value == null)
				return null;

			if (value instanceof String[]) {
				String[] ss = (String[]) value;
				if (ss.length == 1) {
					return Enum.valueOf(toType, ss[0]);
				} else {
					Object[] oo = new Object[ss.length];
					for (int i = 0; i < ss.length; i++) {
						oo[i] = Enum.valueOf(toType, ss[i]);
					}
					return oo;
				}
			}
		}

		return super.convertValue(context, value, toType);
	}

	public static void main(String[] args) {
	}
}