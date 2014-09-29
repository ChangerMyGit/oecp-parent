package oecp.platform.billflow.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.billflow.eo.DataFieldView;
import oecp.platform.billflow.service.BillFlowConfigService;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.biztype.service.BizTypeService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 单据流action
 * 
 * @author slx
 * @date 2011-6-20 下午04:25:21
 * @version 1.0
 */
@Controller("billFlowManagerAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/billflowMng")
public class BillFlowManagerAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	@Resource(name="billFlowConfigService")
	private BillFlowConfigService bfcService;
	@Resource(name="bizTypeService")
	private BizTypeService bizTypeService;
	
	private String bizTypeID;
	private String configID;
	private String[] configIDs;
	private BizType bizType;
	private BillFlowConfig bfConfig;
	private List<DataFieldView> fields;
	
	/**
	 * 获得本公司内的业务类型
	 * @author slx
	 * @date 2011-6-20 下午04:28:05
	 * @modifyNote
	 * @return
	 */
	@Action("listBizTypes")
	public String getBizTypes(){
		List<BizType> biztypes = bizTypeService.getBizTypeByOrg(getOnlineUser().getLoginedOrg().getId());
		String json = FastJsonUtils.toJson(biztypes , new String[]{"id","code","name","description","shared","org"});
		setJsonString(json);
		return SUCCESS;
	}
	
	@Action("loadBizType")
	public String loadBizType() throws BizException{
		BizType biztype = bizTypeService.find(bizTypeID);
		String json = FastJsonUtils.toJson(biztype , new String[]{"id","code","name","description","shared","org"});
		setJsonString(json);
		return SUCCESS;
	}
	
	/**
	 * 保存业务类型
	 * @author slx
	 * @date 2011-6-22 下午02:25:43
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("saveBizType")
	public String saveBizType() throws BizException{
		bizTypeService.save(bizType);
		setJsonString("{success : true , msg : '保存成功'}");
		return SUCCESS;
	}
	
	@Action("delBizType")
	public String delBizType() throws BizException{
		bizTypeService.delete(bizTypeID);
		setJsonString("{success : true , msg : '删除成功'}");
		return SUCCESS;
	}
	/**
	 * 得到某业务类型下的单据流配置信息
	 * @author slx
	 * @date 2011-6-22 下午02:25:59
	 * @modifyNote
	 * @return
	 */
	@Action("listBFConfigs")
	public String loadBillFlowConfigs(){
		List<BillFlowConfig> bfConfigs = bfcService.getBillFlowConfigByBiz(bizTypeID);
		String json = FastJsonUtils.toJson(bfConfigs , new String[]{"id","function","code","name","byHand","byBussiness","description","billCreaterFromPre","billCreaterToNext","billPreWriteBacker","billPreRollBackWriter","billCreaterFromPreCheck","preBillFunction","nextBillFunction"});
		setJsonString(json);
		return SUCCESS;
	}
	
	@Action("listConfigFields")
	public String loadConfigFields() throws BizException{
		List<DataFieldView> fields = bfcService.getPreDataFieldsByCfgID(configID);
		String json = FastJsonUtils.toJson(fields , new String[]{"id","name","dispName","dataType","maxlength","supplement","editable","hidden"});
		setJsonString(json);
		return SUCCESS;
	}
	
	/**
	 * 获得一个单据的配置信息
	 * @author slx
	 * @date 2011-6-22 下午02:26:31
	 * @modifyNote
	 * @return
	 * @throws BizException 
	 */
	@Action("loadBFConfig")
	public String loadBillFlowConfig() throws BizException{
		BillFlowConfig config = bfcService.find(configID);
		String json = FastJsonUtils.toJson(config , new String[]{"id","bizType","function","code","name","byHand","byBussiness","qlType","daobeanname","preQuerySQL","queryDialog","description","billCreaterFromPre","billCreaterToNext","billPreWriteBacker","billPreRollBackWriter","billCreaterFromPreCheck","preBillFunction","nextBillFunction"});
		setJsonString(json);
		return SUCCESS;
	}
	/**
	 * 保存一个单据的配置信息
	 * @author slx
	 * @date 2011-6-22 下午02:26:34
	 * @modifyNote
	 * @return
	 * @throws BizException 
	 */
	@Action("saveBFConfig")
	public String saveBillFlowConfig() throws BizException{
		if(bfConfig.getPreBillFunction()==null||bfConfig.getPreBillFunction().getId()==null)
			bfConfig.setPreBillFunction(null);
		if(bfConfig.getNextBillFunction()==null||bfConfig.getNextBillFunction().getId()==null)
			bfConfig.setNextBillFunction(null);
		bfcService.save(bfConfig);
		setJsonString("{success : true , msg : '保存成功',configID:'"+bfConfig.getId()+"'}");
		return SUCCESS;
	}
	
	@Action("delBFConfigs")
	public String delBillFlowConfig() throws BizException{
		bfcService.delete(configIDs);
		setJsonString("{success : true , msg : '删除成功'}");
		return SUCCESS;
	}
	/**
	 * 保存一个单据的前置字段信息
	 * @author slx
	 * @date 2011-6-22 下午02:26:37
	 * @modifyNote
	 * @return
	 * @throws BizException 
	 */
	@Action("saveBFPreFields")
	public String savePreFields() throws BizException{
		bfcService.savePreDataFields(fields, configID);
		setJsonString("{success : true , msg : '保存成功'}");
		return SUCCESS;
	}
	
	@Action("delBFConfig")
	public String delBFConfig() throws BizException{
		bfcService.delete(configID);
		setJsonString("{success : true , msg : '删除成功'}");
		return SUCCESS;
	}

	public String getBizTypeID() {
		return bizTypeID;
	}
	public void setBizTypeID(String bizTypeID) {
		this.bizTypeID = bizTypeID;
	}
	public String getConfigID() {
		return configID;
	}
	public void setConfigID(String configID) {
		this.configID = configID;
	}
	public BillFlowConfig getBfConfig() {
		return bfConfig;
	}
	public void setBfConfig(BillFlowConfig bfConfig) {
		this.bfConfig = bfConfig;
	}
	public List<DataFieldView> getFields() {
		return fields;
	}
	public void setFields(List<DataFieldView> fields) {
		this.fields = fields;
	}
	public void setBfcService(BillFlowConfigService bfcService) {
		this.bfcService = bfcService;
	}
	public void setBizTypeService(BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
	}
	public void setBizType(BizType bizType) {
		this.bizType = bizType;
	}
	public BizType getBizType() {
		return bizType;
	}
	public String[] getConfigIDs() {
		return configIDs;
	}
	public void setConfigIDs(String[] configIDs) {
		this.configIDs = configIDs;
	}
}


