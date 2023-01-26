package jp.fujiwara.demo.night;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.global.RollCode;
import jp.fujiwara.demo.global.child.ChildDataService;
import jp.fujiwara.demo.math.AdditiveSecretSharing;
import jp.fujiwara.demo.math.Shamir;
import jp.fujiwara.demo.math.ShamirsShare;
import jp.fujiwara.demo.night.model.ActionResultModel;
import jp.fujiwara.demo.night.model.ActionShareModel;
import jp.fujiwara.demo.night.model.DeadOrAliveModel;
import jp.fujiwara.demo.night.model.KilledInfoModel;
import jp.fujiwara.demo.night.model.KilledRollShareModel;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NightService {
    private final GlobalStateService globalStateService;
    private final AdditiveSecretSharing additiveSecretSharing;
    private final RestTemplate restTemplate;
    private final Log log;
    private final ChildDataService childDataService;
    private final Shamir shamir;

    /**
     * 行動は決定したか
     */
    @Getter
    private boolean actionDecided = false;
    /**
     * 各個人のシェアの計算のための値（合計する）
     */
    private int killShareSum = 0;
    /**
     * 各個人が他のプレイヤーから取得したシェアの数
     */
    private int numOfShare = 0;
    /**
     * 親においてシェアの集計用に使う
     */
    private List<Integer> totalShare;
    /**
     * 誰を殺すと選択したか
     * 人狼以外は全員0を指定する
     */
    private int killSelect = 0;
    /**
     * 誰かが殺されたか
     */
    @Getter
    private boolean somebodyKilled = false;
    /**
     * 結果が出たか
     */
    @Getter
    private boolean resulted = false;
    /**
     * 殺された人のid
     */
    @Getter
    private Integer killedId = null;
    /**
     * 殺された人の役職
     */
    @Getter
    private Roll killedRoll = null;
    /**
     * 人狼に殺された人の情報のシェアを合計するための値
     */
    private int decideKillShareSum = 0;
    /**
     * 人狼に殺された人の情報のシェアを受け取った数
     */
    private int decidedKillShareNum = 0;
    /**
     * 親において人狼に殺された人の情報のシェアの集計に使う
     */
    private List<Integer> totalDecideKillShare;

    /**
     * 初期化
     */
    public void init() {
        actionDecided = false;
        killShareSum = 0;
        numOfShare = 0;
        totalShare = new ArrayList<>();
        killSelect = 0;
        somebodyKilled = false;
        resulted = false;
        killedId = null;
        killedRoll = null;
        decideKillShareSum = 0;
        decidedKillShareNum = 0;
        totalDecideKillShare = new ArrayList<>();
    }

    /**
     * @param selectedNumber 人狼が食い殺すと決めた人のid
     */
    public void werewolfAction(Integer selectedNumber) {
        actionDecided = true;
        killSelect = selectedNumber;
        distributeActionInfo(-selectedNumber);
    }

    /**
     * @param selectedNumber 騎士が守ると決めた人のid
     */
    public void knightAction(Integer selectedNumber) {
        actionDecided = true;
        killSelect = 0;
        distributeActionInfo(selectedNumber);
    }

    /**
     * その他の人もシェアを分配する。
     */
    public void othersAction() {
        actionDecided = true;
        killSelect = 0;
        distributeActionInfo(0);
    }

    /**
     * 各参加者が自分の行動に関するシェアを送る
     * 
     * @param killNum 人狼と騎士の攻防のための値。
     */
    public void distributeActionInfo(int killNum) {
        final int numOfPlayers = globalStateService.getNumberOfParticipants();

        // シェアの作成
        final Integer[] share = additiveSecretSharing.prepare(killNum, numOfPlayers);

        // シェアの分散
        int i = 0;
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                getActionShare(share[i++]);
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/night/action_share";
            log.debug("send to " + url);
            final ActionShareModel actionShareModel = new ActionShareModel(share[i++]);
            log.debug("share :" + actionShareModel.getKillShare());
            try {
                restTemplate.postForObject(url, actionShareModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 各参加者が受け取った、各参加者の行動に関するシェアを編集して、親に送る
     * 
     * @param killShare 人狼と騎士の攻防のための情報のシェア
     */
    public void getActionShare(int killShare) {
        numOfShare++;
        killShareSum += killShare;

        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        if (numOfShare < numOfPlayers) {
            return;
        }
        // 全ての参加者のシェアが集まった
        log.debug("sum of share is " + killShareSum);

        if (globalStateService.getIsParent()) {
            totalShare(killShareSum);
        } else {
            final String url = "http://" + childDataService.getParentIpAddress() + "/night/total";
            log.debug("send to " + url);
            final ActionShareModel actionShareModel = new ActionShareModel(killShareSum);
            try {
                restTemplate.postForObject(url, actionShareModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 親が各参加者から、各参加者の行動に関するシェアを集計する
     * 結果は各参加者に送られる
     * 
     * @param share 人狼と騎士の攻防の結果の情報のシェア
     */
    public void totalShare(int share) {
        totalShare.add(share);

        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        if (totalShare.size() < numOfPlayers) {
            return;
        }
        // 全てのシェアを収集し終わった。

        // シェアから結果を取得
        final Integer[] totalShareArray = totalShare.toArray(new Integer[numOfPlayers]);
        final int deadOrAlive = additiveSecretSharing.reconstruct(totalShareArray);
        final boolean killed = deadOrAlive != 0;
        log.info(String.format("dead or alive: %s", killed ? "dead" : "alive"));
        final DeadOrAliveModel deadOrAliveModel = new DeadOrAliveModel(killed);

        // 誰かが殺されたかどうかを共有
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                if (killed) {
                    distributeToConfirmWhoToKill();
                } else {
                    nobodyKilled();
                }
                continue;
            }
            final String url = "http://" + player.getIpAddress() + "/night/deadOrAlive";
            log.debug("send to " + url);
            try {
                restTemplate.postForObject(url, deadOrAliveModel,
                        ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 誰も殺されなかった。
     */
    public void nobodyKilled() {
        log.info("no body has killed.");
        resulted = true;
        somebodyKilled = false;
    }

    /**
     * 誰かが殺されたので、
     * 人狼が誰を食い殺したか、もう一度シェアを配りあう
     */
    public void distributeToConfirmWhoToKill() {
        log.info("somebody has killed.");
        final int numOfPlayers = globalStateService.getNumberOfParticipants();

        final int sendingKillId;

        switch (globalStateService.getRoll()) {
            case WEREWOLF:
                sendingKillId = killSelect;
                break;
            default:
                sendingKillId = 0;
        }

        log.debug(String.format("who i will kill is %d", sendingKillId));

        // シェアの作成
        final Integer[] share = additiveSecretSharing.prepare(sendingKillId, numOfPlayers);

        // シェアの分散
        int i = 0;
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                log.debug("send to me");
                log.debug("share:" + share[i]);
                getKillShare(share[i++]);
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/night/killed_share";
            log.debug("send to " + url);
            final ActionShareModel actionShareModel = new ActionShareModel(share[i++]);
            log.debug("share:" + actionShareModel.getKillShare());
            try {
                restTemplate.postForObject(url, actionShareModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 受け取った人狼が食い殺した人の情報を親に送り直す
     */
    public void getKillShare(int killShare) {
        log.debug("*****NightService.getKillShare*****");
        log.debug("the received share : " + killShare);
        decidedKillShareNum++;
        decideKillShareSum += killShare;

        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        if (decidedKillShareNum < numOfPlayers) {
            return;
        }
        // 全ての参加者のシェアが集まった
        log.debug("sum of share is " + decideKillShareSum);

        if (globalStateService.getIsParent()) {
            totalKillShare(decideKillShareSum);
            return;
        }

        final String url = "http://" + childDataService.getParentIpAddress() + "/night/total_killed";
        log.debug("send to " + url);
        final ActionShareModel actionShareModel = new ActionShareModel(decideKillShareSum);
        try {
            restTemplate.postForObject(url, actionShareModel, ResponseStatus.class);
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 親が誰が殺されたかを集計する。
     * 
     * @param killShare 人狼が食い殺した人の情報のシェア
     */
    public void totalKillShare(int killShare) {
        log.debug("*****NightService.totalKillShare*****");
        log.debug("the received share : " + killShare);
        totalDecideKillShare.add(killShare);

        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        if (totalDecideKillShare.size() < numOfPlayers) {
            return;
        }
        // 全てのシェアが集まった

        // 誰が殺されたかを確認
        final Integer[] shareList = totalDecideKillShare.toArray(new Integer[numOfPlayers]);
        final int killedId = additiveSecretSharing.reconstruct(shareList);
        log.debug(String.format("the killed was %d", killedId));

        // 殺された人が誰かを送信
        // 殺された人の役職に関するシェアを収集
        final ActionResultModel actionResultModel = new ActionResultModel(killedId);
        int i = 0;
        final ShamirsShare[] share0 = new ShamirsShare[numOfPlayers - 1];
        final ShamirsShare[] share1 = new ShamirsShare[numOfPlayers - 1];
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                final List<ShamirsShare> shares = globalStateService.getRollShareFor(killedId);
                log.debug(String.format("player %s's roll share size is %d", player.getPlayerName(), shares.size()));
                share0[i] = shares.get(0);
                share1[i] = shares.get(1);
                i++;
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/night/who_killed";
            log.debug("send to " + url);
            try {
                final KilledRollShareModel killedRollShareModel = restTemplate.postForObject(url, actionResultModel,
                        KilledRollShareModel.class);
                if (killedId != player.getNumber()) {
                    final List<ShamirsShare> shares = killedRollShareModel.getRollShare();
                    log.debug(
                            String.format("player %s's roll share size is %d", player.getPlayerName(), shares.size()));
                    share0[i] = shares.get(0);
                    share1[i] = shares.get(1);
                    i++;
                }
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }

        // 殺された人の役職の情報を復元
        final int rollCode0 = shamir.reconstruct(share0, numOfPlayers - 1);
        final int rollCode1 = shamir.reconstruct(share1, numOfPlayers - 1);
        final Roll killedRoll = Roll.from(new RollCode(rollCode0, rollCode1));

        // 処刑されたプレイヤーの役職を全てのプレイヤーに送信
        final KilledInfoModel completeExecutedInfoModel = new KilledInfoModel(killedId,
                killedRoll);
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                killedWas(killedId, killedRoll);
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/night/killed_complete_info";
            log.debug("send to " + url);
            try {
                restTemplate.postForObject(url, completeExecutedInfoModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 親が集計した、人狼に殺された人の情報を受け取る
     */
    public void killedWas(int killedId, Roll killedRoll) {
        somebodyKilled = true;
        this.killedId = killedId;
        this.killedRoll = killedRoll;
        resulted = true;
    }
}
