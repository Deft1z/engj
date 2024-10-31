package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BSurveyRecord;
import com.kge.energy.crm.repository.entityext.param.SurveyRecordParam;
import com.kge.energy.crm.repository.entityext.result.BSurveyRecordResult;
import com.kge.energy.crm.repository.mapper.BSurveyRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 调查表单记录表(BSurveyRecord)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@Repository
@RequiredArgsConstructor
public class BSurveyRecordDao extends ServiceImpl<BSurveyRecordMapper, BSurveyRecord> {

    private final BSurveyRecordMapper mapper;

    public IPage<BSurveyRecordResult> getByPage(SurveyRecordParam param) {
        Page<BSurveyRecordResult> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.getAll(page, param);
    }

}

