package oecp.platform.bcinfo.eo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.acbase.eo.BaseMasArchivesEO;

import org.apache.commons.lang.StringUtils;

/**
 * 业务组件
 * 
 * @author wangliang
 * 
 */
@Entity
@Table(name = "OECP_SYS_BIZCOMPONENT")
public class BizComponent extends BaseMasArchivesEO {

	private static final long serialVersionUID = 1L;
	private String code;// 组件编号
	private String name;// 组件名
	private String discription;// 组件描述
	private String host;// 主机地址，比如：localhost,127.0.0.1,demo.oecp.cn
	private String servicePort;// web service端口
	private String webPort;// http web端口
	private String contextPath;// 应用上下文路径，默认值："/"，也可以是子目录，比如/demo
//	private String dbType;// 数据库类型
//	private String dbIp;// 数据库IP地址
//	private String dbPort;// 数据库端口
//	private String dbUser;// 数据库用户名
//	private String dbPwd;// 数据库密码
	private Integer initNum; //初始化次数
	private Boolean isConnection; //是否对接
	
	private Integer displayOrder; //显示顺序
	private String daoName;//数据源Dao
	
	@Transient
	public String getHttpDomainUrl() {
		if(StringUtils.isNotBlank(host)){			
			return (getHost().startsWith("http://") ? getHost()
					: ("http://" + getHost()))
					+ ("80".equals(getWebPort()) ? "" : (":" + getWebPort()))
					+ (StringUtils.isEmpty(getContextPath())?"":getContextPath());
		}else{
			return getContextPath();
		}
	}

	@Transient
	public String getWebServiceDomainUrl() {
		if(StringUtils.isNotBlank(host)){
			return (getHost().startsWith("http://") ? getHost()
					: ("http://" + getHost()))
					+ ("80".equals(getServicePort()) ? ""
							: (":" + getServicePort())) + (StringUtils.isEmpty(getContextPath())?"":getContextPath());
		}else
			return null;
	}

	public String getHost() {
//		if (StringUtils.isEmpty(host)) {
//			return "localhost";
//		}
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getContextPath() {
		// if (StringUtils.isEmpty(contextPath)||"/".equals(contextPath)){
		// return "";
		// }
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

//	public String getDbPort() {
//		return dbPort;
//	}
//
//	public void setDbPort(String dbPort) {
//		this.dbPort = dbPort;
//	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getServicePort() {
		if (StringUtils.isEmpty(servicePort)) {
			return "80";
		}
		return servicePort;
	}

	public void setServicePort(String servicePort) {
		this.servicePort = servicePort;
	}

	public String getWebPort() {
		if (StringUtils.isEmpty(webPort)) {
			return "80";
		}
		return webPort;
	}

	public void setWebPort(String webPort) {
		this.webPort = webPort;
	}

//	public String getDbType() {
//		return dbType;
//	}
//
//	public void setDbType(String dbType) {
//		this.dbType = dbType;
//	}

//	public String getDbIp() {
//		return dbIp;
//	}
//
//	public void setDbIp(String dbIp) {
//		this.dbIp = dbIp;
//	}

	public Integer getInitNum() {
		if(initNum == null){
			return 0;
		}
		return initNum;
	}

	public void setInitNum(Integer initNum) {
		this.initNum = initNum;
	}

//	public String getDbUser() {
//		return dbUser;
//	}
//
//	public void setDbUser(String dbUser) {
//		this.dbUser = dbUser;
//	}
//
//	public String getDbPwd() {
//		return dbPwd;
//	}
//
//	public void setDbPwd(String dbPwd) {
//		this.dbPwd = dbPwd;
//	}
	
	public Boolean getIsConnection() {
		if(isConnection == null){
			return false;
		}
		return isConnection;
	}

	public void setIsConnection(Boolean isConnection) {
		this.isConnection = isConnection;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDaoName() {
		return daoName;
	}

	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}

}
