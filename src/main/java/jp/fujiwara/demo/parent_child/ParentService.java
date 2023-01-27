package jp.fujiwara.demo.parent_child;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

/**
 * 親のみが行う処理
 */
@RequiredArgsConstructor
@Service
public class ParentService {
    private final ParentDataService parentDataService;
    private final GlobalStateService globalStateService;
    private final RestTemplate restTemplate;
    private final Log log;
    private final NightService nightService;
    private final EveningService eveningService;

    public void init() {
    }

    /**
     * 全ての子に現在のゲームの状態を通知する。
     * 自分のゲームの状態も変更する。
     * 初期化時のinit以外の処理も実行する
     * 
     * @param state 新しいゲームの状態
     */
    public void notifyStateToChildren(GameState state) {
        globalStateService.set(state);
        switch (state) {
            case NIGHT:
                nightService.start();
                break;
            case EVENING:
                eveningService.start();
                break;
            default:
        }
        for (final ParticipantModel child : parentDataService.children()) {
            final String url = "http://" + child.getIpAddress() + "/child/set_state";
            log.debug("send next state to " + url);
            restTemplate.postForObject(url, state, ResponseStatus.class);
        }
    }

    public void checkIfGameSet() {
        // 殺された各陣営の人数を確認
        int killedWerewolf = 0;
        int killedPeople = 0;
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (!player.isKilled()) {
                continue;
            }
            switch (player.getRoll()) {
                case WEREWOLF:
                    killedWerewolf++;
                    break;
                default:
                    killedPeople++;
            }
        }
        log.info("killed werewolf:" + killedWerewolf);
        log.info("killed people:" + killedPeople);

        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        final int werewolfNum = 1;
        if (killedWerewolf == werewolfNum) {
            // 人間陣営の勝ち
            notifyStateToChildren(GameState.PEOPLE_WON);
            return;
        }
        if ((numOfPlayers - killedPeople - killedWerewolf) <= werewolfNum) {
            // 人狼陣営の勝ち
            notifyStateToChildren(GameState.WEREWOLF_WON);
            return;
        }
    }
}
