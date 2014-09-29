package oecp.platform.otask.eo;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.xwork.StringUtils;
import org.json.JSONObject;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.otask.itf.TaskItf;
import oecp.platform.user.eo.User;
import oecp.platform.warning.itf.WarningItf;

/**
 * 
 * @Desc 任务实体 
 * @author yangtao
 * @date 2012-4-6
 *
 */
@Entity
@Table(name="OECP_TASK")
public class OecpTask  extends StringPKEO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//任务名称
	private String name;
	//任务组
	private OecpTaskGroup oecpTaskGroup;
	//任务描述
	private String description;
	//任务才插件
	private String taskitf;
	//插件中的方法名称
	private String methodName;
	//插件中的方法参数
	private String methodParams;
	//创建时间
	private String createTime;
	//是否启动
	private Boolean isStart;
	//启用人
	private User startUser;
	//执行次数
	private String executeNum;
	//定时任务配置
	private OecpTaskTimer oecpTaskTimer;
	
	
	/**
	 * 
	 * @Desc 获取任务插件 
	 * @author yangtao
	 * @date 2012-3-30
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Transient
	public TaskItf getTaskitfClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		TaskItf wif = null;
		if(this.taskitf.indexOf(".")!=-1)
			wif = (TaskItf)SpringContextUtil.getApplicationContext().getBean(Class.forName(this.taskitf));
		else
			wif = (TaskItf)SpringContextUtil.getBean(this.taskitf);
		return wif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne
	public OecpTaskGroup getOecpTaskGroup() {
		return oecpTaskGroup;
	}
	public void setOecpTaskGroup(OecpTaskGroup oecpTaskGroup) {
		this.oecpTaskGroup = oecpTaskGroup;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Boolean getIsStart() {
		return isStart;
	}
	public void setIsStart(Boolean isStart) {
		if(isStart==null)
			isStart = false;
		this.isStart = isStart;
	}
	public String getExecuteNum() {
		return executeNum;
	}
	public void setExecuteNum(String executeNum) {
		this.executeNum = executeNum;
	}
	@OneToOne(cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	public OecpTaskTimer getOecpTaskTimer() {
		return oecpTaskTimer;
	}
	public void setOecpTaskTimer(OecpTaskTimer oecpTaskTimer) {
		this.oecpTaskTimer = oecpTaskTimer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTaskitf() {
		return taskitf;
	}
	public void setTaskitf(String taskitf) {
		this.taskitf = taskitf;
	}
	@ManyToOne
	public User getStartUser() {
		return startUser;
	}
	public void setStartUser(User startUser) {
		this.startUser = startUser;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodParams() {
		return methodParams;
	}
	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}
	@Transient
	public Map getMethodMapParams(){
		Map<Object,Object> map  = null;
		try {
			if(StringUtils.isNotEmpty(this.methodParams)){
				JSONObject o = new JSONObject(this.methodParams);
				map = this.getJsonObject(o);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	//处理JSONObject
	@Transient
	private Map<Object,Object> getJsonObject(JSONObject js){
		Map<Object,Object> map = new HashMap<Object,Object>();
		Iterator set = js.keys();
		while (set.hasNext()) {
			String key = (String) set.next();
			String value = js.getString(key);
			try {
				if(value.contains(":"))
					map.put(key,this.getJsonObject(new JSONObject(value)));
				else
					map.put(key, value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		return map;
	}
}
