package com.kge.energy.crm.common.util;

public class AppletLinkUtils {

    private static final String ORDER_DETAIL_PATH = "/pages/user/order/detail/detail";
    private static final String CONTRACT_DETAIL_PATH = "/pages/user/contract/detail/detail";

    //获取工单详情query
    public static String getFormDetailQuery(Integer formId) {
        return "type=smsUrl"+ "&path=" + ORDER_DETAIL_PATH + "&formId=" + formId;
    }

    //获取合同详情query
    public static String getContractDetailQuery(Integer contractId) {
        return "type=smsUrl"+ "&path=" + CONTRACT_DETAIL_PATH + "&contractId=" + contractId;
    }

}
