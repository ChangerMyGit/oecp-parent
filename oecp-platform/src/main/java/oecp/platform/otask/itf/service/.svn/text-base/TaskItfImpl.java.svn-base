package oecp.platform.otask.itf.service;

import java.util.Map;

import oecp.framework.exception.BizException;
import oecp.platform.otask.itf.BaseOecpTask;
import oecp.platform.otask.itf.TaskItf;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @Desc  测试任务
 * @author yangtao
 * @date 2012-4-1
 *
 */
@Service("taskItfImpl")
@Transactional
public class TaskItfImpl extends BaseOecpTask implements TaskItf {

	@Override
	public Object execute(Map params) throws BizException{
		// TODO Auto-generated method stub
		this.getCurrentLog().setContent("ww");
		System.out.println("++++++++++++++bbbb"+this.getAboveLog());
		System.out.println("++++++++++++++AAAA");
//		if(1==1)
//			throw new BizException("aaaa");
		return null;
	}
	
	public void getss(Map params) throws BizException{
		System.out.println("bbbbb");
	}

}
