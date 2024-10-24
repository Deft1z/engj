package com.kge.energy.crm.dianhong.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.util.NumberUtils;
import com.kge.energy.crm.dianhong.resp.DhStatisticResp;
import com.kge.energy.dh.req.DeviceControlDataReq;
import com.kge.energy.dh.req.DeviceControlEnableReq;
import com.kge.energy.dh.req.DeviceControlReq;
import com.kge.energy.dh.req.DeviceEnableReq;
import com.kge.energy.dh.resp.DeviceControlDataResp;
import com.kge.energy.dh.resp.DeviceControlEnableResp;
import com.kge.energy.dh.resp.DeviceControlResp;
import com.kge.energy.dh.resp.DeviceEnableResp;
import com.kge.energy.dh.service.SuiliangPvService;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public DeviceEnableResp setControlEnable(DeviceEnableReq req) {
        if(!StrUtil.equals("0", req.getEnable()) && !StrUtil.equals("1", req.getEnable())){
            throw new ServiceException("参数错误");
        }

        return suiliangPvService.setControlEnable(req);
    }

    public DeviceControlResp setControlPercent(DeviceControlReq req) {
        if(!NumberUtils.isPositiveInteger(req.getPowerPercent())){
            throw new ServiceException("参数错误");
        }

        return suiliangPvService.setControlPercent(req);
    }

    public DeviceControlDataResp getDeviceControlData(DeviceControlDataReq req) {
        return suiliangPvService.getDeviceControlData(req);
    }

    public DeviceControlEnableResp getControlEnable(DeviceControlEnableReq req) {
        return suiliangPvService.getControlEnable(req);
    }

}
