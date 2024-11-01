package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BSurveyRecord;
import com.kge.energy.crm.repository.entityext.param.SurveyRecordParam;
import com.kge.energy.crm.repository.entityext.result.BSurveyRecordResult;
import org.apache.ibatis.annotations.Param;

/**
 * 调查表单记录表(BSurveyRecord)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:35
 */
public interface BSurveyRecordMapper extends BaseMapper<BSurveyRecord> {

    IPage<BSurveyRecordResult> getAll(Page<BSurveyRecordResult> page, @Param("param") SurveyRecordParam param);

}

