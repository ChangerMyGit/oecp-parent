package oecp.framework.ognl.accessor;

import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;

import oecp.framework.vo.base.DataVO;
import ognl.ObjectPropertyAccessor;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlOps;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import ognl.enhance.ExpressionCompiler;
import ognl.enhance.UnsupportedCompilationException;

/**
 * VO接口实现类的Ognl属性访问器。 </br> 主要在界面form中的值提交给action中对应的对象类型是平台DataVO接口的实现类时使用。
 * </br> 实现表单提交时，不走对象的get和set方法对属性进行赋值，而走VO接口的setValue方法和getValue方法。 </br>
 * 使用此类需要在struts.xml中增加下面这行配置： </br>
 * {@code <bean type="ognl.PropertyAccessor" name="oecp.framework.vo.base.DataVO" class="oecp.framework.ognl.accessor.DataVOPropertyAccessor" />}
 * 
 * @author slx
 * @date 2011-11-2
 */
public class DataVOPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor {

	/**
	 * 从对象中取值的方法
	 */
	@Override
	public Object getProperty(Map context, Object target, Object name) throws OgnlException {
		context.put(XWorkConverter.LAST_BEAN_CLASS_ACCESSED, target.getClass());
		context.put(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED, name.toString());
		ReflectionContextState.updateCurrentPropertyPath(context, name);
		return ((DataVO) target).getValue(name.toString());
	}

	/**
	 * 往对象中赋值
	 */
	@Override
	public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
		value = OgnlRuntime.getConvertedType((OgnlContext)context, target, null, name.toString(), value, getPropertyClass((OgnlContext)context, target, name));
		((DataVO) target).setValue(name.toString(), value);
	}

	/**
	 * 得到一个字段的类型
	 */
	public Class<?> getPropertyClass(OgnlContext context, Object target, Object name) {
		DataVO datavo = (DataVO) target;
		String beanName = ((String) name).replaceAll("\"", "");

		return datavo.getFieldType(beanName);
	}

	/**
	 * 得到get属性的源代码，表达式编译器使用。 但通过debug断点调试，从来没有进入过此方法。不知是何用意？！
	 */
	public String getSourceAccessor(OgnlContext context, Object target, Object name) {
		DataVO datavo = (DataVO) target;
		String beanName = ((String) name).replaceAll("\"", "");
		Class<?> type = OgnlRuntime.getCompiler().getInterfaceClass(datavo.getFieldType(beanName));
		ExpressionCompiler.addCastString(context, "((" + type.getName() + ")");
		context.setCurrentAccessor(DataVO.class);
		context.setCurrentType(type);
		return ".getBean(" + name + "))";
	}

	/**
	 * 得到set属性的源代码，表达式编译器使用。 同上通过debug断点调试，也从来没有进入过此方法。同样不知是何用意？！
	 */
	public String getSourceSetter(OgnlContext context, Object target, Object name) {
		throw new UnsupportedCompilationException("Can't set beans on IBeanProvider.");
	}

}
