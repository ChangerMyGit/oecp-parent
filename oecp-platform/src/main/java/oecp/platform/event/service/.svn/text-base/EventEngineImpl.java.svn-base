package oecp.platform.event.service;
/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import oecp.bcbase.utils.ThreadPoolUtils;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.event.annotation.Listener;
import oecp.platform.event.eo.EventConfige;
import oecp.platform.event.eo.EventHandleErrorLog;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.OrgService;

import org.apache.commons.lang.xwork.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

/**
 * 事件引擎实现类<br>
 * <br>
 * 主要功能：<br>
 * 1.初始化加载项目中的监听器（java类使用@listener注解注明）加载到监听器队列中。<br>
 * 监听器范围为当前工程中包名以小写“oecp”开头，并且使用<i>@listener</i>注解声明的java类，并且必须是spring托管的<br>
 * <br>
 * 2.实现fireEvent事件触发方法，将事件分发给相应的监听器运行<br>
 * 方法参数固定为“事件源”，“事件名称”，“触发事件的组织机构”，不定参数。<br>
 * 监听器必须遵循约定，监听方法必须以字母 <i>on开头+事件名称（首字母小写）</i><br>
 * 方法参数固定为“事件源”，“触发事件的组织机构”，不定参数
 * 
 * @author wangliang
 * @date 2012-2-8 上午9:12:50
 * @version 1.0
 * 
 */
@Service("eventEngine")
public class EventEngineImpl implements EventEngine {
	
	/**扫包过滤条件，（包名为oecp开头的包）		*/
	public static final String JarPrefix ="oecp";
	/** 监听器队列	 	*/
	private Map<String,ArrayList<String>> listeners = Collections.synchronizedMap(new HashMap<String,ArrayList<String>>());

	/** 权限列表		*/
	private Map<String,List<String>> permission = Collections.synchronizedMap(new HashMap<String,List<String>>());
	
	/** 组织机构服务类	*/
	@Autowired
	private OrgService orgservice;
	
	/**
	 * 事件监听配置服务
	 */
	@Resource
    private EventConfigeService eventConfigeService;
	
	/**
	 * 事件错误日志服务
	 */
	@Resource
    private EventErrorLogService eventErrorLogService;

	/**
	 * 初始化方法 
	 * 扫描cn.oecp下所有带Listener注解的监听类
	 * @author wangliang
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws BizException 
	 * @date 2012-2-8上午10:58:50
	 */
	@PostConstruct
	public void init() throws InstantiationException, IllegalAccessException, BizException{
		initListener();
		initPermission();
	}

	/**
	 * 加载监听器
	 * 搜索“oecp”包名开头的所有jar和class。把@listener注解标注的beanid放到监听器队列中
	 * @author wangliang
	 * @date 2012-2-10下午3:38:09
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void initListener()throws InstantiationException, IllegalAccessException{
		List<String> classFilters = new ArrayList<String>();
		classFilters.add("*");
		ClassPathScanHandler handler = new ClassPathScanHandler(true, true,classFilters);
		Set<Class<?>> packageClass = handler.getPackageAllClasses(JarPrefix, true);
		for (Class<?> cla : packageClass) {//循环赋值(初始化)监听队列
			Listener a = cla.getAnnotation(Listener.class);
			if(a != null){
				String sourceName = a.source();
				if(listeners.containsKey(sourceName)){
					String[] beanid = SpringContextUtil.getApplicationContext().getBeanNamesForType(cla);
					if(beanid != null && beanid.length>0){
						listeners.get(sourceName).add(beanid[0]);
					}
				}else{
					ArrayList<String> list = new ArrayList<String>();
					String[] beanid = SpringContextUtil.getApplicationContext().getBeanNamesForType(cla);
					if(beanid != null && beanid.length>0){
						list.add(beanid[0]);
						listeners.put(sourceName,list);
					}
				}
			}
		}
	}

	/**
	 * 加载权限
	 * @author wangliang
	 * @throws BizException 
	 * @date 2012-2-10下午3:38:23
	 */
	private void initPermission() throws BizException{
		//获取所有组件
		List<Organization> orgs = orgservice.getTopOrgs();
		Set<String> allOrgIds = eachSetOrg(orgs,new HashSet<String>());
		//获取启用组件对应的实体名
		Iterator<String> iterator = allOrgIds.iterator();
		while(iterator.hasNext()){
			eachSetMainEntity(iterator.next());
		}
	}

	/**
	 * 获取组件所有未启用的组件编码
	 * @author wangliang
	 * @date 2012-2-13上午10:46:04
	 * @return
	 */
	private void eachSetMainEntity(String orgid){
		List<BizComponent> qr = orgservice.getNoUsedBCs(orgid);
		if(qr != null && qr.size()>0){
			ArrayList<String> bcCodes = new ArrayList<String>();
			for(int j=0;j<qr.size();j++){
				if(StringUtils.isEmpty(qr.get(j).getCode())){
					continue;
				}else{
					if(!bcCodes.contains(qr.get(j).getCode())){
						bcCodes.add(qr.get(j).getCode());
					}
				}
			}
			permission.put(orgid, bcCodes);
		}
	}

	/**
	 * 获取所有公司主键（包括子公司）
	 * @author wangliang
	 * @date 2012-2-13上午10:37:19
	 * @param orgs	组织列表
	 * @param set	公司主键数据集
	 * @return
	 */
	private Set<String> eachSetOrg(List<Organization> orgs,Set<String> set){
		if(orgs != null && orgs.size()>0){
			for(int i=0;i<orgs.size();i++){
				Organization _org = orgs.get(i);
				set.add(_org.getId());
				if(_org.getChildOrgs() != null && _org.getChildOrgs().size()>0){
					eachSetOrg(_org.getChildOrgs(),set);
				}
			}
		}
		return set;
	}

	@Override
	public void fireEvent(Object source, String eventName, Organization org,Object... objects)  {
		String sourceName = source.getClass().getName();//数据源类名
		ArrayList<String> list = listeners.get(sourceName);
		if(list != null){
			String methodName = getMethodName(eventName);
				//触发监听器
			for(Object obj:list){
				Object bean = SpringContextUtil.getBean((String)obj);
				String bccode = getBCCode(bean.getClass().getName());
				List<String> bccodes = permission.get(org.getId());//获取未启用的组件编码
				if(bccodes ==null || !bccodes.contains(bccode)){//没有未启用编码列表或者不在不启用列表中。监听器所在组件在当前公司中启用。
					try {
						Method method = bean.getClass().getMethod(methodName, source.getClass(),Organization.class,Object[].class);
						if(method != null){
	                        Map<String, String> map = new HashMap<String, String>();
	                        map.put("sourceName", sourceName);
	                        map.put("listener", bean.getClass().getName());
	                        map.put("action", methodName);
	                        // 获取监听器配置
	                        EventConfige confige = eventConfigeService.getEventConfige(map);   
							if (confige != null && "Y".equals(confige.getStartFlag())) {
								// 同步标志
								String synFlag = confige.getSynFlag();
								// 同步
								if ("Y".equals(synFlag))
									method.invoke(bean, new Object[] { source,org, objects });
								else {
									// 启动异步线程
									MethodRunnable command = new MethodRunnable(method,bean,new Object[] {source,org, objects},5,eventErrorLogService);
									ThreadPoolUtils.execute(command);
								}
							} 
						}
					} catch (Exception e) {
						if(e instanceof InvocationTargetException){
							Throwable t = ((InvocationTargetException) e).getTargetException();
							t.printStackTrace();
							throw new RuntimeException(t.getMessage());
						}else{
							e.printStackTrace();
							throw new RuntimeException(e.getMessage());
						}
					}
				}
			}
		}
	}
	public String getBCCode(String sourceName){
		String[] jarArg = sourceName.split("\\.");
		if(jarArg.length<2){
			return null;
		}
		String bccode = jarArg[1];
		return bccode;
	}
	
	/**
	 * 获取事件监听方法名
	 * 例如：delete事件的监听方法对应为onDelete
	 * @author wangliang
	 * @date 2012-2-9下午2:15:25
	 * @param str 
	 * @return
	 */
	public final String getMethodName(String eventName){
		if(eventName == null || eventName.length()<=0){
			return null;
		}
		return "on"+eventName.substring(0,1).toUpperCase()+eventName.substring(1);
	}

	public Map<String,ArrayList<String>> getListeners() {
		return listeners;
	}

	public void setListeners(Map<String,ArrayList<String>> listeners) {
		this.listeners = listeners;
	}

	public void setOrgservice(OrgService orgservice) {
		this.orgservice = orgservice;
	}
	
	public Map<String, List<String>> getPermission() {
		return permission;
	}
	
	public void setPermission(Map<String, List<String>> permission) {
		this.permission = permission;
	}
	
	/**
	 * 
	 * 
	 * @author luanyoubo  
	 * @date 2013年12月17日 下午3:25:47 
	 * @version 1.0
	 *
	 */
	class MethodRunnable implements Runnable {
		private Method method; // 方法
		private Object bean; // 业务bean
		private Object[] args; // 参数
		private int runCount = 0; // 运行次数
		private EventErrorLogService eventErrorLogService;
		
		/**
		 * @param method
		 * @param bean
		 * @param args
		 * @param runCount
		 */
		public MethodRunnable(Method method, Object bean, Object[] args,
				int runCount,EventErrorLogService eventErrorLogService) {
			this.method = method;
			this.bean = bean;
			this.args = args;
			this.runCount = runCount;
			this.eventErrorLogService = eventErrorLogService;
		}

		public MethodRunnable() {};

		/**
		 * 自定义线程
		 */
		public void run() {
			boolean errorFlag = true;
			while (runCount >= 1 && errorFlag) {
				try {
					method.invoke(bean, args);
					errorFlag = false;
				} catch (Exception e) {
					e.printStackTrace();
					errorFlag = true;
				} finally {
					runCount--;
				}
			}
			
			// 执行多次后仍然存在异常
			if(errorFlag) {
				// 执行参数类名称
				String[] argsClassName = new String[args.length];
				// 参数json串
				String[] argsValueJson = new String[args.length];
				for (int i = 0; i < args.length; i++) {
					Object object = args[i];
					// 如果是最后的对象数组参数
					if(args[i].getClass().isArray()){
						// 首先解析数组对象
						List<Map<String,Object>> list = dealArray(args[i]);
						if(list != null && list.size() > 0){
							String json = FastJsonUtils.toJson(list);
							argsClassName[i] = ArrayList.class.getName();
							argsValueJson[i] = json;
						}
					} else {
						String json = FastJsonUtils.toJson(object);
						argsClassName[i] = object.getClass().getName();
						argsValueJson[i] = json;
					}
				}
				ObjectMapper mapper = new ObjectMapper();
				// 存入数据库
				EventHandleErrorLog handleErrorLog = new EventHandleErrorLog();
				handleErrorLog.setBeanClassName(bean.getClass().getName());
				handleErrorLog.setMethodName(method.getName());
				handleErrorLog.setStatus("Y");
				try {
					handleErrorLog.setArgsType(mapper.writeValueAsString(argsClassName));
					handleErrorLog.setAgrsValue(mapper.writeValueAsString(argsValueJson));
					eventErrorLogService.save(handleErrorLog);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		// 处理对象数组参数
		private List<Map<String,Object>> dealArray(Object obj) {
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				Map<String,Object> map = new HashMap<String,Object>();
				Object child = Array.get(obj, i);
				if (!child.getClass().isArray()){
					try {
						map.put(child.getClass().getName(), mapper.writeValueAsString(child));
						map.put("ClassName", child.getClass().getName());
					} catch (Exception e) {}
				} else {
					List<Map<String,Object>> childList = dealArray(child);
					map.put("Array", childList);
					map.put("ClassName", child.getClass().getName());
				}
				list.add(map);
			}
			return list;
		}
		
		// 根据Json解析成数组对象
		private void parseToArray(Object[] parObjs , List<Map<String,Object>> list) throws Exception  {
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> map = list.get(i);
				String className = (String) map.get("ClassName");
				if(!"Array".equals(className)){
					String jsonValue = (String) map.get(className);
					Object obj = JSON.parseObject(jsonValue, Class.forName(className));
					parObjs[i] = obj;
				} else {
					List<Map<String,Object>> childList = (List<Map<String, Object>>) map.get(className);
					Object[] childObjs = new Object[childList.size()];
					parseToArray(childObjs,childList);
					parObjs[i] = childObjs;
				}
			}
		}
	}
}
