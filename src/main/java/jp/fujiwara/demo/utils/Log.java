package jp.fujiwara.demo.utils;

import org.springframework.stereotype.Component;

@Component
public class Log {
    public void debug(String log) {
        System.out.println(log);
    }

    public void error(String log) {
        System.out.println(log);
    }

    public void info(String log) {
        System.out.println(log);
    }
}
