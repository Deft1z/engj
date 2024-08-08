package service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.CrmMgrProvider;
import com.kge.energy.crm.app.resp.App;
import com.kge.energy.crm.app.resp.ListContent;
import com.kge.energy.crm.app.resp.Project;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.user.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = CrmMgrProvider.class)
class UserServiceTest {

    @Resource
    private BUserDao bUserDao;


    @Test
    void findWxUserList() {

        bUserDao.getBaseMapper().selectPage(new Page<>(1L, 10L), new LambdaQueryWrapper<>());

//        userService.findWxUserList(new WxUserListReq().setCurrentPage(1L).setPageSize(10L));
    }


    @Test
    void findListChange() {

        List<ListContent> result = new ArrayList<>();

        for (int i = 0 ; i <= 5 ; i++){
            Project p1 = new Project();
            p1.setId(1);
            p1.setName("userK.getPname()");

            List<App> appsNull = new ArrayList<>();
            ListContent c1 = new ListContent();
            c1.setUid(3);
            c1.setName("userK.getRealname()");
            c1.setMobile("userK.getMobile()");
            c1.setApps(appsNull);

            App a1 = new App();
            List<Project> pnull = new ArrayList<>();
            a1.setName("apps.get(recordMap.get(userK.getAppid() - 1)).getName()");
            a1.setId(2);
            a1.setOid(2);
            a1.setProjects(pnull);


            List<App> appList = new ArrayList<>();
            appList.add(a1);
            c1.setApps(appList);
            result.add(c1);
        }
        System.out.println("初始: reslut = "+result);

        result.get(0).setUid(9);

        System.out.println("改变后: reslut = "+result);

        // 有效
        for (ListContent re : result) {
            re.setUid(98);
        }
        System.out.println("循环变后: reslut = "+result);


    }


}