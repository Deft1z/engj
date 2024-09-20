package com.kge.energy.crm.common.button.helper;


import com.kge.energy.crm.common.button.enums.ButtonEnum;
import com.kge.energy.crm.common.button.resp.BaseButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 按钮处理器抽象类
 *
 * @author wangjihua
 */
public class AbstractButtonHelper {

    public static List<BaseButton> createdButton(ButtonEnum... buttonEnums) {
        List<BaseButton> list = new ArrayList<>();
        for (ButtonEnum buttonEnum : buttonEnums) {
            list.add(new BaseButton()
                    .setCode(buttonEnum.getCode())
                    .setName(buttonEnum.getName())
                    .setEnabled(buttonEnum.getEnabled())
                    .setHint(buttonEnum.getHint()));
        }
        return list;
    }

    public static BaseButton createdButton(ButtonEnum buttonEnum, boolean enabled, String hint) {
        return new BaseButton()
                .setCode(buttonEnum.getCode())
                .setName(buttonEnum.getName())
                .setEnabled(enabled)
                .setHint(hint);
    }

    public static BaseButton createdSingleButton(ButtonEnum buttonEnum) {
        return new BaseButton()
                .setCode(buttonEnum.getCode())
                .setName(buttonEnum.getName())
                .setEnabled(buttonEnum.getEnabled())
                .setHint(buttonEnum.getHint());
    }
}
