/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-18
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.billflow.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QLType;
import oecp.framework.entity.enums.DataType;
import oecp.framework.exception.BizException;
import oecp.framework.exception.DataErrorException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.billflow.eo.DataFieldView;
import oecp.platform.billflow.itf.BillCreaterFromPre;
import oecp.platform.billflow.itf.BillCreaterFromPreCheck;
import oecp.platform.billflow.itf.BillCreaterToNext;
import oecp.platform.billflow.itf.BillPreWriteBacker;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.type.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 单据流配置信息服务类
 * 
 * @author slx
 * @date 2011-6-18 上午09:27:54
 * @version 1.0
 */
@Service("billFlowConfigService")
public class BillFlowConfigServiceImpl extends PlatformBaseServiceImpl<BillFlowConfig> implements BillFlowConfigService {

	// TODO 未实现保存校验
	@Override
	public void create(BillFlowConfig config) throws BizException{
		config.setId(null);
		checkAndCreateField(config);
		getDao().create(config);
	}
	
	@Override
	public List<DataFieldView> getPreDataFieldsByCfgID(String cfgID) throws BizException{
		BillFlowConfig config = this.find(cfgID);
		if(config==null)
			return null;
		config.getPreDatafields().size();
		return config.getPreDatafields();
	}
	
	/**
	 * 覆盖原因：BillFlowConfig内PreDatafields设置为级联更新，为避免误删除PreDatafields而覆盖。
	 */
	@Override
	public void save(BillFlowConfig config) throws BizException {
		if (StringUtils.isEmpty(config.getId())) {
			create(config);
		}else{
			if(config.getPreDatafields() == null){
				BillFlowConfig configdb = this.find(config.getId());
				config.setPreDatafields(configdb.getPreDatafields());
				checkAndCreateField(config);
			}
			getDao().update(config);
		}
	}
	
	@Override
	public void savePreDataFields(List<DataFieldView> dataFields ,String cfgID) throws BizException{
		BillFlowConfig config = this.find(cfgID);
		if(config==null)
			return;
		// 利用级联更新保存PreDatafields
		config.setPreDatafields(dataFields);
		getDao().update(config);
	}
	
	@Override
	public List<BillFlowConfig> getBillFlowConfigByBiz(String biztypeID) {
		return getDao().queryByWhere(BillFlowConfig.class, "o.bizType.id=?", new Object[]{biztypeID});
	}
	
	// 保存和更新配置信息时调用，字段信息有单独的方法维护。
	protected void checkAndCreateField(BillFlowConfig config) throws BizException{
		// 检查配置中的各个类是否是平台给定的接口实现。
		checkConfigClass(config.getBillCreaterFromPreCheck(),BillCreaterFromPreCheck.class,"从前置数据生成本单据的保存校验器");
		checkConfigClass(config.getBillCreaterFromPre(),BillCreaterFromPre.class,"从前置数据生成本单据的生成器");
		checkConfigClass(config.getBillCreaterToNext(),BillCreaterToNext.class,"后置单据生成器");
		checkConfigClass(config.getBillPreWriteBacker(),BillPreWriteBacker.class,"前置单据回写器");
		// 查询语句如果没有，不需要构造字段
		if(config.getQlType()!=null&&StringUtils.isEmpty(config.getPreQuerySQL())){
			throw new BizException("查询语句不能为空");
		}
		// 如果已经有字段信息也不需要构造。
//		if(config.getPreDatafields()!=null && config.getPreDatafields().size() > 0){
//			return ;
//		}
		// 根据查询语句获取到查询结果的字段，形成字段信息，并保存。
		List<DataFieldView> fields = createFieldViews(config);
		if(config.getPreDatafields()!=null){
			List<DataFieldView> hadFields = config.getPreDatafields();
			if(fields!=null){
				for(DataFieldView df : fields){
					for(int i =0;i<hadFields.size();i++){
						if(hadFields.get(i).getName().equals(df.getName())){
							BeanUtils.copyProperties(hadFields.get(i), df);
							break;
						}
					}
				}
			}
		}
		config.setPreDatafields(fields);
		
	}
	/**
	 * 获取前置单据查询dao实例
	 * @author songlixiao
	 * @date 2013-11-29上午11:11:37
	 * @param daoname
	 * @return
	 */
	protected DAO getQueryDao(String daoname){
		return (DAO) SpringContextUtil.getBean(daoname);
	}
	/**
	 * 根据配置中的SQL构造数据字段列表
	 * @author slx
	 * @date 2011-6-20 上午11:12:58
	 * @modifyNote
	 * @param config
	 * @return
	 * @throws DataErrorException 
	 */
	protected List<DataFieldView> createFieldViews(BillFlowConfig config) throws DataErrorException{
		Query q = null;
		List<DataFieldView> fields = null;
		Statement smt = null;
		ResultSet rs = null;
		try{
			if(config.getQlType() == QLType.HQL){// 根据配置创建相应的Query对象
				q = getQueryDao(config.getDaobeanname()).getHibernateSession().createQuery(config.getPreQuerySQL());
				// 获得查询的字段别名和值类型，并以此创建字段视图。
				String[] fieldnames = q.getReturnAliases();
				Type[] fieldtypes = q.getReturnTypes();
				int len = fieldnames.length;
				fields = new ArrayList<DataFieldView>(len);
				for(int i=0 ; i<len ; i++){
					DataFieldView field = new DataFieldView();
					field.setName(fieldnames[i]);
					field.setDispName(fieldnames[i]);
					field.setEditable(false);
					field.setHidden(false);
					// 设置数据类型
					if(fieldtypes[i].getReturnedClass() == java.util.Date.class) {
						field.setDataType(DataType.DATETIME);
						field.setSupplement("yyyy-MM-dd HH:mm:ss");
					}else if(fieldtypes[i].getReturnedClass() == java.sql.Date.class){
						field.setDataType(DataType.DATE);
						field.setSupplement("yyyy-MM-dd");
					}else if(fieldtypes[i].getReturnedClass() == Double.class){
						field.setDataType(DataType.DOUBLE);
						field.setSupplement("8,2");
					}else if(fieldtypes[i].getReturnedClass() == Long.class){
						field.setDataType(DataType.LONG);
					}else if(fieldtypes[i].getReturnedClass() == Integer.class){
						field.setDataType(DataType.INTEGER);
					}else if(fieldtypes[i].getReturnedClass().getSuperclass() == java.lang.Enum.class){
						field.setDataType(DataType.ENUM);
					}else{
						field.setDataType(DataType.STRING);
					}
					
					fields.add(field);
				}
			}else if(config.getQlType() == QLType.SQL){//纯SQL语句只能通过JDBC来实现了
				Connection connection = getQueryDao(config.getDaobeanname()).getHibernateSession().connection();
				smt = connection.createStatement();
				rs = smt.executeQuery(config.getPreQuerySQL());
				ResultSetMetaData md = rs.getMetaData();
				int len = md.getColumnCount();
				fields = new ArrayList<DataFieldView>(len);
				for(int i=1;i<=len;i++){
					DataFieldView field = new DataFieldView();
					field.setName(md.getColumnName(i));
					field.setDispName(md.getColumnName(i));
					field.setEditable(false);
					field.setHidden(false);
					String filedType = md.getColumnTypeName(i);
					// 设置数据类型
					if(filedType.equals("CHAR") || filedType.equals("VARCHAR2") || filedType.equals("NCHAR") //string
							|| filedType.equals("NVARCHAR2") || filedType.equals("ROWID")//string
							|| filedType.equals("VARCHAR")){//特别针对sqlserver的VARCHAR类型
						field.setDataType(DataType.STRING);
					}else if(filedType.equals("FLOAT")){//float
						field.setDataType(DataType.DOUBLE);
					}else if(filedType.equals("DATE")){//date
						field.setDataType(DataType.DATETIME);
						field.setSupplement("yyyy-MM-dd HH:mm:ss");
					}else if(filedType.equals("NUMERIC")){//针对ｓｑｌ中的NUMERIC类型
						field.setDataType(DataType.DOUBLE);
						field.setSupplement("8,2");
					}else if(filedType.equals("NUMBER")){//number
						field.setDataType(DataType.INTEGER);
					}else{//string
						field.setDataType(DataType.STRING);
					}
					
					fields.add(field);
				}
			}
		}catch (Exception e) {
			throw new DataErrorException("查询语句配置错误：" + e.getMessage());
		}finally{
			try {
				if(smt!=null)
					smt.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fields;
	}
	
	/**
	 * 检查一个类是否是指定接口的实现，如果不是则抛出异常。
	 * @author slx
	 * @date 2011-6-20 上午11:50:05
	 * @modifyNote
	 * @param classname
	 * @param interfase
	 * @param classZHName
	 * @throws BizException
	 */
	private void checkConfigClass(String classname , Class interfase ,String classZHName) throws BizException{
		if(StringUtils.isNotEmpty(classname)){
			try {
				Class clazz = Class.forName(classname);
				if(!interfase.isAssignableFrom(clazz)){
					throw new BizException(classZHName.concat("，必须实现[").concat(interfase.getName()).concat("]接口"));
				}
			} catch (ClassNotFoundException e) {
				throw new BizException("找不到指定的类：" +  classname);
			}
		}
	}
}
