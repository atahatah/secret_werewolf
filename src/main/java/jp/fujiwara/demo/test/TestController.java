package jp.fujiwara.demo.test;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.start.StartModel;

@RestController
public class TestController {
    @PostMapping("/start/test")
    public String startTest(@ModelAttribute StartModel start) {
        return "start:" + start.getPlayerName() + start.getIpAddress() + start.getIsParent();
    }
}
