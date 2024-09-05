package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.entityext.result.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashBoardMapper {

    DashBoardStatistic selectStatistic(DashBoardParam param);

    List<DashBoardOrderContract> selectOrderContractList(DashBoardParam param);

    DashBoardUserTrans selectUserTrans(DashBoardParam param);

    List<DashBoardEvaluate> selectEvaluateList();

    Double selectEvaluateAverage();

    DashBoardComplainTypeStatistic selectComplainTypeStatistic(DashBoardParam param);

    List<DashBoardComplainRank> selectComplainRankList(DashBoardParam param);
}
