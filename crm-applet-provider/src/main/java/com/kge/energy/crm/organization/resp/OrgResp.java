package com.kge.energy.crm.organization.resp;

import com.kge.energy.crm.repository.entityext.result.OrgResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class OrgResp {

    private Integer tid;
    private String title;
    private List<OrgResult> companyList;

}
