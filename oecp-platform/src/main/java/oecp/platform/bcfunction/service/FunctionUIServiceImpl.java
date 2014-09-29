package oecp.platform.bcfunction.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.OECPValidator;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.eo.UIElement;

import org.springframework.stereotype.Service;

@Service("functionUIService")
public class FunctionUIServiceImpl extends PlatformBaseServiceImpl<FunctionUI>
		implements FunctionUIService {
	@Resource(name = "functionValidator")
	private OECPValidator validator;

	@Resource(name = "uiElementService")
	private UIElementService uieService;

	@Override
	public QueryResult<FunctionUI> getFunctionUIs(String function_id, int start,
			int limit) {
		return getDao().getScrollData(FunctionUI.class, start, limit,
				"o.function.id=?", new Object[] { function_id },
				new LinkedHashMap<String, String>() {
					{
						put("code", "asc");
					}
				});
	}

	@Override
	public void setDefault(FunctionUI functionui) throws BizException {
		if (functionui.getIsDefault() == null || !functionui.getIsDefault()) {
			functionui.setIsDefault(true);
		}
		List<FunctionUI> list = null;
		// 获取默认模板
		if (functionui.getId() == null) {
			list = getDao().queryByWhere(
					FunctionUI.class,
					"o.isDefault=? AND o.function.id=?",
					new Object[] { new Boolean(true),
							functionui.getFunction().getId() });
		} else {
			list = getDao().queryByWhere(
					FunctionUI.class,
					"o.isDefault=? AND o.function.id=? AND o.id<>?",
					new Object[] { new Boolean(true),
							functionui.getFunction().getId(),
							functionui.getId() });
		}
		if (list != null && list.size() > 0) {
			for (FunctionUI ui : list) {
				ui.setIsDefault(Boolean.FALSE);
				getDao().update(ui);
			}
		}
		getDao().update(functionui);
	}

	@Override
	public List<FunctionUI> getFunctionUIs(String function_id) {
		return getDao().queryByWhere(FunctionUI.class, "o.function.id=?",
				new Object[] { function_id });
	}

	@Override
	public void delete(Serializable[] ids) throws BizException {
		validator.validator("deleteUIs", ids, getDao());
		for (int i = 0; i < ids.length; i++) {
			getDao().getHibernateSession().delete(super.find(ids[i]));
		}
	}

	/**
	 * 覆盖父类，解决删行问题。
	 */
	@Override
	public void save(FunctionUI funui) throws BizException {
		if (funui.getId() == null) {
			this.create(funui);
		} else {
			FunctionUI old = this.find(funui.getId());
			removeOldEles(old.getElements(), funui.getElements());
			this.update(funui);
		}
	}

	private void removeOldEles(List<UIElement> oldes, List<UIElement> newes)
			throws BizException {
		for (int i = 0; i < oldes.size(); i++) {
			UIElement olde = oldes.get(i);
			boolean td = true;
			for (int j = 0; j < newes.size(); j++) {
				UIElement newe = newes.get(j);
				if (olde.equalsPK(newe)) {
					td = false;
					break;
				}
			}
			if (td) {
				uieService.delete(olde.getId());
			}
		}
	}
}
