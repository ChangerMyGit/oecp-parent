/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.api.datapermission;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.entity.base.StringPKEO;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionField;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bcfunction.service.FunctionFieldService;
import oecp.platform.datapermission.eo.DataDiscretePermission;
import oecp.platform.datapermission.eo.DataPermission;
import oecp.platform.datapermission.service.DataDiscretePermissionService;
import oecp.platform.datapermission.service.DataPermissionService;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.maindata.eo.MDResourceField;
import oecp.platform.maindata.service.MDResourceFieldService;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PersonService;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 
 * @author liujt
 * @date 2011-8-23 下午02:30:47
 * @version 1.0
 */
@Service("dataPermissionSQLService")
public class DataPermissionSQLServiceImpl extends
		PlatformBaseServiceImpl<StringPKEO> implements DataPermissionSQLService {

	@Resource
	private UserService userService;
	@Resource
	private PersonService personService;
	@Resource
	private BcFunctionService bcFunctionService;
	@Resource
	private FunctionFieldService functionFieldService;
	@Resource
	private DataPermissionService dataPermissionService;
	@Resource
	private MDResourceFieldService mdResourceFieldService;
	@Resource
	private DataDiscretePermissionService dataDiscretePermissionService;
	
	@Override
	public String getDataPermissionSQL(String userId, String orgId,
			String funcCode) throws BizException {
		Function function = bcFunctionService.getFunctionByCode(funcCode);
		List<FunctionField> functionFields = function.getFunctionFields();
		StringBuffer hqlsb = new StringBuffer();
		for(int i = 0; i < functionFields.size(); i++){
			String hql = getDataPermissionSQL(userId, orgId, funcCode, functionFields.get(i).getName(), null);
			if(hql == null){
				continue;
			}
			if(!"".equals(hqlsb.toString())){
				hqlsb.append(" and ");
			}
			hqlsb.append("(");
			hqlsb.append(hql);
			hqlsb.append(")");
		}
		if("".equals(hqlsb.toString())){
			return "('1'='1')";
		}
		
//		List list = getDao().getHibernateSession().createQuery("from oecp.platform.org.eo.Department d where d.idx = '123'").list();
//		List list2 = getDao().getHibernateSession().createQuery("from User u where createTime > '05-7月 -11 04.07.55.535000000 下午'").list();
		
		return hqlsb.toString();
	}
	
	@Override
	public String getDataPermissionSQL(String userId, String orgId, String funcCode,
			String funFieldName, String asName) throws BizException {
		if(asName == null){
			asName = funFieldName;
		}
		
		Function function = bcFunctionService.getFunctionByCode(funcCode);
		String funId = null;
		if (function != null) {
			funId = function.getId();
		}
		
		boolean isEO = true;
		List<FunctionField> fields = function.getFunctionFields();
		for (FunctionField f : fields) {
			if(funFieldName.equals(f.getName())){
				if(StringUtils.isNotBlank(f.getClassName())){
					try {
						isEO = BaseEO.class.isAssignableFrom(Class.forName(f.getClassName()));
					} catch (ClassNotFoundException e) {
						throw new IllegalArgumentException("功能注册配置异常：功能【"+function.getCode() +"】字段【"+f.getName()+"】类型【"+f.getClassName()+"】.");
					}
				}
				break;
			}
		}

		// 获得当前用户指定公司的岗位集合 postList
		List<Post> postList = getPosts(userId, orgId);
		if(postList == null || postList.size() == 0){
			return null;
		}
		
		// 获得主数据资源 mdResource
		MDResource mdResource = getMDResource(funcCode, funFieldName);
		if(mdResource == null){
			return null;
		}
		
		// 生成HQL查询条件
		StringBuffer hqlsb = new StringBuffer();
		hqlsb.append("(");
		if (postList.size() > 1) {
			hqlsb.append("(");
		}
		for (int i = 0; i < postList.size(); i++) {
			if (i > 0) {
				hqlsb.append(") or (");
			}
			String postid = postList.get(i).getId();
			// 获得规则数据权限
			List<DataPermission> dataPermissionList = dataPermissionService
					.getDataPermissions(postid, mdResource.getId(), funId);
			// 获得离散数据权限
			List<DataDiscretePermission> dataDisPermissionList = dataDiscretePermissionService
					.getDatas(postid, mdResource.getId(), funId);

			if (dataPermissionList.size() == 0
					&& dataDisPermissionList.size() == 0) {
				funId = null;
				dataPermissionList = dataPermissionService.getDataPermissions(
						postid, mdResource.getId(), funId);
				dataDisPermissionList = dataDiscretePermissionService.getDatas(
						postid, mdResource.getId(), funId);
			}

			for (int j = 0; j < dataPermissionList.size(); j++) {
				if (j > 0) {
					hqlsb.append(" and ");
				}
				hqlsb.append(asName);// 别名
				hqlsb.append(".");
				hqlsb.append(dataPermissionList.get(j).getMdField().getName());// 字段名
				hqlsb.append(" ");
				hqlsb.append(dataPermissionList.get(j).getOperator());// 操作符
				hqlsb.append(" '");
				if("like".equals(dataPermissionList.get(j).getOperator())){
					hqlsb.append("%");
				}
				hqlsb.append(dataPermissionList.get(j).getValue());
				if("like".equals(dataPermissionList.get(j).getOperator())){
					hqlsb.append("%");
				}
				hqlsb.append("'");
			}

			List<MDResourceField> mdResourceFields = mdResourceFieldService
					.getRelatedMDFields(mdResource.getId());

			// 离散
			if (dataDisPermissionList.size() > 0) {
				if (dataPermissionList.size() > 0) {
					hqlsb.append(" and ");
				}
				hqlsb.append(asName);
				if(isEO){
					hqlsb.append(".id");
				}
				hqlsb.append(" in(");
				for (int j = 0; j < dataDisPermissionList.size(); j++) {
					hqlsb.append("'");
					hqlsb.append(dataDisPermissionList.get(j).getDataid());
					hqlsb.append("'");
					if (j != dataDisPermissionList.size() - 1) {
						hqlsb.append(",");
					}
				}
				hqlsb.append(")");
			}
			
			getRelatedHql(hqlsb, asName, mdResourceFields, postid, funId);
		}

		if (postList.size() > 1) {
			hqlsb.append(")");
		}
		hqlsb.append(")");
		String hqlstr = hqlsb.toString();
		hqlstr = hqlstr.replaceAll("\\(\\)", "('1'='1')");
		
		return hqlstr;
	}

	/**
	 * 递归方法循环遍历级联数据列
	 * @param hqlsb
	 * @param bname
	 * @param mdResourceFields
	 * @param postid
	 * @param funId
	 */
	private void getRelatedHql(StringBuffer hqlsb, String bname,
			List<MDResourceField> mdResourceFields, String postid, String funId) {
		for (MDResourceField mdResourceField : mdResourceFields) {
			MDResource relatedMD = mdResourceField.getRelatedMD();
			// 获得规则数据权限
			List<DataPermission> dataPerRelatedList = dataPermissionService
					.getDataPermissions(postid, relatedMD.getId(), funId);
			// 获得离散数据权限
			List<DataDiscretePermission> dataDisPermissionList = dataDiscretePermissionService
					.getDatas(postid, relatedMD.getId(), funId);
			// 新别名
			String bnameTmp = bname + "." + mdResourceField.getName();
			// 规则
			for (int m = 0; m < dataPerRelatedList.size(); m++) {
				hqlsb.append(" and ");
				hqlsb.append(bnameTmp);// 别名
				hqlsb.append(".");
				hqlsb.append(dataPerRelatedList.get(m).getMdField().getName());// 字段名
				hqlsb.append(" ");
				hqlsb.append(dataPerRelatedList.get(m).getOperator());// 操作符
				hqlsb.append(" '");
				if("like".equals(dataPerRelatedList.get(m).getOperator())){
					hqlsb.append("%");
				}
				hqlsb.append(dataPerRelatedList.get(m).getValue());
				if("like".equals(dataPerRelatedList.get(m).getOperator())){
					hqlsb.append("%");
				}
				hqlsb.append("'");
			}

			if (dataDisPermissionList.size() > 0) {
				hqlsb.append(" and ");
				hqlsb.append(bnameTmp);
				hqlsb.append(".id in(");
				for (int n = 0; n < dataDisPermissionList.size(); n++) {
					hqlsb.append("'");
					hqlsb.append(dataDisPermissionList.get(n).getDataid());
					hqlsb.append("'");
					if (n != dataDisPermissionList.size() - 1) {
						hqlsb.append(",");
					}
				}
				hqlsb.append(")");
			}

			List<MDResourceField> relatedMDResourceFields = mdResourceFieldService
					.getRelatedMDFields(relatedMD.getId());
			getRelatedHql(hqlsb, bnameTmp, relatedMDResourceFields, postid,
					funId);
		}
	}

	private List<Post> getPosts(String userId, String orgId)
			throws BizException {
		List<Post> posts = new ArrayList<Post>();
		User user = userService.find(userId);
		String personId = user.getPersonId();
		if(StringUtils.isNotEmpty(personId)){
			Person person = personService.find(personId);
			Post post = person.getPost();
			List<Post> otherPosts = person.getOtherPosts();
			if (orgId.equals(post.getOrg().getId())) {
				posts.add(post);
			}
			for (Post otherPost : otherPosts) {
				if (orgId.equals(otherPost.getOrg().getId())) {
					posts.add(otherPost);
				}
			}
		}
		return posts;
	}

	private MDResource getMDResource(String funcCode, String funFieldName) {
		List<FunctionField> funFieldList = functionFieldService
				.getFunctionFields(funcCode);
		FunctionField functionField = null;
		for (FunctionField funFieldTmp : funFieldList) {
			if (funFieldName.equals(funFieldTmp.getName())) {
				functionField = funFieldTmp;
			}
		}
		if(functionField == null){
			return null;
		}
		
		return functionField.getMdType();
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public FunctionFieldService getFunctionFieldService() {
		return functionFieldService;
	}

	public void setFunctionFieldService(
			FunctionFieldService functionFieldService) {
		this.functionFieldService = functionFieldService;
	}

	public DataPermissionService getDataPermissionService() {
		return dataPermissionService;
	}

	public void setDataPermissionService(
			DataPermissionService dataPermissionService) {
		this.dataPermissionService = dataPermissionService;
	}

	public MDResourceFieldService getMdResourceFieldService() {
		return mdResourceFieldService;
	}

	public void setMdResourceFieldService(
			MDResourceFieldService mdResourceFieldService) {
		this.mdResourceFieldService = mdResourceFieldService;
	}

	public DataDiscretePermissionService getDataDiscretePermissionService() {
		return dataDiscretePermissionService;
	}

	public void setDataDiscretePermissionService(
			DataDiscretePermissionService dataDiscretePermissionService) {
		this.dataDiscretePermissionService = dataDiscretePermissionService;
	}

	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

}
