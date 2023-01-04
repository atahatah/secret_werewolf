package jp.fujiwara.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * REST APIのPOSTにおいて{status:ok}だけ返すためのクラス
 */
@AllArgsConstructor
@Data
public class ResponseStatus {
    public ResponseStatus() {
        status = "ok";
    }

    private String status;
}
