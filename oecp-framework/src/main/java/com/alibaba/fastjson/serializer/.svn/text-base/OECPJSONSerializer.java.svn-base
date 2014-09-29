package com.alibaba.fastjson.serializer;

import oecp.framework.vo.base.DataVO;

/**
 * OECPJSONSerializer
 * 为扩展JSONSerializer实现输出VO接口对象数据而继承覆盖实现。
 * 因为原JSONSerializer没有预留钩子（反正我没找到）。因此只好出此下策。
 * @author slx
 * @date 2011-6-28 下午09:14:19
 * @version 1.0
 */
public class OECPJSONSerializer extends JSONSerializer {
	
	public OECPJSONSerializer() {
		super();
	}
	public OECPJSONSerializer(SerializeWriter out) {
		super(out);
	}
	public OECPJSONSerializer(JSONSerializerMap mapping){
		super(mapping);
	}
	public OECPJSONSerializer(SerializeWriter out, JSONSerializerMap mapping){
		super(out,mapping);
	}
	
	@Override
	public ObjectSerializer getObjectWriter(Class<?> clazz) {
		ObjectSerializer writer = getMapping().get(clazz);
		// map中没有，则判断是否DataVO接口的实现类，如果是，则以这个类使用DataVOSerializer来进行json输出。
		if (writer == null) {
			if (DataVO.class.isAssignableFrom(clazz)) {
				getMapping().put(clazz, DataVOSerializer.instance);
			}
			// 将DataVOSerializer放入map后，重新使用super的获取方法获得writer
			writer = super.getObjectWriter(clazz);
		}
		
		return writer;
	}

}
