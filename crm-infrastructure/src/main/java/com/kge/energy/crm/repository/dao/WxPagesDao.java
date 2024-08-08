package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WxPagesMapper;
import com.kge.energy.crm.repository.entity.WxPages;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * wx_pages 微信小程序页面(WxPages)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WxPagesDao extends ServiceImpl<WxPagesMapper, WxPages> {

    private final WxPagesMapper mapper;

}

