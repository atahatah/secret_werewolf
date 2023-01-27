package jp.fujiwara.demo.management_screen;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.parent_child.ParentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ManagementController {
    private final ParentService parentService;
    private final NightService nightService;
    private final EveningService eveningService;
    private final GlobalStateService globalStateService;
    private final ManagementService managementService;

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
                if (nightService.isResulted()) {
                    if (nightService.isSomebodyKilled()) {
                        model.addAttribute("killedId", nightService.getKilledId());
                        model.addAttribute("killedName",
                                globalStateService.getParticipants().get(nightService.getKilledId()).getPlayerName());
                        model.addAttribute("killedRoll", nightService.getKilledRoll().name);
                        return "night/killed";
                    } else {
                        return "night/alive";
                    }
                }
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
                    model.addAttribute("executedName", globalStateService.getParticipants()
                            .get(eveningService.getRecentExecutedId()).getPlayerName());
                    model.addAttribute("executedRoll", eveningService.getRecentExecutedRoll().name);
                    return "evening/result";
                }
                if (eveningService.isVoted()) {
                    return "evening/voted";
                }
                model.addAttribute("participants", globalStateService.getParticipants());
                model.addAttribute("selectedNumber", 0);
                return "evening/vote";
            case PEOPLE_WON:
                return "people_won";
            case WEREWOLF_WON:
                return "werewolf_won";
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
        managementService.initAll();
        if (globalStateService.getIsParent()) {
            parentService.notifyStateToChildren(GameState.START);
        }
        return "redirect:/";
    }
}
