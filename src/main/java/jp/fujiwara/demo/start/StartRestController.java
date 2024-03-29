package jp.fujiwara.demo.start;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

/**
 * 初期化時の親と子のデータのやり取りを定義
 */
@RequiredArgsConstructor
@RestController
public class StartRestController {
    private final ParentDataService parentDataService;
    private final GlobalStateService globalStateService;

    /**
     * 子から親に対して登録するために呼ぶ
     * 親で実行される
     * 
     * @param childModel 親に登録してもらう子の情報
     * @return 登録のステータス
     */
    @PostMapping("/parent/set_child")
    public ResponseStatus setChild(@RequestBody RowChildDataModel childModel) {
        parentDataService.addChild(childModel);
        return new ResponseStatus();
    }

    /**
     * 親から子に参加者全員の情報を送る
     * 子で実行される
     * 
     * @return 登録のステータス
     */
    @PostMapping("/child/notice_participants_info")
    public ResponseStatus noticeParticipantsInfo(@RequestBody List<ParticipantModel> list) {
        globalStateService.set(list);
        return new ResponseStatus();
    }
}
