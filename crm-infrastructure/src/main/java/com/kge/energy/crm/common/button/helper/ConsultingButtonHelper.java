package com.kge.energy.crm.common.button.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.button.enums.WorkOrderButtonEnum;
import com.kge.energy.crm.common.button.resp.BaseButton;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.entityext.result.FlowResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 业务工单按钮 Helper 类
 *
 * @author wangjihua
 */
public class ConsultingButtonHelper extends AbstractButtonHelper {

    /**
     * 待处理工单页面按钮
     */
    public static List<BaseButton> getWaitHandleButton() {
        return createdButton(WorkOrderButtonEnum.HANDLE_WORK_ORDER);
    }

    public static BaseButton getWorkOrderButton(WorkOrderButtonEnum workOrderButtonEnum) {
        return createdSingleButton(workOrderButtonEnum);
    }

    public static List<BaseButton> getWorkOrderButton(WfForm wfForm, List<FlowResult> flowList, List<ContractResult> contractList, UserInfoDto operator) {
        return switch (wfForm.getStatus()) {
            case ConstParam.WaitingForProcessing -> getWaitingForProcessingButton(wfForm, flowList, contractList, operator);
            case ConstParam.Processing -> getProcessingButton(wfForm, flowList, contractList, operator);
            case ConstParam.Processed -> getProcessedButton(wfForm, flowList, contractList, operator);
            case ConstParam.Finished -> getFinishedButton(wfForm, flowList, contractList, operator);
            case ConstParam.Terminated -> getTerminatedButton(wfForm, flowList, contractList, operator);
            default -> new ArrayList<BaseButton>();
        };
    }

    private static List<BaseButton> getWaitingForProcessingButton(WfForm wfForm, List<FlowResult> flowList, List<ContractResult> contractList, UserInfoDto operator) {
        List<WorkOrderButtonEnum> buttonEnumList = new ArrayList<>();
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {

            buttonEnumList.add(WorkOrderButtonEnum.TERMINATE_WORK_ORDER);
            buttonEnumList.add(WorkOrderButtonEnum.ASSIGN_WORK_ORDER);
            return createdButtonList(buttonEnumList);

        } else if (operator.getRoleCodes().contains(RoleEnums.SUB_COMPANY_CUSTOMER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else if (operator.getRoleCodes().contains(RoleEnums.APPLET_USER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else {
            return createdButtonList(buttonEnumList);
        }

    }

    private static List<BaseButton> getProcessingButton(WfForm wfForm, List<FlowResult> flowList, List<ContractResult> contractList, UserInfoDto operator) {
        List<WorkOrderButtonEnum> buttonEnumList = new ArrayList<>();
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {

            buttonEnumList.add(WorkOrderButtonEnum.TERMINATE_WORK_ORDER);

            //二级公司未处理工单或最新状态未处理才显示撤回按钮
            if (flowList.stream().noneMatch(flow -> flow.getStatus().equals(ConstParam.FlowHasFeedback))
            || !StrUtil.equals(CollUtil.getLast(flowList).getStatus(), ConstParam.FlowHasFeedback)) {
                buttonEnumList.add(WorkOrderButtonEnum.WITHDRAW_WORK_ORDER);
            }

            return createdButtonList(buttonEnumList);

        } else if (operator.getRoleCodes().contains(RoleEnums.SUB_COMPANY_CUSTOMER.getCode())) {

            //未添加合同才显示退回按钮
            if (flowList.stream().noneMatch(flow -> flow.getStatus().equals(ConstParam.FlowCompanyContract))) {
                buttonEnumList.add(WorkOrderButtonEnum.RETURN_WORK_ORDER);
            }

            //工单subStatus为待处理才显示处理按钮
            if (StrUtil.equals(wfForm.getSubStatus(), ConstParam.WaitingForProcessing)) {
                buttonEnumList.add(WorkOrderButtonEnum.HANDLE_WORK_ORDER);
            }

            return createdButtonList(buttonEnumList);
        } else if (operator.getRoleCodes().contains(RoleEnums.APPLET_USER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else {
            return createdButtonList(buttonEnumList);
        }
    }

    private static List<BaseButton> getProcessedButton(WfForm wfForm, List<FlowResult> flowList, List<ContractResult> contractList, UserInfoDto operator) {
        List<WorkOrderButtonEnum> buttonEnumList = new ArrayList<>();
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else if (operator.getRoleCodes().contains(RoleEnums.SUB_COMPANY_CUSTOMER.getCode())) {

            //未添加合同才显示退回按钮
            if (flowList.stream().noneMatch(flow -> flow.getStatus().equals(ConstParam.FlowCompanyContract))) {
                buttonEnumList.add(WorkOrderButtonEnum.RETURN_WORK_ORDER);
            }

            //没有正在进行的合同才显示完成按钮
            if(contractList.stream().noneMatch(contractResult -> contractResult.getStatus().equals(ConstParam.ContractNotBegin) ||
                    contractResult.getStatus().equals(ConstParam.ContractUnderWay))){
                buttonEnumList.add(WorkOrderButtonEnum.FINISH_WORK_ORDER);
            }

            //判断是否有 ConstParam.FlowHasFeedback 或 ConstParam.FlowCompanyContract 状态才显示添加合同按钮
            Set<String> statusSet = flowList.stream()
                    .map(FlowResult::getStatus)
                    .collect(Collectors.toSet());
            if (statusSet.contains(ConstParam.FlowHasFeedback) ||
                    statusSet.contains(ConstParam.FlowCompanyContract)) {
                buttonEnumList.add(WorkOrderButtonEnum.ADD_SERVICE_CONTRACT);
            }

            return createdButtonList(buttonEnumList);
        } else if (operator.getRoleCodes().contains(RoleEnums.APPLET_USER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else {
            return createdButtonList(buttonEnumList);
        }
    }

    private static List<BaseButton> getFinishedButton(WfForm wfForm, List<FlowResult> flowList, List<ContractResult> contractList, UserInfoDto operator) {
        List<WorkOrderButtonEnum> buttonEnumList = new ArrayList<>();
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else if (operator.getRoleCodes().contains(RoleEnums.SUB_COMPANY_CUSTOMER.getCode())) {
            return createdButtonList(buttonEnumList);
        } else if (operator.getRoleCodes().contains(RoleEnums.APPLET_USER.getCode())) {
            buttonEnumList.add(WorkOrderButtonEnum.GO_TO_CONTRACT);
            return createdButtonList(buttonEnumList);
        } else {
            return createdButtonList(buttonEnumList);
        }
    }

    private static List<BaseButton> getTerminatedButton(WfForm wfForm, List<FlowResult> flowList, List<ContractResult> contractList, UserInfoDto operator) {
        return new ArrayList<BaseButton>();
    }
}
