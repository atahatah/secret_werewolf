package jp.fujiwara.demo.noon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.evening.EveningService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NoonController {
    private final EveningService eveningService;

    @GetMapping("/noon/finish")
    public String finish() {
        eveningService.init();
        return "redirect:/management";
    }
}
