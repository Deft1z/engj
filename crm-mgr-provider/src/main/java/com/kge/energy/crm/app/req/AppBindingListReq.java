package com.kge.energy.crm.app.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AppBindingListReq {
    @NotNull
    private Integer userId;
}
