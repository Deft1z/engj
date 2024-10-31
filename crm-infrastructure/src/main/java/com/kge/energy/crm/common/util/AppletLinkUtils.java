package com.kge.energy.crm.common.util;

public class AppletLinkUtils {

    private static final String ORDER_DETAIL_PATH = "/pages/user/order/detail/detail";
    private static final String CONTRACT_DETAIL_PATH = "/pages/user/contract/detail/detail";
    private static final String COMPLAIN_DETAIL_PATH = "/pages/user/complaint/detail/detail";
    private static final String SURVEY_ANSWER_PATH = "/pages/survey/answer/add";

    //获取工单详情query
    public static String getFormDetailQuery(Integer formId) {
        return "type=smsUrl"+ "&path=" + ORDER_DETAIL_PATH + "&formId=" + formId;
    }

    //获取合同详情query
    public static String getContractDetailQuery(Integer contractId) {
        return "type=smsUrl"+ "&path=" + CONTRACT_DETAIL_PATH + "&contractId=" + contractId;
    }

    //获取投诉建议详情query
    public static String getComplainDetailQuery(Integer complainId) {
        return "type=smsUrl"+ "&path=" + COMPLAIN_DETAIL_PATH + "&complainId=" + complainId;
    }

    //获取调查问卷详情query
    public static String getSurveyAnswerQuery(Integer recordId) {
        return "type=smsUrl"+ "&path=" + SURVEY_ANSWER_PATH + "&recordId=" + recordId;
    }

}
