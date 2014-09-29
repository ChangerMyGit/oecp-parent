package oecp.bcbase.eo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单据明细，用于标注在主表实体内的子对象上。可用于子表，或多表头的表实体上。
 * <B>请标注于get方法上</B>
 * @author slx
 * @date 2011-12-23
 */
@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BillItems {
}
