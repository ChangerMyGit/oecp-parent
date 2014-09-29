package oecp.framework.entity.pkgen;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import oecp.framework.entity.base.BaseEO;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.exception.JDBCExceptionHelper;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IncrementGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.mapping.Table;
import org.hibernate.type.Type;
import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Long型主键生成器.
 * 生成原理:
 *   先取此表中最大的数字,放入以表名为key的map中.最大值+1返回.以后每次取map中的最大值+1.
 * 注意: 集群时,由于使用了静态map,所以不同机器上生成的key会重复.
 * 
 * @author slx
 * @version 1.0
 */
public class LongPKGenerater implements IdentifierGenerator, Configurable {

	/** 存放pk的Map <表名,最大主键值> **/
	private static HashMap<String,Long> lastPKMap = new HashMap<String, Long>();
	/** 存放pk的Map <表名,sql> **/
	private static HashMap<String,String> sqlMap = new HashMap<String, String>();
	private static final Logger log = LoggerFactory.getLogger(IncrementGenerator.class);
	
	protected static HashMap<String,Long> getLastPKMap() {
		return lastPKMap;
	}
	
	@Override
	// FIXME 集群时会有问题
	public synchronized Serializable generate(SessionImplementor session, Object eo)
			throws HibernateException {
		return generatePK(session,eo);
	}
	
	private synchronized static Serializable generatePK(SessionImplementor session, Object eo)
	throws HibernateException {
		String table_name = ((BaseEO)eo).getTableName();
//		Long lastpk = lastPKMap.get(table_name);
//		if(lastpk == null){
		Long lastpk = getMaxPk(session,table_name);
//		}
		
		lastpk = lastpk + 1;
//		lastPKMap.put(table_name, lastpk);
		return lastpk;
	}

		

	@Override
	public void configure(Type type, Properties params, Dialect dialect)
			throws MappingException {
		String tableList = params.getProperty("tables");
		if (tableList==null) tableList = params.getProperty(PersistentIdentifierGenerator.TABLES);
		String[] tables = StringHelper.split(", ", tableList);
		String column = params.getProperty("column");
		if (column==null) column = params.getProperty(PersistentIdentifierGenerator.PK);
		String schema = params.getProperty(PersistentIdentifierGenerator.SCHEMA);
		String catalog = params.getProperty(PersistentIdentifierGenerator.CATALOG);
		

		StringBuffer buf = new StringBuffer();
		for ( int i=0; i<tables.length; i++ ) {
			if (tables.length>1) {
				buf.append("select ").append(column).append(" from ");
			}
			buf.append( Table.qualify( catalog, schema, tables[i] ) );
			if ( i<tables.length-1) buf.append(" union ");
		}
		if (tables.length>1) {
			buf.insert(0, "( ").append(" ) ids_");
			column = "ids_." + column;
		}
		
		String sql = "select max(" + column + ") from " + buf.toString();
		sqlMap.put(params.getProperty("target_table"), sql);
	}
	
	private static Long getMaxPk( SessionImplementor session ,String table_name){

		String sql = sqlMap.get(table_name);
		log.debug("fetching initial value: " + sql);
		
		Long maxpk = 0L;
		try {
			PreparedStatement st = session.getBatcher().prepareSelectStatement(sql);
			try {
				ResultSet rs = st.executeQuery();
				try {
					if ( rs.next() ) {
						maxpk = rs.getLong(1);
					}
//					sql=null;
					log.debug("first free id: " + maxpk);
				}
				finally {
					rs.close();
				}
			}
			finally {
				session.getBatcher().closeStatement(st);
			}
			
		}
		catch (SQLException sqle) {
			throw JDBCExceptionHelper.convert(
					session.getFactory().getSQLExceptionConverter(),
					sqle,
					"could not fetch initial value for increment generator",
					sql
				);
		}
		
		return maxpk;
	}

}