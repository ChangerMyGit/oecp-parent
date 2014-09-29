package oecp.platform.bcinfo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcinfo.eo.OperationDescription;

@Service("operationDescriptionService")
public class OperationDescriptionServiceImpl extends
		PlatformBaseServiceImpl<OperationDescription> implements
		OperationDescriptionService {

	@Transactional
	public void delOperations(List<OperationDescription> operations) {
		for (OperationDescription operation : operations) {
			this.getDao().delete(OperationDescription.class, operation.getId());
		}
	}
}
