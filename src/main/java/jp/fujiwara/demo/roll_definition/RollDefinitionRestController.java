package jp.fujiwara.demo.roll_definition;

import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
public class RollDefinitionRestController {
    private final RollDefinitionService service;
    private final GlobalStateService globalStateService;

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
        // 自分のランダムな数字と渡されたこれまでの最大値を取得
        int myRandomNumber = service.sampleRollNumber();
        final int othersRandomNumber = rollNumber.getNumber();
        if (myRandomNumber == othersRandomNumber) {
            // 自分のランダムな数字と渡されたランダムな数字が同じ場合は自分の方を1増やす
            myRandomNumber++;
            service.setIncremented();
        }
        service.setMyRandomNumber(myRandomNumber);

        // より大きい方を決定
        final int theBiggerNumber = (myRandomNumber > othersRandomNumber) ? myRandomNumber : othersRandomNumber;

        service.sendRandomNumberToNext(theBiggerNumber);

        return new ResponseStatus();
    }

    /**
     * @param rollNumber
     * @return
     */
    @PostMapping("/roll/check_num")
    public ResponseStatus checkNumber(@RequestBody RollNumber rollNumber) {
        int maxRandomNumber = rollNumber.getNumber();
        if (service.getIsIncremented()) {
            maxRandomNumber++;
        }

        if (globalStateService.getIsParent()) {
            if (service.getMyRandomNumber() > maxRandomNumber) {
                service.setRollNow();
                maxRandomNumber = service.getMyRandomNumber();
            }
            if (service.isFinalLoop()) {
                service.rollsHadDefined();
                return new ResponseStatus();
            }
        } else {
            if (service.getMyRandomNumber() == maxRandomNumber) {
                service.setRollNow();
            }
        }
        service.sendRandomNumberToNext(maxRandomNumber);
        service.prepareNextLoop();
        return new ResponseStatus();
    }
}
