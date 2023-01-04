package jp.fujiwara.demo.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.parent.ParentDataService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class StartRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ParentDataService parentDataService;

    @Test
    public void test() throws Exception {
        // 前提
        final String playerName = "player";
        final String ip = "192.415.24.124:8089";

        // テストデータの作成
        final RowChildDataModel rowChildDataModel = new RowChildDataModel(playerName, ip);
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(rowChildDataModel);

        // 実行
        mockMvc.perform(
                post("/parent/set_child")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"ok\"}"));

        // 確認
        final ParticipantModel one = parentDataService.getParticipants().get(0);
        assertEquals(playerName, one.getPlayerName());
        assertEquals(ip, one.getIpAddress());
        assertEquals(parentDataService.getParticipants().size(), 1);
    }
}
