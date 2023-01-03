package jp.fujiwara.demo.utils;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class GetPortNum {
    // TODO ポート番号を変更できるようにする
    @Getter
    // @Value("${server.port}")
    private String portNum = "8080";
}
