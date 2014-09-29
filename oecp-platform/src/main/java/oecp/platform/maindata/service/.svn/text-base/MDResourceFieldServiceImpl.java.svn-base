package oecp.platform.maindata.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.maindata.eo.MDResourceField;

/**
 * 主数据资源列服务实现
 * 
 * @author liujingtao
 * @date 2011-6-24 下午02:08:20
 * @version 1.0
 */
@Service("mdResourceFieldService")
public class MDResourceFieldServiceImpl extends
		PlatformBaseServiceImpl<MDResourceField> implements
		MDResourceFieldService {

	@Override
	public List<MDResourceField> getMDResourceFields(String mdResourceid) {
		return getDao().queryByWhere(MDResourceField.class, "o.md.id=?",
				new Object[] { mdResourceid });
	}

	@Override
	public List<MDResourceField> getRelatedMDFields(String mdResourceid) {
		return getDao().queryByWhere(MDResourceField.class,
				"o.md.id=? AND o.relatedMD.id is not null",
				new Object[] { mdResourceid });
	}

	@Override
	public List<MDResourceField> getDisplayMDFields(String mdResourceid,
			Boolean isDisplay) {
		return getDao().queryByWhere(MDResourceField.class,
				"o.md.id=? AND o.isDisplay=?",
				new Object[] { mdResourceid, isDisplay });
	}

}
