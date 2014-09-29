package oecp.platform.base.dao;

import javax.annotation.Resource;

import oecp.framework.dao.BaseHibernateDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component("platformDao") 
public class PlatformDao extends BaseHibernateDao {

	@Resource(name="platformSessionFactory")
	private SessionFactory sessionFactory ;
	
	@Override
	public Session getHibernateSession() {
		return sessionFactory.getCurrentSession();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
