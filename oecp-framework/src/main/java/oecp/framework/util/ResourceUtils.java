package oecp.framework.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 国际化资源工具类
 * 
 * @author yongtree
 */
public class ResourceUtils {

	private final static MyResourceBundleControl ctl = new MyResourceBundleControl();

	/**
	 * 返回 {res}.properties 中 key 对应的值
	 * 
	 * @param baseName
	 * @param key
	 * @return
	 */
	public static String getString(String baseName, String key) {
		return _getStringForLocale(Locale.getDefault(), baseName, key);
	}

	public static String ui(String key) {
		return getString("ui", key);
	}

	/**
	 * 返回 {res}.properties 中 key 对应的值
	 * 
	 * @param locale
	 * @param baseName
	 * @param key
	 * @return
	 */
	private static String _getStringForLocale(Locale locale, String baseName,
			String key) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle(baseName, locale,
					ResourceUtils.class.getClassLoader(), ctl);
			return (rb != null) ? rb.getString(key) : null;
		} catch (MissingResourceException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * 返回 {res}.properties 中 key 对应的值，并对值进行参数格式化
	 * 
	 * @param baseName
	 * @param key
	 * @param args
	 * @return
	 */
	public static String getString(String baseName, String key, Object... args) {
		String text = getString(baseName, key);
		return (text != null) ? MessageFormat.format(text, args) : null;
	}

	/**
	 * 返回 {res}.properties 中 key 对应的值，并对值进行参数格式化
	 * 
	 * @param locale
	 * @param baseName
	 * @param key
	 * @param args
	 * @return
	 */
	public static String getStringForLocale(Locale locale, String baseName,
			String key, Object... args) {
		String text = _getStringForLocale(locale, baseName, key);
		return (text != null) ? MessageFormat.format(text, args) : null;
	}

	

	/**
	 * 重载控制器，每1个小时重载一次
	 * 
	 * @author Winter Lau
	 * @date 2010-5-12 下午11:20:02
	 */
	private static class MyResourceBundleControl extends ResourceBundle.Control {

		/**
		 * 每1个小时重载一次
		 */
		@Override
		public long getTimeToLive(String baseName, Locale locale) {
			return 3600000;
		}

		@Override
		public boolean needsReload(String baseName, Locale locale,
				String format, ClassLoader loader, ResourceBundle bundle,
				long loadTime) {
			return true;
		}
	}
}
