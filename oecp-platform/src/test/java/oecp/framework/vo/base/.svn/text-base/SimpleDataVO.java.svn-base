package oecp.framework.vo.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author slx
 * @version 1.0
 */
public class SimpleDataVO implements DataVO {

	private HashMap<String, Object> values = new LinkedHashMap<String, Object>();

	@Override
	public Object getValue(String fieldname) {
		return values.get(fieldname);
	}

	@Override
	public void setValue(String fieldname, Object value) {
		values.put(fieldname, value);
	}

	@Override
	public String[] getFieldNames() {
			return values.keySet().toArray(new String[0]);
	}

	@Override
	public Class<?> getFieldType(String attrname) {
		return String.class;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getFieldNames());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleDataVO other = (SimpleDataVO) obj;
		if (!Arrays.equals(getFieldNames(), other.getFieldNames()))
			return false;
		for (String field : getFieldNames()) {
			if ((this.getValue(field) == null && other.getValue(field) != null)
					|| (this.getValue(field) != null && other.getValue(field) == null)
					|| ((this.getValue(field) != null
							&& other.getValue(field) != null && !this
							.getValue(field).equals(other.getValue(field)))))
				return false;
		}
		return true;
	}
}
