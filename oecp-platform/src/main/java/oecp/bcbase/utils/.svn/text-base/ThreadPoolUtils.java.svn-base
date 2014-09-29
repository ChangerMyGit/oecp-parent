/**
 * oecp-platform - ThreadUtils.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午2:21:23		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.bcbase.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 
 * 线程池工具类
 * @author luanyoubo  
 * @date 2013年12月17日 下午2:21:23 
 * @version 1.0
 *  
 */
public class ThreadPoolUtils {
	/**
	 * 内部线程池
	 */
	private static ExecutorService service =  Executors.newCachedThreadPool();

	/**
	 * 启动线程执行异步操作
	 * @author luanyoubo
	 * @date 2013年12月17日下午2:34:33
	 * @param command
	 */
	public static void execute(Runnable command){
		service.execute(command);
	}
	
}
