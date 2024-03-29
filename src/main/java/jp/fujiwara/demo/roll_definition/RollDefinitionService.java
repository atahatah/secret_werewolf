package jp.fujiwara.demo.roll_definition;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.math.RandomNum;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RollDefinitionService {
    /**
     * ロールを決めるときに使うランダムな数字の最大値
     */
    private final int MAX_RANDOM_NUM = 100;
    /**
     * 処理の状態を保持するクラス
     */
    private final RollDefinitionStateModel stateModel = new RollDefinitionStateModel();

    private final GlobalStateService globalStateService;
    private final RandomNum randomNum;
    private final RestTemplate restTemplate;
    private final Log log;
    private final ShareRollService shareRollService;

    public void init() {
        stateModel.setLoop(1);
    }

    /**
     * 既にロールが決定しているか
     * 
     * @return ロールが決定していればtrue
     */
    public boolean hasRollDefined() {
        return globalStateService.getRoll() != null;
    }

    /**
     * 役を決める時に使う大小比較のための数字。
     * 既に役が決まっている場合は0を返す。
     * 
     * @return ランダムな数字
     */
    public int sampleRollNumber() {
        if (hasRollDefined()) {
            return 0;
        }
        return randomNum.between(1, MAX_RANDOM_NUM);
    }

    /**
     * 親が1つのロールを決定するサイクルを開始するときに初期化する処理
     */
    public void initRollDefinition() {
        log.debug("*****RollDefinitionRestController.initRollDefinition*****");
        log.debug("this loop is " + stateModel.getLoop());
        final int myRandomNumber = sampleRollNumber();
        log.debug(String.format("my random number is %d", myRandomNumber));
        setMyRandomNumber(myRandomNumber);
        final int sendingRandomNumber = myRandomNumber == 0 ? 0 : randomNum.between(1, myRandomNumber);
        log.debug(String.format("but send the random number %d", sendingRandomNumber));
        sendRandomNumberToNext(sendingRandomNumber, "/roll/comp_num");
    }

    /**
     * ロールを決めるときの１回目のループで作った自分のランダムな数字。
     * 2回目のループで回ってきた数字が自分のものか確認する必要がある。
     * 
     * @param myRandomNumber 自分のランダムな数字
     */
    public void setMyRandomNumber(int myRandomNumber) {
        stateModel.setMyRandomNumber(myRandomNumber);
    }

    /**
     * これまでの参加者のうち最も大きなランダムな数字を、次の参加者に送る。
     * 
     * @param theBiggerNumber これまでで最も大きなランダムな数字
     * @param path
     */
    public void sendRandomNumberToNext(int theBiggerNumber, String path) {
        final ParticipantModel next = globalStateService.getNextParticipant();
        final String nextIp = next.getIpAddress();

        // 次の参加者に大きい方の数字を送る
        final String url = "http://" + nextIp + path;
        final RollNumber rollNumber = new RollNumber(theBiggerNumber);
        try {
            restTemplate.postForObject(url, rollNumber, ResponseStatus.class);
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 現在決めているロールを返す。
     * ロールのバランスは人狼1、狩人1、その他全て村人とする。
     * 順番として、
     * 1.人狼.
     * 2.騎士.
     * 3, 4, ... 村人.
     * とする。
     * 
     * @return 現在決めているロール
     */
    public Roll definingRoll(int loop) {
        switch (loop) {
            case 1:
                return Roll.WEREWOLF;
            case 2:
                return Roll.KNIGHT;
            default:
                return Roll.VILLAGER;
        }
    }

    /**
     * @return 先に決めた自分のランダムな値
     */
    public Integer getMyRandomNumber() {
        return stateModel.getMyRandomNumber();
    }

    /**
     * 今回のループで自分のロールを決定
     */
    public void setRollNow() {
        globalStateService.set(definingRoll(stateModel.getLoop()));
        log.info("the defined my own job is:" + globalStateService.getRoll().name());
    }

    /**
     * 最後のループまで役職が振られなかった人に役職を振る。
     */
    public void setRollLast() {
        globalStateService.set(definingRoll(globalStateService.getNumberOfParticipants()));
        log.info("the defined my own job is:" + globalStateService.getRoll().name());
    }

    /**
     * 次の役職を決めるループのために初期化
     */
    public void prepareNextLoop() {
        stateModel.setLoop(stateModel.getLoop() + 1);
    }

    /**
     * @return この役職を決める最後のループであればtrue
     */
    public boolean isFinalLoop() {
        return stateModel.getLoop() >= globalStateService.getNumberOfParticipants() - 1;
    }

    /**
     * @return この役職を決めるループがもう終わっていればtrue
     */
    public boolean isLoopEnd() {
        return stateModel.getLoop() >= globalStateService.getNumberOfParticipants();
    }

    public void rollsHadDefined() {
        shareRollService.init();
    }

}
