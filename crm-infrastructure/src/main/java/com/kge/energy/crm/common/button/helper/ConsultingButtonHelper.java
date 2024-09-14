package com.kge.energy.crm.common.button.helper;

import com.kge.energy.crm.common.button.enums.ConsultingButtonEnum;
import com.kge.energy.crm.common.button.resp.BaseButton;

import java.util.List;

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
        return createdButton(ConsultingButtonEnum.HANDLE_CONSULTING);
    }


}
