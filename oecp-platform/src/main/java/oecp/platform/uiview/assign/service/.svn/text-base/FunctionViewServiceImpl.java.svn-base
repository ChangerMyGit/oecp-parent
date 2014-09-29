/**
 * oecp-platform - FunctionViewServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-2下午2:49:56		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.assign.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.util.OECPBeanUtils;
import oecp.framework.util.entity.EOUtility;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.eo.Organization;
import oecp.platform.print.eo.PrintTemplate;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.vo.FunctionViewVO;
import oecp.platform.uiview.enums.ComponentType;
import oecp.platform.uiview.eo.SpecialAttribute;
import oecp.platform.uiview.eo.UIComponent;
import oecp.platform.uiview.vo.UIComponentVO;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能视图的后台服务
 * 
 * @author slx
 * @date 2011-11-2
 */
@Service("functionViewServiceImpl")
public class FunctionViewServiceImpl extends PlatformBaseServiceImpl<FunctionView> implements FunctionViewService {

	private static final String ONLIST = "onlist";
	private static final String ONFORM = "onform";

	/**
	 * 根据指定功能获得功能视图
	 * 
	 * @author liujt
	 * @date 2011-11-10上午9:14:27
	 * @param functionId
	 * @return
	 */
	@Override
	public List<FunctionView> getViewsByFunctionID(String functionId, String orgid) {
		return getDao().queryByWhere(FunctionView.class, "o.func.id=? AND (o.org.id=? OR o.shared=true)", new Object[] { functionId, orgid });
	}

	@Override
	public FunctionView find_full(Serializable id) throws BizException {
		FunctionView view = find(id);
		UIComponent compeo = view.getViewcomp();
		loadEOItems(compeo);
		return view;
	}

	/**
	 * 递归加载ui控件内的内部对象和每个对象的特殊属性
	 * 
	 * @param eo
	 */
	private void loadEOItems(UIComponent eo) {
		if (eo.getItems().size() > 0) {
			for (int i = 0; i < eo.getItems().size(); i++) {
				eo.getItems().get(i).getAttrs().size();
				loadEOItems(eo.getItems().get(i));
			}
		}
		return;
	}

	@Override
	public FunctionViewVO findViewVO(String funcViewID) throws BizException {
		FunctionView view = find(funcViewID); // 查询到一个视图的eo
		return eo2vo(view); // 把eo转成个vo返回
	}

	/**
	 * 转换vieweo为viewvo
	 * 
	 * @param eo
	 * @return
	 */
	private FunctionViewVO eo2vo(FunctionView eo) {
		FunctionViewVO vo = new FunctionViewVO();
		BeanUtils.copyProperties(eo, vo); // 复制共有属性
		// 需要将list和form两个UIComponent EO对象合并到VO中

		UIComponent formeo = eo.getViewcomp(); // form界面eo
		vo.setViewcompid(formeo.getId());
		List<UIComponent> formitemeos = formeo.getItems();
		UIComponent fheadeo = formitemeos.size() > 0 ? formitemeos.get(0) : null; // form界面表头eo
		UIComponent fbodyeo = formitemeos.size() > 1 ? formitemeos.get(1) : null; // form界面表体eo
		UIComponent ffooteo = formitemeos.size() > 2 ? formitemeos.get(2) : null; // form界面表尾eo

		// 先复制卡片界面的所有属性，然后用追加 "是否列表"属性 和 列表中独有的字段.
		// 先根据3个form界面的配置eo创建三个对应的vo
		UIComponentVO headvo = createVOFromFUIEO(fheadeo, true, false);
		UIComponentVO bodyvo = createVOFromFUIEO(fbodyeo, true, false);
		UIComponentVO footvo = createVOFromFUIEO(ffooteo, true, false);

		vo.setHead(headvo);
		vo.setBody(bodyvo);
		vo.setFoot(footvo);

		return vo;
	}

	/**
	 * 从界面EO中创建VO
	 * 
	 * @param eo
	 * @param onform
	 *            字段是否在form界面使用
	 * @param onlist
	 *            字段是否在list界面使用
	 * @return
	 */
	private UIComponentVO createVOFromFUIEO(UIComponent eo, boolean onform, boolean onlist) {
		if (eo == null) {
			return null;
		}
		UIComponentVO vo = new UIComponentVO();
		BeanUtils.copyProperties(eo, vo); // 先拷贝公有属性
		if (onlist) {
			vo.setValue("onlistid", eo.getId());
		}
		if (ComponentType.Field.equals(eo.getType())) { // 如果是字段控件，则需要设置在form界面上使用。
			vo.setValue(ONFORM, onform);
			vo.setValue(ONLIST, onlist); // 暂时先给个false,列表过滤时再把出现的设置为true
		}

		// 复制eo中的特殊属性到vo
		List<SpecialAttribute> otherattrs = eo.getAttrs();
		if (otherattrs != null) {
			for (SpecialAttribute attr : otherattrs) {
				vo.setValue(attr.getAttrname(), attr.getAttrvalue());
			}
		}

		// 递归创建子元素
		List<UIComponent> items = eo.getItems();
		if (items != null && items.size() > 0) {
			int len = items.size();
			vo.setChildren(new ArrayList<UIComponentVO>(len));
			for (int i = 0; i < len; i++) {
				vo.getChildren().add(createVOFromFUIEO(items.get(i), onform, onlist));
			}
		}

		return vo;
	}

	@Override
	@Transactional()
	public void saveViewVO(FunctionViewVO funviewvo) throws BizException {
		if (StringUtils.isEmpty(funviewvo.getViewcode())) {
			throw new BizException("视图编号不允许为空！");
		}
		if (StringUtils.isEmpty(funviewvo.getViewname())) {
			throw new BizException("视图名称不允许为空！");
		}
		if (funviewvo.getHead() == null && funviewvo.getBody() == null && funviewvo.getFoot() == null) {
			throw new BizException("视图中无任何界面控件！");
		}
		// 将VO转换为EO
		FunctionView vieweo = viewvo2eo(funviewvo);
		// 保存
		save(vieweo);
	}
	
	@Override
	@Transactional()
	public void save(FunctionView funview) throws BizException {
		validateFunctioView(funview);
		super.save(funview);
	}
	
	/**
	 * 校验功能视图
	 * @param funview
	 * @throws BizException
	 */
	private void validateFunctioView(FunctionView funview) throws BizException {
		// 视图编号不能重复，名称在function内不能重复 
		boolean codeisexist = false;
		boolean nameisexist = false;
		if(StringUtils.isEmpty(funview.getId())){
			codeisexist = getDao().isExistedByWhere(FunctionView.class, " o.viewcode=? ", new Object[]{funview.getViewcode()});
			nameisexist = getDao().isExistedByWhere(FunctionView.class, " o.viewname=? AND o.func.id=? ", new Object[]{funview.getViewname(),funview.getFunc().getId()});
		}else{
			codeisexist = getDao().isExistedByWhere(FunctionView.class, " o.viewcode=? AND o.id<>?", new Object[]{funview.getViewcode(),funview.getId()});
			nameisexist = getDao().isExistedByWhere(FunctionView.class, " o.viewname=? AND o.id<>? AND o.func.id=? ", new Object[]{funview.getViewname(),funview.getId(),funview.getFunc().getId()});
		}
		if(codeisexist)
			throw new BizException("视图编号重复！");
		if(nameisexist)
			throw new BizException("视图名称重复！");
		validateComp(funview.getViewcomp());
	}
	
	/**
	 * 校验视图内控件（递归调用，校验内部控件）
	 * @param comp
	 * @throws BizException
	 */
	private void validateComp(UIComponent comp) throws BizException {
		// 控件类型不能为空
		if(comp.getType() == null)
			throw new BizException("控件类型不能为空！");
		if(comp.getType() == ComponentType.Field){
			// field控件下不能有子控件，field必须有title。
			if(comp.getItems()!=null && comp.getItems().size() > 0){
				throw new BizException("字段下不允许再有其他控件！");
			}
			if(StringUtils.isEmpty(comp.getTitle()))
				throw new BizException("字段名不能为空");
		}else{
			List<UIComponent> items = comp.getItems();
			// Panel、grid、tab控件不能没有子控件，grid下只能有field，tab下不能直接放field.
			if(items == null || items.size() < 0){
				throw new BizException("面板、表格、标签页下不允许没有其他控件！");
			}
			if(comp.getType() == ComponentType.Grid){
				for (UIComponent griditem : items) {
					if(griditem.getType() != ComponentType.Field){
						throw new BizException("表格下只允许有字段控件！");
					}
				}
			}
			if(comp.getType() == ComponentType.Tab){
				for (UIComponent griditem : items) {
					if(griditem.getType() == ComponentType.Field){
						throw new BizException("标签下不允许允许直接放字段控件！");
					}
				}
			}
			for (UIComponent item : items) {
				validateComp(item);
			}
		}
	}
	
	private FunctionView viewvo2eo(FunctionViewVO funviewvo) throws BizException {
		String viewid = funviewvo.getId();
		FunctionView funvieweo = null;
		if (!StringUtils.isEmpty(viewid)) {
			funvieweo = find(viewid);
			if (funvieweo == null) {
				throw new BizException("要修改的数据不存在或已被删除！");
			}
		} else {
			funvieweo = new FunctionView();
		}

		OECPBeanUtils.copyObjectValue(funviewvo, funvieweo, "viewcode->viewcode,viewname->viewname,org->org,func->func,shared->shared");
		// 根据FunctionViewVO里的head、body、foot创建列表和表单界面的UIComponentVO
		try {
			// 把两个vo转换为两个eo，如果eo已经存在，覆盖其子控件和特殊属性
			UIComponentVO formvo = getFormPanelVOFormFuncView(funviewvo);
			funvieweo.setViewcomp(compvo2eo(formvo));
		} catch (Exception e) {
			throw new BizException(e);
		}
		return funvieweo;
	}

	/**
	 * 控件vo转换为eo
	 * 
	 * @param vo
	 * @return
	 */
	private UIComponent compvo2eo(UIComponentVO vo) {
		String id = vo.getId();
		UIComponent eo = null;
		if (!StringUtils.isEmpty(id)) {
			eo = getDao().find(UIComponent.class, id);
		} else {
			eo = new UIComponent();
		}
		// 将vo中的字段拷贝到eo中，保证items和attrs不被覆盖
		BeanUtils.copyProperties(vo, eo, new String[] { "items", "attrs" });
		// 处理扩展属性
		List<SpecialAttribute> spattrs = eo.getAttrs();
		if (spattrs == null) { // eo的扩展属性。如果为空，则初始一个list。
			spattrs = new ArrayList<SpecialAttribute>();
			eo.setAttrs(spattrs);
		}
		String[] vospattrnames = vo.getSpecialAttrNames();
		String spattrname = null;
		boolean deleted = true;
		for (int i = 0; i < spattrs.size(); i++) {
			spattrname = spattrs.get(i).getAttrname();
			deleted = true;
			for (int j = 0; j < vospattrnames.length; j++) {
				// 找到相同的赋值，清掉已赋值的名称，跳出内循环
				if (spattrname.equals(vospattrnames[j])) {
					spattrs.get(i).setAttrvalue(vo.getValue(spattrname) == null ? null : vo.getValue(spattrname).toString());
					vospattrnames[j] = null;
					deleted = false;
					break;
				}
			}
			// 内循环后没找到的清掉自己
			if (deleted) {
				spattrs.remove(i);
				i--;
			}
		}
		// vo的扩展属性名称列表中，不为空的名称字段即是新增的，因为相同的已经在前面的循环中被清空。
		for (int i = 0; i < vospattrnames.length; i++) {
			if (vospattrnames[i] != null && vo.getValue(vospattrnames[i]) != null) {
				SpecialAttribute attr = new SpecialAttribute();
				attr.setComp(eo);
				attr.setAttrname(vospattrnames[i]);
				attr.setAttrvalue(vo.getValue(vospattrnames[i]).toString());
				eo.getAttrs().add(attr);
			}
		}

		// 处理内部子对象
		if (vo.getChildren() != null && vo.getChildren().size() > 0) {
			if (eo.getItems() == null) {
				eo.setItems(new ArrayList<UIComponent>());
			} else {
				eo.getItems().clear();
			}
			for (int i = 0; i < vo.getChildren().size(); i++) {
				// 递归调用。用vo的子对象创建子eo，并加到当前eo的items中。
				UIComponent itemeo = compvo2eo(vo.getChildren().get(i));
				itemeo.setParent(eo);
				eo.getItems().add(itemeo);
			}
		}else{
			if(eo.getItems()!=null)
				eo.getItems().clear();
		}

		return eo;
	}

	@Override
	public UIComponentVO getFormPanelByViewID(String viewid) throws BizException, IOException, ClassNotFoundException {
		FunctionViewVO funcview = findViewVO(viewid);
		UIComponentVO formpanel = getFormPanelVOFormFuncView(funcview);
		removeItemByAttr(formpanel, ONFORM);
		return formpanel;
	}

	/**
	 * 从functionView中分离出formpanel的UIComponentVO对象
	 * 
	 * @param funviewvo
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public UIComponentVO getFormPanelVOFormFuncView(FunctionViewVO funviewvo) throws IOException, ClassNotFoundException {
		UIComponentVO headvo = funviewvo.getHead();
		UIComponentVO bodyvo = funviewvo.getBody();
		UIComponentVO footvo = funviewvo.getFoot();

		UIComponentVO formpanel = new UIComponentVO();
		formpanel.setChildren(new ArrayList<UIComponentVO>());
		// 如果Form界面的顶级panel已经存在，则从数据库查询出，然后复制值。否则创建一个新的。
		if (!StringUtils.isEmpty(funviewvo.getViewcompid())) {
			UIComponent formpaneleo = getDao().find(UIComponent.class, funviewvo.getViewcompid());
			BeanUtils.copyProperties(formpaneleo, formpanel);
		} else {
//			formpanel.setTitle(funviewvo.getViewcode().concat("Form界面"));
			formpanel.setType(ComponentType.Panel);
		}

		formpanel.setValue("layout", "vbox");
		formpanel.setValue("layoutConfig", "{align:\"stretch\"}");
//		formpanel.setValue("layout", "border");
		if (headvo != null) {
			headvo.setIdx(0);
			headvo.setValue("split", true);
//			headvo.setValue("region", "north");
			List<UIComponentVO> tabs = findAllChildrenByType(headvo,ComponentType.Tab);
			if(tabs!=null){ // 如果存在tab面板，则为其追加 layoutConfig:{deferredRender:false}
				for (UIComponentVO tab : tabs) {
					tab.setValue("layoutConfig", "{deferredRender:false,forceLayout: true}");
				}
			}
			formpanel.getChildren().add((UIComponentVO) EOUtility.cloneObject(headvo));
		}
		if (bodyvo != null) {
			bodyvo.setIdx(5);
//			bodyvo.setValue("region", "center");
			bodyvo.setValue("layout", "fit");
			bodyvo.setValue("split", true);
			if(bodyvo.getChildren()==null){
				bodyvo.setValue("flex", 0);
				bodyvo.setHidden(true);
			}else{
				bodyvo.setValue("flex", 1);
				bodyvo.setValue("split", true);
			}
			List<UIComponentVO> tabs = findAllChildrenByType(bodyvo,ComponentType.Tab);
			if(tabs!=null){ // 如果存在tab面板，则为其追加 layoutConfig:{deferredRender:false}
				for (UIComponentVO tab : tabs) {
					tab.setValue("layoutConfig", "{deferredRender:false}");
				}
			}
			formpanel.getChildren().add((UIComponentVO) EOUtility.cloneObject(bodyvo));
		}
		if (footvo != null) {
			footvo.setIdx(10);
			footvo.setValue("split", true);
//			footvo.setValue("region", "south");
			formpanel.getChildren().add((UIComponentVO) EOUtility.cloneObject(footvo));
		}
		return formpanel;
	}

	public UIComponentVO getListPanelByViewID(String viewid) throws BizException, IOException, ClassNotFoundException {
		FunctionViewVO funcview = findViewVO(viewid);
		UIComponentVO formpanel = getListPanelVOFormFuncView(funcview);
		removeItemByAttr(formpanel, ONLIST);
		return formpanel;
	}

	/**
	 * 从functionView中分离出listpanel的UIComponentVO对象
	 * 
	 * @param funviewvo
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public UIComponentVO getListPanelVOFormFuncView(FunctionViewVO funviewvo) throws IOException, ClassNotFoundException {
		UIComponentVO headvo = funviewvo.getHead();
		UIComponentVO bodyvo = funviewvo.getBody();
		UIComponentVO footvo = funviewvo.getFoot();

		UIComponentVO listpanel = new UIComponentVO();
		listpanel.setChildren(new ArrayList<UIComponentVO>());
		// 如果List界面的顶级panel已经存在，则从数据库查询出，然后复制值。否则创建一个新的。
		if (!StringUtils.isEmpty(funviewvo.getViewcompid())) {
			UIComponent listpaneleo = getDao().find(UIComponent.class, funviewvo.getViewcompid());
			BeanUtils.copyProperties(listpaneleo, listpanel);
		} else {
//			listpanel.setTitle(funviewvo.getViewcode().concat("List界面"));
			listpanel.setType(ComponentType.Panel);
		}
//		listpanel.setValue("layout", "border");
		listpanel.setValue("layout", "vbox");
		listpanel.setValue("layoutConfig", "{align:\"stretch\"}");
		
		UIComponentVO listheadvo = null;
		boolean hashead = false;
		if (headvo != null) {
			hashead = true;
			listheadvo = (UIComponentVO) EOUtility.cloneObject(headvo);
			listheadvo.setIdx(0);
//			listheadvo.setValue("region", "center");
			listheadvo.setValue("layout", "fit");
			listheadvo.setValue("flex", 1);
			listheadvo.setValue("cancommit", true);
			listheadvo.setValue("split", true);
			listpanel.getChildren().add(listheadvo);
		} else if (footvo != null) {
			listpanel.getChildren().add((UIComponentVO) EOUtility.cloneObject(footvo));
			listpanel.getChildren().get(0).setIdx(0);
			listpanel.getChildren().get(0).setValue("layout", "fit");
			listpanel.getChildren().get(0).setValue("flex", 1);
			listpanel.getChildren().get(0).setValue("cancommit", true);
			listpanel.getChildren().get(0).setValue("split", true);
		}
		if (bodyvo != null && bodyvo.getChildren()!=null) {
			UIComponentVO listbodyvo = (UIComponentVO) EOUtility.cloneObject(bodyvo);
//			listbodyvo.setValue("region", "south");
			listbodyvo.setValue("layout", "fit");
			listbodyvo.setValue("flex", 1);
			listbodyvo.setValue("split", true);
			List<UIComponentVO> tabs = findAllChildrenByType(listbodyvo,ComponentType.Tab);
			if(tabs!=null){ // 如果存在tab面板，则为其追加 layoutConfig:{deferredRender:false}
				for (UIComponentVO tab : tabs) {
					tab.setValue("layoutConfig", "{deferredRender:false}");
				}
			}
			listpanel.getChildren().add(listbodyvo);
			listpanel.getChildren().get(0).setIdx(5);
			List<UIComponentVO> bodyGridpanels = getFieldPanel(listbodyvo);
			for (int i = 0; i < bodyGridpanels.size(); i++) {// 在list界面上，设置所有承载字段的面板或表格为不可编辑
				bodyGridpanels.get(i).setValue("editable", "false");
			}
		}
		List<UIComponentVO> fieldpanels;
		if(hashead){
			fieldpanels = getFieldPanel(listheadvo);
			fieldpanels.get(0).setType(ComponentType.Grid); // 表头第一个面板为主EO所在面板，在列表界面将其变成Grid
			if(fieldpanels.get(0).getHeight()==null)
				fieldpanels.get(0).setHeight(260);
		}
		// 把表尾的字段拷贝到表头的第一个标签中（如果有标签的话）。
		if (footvo != null && hashead) { // 表尾不为空，并且有表头
			// 找到表头上第一个带字段的list
			fieldpanels = getFieldPanel(listheadvo);
			// 把表头转成表格
			for (int i = 0; i < fieldpanels.size(); i++) {
				fieldpanels.get(i).setValue("editable", "false"); // 设置表头所有表格或面板不可编辑
//				if(fieldpanels.get(i).getHeight()==null)// XXX slx 列表界面上表格默认260px
//					fieldpanels.get(i).setHeight(260);
			}
			List<UIComponentVO> firstfieldlist = fieldpanels.get(0).getChildren();
			// 找到表尾字段
			footvo.setIdx(10);
			UIComponentVO listfootvo = (UIComponentVO) EOUtility.cloneObject(footvo);
			List<UIComponentVO> footfieldlist = listfootvo.getChildren();
			while ((footfieldlist != null && footfieldlist.size() > 0) && !footfieldlist.get(0).getType().equals(ComponentType.Field)) {
				footfieldlist = footfieldlist.get(0).getChildren();
			}
			if (footfieldlist != null) {
				// 表尾有字段，增大索引号然后追加到表头第一个字段列表中
				for (int i = 0; i < footfieldlist.size(); i++) {
					int idx = firstfieldlist.get(firstfieldlist.size() - 1).getIdx() + 10;
					footfieldlist.get(i).setIdx(idx);
					firstfieldlist.add(footfieldlist.get(i));
				}
			}
		}
		List<UIComponentVO> fields = findAllChildrenByType(listpanel,ComponentType.Field); // 设置所有字段为不可编辑
		for (UIComponentVO field : fields) {
			field.setValue("readOnly", "true");
			if("checkbox".equals(field.getValue("xtype"))){
				field.setValue("disabled", "true");
			}
		}
		return listpanel;
	}
	

	/**
	 * 得到所有放字段的panel或者grid
	 * 
	 * @param compvo
	 * @return
	 */
	private List<UIComponentVO> getFieldPanel(UIComponentVO compvo) {
		if (compvo == null || compvo.getChildren() == null || compvo.getChildren().size() < 1 || compvo.getType().equals(ComponentType.Field)) {
			return null;
		}
		List<UIComponentVO> panels = new ArrayList<UIComponentVO>();
		List<UIComponentVO> children = compvo.getChildren();
		if (children.get(0).getType().equals(ComponentType.Field)) {
			panels.add(compvo);
		} else {
			for (int i = 0; i < children.size(); i++) {
				List<UIComponentVO> tpanels = getFieldPanel(children.get(i));
				if (tpanels != null)
					panels.addAll(tpanels);
			}
		}

		return panels;
	}
	
	/**
	 * 从一个控件中查找所有同一个类型的子控件
	 * @author songlixiao
	 * @date 2014年4月4日下午2:30:41
	 * @param compvo
	 * @param type
	 * @return
	 */
	private List<UIComponentVO> findAllChildrenByType(UIComponentVO compvo,ComponentType type){
		if (compvo == null || compvo.getChildren() == null || compvo.getChildren().size() < 1) {
			return null;
		}
		List<UIComponentVO> panels = new ArrayList<UIComponentVO>();
		List<UIComponentVO> children = compvo.getChildren();
		for (UIComponentVO c : children) {
			if(c.getType().equals(type)){
				panels.add(c);
			}
			List<UIComponentVO> tpanels = findAllChildrenByType(c,type);
			if (tpanels != null)
				panels.addAll(tpanels);
		}

		return panels;
	}

	/**
	 * 根据一个boolean属性，从控件中删除子控件。
	 * 如：是否onlist，Field上此属性为空或false时，此Field将被移除，其他类型控件上此属性为空保留，为false删除。
	 * 
	 * @param compvo
	 */
	private void removeItemByAttr(UIComponentVO compvo, String attrname) {
		List<UIComponentVO> children = compvo.getChildren();
		if (children != null) {
			Object attrvalue = null;
			for (int i = 0; i < children.size(); i++) {
				attrvalue = children.get(i).getValue(attrname);
				boolean needremove = false;
				if (children.get(i).getType().equals(ComponentType.Field)) {
					if (attrvalue == null || !Boolean.valueOf(attrvalue.toString())) {
						needremove = true;
					}
				} else {
					if (attrvalue != null && !Boolean.valueOf(attrvalue.toString())) {
						needremove = true;
					}
				}
				if (needremove) {
					children.remove(i);
					i--;
				} else { // 没有被移除的进入递归
					removeItemByAttr(children.get(i), attrname);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see oecp.platform.uiview.assign.service.FunctionViewService#cloneViewByID(java.lang.String, java.lang.String, java.lang.String, oecp.platform.org.eo.Organization, boolean)
	 */
	@Override
	public FunctionViewVO cloneViewByID(String viewid, String viewcode, String viewname, Organization org, boolean shared) throws BizException {
		FunctionView srcview = find_full(viewid);
		FunctionView tagetview = new FunctionView();
		BeanUtils.copyProperties(srcview, tagetview, new String[] { "viewcomp","printTemplates" });
		tagetview.setId(null);
		tagetview.setViewcode(viewcode);
		tagetview.setViewname(viewname);
		tagetview.setOrg(org);
		tagetview.setShared(shared);
		tagetview.setSysdefault(false);
		UIComponent compeo = cloneCompEO(srcview.getViewcomp());
		tagetview.setViewcomp(compeo);
		save(tagetview);
		return findViewVO(tagetview.getId());
	}

	/**
	 * 清除eo和它的子控件的id属性及其扩展属性的id属性。
	 * 
	 * @param compeo
	 */
	private UIComponent cloneCompEO(UIComponent srceo) {
		if (srceo == null) {
			return null;
		}
		UIComponent cloneo = new UIComponent();
		BeanUtils.copyProperties(srceo, cloneo, new String[] { "id", "items", "attrs", "parent" });

		List<SpecialAttribute> attrs = srceo.getAttrs();
		if (attrs != null && attrs.size() > 0) {
			cloneo.setAttrs(new ArrayList<SpecialAttribute>());
			for (SpecialAttribute attr : attrs) {
				SpecialAttribute cloneattr = new SpecialAttribute();
				cloneattr.setAttrname(attr.getAttrname());
				cloneattr.setAttrvalue(attr.getAttrvalue());
				cloneattr.setComp(cloneo);
				cloneo.getAttrs().add(cloneattr);
			}
		}

		List<UIComponent> items = srceo.getItems();
		if (items != null && items.size() > 0) {
			cloneo.setItems(new ArrayList<UIComponent>());
			for (UIComponent itemeo : items) {
				UIComponent cloneitem = cloneCompEO(itemeo);
				cloneo.getItems().add(cloneitem);
				cloneitem.setParent(cloneo);
			}
		}

		return cloneo;
	}
	
	@Transactional
	public void saveViewPrintTemplates(String viewid,String printTemplateIds) throws BizException{
		FunctionView fv = this.find(viewid);
		if(StringUtils.isEmpty(printTemplateIds)){
			fv.setPrintTemplates(null);
		} else {
			String[] _pids =  printTemplateIds.split(",");
			ArrayList<PrintTemplate> list = new ArrayList<PrintTemplate>();
			for(int i=0; i< _pids.length; i++){
				PrintTemplate _pt = new PrintTemplate();
				_pt.setId(_pids[i]);
				list.add(_pt);
			}
			fv.setPrintTemplates(list);
		}
		this.save(fv);
	}
}
