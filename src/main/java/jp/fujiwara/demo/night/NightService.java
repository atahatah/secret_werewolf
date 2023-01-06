package jp.fujiwara.demo.night;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.noon.NoonService;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NightService {
    private final ParentService parentService;

    public void init() {
        parentService.notifyStateToChildren(GameState.NIGHT);
    }
}
