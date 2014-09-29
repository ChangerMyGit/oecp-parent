package oecp.platform.base.service;

import java.io.IOException;
import java.util.Locale;

import oecp.framework.validator.OECPAbstractValidator;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * OECP平台校验器
 * <br> 校验器使用说明:
 * <br> 因为校验器使用时应该为每个Service一个,所以虽然类封装为此类,可是使用时的配置却不相同.
 * <br> 因此需要使用Spring配置声明bean的方式来使用.
 * <br> Spring配置文件示例:
 * <pre> 
 * {@code 
 * 	<bean id="testValidator" class="oecp.platform.base.service.OECPValidator" init-method="init">
 * 		<!-- 校验规则文件 -->
 * 		<constructor-arg value="oecp/platform/test/validate-test.xml"></constructor-arg>
 * 		<!-- 校验消息资源文件 -->
 * 		<constructor-arg value="oecp.platform.test.validatortestResources"></constructor-arg>
 * 	</bean>
 * }
 * </pre> 
 * @author slx
 * @date 2011 4 13 15:23:08
 * @version 1.0
 */
@Component("validatorGlobalSetting")
public class OECPValidator extends OECPAbstractValidator {

	private Locale locale = Locale.getDefault();
	/** 全局扩展规则 **/
	private static String globalRulesXMLPath = "oecp/platform/base/service/validator-rules-extends.xml";
	/** 全局消息资源 **/
	private static String globalExtendResource = "oecp.platform.base.service.validatortestResources";
	
	public OECPValidator() {
	}
	
	public OECPValidator(String customerRuleXml) throws IOException, SAXException { 
		setValidatorRuleXML(customerRuleXml); 
	} 
	
	public OECPValidator(String customerRuleXml,String messageResource) throws IOException, SAXException { 
		setMessageResource(messageResource); 
		setValidatorRuleXML(customerRuleXml); 
	} 

	
	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	protected String getGlobalExtendResource() {
		return globalExtendResource;
	}

	@Override
	protected String getGlobalRulesXMLPath() {
		return globalRulesXMLPath;
	}


}
