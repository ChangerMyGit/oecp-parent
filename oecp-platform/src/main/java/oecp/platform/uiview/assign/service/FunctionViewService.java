/**
 * oecp-platform - FunctionViewService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-2下午2:48:53		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.assign.service;

import java.io.IOException;
import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.org.eo.Organization;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.vo.FunctionViewVO;
import oecp.platform.uiview.vo.UIComponentVO;

/**
 * 功能视图的后台服务接口
 * @author slx
 * @date 2011-11-2
 */
public interface FunctionViewService extends BaseService<FunctionView> {
	
	/**
	 * 查询一个function在本公司内的所有视图。
	 * @param functionId
	 * @param orgid
	 * @return
	 */
	public List<FunctionView> getViewsByFunctionID(String functionId , String orgid);
	
	/**
	 * 保存一个视图vo对象
	 * @param vo
	 * @throws BizException
	 */
	public void saveViewVO(FunctionViewVO vo) throws BizException;
	
	/**
	 * 根据视图id查询视图对象。
	 * @param funcViewID
	 * @return
	 * @throws BizException
	 */
	public FunctionViewVO findViewVO(String funcViewID) throws BizException;
	
	/**
	 * 根据视图id返回列表界面vo
	 * @param viewid
	 * @return
	 * @throws BizException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public UIComponentVO getListPanelByViewID(String viewid) throws BizException, IOException, ClassNotFoundException;

	/**
	 * 根据视图id返回表单界面vo
	 * @param viewid
	 * @return
	 * @throws BizException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public UIComponentVO getFormPanelByViewID(String viewid) throws BizException, IOException, ClassNotFoundException;
	
	/**
	 * 克隆一个新的视图
	 * @param viewid
	 * @param viewcode
	 * @param viewname
	 * @param org
	 * @param share
	 * @return
	 * @throws BizException
	 */
	public FunctionViewVO cloneViewByID(String viewid,String viewcode,String viewname,Organization org,boolean share) throws BizException;
	
	/**
	 * 更新视图对应打印模板
	 * @author wangliang
	 * @date 2012-3-29下午4:39:52
	 * @param viewid
	 * @param printTemplateIds
	 * @throws BizException
	 */
	public void saveViewPrintTemplates(String viewid,String printTemplateIds) throws BizException;
}
