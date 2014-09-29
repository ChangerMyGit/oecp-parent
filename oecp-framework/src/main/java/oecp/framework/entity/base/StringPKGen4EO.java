/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 4
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.framework.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 自动生成4为流水主键的EO
 * @author slx
 * @date 2011 5 4 16:13:40
 * @version 1.0
 */
@MappedSuperclass
public abstract class StringPKGen4EO extends BaseEO<String> {

	private static final long serialVersionUID = 1L;
	private String id; 

	@Id
	@Column(name = "pk", length = 20)
	@GeneratedValue(generator = "string4")
	@GenericGenerator(strategy = "oecp.framework.entity.pkgen.String4PKGenerater", name = "string4")
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

}
