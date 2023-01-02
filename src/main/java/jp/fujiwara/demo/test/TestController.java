package jp.fujiwara.demo.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/start/test")
    public String startTest(@RequestParam String message) {
        return "start:" + message;
    }
}
