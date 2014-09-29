/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-20
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.framework.dao;


import oecp.framework.vo.base.DataVO;

import org.hibernate.transform.BasicTransformerAdapter;

/**
 * Hibernate查询结果转换器，实现将结果转换为DataVO。
 * @author slx
 * @date 2011-6-20 下午01:48:06
 * @version 1.0
 */
public class AliasToDataVOTransformer extends BasicTransformerAdapter {
	private static final long serialVersionUID = 1L;
	private Class<? extends DataVO> clazz;

	private AliasToDataVOTransformer() {
	}
	
	public static AliasToDataVOTransformer newInstance(Class<? extends DataVO> clazz){
		AliasToDataVOTransformer tr = new AliasToDataVOTransformer();
		tr.clazz = clazz;
		return tr;
	}
	
	public Object transformTuple(Object[] tuple, String[] aliases) {
		try {
			DataVO vo = clazz.newInstance();
			for ( int i=0; i<tuple.length; i++ ) {
				String alias = aliases[i];
				if ( alias!=null ) {
					if(alias.equals(alias.toUpperCase())){// 别名整体为大写时,转为小写
						alias = alias.toLowerCase();
					}
					vo.setValue( alias, tuple[i] );
				}
			}
			return vo;
		} catch (Exception e) {
			throw new RuntimeException("查询结果转换为DataVO失败", e);
		} 
	}
}
