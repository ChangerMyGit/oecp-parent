/**
 * oecp-platform - CanlendarNoteAction.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:上午11:34:43		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.canlendar.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oecp.framework.exception.BizException;
import oecp.framework.util.date.DateUtil;
import oecp.platform.canlendar.eo.CanlendarNote;
import oecp.platform.canlendar.service.CanlendarNoteService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

/** 
 *
 * @author luanyoubo  
 * @date 2014年3月31日 上午11:34:43 
 * @version 1.0
 *  
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/canlendar")
public class CanlendarNoteAction extends BasePlatformAction{
	private static final long serialVersionUID = 3411771642286792057L;
	private CanlendarNote canlendarNote;
	private String noteid;
	
    @Autowired
	private CanlendarNoteService<CanlendarNote> canlendarNoteService;
    
    /**
     * 获取所有的备忘事项
     * @author luanyoubo
     * @date 2014年3月31日下午1:11:27
     * @return
     * @throws BizException
     */
    @Action("getNotes")
    public String getNotes() throws BizException{
    	List<CanlendarNote> notes = canlendarNoteService.getNotes(getOnlineUser().getUser().getId());
    	Map<String,Object> map = new HashMap<String,Object>();
    	List result = new ArrayList();
    	int i = 1000;
    	for(CanlendarNote note :notes){
    		Map<String, Object> childMap = new HashMap<String, Object>();
    		childMap.put("id", i++);
    		childMap.put("title", note.getTitle());
    		childMap.put("notes", note.getNotes());
    		childMap.put("start", DateUtil.getDateStr(note.getStartDate()));
    		childMap.put("end", DateUtil.getDateStr(note.getEndDate()));
    		childMap.put("pk", note.getId());
    		result.add(childMap);
    	}
    	map.put("evts", result);
    	String json = JSON.toJSONString(map);
    	setJsonString(json);
    	return SUCCESS;
    }
    
    /**
     * 保存备忘事项
     * @author luanyoubo
     * @date 2014年3月31日下午1:17:24
     * @return
     * @throws BizException
     */
    @Action("saveNote")
	public String addNote() throws BizException {
		try {
			canlendarNote.setUserID(getOnlineUser().getUser().getId());
			canlendarNoteService.save(canlendarNote);
			setJsonString(JSON.toJSONString(canlendarNote));
		} catch (Exception e) {
			returnErrorMsg(e.getMessage());
		}
		return SUCCESS;
	}
    
    /**
     * 删除备忘事项
     * @author luanyoubo
     * @date 2014年4月1日下午1:37:35
     * @return
     * @throws BizException
     */
    @Action("deleteNote")
	public String deleteNote() throws BizException {
		canlendarNoteService.delete(canlendarNote.getId());
		return SUCCESS;
	}
    
	public void setCanlendarNoteService(CanlendarNoteService canlendarNoteService) {
		this.canlendarNoteService = canlendarNoteService;
	}

	public CanlendarNote getCanlendarNote() {
		return canlendarNote;
	}

	public void setCanlendarNote(CanlendarNote canlendarNote) {
		this.canlendarNote = canlendarNote;
	}

	public String getNoteid() {
		return noteid;
	}

	public void setNoteid(String noteid) {
		this.noteid = noteid;
	}
    
}