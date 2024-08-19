package com.kge.energy.crm.file.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class GetFileIdByPathResp {

    private Integer fileId;

    private String filepath;

}
