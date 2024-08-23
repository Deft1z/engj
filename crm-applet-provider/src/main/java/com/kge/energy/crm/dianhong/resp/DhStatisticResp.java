package com.kge.energy.crm.dianhong.resp;

import com.kge.energy.dh.resp.PcsDeviceResp;
import com.kge.energy.dh.resp.PcsStatusStatisticResp;
import com.kge.energy.dh.resp.PvDataResp;
import lombok.Data;

import java.util.List;

@Data
public class DhStatisticResp {
    private PcsStatusStatisticResp status;
    private List<PcsDeviceResp> devices;
    private PvDataResp total;
}
