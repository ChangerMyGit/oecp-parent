/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.portal.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import oecp.framework.util.DateUtil;
import oecp.framework.web.JsonResult;
import oecp.platform.web.BasePlatformAction;
import oecp.platform.web.OnlineUser;
import oecp.platform.web.WebContext;

/**
 * 在线用户action
 * 
 * @author yongtree
 * @date 2011-5-17 上午09:58:48
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/portal/onlineUser")
public class OnlineUserAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	private String orgId;

	@Action(value = "list")
	public String list() {
		List<OnlineUser> users = WebContext.getOnlineUsers();

		List<OnlineUser> list = new ArrayList<OnlineUser>();
		// 根据机构条件检索出符合条件的在线用户列表
		if (StringUtils.isNotEmpty(orgId)) {
			for (OnlineUser ou : users) {
				if (orgId.equals(ou.getLoginedOrg().getId())) {
					list.add(ou);
				}
			}
		} else {
			list = users;
		}

		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

		int size = list.size();
		if (start < size) {
			int length = (limit > (size - start)) ? (size - start) : limit;
			for (int i = start; i < length; i++) {
				Map<String, Object> onlineUserMap = new HashMap<String, Object>();
				OnlineUser onlineUser = list.get(i);
				onlineUserMap.put("sessionId", onlineUser.getSessionId());
				onlineUserMap.put("userId", onlineUser.getUser().getId());
				onlineUserMap.put("username", onlineUser.getUser().getName());
				onlineUserMap.put("loginId", onlineUser.getUser().getLoginId());
				onlineUserMap.put("loginTime", DateUtil.getDateStr(onlineUser.getLoginTime(),"yyyy-MM-dd HH:mm:ss"));
				onlineUserMap.put("lastActiveTime", DateUtil.getDateStr(onlineUser.getLastActiveTime(),"yyyy-MM-dd HH:mm:ss"));
				onlineUserMap.put("orgId", onlineUser.getLoginedOrg().getId());
				onlineUserMap.put("orgCode", onlineUser.getLoginedOrg()
						.getCode());
				onlineUserMap.put("orgName", onlineUser.getLoginedOrg()
						.getName());
				maps.add(onlineUserMap);
			}
		}
		this
				.setJsonString(JSON.toJSONString(new JsonResult(list.size(),
						maps)));
		return SUCCESS;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
