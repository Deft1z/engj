package com.kge.energy.crm.tmp;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entity.CmsBlock;
import com.kge.energy.crm.repository.mapper.CmsBlockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WashDataService {

    private final JdbcTemplate jdbcTemplate;

    private final CmsBlockMapper cmsBlockMapper;

    private final BResourceDao bResourceDao;

    @Transactional
    public void insertResource() {

        // 小程序端
        List<CmsBlock> cmsBlockList = new LambdaQueryChainWrapper<>(CmsBlock.class)
                .in(CmsBlock::getType, "首页模块", "家园模块")
                .orderByAsc(CmsBlock::getType)
                .orderByAsc(CmsBlock::getCode)
                .list();

//        String sql = "select a.block_id,a.block_content_id,\n" +
//                "       f.app_uuid,\n" +
//                "       f.app_id,\n" +
//                "       f.name     AS app_name,\n" +
//                "       f.app_address,\n" +
//                "       f.bind_type,\n" +
//                "       f.bind_address,\n" +
//                "       f.scope,\n" +
//                "       a.title,\n" +
//                "       a.desc,\n" +
//                "       d.type,\n" +
//                "       b.filepath AS image_url,\n" +
//                "       c.path     AS page_url,\n" +
//                "       a.remark,\n" +
//                "       e.filepath AS page_name\n" +
//                "from cms_block_content a\n" +
//                "         LEFT JOIN s_file b ON b.file_id = a.image_url\n" +
//                "         LEFT JOIN wx_pages c ON c.pages_id = a.page_url\n" +
//                "         LEFT JOIN cms_block d ON d.block_id = a.block_id\n" +
//                "         LEFT JOIN s_file e ON e.file_id = a.page_file\n" +
//                "         LEFT JOIN b_app f ON f.app_id = a.app_id\n" +
//                "where  a.flag != -1\n" +
//                "  And d.flag != -1\n" +
//                "order by a.block_id,a.block_content_id";
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);

        List<BResource> bResources = new ArrayList<>();
        Integer resourceId = 299;

        for (CmsBlock cmsBlock : cmsBlockList) {

//            maps.stream()
//                    .filter(map -> map.get("block_id").equals(cmsBlock.getBlockId()))
//                    .forEach(map -> {
//
//                    });
        }


    }
}
