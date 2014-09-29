/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.otask.itf.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.otask.itf.BaseOecpTask;
import oecp.platform.otask.itf.TaskItf;
import oecp.platform.otask.service.OecpTaskService;

/** 
 * 定时取删除任务的日志，防止数据量过大影响系统效率
 * @author Administrator  
 * @date 2012-6-27 下午02:40:49 
 * @version 1.0
 *  
 */
@Service("deleteLogTaskItfImpl")
@Transactional
public class DeleteLogTaskItfImpl  extends BaseOecpTask implements TaskItf {

	/**
	 * 每一个月删除一次任务日志
	 */
	@Override
	public Object execute(Map params) throws BizException {
		// TODO Auto-generated method stub
		OecpTaskService oecpTaskService = (OecpTaskService)SpringContextUtil.getBean("oecpTaskService");
		oecpTaskService.deleteTaskLog();
		return null;
	}
}
