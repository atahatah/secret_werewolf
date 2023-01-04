package jp.fujiwara.demo.parent_child;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.parent.ParentDataService;
import lombok.RequiredArgsConstructor;

/**
 * 親のみが行う処理
 */
@RequiredArgsConstructor
@Service
public class ParentService {
    private final ParentDataService parentDataService;
    private final GlobalStateService globalStateService;

    /**
     * 全ての子に現在のゲームの状態を通知する。
     * 自分のゲームの状態も変更する。
     * 
     * @param state 新しいゲームの状態
     */
    public void notifyStateToChildren(GameState state) {
        globalStateService.set(state);
        for (final ParticipantModel child : parentDataService.children()) {
            final String url = "http://" + child.getIpAddress() + "/child/notice_participants_info";
            final RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(url, state, GameState.class);
        }
    }
}
