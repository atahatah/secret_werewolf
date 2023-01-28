package jp.fujiwara.demo.evening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.evening.model.CompleteExecutedInfoModel;
import jp.fujiwara.demo.evening.model.ExecutedModel;
import jp.fujiwara.demo.evening.model.ExecutedPlayersJobModel;
import jp.fujiwara.demo.evening.model.ShareVoteModel;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.global.RollCode;
import jp.fujiwara.demo.global.child.ChildDataService;
import jp.fujiwara.demo.math.AdditiveSecretSharing;
import jp.fujiwara.demo.math.Shamir;
import jp.fujiwara.demo.math.ShamirsShare;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EveningService {
    private final AdditiveSecretSharing additiveSecretSharing;
    private final GlobalStateService globalStateService;
    private final RestTemplate restTemplate;
    private final Log log;
    private final ChildDataService childDataService;
    private final Shamir shamir;

    /**
     * 各個人のシェアの計算のための値（合計する）
     */
    private int shareSum = 0;
    /**
     * 各個人が他のプレイヤーから取得したシェアの数
     */
    private int numOfShare = 0;
    /**
     * 親が投票結果の集計に使う
     */
    private List<Integer> totalShare;
    /**
     * 投票は終了したか
     */
    @Getter
    private boolean voted = false;
    /**
     * 投票の集計結果は取得したか
     */
    @Getter
    private boolean resulted = false;
    /**
     * 最も直近で処刑されたプレイヤーのid
     */
    @Getter
    private Integer recentExecutedId = null;
    /**
     * 最も直近で処刑されたプレイヤーの役職
     */
    @Getter
    private Roll recentExecutedRoll = null;

    /**
     * 初期化
     */
    public void init() {
        shareSum = 0;
        numOfShare = 0;
        totalShare = new ArrayList<>();
        voted = false;
        resulted = false;
        recentExecutedId = null;
        recentExecutedRoll = null;
    }

    /**
     * 状態の最初に実行が必要な処理
     */
    public void start() {
        // 死んでいる人は自動的に存在しない人に投票
        if (globalStateService.getHasKilled()) {
            shareVote(globalStateService.getNumberOfParticipants() + 1);
        }
    }

    /**
     * 投票された情報を秘密分散する。
     * そのためにシェアを全ての参加者へ送る。
     * 
     * @param number 投票した人のID
     */
    public void shareVote(int number) {
        log.debug("******EveningService.shareVote*****");
        voted = true;

        // シェアの生成
        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        log.info("player voted to " + number);
        final Integer[] share = additiveSecretSharing.createShareForVote(number, numOfPlayers);
        log.debug("the shares are :");
        for (int n : share) {
            log.debug("" + n);
        }

        int i = 0;
        // シェアの送信
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            log.debug(String.format("send share %d to %s(%d)", share[i], player.getPlayerName(), player.getNumber()));

            // 自分に対して送るシェア
            if (player.getNumber() == globalStateService.getMyId()) {
                gatherShare(share[i]);
                i++;
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/evening/share_vote";
            final ShareVoteModel shareVoteModel = new ShareVoteModel(share[i]);
            try {
                restTemplate.postForObject(url, shareVoteModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
            i++;
        }
    }

    /**
     * 投票された情報のシェアを集めるためのメソッド。
     * このメソッド中で、シェア同士の足し算も実行し全ての投票結果を足し合わせることも行う。
     * また、全てのシェアを集め切ると、親にそのシェアを送付する。
     * 
     * @param share 受け取った、投票された情報のシェア
     */
    public void gatherShare(int share) {
        log.debug("******EveningService.gatherShare*****");
        numOfShare++;
        shareSum += share;
        if (numOfShare < globalStateService.getNumberOfParticipants()) {
            return;
        }
        // 全てのシェアが集まった
        log.debug("sum of share is " + shareSum);

        if (globalStateService.getIsParent()) {
            totalShare(shareSum);
        } else {
            final String url = "http://" + childDataService.getParentIpAddress() + "/evening/total";
            final ShareVoteModel shareVoteModel = new ShareVoteModel(shareSum);
            try {
                restTemplate.postForObject(url, shareVoteModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 親において、投票結果を受け取り、全ての結果を受け取ると集計する。
     * その後、各参加者に集計結果を通知する。
     * 
     * @param share 受け取った、投票結果の情報のシェア
     */
    public void totalShare(int share) {

        totalShare.add(share);

        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        if (totalShare.size() < numOfPlayers) {
            return;
        }
        // 全てのシェアを合算し終わった

        // シェアから得票数を復元
        Integer[] shareAsArray = totalShare.toArray(new Integer[numOfPlayers]);
        final int[] voteNum = additiveSecretSharing.reconstructVote(shareAsArray);

        // 処刑者の決定
        List<Integer> maxNumber = new ArrayList<>();
        int maxVoteNum = -1;
        for (int i = 0; i < numOfPlayers; i++) {
            log.debug(String.format("%d %d", i, voteNum[i]));
            if (maxVoteNum < voteNum[i]) {
                maxNumber = new ArrayList<>();
                maxNumber.add(i);
                maxVoteNum = voteNum[i];
            } else if (maxVoteNum == voteNum[i]) {
                maxNumber.add(i);
            }
        }
        final Integer executed = maxNumber.get(0);
        log.info("the person who executed is " + executed);

        final Map<Integer, ExecutedPlayersJobModel> returnedMap = new HashMap<>();
        // 処刑者の通知
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/evening/executed";
            final ExecutedModel executedModel = new ExecutedModel(executed);
            try {
                ExecutedPlayersJobModel returned = restTemplate.postForObject(url, executedModel,
                        ExecutedPlayersJobModel.class);
                returnedMap.put(player.getNumber(), returned);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }

        // 処刑されたプレイヤーの役職をシェアから復元
        final List<ShamirsShare> share0List = new ArrayList<>();
        final List<ShamirsShare> share1List = new ArrayList<>();

        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == executed) {
                continue;
            }
            if (player.getNumber() == globalStateService.getMyId()) {
                final List<ShamirsShare> shares = globalStateService.getRollShareFor(executed);
                log.debug(String.format("player %s's roll share size is %d", player.getPlayerName(), shares.size()));
                share0List.add(shares.get(0));
                share1List.add(shares.get(1));
                continue;
            }
            final List<ShamirsShare> shares = returnedMap.get(player.getNumber()).getShare();
            log.debug(String.format("player %s's roll share size is %d", player.getPlayerName(), shares.size()));
            share0List.add(shares.get(0));
            share1List.add(shares.get(1));
        }

        final ShamirsShare[] share0 = share0List.toArray(new ShamirsShare[numOfPlayers - 1]);
        final ShamirsShare[] share1 = share1List.toArray(new ShamirsShare[numOfPlayers - 1]);
        final int rollCode0 = shamir.reconstruct(share0, numOfPlayers - 1);
        final int rollCode1 = shamir.reconstruct(share1, numOfPlayers - 1);

        final Roll executedRoll = Roll.from(new RollCode(rollCode0, rollCode1));
        log.info(String.format("the roll of who executed is %s", executedRoll.name));

        // 処刑されたプレイヤーの役職を全てのプレイヤーに送信
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                executedIs(executed, executedRoll);
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/evening/executed_complete_info";
            final CompleteExecutedInfoModel completeExecutedInfoModel = new CompleteExecutedInfoModel(executed,
                    executedRoll);
            try {
                restTemplate.postForObject(url, completeExecutedInfoModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 処刑者の通知を受け取る。
     * 
     * @param executedId 処刑者のid
     * @param executedId 処刑者の役職
     */
    public void executedIs(Integer executedId, Roll executedRoll) {
        resulted = true;
        this.recentExecutedId = executedId;
        this.recentExecutedRoll = executedRoll;

        globalStateService.killed(executedId, executedRoll);
        log.info(String.format("player %d has killed.", executedId));
    }
}
