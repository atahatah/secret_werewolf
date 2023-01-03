package jp.fujiwara.demo.start;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.utils.ResponseStatus;

/**
 * 初期化時の親と子のデータのやり取りを定義
 */
@RestController
public class StartRestController {
    @Autowired
    ParentDataService parentDataService;

    @Autowired
    GlobalStateService globalStateService;

    /**
     * 子から親に対して登録するために呼ぶ
     * 親で実行される
     * 
     * @param childModel 親に登録してもらう子の情報
     * @return 登録のステータス
     */
    @PostMapping("/parent/set_child")
    public ResponseStatus setChild(@ModelAttribute RowChildDataModel childModel) {
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
    public ResponseStatus noticeParticipantsInfo(@ModelAttribute List<ParticipantModel> list) {
        globalStateService.set(list);
        return new ResponseStatus();
    }
}
