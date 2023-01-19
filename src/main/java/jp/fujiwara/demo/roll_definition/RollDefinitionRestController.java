package jp.fujiwara.demo.roll_definition;

import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
public class RollDefinitionRestController {
    private final RollDefinitionService service;
    private final GlobalStateService globalStateService;
    private final Log log;

    /**
     * 参加者から次の参加者へ数字を送る。
     * 渡された数字と、自分のランダムな数字と比較して大きい方の数字を次の参加者へ送る。
     * ロールの決定の最初のループ。
     * 
     * @param rollNumber これまでの各参加者のランダムな数字の内、最も大きな値
     * @return 処理のステータス
     */
    @PostMapping("/roll/comp_num")
    public ResponseStatus compareTowNumbers(@RequestBody RollNumber rollNumber) {
        final ParticipantModel previous = globalStateService.getPreviousParticipant();
        log.debug("*****RollDefinitionRestController.compareTwoNumbers*****");
        log.debug(String.format("get number: %d from %s(%d)",
                rollNumber.getNumber(),
                previous.getPlayerName(),
                previous.getNumber()));

        // 自分のランダムな数字と渡されたこれまでの最大値を取得
        int myRandomNumber = service.sampleRollNumber();
        log.debug(String.format("my number is: %d", myRandomNumber));
        final int othersRandomNumber = rollNumber.getNumber();
        if (myRandomNumber == othersRandomNumber) {
            // 自分のランダムな数字と渡されたランダムな数字が同じ場合は自分の方を1増やす
            log.debug("two numbers equal, so increment my random number.");
            myRandomNumber++;
        }
        service.setMyRandomNumber(myRandomNumber);

        // より大きい方を決定
        final int theBiggerNumber = (myRandomNumber > othersRandomNumber) ? myRandomNumber : othersRandomNumber;

        final ParticipantModel next = globalStateService.getNextParticipant();
        log.debug(String.format("send the number %d to %s(%d)",
                theBiggerNumber,
                next.getPlayerName(),
                next.getNumber()));
        service.sendRandomNumberToNext(theBiggerNumber, next.getIsParent() ? "/roll/check_num" : "/roll/comp_num");

        return new ResponseStatus();
    }

    /**
     * @param rollNumber
     * @return
     */
    @PostMapping("/roll/check_num")
    public ResponseStatus checkNumber(@RequestBody RollNumber rollNumber) {
        log.debug("*****RollDefinitionService.checkNumber*****");

        int maxRandomNumber = rollNumber.getNumber();

        final ParticipantModel previous = globalStateService.getPreviousParticipant();
        log.debug(String.format("get number: %d from %s(%d)",
                rollNumber.getNumber(),
                previous.getPlayerName(),
                previous.getNumber()));

        if (globalStateService.getIsParent()) {
            if (service.getMyRandomNumber() > maxRandomNumber) {
                service.setRollNow();
                maxRandomNumber = service.getMyRandomNumber();
            }
        } else {
            if (service.getMyRandomNumber() == maxRandomNumber) {
                service.setRollNow();
            }
        }

        // 最後の週まで役職が振られなかった人に役職を割り当てる
        if (service.isFinalLoop() && !service.hasRollDefined()) {
            service.setRollLast();
        }

        // データを送るより先にしておかないとループ番号をインクリメントする前に次のデータが来てしまう
        log.debug("prepare for the next loop");
        service.prepareNextLoop();

        final ParticipantModel next = globalStateService.getNextParticipant();
        log.debug(String.format("send the number %d to %s(%d)",
                maxRandomNumber,
                next.getPlayerName(),
                next.getNumber()));
        service.sendRandomNumberToNext(maxRandomNumber, next.getIsParent() ? "/roll/next" : "/roll/check_num");
        return new ResponseStatus();
    }

    @PostMapping("/roll/next")
    public ResponseStatus nextLoop(@RequestBody RollNumber rollNumber) {
        log.debug("*****RollDefinitionRestController.nextLoop*****");
        if (service.isLoopEnd()) {
            log.debug("the end of the roll definition");
            service.rollsHadDefined();
            return new ResponseStatus();
        }

        service.initRollDefinition();
        return new ResponseStatus();
    }
}
