package jp.fujiwara.demo.roll_definition;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.math.Shamir;
import jp.fujiwara.demo.math.ShamirsShare;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.parent_child.ParentService;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShareRollService {
    private final NightService nightService;
    private final EveningService eveningService;
    private final ParentService parentService;
    private final GlobalStateService globalStateService;
    private final RestTemplate restTemplate;
    private final Shamir shamir;
    private final Log log;

    public void init() {
        share();
    }

    /**
     * 参加者に役職情報のシェアを分散
     */
    public void share() {
        log.debug("*****ShareRollService.share*****");
        final int numOfPlayers = globalStateService.getNumberOfParticipants();

        // シェアの作成
        final List<Integer> code = globalStateService.getRoll().rollCode.getCodes();
        final ShamirsShare[] share0 = shamir.prepare(code.get(0), numOfPlayers - 1, numOfPlayers);
        final ShamirsShare[] share1 = shamir.prepare(code.get(1), numOfPlayers - 1, numOfPlayers);
        log.debug("share0 from " + code.get(0) + " :");
        for (final ShamirsShare share : share0) {
            log.debug(String.format("(%s, %s)", share.getX(), share.getY()));
        }
        log.debug("share1 from " + code.get(1) + " :");
        for (final ShamirsShare share : share1) {
            log.debug(String.format("(%s, %s)", share.getX(), share.getY()));
        }

        // 各参加者にシェアを分散
        int i = 0;
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            final List<ShamirsShare> share = new ArrayList<>();
            share.add(share0[i]);
            share.add(share1[i]);

            // 自分自身のシェアはPOSTせずに保存する。
            if (player.getNumber() == globalStateService.getMyId()) {
                storeShare(player.getNumber(), share);
                continue;
            }

            // 送信
            final String url = "http://" + player.getIpAddress() + "/roll/share";
            log.debug("url:" + url);
            log.debug(String.format("send (%d, %d) (%d, %d) to %s(%d)",
                    share0[i].getX(),
                    share0[i].getY(),
                    share1[i].getX(),
                    share1[i].getY(),
                    player.getPlayerName(),
                    player.getNumber()));

            final ShareRollModel shareRollModel = new ShareRollModel(globalStateService.getMyId(), share);
            try {
                restTemplate.postForObject(url, shareRollModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
            i++;
        }

        if (globalStateService.getIsParent()) {
            finish();
        }
    }

    /**
     * シェア情報の保存
     * 
     * @param shareRollModel シェア情報
     */
    public void storeShare(int id, List<ShamirsShare> share) {
        globalStateService.set(id, share);
    }

    /**
     * 役職を決める処理の最後。
     * 親が実行する。
     */
    public void finish() {
        // nightService.init();
        // parentService.notifyStateToChildren(GameState.NIGHT);
        eveningService.init();
        parentService.notifyStateToChildren(GameState.EVENING);
    }
}
