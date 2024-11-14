package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.IamUser;
import com.kge.energy.crm.repository.entityext.result.IamUserResult;
import org.apache.ibatis.annotations.Param;

/**
 * iam用户表(IamUser)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:38
 */
public interface IamUserMapper extends BaseMapper<IamUser> {

    IamUserResult getByUserId(@Param("userId") String userId, @Param("modifyTime") String modifyTime);

    String getLatestModifyTime();

}

