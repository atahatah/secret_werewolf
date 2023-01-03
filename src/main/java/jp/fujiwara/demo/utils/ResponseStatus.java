package jp.fujiwara.demo.utils;

import lombok.Data;

/**
 * REST APIのPOSTにおいて{status:ok}だけ返すためのクラス
 */
@Data
public class ResponseStatus {
    public ResponseStatus() {
        status = "ok";
    }

    private String status;
}
