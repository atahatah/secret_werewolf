package jp.fujiwara.demo.parent_child;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GameState;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

/**
 * 子のみが受け取るAPI
 */
@RequiredArgsConstructor
@RestController
public class ChildRestController {
    private final GlobalStateService globalStateService;

    @PostMapping("/child/set_state")
    public ResponseStatus setState(@RequestBody GameState state) {
        globalStateService.set(state);

        return new ResponseStatus();
    }
}
