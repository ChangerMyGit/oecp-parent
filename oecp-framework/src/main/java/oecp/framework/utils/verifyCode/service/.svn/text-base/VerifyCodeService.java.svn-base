/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
*/                                                                
  

package oecp.framework.utils.verifyCode.service;

import java.util.List;

/** 
 *	验证码生成器接口
 * @author hailang  
 * @date 2011-12-2 下午02:49:38 
 * @version 1.0
 *  
 */
public interface VerifyCodeService {
	/**
	 * 生成验证码字符
	 * @author hailang
	 * @date 2011-12-2下午02:58:37
	 * @return
	 */
	public String generateVerifyCodeKey();
	/**
	 * 根据传递的验证码字符，生成验证码图片
	 * @author hailang
	 * @date 2011-12-2下午02:58:52
	 * @param verifyCodeKey 验证码字符
	 * @return
	 */
	public byte[] generateVerifyCodeImage(String verifyCodeKey);
	/**
	 * 根据传递的验证码字符与验证码值，比对验证码是否正确
	 * @author hailang
	 * @date 2011-12-2下午02:59:46
	 * @param verifyCodeKey
	 * @param verifyCodeValue
	 * @return
	 */
	public boolean validateVerifyCode(String verifyCodeKey,String verifyCodeValue);
	/**
	 * 
	 * @author hailang
	 * @date 2011-12-2下午03:00:50
	 * @return
	 */
	public List<String> getPreDefinedTexts();
	public void setPreDefinedTexts(List<String> preDefinedTexts);
	
	
	public String getVerfiyCodeValue(String verifyCodeKey);
}
