package jp.fujiwara.demo.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.start.StartModel;

@RestController
public class TestController {
    @PostMapping("/start/test")
    public String startTest(@RequestBody StartModel start) {
        return "start:" + start.getPlayerName() + start.getParentIpAddress() + start.getIsParent();
    }
}
