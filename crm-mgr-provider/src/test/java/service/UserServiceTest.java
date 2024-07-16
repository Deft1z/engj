package service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.CrmMgrProvider;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.user.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CrmMgrProvider.class)
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private BUserDao bUserDao;


    @Test
    void findWxUserList() {

        bUserDao.getBaseMapper().selectPage(new Page<>(1L, 10L), new LambdaQueryWrapper<>());

//        userService.findWxUserList(new WxUserListReq().setCurrentPage(1L).setPageSize(10L));
    }


}