package jp.fujiwara.demo.evening;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EveningService {
    private final ParentService parentService;
    private final NightService nightService;

    public void init() {
        parentService.notifyStateToChildren(GameState.EVENING);
    }

    public void finish() {
        nightService.init();
    }
}
