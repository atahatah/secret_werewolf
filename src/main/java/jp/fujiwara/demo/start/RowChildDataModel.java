package jp.fujiwara.demo.start;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 子から親に登録してもらうために送るデータ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowChildDataModel {
    private String playerName;
    /**
     * port番号まで含める
     */
    private String ip;
}
