package jp.fujiwara.demo.noon;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

/**
 * 昼。
 * 議論パート。
 * 親がボタンを押すことで終了することができる。
 */
@RequiredArgsConstructor
@Service
public class NoonService {
    private final ParentService parentService;
    private final EveningService eveningService;

    public void init() {
        parentService.notifyStateToChildren(GameState.NIGHT);
    }

    public void finish() {
        eveningService.init();
    }
}
