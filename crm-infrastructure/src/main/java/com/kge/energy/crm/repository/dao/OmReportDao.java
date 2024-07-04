package com.kge.energy.crm.repository.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.repository.entityext.param.OmReportListParam;
import com.kge.energy.crm.repository.mapper.OmReportMapper;
import com.kge.energy.crm.repository.entity.OmReport;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;

/**
 * om_report(OmReport)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class OmReportDao extends ServiceImpl<OmReportMapper, OmReport> {

    private final OmReportMapper mapper;

    /**
     * 方式一：通过 mybatis-plus 自带的方法，适用于单表查询，条件特别少的情况
     */
    public List<OmReport> getList(OmReportListParam param) {
        if (ObjUtil.isNull(param)) {
            return Collections.EMPTY_LIST;
        }

        LambdaQueryWrapper<OmReport> wrapper = Wrappers.<OmReport>lambdaQuery()
                .eq(OmReport::getOperator, param.getOperator());

        return mapper.selectList(wrapper);
    }

    /**
     * 方式二：通过 mybatis xml 的方式，适用所有情况，特别是条件特别复杂的情况
     */
    public List<OmReport> getList2(OmReportListParam param) {
        if (ObjUtil.isNull(param)) {
            return Collections.EMPTY_LIST;
        }

        return mapper.getList(param);
    }
}

