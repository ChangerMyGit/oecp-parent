/**
 * oecp-mdm - BCPKGenerater.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:2014年8月14日 上午10:04:19		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.framework.entity.pkgen;

import java.io.Serializable;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.entity.pkgen.String16PKGenerater;

import org.hibernate.engine.SessionImplementor;

/**
 * 业务组件主键生成器
 * @author songlixiao
 *
 */
public class ExistString16PKGenerater extends String16PKGenerater {

	public Serializable generate(SessionImplementor session, Object obj) {
		BaseEO mdmeo = (BaseEO)obj;
		if(mdmeo.getExistId()!=null){
			if(mdmeo.getExistId().toString().trim().length()!=0)
				return (Serializable)mdmeo.getExistId();
		}
		return super.generate(session, obj);
	}
}
