package oecp.framework.entity.pkgen;

import java.io.Serializable;

import org.hibernate.engine.SessionImplementor;

/**
 * 4位字符主键生成器
 * 
 * @author slx
 * @date 2011 5 4 10:49:31
 * @version 1.0
 */
public class String4PKGenerater extends LongPKGenerater {

	public Serializable generate(SessionImplementor session, Object obj) {
		Serializable id = super.generate(session, obj);
		return format((Long)id);
	}
	
	protected String format(Long longValue) {
		String formatted = Long.toHexString( longValue );
		StringBuffer buf = new StringBuffer( "0000" );
		buf.replace( 4 - formatted.length(), 4, formatted );
		return buf.toString();
	}
}
