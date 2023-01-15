package jp.fujiwara.demo.evening;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EveningController {
    private final NightService nightService;
    private final ParentService parentService;

    @GetMapping("/evening/finish")
    public String finish() {
        nightService.init();
        parentService.notifyStateToChildren(GameState.NIGHT);
        return "redirect:management";
    }
}
