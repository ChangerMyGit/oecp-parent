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

package oecp.platform.rpt.setting.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.base.service.OECPValidator;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.rpt.setting.eo.Report;
import oecp.platform.uiview.eo.UIComponent;
import oecp.platform.uiview.utils.UIComponentObjUtils;
import oecp.platform.uiview.vo.UIComponentVO;

import org.springframework.stereotype.Service;

/**
 * 报表设置工具类
 * 
 * @author slx
 * @date 2012-4-26 下午4:25:14
 * @version 1.0
 */
@Service("reportSettingService")
public class ReportSettingServiceImpl extends PlatformBaseServiceImpl<Report> implements ReportSettingService {

	@Resource(name="reportSettingValidator")
	private OECPValidator validator;
	
	@Override
	public String[] getDaoNames() {
		return SpringContextUtil.getApplicationContext().getBeanNamesForType(DAO.class);
	}

	@Override
	public QueryResult<Report> queryReports(List<QueryCondition> queryConditions,int start, int limit) {
		return getDao().getScrollData(Report.class, start, limit, queryConditions, new LinkedHashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("code", "ASC");
			}
		});
	}
	
	@Override
	public void save(Report report) throws BizException {
		validator.validator("save", report, getDao());
		if(report.getView()!=null){
			UIComponentVO uivo = report.getView().getMainuivo();
			// VO 转 EO 后保存
			report.getView().setMainui(UIComponentObjUtils.transfer2UIEO(uivo));
			report.getView().setMainuivo(null);
			uivo = null;
		}
		super.save(report);
	}
	
	@Override
	public Report find_full(Serializable id) throws BizException {
		Report report = super.find_full(id);
		// EO 转 VO 后返回
		if(report.getView()!=null){
			UIComponent uieo = report.getView().getMainui();
			report.getView().setMainuivo(UIComponentObjUtils.transfer2UIVO(uieo));
		}
		if(report.getQueryscheme()!=null){
			report.getQueryscheme().getFixedconditions().size();
			report.getQueryscheme().getCommonconditions().size();
			report.getQueryscheme().getOtherconditions().size();
		}
		return report;
	}

	public void setValidator(OECPValidator validator) {
		this.validator = validator;
	}
}
