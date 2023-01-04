package jp.fujiwara.demo.noon;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class NoonRestController {
    private final NoonService noonService;

    /**
     * 親が昼の会議を終了するときにコールする。
     * 全ての参加者にゲームの状態を夕方へ変更するように通知する。
     * 
     * @return ステータス
     */
    @GetMapping("/noon/finish")
    public ResponseStatus finishDiscussion() {
        noonService.finish();
        return new ResponseStatus();
    }
}
