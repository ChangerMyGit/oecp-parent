/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.auth.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oecp.framework.web.JsonResult;
import oecp.framework.web.WebConstant;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.OrganizationConfig;
import oecp.platform.user.enums.UserState;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;
import oecp.platform.web.BasePlatformAction;
import oecp.platform.web.OnlineUser;
import oecp.platform.web.WebContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 用户认证Action
 * 
 * @author yongtree
 * @date 2011-5-3 下午02:41:24
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/auth")
public class AuthAction extends BasePlatformAction {

	private static final long serialVersionUID = 7745827512413783647L;

	private String loginId;

	private String pwd;// 未加密的密码

	private String md5Pwd;// 加密的密码

	@Autowired
	private UserService userService;

	@Transactional
	@Action(value = "login", interceptorRefs = { @InterceptorRef("defaultStack") })
	public String doLogin() throws Exception {

		if (StringUtils.isEmpty(loginId)) {
			returnErrorMsg("登录名不能为空！");
		} else {
			if (StringUtils.isEmpty(md5Pwd)) {
				if (StringUtils.isEmpty(pwd)) {
					returnErrorMsg("密码不能为空！");
				} else {
					md5Pwd = DigestUtils.md5Hex(pwd);
				}
			}
			User loginUser = this.userService.getLoginUser(loginId, md5Pwd);
			// memcachedClient.add( loginUser.getId(), "login in!");
			if (loginUser != null) {
				if (UserState.disabled.equals(loginUser.getState())) {
					returnErrorMsg("该账户已被禁用！");
				} else {
					// 查找该用户所拥有的组织机构，如果只有一个ORG直接进入首页；如果有多个，将ORG列表返回；如果没有，将错误信息返回。
					ArrayList<Organization> orgs = (ArrayList<Organization>) this.userService
							.getOrganizationsToUser(loginUser);
					OnlineUser onlineUser = new OnlineUser();
					onlineUser.setUser(loginUser);
					onlineUser.setAccessToken(UUID.randomUUID().toString());
					if (orgs.size() == 0) {
						returnErrorMsg("您没有进入任何公司的权限，请联系管理员！");
					} else {
						if (orgs.size() == 1) {
							// 如果用户拥有一个公司，则直接进入首页
							onlineUser.setLoginedOrg(orgs.get(0));
							onlineUser.login();
							this.setJsonString(JSON
									.toJSONString(new JsonResult(true, "登录成功！")));
							//
							/** begin 增加组织logo图片 **/
							OrganizationConfig ogc = this.getOnlineUser()
									.getLoginedOrg().getOrganizationConfig();
							String logoUrl = WebConstant.OECP_DEFAULT_ORG_LOGOURL;
							if (ogc != null) {
								logoUrl = this.getOnlineUser().getLoginedOrg()
										.getOrganizationConfig().getLogoUrl();
							}
							this.getSession().setAttribute(
									WebConstant.OECP_ORG_LOGOURL, logoUrl);
							/** end 增加组织logo图片 **/
						} else {
							onlineUser.getData().put(
									WebConstant.OECP_SESSION_ORGS, orgs);
							onlineUser.activating();
							List<Map<String, String>> data = new ArrayList<Map<String, String>>();
							for (Organization o : orgs) {
								Map<String, String> org = new HashMap<String, String>();
								org.put("id", o.getId());
								org.put("code", o.getCode());
								org.put("name", o.getName());
								data.add(org);
							}
							this.setJsonString(JSON
									.toJSONString(new JsonResult(data)));
						}
						//写cookie
						Cookie cookie = new Cookie("access_token", onlineUser.getAccessToken());
						cookie.setMaxAge(-1);
						cookie.setPath("/");
						this.getResponse().addCookie(cookie);
					}
				}
			} else {
				returnErrorMsg("您的用户名或密码不对，重试或联系管理员！");
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 集成外部项目的安全验证
	 * @author yangtao
	 * @date 2013-1-22下午04:20:48
	 * @return
	 * @throws Exception
	 */
	@Action(value = "check")
	public String check() throws Exception {
		HttpServletRequest request =  this.getRequest();
		HttpServletResponse response =  this.getResponse();
		String targetUrl = request.getParameter("target_url");
		String accessToken = request.getParameter("access_token");
		OnlineUser onlineUser = WebContext.getCurrentUser();
		if(onlineUser!=null){
			String currentAccessToken = onlineUser.getAccessToken();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
			if(currentAccessToken.equalsIgnoreCase(accessToken)){//验证通过
				targetUrl += "?access_token="+accessToken+"&user_login_id="+onlineUser.getUser().getLoginId()+"&error_basepath="+basePath;
				response.sendRedirect(targetUrl);
				return null;
			}else{//验证不通过
				return LOGIN;
			}
		}else{
			return LOGIN;
		}
	}

	@Action(value = "logout")
	public void doLogout() {
		getOnlineUser().logout();
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMd5Pwd() {
		return md5Pwd;
	}

	public void setMd5Pwd(String md5Pwd) {
		this.md5Pwd = md5Pwd;
	}

}
