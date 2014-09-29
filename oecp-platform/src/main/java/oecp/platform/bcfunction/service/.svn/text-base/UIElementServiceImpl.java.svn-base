package oecp.platform.bcfunction.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.eo.UIElement;
/**
 * @author wangliang
 * @date 2011-4-12
 */
@Service("uiElementService")
public class UIElementServiceImpl extends PlatformBaseServiceImpl<UIElement> implements
		UIElementService {

	@Override
	public List<UIElement> getElements(String functionUI_pk) {
		return getDao().queryByWhere(UIElement.class, "o.functionUI.id=?", new Object[]{functionUI_pk});
	}

}
