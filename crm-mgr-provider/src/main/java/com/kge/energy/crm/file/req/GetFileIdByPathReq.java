package com.kge.energy.crm.file.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author wangjihua
 */
@Data
public class GetFileIdByPathReq {

    @NotBlank
    private String path;
}
