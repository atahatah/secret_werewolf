package jp.fujiwara.demo.evening;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.parent_child.ParentService;
import jp.fujiwara.demo.utils.Log;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EveningController {
    private final NightService nightService;
    private final ParentService parentService;
    private final EveningService eveningService;
    private final Log log;

    @PostMapping("/evening/vote")
    public String vote(@ModelAttribute VoteModel voteModel) {
        log.debug("*****EveningController.vote*****");
        eveningService.vote(voteModel.getSelectedNumber());
        return "redirect:/management";
    }

    @GetMapping("/evening/finish")
    public String finish() {
        nightService.init();
        parentService.notifyStateToChildren(GameState.NIGHT);
        return "redirect:management";
    }
}
