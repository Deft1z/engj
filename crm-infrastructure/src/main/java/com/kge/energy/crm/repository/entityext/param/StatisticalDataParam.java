package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class StatisticalDataParam {

    private String dimension;

    private LocalDate startTime;

    private LocalDate endTime;

    private Integer tenantId;

    private Integer orgId;

    private List<String> statDims;
}
