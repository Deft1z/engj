package com.kge.energy.crm.common.util;

public class AppletLinkUtils {

    private static final String BIZ_TYPE_ORDER_DETAIL = "orderDetail";
    private static final String BIZ_TYPE_CONTRACT_DETAIL = "contractDetail";
    private static final String BIZ_TYPE_COMPLAIN_DETAIL = "complainDetail";
    private static final String BIZ_TYPE_SURVEY_ANSWER = "surveyAnswer";

    private static final String ORDER_DETAIL_PATH = "/pages/user/order/detail/detail";
    private static final String CONTRACT_DETAIL_PATH = "/pages/user/contract/detail/detail";
    private static final String COMPLAIN_DETAIL_PATH = "/pages/user/complaint/detail/detail";
    private static final String SURVEY_ANSWER_PATH = "/pages/survey/answer/add";

    //获取工单详情query
    public static String getFormDetailQuery(Integer formId) {
        return "type=" + BIZ_TYPE_ORDER_DETAIL + "&path=" + ORDER_DETAIL_PATH + "&detailId=" + formId;
    }

    //获取合同详情query
    public static String getContractDetailQuery(Integer contractId) {
        return "type=" + BIZ_TYPE_CONTRACT_DETAIL + "&path=" + CONTRACT_DETAIL_PATH + "&detailId=" + contractId;
    }

    //获取投诉建议详情query
    public static String getComplainDetailQuery(Integer complainId) {
        return "type=" + BIZ_TYPE_COMPLAIN_DETAIL + "&path=" + COMPLAIN_DETAIL_PATH + "&detailId=" + complainId;
    }

    //获取调查问卷答复query
    public static String getSurveyAnswerQuery(Integer recordId) {
        return "type=" + BIZ_TYPE_SURVEY_ANSWER + "&path=" + SURVEY_ANSWER_PATH + "&recordId=" + recordId;
    }

}
