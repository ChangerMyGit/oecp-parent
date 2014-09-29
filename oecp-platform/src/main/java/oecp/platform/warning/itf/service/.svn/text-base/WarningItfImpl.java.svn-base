package oecp.platform.warning.itf.service;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.dao.DAO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.org.eo.Organization;
import oecp.platform.warning.eo.WarnItfContent;
import oecp.platform.warning.itf.WarningItf;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预警插件模板
 *
 * @author YangTao
 * @date 2012-3-22下午02:43:00
 * @version 1.0
 */
@Service("warningItfImpl")
@Transactional
public class WarningItfImpl implements WarningItf {

	/**
	 * 事件预警插件
	 * 注意：返回的有预警的消息和取消预警的消息；
	 * 		这个返回集合没有为空的情况，要是没有预警，也得返回，只不过每个isWarning是false的
	 */
	@Override
	public List<WarnItfContent> isWarningOnEvent(Object source, String eventName, Organization org,Object... objects) {
		// TODO Auto-generated method stub
		DAO dao = (DAO)SpringContextUtil.getBean("platformDao");
		Session s = dao.getHibernateSession();
		List<WarnItfContent> list = new ArrayList<WarnItfContent>();
		WarnItfContent wif = new WarnItfContent();
		wif.setIsWarning(false);
		wif.setKey("A");
		wif.setMessageContent("请注意：A服务器马上关闭");
		WarnItfContent wif2 = new WarnItfContent();
		wif2.setIsWarning(true);
		wif2.setKey("B");
		wif2.setMessageContent("请注意：B服务器马上关闭");
		list.add(wif);
		list.add(wif2);
		return list;
	}
	/**
	 * 定时器预警插件
	 * 注意：返回的只是预警的消息;
	 * 		集合为空说明没有预警消息，取消以前发得所有预警
	 */
	@Override
	public List<WarnItfContent> isWarningOnTimer() {
		// TODO Auto-generated method stub
		List<WarnItfContent> list = new ArrayList<WarnItfContent>();
		WarnItfContent wif = new WarnItfContent();
		wif.setIsWarning(true);
		wif.setKey("A商品");
		wif.setMessageContent("请注意：A商品库存量低于1000Kg！！！");
		WarnItfContent wif2 = new WarnItfContent();
		wif2.setIsWarning(true);
		wif2.setKey("B商品");
		wif2.setMessageContent("请注意：B商品库存量低于500Kg！！！");
		WarnItfContent wif3 = new WarnItfContent();
		wif3.setIsWarning(true);
		wif3.setKey("C商品");
		wif3.setMessageContent("请注意：C商品库存量低于500Kg！！！");
//		list.add(wif);
		list.add(wif2);
//		list.add(wif3);
		return list;
	}

}
