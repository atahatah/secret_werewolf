package jp.fujiwara.demo.start;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {
    @RequestMapping("/start")
    public String start(@ModelAttribute("model") StartModel startModel) {
        return "start";
    }
}
