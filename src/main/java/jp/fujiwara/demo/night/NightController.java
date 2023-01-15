package jp.fujiwara.demo.night;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.noon.NoonService;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NightController {
    private final NoonService noonService;
    private final ParentService parentService;

    @GetMapping("/night/finish")
    public String finish() {
        noonService.init();
        parentService.notifyStateToChildren(GameState.NOON);
        return "redirect:/management";
    }
}
