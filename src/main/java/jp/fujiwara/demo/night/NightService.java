package jp.fujiwara.demo.night;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.global.GlobalStateService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NightService {
    private final GlobalStateService globalStateService;

    public void init() {
        // TODO
    }

}
