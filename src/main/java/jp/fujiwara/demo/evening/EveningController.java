package jp.fujiwara.demo.evening;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.night.NightService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EveningController {
    private final NightService nightService;

    @GetMapping("/evening/finish")
    public String finish() {
        nightService.init();
        return "redirect:management";
    }
}
