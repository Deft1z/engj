package com.kge.energy.crm.company.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDeleteReq {
    @NotNull
    private List<Integer> ids;
}
