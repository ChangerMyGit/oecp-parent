/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2009-6-29
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

package oecp.framework.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 基础档案实体，所有档案型的实体可以继承该实体，比如组织结构，产品信息等等。
 * @deprecated 不建议使用该数值型主键，建议使用字符串型主键
 * @author yongtree
 * @date 2009-6-29 下午01:58:01
 * @version 1.0
 */
@MappedSuperclass
public abstract class NumberPKEO extends BaseEO<Long> {

	private static final long serialVersionUID = -6560656006561154616L;
	private Long id;

	@Id
	@Column(name = "pk")
	@GenericGenerator(strategy = "oecp.framework.entity.pkgen.LongPKGenerater", name = "longGenerator")
	@GeneratedValue(generator = "longGenerator")
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long pk) {
		this.id = pk;
	}
}
