package jp.fujiwara.demo.night;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.night.model.NightModel;
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
    private final NightService nightService;

    @PostMapping("/night/werewolf")
    public String werewolf(@ModelAttribute NightModel nightModel) {
        log.debug("****NightController.werewolf*****");
        log.debug("werewolf:" + nightModel.getSelectedNumber());
        nightService.werewolfAction(nightModel.getSelectedNumber());
        return "redirect:/management";
    }

    @PostMapping("/night/knight")
    public String knight(@ModelAttribute NightModel nightModel) {
        log.debug("****NightController.werewolf*****");
        log.debug("knight:" + nightModel.getSelectedNumber());
        nightService.knightAction(nightModel.getSelectedNumber());
        return "redirect:/management";
    }

    @PostMapping("/night/others")
    public String others() {
        log.debug("*****NightController.others*****");
        nightService.othersAction();
        return "redirect:/management";
    }

    @GetMapping("/night/finish")
    public String finish() {
        noonService.init();
        parentService.notifyStateToChildren(GameState.NOON);
        return "redirect:/management";
    }
}
