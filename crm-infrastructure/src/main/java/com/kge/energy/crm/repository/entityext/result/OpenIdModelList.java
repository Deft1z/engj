package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangrongjun
 */
@Data
@Accessors(chain = true)
public class OpenIdModelList {

    Integer Id;
    Integer Appid;
    Integer State;
    Integer Flag;
    Integer Uid;
    Integer Oid;
    Integer Pid;
    Integer Rflag;
    Integer Proaid;
    String  Pname;
    String  Realname;
    String  Name;
    String  Mobile;
    String  Remark;
}
