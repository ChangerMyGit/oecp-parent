package oecp.bcbase.eo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注单据主表。用于标注单据子表对象上与单据主表关联的字段。
 * <B>请标注于get方法上,注意：每个子表对象上关联的主表只能有一个。</B>
 * @author slx
 * @date 2011-12-23
 */
@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BillMain {

}
