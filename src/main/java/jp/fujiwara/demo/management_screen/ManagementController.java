package jp.fujiwara.demo.management_screen;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagementController {
    @GetMapping("/management/child")
    public String childManagement() {
        return "child_management";
    }

    @GetMapping("/management/parent")
    public String parentManagement() {
        return "parent_management";
    }

}
