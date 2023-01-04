package jp.fujiwara.demo.start;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.utils.GetIpAddress;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StartServiceTest {
    @Autowired
    private StartService startService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GetIpAddress getIpAddress;

    @Test
    void sendToParent() {
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        // 前提
        final String playerName = "player";
        final String myIp = getIpAddress.getIpAddressWithPort();
        final String parentIp = "192.168.11.2:8081";

        // mock modelの用意
        final StartModel startModel = new StartModel();
        startModel.setIsParent(false);
        startModel.setPlayerName(playerName);
        startModel.setParentIpAddress(parentIp);

        // テストする内容
        final String testUrl = "http://" + parentIp + "/parent/set_child";
        final String expectedContent = "{\"playerName\":\"" + playerName + "\",\"ip\":\"" + myIp + "\"}";
        final String responseBody = "{\"status\":\"ok\"}";
        mockServer
                .expect(requestTo(testUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(expectedContent))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        // テストの実行
        startService.sendToParent(startModel);
    }
}
