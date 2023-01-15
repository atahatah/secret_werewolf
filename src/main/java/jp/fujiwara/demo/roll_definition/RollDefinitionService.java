package jp.fujiwara.demo.roll_definition;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.math.RandomNum;
import jp.fujiwara.demo.night.NightService;
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
    private final NightService nightService;
    private final RestTemplate restTemplate;

    public void init() {
        stateModel.setLoop(1);
        stateModel.setIsIncremented(false);
    }

    /**
     * 既にロールが決定しているか
     * 
     * @return ロールが決定していればtrue
     */
    public boolean hasRollDefined() {
        return globalStateService.getRoll() == null;
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
        return randomNum.next(MAX_RANDOM_NUM);
    }

    /**
     * 親が1つのロールを決定するサイクルを開始するときに初期化する処理
     */
    public void initRollDefinition() {
        final int myRandomNumber = sampleRollNumber();
        setMyRandomNumber(myRandomNumber);
        final int sendingRandomNumber = randomNum.next(myRandomNumber);
        sendRandomNumberToNext(sendingRandomNumber);
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
     * ただし、親の場合は送る先のURLを変える。
     * 
     * @param theBiggerNumber これまでで最も大きなランダムな数字
     */
    public void sendRandomNumberToNext(int theBiggerNumber) {
        final ParticipantModel next = globalStateService.getNextParticipant();
        final String nextIp = next.getIpAddress();

        if (next.getIsParent()) {
            // 次の参加者（親）に大きい方の数字を送る
            final String url = "http://" + nextIp + "/roll/check_num";
            final RollNumber rollNumber = new RollNumber(theBiggerNumber);
            restTemplate.postForObject(url, rollNumber, ResponseStatus.class);
        } else {
            // 次の参加者（子）に大きい方の数字を送る
            final String url = "http://" + nextIp + "/roll/comp_num";
            final RollNumber rollNumber = new RollNumber(theBiggerNumber);
            restTemplate.postForObject(url, rollNumber, ResponseStatus.class);
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
    public Roll definingRoll() {
        switch (stateModel.getLoop()) {
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
        globalStateService.set(definingRoll());
        System.out.println("役職：" + globalStateService.getRoll().name());
    }

    /**
     * @return この役職を決めるループで自分がインクリメントしたか
     */
    public boolean getIsIncremented() {
        return stateModel.getIsIncremented();
    }

    /**
     * この役職を決めるループで自分がインクリメントしたことを記録
     */
    public void setIncremented() {
        stateModel.setIsIncremented(true);
    }

    /**
     * 次の役職を決めるループのために初期化
     */
    public void prepareNextLoop() {
        stateModel.setIsIncremented(false);
        stateModel.setLoop(stateModel.getLoop() + 1);
    }

    /**
     * @return この役職を決めるループが最後であればtrue
     */
    public boolean isFinalLoop() {
        return stateModel.getLoop() >= globalStateService.getNumberOfParticipants();
    }

    /**
     * 役職を決める処理の最後。
     * 親が実行する。
     */
    public void rollsHadDefined() {
        nightService.init();
    }

}