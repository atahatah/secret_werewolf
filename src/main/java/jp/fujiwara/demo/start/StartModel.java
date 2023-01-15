package jp.fujiwara.demo.start;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一番最初の設定画面でのデータ入力を受け取るデータを受渡するため
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartModel {
    private String playerName;
    private Boolean isParent;
    /**
     * port番号まで含める
     */
    private String parentIpAddress;
    /**
     * port番号まで含めない
     */
    private String myIpAddress;
    private String myPortNum;
}
