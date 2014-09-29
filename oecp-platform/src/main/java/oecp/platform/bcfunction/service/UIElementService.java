package oecp.platform.bcfunction.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.framework.service.BaseService;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.eo.UIElement;

/**
 * 界面元素服务类
 * @author wangliang
 * @date 2011-4-11
 */
public interface UIElementService extends BaseService<UIElement> {
	/**
	 * 根据功能界面获取界面元素
	 * @param functionui
	 * @return
	 */
	public List<UIElement> getElements(String functionUI_pk);
}
