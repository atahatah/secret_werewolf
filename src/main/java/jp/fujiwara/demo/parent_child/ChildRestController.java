package jp.fujiwara.demo.parent_child;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.noon.NoonService;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

/**
 * 子のみが受け取るAPI
 */
@RequiredArgsConstructor
@RestController
public class ChildRestController {
    private final GlobalStateService globalStateService;
    private final Log log;
    private final EveningService eveningService;
    private final NightService nightService;
    private final NoonService noonService;

    @PostMapping("/child/set_state")
    public ResponseStatus setState(@RequestBody GameState state) {
        globalStateService.set(state);
        switch (state) {
            case DEAD:
                log.error("nothing init for " + state.name());
                break;
            case EVENING:
                eveningService.init();
                break;
            case NIGHT:
                nightService.init();
                break;
            case NOON:
                noonService.init();
                break;
            case PEOPLE_WON:
                log.error("nothing init for " + state.name());
                break;
            case ROLL_DEFINITION:
                log.error("nothing init for " + state.name());
                break;
            case START:
                log.error("nothing init for " + state.name());
                break;
            case WEREWOLF_WON:
                log.error("nothing init for " + state.name());
                break;
            default:
                log.error("nothing init for " + state.name());
                break;
        }

        return new ResponseStatus();
    }
}
