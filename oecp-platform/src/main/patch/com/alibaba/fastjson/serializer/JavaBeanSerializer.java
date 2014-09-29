/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.JavaBeanMapping;

/**
 * 
 * @author wenshao<szujobs@hotmail.com>
 * 
 */
public class JavaBeanSerializer implements ObjectSerializer {
	// serializers
	private final FieldSerializer[] getters;

	private static Set<Class<?>> primitiveClassSet = new HashSet<Class<?>>();
	private static Set<Class<?>> directClassSet = new HashSet<Class<?>>();
	static {
		primitiveClassSet.add(boolean.class);
		primitiveClassSet.add(byte.class);
		primitiveClassSet.add(short.class);
		primitiveClassSet.add(int.class);
		primitiveClassSet.add(long.class);
		primitiveClassSet.add(float.class);
		primitiveClassSet.add(double.class);

		directClassSet.add(Boolean.class);
		directClassSet.add(Byte.class);
		directClassSet.add(Short.class);
		directClassSet.add(Integer.class);
		directClassSet.add(Long.class);
		directClassSet.add(Float.class);
		directClassSet.add(Double.class);
		directClassSet.add(BigInteger.class);
		directClassSet.add(BigDecimal.class);
	}

	public FieldSerializer[] getGetters() {
		return getters;
	}

	public JavaBeanSerializer(Class<?> clazz) {
		this(clazz, (Map<String, String>) null);
	}

	public JavaBeanSerializer(Class<?> clazz, String... aliasList) {
		this(clazz, createAliasMap(aliasList));
	}

	static Map<String, String> createAliasMap(String... aliasList) {
		Map<String, String> aliasMap = new HashMap<String, String>();
		for (String alias : aliasList) {
			aliasMap.put(alias, alias);
		}

		return aliasMap;
	}

	public JavaBeanSerializer(Class<?> clazz, Map<String, String> aliasMap) {
		List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();
		for (Method method : clazz.getMethods()) {
			String methodName = method.getName();

			if (Modifier.isStatic(method.getModifiers())) {
				continue;
			}

			if (method.getReturnType().equals(Void.TYPE)) {
				continue;
			}

			if (method.getParameterTypes().length != 0) {
				continue;
			}

			JSONField annotation = method.getAnnotation(JSONField.class);

			if (annotation != null) {
				if (!annotation.serialize()) {
					continue;
				}

				if (annotation.name().length() != 0) {
					String propertyName = annotation.name();

					if (aliasMap != null) {
						propertyName = aliasMap.get(propertyName);
						if (propertyName == null) {
							continue;
						}
					}

					getterList.add(createFieldSerializer(propertyName, method));
					continue;
				}
			}

			if (methodName.startsWith("get")) {
				if (methodName.length() < 4) {
					continue;
				}

				if (methodName.equals("getClass")) {
					continue;
				}

				if (!Character.isUpperCase(methodName.charAt(3))) {
					continue;
				}

				String propertyName = Character.toLowerCase(methodName
						.charAt(3))
						+ methodName.substring(4);

				/*********************** 处理BaseEO基类 生成Json时Id重复问题 ***/
				boolean exist = false;
				for (FieldSerializer f : getterList) {
					if (f.getName().equals(propertyName)) {
						exist = true;
						break;
					}
				}
				if (exist) {
					continue;
				}
				/******************************************************************/

				Field field = JavaBeanMapping.getField(clazz, propertyName);
				if (field != null) {
					if (Modifier.isTransient(field.getModifiers())) {
						continue;
					}

					JSONField fieldAnnotation = field
							.getAnnotation(JSONField.class);

					if (fieldAnnotation != null
							&& fieldAnnotation.name().length() != 0) {
						propertyName = fieldAnnotation.name();

						if (aliasMap != null) {
							propertyName = aliasMap.get(propertyName);
							if (propertyName == null) {
								continue;
							}
						}
					}
				}

				if (aliasMap != null) {
					propertyName = aliasMap.get(propertyName);
					if (propertyName == null) {
						continue;
					}
				}

				getterList.add(createFieldSerializer(propertyName, method));
			}

			if (methodName.startsWith("is")) {
				if (methodName.length() < 3) {
					continue;
				}

				if (!Character.isUpperCase(methodName.charAt(2))) {
					continue;
				}

				String propertyName = Character.toLowerCase(methodName
						.charAt(2))
						+ methodName.substring(3);

				Field field = JavaBeanMapping.getField(clazz, propertyName);
				if (field != null) {
					JSONField fieldAnnotation = field
							.getAnnotation(JSONField.class);

					if (fieldAnnotation != null
							&& fieldAnnotation.name().length() != 0) {
						propertyName = fieldAnnotation.name();

						if (aliasMap != null) {
							propertyName = aliasMap.get(propertyName);
							if (propertyName == null) {
								continue;
							}
						}
					}
				}

				if (aliasMap != null) {
					propertyName = aliasMap.get(propertyName);
					if (propertyName == null) {
						continue;
					}
				}

				getterList.add(createFieldSerializer(propertyName, method));
			}
		}

		//
		getters = getterList.toArray(new FieldSerializer[getterList.size()]);
	}

	public void write(JSONSerializer serializer, Object object)
			throws IOException {
		SerializeWriter out = serializer.getWrier();

		try {
			if (getters.length == 0) {
				out.append("{}");
				return;
			}

			out.append('{');

			boolean commaFlag = false;
			for (int i = 0; i < getters.length; ++i) {
				FieldSerializer getter = getters[i];

				Object propertyValue = getter.getPropertyValue(object);

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
						if (!propertyFilter.apply(object, getter.getName(),
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

				getter.writeProperty(serializer, propertyValue);

				commaFlag = true;
			}

			out.append('}');
		} catch (Exception e) {
			throw new JSONException("write javaBean error", e);
		}
	}

	public static FieldSerializer createFieldSerializer(String name,
			Method method) {
		Class<?> clazz = method.getReturnType();

		if (byte.class == clazz || Byte.class == clazz) {
			return new ByteFieldSerializer(name, method);
		}

		if (short.class == clazz || Short.class == clazz) {
			return new ShortFieldSerializer(name, method);
		}

		if (int.class == clazz || Integer.class == clazz) {
			return new IntegerFieldSerializer(name, method);
		}

		if (long.class == clazz || Long.class == clazz) {
			return new LongFieldSerializer(name, method);
		}

		if (String.class == clazz) {
			return new StringFieldSerializer(name, method);
		}

		if (byte[].class == clazz) {
			return new ByteArrayFieldSerializer(name, method);
		}

		if (primitiveClassSet.contains(clazz)) {
			return new PrimitiveFieldSerializer(name, method);
		}

		if (directClassSet.contains(clazz)) {
			return new ToStringFieldSerializer(name, method);
		}

		if (Date.class.isAssignableFrom(clazz)) {
			return new DateFieldSerializer(name, method);
		}

		if (List.class.isAssignableFrom(clazz)) {
			return new ListFieldSerializer(name, method);
		}

		if (Collection.class.isAssignableFrom(clazz)) {
			return new CollectionFieldSerializer(name, method);
		}

		return new ObjectFieldSerializer(name, method);
	}

}