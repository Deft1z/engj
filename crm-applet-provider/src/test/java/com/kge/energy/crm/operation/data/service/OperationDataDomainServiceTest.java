package com.kge.energy.crm.operation.data.service;

import com.kge.energy.crm.CrmAppletProvider;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = CrmAppletProvider.class)
class OperationDataDomainServiceTest {

    @Resource
    private OperationDataDomainService operationDataDomainService;

    @Test
    void statisticalData() {
        LocalDate startTime = LocalDate.now().plusYears(-1L);
        LocalDate endTime = LocalDate.now();
        Integer tenantId = 1;
        Integer orgId = null;
        StatisticalDataResult result = operationDataDomainService.statisticalData(startTime, endTime, tenantId, orgId);
        log.info("result : {}", result);
    }
}
