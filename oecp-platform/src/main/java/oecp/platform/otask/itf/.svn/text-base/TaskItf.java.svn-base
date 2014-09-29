package oecp.platform.otask.itf;

import java.io.Serializable;
import java.util.Map;

import oecp.framework.exception.BizException;
/**
 * 
 * @Desc 任务插件 ，可以把定时完成的任务逻辑，写在这个实现类里面
 *       execute是默认的一个方法，还可以根据业务需要增加多个方法，
 *       参数都是Map，业务所需值，根据后台配置在Map中取出
 * @author yangtao
 * @date 2012-3-30
 *
 */
public interface TaskItf  extends Serializable{
	/**
	 * 
	 * @Desc 任务插件的默认执行方法，业务逻辑写在此方法中 
	 * @author yangtao
	 * @date 2012-4-6
	 *
	 * @return
	 */
	public Object execute(Map params)throws BizException;
}
