package com.kge.energy.crm.common.page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

/**
 * @author wangjihua
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PageResp<T> {

    private Long currentPage;

    private Long pageSize;

    private Long total;

    private List<T> list;

    public PageResp(IPage<T> page) {
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("current", "currentPage");
        mapping.put("size", "pageSize");
        mapping.put("records", "list");

        BeanUtil.copyProperties(page, this, CopyOptions.create().setFieldMapping(mapping));
    }
}
