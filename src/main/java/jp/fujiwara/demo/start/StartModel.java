package jp.fujiwara.demo.start;

import lombok.Data;

/**
 * 一番最初の設定画面でのデータ入力を受け取るデータを受渡するため
 */
@Data
public class StartModel {
    private String playerName;
    private Boolean isParent;
    /**
     * port番号まで含める
     */
    private String parentIpAddress;
}
