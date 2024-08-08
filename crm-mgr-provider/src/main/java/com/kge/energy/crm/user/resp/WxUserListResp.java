package com.kge.energy.crm.user.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kge.energy.crm.repository.entity.BUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class WxUserListResp {

    private Long CurrentPage;

    private Long PageSize;

    private Long Total;

    @JsonProperty("list")
    private List<BUser> UserList;

}
