package external.ct.service;

import com.kge.energy.crm.CrmAppletProvider;
import com.kge.energy.crm.external.ct.req.CtRemoteResp;
import com.kge.energy.crm.external.ct.req.CtTokenReq;
import com.kge.energy.crm.external.ct.service.CtService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = CrmAppletProvider.class)
class CtServiceTest {

    @Resource
    private CtService ctService;

    @Test
    void getCtToken() throws Exception {
        CtTokenReq ctTokenReq = new CtTokenReq()
                .setOpenid(292)
                .setAppId(1)
                .setAppSecret("179dd20e0d1cdf48f791355430000001")
                .setInterfaceAddress("https://energy.en168.net:8100/api/pbp-system/v1");
        CtRemoteResp ctToken = ctService.getCtToken(ctTokenReq);
        log.info("{}", ctToken);
    }

    @Test
    void accountUnbind() {
    }
}
