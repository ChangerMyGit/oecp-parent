package oecp.platform.maindata.service;

import java.io.Serializable;
import java.util.List;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.maindata.eo.MDResourceField;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 主数据资源类型服务实现
 *
 * @author liujingtao
 * @date 2011-6-24 上午09:37:56
 * @version 1.0
 */
@Service("mdResourceService")
public class MDResourceServiceImpl extends PlatformBaseServiceImpl<MDResource> implements MDResourceService{
	
	@Override
	public List<MDResource> getAllMDResources() {
		return getDao().queryByWhere(MDResource.class, null, null);
	}

	@Override
	public MDResource getMdResourceByEOClassName(String eoClassName) {
		return getDao().findByWhere(MDResource.class, "o.eoClassName=?", new Object[]{eoClassName});
	}

	@Override
	public List<MDResource> getMDResources(int start, int limit) {
		return getDao().queryByWhere(MDResource.class, null, null, start, limit);
	}

	@Override
	public long getTotalCount() {
		return getDao().getCount(MDResource.class);
	}

	@Override
	public List<MDResource> queryMDResource(String code, int start, int limit) {
		return getDao().queryByWhere(MDResource.class, "o.code like ?", new Object[]{code}, start, limit);
	}

	@Override
	public void saveMDResource(MDResource mdResource, List<MDResourceField> fields) throws BizException {
				if(fields!=null&&fields.size()>0){
					for (int i = 0; i < fields.size(); i++) {
						if(StringUtils.isEmpty(fields.get(i).getId())){
							fields.remove(i);
							i --;
						}
					}
					Serializable[] entityids = new String[fields.size()];
					for (int i = 0; i < entityids.length; i++) {
						entityids[i] = fields.get(i).getId();
					}
					getDao().delete(MDResourceField.class, entityids);
				}
				if(StringUtils.isEmpty(mdResource.getId())){
					mdResource.setId(null);
				}
				
				List<MDResourceField> mdfs = mdResource.getFields();
				
				if(mdfs != null){
					for (MDResourceField mdf : mdfs) {
						if(StringUtils.isEmpty(mdf.getId())){
							mdf.setId(null);
						}
						mdf.setMd(mdResource);
						if(mdf.getRelatedMD()!=null && mdf.getRelatedMD().getId()==null){
							mdf.setRelatedMD(null);
						}else if(mdf.getRelatedMD()!=null && mdf.getRelatedMD().getId() != null){
							MDResource md = new MDResource();
							md.setId(mdf.getRelatedMD().getId());
							mdf.setRelatedMD(md);
							
						}
					}
				}
				
				if(mdResource.getId()==null){
					getDao().create(mdResource);
				}else{
					getDao().update(mdResource);
				}
		
	}
	
}
