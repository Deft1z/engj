package iam;

import com.kge.energy.crm.CrmMgrProvider;
import com.kge.energy.crm.external.iam.service.IamService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CrmMgrProvider.class)
class IamServiceTest {

    @Resource
    private IamService iamService;

    @Test
    void checkTicket() {

        String ticket = "iam@52b8f8c683d343be8a2732972e5d017f";
        iamService.checkTicket(ticket);
    }

    @Test
    void getUserForToken() {
    }
}
