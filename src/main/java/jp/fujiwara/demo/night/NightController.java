package jp.fujiwara.demo.night;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.fujiwara.demo.noon.NoonService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NightController {
    private final NoonService noonService;

    @GetMapping("/night/finish")
    public String finish() {
        noonService.init();
        return "redirect:/management";
    }
}
