/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oecp.framework.util.web.struts2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 *日期转换器
 * @author yongtree
 */
public class DateConverter extends StrutsTypeConverter {

	private static SimpleDateFormat[] FORMATERS = new  SimpleDateFormat[]{
		new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE),
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE),
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.CHINESE)};
	
	@Override
	public Object convertFromString(Map context, String[] values, Class clazz) {
		String timeString = values[0];
		Date date = null;
		for (SimpleDateFormat formater : FORMATERS) {
			try {
			date = formater.parse(timeString);
			} catch (ParseException ex) {
			}
		}
		if(date == null){ // 所有格式都无法匹配时抛出异常
			throw new IllegalArgumentException("无效的日期格式！");
		}
		
		return date;
	}

	@Override
	public String convertToString(Map context, Object object) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
		Date date = (Date)object;
		return df.format(date);
	}

}
