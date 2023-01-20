package jp.fujiwara.demo.night;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.noon.NoonService;
import jp.fujiwara.demo.parent_child.ParentService;
import jp.fujiwara.demo.utils.Log;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NightController {
    private final NoonService noonService;
    private final ParentService parentService;
    private final Log log;

    @PostMapping("/night/werewolf")
    public String werewolf(@ModelAttribute NightModel nightModel) {
        log.debug("****NightController.werewolf*****");
        // TODO
        log.debug("werewolf:" + nightModel.selectedNumber);
        return "redirect:/management";
    }

    @PostMapping("/night/knight")
    public String knight(@ModelAttribute NightModel nightModel) {
        log.debug("****NightController.werewolf*****");
        // TODO
        log.debug("knight:" + nightModel.selectedNumber);
        return "redirect:/management";
    }

    @GetMapping("/night/finish")
    public String finish() {
        noonService.init();
        parentService.notifyStateToChildren(GameState.NOON);
        return "redirect:/management";
    }
}
