package com.kge.energy.crm.app.req;

import com.kge.energy.crm.common.page.PageReq;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxUserAppReq extends PageReq {

    @NotNull
    private Integer userId;

}
