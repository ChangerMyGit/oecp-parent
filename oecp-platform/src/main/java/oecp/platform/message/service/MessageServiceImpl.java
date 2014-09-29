package oecp.platform.message.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.message.eo.Message;
import oecp.platform.message.eo.MessageContent;
import oecp.platform.user.eo.User;

/**
 * 消息服务实现类
 * @author lintao
 * @date 2011-6-30 上午09:35:15
 * @version 1.0
 * 
 */

@Service("messageService")
public class MessageServiceImpl extends PlatformBaseServiceImpl<Message> implements
		MessageService {

	@Override
	public List<MessageContent> findMessageByUserId(String userId,String state) {
		List<Message> meList = getDao().queryByWhere(Message.class, "o.user.id=? and o.state=?", new Object[]{userId,state});
		List<MessageContent> mcList = new ArrayList<MessageContent>(); 
		for(Message m : meList){
			mcList.add(m.getContent());
		}
		return mcList;
	}

	@Override
	public long getMessageCount(String userId,String state) {
		String sql = "select count(*) from Message m where m.user.id=? and m.state=?";
		Query query = getDao().getHibernateSession().createQuery(sql);
		query.setString(0, userId);
		query.setString(1, state);
		List<Long> list = query.list();
		return list.get(0);
	}
	

	@Override
	public void sendMessage(String title, String content, String userId) throws BizException {
		MessageContent mc = new MessageContent();
		mc.setTitle(title);
		mc.setContent(content);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		mc.setCreatetime(sdf.format(new Date()));
		getDao().create(mc);
		Message m = new Message();
		m.setState("0");
		User user = new User();
		user.setId(userId);
		m.setUser(user);
		m.setContent(mc);
		create(m);
	}
	
	@Override
	public void sendMessage(String title, String content, String billId,
			String openPath, boolean frame, String userId) throws BizException {
		MessageContent mc = new MessageContent();
		mc.setTitle(title);
		mc.setContent(content);
		mc.setBillId(billId);
		mc.setOpenPath(openPath);
		mc.setFrame(frame);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		mc.setCreatetime(sdf.format(new Date()));
		getDao().create(mc);
		Message m = new Message();
		m.setState("0");
		User user = new User();
		user.setId(userId);
		m.setUser(user);
		m.setContent(mc);
		create(m);
	}

	@Override
	public void updateMessageState(String contentId, String userId)
			throws BizException {
		Message m = getDao().findByWhere(Message.class, "o.user.id=? and o.content.id=?", new Object[]{userId,contentId});
		m.setState("1");
		update(m);
	}








}
