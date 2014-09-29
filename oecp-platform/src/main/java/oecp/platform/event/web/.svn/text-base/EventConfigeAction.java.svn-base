/**
 * oecp-platform - EventConfigAction.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:上午9:46:14		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.Constants;
import oecp.framework.web.JsonResult;
import oecp.platform.event.eo.EventConfige;
import oecp.platform.event.eo.EventHandleErrorLog;
import oecp.platform.event.service.EventConfigeService;
import oecp.platform.event.service.EventErrorLogService;
import oecp.platform.web.BasePlatformAction;
import oecp.platform.web.annotation.EventDescription;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 监听器配置Action
 * 
 * @author luanyoubo
 * @date 2013年12月16日 上午9:46:14
 * @version 1.0
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/eventConfige")
public class EventConfigeAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	@Resource
    private EventConfigeService eventConfigeService;
	
	@Resource
	private EventErrorLogService eventErrorLogService;
	
	// 参数ID数组
	private String[] ids;
	
	// 同步/异步参数
	private String synParam;
	
	/**
	 * 获取所有事件监听
	 * @author luanyoubo
	 * @date 2013年12月16日上午9:49:20
	 * @return
	 * @throws BizException
	 */
	@Action("list")
	public String listEvents() throws BizException {
		QueryResult<EventConfige> qr = eventConfigeService.getListeners(start,limit);
		List<EventConfige> listenersList = qr.getResultlist();
		for (EventConfige confige : listenersList) {
			if (Constants.TRUE.equals(confige.getStartFlag()))
				confige.setStartFlag("启动");
			else
				confige.setStartFlag("未启动");
			if (Constants.TRUE.equals(confige.getSynFlag()))
				confige.setSynFlag("同步");
			else
				confige.setSynFlag("异步");
		}
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(),listenersList);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 启动选中的监听器
	 * @author luanyoubo
	 * @date 2013年12月16日下午3:34:13
	 * @return
	 * @throws BizException
	 */
	@Action("start")
    @EventDescription("启动事件")
	public String startEvents() throws BizException {
		for(String id : ids){
			eventConfigeService.start(id);
		}
		setJsonString("{success : true,msg : '启动成功'}");
		return SUCCESS;
	}
	
	
	/**
	 * 关闭选中的监听器
	 * @author luanyoubo
	 * @date 2013年12月16日下午3:34:13
	 * @return
	 * @throws BizException
	 */
	@Action("stop")
	public String closeEvents() throws BizException {
		for(String id : ids){
			eventConfigeService.close(id);
		}
		setJsonString("{success : true,msg : '停用成功'}");
		return SUCCESS;
	}
	
	/**
	 * 保存同步异步状态
	 * @author luanyoubo
	 * @date 2013年12月17日上午8:54:16
	 * @return
	 * @throws BizException
	 */
	@Action("synConfige")
	public String synConfige() throws BizException {
		for(String id : ids){
			EventConfige confige = eventConfigeService.find(id);
			confige.setSynFlag(synParam);
			eventConfigeService.save(confige);
		}
		setJsonString("{success : true,msg : '保存成功'}");
		return SUCCESS;
	}
	
	/**
	 * 重新执行
	 * @author luanyoubo
	 * @date 2013年12月19日下午3:39:56
	 * @return
	 * @throws BizException
	 */
	@Action("restartEvent")
	public String restartEvent() throws Exception {
		for(String id : ids){
			eventErrorLogService.restartEvent(id);
		}
		setJsonString("{success : true,msg : '执行成功'}");
		return SUCCESS;
	}
	
	/**
	 * 返回错误日志
	 * @author luanyoubo
	 * @date 2013年12月19日下午3:49:30
	 * @return
	 * @throws BizException
	 */
	@Action("errors")
	public String getEventErrors() throws BizException {
		QueryResult<EventHandleErrorLog> qr = eventErrorLogService.getEventErrorLogs(start,limit);
		List<EventHandleErrorLog> errorList = qr.getResultlist();
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(),errorList);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getSynParam() {
		return synParam;
	}

	public void setSynParam(String synParam) {
		this.synParam = synParam;
	}
	
}
