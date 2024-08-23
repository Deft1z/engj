package com.kge.energy.crm.dianhong.service;

import com.kge.energy.crm.dianhong.resp.DhStatisticResp;
import com.kge.energy.dh.resp.PcsDeviceResp;
import com.kge.energy.dh.service.SuiliangPvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DianhongService {

    private final SuiliangPvService suiliangPvService;

    public DhStatisticResp getDianhongStatistic() {
        DhStatisticResp result = new DhStatisticResp();
        result.setStatus(suiliangPvService.getPcsStatusStatistic());
        result.setDevices(suiliangPvService.selectPcsDeviceList());
        result.setTotal(suiliangPvService.getPvData());
        return result;
    }

}
