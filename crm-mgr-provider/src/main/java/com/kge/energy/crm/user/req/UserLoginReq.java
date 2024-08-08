package com.kge.energy.crm.user.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class UserLoginReq {

    @NotBlank
    private String name;

    @NotBlank
    private String passwd;
}
