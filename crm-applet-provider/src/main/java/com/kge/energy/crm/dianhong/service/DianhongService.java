package com.kge.energy.crm.dianhong.service;

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

    public Map<String, Object> getDianhongStatistic() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", suiliangPvService.getPcsStatusStatistic());
        result.put("devices", suiliangPvService.selectPcsDeviceList());
        result.put("total", suiliangPvService.getPvData());
        return result;
    }

}
