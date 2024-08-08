package com.kge.energy.crm.repository.entityext.result;

import com.kge.energy.crm.repository.entity.OmReport;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OperationDetail extends OmReport {
   private String filepath;
}
