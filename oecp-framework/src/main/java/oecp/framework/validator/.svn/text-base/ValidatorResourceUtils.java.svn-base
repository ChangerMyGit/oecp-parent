package oecp.framework.validator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 校验I18N资源工具 <br>
 * 实现合并资源,统一key时,后加入的配置将覆盖前面的.
 * 
 * @author slx
 * @date 2011 4 14 12:08:37
 * @version 1.0
 */
public class ValidatorResourceUtils {

	private Map<String,ResourceBundle> resourceBundles = new LinkedHashMap<String,ResourceBundle>();
	private List<String> resourceNames = new ArrayList<String>();
	/** 刷新时间 **/
	private static long updateTime;
	/** 是否自动刷洗 **/
	private static boolean autoUpdate;
	
	/** 刷新控制器 **/
	private ResourceBundleControl control = new ResourceBundleControl();

	/**
	 * 根据key去的资源消息.不存在是直接返回key.
	 * @author slx
	 * @date 2011 4 14 14:56:50
	 * @modifyNote
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		int pos = resourceNames.size() - 1;
		for (; pos >= 0; pos--) {
			String resourceName = resourceNames.get(pos);
			ResourceBundle rsbd = resourceBundles.get(resourceName);
			if (rsbd.containsKey(key)) {
				return rsbd.getString(key);
			}
		}
		return key;
	}

	/**
	 * 加载资源
	 * @author slx
	 * @date 2011 4 14 14:57:32
	 * @modifyNote
	 * @param baseName
	 * 		资源名称
	 * @param locale
	 * 		本地
	 */
	public void loadResource(String baseName, Locale locale) {
		if(!resourceNames.contains(baseName)){
			ResourceBundle rsbd = ResourceBundle.getBundle(baseName, locale, control);
			resourceBundles.put(baseName, rsbd);
			resourceNames.add(baseName);
		}
	}
	
	/**
	 * 设置自动刷新资源
	 * @author slx
	 * @date 2011 4 14 14:31:56
	 * @modifyNote
	 * @param autoUpdate
	 * 		是否自动刷新
	 * @param updateTime
	 * 		刷新时间(秒)
	 */
	public static void setAutoUpdate(boolean autoUpdate ,long updateTime) {
		ValidatorResourceUtils.autoUpdate = autoUpdate;
		ValidatorResourceUtils.updateTime = updateTime;
	}

	/**
	 * 定时刷进控制器
	 * 
	 * @author slx
	 * @date 2011 4 14 14:30:06
	 * @version 1.0
	 */
	private static class ResourceBundleControl extends ResourceBundle.Control {
		/**
		 * 
		 * 每1个小时重载一次
		 */
		@Override
		public long getTimeToLive(String baseName, Locale locale) {
			return updateTime;
		}

		@Override
		public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
			return autoUpdate;
		}

	}
}
