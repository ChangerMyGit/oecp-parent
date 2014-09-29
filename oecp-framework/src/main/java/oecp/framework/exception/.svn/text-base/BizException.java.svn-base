package oecp.framework.exception;

/**
 * 通用业务异常类
 * 
 * @author slx
 * @date 2011 4 7 10:35:11
 * @version 1.0
 */
public class BizException extends Exception {

	private static final long serialVersionUID = 1L;

	private String exceptionCode;
	/**
	 * @param msg
	 * 		异常消息.
	 */
	public BizException(String msg) {
		super(msg);
	}
	
	public BizException(Exception e){
		super(e);
	}
	
	/**
	 * 带异常编号的构造函数,
	 * 异常编号在多语言,或者异常信息在配置文件中时使用.
	 * @param expcode
	 * 		异常编号
	 * @param msg
	 * 		异常消息
	 */
	public BizException(String expcode,String msg){
		this(msg);
		exceptionCode = expcode;
	}
	
	public String getExceptionCode() {
		return exceptionCode;
	}
}
