package jp.fujiwara.demo.management_screen;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.global.child.ChildDataService;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.noon.NoonService;
import jp.fujiwara.demo.parent_child.ParentService;
import jp.fujiwara.demo.roll_definition.RollDefinitionService;
import jp.fujiwara.demo.start.StartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ManagementController {
    private final StartService startService;
    private final ParentService parentService;
    private final NoonService noonService;
    private final NightService nightService;
    private final EveningService eveningService;
    private final GlobalStateService globalStateService;
    private final ParentDataService parentDataService;
    private final ChildDataService childDataService;
    private final RollDefinitionService rollDefinitionService;

    @GetMapping("/management")
    public String childManagement(Model model) {

        final Roll roll = globalStateService.getRoll();
        if (roll != null) {
            model.addAttribute("hasRoll", true);
            model.addAttribute("roll", roll.name);
        } else {
            model.addAttribute("hasRoll", false);
        }

        if (globalStateService.getIsParent()) {
            model.addAttribute("isParent", true);
        }

        switch (globalStateService.getState()) {
            case NIGHT:
                if (nightService.isActionDecided()) {
                    return "night/waiting";
                }
                switch (globalStateService.getRoll()) {
                    case WEREWOLF:
                        model.addAttribute("participants", globalStateService.getParticipants());
                        model.addAttribute("selectedNumber", 0);
                        return "night/werewolf";
                    case KNIGHT:
                        model.addAttribute("participants", globalStateService.getParticipants());
                        model.addAttribute("selectedNumber", 0);
                        return "night/knight";
                    default:
                        return "night/villager";
                }
            case NOON:
                if (globalStateService.getIsParent()) {
                    return "noon/parent_management";
                } else {
                    return "noon/child_management";
                }
            case EVENING:
                if (eveningService.isResulted()) {
                    model.addAttribute("executed", eveningService.getRecentExecutedId());
                    return "evening/result";
                }
                if (eveningService.isVoted()) {
                    return "evening/voted";
                }
                model.addAttribute("participants", globalStateService.getParticipants());
                model.addAttribute("selectedNumber", 0);
                return "evening/vote";
            default:
                if (globalStateService.getIsParent()) {
                    return "parent_management";
                } else {
                    return "child_management";
                }
        }
    }

    @GetMapping("/init")
    public String init() {
        startService.init();
        parentService.init();
        noonService.init();
        nightService.init();
        eveningService.init();
        globalStateService.init();
        parentDataService.init();
        childDataService.init();
        rollDefinitionService.init();
        return "redirect:/";
    }
}
