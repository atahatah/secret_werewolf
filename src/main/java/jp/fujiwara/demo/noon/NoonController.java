package jp.fujiwara.demo.noon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NoonController {
    private final EveningService eveningService;
    private final ParentService parentService;

    @GetMapping("/noon/finish")
    public String finish() {
        eveningService.init();
        parentService.notifyStateToChildren(GameState.EVENING);
        return "redirect:/management";
    }
}
