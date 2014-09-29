package com.alibaba.fastjson.parser.deserializer;


import static com.alibaba.fastjson.parser.TypeUtils.castToBigDecimal;
import static com.alibaba.fastjson.parser.TypeUtils.castToBigInteger;
import static com.alibaba.fastjson.parser.TypeUtils.castToBoolean;
import static com.alibaba.fastjson.parser.TypeUtils.castToByte;
import static com.alibaba.fastjson.parser.TypeUtils.castToDate;
import static com.alibaba.fastjson.parser.TypeUtils.castToDouble;
import static com.alibaba.fastjson.parser.TypeUtils.castToFloat;
import static com.alibaba.fastjson.parser.TypeUtils.castToInt;
import static com.alibaba.fastjson.parser.TypeUtils.castToLong;
import static com.alibaba.fastjson.parser.TypeUtils.castToShort;
import static com.alibaba.fastjson.parser.TypeUtils.castToString;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class DefaultObjectDeserializer implements ObjectDeserializer {

	public DefaultObjectDeserializer() {
	}

	public void parseMap(DefaultExtJSONParser parser, Map<String, Object> map,
			Type valueType) {
		JSONScanner lexer = parser.getLexer();

		if (lexer.token() != JSONToken.LBRACE) {
			throw new JSONException("syntax error, expect {, actual "
					+ lexer.token());
		}

		for (;;) {
			lexer.skipWhitespace();
			char ch = lexer.getCurrent();
			if (parser.isEnabled(Feature.AllowArbitraryCommas)) {
				while (ch == ',') {
					lexer.incrementBufferPosition();
					lexer.skipWhitespace();
					ch = lexer.getCurrent();
				}
			}

			String key;
			if (ch == '"') {
				key = lexer.scanSymbol(parser.getSymbolTable(), '"');
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos());
				}
			} else if (ch == '}') {
				lexer.incrementBufferPosition();
				lexer.resetStringPosition();
				return;
			} else if (ch == '\'') {
				if (!parser.isEnabled(Feature.AllowSingleQuotes)) {
					throw new JSONException("syntax error");
				}

				key = lexer.scanSymbol(parser.getSymbolTable(), '\'');
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos());
				}
			} else {
				if (!parser.isEnabled(Feature.AllowUnQuotedFieldNames)) {
					throw new JSONException("syntax error");
				}

				key = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos()
							+ ", actual " + ch);
				}
			}

			lexer.incrementBufferPosition();
			lexer.skipWhitespace();
			ch = lexer.getCurrent();

			lexer.resetStringPosition();

			Object value;
			lexer.nextToken();

			if (lexer.token() == JSONToken.NULL) {
				value = null;
				lexer.nextToken();
			} else {
				value = parser.parseObject(valueType);
			}

			map.put(key, value);

			if (lexer.token() == JSONToken.RBRACE) {
				lexer.nextToken();
				return;
			}
		}
	}

	// 这个方法做了很多优化，所以结构不是很好
	public void parseObject(DefaultExtJSONParser parser, Object object) {
		Class<?> clazz = object.getClass();
		Map<String, Method> setters = parser.getMapping().getSetters(clazz);

		JSONScanner lexer = parser.getLexer();

		if (lexer.token() != JSONToken.LBRACE) {
			throw new JSONException("syntax error, expect {, actual "
					+ lexer.token());
		}

		for (;;) {
			lexer.skipWhitespace();
			char ch = lexer.getCurrent();
			if (parser.isEnabled(Feature.AllowArbitraryCommas)) {
				while (ch == ',') {
					lexer.incrementBufferPosition();
					lexer.skipWhitespace();
					ch = lexer.getCurrent();
				}
			}

			String key;
			if (ch == '"') {
				key = lexer.scanSymbol(parser.getSymbolTable(), '"');
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos());
				}
			} else if (ch == '}') {
				lexer.incrementBufferPosition();
				lexer.resetStringPosition();
				return;
			} else if (ch == '\'') {
				if (!parser.isEnabled(Feature.AllowSingleQuotes)) {
					throw new JSONException("syntax error");
				}

				key = lexer.scanSymbol(parser.getSymbolTable(), '\'');
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos());
				}
			} else {
				if (!parser.isEnabled(Feature.AllowUnQuotedFieldNames)) {
					throw new JSONException("syntax error");
				}

				key = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':') {
					throw new JSONException("expect ':' at " + lexer.pos()
							+ ", actual " + ch);
				}
			}

			lexer.incrementBufferPosition();
			lexer.skipWhitespace();
			ch = lexer.getCurrent();

			lexer.resetStringPosition();

			Method method = setters.get(key);
			if (method == null) {
				if (!parser.isIgnoreNotMatch()) {
					throw new JSONException("setter not found, class "
							+ clazz.getName() + ", property " + key);
				}

				lexer.nextToken();
				parser.parse(); // skip

				if (lexer.token() == JSONToken.RBRACE) {
					lexer.nextToken();
					return;
				}
			} else {
				try {
					Class<?> propertyType = null;
					// 对参数类型进行赋值,由于主键是泛型,转换时可能出现类型错误.//////
					if ("id".equals(key)
							&& object instanceof oecp.framework.entity.base.NumberPKEO
							&& method.getParameterTypes()[0] == Object.class) {
						propertyType = Long.class;
					} else if ("id".equals(key)
							&& (object instanceof oecp.framework.entity.base.StringPKEO
									|| object instanceof oecp.framework.entity.base.ManualPKEO || object instanceof oecp.framework.entity.base.StringPKGen4EO)
							&& method.getParameterTypes()[0] == Object.class) {
						propertyType = String.class;
					} else {
						propertyType = method.getParameterTypes()[0];
					}
					// //////////////////////////////////////////////////////////
					if (ch == '"' && propertyType == String.class) {
						lexer.scanString();
						String value = lexer.stringVal();

						method.invoke(object, value);

					} else if ((ch >= '0' && ch <= '9' || ch == '-')
							&& (propertyType == int.class || propertyType == Integer.class)) {
						lexer.scanNumber();

						int value = lexer.intValue();

						method.invoke(object, value);

					} else if ((ch >= '0' && ch <= '9' || ch == '-')
							&& (propertyType == long.class || propertyType == Long.class)) {
						lexer.scanNumber();

						long value = lexer.longValue();

						method.invoke(object, value);
					} else {

						Object value;
						lexer.nextToken();

						// 专门调整顺序的
						if (propertyType.equals(String.class)) {
							value = lexer.stringVal();
							value = castToString(parser.parse());
						} else if (propertyType.equals(int.class)
								|| propertyType.equals(Integer.class)) {
							value = castToInt(parser.parse());
						} else if (propertyType.equals(long.class)
								|| propertyType.equals(Long.class)) {
							value = castToLong(parser.parse());
						} else if (propertyType.equals(boolean.class)
								|| propertyType.equals(Boolean.class)) {
							value = castToBoolean(parser.parse());
						} else if (propertyType.equals(BigDecimal.class)) {
							value = castToBigDecimal(parser.parse());
						} else if (propertyType.equals(Date.class)) {
							Object parsedValue = parser.parse();
							if (parsedValue instanceof String) {
								String text = (String) parsedValue;
								JSONScanner dateLexer = new JSONScanner(text);
								if (dateLexer.scanISO8601DateIfMatch()) {
									value = dateLexer.getCalendar().getTime();
								} else {
									value = castToDate(parsedValue);
								}
							} else {
								value = castToDate(parsedValue);
							}
						} else if (propertyType.equals(float.class)
								|| propertyType.equals(Float.class)) {
							value = castToFloat(parser.parse());
						} else if (propertyType.equals(double.class)
								|| propertyType.equals(Double.class)) {
							value = castToDouble(parser.parse());
						} else if (Collection.class
								.isAssignableFrom(propertyType)) {
							Type type = method.getGenericParameterTypes()[0];

							Object argVal;
							argVal = parser.parseArrayWithType(type);

							value = argVal;
						} else if (propertyType.equals(short.class)
								|| propertyType.equals(Short.class)) {
							value = castToShort(parser.parse());
						} else if (propertyType.equals(byte.class)
								|| propertyType.equals(Byte.class)) {
							value = castToByte(parser.parse());
						} else if (propertyType.equals(BigInteger.class)) {
							value = castToBigInteger(parser.parse());
						} else {
							if (lexer.token() == JSONToken.NULL) {
								value = null;
								lexer.nextToken();
							} else {
								value = parser.parseObject(propertyType);
							}
						}

						method.invoke(object, value);

						if (lexer.token() == JSONToken.RBRACE) {
							lexer.nextToken();
							return;
						}
					}
				} catch (Throwable e) {
					throw new JSONException("set proprety error, "
							+ method.getName(), e);
				}
			}

			lexer.skipWhitespace();
			ch = lexer.getCurrent();

			if (ch == ',') {
				lexer.incrementBufferPosition();
				continue;
			} else if (ch == '}') {
				lexer.incrementBufferPosition();
				lexer.nextToken();
				return;
			}

		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultExtJSONParser parser, Type type) {
		if (type instanceof Class<?>) {
			return deserialze(parser, (Class<T>) type);
		}

		if (type instanceof ParameterizedType) {
			return (T) deserialze(parser, (ParameterizedType) type);
		}

		throw new JSONException("not support type : " + type);
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public <T> T deserialze(DefaultExtJSONParser parser, ParameterizedType type) {
		try {
			Type rawType = type.getRawType();
			if (rawType instanceof Class<?>) {
				Class<?> rawClass = (Class<?>) rawType;

				if (Map.class.isAssignableFrom(rawClass)) {
					Map map;

					if (rawClass == Map.class) {
						map = new HashMap();
					} else if (rawClass == SortedMap.class) {
						map = new TreeMap();
					} else if (rawClass == NavigableMap.class) {
						map = new TreeMap();
					} else if (rawClass == HashMap.class) {
						map = new HashMap();
					} else if (rawClass == LinkedHashMap.class) {
						map = new LinkedHashMap();
					} else if (rawClass == ConcurrentMap.class) {
						map = new ConcurrentHashMap();
					} else if (rawClass == ConcurrentHashMap.class) {
						map = new ConcurrentHashMap();
					} else if (rawClass == JSONObject.class) {
						map = new JSONObject();
					} else {
						map = (Map) rawClass.newInstance();
					}

					Type valueType = type.getActualTypeArguments()[1];

					parseMap(parser, map, valueType);

					return (T) map;
				}

				if (Collection.class.isAssignableFrom(rawClass)) {
					Collection collection;

					if (rawClass == Collection.class) {
						collection = new ArrayList();
					} else if (rawClass == List.class) {
						collection = new ArrayList();
					} else if (rawClass == AbstractCollection.class) {
						collection = new ArrayList();
					} else if (rawClass == ArrayList.class) {
						collection = new ArrayList();
					} else if (rawClass == CopyOnWriteArrayList.class) {
						collection = new ArrayList();
					} else if (rawClass == JSONArray.class) {
						collection = new JSONArray();
					} else {
						collection = (Collection) rawClass.newInstance();
					}

					Type valueType = null;

					if (type.getActualTypeArguments().length > 0) {
						valueType = type.getActualTypeArguments()[0];
					}
					parseArray(parser, collection, valueType);

					return (T) collection;
				}
			}

			throw new JSONException("not support type : " + type);
		} catch (JSONException e) {
			throw e;
		} catch (Throwable e) {
			throw new JSONException(e.getMessage(), e);
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public final void parseArray(DefaultExtJSONParser parser,
			final Collection array, Type valueType) {
		final JSONLexer lexer = parser.getLexer();

		parser.accept(JSONToken.LBRACKET);

		for (;;) {
			if (parser.isEnabled(Feature.AllowArbitraryCommas)) {
				while (lexer.token() == JSONToken.COMMA) {
					lexer.nextToken();
					continue;
				}
			}

			if (lexer.token() == JSONToken.RBRACKET) {
				break;
			}

			Object val;
			if (valueType == null) {
				val = parser.parse();
			} else {
				val = parser.parseObject(valueType);
			}

			array.add(val);

			if (lexer.token() == JSONToken.COMMA) {
				lexer.nextToken();
				continue;
			}
		}

		parser.accept(JSONToken.RBRACKET);
	}

	public <T> T deserialze(DefaultExtJSONParser parser, Class<T> clazz) {
		try {
			T value = clazz.newInstance();
			parseObject(parser, value);
			return value;
		} catch (JSONException e) {
			throw e;
		} catch (Throwable e) {
			throw new JSONException(e.getMessage(), e);
		}
	}
}
