package com.kge.energy.crm.opetation.service;

import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.OperationDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class OperationService {

    private final OmReportDao omReportDao;

    public OperationDetail getDetail(OperationParam param) {
        return omReportDao.getDetail(param);
    }
}
