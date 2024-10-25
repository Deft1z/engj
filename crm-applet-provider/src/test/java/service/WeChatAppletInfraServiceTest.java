package service;

import com.kge.energy.crm.CrmAppletProvider;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = CrmAppletProvider.class)
class WeChatAppletInfraServiceTest {

    @Resource
    private WeChatAppletInfraService weChatAppletInfraService;

    @Test
    void getAccessToken() {
        String accessToken = weChatAppletInfraService.getAccessToken();
        System.out.println(accessToken);
    }
}
