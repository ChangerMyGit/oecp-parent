package oecp.platform.warning.itf;

import java.io.Serializable;
import java.util.List;

import oecp.framework.dao.DAO;
import oecp.platform.org.eo.Organization;
import oecp.platform.warning.eo.WarnItfContent;

/**
 * 预警插件接口
 *
 * @author YangTao
 * @date 2012-3-6下午02:05:10
 * @version 1.0
 */
public interface WarningItf extends Serializable{
	/**
	 * 事件预警插件
	 * 注意：返回的集合：预警的消息和取消预警的消息；
	 * 		这个返回集合没有为空的情况，要是没有预警，也得返回，只不过每个isWarning是false的
	 * @author YangTao
	 * @date 2012-3-12上午10:33:42
	 * @return
	 */
	public List<WarnItfContent> isWarningOnEvent(Object source, String eventName, Organization org,Object... objects);
	/**
	 * 定时器预警插件
	 * 注意：返回的只是预警的消息;
	 * 		集合为空说明没有预警消息，取消以前发得预警
	 * @author YangTao
	 * @date 2012-3-12上午10:34:02
	 * @return
	 */
	public List<WarnItfContent> isWarningOnTimer();
}
