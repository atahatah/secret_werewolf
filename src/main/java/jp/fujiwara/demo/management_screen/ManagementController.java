package jp.fujiwara.demo.management_screen;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.global.GlobalStateService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ManagementController {
    private final GlobalStateService globalStateService;

    @GetMapping("/management")
    public String childManagement() {
        switch (globalStateService.getState()) {
            case NIGHT:
                switch (globalStateService.getRoll()) {
                    case WEREWOLF:
                        return "night/werewolf";
                    case KNIGHT:
                        return "night/knight";
                    default:
                        return "night/villager";
                }
            default:
                if (globalStateService.getIsParent()) {
                    return "parent_management";
                } else {
                    return "child_management";
                }
        }
    }
}
