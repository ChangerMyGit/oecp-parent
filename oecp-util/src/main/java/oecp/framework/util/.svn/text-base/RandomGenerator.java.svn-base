/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
 */

package oecp.framework.util;

import java.util.Random;


/**
 * 生成随机的验证码主键
 * 
 * @author hailang
 * @date 2011-12-2 下午03:20:05
 * @version 1.0
 * 
 */
public class RandomGenerator {
	private static String range = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	private static String range_num = "0123456789";
	private static String range_letter = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

	@Deprecated
	public static synchronized String getRandomString() {
		Random random = new Random();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			result.append(range.charAt(random.nextInt(range.length())));
		}
		return result.toString();
	}

	/**
	 *  得到随机码
	 * @author yongtree
	 * @date 2012-9-4下午3:42:35
	 * @param scope 随机码的取值范围： 1-字母；2-数字；3-字母和数字
	 * @param length 随机码的长度 默认为4位
	 * @param type 大小写类型：1-小写；2-大写；其他为不区分大小写
	 * @return
	 */
	public static String getRandomKey(int scope, int length, int type) {
		String r;
		switch (scope) {
		case 1:
			r = range_letter;
			break;
		case 2:
			r = range_num;
			break;
		default:
			r = range;
			break;
		}
		if (length <= 0)
			length = 4;
		Random random = new Random();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < length; i++) {
			result.append(r.charAt(random.nextInt(r.length())));
		}
		return (type == 1 ? result.toString().toLowerCase()
				: (type == 2 ? result.toString().toUpperCase() : result
						.toString()));
	}

	/**
	 * 生成16位不重复的字符串
	 * 
	 * @author yongtree
	 * @date 2012-9-4下午3:24:11
	 * @return
	 */
	public static String get16UniqueKey() {
		return new StringBuffer(24).append(getJVM_last2()).append(getHmTime())
				.append(format(getCount())).toString();
	}

	private static String getJVM_last2() {
		return format(getJVM()).substring(5);
	}

	private static int getJVM() {
		return JVM;
	}

	private static String getHmTime() {
		long l = System.currentTimeMillis();
		return Long.toString(l, 32);
	}

	private static short getCount() {
		synchronized (RandomGenerator.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	private static String format(int intValue) {
		String formatted = Integer.toHexString(intValue);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	private static short counter = (short) 0;

	
}
