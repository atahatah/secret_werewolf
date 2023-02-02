package jp.fujiwara.demo.management_screen;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
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
    private final ManagementService managementService;
    private final ParentDataService parentDataService;
    private final ChildDataService childDataService;
    private final RollDefinitionService rollDefinitionService;

    @GetMapping("/management") // webのパス指定
    public String childManagement(Model model) {
        final List<ParticipantModel> livingOtherPlayers = new ArrayList<>();
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                continue;
            }
            if (player.isKilled()) {
                continue;
            }
            livingOtherPlayers.add(player);
        }
        model.addAttribute("participants", livingOtherPlayers);

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
                        model.addAttribute("killShares", nightService.getKillShares());
                        model.addAttribute("killShareSum", nightService.getKillShareSum());
                        model.addAttribute("receiverName", nightService.getReceiverName());
                        model.addAttribute("generatedShares", nightService.getGeneratedShares());

                        return "night/killed";
                    } else {
                        model.addAttribute("killShares", nightService.getKillShares());
                        model.addAttribute("killShareSum", nightService.getKillShareSum());
                        model.addAttribute("receiverName", nightService.getReceiverName());
                        model.addAttribute("generatedShares", nightService.getGeneratedShares());
                        return "night/alive";
                    }
                }
                if (nightService.isActionDecided()) {
                    return "night/waiting";
                }
                switch (globalStateService.getRoll()) {
                    case WEREWOLF:
                        model.addAttribute("selectedNumber", 0);
                        return "night/werewolf";
                    case KNIGHT:
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
                    model.addAttribute("receiveShares", eveningService.getReceiveShares());
                    model.addAttribute("shereSum", eveningService.getShareSum());
                    model.addAttribute("receiverNames", eveningService.getReceiverNames());
                    model.addAttribute("generatedShares", eveningService.getGeneratedShares());
                    return "evening/result";
                }
                if (eveningService.isVoted()) {
                    model.addAttribute("receiveShares", eveningService.getReceiveShares());
                    model.addAttribute("shereSum", eveningService.getShareSum());
                    // display shares i made and receivers
                    return "evening/voted";
                }
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
