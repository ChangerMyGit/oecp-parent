/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 10
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.platform.org.web;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.file.enums.FileSource;
import oecp.framework.file.itf.IFileUploader;
import oecp.framework.util.OECPBeanUtils;
import oecp.framework.web.JsonResult;
import oecp.framework.web.ext.JsonTreeVO;
import oecp.framework.web.ext.JsonTreeVOBuilder;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.file.eo.UploadFile;
import oecp.platform.file.service.UploadFileService;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.OrgService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

/**
 * 组织目录管理Action
 * 
 * @author slx
 * @date 2011 5 10 11:45:23
 * @version 1.0
 */
@Controller("orgManageAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/org")
public class OrgManageAction extends BasePlatformAction  implements IFileUploader {

	private static final long serialVersionUID = 1L;

	@Resource(name = "orgService")
	private OrgService orgService;

	private Organization org;
	
	private File[] upload;
	private String[] uploadFileName;
	private String[] uploadContentType;
	
	@Resource(name = "uploadFileService2")
	private UploadFileService uploadFileService2;

	/**
	 * 公司树
	 * 
	 * @author slx
	 * @date 2011 5 23 09:36:41
	 * @modifyNote
	 * @return
	 */
	@Action("tree")
	public String treeOrgs() {
		List<Organization> orgs = orgService.getTopOrgs();

		List<JsonTreeVO> orgvos = JsonTreeVOBuilder.getTreeVOsFromEOs(orgs, "id", "name", "childOrgs",JsonTreeVOBuilder.CheckShow.NONE);
		String json = JSON.toJSONString(orgvos, true);
		super.setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 公司详细信息
	 * 
	 * @author slx
	 * @date 2011 5 23 09:36:56
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("orgInfo")
	public String getOrgs() throws BizException {
		String id = super.getRequest().getParameter("id");
		Organization org = orgService.find(id);
		if(org.getParent() == null){ // parent为空时防止页面解析失败
			org.setParent(new Organization());
		}
		JsonResult jr = new JsonResult(org);
		jr.setContainFields(new String[] { "id", "code", "name", "parent", "lock", "idx"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 保存公司信息
	 * 
	 * @author slx
	 * @date 2011 5 23 09:38:03
	 * @modifyNote
	 * @return
	 */
	@Action("save")
	public String save() {
		try {
			if (StringUtils.isEmpty(org.getParent().getId())) {
				org.setParent(null);
			}
			List<UploadFile> files = uploadFileService2.saveFilesButNoSaveEO(this);
			
			orgService.saveOrg(org,files);
			setJsonString("{success : true , msg : '保存成功'}");
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success : false , msg : '".concat(e.getMessage()).concat("'}"));
		}
		return SUCCESS;
	}

	/**
	 * 删除公司信息
	 * 
	 * @author slx
	 * @date 2011 5 23 09:38:16
	 * @modifyNote
	 * @return
	 */
	@Action("delete")
	public String delete() {
		try {
			String id = getRequest().getParameter("id");
			orgService.delete(id);
			setJsonString("{success : true , msg : '删除成功'}");
		} catch (Exception e) {
			setJsonString("{success : false , msg : '" + e.getMessage() + "'}");
		}
		return SUCCESS;
	}

	/**
	 * 显示组件建账列表
	 * 
	 * @author slx
	 * @date 2011 5 23 09:38:26
	 * @modifyNote
	 * @return
	 */
	@Action("showBCs")
	public String showBCs() {
		String id = super.getRequest().getParameter("id");
		List<OrgUseBC> orgbcs = orgService.getUsedBCs(id);// 获得所有已启用的记录
		String orgbc_formule = new StringBuffer()
				.append("org.id->orgid")
				.append(",bc.id->bcid")
				.append(",bc.code->bcCode")
				.append(",bc.name->bcName")
				.append(",bc.discription->bcDiscription")
				.append(",useDate->startUseDate")
				.append(",[java.lang.Boolean:true]->used")
				.toString();
		List<OrgUseBCVO> vos = OECPBeanUtils.createObjectList(orgbcs, OrgUseBCVO.class,orgbc_formule);

		List<BizComponent> bcs = orgService.getNoUsedBCs(id);	// 所有未启用的记录
		String bcs_formule = new StringBuffer()
				.append("[java.lang.String:").append(id).append("]->bcid")
				.append(",id->bcid")
				.append(",code->bcCode")
				.append(",name->bcName")
				.append(",discription->bcDiscription")
				.append(",[java.lang.Boolean:false]->used")
				.toString();
		List<OrgUseBCVO> bcs_vos = OECPBeanUtils.createObjectList(bcs, OrgUseBCVO.class,bcs_formule);
		
		vos.addAll(bcs_vos);
		JsonResult rs_json = new JsonResult(true, "获取建账信息成功", vos);
		rs_json.setTotalCounts(bcs_vos.size());
		setJsonString(JSON.toJSONString(rs_json));
		
		return SUCCESS;
	}

	private String orgid;
	private List<String> bcids;
	/**
	 * 启用组件
	 * 
	 * @author slx
	 * @date 2011 5 23 09:38:52
	 * @modifyNote
	 * @return
	 */
	@Action("startUseBCs")
	public String startUseBCs() {
		try {
			orgService.orgStartUseBCs(this.getOnlineUser().getLoginedOrg(),orgid, bcids);
			setJsonString("{success : true , msg : '建账成功'}");
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success : false , msg : '".concat(e.getMessage()).concat("'}"));
		}
		return SUCCESS;
	}
	
	@Override
	public String[] getDisplayName() {
		return uploadFileName;
	}
	@Override
	public FileSource getFileSource() {
		return FileSource.ORG_LOGO;
	}
	
	@Override
	public String getSavePath() {
		return "/upload/org/logo";
	}
	
	@Override
	public String getUid() {
		return getOnlineUser().getUser().getId();
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public List<String> getBcids() {
		return bcids;
	}

	public void setBcids(List<String> bcids) {
		this.bcids = bcids;
	}

	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public UploadFileService getUploadFileService2() {
		return uploadFileService2;
	}

	public void setUploadFileService2(UploadFileService uploadFileService2) {
		this.uploadFileService2 = uploadFileService2;
	}

}
