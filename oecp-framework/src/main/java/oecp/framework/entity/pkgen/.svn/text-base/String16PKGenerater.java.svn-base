/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-11-3
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */    

package oecp.framework.entity.pkgen;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;


/**
 * 16位主键生生成器.继承自hibernate的UUID生成,去掉了ip地址和一个毫秒数
 * @author slx
 *
 */
public class String16PKGenerater extends UUIDHexGenerator {
	
	public Serializable generate(SessionImplementor session, Object obj) {
		return new StringBuffer(24)
		.append( getJVM_last2() )
		.append( getHmTime() )
//		.append( format( getNanoTime() ) )
		.append( format( getCount() ) )
		.toString();
	}
	
//	protected short getNanoTime() {
//		
//		return (short)(System.nanoTime()>>>32);
//	}
	
	protected String getJVM_last2() {
		return format( getJVM() ).substring(5);
	}
	
	protected String getHmTime() {
		long l =  System.currentTimeMillis();
		return Long.toString(l,32);
	}
	
	
	public static void main( String[] args ) throws Exception {
		Properties props = new Properties();
		props.setProperty("separator", "");
		IdentifierGenerator gen = new String16PKGenerater();
//		( (Configurable) gen ).configure(Hibernate.STRING, props, null);
		IdentifierGenerator gen2 = new String16PKGenerater();
//		( (Configurable) gen2 ).configure(Hibernate.STRING, props, null);

		for ( int i=0; i<10; i++) {
			String id = (String) gen.generate(null, null);
			System.out.println(id +  " " + id.length());
			String id2 = (String) gen2.generate(null, null);
			System.out.println(id2);
		}
		
	}

}
