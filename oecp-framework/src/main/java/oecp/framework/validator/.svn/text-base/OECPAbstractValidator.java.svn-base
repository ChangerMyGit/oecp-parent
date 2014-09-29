/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                		*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 4 13
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.framework.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import oecp.framework.dao.DAO;
import oecp.framework.exception.DataErrorException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResult;
import org.apache.commons.validator.ValidatorResults;
import org.springframework.validation.Errors;
import org.xml.sax.SAXException;

/**
 * 数据校验抽象类
 * <br> 
 * <br> 实现此类请提供一个单参数的构造函数和一个双参数的构造函数,以便Spring使用构造函数注入使用.实现如下:
 * <br> public OECPValidator(String customerRuleXml) throws IOException, SAXException {
 * <br> 	setValidatorRuleXML(customerRuleXml);
 * <br> }
 * <br> 
 * <br> public OECPValidator(String customerRuleXml,String messageResource) throws IOException, SAXException {
 * <br> 	setMessageResource(messageResource);
 * <br> 	setValidatorRuleXML(customerRuleXml);
 * <br> }
 * <br> 
 * <br> 此构造函数将会在找不到文件或者文件格式错误情况下抛出异常.
 * <br> 因为资源消息可以默认使用全局设置,所以有可能没有,但校验规则是必须有的.
 * <br> 
 * <br> 另外,使用构造函数注入时,类必须有默认构造函数,即无参构造函数.虽然不会调用,但必须存在.此为Spring要求.
 * <br> 
 * <br> 使用spring配置校验器bean时,请不要忘记配置<b> init-method=init</b>
 * <br> 
 * @author slx
 * @date 2011 4 13 14:52:19
 * @version 1.0
 */
public abstract class OECPAbstractValidator {

	/** 提示信息资源工具 **/
	private ValidatorResourceUtils resourceUtils = new ValidatorResourceUtils();

	private ValidatorResources validatorRules = null;

	private static String springRuleXml = "oecp/framework/validator/spring-validator-rules.xml";
	private String customerRuleXml = null;
	
	private static String springResource = "oecp.framework.validator.validatortestResources";
	private String messageResource = null;

	public void init() throws Exception {
		buildRules();
		buildRresources();
	}
	

	/**
	 * 设置提示信息资源名称. 格式如:oecp.platform.test.validatortestResources
	 * 
	 * @author slx
	 * @date 2011 4 13 15:31:35
	 * @modifyNote
	 * @param messageResource
	 */
	protected void setMessageResource(String resourceBaseName) {
		messageResource = resourceBaseName;
	}

	/**
	 * 设置校验规则文件位置
	 * 
	 * @author slx
	 * @date 2011 4 13 16:21:51
	 * @modifyNote
	 * @param xmlPath
	 * 		规则文件路径(相对于classpath的)
	 * @throws SAXException 
	 * 		文件格式错误时抛出
	 * @throws IOException 
	 * 		文件找不到或打不开
	 */
	protected void setValidatorRuleXML(String xmlPath) throws IOException, SAXException {
		this.customerRuleXml = xmlPath;
	}
	/**
	 * 根据制定的form规则校验bean
	 * 
	 * @author slx
	 * @date 2011 4 13 16:31:32
	 * @modifyNote
	 * @param formName
	 *            校验规则配置使用的form名称
	 * @param bean
	 *            需要校验的bean
	 * @throws DataErrorException
	 *             校验存在错误则抛出,错误信息在异常中.
	 */
	public void validator(String formName, Object bean ) throws DataErrorException {
		validator(formName, bean , null);
	}
	/**
	 * 根据制定的form规则校验bean
	 * 
	 * @author slx
	 * @date 2011 4 13 16:31:32
	 * @modifyNote
	 * @param formName
	 *            校验规则配置使用的form名称
	 * @param bean
	 *            需要校验的bean
	 * @param dao
	 *            操作数据库的dao对象
	 * @throws DataErrorException
	 *             校验存在错误则抛出,错误信息在异常中.
	 */
	public void validator(String formName, Object bean ,DAO dao) throws DataErrorException {
		// 使用Spring的校验规则器需要传入一个 Errors对象
		Errors errors = new org.springframework.validation.MapBindingResult(new HashMap(), formName);
		// 创建Validator对象,制定要使用的form规则
		Validator validator = new Validator(validatorRules, formName);
		// 制定要校验的bean
		validator.setParameter(Validator.BEAN_PARAM, bean);
		// 传入spring需要的参数
		validator.setParameter("org.springframework.validation.Errors", errors);
		if(dao != null){
			validator.setParameter("oecp.framework.dao.DAO", dao);
		}
		
		ValidatorResults results = null;
		try {
			// 执行校验
			results = validator.validate();
		} catch (ValidatorException e) {
			// 校验异常则抛出一个运行异常,因为校验异常通常是因为框架使用不当引起的.
			throw new RuntimeException(" Validator Framework cann't run. Improper used ! :".concat(e.getMessage()));
		}
		DataErrorException error = checkValidatorResults(results);

		if (error != null)
			throw error;

	}
	
	/**
	 * 构建校验规则
	 * 
	 * @author slx
	 * @date 2011 4 14 16:43:29
	 * @modifyNote
	 * @throws IOException
	 *             文件没找到将抛出异常
	 * @throws SAXException
	 *             文件格式有错误时抛出
	 */
	private void buildRules() throws IOException, SAXException{
		List<InputStream> rulePaths = new ArrayList<InputStream>();
		
		File _f1 = new File(getClassPath()+(springRuleXml));
		if(_f1.exists()){//判断本地文件存在
			InputStream _is1 = new FileInputStream(_f1);
			rulePaths.add(_is1);
		}else{//读取jar包内的资源文件
			rulePaths.add(this.getClass().getResourceAsStream("/"+springRuleXml));
		}
		if (StringUtils.isNotEmpty(getGlobalRulesXMLPath())){
			File _f2 = new File(getClassPath().concat(getGlobalRulesXMLPath()));
			if(_f2.exists()){
				InputStream _is2 = new FileInputStream(_f2);
				rulePaths.add(_is2);
			}else{
				rulePaths.add(this.getClass().getResourceAsStream("/"+getGlobalRulesXMLPath()));
			}
		}
		if(customerRuleXml!=null){
			File _f3 = new File(getClassPath().concat(customerRuleXml));
			if(_f3.exists()){
				InputStream _is3 = new FileInputStream(_f3);
				rulePaths.add(_is3);
			}else{
				rulePaths.add(this.getClass().getResourceAsStream("/"+customerRuleXml));
			}
		}
		
		InputStream[] paths = new InputStream[0];
		paths =	rulePaths.toArray(paths);
		validatorRules = new ValidatorResources(paths);
	}
	
	private void buildRresources() {
		resourceUtils.loadResource(springResource, getLocaleSetting());
		if (StringUtils.isNotEmpty(getGlobalExtendResource()))
			resourceUtils.loadResource(getGlobalExtendResource(), getLocaleSetting());
		if (messageResource != null)
			resourceUtils.loadResource(messageResource, getLocaleSetting());
	}

	/**
	 * 获取全局规则XML路径
	 * <br> 格式为从classPath还是算起的文件夹路径以及全文件名
	 * <br> 如:oecp/framework/validator/spring-validator-rules.xml
	 * @author slx
	 * @date 2011 4 15 10:15:58
	 * @modifyNote
	 * @return
	 */
	protected abstract String getGlobalRulesXMLPath();
	/**
	 * 获取全局扩展的资源消息文件路径.
	 * <br> 如:
	 * <br> calsspath路径下有多语言文件:
	 * <br> oecp/framework/validator/validatortestResources_zh_CN.properties
	 * <br> 应写作: 
	 * <br> oecp.framework.validator.validatortestResources <B>多语言后缀省略</B>
	 * @author slx
	 * @date 2011 4 15 10:16:18
	 * @modifyNote
	 * @return
	 */
	protected abstract String getGlobalExtendResource();
	
	/**
	 * 检查校验结果,整理错误信息. 如果有错误则返回一个数据错误异常,如果没有错误,则返回空.
	 * 
	 * @author slx
	 * @date 2011 4 13 16:49:39
	 * @modifyNote
	 * @param results
	 * @return
	 */
	private DataErrorException checkValidatorResults(ValidatorResults results) {
		// 如果验证结果中为空,则说明没有发生错误,无任何错误信息.则直接返回.
		if (results.isEmpty())
			return null;

		boolean success = true;
		StringBuffer errormsg = new StringBuffer("");

		// 得到要校验的字段名称
		Set<String> propertyNames = results.getPropertyNames();
		Iterator<String> it_propertyNames = propertyNames.iterator();
		// 逐个字段检查错误
		while (it_propertyNames.hasNext()) {
			String proName = it_propertyNames.next();
			// 得到字段校验结果
			ValidatorResult result = results.getValidatorResult(proName);
			// 得到校验条件名称集合
			Iterator<String> actions = result.getActions();
			// 逐个校验条件检查是否通过
			while (actions.hasNext()) {
				String actionName = actions.next();
				// 校验没通过,整理错误信息.
				if (!result.isValid(actionName)) {
					success = false;
					// 获得设置的消息
					ValidatorAction action = validatorRules.getValidatorAction(actionName);
					String message = resourceUtils.getString(action.getMsg());

					// 获得消息需要的参数
					Arg[] args = result.getField().getArgs(actionName);
					Object[] argvalues = new Object[args.length];
					for (int i = 0; i < args.length; i++) {
						argvalues[i] = resourceUtils.getString(args[i].getKey());
					}

					// 格式化消息,并追加到消息串中.
					errormsg.append(MessageFormat.format(message, argvalues)).append("  ");
				}
			}
		}

		if (success) {
			return null;
		} else {// 校验失败返回异常
			return new DataErrorException(errormsg.toString());
		}
	}

	/**
	 * 得到本地地理位置,多语言用.
	 * @author slx
	 * @date 2011 4 15 10:12:06
	 * @modifyNote
	 * @return
	 */
	protected abstract Locale getLocale();
	
	private Locale getLocaleSetting(){
		if(getLocale()!=null){
			return getLocale();
		}
		
		return Locale.getDefault();
	};
		

	private String getClassPath() {
		return Thread.currentThread().getContextClassLoader().getResource("").toString();
	}

}