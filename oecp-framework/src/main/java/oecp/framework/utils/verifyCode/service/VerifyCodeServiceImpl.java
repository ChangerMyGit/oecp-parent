/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
*/                                                                
  

package oecp.framework.utils.verifyCode.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import oecp.framework.util.RandomGenerator;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/** 
 * 验证码实现类
 * @author hailang  
 * @date 2011-12-2 下午03:16:54 
 * @version 1.0
 *  
 */
@Service("verifyCodeService")
public class VerifyCodeServiceImpl implements VerifyCodeService{
	
	private DefaultKaptcha defaultKaptcha;//默认的Google验证码生成器
	private  Map<String, String> verifyCodeMap=new HashMap<String,String>();//存放验证码主键与验证码字符串
	private List<String> preDefinedTexts;//用户预定义的内容
	private int textCount=0;
	
	/**
	 * 初始化验证码生成器
	 * @author hailang
	 * @date 2011-12-2下午03:38:50
	 */
	@PostConstruct
	public void initGenerator(){
		defaultKaptcha=new DefaultKaptcha();
		defaultKaptcha.setConfig(new Config(new Properties()));
	}

	/* (non-Javadoc)
	 * @see cn.doconline.core.utils.VerifyCodeService#generateVerifyCode()
	 */
	@Override
	public String generateVerifyCodeKey() {
		String key=RandomGenerator.getRandomString();//生成验证码主键(无任何意义，只是与验证码字符串做匹配);
		String value=getVerifyCodeText();//生成验证码字符串
		verifyCodeMap.put(key, value);
		return key;
	}

	/**
	 * 生成验证码字符串
	 * @author hailang
	 * @date 2011-12-2下午03:42:19
	 * @return
	 */
	private String getVerifyCodeText() {
		if(preDefinedTexts!=null && !preDefinedTexts.isEmpty()){
			String text=preDefinedTexts.get(textCount);
			textCount=(textCount+1)%preDefinedTexts.size();
			return text;
		}else{
			return defaultKaptcha.createText();//默认的验证码生成器生成验证码字符串
		}
	}

	/* (non-Javadoc)
	 * @see cn.doconline.core.utils.VerifyCodeService#generateVerifyCodeImage(java.lang.String)
	 */
	@Override
	public byte[] generateVerifyCodeImage(String verifyCodeKey) {
		String text=verifyCodeMap.get(verifyCodeKey);
		if(text==null){
			throw new RuntimeException("验证码不存在");
		}
		BufferedImage image=defaultKaptcha.createImage(text);
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", bos);
		} catch (Exception e) {
			throw new RuntimeException("验证码生成错误！");
		}
		return bos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see cn.doconline.core.utils.VerifyCodeService#getPreDefinedTexts()
	 */
	@Override
	public List<String> getPreDefinedTexts() {
		return preDefinedTexts;
	}

	/* (non-Javadoc)
	 * @see cn.doconline.core.utils.VerifyCodeService#setPreDefinedTexts(java.util.List)
	 */
	@Override
	public void setPreDefinedTexts(List<String> preDefinedTexts) {
	    this.preDefinedTexts=preDefinedTexts;
		
	}

	/* (non-Javadoc)
	 * @see cn.doconline.core.utils.VerifyCodeService#validateVerifyCode(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validateVerifyCode(String verifyCodeKey,
			String verifyCodeValue) {
		String text=verifyCodeMap.get(verifyCodeKey);
		if(text==null){
			throw new RuntimeException("验证码不存在");
		}
		if(StringUtils.equals(text, verifyCodeValue)){
			verifyCodeMap.remove(verifyCodeKey);
			return true;
		}
		return false;
	}
	public String getVerfiyCodeValue(String verifyCodeKey){
		return verifyCodeMap.get(verifyCodeKey);
	}
}
