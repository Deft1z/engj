package com.kge.energy.crm.user.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class WxUserListReq {

    private Long CurrentPage = 1L;

    private Long PageSize = 10L;

    @Schema(description = "租户ID")
    private Integer tenantId;

    private Map<String, String> Sort;

    private SearchMapBean searchMap;

    @Data
    @Accessors(chain = true)
    public static class SearchMapBean {
        private String name;
    }
}
