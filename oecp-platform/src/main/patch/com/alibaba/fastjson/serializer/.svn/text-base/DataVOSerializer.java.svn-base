/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-28
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONException;

import oecp.framework.vo.base.DataVO;

/**
 * 为将DataVO接口输出为json扩展的Serializer
 * 
 * @author slx
 * @date 2011-6-28 下午08:04:01
 * @version 1.0
 */
public class DataVOSerializer implements ObjectSerializer {

	public static final DataVOSerializer instance = new DataVOSerializer();

	public void write(JSONSerializer serializer, Object object) throws IOException {
		SerializeWriter out = serializer.getWrier();
		DataVO vo = (DataVO) object;
		if (vo == null) {
			out.append("null");
			return;
		}
		try {
			String[] fields = vo.getFieldNames();
			if (fields.length == 0) {
				out.append("{}");
				return;
			}

			out.append('{');

			boolean commaFlag = false;
			for (int i = 0; i < fields.length; ++i) {

				Object propertyValue = vo.getValue(fields[i]);

				if (propertyValue == null
						&& (!serializer
								.isEnabled(SerializerFeature.WriteMapNullValue))) {
					continue;
				}

				List<PropertyFilter> propertyFilters = serializer
						.getPropertyFiltersDirect();
				if (propertyFilters != null) {
					boolean apply = true;
					for (PropertyFilter propertyFilter : propertyFilters) {
						if (!propertyFilter.apply(object, fields[i],
								propertyValue)) {
							apply = false;
							break;
						}
					}

					if (!apply) {
						continue;
					}
				}

				if (commaFlag) {
					out.append(',');
				}

				commaFlag = true;
				if (propertyValue == null) {
					out.write(fields[i] + " : null ");
					continue;
				}
				ObjectSerializer objserializer = serializer
						.getObjectWriter(propertyValue.getClass());
				out.write("\"".concat(fields[i]).concat("\":"));
				objserializer.write(serializer, propertyValue);
			}

			out.append('}');
		} catch (Exception e) {
			throw new JSONException("write javaBean error", e);
		}
	}

}
